# berry-books 結合・E2Eテストガイド

**作成日:** 2025-12-20  
**バージョン:** 1.0.0  
**ステータス:** 実装完了

---

## 概要

本ドキュメントは、berry-booksプロジェクトの結合テストおよびE2Eテストの実装ガイドです。

---

## 1. テスト構成

### 1.1 実装済みテスト

#### セクション1: 機能間結合テスト

| テストID | テスト名 | 実装ファイル | ステータス |
|---------|---------|-------------|----------|
| T_INTEG_001 | ログイン → 書籍検索 → カート追加 | LoginToCartIntegrationTest.java | ✅ 完了 |
| T_INTEG_002 | カート → 注文処理 → 注文履歴 | CartToOrderHistoryIntegrationTest.java | ✅ 完了 |
| T_INTEG_003 | 在庫減算と楽観的ロック | OptimisticLockIntegrationTest.java | ✅ 完了 |
| T_INTEG_004 | 認証フィルター | AuthenticationFilterIntegrationTest.java | ✅ 完了 |

#### セクション2: エンドツーエンドテスト（Playwright）

| テストID | テスト名 | 実装ファイル | ステータス |
|---------|---------|-------------|----------|
| T_INTEG_005 | 新規顧客の完全購入フロー | CompletePurchaseFlowE2ETest.java | ✅ 完了 |
| T_INTEG_006 | 在庫不足エラーフロー | StockErrorFlowE2ETest.java | ✅ 完了 |
| T_INTEG_007 | 配送料金計算の全パターン | DeliveryFeeCalculationE2ETest.java | ✅ 完了 |
| T_INTEG_008 | Playwright E2Eテストスイート | PlaywrightE2ETestSuite.java | ✅ 完了 |

---

## 2. テストの実行方法

### 2.1 統合テストの実行

```bash
# 全ての統合テストを実行
./gradlew :projects:java:berry-books-mvc-sdd:test

# 特定のテストクラスのみ実行
./gradlew :projects:java:berry-books-mvc-sdd:test --tests LoginToCartIntegrationTest

# E2Eテストのみ実行
./gradlew :projects:java:berry-books-mvc-sdd:test --tests *E2ETest
```

### 2.2 前提条件

1. **Payara Serverの起動**
   ```bash
   ./gradlew :projects:java:berry-books-mvc-sdd:startPayara
   ```

2. **HSQLDBの起動**
   ```bash
   ./gradlew :projects:java:berry-books-mvc-sdd:startHsqldb
   ```

3. **アプリケーションのデプロイ**
   ```bash
   ./gradlew :projects:java:berry-books-mvc-sdd:deploy
   ```

4. **テストデータの準備**
   - 顧客データ、書籍データ、カテゴリデータが登録されていること
   - 在庫データが適切に設定されていること

---

## 3. パフォーマンステスト

### 3.1 T_INTEG_009: 書籍検索のレスポンスタイムテスト

**目的:** 書籍検索のレスポンスタイムが2秒以内であることを確認する

**実施方法:**
1. JMeterまたはPlaywrightを使用してレスポンスタイムを計測
2. カテゴリ「Java」で検索（50冊の書籍が存在すると仮定）
3. レスポンスタイムを記録

**期待結果:** レスポンスタイム2秒以内

### 3.2 T_INTEG_010: 注文処理のレスポンスタイムテスト

**目的:** 注文処理のレスポンスタイムが3秒以内であることを確認する

**実施方法:**
1. カートに5冊の書籍を追加
2. 注文確定ボタンをクリック
3. レスポンスタイムを計測

**期待結果:** レスポンスタイム3秒以内

### 3.3 T_INTEG_011: 同時アクセステスト（50ユーザー）

**目的:** 50ユーザーの同時アクセスに対応できることを確認する

**実施方法:**
1. JMeterで負荷テストシナリオを作成
2. 50ユーザーが同時にログイン
3. 各ユーザーが書籍検索を実行
4. 各ユーザーがカートに書籍を追加
5. エラーが発生しないことを確認

**期待結果:** 50ユーザーの同時アクセスに対応できる

**JMeterテストプランの例:**
- Thread Group: 50 threads (users)
- Ramp-Up Period: 10 seconds
- Loop Count: 1
- HTTP Request Samplers:
  - ログイン
  - 書籍検索
  - カート追加

---

## 4. ブラウザ互換性テスト

### 4.1 T_INTEG_012: Chromeでの動作確認

**実施方法:**
1. Playwright Chromiumブラウザでテストを実行
2. エンドツーエンドテスト（T_INTEG_005）をChromeで実行
3. スクリーンショットを取得

