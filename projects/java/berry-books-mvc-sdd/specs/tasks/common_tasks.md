# 共通機能タスク

**タスクファイル:** common_tasks.md  
**担当者:** 共通機能チーム（1-2名）  
**推奨スキル:** JPA, Jakarta EE, REST API Client  
**想定工数:** 16時間  
**依存タスク:** setup_tasks.md

---

## 概要

複数機能で共有される共通コンポーネントを実装します。これらのコンポーネントは、機能別実装の前提となります。

**参照SPEC:**
- [architecture_design.md](../baseline/system/architecture_design.md)
- [data_model.md](../baseline/system/data_model.md)
- [functional_design.md](../baseline/system/functional_design.md)
- [external_interface.md](../baseline/system/external_interface.md)

---

## タスクリスト

### セクション1: 共通エンティティ（Entity層）

- [ ] [P] **T-COMMON-001**: Publisherエンティティの作成
  - **目的**: 出版社エンティティを実装する
  - **対象**: entity/Publisher.java
  - **参照SPEC**: data_model.md#31-publisher出版社マスタ
  - **注意事項**: 
    - @Entity, @Id, @GeneratedValue
    - PUBLISHER_ID (自動採番)
    - PUBLISHER_NAME (NOT NULL)

- [ ] [P] **T-COMMON-002**: Categoryエンティティの作成
  - **目的**: カテゴリエンティティを実装する
  - **対象**: entity/Category.java
  - **参照SPEC**: data_model.md#32-categoryカテゴリマスタ
  - **注意事項**: 
    - @Entity, @Id, @GeneratedValue
    - CATEGORY_ID (自動採番)
    - CATEGORY_NAME (NOT NULL)

- [ ] [P] **T-COMMON-003**: Bookエンティティの作成
  - **目的**: 書籍エンティティを実装する
  - **対象**: entity/Book.java
  - **参照SPEC**: data_model.md#33-book書籍
  - **注意事項**: 
    - @Entity, @Id, @GeneratedValue
    - @ManyToOne でCategory, Publisherと関連付け
    - フィールド: BOOK_ID, BOOK_NAME, AUTHOR, PRICE
    - 画面表示では`#{book.bookName.replace(' ', '_')}.jpg`で画像ファイル名を動的生成
      - 例: "Java SEディープダイブ" → "Java_SEディープダイブ.jpg"
      - `h:graphicImage library="images" name="covers/#{book.bookName.replace(' ', '_')}.jpg"`で使用
      - CSSクラス: `styleClass="book-thumbnail"`（高さ5cm）

- [ ] [P] **T-COMMON-004**: Stockエンティティの作成（楽観的ロック対応）
  - **目的**: 在庫エンティティを実装する（楽観的ロック制御を含む）
  - **対象**: entity/Stock.java
  - **参照SPEC**: data_model.md#34-stock在庫
  - **注意事項**: 
    - @Entity, @Id (BOOK_ID)
    - @Version 列（BIGINT VERSION）を必ず含める
    - @OneToOne でBookと関連付け
    - QUANTITY (在庫数)

- [ ] [P] **T-COMMON-005**: Customerエンティティの作成
  - **目的**: 顧客エンティティを実装する
  - **対象**: entity/Customer.java
  - **参照SPEC**: data_model.md#35-customer顧客
  - **注意事項**: 
    - @Entity, @Id, @GeneratedValue
    - EMAIL (UNIQUE制約)
    - PASSWORD (平文保存、学習用のみ)
    - CUSTOMER_ID, CUSTOMER_NAME, BIRTHDAY, ADDRESS

- [ ] [P] **T-COMMON-006**: OrderTranエンティティの作成
  - **目的**: 注文トランザクションエンティティを実装する
  - **対象**: entity/OrderTran.java
  - **参照SPEC**: data_model.md#36-order_tran注文取引
  - **注意事項**: 
    - @Entity, @Id, @GeneratedValue
    - @ManyToOne でCustomerと関連付け
    - @OneToMany でOrderDetailと関連付け
    - ORDER_TRAN_ID, ORDER_DATE, TOTAL_PRICE, DELIVERY_PRICE, DELIVERY_ADDRESS, SETTLEMENT_TYPE

