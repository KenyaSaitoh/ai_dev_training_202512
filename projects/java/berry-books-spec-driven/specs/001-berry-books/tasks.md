# berry-books - 実装タスク

**Feature ID:** 001-berry-books  
**Version:** 1.0.0  
**Last Updated:** 2025-12-13  
**Status:** タスク分解完了

---

## タスク実行ガイド

### 記号
- **[ ]** 未開始
- **[P]** 同レベルの他の[P]タスクと並行実行可能
- **[→]** 直前のタスクの完了に依存
- **[✓]** 完了

### 依存関係ルール
1. [P]マーク以外のタスクは上から下へ順次実行
2. [P]タスクは同一セクション内の他の[P]タスクと同時実行可能
3. [→]マークのタスクは直前のタスクの完了が必要
4. サブタスクは親タスクの依存関係を継承

---

## フェーズ 1: インフラストラクチャセットアップ

### 1.1 開発環境

- [ ] **タスク 1.1.1**: プロジェクト構造のセットアップ
  - Gradleマルチプロジェクト構成を作成
  - Jakarta EE 10依存関係でbuild.gradleを設定
  - ソースディレクトリのセットアップ (src/main/java, src/main/resources, src/main/webapp)
  - settings.gradleの設定
  - **想定時間**: 30分

- [ ] [→] **タスク 1.1.2**: アプリケーションサーバーの設定
  - Payara Server 6.xのインストール
  - ドメイン設定（ポート、ログ）
  - JNDIデータソースのセットアップ (jdbc/HsqldbDS)
  - サーバー起動テスト
  - **想定時間**: 30分

- [ ] [→] **タスク 1.1.3**: データベースの設定
  - HSQLDB 2.7.xのインストール
  - データベース初期化スクリプトの作成
    - 1_BOOKSTORE_DROP.sql
    - 2_BOOKSTORE_DDL.sql
    - 3_BOOKSTORE_DML.sql
  - データベース接続テスト
  - **想定時間**: 45分

- [ ] [→] **タスク 1.1.4**: ログの設定
  - SLF4J + Logback依存関係の追加
  - logback.xml設定ファイルの作成
  - ログレベルの設定（開発用INFO）
  - ログ出力テスト
  - **想定時間**: 15分

---

## フェーズ 2: 永続化レイヤー

### 2.1 データベーススキーマ

- [ ] **タスク 2.1.1**: マスタテーブルの作成
  - PUBLISHERテーブルの実装
  - CATEGORYテーブルの実装
  - マスタデータの挿入
  - **想定時間**: 30分

- [ ] [P] **タスク 2.1.2**: BOOKテーブルの作成
  - 外部キー付きBOOKテーブルの実装
  - インデックスの作成 (CATEGORY_ID, BOOK_NAME)
  - サンプル書籍データの挿入（50件）
  - **想定時間**: 45分

- [ ] [P] **タスク 2.1.3**: STOCKテーブルの作成
  - VERSIONカラム付きSTOCKテーブルの実装
  - BOOKテーブルへのリンク（1:1関係）
  - 在庫データの挿入（BOOKレコードに対応）
  - **想定時間**: 30分

- [ ] [P] **タスク 2.1.4**: CUSTOMERテーブルの作成
  - CUSTOMERテーブルの実装
  - EMAIL に UNIQUE 制約を追加
  - テスト顧客データの挿入（3-5件）
  - **想定時間**: 30分

- [ ] [→] **タスク 2.1.5**: 注文テーブルの作成
  - ORDER_TRANテーブルの実装
  - ORDER_DETAILテーブルの実装（複合キー）
  - 外部キーリレーションシップの作成
  - テスト注文データの挿入
  - **想定時間**: 45分

### 2.2 JPAエンティティ

- [ ] **タスク 2.2.1**: JPAの設定
  - persistence.xmlの作成
  - EclipseLinkプロバイダーの設定
  - トランザクションタイプをJTAに設定
  - データソースJNDIルックアップの設定
  - **想定時間**: 20分

- [ ] [P] **タスク 2.2.2**: マスタエンティティの実装
  - Categoryエンティティの作成
  - Publisherエンティティの作成
  - JPAアノテーションの追加 (@Entity, @Table, @Id)
  - **想定時間**: 30分

