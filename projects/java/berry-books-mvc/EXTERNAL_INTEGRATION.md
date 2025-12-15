# 外部システム連携 - REST API統合

## 📋 概要

`berry-books-mvc` プロジェクトは、顧客情報の管理において **berry-books-rest** の REST API を外部システムとして呼び出します。

### アーキテクチャ変更

#### 変更前（直接データベースアクセス）
```
CustomerBean/LoginBean (JSF)
    ↓
CustomerService
    ↓
CustomerDao (EntityManager)
    ↓
Database
```

#### 変更後（REST API経由）
```
CustomerBean/LoginBean (JSF)
    ↓
CustomerService
    ↓
CustomerRestClient (JAX-RS Client)
    ↓ HTTP
berry-books-rest API
    ↓
Database
```

## 🔧 実装内容

### 1. 新規作成ファイル

#### `pro.kensait.berrybooks.client.CustomerRestClient`
- Jakarta REST Client API を使用した REST クライアント
- `berry-books-rest` の Customer API を呼び出す
- 以下のメソッドを実装：
  - `findByEmail(String email)` - GET /customers/query_email
  - `findById(Integer customerId)` - GET /customers/{customerId}
  - `register(Customer customer)` - POST /customers/

#### `pro.kensait.berrybooks.client.dto.CustomerTO`
- REST API のレスポンス/リクエスト用データ転送オブジェクト
- `berry-books-rest` の CustomerTO と同一構造

#### `pro.kensait.berrybooks.client.dto.ErrorResponse`
- REST API のエラーレスポンス用オブジェクト

### 2. 修正ファイル

#### `CustomerService`
- **変更前**: `CustomerDao` を注入してデータベースに直接アクセス
- **変更後**: `CustomerRestClient` を注入して REST API 経由でアクセス
- 全メソッドが REST API 呼び出しに変更：
  - `registerCustomer()` - REST API 経由で顧客登録
  - `authenticate()` - REST API でメール検索 → パスワード検証（berry-books-mvc 側）
  - `getCustomer()` - REST API 経由で顧客取得

#### `config.properties`
```properties
# REST API設定（berry-books-rest APIのベースURL）
customer.api.base-url = http://localhost:8080/berry-books-rest/customers
```

### 3. berry-books-rest の修正

#### `CustomerTO` にパスワードフィールドを追加
- 認証機能を REST API 経由で実現するため、パスワードフィールドを追加
- `CustomerResource` の変換メソッドを修正

## 🚀 セットアップ手順

### 前提条件

1. **berry-books-rest が起動していること**
   ```bash
   # プロジェクトルートで
   ./gradlew :projects:java:berry-books-rest:war
   ./gradlew :projects:java:berry-books-rest:deploy
   ```

2. **berry-books-rest が以下の URL でアクセス可能であること**
   ```
   http://localhost:8080/berry-books-rest/customers
   ```

### berry-books-mvc の起動

```bash
# プロジェクトルートで
./gradlew :projects:java:berry-books-mvc:war
./gradlew :projects:java:berry-books-mvc:deploy
```

## 🔍 動作確認

### 1. 顧客登録のテスト

1. `berry-books-mvc` にアクセス: http://localhost:8080/berry-books-mvc
2. 新規顧客登録を実行
3. バックグラウンドで `berry-books-rest` の REST API が呼び出される

### 2. ログインのテスト

1. ログイン画面でメールアドレスとパスワードを入力
2. `berry-books-rest` の `/customers/query_email` が呼び出される
3. 取得した顧客情報を使って `berry-books-mvc` 側でパスワード検証

### 3. ログで確認

```bash
# Payara Server のログを監視
tail -f payara6/glassfish/domains/domain1/logs/server.log
```

以下のようなログが出力されます：

```
[ CustomerService#registerCustomer ] - REST API呼び出し
[ CustomerRestClient#register ] customer=...
[ CustomerService#authenticate ] email=test@example.com - REST API呼び出し
[ CustomerRestClient#findByEmail ] email=test@example.com
```

## 📊 REST API エンドポイント

`berry-books-mvc` が呼び出す `berry-books-rest` の API：

| メソッド | エンドポイント | 説明 | 使用箇所 |
|---------|---------------|------|---------|
| GET | `/customers/{id}` | 顧客をIDで取得 | `getCustomer()` |
| GET | `/customers/query_email?email={email}` | 顧客をメールで検索 | `authenticate()` |
| POST | `/customers/` | 顧客を新規登録 | `registerCustomer()` |

## ⚠️ 注意事項

### 1. REST API の起動順序

`berry-books-rest` を先に起動してから、`berry-books-mvc` を起動してください。

### 2. 認証処理について

- 認証は REST API の `query_email` で顧客情報を取得
- パスワード検証は `berry-books-mvc` 側で実行（平文比較）
- より安全な実装には、`berry-books-rest` に認証専用エンドポイント（POST /customers/authenticate）の追加を推奨

### 3. エラーハンドリング

REST API が利用できない場合：
- `CustomerRestClient` は例外をスローまたは null を返す
- ユーザーにはエラーメッセージが表示される

### 4. タイムアウト設定

デフォルトの JAX-RS Client タイムアウトが適用されます。必要に応じて `CustomerRestClient#init()` でタイムアウトを設定できます。

## 🧪 テスト

### ユニットテスト

`CustomerServiceTest` は `CustomerRestClient` をモック化してテストを実行します：

```bash
./gradlew :projects:java:berry-books-mvc:test --tests "*CustomerServiceTest"
```

### 結合テスト（手動）

1. `berry-books-rest` を起動
2. `berry-books-mvc` を起動
3. ブラウザで `berry-books-mvc` にアクセスして実際の操作を実行

## 🔒 セキュリティ考慮事項

### 現在の実装

- パスワードは平文で REST API を経由して送受信
- HTTPS を使用していない

### 本番環境への推奨事項

1. **HTTPS の使用**
   ```properties
   customer.api.base-url = https://api.example.com/customers
   ```

2. **パスワードハッシュ化**
   - データベースにハッシュ化されたパスワードを保存
   - 認証時はハッシュ値を比較

3. **認証トークン**
   - JWT などのトークンベース認証の導入
   - API キーによるアクセス制御

4. **認証専用エンドポイント**
   - POST /customers/authenticate の実装
   - パスワード検証を REST API 側で実行

## 📚 関連ドキュメント

- `berry-books-rest/README.md` - REST API の仕様
- `berry-books-rest/spec/openapi.yaml` - OpenAPI 仕様書
- `berry-books-mvc/README.md` - アプリケーションの概要

## 🔄 今後の拡張案

1. **キャッシュの導入**
   - 頻繁にアクセスされる顧客情報をキャッシュ
   - Redis などの分散キャッシュの利用

2. **リトライ機能**
   - REST API 呼び出し失敗時の自動リトライ
   - Circuit Breaker パターンの導入

3. **非同期処理**
   - CompletableFuture を使った非同期 REST 呼び出し
   - レスポンスタイムの改善

4. **監視とログ**
   - REST API 呼び出しの成功/失敗の記録
   - レスポンスタイムのモニタリング