- [ ] **T-COMMON-007**: OrderDetailPKクラスの作成
  - **目的**: 注文明細の複合主キークラスを実装する
  - **対象**: entity/OrderDetailPK.java
  - **参照SPEC**: data_model.md#37-order_detail注文明細
  - **注意事項**: 
    - @Embeddable
    - ORDER_TRAN_ID, ORDER_DETAIL_ID
    - equals(), hashCode() を実装

- [ ] **T-COMMON-008**: OrderDetailエンティティの作成
  - **目的**: 注文明細エンティティを実装する
  - **対象**: entity/OrderDetail.java
  - **参照SPEC**: data_model.md#37-order_detail注文明細
  - **注意事項**: 
    - @Entity, @EmbeddedId (OrderDetailPK)
    - @ManyToOne でOrderTran, Bookと関連付け
    - PRICE, COUNT

---

### セクション2: 共通DAO（Data Access層）

- [ ] [P] **T-COMMON-009**: BookDaoの作成
  - **目的**: 書籍データアクセスクラスを実装する
  - **対象**: dao/BookDao.java
  - **参照SPEC**: 
    - architecture_design.md#412-データアクセス層dao
    - data_model.md#61-共通クエリパターン
  - **注意事項**: 
    - @ApplicationScoped
    - @PersistenceContext で EntityManager注入
    - 主要メソッド: findAll(), findById(), findByCategory(), searchByKeyword()

- [ ] [P] **T-COMMON-010**: CategoryDaoの作成
  - **目的**: カテゴリデータアクセスクラスを実装する
  - **対象**: dao/CategoryDao.java
  - **参照SPEC**: architecture_design.md#412-データアクセス層dao
  - **注意事項**: 
    - @ApplicationScoped
    - 主要メソッド: findAll(), findById()

- [ ] [P] **T-COMMON-011**: StockDaoの作成
  - **目的**: 在庫データアクセスクラスを実装する
  - **対象**: dao/StockDao.java
  - **参照SPEC**: 
    - architecture_design.md#412-データアクセス層dao
    - data_model.md#34-stock在庫
  - **注意事項**: 
    - @ApplicationScoped
    - 主要メソッド: findByBookId(), updateStock()
    - 楽観的ロックを考慮したupdate処理

- [ ] [P] **T-COMMON-012**: OrderTranDaoの作成
  - **目的**: 注文トランザクションデータアクセスクラスを実装する
  - **対象**: dao/OrderTranDao.java
  - **参照SPEC**: 
    - architecture_design.md#412-データアクセス層dao
    - data_model.md#36-order_tran注文取引
  - **注意事項**: 
    - @ApplicationScoped
    - 主要メソッド: 
      - persist(OrderTran) - 注文トランザクションを永続化
      - findByCustomerId(Integer) - 顧客IDで注文履歴を取得（注文日降順）
      - findByCustomerIdWithDetails(Integer) - 顧客IDで注文履歴を取得（JOIN FETCHで明細、書籍、出版社を同時取得、N+1問題回避）
      - findById(Integer) - 注文IDで検索
      - findByIdWithDetails(Integer) - 注文と明細をJOIN FETCHで取得

- [ ] [P] **T-COMMON-013**: OrderDetailDaoの作成
  - **目的**: 注文明細データアクセスクラスを実装する
  - **対象**: dao/OrderDetailDao.java
  - **参照SPEC**: 
    - architecture_design.md#412-データアクセス層dao
    - data_model.md#37-order_detail注文明細
  - **注意事項**: 
    - @ApplicationScoped
    - 主要メソッド: save(), findByOrderTranId()

---

### セクション3: 共通ユーティリティ