- [ ] [P] **タスク 2.2.3**: Bookエンティティの実装
  - Bookエンティティの作成
  - @ManyToOneリレーションシップの追加 (Category, Publisher)
  - STOCK用の@SecondaryTableの追加（quantityフィールド）
  - エンティティマッピングのテスト
  - **想定時間**: 45分

- [ ] [P] **タスク 2.2.4**: Stockエンティティの実装
  - Stockエンティティの作成
  - 楽観的ロック用の@Versionアノテーションの追加
  - バージョン管理のテスト
  - **想定時間**: 30分

- [ ] [P] **タスク 2.2.5**: Customerエンティティの実装
  - Customerエンティティの作成
  - バリデーションアノテーションの追加 (@NotNull, @Email)
  - エンティティマッピングのテスト
  - **想定時間**: 30分

- [ ] [→] **タスク 2.2.6**: 注文エンティティの実装
  - OrderTranエンティティの作成
  - OrderDetailエンティティの作成
  - OrderDetailPK（複合キークラス）の作成
  - @OneToManyリレーションシップの追加 (OrderTran → OrderDetail)
  - エンティティリレーションシップのテスト
  - **想定時間**: 60分

---

## フェーズ 3: データアクセスレイヤー

### 3.1 DAO実装

- [ ] [P] **タスク 3.1.1**: BookDaoの実装
  - BookDaoクラスの作成 (@ApplicationScoped)
  - EntityManagerの注入 (@PersistenceContext)
  - findById()の実装
  - findAll()の実装
  - findByCategory()の実装
  - findByKeyword()の実装
  - findByCategoryAndKeyword()の実装
  - findByCriteriaAPI()の実装（動的クエリ）
  - **想定時間**: 90分

- [ ] [P] **タスク 3.1.2**: CategoryDaoの実装
  - CategoryDaoクラスの作成
  - findAll()の実装
  - findById()の実装
  - **想定時間**: 30分

- [ ] [P] **タスク 3.1.3**: StockDaoの実装
  - StockDaoクラスの作成
  - findById()の実装
  - update()の実装（楽観的ロック処理付き）
  - バージョン競合検出のテスト
  - **想定時間**: 45分

- [ ] [P] **タスク 3.1.4**: CustomerDaoの実装
  - CustomerDaoクラスの作成
  - findById()の実装
  - findByEmail()の実装（ログイン用）
  - persist()の実装（登録用）
  - emailExists()の実装（重複チェック）
  - **想定時間**: 60分

- [ ] [→] **タスク 3.1.5**: OrderTranDaoの実装
  - OrderTranDaoクラスの作成
  - persist()の実装
  - findById()の実装
  - findByIdWithDetails()の実装（JOIN FETCH）
  - findByCustomerId()の実装（注文履歴）
  - findOrderHistoryByCustomerId()の実装（DTOプロジェクション付き）
  - **想定時間**: 90分

- [ ] [→] **タスク 3.1.6**: OrderDetailDaoの実装
  - OrderDetailDaoクラスの作成
  - persist()の実装
  - findById()の実装（複合キー）
  - findByOrderTranId()の実装
  - **想定時間**: 45分

---

## フェーズ 4: ビジネスロジックレイヤー

### 4.1 サービス実装

- [ ] [P] **タスク 4.1.1**: BookServiceの実装
  - BookServiceクラスの作成 (@ApplicationScoped)
  - BookDaoの注入 (@Inject)
  - getBook(bookId)の実装
  - getBooksAll()の実装
  - searchBook(categoryId)の実装
  - searchBook(keyword)の実装
  - searchBook(categoryId, keyword)の実装
  - searchBookWithCriteria()の実装（動的）
  - ログ追加（メソッドエントリポイント）
  - **想定時間**: 90分

- [ ] [P] **タスク 4.1.2**: CategoryServiceの実装
  - CategoryServiceクラスの作成
  - CategoryDaoの注入
  - getAllCategories()の実装
  - getCategoryMap()の実装（ドロップダウン用）
  - **想定時間**: 30分

