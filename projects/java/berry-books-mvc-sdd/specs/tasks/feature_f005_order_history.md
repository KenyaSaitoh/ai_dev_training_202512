# F-005: 注文履歴参照 - 機能タスク

**タスクファイル:** feature_f005_order_history.md  
**担当者:** 担当者E（1名）  
**推奨スキル:** JSF, JPA, Jakarta EE  
**想定工数:** 10時間  
**依存タスク:** common_tasks.md

---

## 概要

登録顧客が過去の注文履歴を確認し、購入済み書籍と配送状況を把握する機能を実装します。

**参照SPEC:**
- [F_005_order_history/functional_design.md](../baseline/features/F_005_order_history/functional_design.md)
- [F_005_order_history/behaviors.md](../baseline/features/F_005_order_history/behaviors.md)
- [F_005_order_history/screen_design.md](../baseline/features/F_005_order_history/screen_design.md)

---

## ビジネスルール

| ルールID | 説明 |
|---------|-------------|
| BR-040 | 注文履歴は顧客IDでフィルタリング |
| BR-041 | 注文日降順（新しい順）でソート |
| BR-042 | 注文詳細は注文IDで取得 |

---

## タスクリスト

### セクション1: DTOクラス

- [ ] **T-F005-001**: OrderHistoryTOクラスの作成
  - **目的**: 注文履歴表示用DTOを実装する
  - **対象**: service/order/OrderHistoryTO.java
  - **参照SPEC**: functional_design.md#53-転送オブジェクト
  - **実装方式**: Java Record
  - **構造**: ORDER_TRANとORDER_DETAILを非正規化。1注文明細=1インスタンス
  - **フィールド**: orderDate, tranId, detailId, bookName, publisherName, price, count

- [ ] **T-F005-002**: OrderDetailTOクラスの作成
  - **目的**: 注文明細表示用DTOを実装する
  - **対象**: service/order/OrderDetailTO.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - POJO
    - フィールド:
      - orderDetailId (Integer) - 注文明細ID
      - bookId (Integer) - 書籍ID
      - bookName (String) - 書籍名
      - author (String) - 著者
      - price (BigDecimal) - 価格
      - count (Integer) - 数量
      - subtotal (BigDecimal) - 小計（price × count）
    - getter/setter

- [ ] **T-F005-003**: OrderSummaryTOクラスの作成
  - **目的**: 注文一覧表示用DTOを実装する
  - **対象**: service/order/OrderSummaryTO.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - POJO
    - フィールド:
      - orderTranId (Integer) - 注文ID
      - orderDate (LocalDate) - 注文日
      - totalPrice (BigDecimal) - 合計金額
      - itemCount (Integer) - アイテム数
    - getter/setter

---

### セクション2: サービス層

- [ ] **T-F005-004**: OrderServiceの注文履歴メソッド追加
  - **目的**: 注文履歴参照のビジネスロジックを実装する
  - **対象**: service/order/OrderService.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - 既存のOrderServiceに注文履歴関連メソッドを追加
    - @Inject OrderTranDao
    - 主要メソッド:
      - `getOrderHistory(Integer customerId)` - 注文履歴一覧を取得
        - **実装**: 注文トランザクションと注文明細を結合し、各明細ごとに1つのOrderHistoryTOを生成
        - OrderTranDao.findByCustomerIdWithDetails()を使用（JOIN FETCHで明細、書籍、出版社を同時取得）
        - 顧客IDでフィルタリング（BR-040）
        - 注文日降順でソート（BR-041）
        - List<OrderHistoryTO>を返却（各注文明細ごとに1レコード）
      - `getOrderDetail(Integer orderTranId)` - 注文詳細を取得
        - 注文IDで取得（BR-042）
        - JOIN FETCHで注文明細を即時読み込み
        - OrderSummaryTOを返却
    - データマッピング:
      - OrderTran + OrderDetail → OrderHistoryTO (Record)
      - LocalDateTimeをLocalDateに変換（orderTran.getOrderDate().toLocalDate()）
      - 複合キーから注文明細IDを取得（detail.getId().getOrderDetailId()）

---

### セクション3: プレゼンテーション層（Managed Bean）

- [ ] **T-F005-005**: OrderHistoryBeanの作成
  - **目的**: 注文履歴画面のコントローラーを実装する
  - **対象**: web/order/OrderHistoryBean.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @ViewScoped, implements Serializable
    - @Inject OrderService, CustomerBean
    - フィールド:
      - orderHistoryList (List<OrderHistoryTO>) - 注文履歴リスト
      - orderSummary (OrderSummaryTO) - 注文サマリー（注文詳細表示用）
      - orderTranId (Integer) - 注文ID（注文詳細画面用パラメータ）
      - selectedDetailId (Integer) - 注文明細ID（注文詳細画面用パラメータ）
    - 主要メソッド:
      - init() - @PostConstruct: viewActionから明示的にメソッド呼び出しされるため、実処理なし
      - loadOrderHistory() - 注文履歴を読み込む
        - **重要**: customerBean及びcustomerのnullチェックを実施
        - ログインしていない場合は空のリストを初期化（NullPointerException防止）
      - loadOrderDetail() - 注文詳細を読み込む
    - **エラーハンドリング**: customerBeanまたはcustomerがnullの場合、空のリストを初期化してNullPointerExceptionを防止