**期待結果:** 全機能が正常に動作する

### 4.2 T_INTEG_013: Firefoxでの動作確認

**実施方法:**
1. Playwright Firefoxブラウザでテストを実行
2. エンドツーエンドテスト（T_INTEG_005）をFirefoxで実行
3. スクリーンショットを取得

**期待結果:** 全機能が正常に動作する

### 4.3 T_INTEG_014: Edgeでの動作確認

**実施方法:**
1. 手動でEdgeブラウザを起動
2. エンドツーエンドテスト（T_INTEG_005）の手順を手動実行
3. 動作を確認

**期待結果:** 全機能が正常に動作する

---

## 5. 最終検証タスク

### 5.1 T_INTEG_015: 全受入基準の確認

**対象:** behaviors.md の全シナリオ

**確認項目:**
- F-001: 全4シナリオ（カテゴリ検索、キーワード検索、組み合わせ検索、在庫なし表示）
- F-002: 全4シナリオ（カート追加、削除、クリア、同じ書籍追加）
- F-003: 全6シナリオ（注文確定、在庫不足、楽観的ロック、配送料金計算3パターン）
- F-004: 全6シナリオ（新規登録、重複エラー、ログイン、ログイン失敗、アクセス制限、ログアウト）
- F-005: 全3シナリオ（注文履歴一覧、注文詳細、空の履歴）

**期待結果:** 全受入基準が満たされている

### 5.2 T_INTEG_016: エラーメッセージの確認

**対象:** 全エラーシナリオ

**確認項目:**
- VAL-001〜004: 検証エラー
- BIZ-001〜005: ビジネスエラー
- SYS-001〜003: システムエラー

**期待結果:** 全エラーメッセージが正しく表示される

### 5.3 T_INTEG_017: ログ出力の確認

**対象:** 全Serviceクラス、全Managed Beanクラス

**確認方法:**
1. エンドツーエンドテスト（T_INTEG_005）を実行
2. ログファイルを確認
3. 全Serviceメソッドのエントリログが出力されていることを確認
4. ログパターンが`[ ClassName#methodName ]`形式であることを確認

**期待結果:** 全主要処理でログが正しく出力される

### 5.4 T_INTEG_018: テストカバレッジの確認

**対象:** 全Serviceクラス

**確認方法:**
1. JaCoCoでカバレッジレポートを生成
   ```bash
   ./gradlew :projects:java:berry-books-mvc-sdd:jacocoTestReport
   ```
2. Serviceレイヤーのカバレッジが80%以上であることを確認

**期待結果:** カバレッジ80%以上

### 5.5 T_INTEG_019: 最終ビルドとデプロイの確認

**確認方法:**
1. `./gradlew clean`を実行
2. `./gradlew :projects:java:berry-books-mvc-sdd:war`を実行
3. ビルドが成功することを確認
4. `./gradlew :projects:java:berry-books-mvc-sdd:deploy`を実行
5. デプロイが成功することを確認
6. `http://localhost:8080/berry-books/`にアクセス
7. アプリケーションが正常に起動していることを確認

**期待結果:** ビルドとデプロイが成功する

---

## 6. トラブルシューティング

### 6.1 E2Eテストが失敗する場合

**原因:**
- Payara Serverが起動していない
- アプリケーションがデプロイされていない
- テストデータが不足している

**対処法:**
1. Payara Serverの起動状態を確認
2. アプリケーションのデプロイ状態を確認
3. ログを確認してエラー原因を特定

### 6.2 Playwrightのインストールエラー

**原因:**
- Playwrightブラウザドライバーがインストールされていない

**対処法:**
```bash
# Playwrightブラウザドライバーをインストール
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

---

## 7. まとめ

本ドキュメントでは、berry-booksプロジェクトの結合・E2Eテストの実装方法と実行方法を説明しました。

**実装済みテスト:**
- 機能間結合テスト: 4件
- E2Eテスト: 4件
- パフォーマンステスト: 3件（実装ガイド）
- ブラウザ互換性テスト: 3件（実装ガイド）
- 最終検証タスク: 5件（チェックリスト）

**次のステップ:**
1. テストの実行と結果の確認
2. パフォーマンステストの実施
3. ブラウザ互換性テストの実施
4. 最終検証タスクの完了

---

**参考資料:**
- [architecture_design.md](../specs/baseline/system/architecture_design.md) - テスト戦略
- [behaviors.md](../specs/baseline/system/behaviors.md) - 受入基準
- [integration_tasks.md](../tasks/integration_tasks.md) - タスクリスト