- [ ] [P] **タスク 4.1.3**: CustomerServiceの実装
  - CustomerServiceクラスの作成
  - EmailAlreadyExistsExceptionの作成
  - CustomerDaoの注入
  - registerCustomer()の実装（重複チェック付き）
  - authenticate(email, password)の実装
  - getCustomer(customerId)の実装
  - getCustomerByEmail()の実装
  - ログとバリデーションの追加
  - **想定時間**: 75分

- [ ] [P] **タスク 4.1.4**: DeliveryFeeServiceの実装
  - DeliveryFeeServiceクラスの作成
  - calculateDeliveryFee(address, totalPrice)の実装
  - isOkinawa(address)の実装
  - isFreeDelivery(totalPrice)の実装
  - ビジネスルール定数の追加
  - 配送料計算ロジックのテスト
  - **想定時間**: 45分

- [ ] [→] **タスク 4.1.5**: OrderServiceの実装
  - OrderServiceIFインターフェースの作成
  - OrderServiceクラスの作成 (@Transactional)
  - OutOfStockExceptionの作成
  - OrderTO, OrderHistoryTO, OrderSummaryTOの作成
  - OrderTranDao, OrderDetailDao, StockDao, BookDaoの注入
  - getOrderHistory()の実装（3バリエーション）
  - getOrderTran()の実装
  - getOrderTranWithDetails()の実装
  - getOrderDetail()の実装
  - getOrderDetails()の実装
  - orderBooks()の実装（メインビジネスロジック）:
    - 在庫可用性チェック
    - 楽観的ロック付き在庫更新
    - OrderTran作成
    - OrderDetail作成
    - 例外ハンドリング
  - 全メソッドへのログ追加
  - **想定時間**: 180分（3時間）

---

## フェーズ 5: プレゼンテーションレイヤー

### 5.1 共通ユーティリティ

- [ ] [P] **タスク 5.1.1**: MessageUtilの実装
  - MessageUtilクラスの作成
  - messages.propertiesの読み込み
  - get(key)メソッドの実装
  - messages.propertiesファイルの作成（エラーメッセージ）
  - **想定時間**: 30分

- [ ] [P] **タスク 5.1.2**: SettlementType Enumの実装
  - SettlementType enumの作成
  - コードの定義（1:銀行振込, 2:クレジット, 3:着払い）
  - fromCode()メソッドの実装
  - getDisplayNameByCode()メソッドの実装
  - getAllCodes()メソッドの実装
  - **想定時間**: 30分

- [ ] [P] **タスク 5.1.3**: AddressUtilの実装
  - AddressUtilクラスの作成
  - 住所パース/バリデーションロジックの実装（必要な場合）
  - **想定時間**: 20分

### 5.2 セッション管理

- [ ] **タスク 5.2.1**: CartItemの実装
  - CartItemクラスの作成（DTO）
  - フィールドの追加: bookId, bookName, publisherName, price, count, version
  - 削除用のremoveフラグの追加
  - Serializableの実装
  - **想定時間**: 20分

- [ ] [→] **タスク 5.2.2**: CartSessionの実装
  - CartSessionクラスの作成 (@SessionScoped)
  - フィールドの追加: cartItems, totalPrice, deliveryPrice, deliveryAddress
  - clear()メソッドの実装
  - Serializableの実装
  - **想定時間**: 30分

- [ ] **タスク 5.2.3**: CustomerBeanの実装（セッション）
  - CustomerBeanクラスの作成 (@SessionScoped)
  - フィールドの追加: Customer customer
  - CustomerServiceの注入
  - register()メソッドの実装
  - isLoggedIn()メソッドの実装
  - logout()メソッドの実装
  - Serializableの実装
  - **想定時間**: 45分

- [ ] [→] **タスク 5.2.4**: LoginBeanの実装
  - LoginBeanクラスの作成 (@SessionScoped)
  - フィールドの追加: email, password
  - CustomerService, CustomerBeanの注入
  - login()メソッドの実装
  - logout()メソッドの実装
  - ナビゲーションロジックの追加
  - Serializableの実装
  - **想定時間**: 45分

### 5.3 Managed Bean

