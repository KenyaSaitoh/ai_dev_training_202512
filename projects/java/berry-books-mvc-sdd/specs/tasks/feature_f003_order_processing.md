# F-003: 注文処理 - 機能タスク

**タスクファイル:** feature_f003_order_processing.md  
**担当者:** 担当者C（1名）  
**推奨スキル:** JPA, トランザクション管理, 楽観的ロック, Jakarta EE  
**想定工数:** 16時間  
**依存タスク:** common_tasks.md

---

## 概要

カート内の書籍を購入し、配送先と決済方法を指定して注文を確定する機能を実装します。楽観的ロック制御による在庫管理を含みます。

**参照SPEC:**
- [F_003_order_processing/functional_design.md](../baseline/features/F_003_order_processing/functional_design.md)
- [F_003_order_processing/behaviors.md](../baseline/features/F_003_order_processing/behaviors.md)
- [F_003_order_processing/screen_design.md](../baseline/features/F_003_order_processing/screen_design.md)

---

## ビジネスルール

| ルールID | 説明 | 詳細 |
|---------|-------------|---------|
| BR-020 | 配送料金計算ルール | 通常800円、沖縄県1700円、購入金額5000円以上で送料無料 |
| BR-021 | 決済方法選択肢 | 銀行振込、クレジットカード、着払い |
| BR-022 | 在庫チェックタイミング | 注文確定時に全書籍の在庫を確認 |
| BR-023 | 在庫減算タイミング | 在庫チェック後、注文登録前に減算 |
| BR-024 | 楽観的ロック制御 | カート追加時のバージョン番号で在庫更新 |
| BR-025 | トランザクション範囲 | 在庫チェック〜注文登録〜在庫減算は単一トランザクション |

---

## タスクリスト

### セクション1: DTOクラス

- [ ] **T-F003-001**: OrderTOクラスの作成
  - **目的**: 注文情報転送オブジェクトを実装する
  - **対象**: service/order/OrderTO.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - POJO
    - フィールド:
      - cartItems (List<CartItem>) - カート内容
      - customerId (Integer) - 顧客ID
      - deliveryAddress (String) - 配送先住所
      - settlementType (Integer) - 決済方法
      - totalPrice (BigDecimal) - 合計金額
      - deliveryPrice (BigDecimal) - 配送料金
    - getter/setter

- [ ] **T-F003-002**: OutOfStockExceptionの作成
  - **目的**: 在庫不足例外を実装する
  - **対象**: service/order/OutOfStockException.java
  - **参照SPEC**: functional_design.md#7-例外エラー処理
  - **注意事項**: 
    - RuntimeExceptionを継承
    - フィールド:
      - bookId (Integer)
      - bookName (String)
      - requestedQuantity (Integer)
      - availableQuantity (Integer)
    - コンストラクタとgetter

---

### セクション2: サービス層

- [ ] **T-F003-003**: OrderServiceIFインターフェースの作成
  - **目的**: 注文サービスのインターフェースを定義する
  - **対象**: service/order/OrderServiceIF.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - インターフェース
    - メソッド: orderBooks(OrderTO orderTO)

- [ ] **T-F003-004**: OrderServiceの作成
  - **目的**: 注文処理のビジネスロジックを実装する
  - **対象**: service/order/OrderService.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - @ApplicationScoped
    - implements OrderServiceIF
    - @Inject StockDao, OrderTranDao, OrderDetailDao, DeliveryFeeService
    - @Transactional（BR-025: トランザクション境界）
    - 主要メソッド:
      - orderBooks(OrderTO orderTO) - 注文処理
        1. 在庫チェック（BR-022）
        2. 楽観的ロック制御（BR-024）
        3. 在庫減算（BR-023）
        4. 注文トランザクション作成
        5. 注文明細作成
    - 例外処理:
      - OutOfStockException（在庫不足）
      - OptimisticLockException（楽観的ロック競合）

---

### セクション3: プレゼンテーション層（Managed Bean）

- [ ] **T-F003-005**: OrderBeanの作成
  - **目的**: 注文入力画面のコントローラーを実装する
  - **対象**: web/order/OrderBean.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @ViewScoped, implements Serializable
    - @Inject OrderService, DeliveryFeeService, CartSession, CustomerBean
    - フィールド:
      - deliveryAddress (String) - 配送先住所
      - settlementType (Integer) - 決済方法
      - deliveryPrice (BigDecimal) - 配送料金
      - errorMessage (String) - エラーメッセージ
    - 主要メソッド:
      - init() - @PostConstruct: 配送料金を計算
      - calculateDeliveryFee() - 配送料金を再計算（住所変更時）
      - confirmOrder() - 注文確定
      - navigateToCart() - カート画面に戻る
    - 例外処理:
      - OutOfStockException → orderError.xhtmlに遷移
      - OptimisticLockException → orderError.xhtmlに遷移

