# berry-books - アーキテクチャ設計書

**プロジェクトID:** berry-books  
**バージョン:** 1.3.4  
**最終更新日:** 2025-12-21  
**ステータス:** アーキテクチャ設計完了（セッション管理・スコープ設計・複合主キーマッピング・画面遷移・UI/UX改善）

---

## 1. 技術スタック

### 1.1 コアプラットフォーム

| レイヤー | 技術 | バージョン | 選定理由 |
|-------|-----------|---------|-----------|
| **ランタイム** | JDK | 21 | LTSバージョン、最新のJava機能を使用可能 |
| **プラットフォーム** | Jakarta EE | 10.0 | Javaエンタープライズアプリケーションの標準 |
| **アプリサーバー** | Payara Server | 6.x | Jakarta EE 10完全準拠、本番環境対応 |
| **データベース** | HSQLDB | 2.7.x | 学習・開発用の組み込みデータベース |
| **ビルドツール** | Gradle | 8.x | マルチプロジェクト対応の最新ビルドツール |

### 1.2 Jakarta EE仕様

```mermaid
graph TD
    A[Jakarta EE 10 Platform] --> B[Jakarta Faces 4.0]
    A --> C[Jakarta Persistence 3.1]
    A --> D[Jakarta Transactions 2.0]
    A --> E[Jakarta CDI 4.0]
    A --> F[Jakarta Bean Validation 3.0]
    A --> G[Jakarta Servlet 6.0]
    
    B --> H[View Layer: Facelets XHTML]
    C --> I[Persistence Layer: EclipseLink]
    D --> J[Transaction Management: JTA]
    E --> K[Dependency Injection]
    F --> L[Input Validation]
    G --> M[HTTP Request Handling]
```

| 仕様 | バージョン | 目的 |
|--------------|---------|---------|
| Jakarta Faces (JSF) | 4.0 | MVC Webフレームワーク |
| Jakarta Persistence (JPA) | 3.1 | オブジェクト関係マッピング |
| Jakarta Transactions (JTA) | 2.0 | 分散トランザクション管理 |
| Jakarta CDI | 4.0 | 依存性注入とコンテキスト管理 |
| Jakarta Bean Validation | 3.0 | 入力検証フレームワーク |
| Jakarta Servlet | 6.0 | HTTPリクエスト/レスポンス処理 |

### 1.3 追加ライブラリ

| ライブラリ | 目的 | 選定理由 |
|---------|---------|-----------|
| SLF4J + Log4j2 | ログ出力 | 業界標準、高性能な非同期ロギング |
| JUnit 5 | ユニットテスト | 最新のテストフレームワーク |
| Mockito | モッキング | ユニットテストの独立性確保 |
| Playwright | E2Eテスト | クロスブラウザ対応、画面遷移テスト自動化 |
| Jakarta REST Client | REST API呼び出し | berry-books-rest APIとの連携用 |

### 1.4 外部API連携

| 連携先 | プロトコル | 目的 |
|--------|----------|------|
| berry-books-rest | REST API (JAX-RS) | CUSTOMERテーブルへのアクセス |

---

## 2. アーキテクチャ設計

### 2.1 レイヤードアーキテクチャ

```mermaid
graph TB
    subgraph "Presentation Layer"
        View[JSF Facelets<br/>XHTML + CSS]
        Controller[Managed Beans<br/>@Named @ViewScoped<br/>@SessionScoped]
    end
    
    subgraph "Business Logic Layer"
        Service[Service Classes<br/>@ApplicationScoped<br/>@Transactional]
    end
    
    subgraph "Data Access Layer"
        DAO[DAO Classes<br/>@ApplicationScoped<br/>EntityManager]
    end
    
    subgraph "Persistence Layer"
        Entity[JPA Entities<br/>@Entity<br/>Relationships]
    end
    
    subgraph "Database Layer"
        DB[(HSQLDB<br/>testdb)]
    end
    
    subgraph "External API Layer"
        RestAPI[berry-books-rest<br/>REST API]
    end
    
    View -->|User Input| Controller
    Controller -->|@Inject| Service
    Service -->|@Inject| DAO
    Service -->|REST Client| RestAPI
    DAO -->|@PersistenceContext| Entity
    Entity -->|JDBC| DB
    RestAPI -->|JDBC| DB
    
    Controller -->|Display Data| View
```

### 2.2 コンポーネントの責務