- [ ] [P] **T-COMMON-014**: MessageUtilクラスの作成
  - **目的**: メッセージリソースユーティリティを実装する
  - **対象**: common/MessageUtil.java
  - **参照SPEC**: 
    - architecture_design.md#511-共通ユーティリティcommon
    - functional_design.md#511-共通ユーティリティcommon
  - **注意事項**: 
    - finalクラス、staticメソッド
    - messages.propertiesからメッセージを取得
    - 主要メソッド: getMessage(String key), getMessage(String key, Object... params)

- [ ] [P] **T-COMMON-015**: SettlementType列挙型の作成
  - **目的**: 決済方法列挙型を実装する
  - **対象**: common/SettlementType.java
  - **参照SPEC**: 
    - architecture_design.md#511-共通ユーティリティcommon
    - data_model.md#36-order_tran注文取引
  - **注意事項**: 
    - Enum型
    - 定数: BANK_TRANSFER(1), CREDIT_CARD(2), CASH_ON_DELIVERY(3)
    - 主要メソッド: getCode(), getName(), fromCode(int)

- [ ] [P] **T-COMMON-016**: AddressUtilクラスの作成
  - **目的**: 住所処理ユーティリティを実装する
  - **対象**: util/AddressUtil.java
  - **参照SPEC**: 
    - functional_design.md (F-003 注文処理)
    - behaviors.md#配送料金計算
  - **注意事項**: 
    - finalクラス、staticメソッド
    - 主要メソッド: isOkinawa(String address)
    - 「沖縄県」を含むかチェック

---

### セクション4: REST APIクライアント

- [ ] **T-COMMON-017**: CustomerRestClientの作成
  - **目的**: berry-books-rest API へのREST APIクライアントを実装する
  - **対象**: client/customer/CustomerRestClient.java
  - **参照SPEC**: 
    - external_interface.md#21-berry-books-rest-api顧客管理
    - architecture_design.md#53-restクライアント層
  - **注意事項**: 
    - @ApplicationScoped
    - Jakarta REST Client APIを使用
    - ベースURL: http://localhost:8080/berry-books-rest
    - 主要メソッド:
      - getCustomer(Integer customerId) - GET /customers/{customerId}
      - getCustomerByEmail(String email) - GET /customers/query_email?email={email}
      - registerCustomer(CustomerTO) - POST /customers/
    - エラーハンドリング: 404 Not Found, 409 Conflict, 500 Internal Server Error

---

### セクション5: 共通サービス

- [ ] **T-COMMON-018**: CategoryServiceの作成
  - **目的**: カテゴリビジネスロジックを実装する
  - **対象**: service/category/CategoryService.java
  - **参照SPEC**: 
    - architecture_design.md#413-ビジネスロジック層service
    - functional_design.md (F-001 書籍検索)
  - **注意事項**: 
    - @ApplicationScoped
    - @Inject CategoryDao
    - 主要メソッド: getAllCategories()

- [ ] **T-COMMON-019**: DeliveryFeeServiceの作成
  - **目的**: 配送料金計算ロジックを実装する
  - **対象**: service/delivery/DeliveryFeeService.java
  - **参照SPEC**: 
    - functional_design.md#33-注文処理f-003
    - behaviors.md#配送料金計算
  - **注意事項**: 
    - @ApplicationScoped
    - @Inject AddressUtil
    - 主要メソッド: calculateDeliveryFee(BigDecimal totalPrice, String address)
    - ビジネスルール（BR-020）: 
      - 通常: 800円
      - 沖縄県: 1700円
      - 5000円以上: 送料無料

---

### セクション6: 認証フィルター

- [ ] **T-COMMON-020**: AuthenticationFilterの作成
  - **目的**: 認証フィルターを実装する
  - **対象**: web/filter/AuthenticationFilter.java
  - **参照SPEC**: 
    - architecture_design.md#91-認証フロー
    - functional_design.md (F-004 顧客認証)
    - behaviors.md#認証セッション
  - **注意事項**: 
    - `@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"*.xhtml"})`
    - 公開ページ（BR-033）: index.xhtml, customerInput.xhtml, customerOutput.xhtml
    - JSFリソース: /jakarta.faces.resource/ も除外
    - 未ログインの場合、index.xhtmlにリダイレクト（BR-034）
    - `@Inject LoginBean`でログイン状態を確認（`loginBean.isLoggedIn()`）