---

### セクション4: ビュー層（XHTML）

- [ ] **T-F003-006**: bookOrder.xhtmlの作成（注文入力画面）
  - **目的**: 注文入力画面を実装する
  - **対象**: webapp/bookOrder.xhtml
  - **参照SPEC**: screen_design.md#1-注文入力画面
  - **注意事項**: 
    - 注文内容確認（カート内容、合計金額）
    - カバー画像表示（h:graphicImage value="resources/covers/#{book.imageFileName}"）
      - BookエンティティのgetImageFileName()メソッドで書籍名 + ".jpg" を返す
    - 画像ファイルが存在しない場合、no-image.jpgを表示
    - 画像サイズ: 最大幅60px、高さ自動調整
    - 配送先住所入力（h:inputText）
    - 決済方法選択（h:selectOneRadio）
      - 銀行振込、クレジットカード、着払い（BR-021）
    - 配送料金表示（#{orderBean.deliveryPrice}）
      - 住所変更時に自動再計算（BR-020）
    - 「注文確定」ボタン（h:commandButton action="#{orderBean.confirmOrder}"）
    - 「カートに戻る」リンク
    - バリデーション:
      - 配送先住所（必須）
      - 決済方法（必須）

- [ ] **T-F003-007**: orderSuccess.xhtmlの作成（注文完了画面）
  - **目的**: 注文完了画面を実装する
  - **対象**: webapp/orderSuccess.xhtml
  - **参照SPEC**: screen_design.md#2-注文完了画面
  - **注意事項**: 
    - 注文完了メッセージ
    - 注文内容表示（注文ID、注文日、合計金額、配送料金）
    - 「注文履歴を見る」リンク
    - 「書籍検索に戻る」リンク

- [ ] **T-F003-008**: orderError.xhtmlの作成（注文エラー画面）
  - **目的**: 注文エラー画面を実装する
  - **対象**: webapp/orderError.xhtml
  - **参照SPEC**: screen_design.md#3-注文エラー画面
  - **注意事項**: 
    - エラーメッセージ表示（#{orderBean.errorMessage}）
    - エラー内容:
      - BIZ-003: 在庫不足エラー
      - BIZ-004: 楽観的ロック競合エラー
    - 「カートに戻る」ボタン

---

### セクション5: ナビゲーション設定

- [ ] **T-F003-009**: faces-config.xmlへのナビゲーション追加
  - **目的**: 画面遷移ルールを設定する
  - **対象**: webapp/WEB-INF/faces-config.xml
  - **参照SPEC**: screen_design.md#画面遷移図
  - **注意事項**: 
    - cartView → bookOrder (注文へ進む)
    - bookOrder → orderSuccess (注文成功)
    - bookOrder → orderError (注文失敗)
    - orderError → cartView (カートに戻る)
    - orderSuccess → orderHistory (注文履歴)
    - orderSuccess → bookSearch (書籍検索)

---

## 並行実行可能なタスク

### 並行実行可能
- T-F003-001 (OrderTO), T-F003-002 (Exception) と T-F003-006, T-F003-007, T-F003-008 (XHTML)

### 順次実行が必要
- T-F003-001, T-F003-002 → T-F003-003 (Interface) → T-F003-004 (OrderService) → T-F003-005 (OrderBean) → T-F003-006, T-F003-007, T-F003-008 (XHTML)

---

## ユニットテスト

### サービステスト
- [ ] **T-F003-UT-001**: OrderServiceTestの作成
  - **目的**: OrderServiceのユニットテストを作成する
  - **対象**: test/java/service/order/OrderServiceTest.java
  - **参照SPEC**: behaviors.md (F-003のシナリオ)
  - **テストケース**:
    - testOrderBooksSuccess() - 注文成功（正常系）
    - testOrderBooksOutOfStock() - 在庫不足エラー（BR-022）
    - testOrderBooksOptimisticLock() - 楽観的ロック競合（BR-024）
    - testStockIsDecrementedAfterOrder() - 在庫減算確認（BR-023）
    - testOrderCreatesOrderTranAndOrderDetail() - 注文レコード作成
    - testTransactionRollbackOnError() - エラー時のロールバック（BR-025）

### 配送料金計算テスト
- [ ] **T-F003-UT-002**: DeliveryFeeServiceTestの作成
  - **目的**: DeliveryFeeServiceのユニットテストを作成する
  - **対象**: test/java/service/delivery/DeliveryFeeServiceTest.java
  - **参照SPEC**: behaviors.md#配送料金計算
  - **テストケース**:
    - testDeliveryFeeNormal() - 通常配送料金800円（BR-020）
    - testDeliveryFeeOkinawa() - 沖縄県配送料金1700円（BR-020）
    - testDeliveryFeeFreeOver5000() - 5000円以上送料無料（BR-020）
    - testDeliveryFeeFreeOver5000Okinawa() - 沖縄県でも5000円以上送料無料（BR-020）