| レイヤー | 責務 | 禁止事項 |
|-------|-----------------|-------------------|
| **View (XHTML)** | • UIレンダリング<br/>• ユーザー入力の収集<br/>• 表示フォーマット | • ビジネスロジック<br/>• 直接データベースアクセス<br/>• 複雑な計算 |
| **Controller (Managed Bean)** | • リクエスト処理<br/>• ナビゲーション制御<br/>• 入力検証の表示<br/>• サービス委譲 | • 直接データベースアクセス<br/>• ビジネスルール実装<br/>• トランザクション管理 |
| **Service** | • ビジネスロジック<br/>• トランザクション境界<br/>• 複数DAOの連携<br/>• ビジネス検証<br/>• REST API呼び出し（顧客管理） | • UI固有ロジック<br/>• 直接SQLクエリ<br/>• HTTPリクエスト処理 |
| **DAO** | • CRUD操作<br/>• クエリ実行<br/>• エンティティライフサイクル管理 | • ビジネスロジック<br/>• トランザクション制御<br/>• UI関連処理 |
| **REST Client** | • REST API呼び出し<br/>• リクエスト/レスポンス変換<br/>• エラーハンドリング | • ビジネスロジック<br/>• 直接データベースアクセス |
| **Entity** | • データ構造<br/>• リレーションシップ<br/>• データベースマッピング | • ビジネスロジック<br/>• 検証ロジック（Bean Validationを使用） |

---

## 3. デザインパターン

### 3.1 適用パターン

```mermaid
classDiagram
    class MVCPattern {
        <<pattern>>
        +Model: Entity + Service
        +View: XHTML
        +Controller: Managed Bean
    }
    
    class ServiceLayerPattern {
        <<pattern>>
        +Business logic encapsulation
        +Transaction boundaries
        +DAO coordination
    }
    
    class RepositoryPattern {
        <<pattern>>
        +Data access abstraction
        +Query encapsulation
        +Entity management
    }
    
    class DTOPattern {
        <<pattern>>
        +Data transfer objects
        +Layer decoupling
        +Serialization
    }
    
    class SessionFacadePattern {
        <<pattern>>
        +Session state management
        +Cart management
        +User session
    }
    
    MVCPattern --> ServiceLayerPattern
    ServiceLayerPattern --> RepositoryPattern
    RepositoryPattern --> DTOPattern
    MVCPattern --> SessionFacadePattern
```

| パターン | 実装 | メリット |
|---------|---------------|---------|
| **MVC** | JSF + Managed Bean + Service | 関心事の分離 |
| **サービスレイヤー** | @ApplicationScoped Service クラス | ビジネスロジックの集約 |
| **リポジトリ (DAO)** | EntityManager を使用した DAO クラス | データアクセスの抽象化 |
| **DTO/転送オブジェクト** | OrderTO, OrderHistoryTO | レイヤー間の疎結合 |
| **セッションファサード** | @SessionScoped beans | セッション状態管理 |
| **依存性注入** | @Inject (CDI) | 疎結合化 |
| **楽観的ロック** | @Version (JPA) | 並行制御 |
| **トランザクションスクリプト** | @Transactional メソッド | トランザクション管理 |

---

## 4. パッケージ構造と命名規則

### 4.1 パッケージ編成