- [ ] [P] **タスク 5.3.1**: BookSearchBeanの実装
  - BookSearchBeanクラスの作成 (@SessionScoped)
  - BookService, CategoryServiceの注入
  - フィールドの追加: categoryId, keyword, bookList, categoryMap
  - @PostConstruct init()メソッドの実装
  - search()メソッドの実装（静的クエリ）
  - search2()メソッドの実装（動的クエリ）
  - loadAllBooks()メソッドの実装
  - refreshBookList()メソッドの実装
  - ログの追加
  - Serializableの実装
  - **想定時間**: 90分

- [ ] [P] **タスク 5.3.2**: CartBeanの実装
  - CartBeanクラスの作成 (@SessionScoped)
  - BookService, StockDao, CartSession, CustomerBean, DeliveryFeeServiceの注入
  - addBook(bookId, count)メソッドの実装
  - removeSelectedBooks()メソッドの実装
  - clearCart()メソッドの実装
  - proceedToOrder()メソッドの実装（配送料計算付き）
  - viewCart()メソッドの実装
  - エラーメッセージハンドリングの追加
  - Serializableの実装
  - **想定時間**: 120分

- [ ] [→] **タスク 5.3.3**: OrderBeanの実装
  - OrderBeanクラスの作成 (@ViewScoped)
  - OrderService, CartSession, CustomerBeanの注入
  - フィールドの追加: orderTran, orderList
  - placeOrder()メソッドの実装:
    - CartSessionからOrderTOを作成
    - OrderService.orderBooks()を呼び出し
    - OutOfStockExceptionの処理
    - OptimisticLockExceptionの処理
    - 成功時にカートをクリア
  - loadOrderHistory()メソッドの実装
  - getOrderDetail()メソッドの実装
  - ナビゲーションロジックの追加
  - エラーメッセージハンドリングの追加
  - Serializableの実装
  - **想定時間**: 120分

### 5.4 セキュリティフィルター

- [ ] **タスク 5.4.1**: AuthenticationFilterの実装
  - AuthenticationFilterクラスの作成 (@WebFilter)
  - urlPatternsの追加: "*.xhtml"
  - publicPagesリストの定義 (index.xhtml, customerInput.xhtml, customerOutput.xhtml)
  - doFilter()メソッドの実装:
    - リクエストURIの抽出
    - 公開ページかチェック
    - セッションにCustomerBeanがあるかチェック
    - 未認証の場合ログインにリダイレクト
  - ログの追加
  - **想定時間**: 60分

---

## フェーズ 6: ビューレイヤー

### 6.1 CSSとリソース