- [ ] **T-F005-006**: OrderDetailBeanの作成
  - **目的**: 注文詳細画面のコントローラーを実装する
  - **対象**: web/order/OrderDetailBean.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @ViewScoped, implements Serializable
    - @Inject OrderHistoryService
    - フィールド:
      - orderTranId (Integer) - 注文ID（パラメータ）
      - orderHistory (OrderHistoryTO) - 注文詳細
    - 主要メソッド:
      - init() - @PostConstruct: 注文詳細を取得
        - orderTranIdをリクエストパラメータから取得
        - OrderHistoryServiceのgetOrderDetailメソッドを呼び出し
      - navigateToHistory() - 注文履歴画面に戻る

---

### セクション4: ビュー層（XHTML）

- [ ] **T-F005-007**: orderHistory.xhtmlの作成（注文履歴画面）
  - **目的**: 注文履歴一覧画面を実装する
  - **対象**: webapp/orderHistory.xhtml
  - **参照SPEC**: screen_design.md#1-注文履歴画面
  - **注意事項**: 
    - 注文履歴一覧テーブル（h:dataTable）
    - 表示項目: 注文ID, 注文日, 合計金額, アイテム数
    - 「詳細を見る」リンク（h:commandLink action="#{orderHistoryBean.navigateToDetail}"）
    - 「書籍検索に戻る」リンク
    - 注文履歴が空の場合、「注文履歴がありません」メッセージを表示

- [ ] **T-F005-008**: orderDetail.xhtmlの作成（注文詳細画面）
  - **目的**: 注文詳細画面を実装する
  - **対象**: webapp/orderDetail.xhtml
  - **参照SPEC**: screen_design.md#2-注文詳細画面
  - **注意事項**: 
    - 注文情報表示:
      - 注文ID, 注文日, 配送先住所, 決済方法
    - 注文詳細テーブル（.order-detail-table）
      - 表示項目: カバー画像（セル幅35%）, 書籍情報（書籍名、出版社、カテゴリ、価格）
      - カバー画像表示:
        - `h:graphicImage library="images" name="covers/#{orderHistoryBean.orderSummary.orderDetails[0].book.bookName.replace(' ', '_')}.jpg"`
        - CSSクラス: `styleClass="book-thumbnail"`（高さ5cm、幅自動）
        - セルクラス: `.book-image-cell`（中央配置、幅35%）
      - 画像ファイルが存在しない場合、onErrorでno-image.jpgを表示
    - 合計金額表示（配送料金、総計）
    - 「注文履歴に戻る」リンク

---

### セクション5: ナビゲーション設定

- [ ] **T-F005-009**: faces-config.xmlへのナビゲーション追加
  - **目的**: 画面遷移ルールを設定する
  - **対象**: webapp/WEB-INF/faces-config.xml
  - **参照SPEC**: screen_design.md#画面遷移図
  - **注意事項**: 
    - bookSearch → orderHistory (注文履歴メニュー)
    - orderHistory → orderDetail (詳細を見る)
    - orderDetail → orderHistory (注文履歴に戻る)
    - orderHistory → bookSearch (書籍検索に戻る)

---

## 並行実行可能なタスク

### 並行実行可能
- T-F005-001, T-F005-002, T-F005-003 (DTOクラス) と T-F005-007, T-F005-008 (XHTML)

### 順次実行が必要
- T-F005-001, T-F005-002, T-F005-003 → T-F005-004 (OrderHistoryService) → T-F005-005, T-F005-006 (Beans) → T-F005-007, T-F005-008 (XHTML)

---

## ユニットテスト

### サービステスト
- [ ] **T-F005-UT-001**: OrderHistoryServiceTestの作成
  - **目的**: OrderHistoryServiceのユニットテストを作成する
  - **対象**: test/java/service/order/OrderHistoryServiceTest.java
  - **参照SPEC**: behaviors.md (F-005のシナリオ)
  - **テストケース**:
    - testGetOrderHistory() - 注文履歴一覧を取得
    - testGetOrderHistoryFilteredByCustomerId() - 顧客IDでフィルタリング（BR-040）
    - testGetOrderHistorySortedByOrderDate() - 注文日降順でソート（BR-041）
    - testGetOrderHistoryEmpty() - 注文履歴が空の場合
    - testGetOrderDetail() - 注文詳細を取得（BR-042）
    - testGetOrderDetailNotFound() - 注文が見つからない場合