```
pro.kensait.berrybooks/
├── common/                      # 共通ユーティリティと定数
│   ├── MessageUtil              # メッセージリソースユーティリティ
│   └── SettlementType          # 決済方法列挙型
│
├── util/                        # 汎用ユーティリティ
│   └── AddressUtil             # 住所処理ユーティリティ
│
├── web/                         # プレゼンテーション層（JSF Managed Beans）
│   ├── book/
│   │   ├── BookSearchBean      # 書籍検索コントローラー
│   │   └── SearchParam         # 検索パラメータホルダー
│   ├── cart/
│   │   ├── CartBean            # ショッピングカートコントローラー
│   │   ├── CartItem            # カートアイテムDTO
│   │   └── CartSession         # カートセッションファサード
│   ├── order/
│   │   └── OrderBean           # 注文処理コントローラー
│   ├── customer/
│   │   └── CustomerBean        # 顧客管理コントローラー
│   ├── login/
│   │   └── LoginBean           # ログインコントローラー
│   └── filter/
│       └── AuthenticationFilter # 認証フィルター
│
├── service/                     # ビジネスロジック層
│   ├── book/
│   │   └── BookService         # 書籍ビジネスロジック
│   ├── category/
│   │   └── CategoryService     # カテゴリ管理
│   ├── customer/
│   │   ├── CustomerService     # 顧客管理（REST API経由）
│   │   └── EmailAlreadyExistsException
│   ├── delivery/
│   │   └── DeliveryFeeService  # 配送料金計算
│   └── order/
│       ├── OrderService        # 注文処理
│       ├── OrderServiceIF      # 注文サービスインターフェース
│       ├── OrderTO             # 注文転送オブジェクト
│       ├── OrderHistoryTO      # 注文履歴DTO
│       ├── OrderSummaryTO      # 注文サマリーDTO
│       └── OutOfStockException # 在庫切れ例外
│
├── dao/                         # データアクセス層
│   ├── BookDao                 # 書籍データアクセス
│   ├── CategoryDao             # カテゴリデータアクセス
│   ├── StockDao                # 在庫データアクセス
│   ├── OrderTranDao            # 注文トランザクションデータアクセス
│   └── OrderDetailDao          # 注文明細データアクセス
│
├── client/                      # 外部APIクライアント層
│   └── customer/
│       └── CustomerRestClient   # 顧客管理REST APIクライアント
│
└── entity/                      # 永続化層（JPAエンティティ）
    ├── Book                    # 書籍エンティティ
    ├── Category                # カテゴリエンティティ
    ├── Publisher               # 出版社エンティティ
    ├── Stock                   # 在庫エンティティ（@Version付き）
    ├── Customer                # 顧客エンティティ
    ├── OrderTran               # 注文トランザクションエンティティ
    ├── OrderDetail             # 注文明細エンティティ
    └── OrderDetailPK           # 注文明細複合キー
```

### 4.2 命名規則

| コンポーネントタイプ | パターン | 例 |
|---------------|---------|---------|
| Entity | PascalCase 名詞 | `Book`, `OrderTran` |
| DAO | EntityName + Dao | `BookDao`, `StockDao` |
| Service | EntityName + Service | `BookService`, `OrderService` |
| Managed Bean | FeatureName + Bean | `BookSearchBean`, `CartBean` |
| DTO/TO | Purpose + TO | `OrderTO`, `OrderHistoryTO` |
| Exception | ErrorType + Exception | `OutOfStockException` |
| Enum | PascalCase | `SettlementType` |
| Utility | FeatureName + Util | `MessageUtil`, `AddressUtil` |

---

## 5. 状態管理（CDIスコープ）

### 5.1 CDIスコープ

```mermaid
graph LR
    A[@RequestScoped] -->|Single Request| B[Input validation<br/>Simple queries]
    C[@ViewScoped] -->|Single Page<br/>Multiple Ajax| D[Order input<br/>Simple forms]
    E[@SessionScoped] -->|User Session| F[Login state<br/>Shopping cart<br/>Search results]
    G[@ApplicationScoped] -->|Application Lifetime| H[Services<br/>DAOs<br/>Stateless beans]
```

| スコープ | ライフサイクル | 使用ケース | Serializable実装必須 |
|-------|-----------|-----------|----------------------------|
| @RequestScoped | 単一HTTPリクエスト | 入力フォーム、単純なクエリ | いいえ |
| @ViewScoped | 単一ページビュー（Ajax対応） | 注文入力フォーム、単純なフォーム | **はい** |
| @SessionScoped | ユーザーセッション | ログイン状態、ショッピングカート、**書籍検索結果** | **はい** |
| @ApplicationScoped | アプリケーション起動〜終了 | Services、DAOs、ユーティリティ | いいえ |

**重要な設計変更:**
- `BookSearchBean`は`@SessionScoped`を使用（`@ViewScoped`ではない）
- 理由: `faces-redirect=true`でリダイレクトする場合、`@ViewScoped`では状態が失われる
- `@SessionScoped`により、リダイレクト後も検索結果（`bookList`）が保持される

---

## 6. トランザクション管理

### 6.1 トランザクション境界

```mermaid
graph TD
    A[User Request] --> B{Transaction Required?}
    B -->|Yes| C[@Transactional Service Method]
    B -->|No| D[Non-Transactional Operation]
    
    C --> E[BEGIN TRANSACTION]
    E --> F[Business Logic]
    F --> G[DAO Operations]
    G --> H{Success?}
    H -->|Yes| I[COMMIT]
    H -->|No| J[ROLLBACK]
    I --> K[Return Result]
    J --> L[Throw Exception]
```

### 6.2 トランザクション戦略

**基本方針:**
- **トランザクションタイプ**: JTA (Jakarta Transactions)
- **宣言**: サービスレイヤーで `@Transactional`
- **伝播**: REQUIRED (デフォルト) - 既存に参加または新規作成
- **ロールバック**: RuntimeException で自動ロールバック
- **スコープ**: サービスメソッドレベル