### 結合テスト（手動）
- [ ] **T-F003-IT-001**: 注文処理の動作確認
  - **目的**: 注文処理が正しく動作することを確認する
  - **確認項目**:
    - 注文入力画面が正しく表示される
    - 配送先住所を入力できる
    - 決済方法を選択できる
    - 配送料金が自動計算される（BR-020）
    - 注文確定が成功する
    - 在庫が減算される（BR-023）
    - 注文トランザクションと注文明細が作成される

- [ ] **T-F003-IT-002**: 在庫不足エラーの動作確認
  - **目的**: 在庫不足時にエラーが表示されることを確認する
  - **確認項目**:
    - カートに在庫以上の数量を追加
    - 注文確定時に在庫不足エラーが表示される
    - カートに戻ることができる

- [ ] **T-F003-IT-003**: 楽観的ロック競合の動作確認
  - **目的**: 同時購入時にエラーが表示されることを確認する
  - **確認項目**:
    - 2つのブラウザで同じ書籍をカートに追加
    - 1つ目のブラウザで注文確定成功
    - 2つ目のブラウザで注文確定時に楽観的ロック競合エラーが表示される
    - カートに戻ることができる

---

## 受入基準（behaviors.md参照）

### Scenario 1: 注文を確定する（正常系）
**Given** カートに書籍A（数量1）が追加されている  
**When** 配送先住所と決済方法を入力して「注文確定」  
**Then** 注文が確定され、在庫が減算され、注文完了画面が表示される

### Scenario 2: 在庫不足エラー（異常系）
**Given** カートに書籍A（数量10）が追加されているが、実際の在庫は5  
**When** 注文を確定  
**Then** 在庫不足エラーが表示され、カートに戻る

### Scenario 3: 同時注文による競合（楽観的ロック）
**Given** ユーザーAとユーザーBが同じ書籍をカートに追加  
**When** ユーザーAが先に注文確定、その後ユーザーBが注文確定  
**Then** ユーザーBに楽観的ロック競合エラーが表示される

### Scenario 4: 配送料金の自動計算（通常）
**Given** カートに書籍A（3000円）が追加されている  
**When** 配送先住所に「東京都」を入力  
**Then** 配送料金800円が表示される

### Scenario 5: 沖縄県への配送料金
**Given** カートに書籍A（3000円）が追加されている  
**When** 配送先住所に「沖縄県」を入力  
**Then** 配送料金1700円が表示される

### Scenario 6: 送料無料（5000円以上）
**Given** カートに書籍A（5000円）が追加されている  
**When** 配送先住所を入力  
**Then** 配送料金0円（送料無料）が表示される

---

## 完了条件

- [ ] 全タスクが完了している
- [ ] OrderServiceが正しく動作している
- [ ] DeliveryFeeServiceが正しく動作している
- [ ] OrderBeanが正しく動作している
- [ ] 注文入力画面が正しく表示される
- [ ] 注文完了画面が正しく表示される
- [ ] 注文エラー画面が正しく表示される
- [ ] 画面遷移が正しく動作している
- [ ] 楽観的ロック制御が正しく動作している
- [ ] トランザクション管理が正しく動作している
- [ ] 全受入基準が満たされている
- [ ] ユニットテストが作成され、カバレッジ80%以上を達成している

---

## トラブルシューティング

### 注文が確定されない
- OrderServiceのorderBooksメソッドが正しく実装されているか確認
- @Transactionalアノテーションが正しく設定されているか確認（BR-025）
- 在庫チェックが正しく動作しているか確認（BR-022）

### 在庫が減算されない
- OrderServiceで在庫減算処理が実装されているか確認（BR-023）
- トランザクションが正しくコミットされているか確認

### 楽観的ロックが動作しない
- StockエンティティにVERSION列が存在するか確認
- CartItemに在庫バージョン番号が保存されているか確認（BR-012）
- StockDaoのupdateStockメソッドでVERSIONチェックが行われているか確認（BR-024）

### 配送料金が正しく計算されない
- DeliveryFeeServiceのcalculateDeliveryFeeメソッドが正しく実装されているか確認（BR-020）
- AddressUtilのisOkinawaメソッドが正しく動作しているか確認
- 5000円以上送料無料ルールが正しく実装されているか確認

### OptimisticLockExceptionがハンドリングされない
- OrderServiceでOptimisticLockExceptionをcatchしているか確認
- OrderBeanでOptimisticLockExceptionをcatchしてorderError画面に遷移しているか確認

---

**次のタスク:** 他の機能（F-001, F-002, F-004, F-005）と並行実行可能