### Beanテスト
- [ ] **T-F005-UT-002**: OrderHistoryBeanTestの作成
  - **目的**: OrderHistoryBeanのユニットテストを作成する
  - **対象**: test/java/web/order/OrderHistoryBeanTest.java
  - **参照SPEC**: behaviors.md (F-005のシナリオ)
  - **テストケース**:
    - testInit() - 注文履歴一覧の初期化
    - testNavigateToDetail() - 注文詳細画面に遷移
    - testNavigateToSearch() - 書籍検索画面に戻る

- [ ] **T-F005-UT-003**: OrderDetailBeanTestの作成
  - **目的**: OrderDetailBeanのユニットテストを作成する
  - **対象**: test/java/web/order/OrderDetailBeanTest.java
  - **参照SPEC**: behaviors.md (F-005のシナリオ)
  - **テストケース**:
    - testInit() - 注文詳細の初期化
    - testNavigateToHistory() - 注文履歴画面に戻る

### 結合テスト（手動）
- [ ] **T-F005-IT-001**: 注文履歴機能の動作確認
  - **目的**: 注文履歴機能が正しく動作することを確認する
  - **確認項目**:
    - 注文履歴画面が正しく表示される
    - 注文履歴が顧客IDでフィルタリングされている（BR-040）
    - 注文履歴が注文日降順で表示されている（BR-041）
    - 注文履歴が空の場合、「注文履歴がありません」メッセージが表示される

- [ ] **T-F005-IT-002**: 注文詳細機能の動作確認
  - **目的**: 注文詳細機能が正しく動作することを確認する
  - **確認項目**:
    - 「詳細を見る」リンクから注文詳細画面に遷移できる
    - 注文詳細画面が正しく表示される
    - 注文情報（注文ID、注文日、配送先住所、決済方法）が表示される
    - 注文明細（書籍名、著者、価格、数量、小計）が表示される
    - 合計金額が正しく表示される
    - 「注文履歴に戻る」リンクから注文履歴画面に戻れる

---

## 受入基準（behaviors.md参照）

### Scenario 1: 注文履歴一覧を表示する
**Given** ログイン済みユーザーが注文履歴メニューをクリック  
**When** 注文履歴画面が表示される  
**Then** 自分の注文履歴が新しい順に表示される（BR-040, BR-041）

### Scenario 2: 注文詳細を表示する
**Given** 注文履歴画面が表示されている  
**When** 注文IDの「詳細を見る」リンクをクリック  
**Then** 注文詳細画面が表示され、注文情報と注文明細が表示される（BR-042）

### Scenario 3: 注文履歴が空の場合
**Given** ログイン済みユーザーが注文履歴メニューをクリック  
**When** 注文履歴画面が表示される（注文履歴がない場合）  
**Then** 「注文履歴がありません」メッセージが表示される

---

## 完了条件

- [ ] 全タスクが完了している
- [ ] OrderHistoryServiceが正しく動作している
- [ ] OrderHistoryBeanが正しく動作している
- [ ] OrderDetailBeanが正しく動作している
- [ ] 注文履歴画面が正しく表示される
- [ ] 注文詳細画面が正しく表示される
- [ ] 画面遷移が正しく動作している
- [ ] 全受入基準が満たされている
- [ ] ユニットテストが作成され、カバレッジ80%以上を達成している

---

## トラブルシューティング

### 注文履歴が表示されない
- OrderHistoryServiceのgetOrderHistoryメソッドが正しく実装されているか確認
- OrderTranDaoのfindByCustomerIdメソッドが正しく動作しているか確認
- 顧客IDが正しく取得されているか確認（CustomerBeanから）

### 注文履歴が正しい順序で表示されない
- OrderHistoryServiceで注文日降順でソートしているか確認（BR-041）
- OrderTranDaoのJPQLクエリで「ORDER BY o.orderDate DESC」が指定されているか確認

### 注文詳細が表示されない
- OrderHistoryServiceのgetOrderDetailメソッドが正しく実装されているか確認
- OrderTranDaoのfindByIdメソッドでJOIN FETCHを使用しているか確認
- 注文IDが正しく渡されているか確認（リクエストパラメータから）

### 他の顧客の注文が表示される
- OrderHistoryServiceで顧客IDでフィルタリングしているか確認（BR-040）
- セキュリティ上の問題なので、必ず確認すること

### 決済方法名が表示されない
- OrderHistoryServiceでSettlementType列挙型を使用して決済方法名を取得しているか確認
- SettlementTypeのgetName()メソッドが正しく実装されているか確認

---

**次のタスク:** 他の機能（F-001〜F-004）と並行実行可能