**トランザクション境界の例:**
- **OrderService.orderBooks()** - 以下を単一トランザクションで実行:
  1. 在庫可用性チェック
  2. 在庫更新（楽観的ロック付き）
  3. 注文トランザクション作成
  4. 注文明細作成

---

## 7. 並行制御（楽観的ロック）

### 7.1 楽観的ロック戦略

```mermaid
stateDiagram-v2
    [*] --> CartAdd: User adds to cart
    CartAdd --> StoreVersion: Save VERSION=1
    StoreVersion --> [*]
    
    [*] --> OrderStart: User confirms order
    OrderStart --> StockCheck: Check availability
    StockCheck --> UpdateStock: Update with VERSION=1
    
    UpdateStock --> VersionMatch: Database check
    VersionMatch --> Success: VERSION matches
    VersionMatch --> Failure: VERSION mismatch
    
    Success --> Commit: Commit transaction
    Failure --> Rollback: Rollback transaction
    
    Commit --> [*]
    Rollback --> ErrorDisplay: Show error to user
    ErrorDisplay --> [*]
```

### 7.2 実装詳細

**技術仕様:**
- **バージョンカラム**: `STOCK.VERSION` (BIGINT NOT NULL)
- **JPAアノテーション**: Stock エンティティに `@Version`
- **更新クエリ**: 自動的に WHERE 句 `AND VERSION = ?` を追加
- **例外**: バージョン不一致時に `OptimisticLockException`
- **ユーザーアクション**: ユーザーに通知、カート再確認を許可

**処理フロー:**

1. **カート追加時**: 在庫エンティティからVERSION値を取得し、カートアイテムに保存
2. **注文確定時**: カートアイテムに保存したVERSION値で在庫を更新
3. **バージョンチェック**: データベースのWHERE句に「AND VERSION = ?」条件を追加
4. **成功時**: 在庫数を減算し、VERSION値を自動インクリメント
5. **失敗時**: OptimisticLockExceptionをスロー、ユーザーにエラー表示

---

### 7.3 複合主キーとJPAマッピング

ORDER_DETAILテーブルは複合主キー `(ORDER_TRAN_ID, ORDER_DETAIL_ID)` を使用しています。
ORDER_TRAN_IDは複合主キーの一部であると同時に、ORDER_TRANへの外部キーでもあります。

#### 7.3.1 設計方針

**複合主キーの設計:**
- 埋め込み可能なクラスとして複合主キーを定義
- 注文トランザクションIDと注文明細IDで構成
- equals()とhashCode()を適切に実装

**エンティティ設計:**
- 複合主キーを埋め込み識別子として保持
- 親エンティティ（OrderTran）への関連を定義
- 関連は遅延ロード（LAZY）で設定（N+1問題回避）
- 複合主キーの一部と外部キーの二重定義を適切に処理

#### 7.3.2 複合主キーと外部キーの統合パターン

**課題:**
ORDER_TRAN_IDは複合主キーの一部であると同時に、ORDER_TRANへの外部キーでもある。これを誤って処理すると以下の問題が発生する。

**問題パターン:**
- 外部キーカラムに `insertable = false, updatable = false` を設定
- JPAはORDER_TRAN_IDをINSERT文に含めない
- ORDER_TRAN_IDがNULLとなり、NOT NULL制約違反が発生

**解決パターン:**
- 複合主キーと関連の統合マッピングアノテーション（`@MapsId`）を使用
- 親エンティティへの関連を設定することで、複合主キーのフィールドが自動的に設定される
- INSERT文に外部キーカラムが正しく含まれる
- 複合主キーのフィールドを手動で設定する必要がなくなる

#### 7.3.3 データ永続化フロー

**注文明細の作成手順:**
1. 複合主キーオブジェクトを生成し、各フィールドを設定
2. 注文明細エンティティを生成し、複合主キーを設定
3. 親エンティティ（OrderTran）への関連を設定（複合主キーの一部が自動設定される）
4. その他のフィールド（書籍ID、価格、数量）を設定
5. エンティティを永続化

**生成されるSQL:**
- ORDER_TRAN_ID、ORDER_DETAIL_ID、BOOK_ID、PRICE、COUNTを含むINSERT文
- 全てのNOT NULL制約を満たす

### 7.4 注文成功画面でのデータロード

#### 7.4.1 課題

**問題:**
注文完了後、注文成功画面で注文情報（注文番号、注文日時、総合計）が表示されない。