---

## 並行実行可能なタスク

### 並行実行可能（異なるエンティティ）
- T-COMMON-001, T-COMMON-002, T-COMMON-003, T-COMMON-005 (Publisher, Category, Book, Customer)
- T-COMMON-004 は T-COMMON-003 の後（BookとStockは1:1関連）
- T-COMMON-007, T-COMMON-008 は T-COMMON-006 の後（OrderTranとOrderDetailは1:N関連）

### 並行実行可能（異なるDAO）
- T-COMMON-009, T-COMMON-010, T-COMMON-011, T-COMMON-012, T-COMMON-013
- ただし、対応するエンティティが完成していることが前提

### 並行実行可能（異なるユーティリティ）
- T-COMMON-014, T-COMMON-015, T-COMMON-016

### 並行実行可能（独立したサービス）
- T-COMMON-018, T-COMMON-019

### 順次実行が必要
- セクション1（Entity）→ セクション2（DAO）→ セクション3-5（並行実行可能）

---

## 完了条件

- [ ] 全タスクが完了している
- [ ] 全エンティティがデータベーステーブルにマッピングされている
- [ ] 全DAOクラスが基本的なCRUD操作を提供している
- [ ] 共通ユーティリティが正しく動作している
- [ ] REST APIクライアントが正しく動作している
- [ ] 認証フィルターが未ログインユーザーをブロックしている
- [ ] ユニットテストが作成され、カバレッジ80%以上を達成している

---

## ユニットテスト

### エンティティテスト
- [ ] Entityクラスのgetter/setterが正しく動作すること
- [ ] リレーションシップが正しくマッピングされていること
- [ ] @Versionアノテーションが正しく動作すること（Stock）

### DAOテスト
- [ ] 各DAOの主要メソッドが正しく動作すること
- [ ] JPQLクエリが正しい結果を返すこと
- [ ] 楽観的ロックが正しく動作すること（StockDao）

### ユーティリティテスト
- [ ] MessageUtilがメッセージを正しく取得できること
- [ ] SettlementTypeの列挙型変換が正しく動作すること
- [ ] AddressUtilが沖縄県を正しく判定できること

### サービステスト
- [ ] CategoryServiceが全カテゴリを取得できること
- [ ] DeliveryFeeServiceが配送料金を正しく計算できること（通常、沖縄、送料無料の3パターン）

### REST APIクライアントテスト
- [ ] CustomerRestClientが顧客情報を正しく取得できること
- [ ] エラーハンドリングが正しく動作すること（404, 409, 500）

---

## トラブルシューティング

### エンティティマッピングエラー
- @Entity, @Id, @GeneratedValue が正しく設定されているか確認
- 外部キーの@ManyToOne, @OneToMany が正しく設定されているか確認

### 楽観的ロックエラー
- Stock エンティティに@Version列が存在するか確認
- VERSION列のデータ型がBIGINTであるか確認

### REST API接続エラー
- berry-books-rest プロジェクトが起動しているか確認
- ベースURLが正しいか確認（http://localhost:8080/berry-books-rest）

### 認証フィルターエラー
- CustomerBeanがセッションスコープに存在するか確認
- 公開ページのパスが正しく設定されているか確認

---

**次のフェーズ:** 機能別実装（F-001〜F-005を並行実行）
- [F-001: 書籍検索・閲覧](feature_f001_book_search.md)
- [F-002: ショッピングカート管理](feature_f002_shopping_cart.md)
- [F-003: 注文処理](feature_f003_order_processing.md)
- [F-004: 顧客管理・認証](feature_f004_customer_auth.md)
- [F-005: 注文履歴参照](feature_f005_order_history.md)