- [ ] **タスク 6.1.1**: CSSスタイルシートの作成
  - webapp/resources/css/にstyle.cssを作成
  - テーマカラーの定義 (#CF3F4E - ストロベリーレッド)
  - テーブルのスタイル（書籍リスト、カート、注文履歴）
  - フォームのスタイル（入力フィールド、ボタン）
  - エラーメッセージのスタイル
  - レスポンシブデザインルールの追加
  - **想定時間**: 60分

- [ ] [P] **タスク 6.1.2**: 書籍カバー画像の追加
  - images/covers/ディレクトリの作成
  - no-image.jpgプレースホルダーの追加
  - 書籍カバー画像の追加（50枚）
  - **想定時間**: 30分

### 6.2 XHTMLページ

- [ ] [P] **タスク 6.2.1**: index.xhtmlの作成（ログインページ）
  - ログインフォームの作成（email, password）
  - 登録ページへのリンク追加
  - エラーメッセージ表示の追加
  - LoginBean.login()への接続
  - **想定時間**: 45分

- [ ] [P] **タスク 6.2.2**: customerInput.xhtmlの作成（登録ページ）
  - 登録フォームの作成（name, email, password, birthday, address）
  - バリデーションメッセージの追加
  - CustomerBean.register()への接続
  - customerOutput.xhtmlへのナビゲーション
  - **想定時間**: 60分

- [ ] [P] **タスク 6.2.3**: customerOutput.xhtmlの作成（登録成功）
  - 成功メッセージの表示
  - 登録顧客情報の表示
  - ログインページへのリンク追加
  - **想定時間**: 20分

- [ ] [→] **タスク 6.2.4**: bookSearch.xhtmlの作成（検索ページ）
  - 検索フォームの作成（カテゴリドロップダウン、キーワード入力）
  - 検索ボタンの追加（BookSearchBean.search()へ接続）
  - 「全書籍を表示」リンクの追加
  - ナビゲーションメニューの追加（ログアウト、注文履歴）
  - **想定時間**: 45分

- [ ] [→] **タスク 6.2.5**: bookSelect.xhtmlの作成（検索結果ページ）
  - 書籍リストテーブルの作成 (h:dataTable)
  - 表示: bookId, bookName, author, category, publisher, price, quantity
  - 行ごとに「カートへ」ボタンを追加（数量入力付き）
  - CartBean.addBook()への接続
  - ナビゲーションメニューの追加
  - 在庫切れ書籍の「カートへ」ボタンを無効化
  - **想定時間**: 75分

- [ ] [→] **タスク 6.2.6**: cartView.xhtmlの作成（カートページ）
  - カートアイテムテーブルの作成
  - 表示: bookName, publisherName, price, count
  - 削除用チェックボックスの追加
  - 「選択削除」ボタンの追加
  - 「カートをクリア」ボタンの追加
  - 合計金額の表示
  - 「注文手続きへ」ボタンの追加
  - CartBeanメソッドへの接続
  - 空カートエラーハンドリングの追加
  - **想定時間**: 75分

- [ ] [→] **タスク 6.2.7**: bookOrder.xhtmlの作成（注文入力ページ）
  - カートサマリーの表示（読み取り専用）
  - 配送先住所入力の追加（顧客からデフォルト）
  - 決済方法ラジオボタンの追加（1:銀行振込, 2:クレジット, 3:着払い）
  - 合計金額の表示
  - 配送料金の表示（自動計算）
  - 総合計の表示
  - 「注文確定」ボタンの追加
  - OrderBean.placeOrder()への接続
  - エラーメッセージ表示の追加（在庫不足、楽観的ロック）
  - **想定時間**: 90分

- [ ] [→] **タスク 6.2.8**: orderSuccess.xhtmlの作成（注文完了ページ）
  - 成功メッセージの表示
  - 注文IDの表示
  - 注文履歴へのリンク追加
  - 買い物を続けるリンク追加
  - **想定時間**: 30分

- [ ] [→] **タスク 6.2.9**: orderError.xhtmlの作成（注文エラーページ）
  - エラーメッセージの表示（OutOfStockException, OptimisticLockException）
  - カートへ戻るリンクの追加
  - 検索ページへのリンク追加
  - **想定時間**: 30分

- [ ] [→] **タスク 6.2.10**: orderHistory.xhtmlの作成（注文履歴ページ - バリエーション1）
  - 注文リストテーブルの作成 (h:dataTable)
  - 表示: orderTranId, orderDate, deliveryAddress, settlementType, totalPrice
  - 注文詳細ページへのリンク追加
  - OrderBean.loadOrderHistory()への接続
  - ナビゲーションメニューの追加
  - **想定時間**: 60分

- [ ] [P] **タスク 6.2.11**: orderHistory2.xhtmlの作成（注文履歴ページ - バリエーション2）
  - orderHistory.xhtmlと同じだがDTOプロジェクション使用
  - OrderBean.loadOrderHistory2()への接続
  - **想定時間**: 45分

- [ ] [P] **タスク 6.2.12**: orderHistory3.xhtmlの作成（注文履歴ページ - バリエーション3）
  - orderHistory.xhtmlと同じだがJOIN FETCH使用
  - OrderBean.loadOrderHistory3()への接続
  - **想定時間**: 45分

- [ ] [→] **タスク 6.2.13**: orderDetail.xhtmlの作成（注文詳細ページ）
  - 注文ヘッダーの表示（orderTranId, orderDate, deliveryAddress等）
  - 注文明細テーブルの作成（bookName, price, count, subtotal）
  - 配送料金の表示
  - 総合計の表示
  - OrderBean.getOrderDetail()への接続
  - 注文履歴へ戻るリンクの追加
  - **想定時間**: 60分

- [ ] [P] **タスク 6.2.14**: cartClear.xhtmlの作成（カートクリア確認）
  - カートクリアメッセージの表示
  - 検索ページへ戻るリンクの追加
  - **想定時間**: 15分

### 6.3 JSF設定

- [ ] **タスク 6.3.1**: faces-config.xmlの作成
  - ナビゲーションルールの設定（必要な場合）
  - マネージドBeanスキャンの設定
  - メッセージバンドルの設定
  - **想定時間**: 20分

- [ ] **タスク 6.3.2**: web.xmlの作成
  - Faces Servletの設定
  - サーブレットマッピングの設定 (*.xhtml, /faces/*)
  - JSFパラメータの設定 (PROJECT_STAGE, STATE_SAVING_METHOD)
  - セッションタイムアウトの設定（60分）
  - ウェルカムファイルの設定 (index.xhtml)
  - **想定時間**: 30分

- [ ] **タスク 6.3.3**: beans.xmlの作成
  - CDI Bean検出の有効化
  - bean-discovery-mode="all"の設定
  - **想定時間**: 10分

---

## フェーズ 7: テスト

### 7.1 ユニットテスト

- [ ] [P] **タスク 7.1.1**: BookServiceのテスト
  - BookDaoのモック
  - getBooksAll()のテスト
  - searchBook()メソッドのテスト
  - DAOメソッド呼び出しの検証
  - **想定時間**: 60分

- [ ] [P] **タスク 7.1.2**: OrderServiceのテスト
  - OrderTranDao, OrderDetailDao, StockDao, BookDaoのモック
  - orderBooks()のテスト - 成功ケース
  - orderBooks()のテスト - OutOfStockException
  - orderBooks()のテスト - OptimisticLockException
  - getOrderHistory()メソッドのテスト
  - **想定時間**: 120分

- [ ] [P] **タスク 7.1.3**: DeliveryFeeServiceのテスト
  - calculateDeliveryFee()のテスト - 標準（800円）
  - calculateDeliveryFee()のテスト - 沖縄（1700円）
  - calculateDeliveryFee()のテスト - 送料無料（5000円以上）
  - エッジケースのテスト（4999円、5000円、5001円）
  - **想定時間**: 45分

- [ ] [P] **タスク 7.1.4**: CustomerServiceのテスト
  - CustomerDaoのモック
  - registerCustomer()のテスト - 成功
  - registerCustomer()のテスト - EmailAlreadyExistsException
  - authenticate()のテスト - 成功
  - authenticate()のテスト - 失敗
  - **想定時間**: 60分

- [ ] [P] **タスク 7.1.5**: SettlementType Enumのテスト
  - fromCode()メソッドのテスト
  - getDisplayNameByCode()メソッドのテスト
  - 無効コードのテスト
  - **想定時間**: 30分

### 7.2 統合テスト（手動）

- [ ] **タスク 7.2.1**: エンドツーエンド注文フローのテスト
  - テストユーザーとしてログイン
  - 書籍検索
  - カートに書籍を追加
  - 注文手続きへ進む
  - 配送先住所の入力
  - 決済方法の選択
  - 注文確定
  - 注文完了の確認
  - 注文履歴の確認
  - **想定時間**: 60分

- [ ] **タスク 7.2.2**: 同時注文シナリオのテスト
  - 2つのブラウザセッションを開く
  - 両セッションで同じ書籍をカートに追加（在庫限定）
  - セッション1で注文確定（成功すべき）
  - セッション2で注文確定（OptimisticLockExceptionで失敗すべき）
  - セッション2でエラーメッセージを確認
  - **想定時間**: 30分

- [ ] **タスク 7.2.3**: 認証フローのテスト
  - ログイン成功のテスト
  - ログイン失敗のテスト
  - 保護ページへのアクセステスト（リダイレクトすべき）
  - ログアウトのテスト
  - セッションタイムアウトのテスト
  - **想定時間**: 45分

- [ ] **タスク 7.2.4**: 登録フローのテスト
  - 新規顧客登録（成功）
  - 重複メール登録の試行（失敗すべき）
  - 登録後のログイン確認
  - **想定時間**: 30分

---

## フェーズ 8: デプロイとドキュメント

### 8.1 ビルド構成

- [ ] **タスク 8.1.1**: Gradle WARタスクの設定
  - archiveFileNameの設定
  - webAppDirectoryの設定
  - WAR生成のテスト
  - **想定時間**: 20分

- [ ] **タスク 8.1.2**: デプロイスクリプトの作成
  - build.gradleにdeployタスクを作成
  - undeployタスクの作成
  - setupHsqldbタスクの作成
  - Payaraへのデプロイテスト
  - **想定時間**: 45分

### 8.2 ドキュメント

- [ ] [P] **タスク 8.2.1**: README.mdの作成
  - プロジェクト概要の記述
  - セットアップ手順の記述
  - Gradleコマンドの記述
  - アクセスURLの記述
  - ログイン認証情報の記述
  - **想定時間**: 60分

- [ ] [P] **タスク 8.2.2**: アーキテクチャのドキュメント化
  - アーキテクチャ図の作成
  - パッケージ構造の記述
  - 使用デザインパターンの記述
  - **想定時間**: 45分

- [ ] [P] **タスク 8.2.3**: APIドキュメントの作成
  - ServiceレイヤーのJavaDoc
  - DAOレイヤーのJavaDoc
  - ビジネスルールの記述
  - **想定時間**: 60分

### 8.3 最終検証

- [ ] **タスク 8.3.1**: コードレビュー
  - 全コードの一貫性レビュー
  - ログカバレッジの確認
  - エラーハンドリングの検証
  - NULL安全性の確認
  - **想定時間**: 90分

- [ ] **タスク 8.3.2**: パフォーマンステスト
  - 50同時ユーザーでのテスト
  - レスポンスタイム3秒未満の確認
  - データベースコネクションプールの確認
  - メモリ使用量の監視
  - **想定時間**: 60分

- [ ] **タスク 8.3.3**: セキュリティ監査
  - 全ページでの認証確認
  - セッション管理の確認
  - 入力検証の確認
  - パスワード保存の確認（平文の制限を認識）
  - **想定時間**: 45分

---

## サマリー

### 総想定時間
- **フェーズ 1**: インフラストラクチャセットアップ - 2時間
- **フェーズ 2**: 永続化レイヤー - 7時間
- **フェーズ 3**: データアクセスレイヤー - 6時間
- **フェーズ 4**: ビジネスロジックレイヤー - 8時間
- **フェーズ 5**: プレゼンテーションレイヤー - 12時間
- **フェーズ 6**: ビューレイヤー - 12時間
- **フェーズ 7**: テスト - 6.5時間
- **フェーズ 8**: デプロイとドキュメント - 6時間

**総計**: 約60時間（単独開発者で8営業日）

### クリティカルパス
```
インフラストラクチャセットアップ → データベーススキーマ → JPAエンティティ → DAO → 
サービス → セッション管理 → Managed Bean → ビュー → テスト → デプロイ
```

### 並列実行の機会
- フェーズ 2.1: 全テーブル作成タスクを並列実行可能
- フェーズ 2.2: エンティティ実装を並列実行可能（OrderDetailはOrderTranに依存を除く）
- フェーズ 3.1: 全DAO実装を並列実行可能（OrderDetailDaoはOrderTranDaoに依存を除く）
- フェーズ 4.1: サービス実装を並列実行可能（OrderServiceは他に依存を除く）
- フェーズ 6.2: 多くのXHTMLページを並列開発可能

### リスク軽減
- **ハイリスクタスク**:
  - タスク 4.1.5 (OrderService) - 最も複雑なビジネスロジック
  - タスク 5.3.3 (OrderBean) - 複雑な例外処理
  - タスク 6.2.7 (bookOrder.xhtml) - 計算を含む複雑なUI
  
- **軽減戦略**:
  - ハイリスクタスクに追加時間を割り当て
  - 統合前に包括的なユニットテストを実装
  - 手動テスト用の詳細なテストシナリオを作成

---

**ドキュメント終了**

*このタスク分解は、spec.md（何を）とplan.md（どのように）から導出された構造化実装計画を提供します。タスクは依存関係に応じて、並列化の機会を考慮しながら順次実行されるように設計されています。*