**原因:**
1. リダイレクト時にViewScopedのBean状態が失われる
2. URLパラメータで注文IDを引き継ぐ仕組みがない
3. パラメータから注文データを再取得する仕組みがない

#### 7.4.2 設計方針

**データ引き継ぎパターン:**
- 注文処理完了時に注文IDをURLパラメータとしてリダイレクト
- 遷移先画面でURLパラメータを受け取り、該当データを再取得
- ViewScopedのBeanに注文ID受け取り用フィールドを追加
- Serviceレイヤーに注文データ（明細含む）を取得するメソッドを追加

**JSFライフサイクル考慮事項:**
- 初期化メソッド（`@PostConstruct`）はビューパラメータ設定前に実行される
- ビューパラメータは初期化後に設定される
- データロードはビューアクションで実行し、パラメータ設定後に確実に取得する

**実行フロー:**
1. RESTORE_VIEW → 初期化メソッド実行（パラメータはまだ未設定）
2. APPLY_REQUEST_VALUES → ビューパラメータ設定
3. INVOKE_APPLICATION → ビューアクション実行（パラメータを使ってデータロード）
4. RENDER_RESPONSE → 取得データを画面表示

#### 7.4.3 コンポーネント責務

**プレゼンテーション層（Backing Bean）:**
- 注文ID受け取り用のフィールド保持
- ビューアクション用のデータロードメソッド提供
- リダイレクト時の注文ID付与

**ビジネスロジック層（Service）:**
- 注文IDから注文トランザクションと明細を同時取得
- データ不在時の例外処理

**データアクセス層（DAO）:**
- JOIN FETCHで注文トランザクションと明細を一括取得（N+1問題回避）

**ビュー層（XHTML）:**
- メタデータセクションでビューパラメータとビューアクションを定義
- 注文情報の各項目を適切にフォーマット表示

---

## 8. エラーハンドリング方針

### 8.1 例外階層

```mermaid
classDiagram
    RuntimeException <|-- BusinessException
    BusinessException <|-- OutOfStockException
    BusinessException <|-- EmailAlreadyExistsException
    RuntimeException <|-- OptimisticLockException
    
    class BusinessException {
        +String errorCode
        +String message
    }
    
    class OutOfStockException {
        +Integer bookId
        +String bookName
    }
    
    class EmailAlreadyExistsException {
        +String email
    }
```

### 8.2 エラーハンドリングフロー

```mermaid
flowchart TD
    A[Exception Occurs] --> B{Exception Type?}
    B -->|Business Exception| C[Log WARN]
    B -->|System Exception| D[Log ERROR with Stack Trace]
    B -->|Validation Exception| E[Log INFO]
    
    C --> F[Display User-Friendly Message]
    D --> G[Display Generic Error]
    E --> H[Display Validation Errors]
    
    F --> I[Stay on Current Page]
    G --> J[Redirect to Error Page]
    E --> I
    
    I --> K[Allow User Recovery]
    J --> L[Contact Support]
```

### 8.3 エラーハンドリング方針

| 例外タイプ | ログレベル | ユーザー表示 | アクション |
|-----------|----------|-------------|----------|
| ビジネス例外 | WARN | 分かりやすいメッセージ | 現在のページに留まる |
| システム例外 | ERROR（スタックトレース付き） | 汎用エラーメッセージ | エラーページにリダイレクト |
| 検証例外 | INFO | 検証エラーメッセージ | 現在のページに留まる |

---

## 9. セキュリティ

### 9.1 認証フロー

```mermaid
sequenceDiagram
    participant User
    participant Filter as AuthenticationFilter
    participant Session
    participant Bean as CustomerBean
    participant Service as CustomerService
    participant RestAPI as berry-books-rest API
    
    User->>Filter: HTTP Request
    Filter->>Filter: Extract request URI
    
    alt Public Page
        Filter->>User: Allow access
    else Protected Page
        Filter->>Session: session.getAttribute("customerBean")
        alt Logged In
            Session-->>Filter: CustomerBean exists
            Filter->>User: Allow access
        else Not Logged In
            Session-->>Filter: CustomerBean null
            Filter->>User: Redirect to login page
        end
    end
    
    Note over User,RestAPI: Login Process
    User->>Bean: Enter credentials
    Bean->>Service: authenticate(email, password)
    Service->>RestAPI: GET /customers/query_email?email={email}
    RestAPI-->>Service: CustomerTO
    Service->>Service: Password validation
    Service-->>Bean: Customer object
    Bean->>Bean: Set customer in CustomerBean (CDI)
    Bean->>Session: sessionMap.put("customerBean", customerBean) [EXPLICIT]
    Bean-->>User: Navigate to bookSearch
    
    Note over User,Session: Logout Process
    User->>Bean: Click logout
    Bean->>Bean: customer = null
    Bean->>Session: sessionMap.remove("customerBean") [EXPLICIT]
    Bean-->>User: Redirect to login page
```

**重要な実装ポイント:**

1. **AuthenticationFilter の定義**:
   - `@WebFilter`アノテーションは使用しない（二重定義を防ぐため）
   - `web.xml`で`<filter>`と`<filter-mapping>`を使用して定義
   - `urlPatterns="*.xhtml"`でXHTMLページのみをフィルタリング

2. **セッション管理の明示的な処理**:
   - CDI管理下の`@SessionScoped` Beanは`session.getAttribute()`で取得できないため
   - ログイン成功時に`FacesContext.getExternalContext().getSessionMap().put()`で明示的に保存
   - ログアウト時に`sessionMap.remove()`で明示的に削除
   - これにより、`AuthenticationFilter`がServlet APIの`HttpSession.getAttribute()`で取得可能

### 9.2 セキュリティ対策

| 対策 | 実装 | 制限事項 |
|---------|---------------|------------|
| **認証** | Servlet Filter + Session | シンプルなemail/passwordのみ |
| **セッション管理** | HTTP-Only Cookie | secureフラグ未使用（開発環境） |
| **入力検証** | Bean Validation (@NotNull, @Size等) | サーバーサイドのみ |
| **パスワード保存** | 平文 | ⚠️ 学習用のみ、本番環境では使用不可 |
| **CSRF保護** | JSF ViewState | 基本的な保護 |
| **SQLインジェクション** | JPA/JPQL (Prepared Statements) | パラメータ化クエリ |

### 9.3 セキュリティ制約

**公開ページ（認証不要）:**
- `index.xhtml` (ログインページ)
- `customerInput.xhtml` (登録ページ)
- `customerOutput.xhtml` (登録完了)

**保護ページ（認証必須）:**
- 上記以外の全ページ

---

## 10. データベース構成

### 10.1 永続化構成

**persistence.xml:**

```xml
<persistence-unit name="BerryBooksPU" transaction-type="JTA">
    <jta-data-source>jdbc/HsqldbDS</jta-data-source>
    <properties>
        <property name="jakarta.persistence.schema-generation.database.action" 
                  value="none"/>
        <property name="eclipselink.logging.level" value="FINE"/>
        <property name="eclipselink.logging.parameters" value="true"/>
    </properties>
</persistence-unit>
```

### 10.2 コネクションプール

| パラメータ | 値 | 備考 |
|-----------|-------|-------|
| **JNDI名** | jdbc/HsqldbDS | DataSource JNDI ルックアップ |
| **プール名** | HsqldbPool | コネクションプール識別子 |
| **ドライバ** | org.hsqldb.jdbc.JDBCDriver | HSQLDB JDBC ドライバ |
| **URL** | jdbc:hsqldb:hsql://localhost:9001/testdb | TCP接続 |
| **ユーザー** | SA | デフォルトHSQLDBユーザー |
| **パスワード** | (空) | パスワードなし |
| **最小プールサイズ** | 10 | 最小接続数 |
| **最大プールサイズ** | 50 | 最大接続数 |

### 10.3 データベース制約

- **トランザクション分離レベル**: READ_COMMITTED（デフォルト）
- **接続タイムアウト**: 30秒
- **アイドルタイムアウト**: 300秒

---

## 11. ログ戦略

### 11.1 ログフレームワーク

```
SLF4J (API) → Log4j2 (Implementation)
```

**依存関係:**
- `org.slf4j:slf4j-api:2.0.x` - SLF4J API（ロギングファサード）
- `org.apache.logging.log4j:log4j-core:2.21.x` - Log4j2コア実装
- `org.apache.logging.log4j:log4j-slf4j2-impl:2.21.x` - SLF4J 2.xバインディング

**設定ファイル:**
- `src/main/resources/log4j2.xml` - Log4j2設定（XML形式）

### 11.2 ログレベル

| レベル | 用途 | 例 |
|-------|-------|---------|
| **ERROR** | システムエラー、例外 | データベース接続失敗 |
| **WARN** | ビジネス例外、警告 | OutOfStockException、検証失敗 |
| **INFO** | メソッド開始点、主要イベント | "[ OrderService#orderBooks ]" |
| **DEBUG** | 詳細フロー、パラメータ値 | "Stock version: 1, quantity: 10" |
| **TRACE** | 非常に詳細なデバッグ | このプロジェクトでは未使用 |

### 11.3 ログパターン

```
標準形式:
INFO  [ ClassName#methodName ] message

パラメータ付き:
INFO  [ ClassName#methodName ] param1=value1, param2=value2

例外:
ERROR [ ClassName#methodName ] Error message
java.lang.RuntimeException: ...
    at ...
```

### 11.4 ログ出力方針

**ログ出力対象:**
- 全パブリックサービスメソッドのエントリ
- ビジネス例外の発生
- システム例外の発生（スタックトレース付き）
- トランザクション開始・コミット・ロールバック
- 重要な状態変更（在庫更新、注文作成など）

**ログ出力対象外:**
- 単純なGetter/Setter
- プライベートメソッド（必要に応じて）
- 正常系の詳細フロー（DEBUGレベルで出力）

---

## 12. ビルド＆デプロイ

### 12.1 ビルドプロセス

```mermaid
graph LR
    A[Source Code] --> B[Gradle Build]
    B --> C[Compile Java]
    C --> D[Process Resources]
    D --> E[Run Tests]
    E --> F[Package WAR]
    F --> G[berry-books.war]
    G --> H[Deploy to Payara]
```

### 12.2 Gradleタスク

| タスク | コマンド | 説明 |
|------|---------|-------------|
| WARビルド | `./gradlew :projects:java:berry-books:war` | コンパイルとパッケージング |
| テスト実行 | `./gradlew :projects:java:berry-books:test` | ユニットテスト実行 |
| デプロイ | `./gradlew :projects:java:berry-books:deploy` | Payaraへデプロイ |
| アンデプロイ | `./gradlew :projects:java:berry-books:undeploy` | Payaraから削除 |
| DB初期化 | `./gradlew :projects:java:berry-books:setupHsqldb` | データベース初期化 |

### 12.3 デプロイ構成

```mermaid
graph LR
    subgraph "Development Machine"
        A[IDE: VS Code / IntelliJ]
        B[Gradle Build Tool]
        C[Git Repository]
    end
    
    subgraph "Application Server"
        D[Payara Server 6<br/>Port 8080]
        E[berry-books.war]
    end
    
    subgraph "Database Server"
        F[HSQLDB Server<br/>Port 9001]
        G[testdb]
    end
    
    A --> B
    B --> E
    E --> D
    D --> F
    F --> G
```

| コンポーネント | 配置場所 | ポート | 備考 |
|-----------|---------|------|-------|
| Payara Server | `./payara6/` | 8080 (HTTP), 4848 (Admin) | スタンドアロンインストール |
| HSQLDB Server | `./hsqldb/` | 9001 (TCP) | バックグラウンドプロセス |
| アプリケーションWAR | `build/libs/berry-books.war` | - | デプロイ成果物 |
| アプリケーションコンテキスト | `/berry-books` | - | コンテキストルート |

---

## 13. テスト戦略

### 13.1 テストピラミッド

```mermaid
graph TD
    A[Manual Testing] --> B[Integration Tests]
    B --> C[Unit Tests]
    
    C --> D[Service Layer Tests<br/>80%+ Coverage]
    C --> E[DAO Layer Tests<br/>Key Queries]
    
    B --> F[End-to-End Flows<br/>Happy Path]
    B --> G[Error Scenarios<br/>Edge Cases]
    
    A --> H[User Acceptance<br/>Main Features]
```

### 13.2 テストカバレッジ

| レイヤー | カバレッジ目標 | テストフレームワーク |
|-------|----------------|---------------|
| サービスレイヤー | 80%以上 | JUnit 5 + Mockito |
| DAOレイヤー | 主要メソッド | JUnit 5 + インメモリDB |
| E2Eテスト | 主要画面遷移フロー | Playwright (Java) |
| 結合テスト | 主要フロー | JUnit 5 + Arquillian（オプション） |
| UI | 重要パス | Playwright + 手動テスト |

### 13.3 テスト方針

**ユニットテスト:**
- サービスレイヤーの全パブリックメソッド
- ビジネスロジックの境界値テスト
- エラーシナリオのテスト
- Mockitoを使用してDAOをモック

**E2Eテスト（Playwright）:**
- 画面遷移図（screen_design.md）に基づいた主要フローの自動化
- ログイン → 書籍検索 → カート → 注文 → 履歴参照の一連の流れ
- エラーケース（在庫不足、認証エラー等）の画面表示確認
- クロスブラウザテスト（Chromium, Firefox, WebKit）
- スクリーンショット取得とビジュアルリグレッション

**結合テスト:**
- 主要な業務フローのEnd-to-Endテスト
- 楽観的ロックの動作確認
- トランザクション境界の確認

**手動テスト:**
- UI/UXの確認
- ブラウザ互換性
- 主要機能の受入テスト

---

## 14. パフォーマンス考慮事項

### 14.1 最適化戦略

| 戦略 | 実装 | メリット |
|----------|---------------|---------|
| **遅延ロード** | JPAの `FetchType.LAZY` for collections | 初期クエリのオーバーヘッド削減 |
| **結合フェッチ** | JPQLの `JOIN FETCH` for eager loading | N+1問題の回避 |
| **コネクションプーリング** | Payaraコネクションプール (min=10, max=50) | データベース接続の再利用 |
| **セッション管理** | @SessionScoped (カート/ログイン) | データベースクエリの削減 |
| **楽観的ロック** | @Version (データベースロックなし) | 高並行性 |

### 14.2 期待パフォーマンス

| 指標 | 目標値 | 測定条件 |
|--------|--------|-------------|
| 検索クエリ | 2秒以内 | カテゴリ/キーワードで50冊 |
| 注文処理 | 3秒以内 | 在庫更新を含む |
| ページロード | 3秒以内 | ファーストペイント |
| 同時ユーザー数 | 50ユーザー | 開発環境 |

---

## 15. 技術リスクと軽減策

| リスク | 影響 | 発生確率 | 軽減策 |
|------|--------|-------------|------------|
| 楽観的ロック競合 | 注文失敗 | 中 | 明確なエラーメッセージ、再試行許可 |
| セッションタイムアウト | カートデータ喪失 | 低 | 60分タイムアウト、警告メッセージ |
| データベース接続枯渇 | サービス利用不可 | 低 | コネクションプール監視 |
| メモリリーク（セッションオブジェクト） | サーバー不安定化 | 低 | 適切なセッションクリーンアップ、監視 |
| HSQLDB制限 | パフォーマンス問題 | 中 | 開発用途のみ、PostgreSQL移行計画 |

---

## 16. 開発ガイドライン

### 16.1 コード標準

- **Javaバージョン**: 適切な箇所でJava 21機能を使用
- **コードスタイル**: Jakarta EE規約に従う
- **ログ出力**: 全パブリックサービスメソッドのエントリをログ
- **コメント**: パブリックAPI用にJavaDoc
- **エラーハンドリング**: 例外を握りつぶさない
- **NULL安全性**: 適切な箇所でOptionalを使用

### 16.2 Gitワークフロー

```mermaid
gitGraph
    commit id: "Initial commit"
    branch develop
    checkout develop
    commit id: "Feature: Book search"
    commit id: "Feature: Shopping cart"
    branch feature/order-processing
    checkout feature/order-processing
    commit id: "Implement order service"
    commit id: "Add optimistic locking"
    checkout develop
    merge feature/order-processing
    checkout main
    merge develop tag: "v1.0.0"
```

---

## 17. 将来の拡張（スコープ外）

### 17.1 技術的改善

- REST API layer (JAX-RS)
- Password hashing (BCrypt)
- HTTPS support
- OAuth 2.0 authentication
- Caching layer (EhCache)
- Message queue (JMS)
- Microservices architecture

### 17.2 インフラ改善

- ロードバランサー
- データベースレプリケーション
- 静的アセット用CDN
- モニタリングと可観測性 (Prometheus, Grafana)
- CI/CDパイプライン

---

## 18. 参考資料

### 18.1 技術ドキュメント

- [Jakarta EE 10 Platform Specification](https://jakarta.ee/specifications/platform/10/)
- [Jakarta Faces 4.0 Specification](https://jakarta.ee/specifications/faces/4.0/)
- [Jakarta Persistence 3.1 Specification](https://jakarta.ee/specifications/persistence/3.1/)
- [Payara Server Documentation](https://docs.payara.fish/)
- [EclipseLink JPA Documentation](https://www.eclipse.org/eclipselink/documentation/)

### 18.2 ベストプラクティス

- [Jakarta EE Design Patterns](https://www.oracle.com/java/technologies/design-patterns.html)
- [Optimistic Locking in JPA](https://thorben-janssen.com/optimistic-locking-in-jpa-hibernate/)
- [CDI Scopes Best Practices](https://jakarta.ee/specifications/cdi/4.0/jakarta-cdi-spec-4.0.html)

