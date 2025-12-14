# berry-books - 振る舞い仕様書（Acceptance Criteria）

**プロジェクトID:** berry-books  
**バージョン:** 1.1.0  
**Last Updated:** 2025-12-14  
**ステータス:** 受入基準詳細化

---

## 概要

このドキュメントは、berry-booksシステムの外形的な振る舞いを定義する。
各シナリオはGiven-When-Then形式で記述され、ブラックボックステストの基礎となる。

**関連ドキュメント:**
- [requirements.md](requirements.md) - ビジネス要件（What & Why）
- [functional_design.md](functional_design.md) - 機能設計（Flows & UI）
- [architecture_design.md](architecture_design.md) - アーキテクチャ設計
- [data_model.md](data_model.md) - データモデル
- [tasks.md](tasks.md) - 実装タスク

---

## 画面マッピング

このドキュメントで参照する画面とファイル名の対応：

| 画面ID | 画面名 | ファイル名 | 認証要否 |
|--------|--------|-----------|---------|
| SC-001 | ログイン画面 | index.xhtml | 不要 |
| SC-002 | 新規登録画面 | customerInput.xhtml | 不要 |
| SC-003 | 登録完了画面 | customerOutput.xhtml | 不要 |
| SC-004 | 書籍検索画面 | bookSearch.xhtml | 必要 |
| SC-005 | 検索結果画面 | bookSelect.xhtml | 必要 |
| SC-006 | カート画面 | cartView.xhtml | 必要 |
| SC-007 | 注文入力画面 | bookOrder.xhtml | 必要 |
| SC-008 | 注文完了画面 | orderSuccess.xhtml | 必要 |
| SC-009 | 注文エラー画面 | orderError.xhtml | 必要 |
| SC-010 | 注文履歴画面 | orderHistory.xhtml | 必要 |
| SC-011 | 注文詳細画面 | orderDetail.xhtml | 必要 |

---

## 機能 3.1: 書籍検索・閲覧

**画面:** SC-004 (bookSearch.xhtml) → SC-005 (bookSelect.xhtml)  
**Managed Bean:** BookSearchBean

### Scenario: カテゴリで書籍を絞り込む

```gherkin
Given ユーザーが書籍検索画面（bookSearch.xhtml）にアクセスしている
When ユーザーがカテゴリドロップダウンから「Java」を選択して検索する
Then 検索結果画面（bookSelect.xhtml）に遷移する
And Java カテゴリの書籍一覧がh:dataTableで表示される
And 各書籍には以下の情報が表示される
  | フィールド | データソース |
  | 書籍ID | Book.bookId |
  | 書籍名 | Book.bookName |
  | 著者 | Book.author |
  | カテゴリ | Category.categoryName |
  | 出版社 | Publisher.publisherName |
  | 価格 | Book.price |
  | 在庫数 | Stock.quantity |
And 検索結果は書籍ID昇順（BR-003）でソートされる
```

**受入基準:**
- [ ] カテゴリドロップダウンに全カテゴリが表示される
- [ ] 選択されたカテゴリの書籍のみが表示される
- [ ] 書籍リストが空の場合、「該当する書籍がありません」メッセージが表示される

### Scenario: キーワードで書籍を検索する

```gherkin
Given ユーザーが書籍検索画面（bookSearch.xhtml）にアクセスしている
When ユーザーがキーワードフィールドに「SpringBoot」を入力して検索する
Then 検索結果画面（bookSelect.xhtml）に遷移する
And 書籍名（Book.bookName）または著者（Book.author）に「SpringBoot」を含む書籍一覧が表示される
And 検索は大文字小文字を区別しない（LIKE検索）
```

**受入基準:**
- [ ] 部分一致検索が機能する（例: "SpringBoot" → "SpringBoot in Cloud", "SpringBootによるエンタープライズ開発"）
- [ ] 書籍名と著者名の両方が検索対象となる（BR-002）
- [ ] カテゴリが未選択の場合、全カテゴリが対象となる（BR-001）

### Scenario: カテゴリとキーワードを組み合わせて検索する

```gherkin
Given ユーザーが書籍検索画面（bookSearch.xhtml）にアクセスしている
When ユーザーがカテゴリ「SpringBoot」とキーワード「Cloud」を入力して検索する
Then SpringBoot カテゴリかつ「Cloud」を含む書籍のみが表示される
And 検索結果はBookDao.findByCategoryAndKeyword()で取得される
```

**受入基準:**
- [ ] カテゴリとキーワードの両方の条件がAND条件で適用される
- [ ] WHERE句が正しく構築される: `WHERE category_id = ? AND (book_name LIKE ? OR author LIKE ?)`

### Scenario: 在庫なしの書籍も表示される

```gherkin
Given ユーザーが検索結果画面（bookSelect.xhtml）を閲覧している
When 在庫数が0の書籍（Stock.quantity = 0）が存在する
Then その書籍も検索結果に表示される（BR-004）
And 在庫数カラムに「0」が表示される
But 「カートへ」ボタンが無効化される（disabled属性）
And ボタンのスタイルがグレーアウトされる
```

**受入基準:**
- [ ] 在庫0の書籍が検索結果から除外されない
- [ ] 在庫0の書籍の「カートへ」ボタンがクリック不可
- [ ] ユーザーが在庫状況を視覚的に判断できる

---

## 機能 3.2: ショッピングカート管理

**画面:** SC-005 (bookSelect.xhtml) → SC-006 (cartView.xhtml)  
**Managed Bean:** CartBean  
**Session Bean:** CartSession

### Scenario: 書籍をカートに追加する

```gherkin
Given ユーザーが検索結果画面（bookSelect.xhtml）で書籍を閲覧している
And 書籍の在庫数（Stock.quantity）が2以上である
When ユーザーが書籍の数量フィールドに「2」を入力する
And 「カートへ」ボタンをクリックする
Then CartBean.addBook(bookId, count)が呼び出される
And CartItemがCartSession.cartItemsに追加される
And カートアイテムに以下の情報が保存される
  | フィールド | 値 |
  | bookId | 選択した書籍のID |
  | bookName | 書籍名 |
  | publisherName | 出版社名 |
  | price | 単価 |
  | count | 2 |
  | version | 在庫のVERSION値（BR-012） |
And カート画面（cartView.xhtml）に遷移する
And カートに選択した書籍が数量「2」で表示される
And 小計（price × count）が表示される
And 合計金額（CartSession.totalPrice）が自動計算される（BR-013）
```

**受入基準:**
- [ ] カート追加時に在庫バージョン番号が保存される（楽観的ロック用）
- [ ] 数量が0以下の場合、検証エラー「VAL-004: 数量は1以上を入力してください」が表示される
- [ ] カートアイテムがセッションスコープで保持される（BR-010）

### Scenario: カート内の書籍を削除する

```gherkin
Given ユーザーがカート画面（cartView.xhtml）でカート内に複数の書籍がある
When ユーザーが特定の書籍の削除チェックボックスをチェックする
And 「選択削除」ボタンをクリックする
Then CartBean.removeSelectedBooks()が呼び出される
And チェックされた書籍（CartItem.remove = true）がCartSession.cartItemsから削除される
And 合計金額（CartSession.totalPrice）が再計算される
And カート画面が更新される
```

**受入基準:**
- [ ] 複数の書籍を同時に削除できる
- [ ] 削除後、カートが空になった場合、「カートが空です」メッセージが表示される
- [ ] 削除操作はセッションに即座に反映される

### Scenario: カート全体をクリアする

```gherkin
Given ユーザーがカート画面（cartView.xhtml）でカート内に書籍がある
When ユーザーが「カートをクリア」ボタンをクリックする
Then CartBean.clearCart()が呼び出される
And CartSession.clear()が実行される
And 全ての書籍がCartSession.cartItemsから削除される
And 合計金額が0円になる
And カートクリア確認画面（cartClear.xhtml）に遷移する
```

**受入基準:**
- [ ] ワンクリックで全てのアイテムが削除される
- [ ] 確認ダイアログなしで即座にクリアされる
- [ ] クリア後のメッセージが表示される

### Scenario: 同じ書籍を複数回追加する

```gherkin
Given ユーザーがカートに書籍A（bookId=1、数量2）を追加済み
When ユーザーが検索結果画面から再度書籍A（数量3）をカートに追加する
Then CartBean.addBook()が既存のCartItemを検出する（BR-011）
And 既存のCartItemのcount値が「2 + 3 = 5」に更新される
And 小計（price × 5）が再計算される
And 合計金額が更新される
And カート画面に遷移する
And 書籍Aが1行のみ表示される（重複行なし）
And 数量カラムに「5」が表示される
```

**受入基準:**
- [ ] 同じ書籍IDのアイテムが重複して追加されない
- [ ] 数量が正しく加算される
- [ ] バージョン番号は最新のもので上書きされる

---

## 機能 3.3: 注文処理

**画面:** SC-006 (cartView.xhtml) → SC-007 (bookOrder.xhtml) → SC-008 (orderSuccess.xhtml)  
**Managed Bean:** OrderBean  
**Service:** OrderService (@Transactional)

### Scenario: 注文を確定する（正常系）

```gherkin
Given ユーザーがカート画面（cartView.xhtml）でカート内に書籍がある
And 「注文手続きへ」ボタンをクリックし注文画面（bookOrder.xhtml）に遷移している
And 全ての書籍の在庫（Stock.quantity）が注文数以上である
When ユーザーが配送先住所フィールドに「東京都渋谷区神南1-1-1」を入力する
And 決済方法ラジオボタンで「クレジットカード」（code=2）を選択する
And 配送料金「800円」が自動計算される（BR-020）
And 「注文確定」ボタンをクリックする
Then OrderBean.placeOrder()が呼び出される
And OrderTOが作成される
  | フィールド | 値 |
  | customerId | セッションの顧客ID |
  | deliveryAddress | 東京都渋谷区神南1-1-1 |
  | deliveryPrice | 800 |
  | settlementCode | 2 |
  | cartItems | CartSession.cartItems |
And OrderService.orderBooks(orderTO)が@Transactionalで実行される
And 在庫チェック（BR-022）が行われる
And 楽観的ロックで在庫が更新される（BR-024）
  - UPDATE STOCK SET quantity = ? WHERE book_id = ? AND version = ?
And VERSIONカラムが自動インクリメントされる
And OrderTranエンティティが作成される
And OrderDetailエンティティが作成される
And トランザクションがコミットされる（BR-025）
And 注文IDが発行される（OrderTran.orderTranId）
And CartSession.clear()が呼び出される
And 注文完了画面（orderSuccess.xhtml）に遷移する
And 注文IDが表示される
```

**受入基準:**
- [ ] トランザクションが正常にコミットされる
- [ ] 在庫数が注文数分減少する
- [ ] カートが完全にクリアされる
- [ ] 注文IDが画面に表示される
- [ ] 注文履歴に新しい注文が追加される

### Scenario: 在庫不足エラー（異常系）

```gherkin
Given ユーザーがカートに書籍A（bookId=1、数量5）を追加している
And 書籍Aの現在の在庫数（Stock.quantity）が「3」である
When ユーザーが注文画面（bookOrder.xhtml）で注文確定ボタンをクリックする
Then OrderService.orderBooks()内で在庫チェックが行われる
And 在庫数（3）< 注文数（5）が検出される
And OutOfStockExceptionがスローされる
And OrderBeanで例外がキャッチされる
And FacesMessageが追加される: 「BIZ-003: 在庫が不足しています」
And 注文エラー画面（orderError.xhtml）に遷移する
And エラーメッセージが表示される
And 注文トランザクションはロールバックされる
And カートはクリアされない
And ユーザーはカート画面に戻ることができる
```

**受入基準:**
- [ ] 在庫チェックが注文確定時（BR-022）に行われる
- [ ] OutOfStockExceptionが適切にハンドリングされる
- [ ] エラーメッセージ「BIZ-003」が表示される
- [ ] トランザクションがロールバックされる
- [ ] カート内容が保持される

### Scenario: 同時注文による競合（楽観的ロック）

```gherkin
Given ユーザーA（セッションA）とユーザーB（セッションB）が同じ書籍（bookId=1）をカートに追加している
And 書籍の現在の在庫数が5、VERSIONが1である
And ユーザーAのカートアイテムのversion値が1である
And ユーザーBのカートアイテムのversion値が1である
And ユーザーAが数量3、ユーザーBが数量3を注文しようとしている
When ユーザーAが先に注文確定ボタンをクリックする
Then ユーザーAの注文が成功する
And 在庫が5→2に減少する
And VERSIONが1→2にインクリメントされる
And ユーザーAのトランザクションがコミットされる
When ユーザーBが後から注文確定ボタンをクリックする
Then OrderService.orderBooks()内で在庫更新が実行される
And UPDATE文のWHERE条件「version = 1」が一致しない（現在version=2）
And 更新行数が0となる
And OptimisticLockExceptionがスローされる（BR-024）
And OrderBeanで例外がキャッチされる
And FacesMessageが追加される: 「BIZ-004: 他のユーザーが購入済みです。カートを確認してください」
And 注文エラー画面（orderError.xhtml）に遷移する
And エラーメッセージが表示される
And ユーザーBのトランザクションはロールバックされる
And ユーザーBの注文は確定されない
And ユーザーBのカートはクリアされない
And ユーザーBはカート画面に戻って数量を調整できる
```

**受入基準:**
- [ ] 楽観的ロック制御（BR-024）が正常に機能する
- [ ] カート追加時のバージョン番号（BR-012）が保存される
- [ ] バージョン不一致時にOptimisticLockExceptionが発生する
- [ ] エラーメッセージ「BIZ-004」が表示される
- [ ] 先着ユーザーの注文のみが成功する
- [ ] 後続ユーザーはカート内容を保持したまま調整できる

### Scenario: 配送料金の自動計算（通常）

```gherkin
Given ユーザーがカート画面で購入金額3000円のカートを確認している
When ユーザーが「注文手続きへ」ボタンをクリックする
And 注文画面（bookOrder.xhtml）で配送先住所フィールドに「東京都渋谷区」を入力する
Then DeliveryFeeService.calculateDeliveryFee()が呼び出される
And 購入金額3000円 < 5000円であることを確認
And 住所が「沖縄県」で始まらないことを確認
And 配送料金「800円」が返される（BR-020）
And 画面に配送料金「800円」が表示される
And 総合計「3000 + 800 = 3800円」が表示される
```

**受入基準:**
- [ ] 購入金額が5000円未満の場合、通常配送料800円が適用される
- [ ] 配送料金が自動計算される
- [ ] 総合計 = 購入金額 + 配送料金

### Scenario: 沖縄県への配送料金

```gherkin
Given ユーザーがカート画面で購入金額3000円のカートを確認している
When ユーザーが注文画面（bookOrder.xhtml）で配送先住所フィールドに「沖縄県那覇市」を入力する
Then DeliveryFeeService.calculateDeliveryFee()が呼び出される
And 購入金額3000円 < 5000円であることを確認
And 住所が「沖縄県」で始まることを検出
And 配送料金「1700円」が返される（BR-020）
And 画面に配送料金「1700円」が表示される
And 総合計「3000 + 1700 = 4700円」が表示される
```

**受入基準:**
- [ ] 配送先住所が「沖縄県」で始まる場合、配送料1700円が適用される
- [ ] isOkinawa()メソッドが正しく判定する
- [ ] 沖縄料金は購入金額に関わらず適用される（5000円未満の場合）

### Scenario: 送料無料（5000円以上）

```gherkin
Given ユーザーがカート画面で購入金額5500円のカートを確認している
When ユーザーが注文画面（bookOrder.xhtml）で配送先住所フィールドに「東京都渋谷区」を入力する
Then DeliveryFeeService.calculateDeliveryFee()が呼び出される
And 購入金額5500円 >= 5000円であることを確認
And 配送料金「0円」が返される（BR-020: 送料無料）
And 画面に配送料金「0円（送料無料）」が表示される
And 総合計「5500 + 0 = 5500円」が表示される
```

**受入基準:**
- [ ] 購入金額が5000円以上の場合、配送料が0円になる
- [ ] isFreeDelivery()メソッドが正しく判定する
- [ ] 総合計が購入金額と一致する

### Scenario: 送料無料（5000円以上、沖縄県）

```gherkin
Given ユーザーがカート画面で購入金額5500円のカートを確認している
When ユーザーが注文画面（bookOrder.xhtml）で配送先住所フィールドに「沖縄県那覇市」を入力する
Then DeliveryFeeService.calculateDeliveryFee()が呼び出される
And 購入金額5500円 >= 5000円であることを確認
And 配送料金「0円」が返される（BR-020: 5000円以上は沖縄でも送料無料）
And 画面に配送料金「0円（送料無料）」が表示される
And 総合計「5500円」が表示される
```

**受入基準:**
- [ ] 沖縄県でも5000円以上なら送料無料が適用される
- [ ] 配送料金計算の優先順位が正しい（送料無料 > 沖縄料金 > 通常料金）

---

## 機能 3.4: 顧客管理・認証

**画面:** SC-001 (index.xhtml) → SC-002 (customerInput.xhtml) → SC-003 (customerOutput.xhtml)  
**Managed Bean:** CustomerBean, LoginBean  
**Service:** CustomerService

### Scenario: 新規顧客登録（正常系）

```gherkin
Given ユーザーがログイン画面（index.xhtml）にアクセスしている
When ユーザーが「新規登録」リンクをクリックする
Then 新規登録画面（customerInput.xhtml）に遷移する
When ユーザーが以下の情報を入力フォームに入力する
  | フィールド | 値 | エンティティマッピング |
  | 顧客名 | Alice | Customer.customerName |
  | メールアドレス | alice@gmail.com | Customer.email |
  | パスワード | password123 | Customer.password |
  | 生年月日 | 1990-01-01 | Customer.birthday |
  | 住所 | 東京都渋谷区神南1-1-1 | Customer.address |
And 「登録」ボタンをクリックする
Then CustomerBean.register()が呼び出される
And CustomerService.registerCustomer()が呼び出される
And CustomerDao.findByEmail("alice@gmail.com")で重複チェックが行われる
And 重複がないことを確認
And Customerエンティティが作成される
And CustomerDao.persist(customer)でデータベースに保存される
And 登録完了画面（customerOutput.xhtml）に遷移する
And 登録された顧客情報が表示される
And ログイン画面へのリンクが表示される
```

**受入基準:**
- [ ] 全ての必須フィールドが入力されている場合のみ登録可能
- [ ] メールアドレスが一意性チェックされる（BR-030）
- [ ] パスワードが平文で保存される（BR-031、学習用のみ）
- [ ] 登録後、顧客IDが自動採番される

### Scenario: メールアドレス重複エラー（異常系）

```gherkin
Given データベースのCUSTOMERテーブルに以下のレコードが存在する
  | email | customerName |
  | alice@gmail.com | Alice |
When ユーザーが新規登録画面（customerInput.xhtml）で同じメールアドレス「alice@gmail.com」を入力する
And 「登録」ボタンをクリックする
Then CustomerService.registerCustomer()内でCustomerDao.findByEmail()が実行される
And 既存の顧客レコードが検出される
And EmailAlreadyExistsExceptionがスローされる
And CustomerBeanで例外がキャッチされる
And FacesMessageが追加される: 「BIZ-001: メールアドレスが既に登録されています」
And エラーメッセージが新規登録画面に表示される
And アカウントは作成されない
And 入力フォームは保持される
```

**受入基準:**
- [ ] メールアドレスの一意性制約（BR-030）が検証される
- [ ] EmailAlreadyExistsExceptionが適切にハンドリングされる
- [ ] エラーメッセージ「BIZ-001」が表示される
- [ ] ユーザーが再入力できる

### Scenario: ログイン（正常系）

```gherkin
Given データベースに以下の顧客レコードが存在する
  | customerId | email | password | customerName |
  | 1 | alice@gmail.com | password123 | Alice |
When ユーザーがログイン画面（index.xhtml）でメールアドレス「alice@gmail.com」を入力する
And パスワードフィールドに「password123」を入力する
And 「ログイン」ボタンをクリックする
Then LoginBean.login()が呼び出される
And CustomerService.authenticate("alice@gmail.com", "password123")が呼び出される
And CustomerDao.findByEmail()でCustomerエンティティを取得
And パスワードが一致することを確認（平文比較、BR-031）
And CustomerエンティティがCustomerBeanに保存される（@SessionScoped）
And ログインが成功する
And 書籍検索画面（bookSearch.xhtml）に遷移する
And セッションにログイン状態が保存される
```

**受入基準:**
- [ ] 正しい認証情報でログインできる
- [ ] Customerエンティティがセッションスコープで保持される（BR-034）
- [ ] ログイン後は認証が必要なページにアクセスできる
- [ ] セッションタイムアウトは60分（BR-032）

### Scenario: ログイン失敗（異常系）

```gherkin
Given データベースに顧客「alice@gmail.com」（password: "password123"）が存在する
When ユーザーがログイン画面（index.xhtml）でメールアドレス「alice@gmail.com」を入力する
And パスワードフィールドに誤ったパスワード「wrongpassword」を入力する
And 「ログイン」ボタンをクリックする
Then LoginBean.login()が呼び出される
And CustomerService.authenticate()でパスワードが一致しない
And nullが返される
And FacesMessageが追加される: 「BIZ-002: メールアドレスまたはパスワードが正しくありません」
And エラーメッセージがログイン画面に表示される
And ログインは成功しない
And ログイン画面に留まる
```

**受入基準:**
- [ ] 誤ったパスワードでログインできない
- [ ] エラーメッセージ「BIZ-002」が表示される
- [ ] セキュリティのため、メールアドレスとパスワードのどちらが誤っているか特定しない
- [ ] ログイン試行回数の制限はない（学習用簡易実装）

### Scenario: 未ログインユーザーのアクセス制限

```gherkin
Given ユーザーがログインしていない（セッションにCustomerBeanが存在しない）
When ユーザーがブラウザで直接URL「/bookSearch.xhtml」にアクセスしようとする
Then AuthenticationFilterが起動する
And リクエストURIが「bookSearch.xhtml」であることを検出
And publicPagesリストをチェック（index.xhtml, customerInput.xhtml, customerOutput.xhtml）
And 「bookSearch.xhtml」が公開ページではないことを確認
And セッションからCustomerBeanを取得
And CustomerBeanが存在しないまたはCustomerBean.isLoggedIn()がfalseであることを確認
And response.sendRedirect("/berry-books/index.xhtml")が実行される
And ログイン画面（index.xhtml）にリダイレクトされる
And ユーザーは書籍検索画面にアクセスできない
```

**受入基準:**
- [ ] 未認証ユーザーは保護ページにアクセスできない（BR-034）
- [ ] AuthenticationFilter（@WebFilter）が全てのxhtmlページで動作する
- [ ] 公開ページ（BR-033）は認証なしでアクセスできる
- [ ] リダイレクト後のURLが正しい

### Scenario: ログアウト

```gherkin
Given ユーザーがログイン済みの状態（CustomerBeanにCustomerが存在）
When ユーザーが書籍検索画面（bookSearch.xhtml）のナビゲーションメニューで「ログアウト」リンクをクリックする
Then LoginBean.logout()が呼び出される
And CustomerBean.logout()が呼び出される
And CustomerBeanのcustomerフィールドがnullに設定される
And CartSession.clear()が呼び出される
And HttpSession.invalidate()が実行される
And セッションが無効化される
And ログイン画面（index.xhtml）に遷移する
And ユーザーは再度ログインが必要な状態になる
```

**受入基準:**
- [ ] ログアウト後、セッションが完全に無効化される
- [ ] カート内容がクリアされる
- [ ] ログアウト後、保護ページにアクセスするとログイン画面にリダイレクトされる
- [ ] ブラウザの「戻る」ボタンを押してもログイン画面にリダイレクトされる

---

## 機能 3.5: 注文履歴参照

**画面:** SC-010 (orderHistory.xhtml) → SC-011 (orderDetail.xhtml)  
**Managed Bean:** OrderBean  
**Service:** OrderService

### Scenario: 注文履歴一覧を表示する

```gherkin
Given ユーザー（customerId=1）がログイン済み
And データベースに以下の注文が存在する
  | orderTranId | customerId | orderDate | totalPrice | deliveryPrice | settlementCode |
  | 1001 | 1 | 2025-12-01 | 3000 | 800 | 2 |
  | 1002 | 1 | 2025-12-05 | 5500 | 0 | 1 |
  | 1003 | 1 | 2025-12-10 | 4200 | 800 | 3 |
When ユーザーが書籍検索画面のナビゲーションメニューで「注文履歴」リンクをクリックする
Then 注文履歴画面（orderHistory.xhtml）に遷移する
And OrderBean.loadOrderHistory()が呼び出される
And OrderService.getOrderHistory(customerId=1)が呼び出される
And OrderTranDao.findByCustomerId(1)で注文を取得（BR-040）
And 注文日降順（BR-041）でソートされる
And 注文一覧がh:dataTableで表示される
And 各注文には以下の情報が表示される
  | フィールド | データソース | 例 |
  | 注文ID | OrderTran.orderTranId | 1003, 1002, 1001 |
  | 注文日 | OrderTran.orderDate | 2025-12-10 |
  | 配送先 | OrderTran.deliveryAddress | 東京都渋谷区... |
  | 決済方法 | SettlementType.getDisplayNameByCode(settlementCode) | 着払い |
  | 合計金額 | OrderTran.totalPrice + deliveryPrice | 5000円 |
```

**受入基準:**
- [ ] ログイン中の顧客の注文のみが表示される（BR-040）
- [ ] 注文が新しい順に表示される（BR-041）
- [ ] 決済方法コードが表示名に変換される（SettlementType.getDisplayNameByCode）
- [ ] 各注文行から注文詳細画面へのリンクがある

### Scenario: 注文詳細を表示する

```gherkin
Given ユーザーが注文履歴一覧（orderHistory.xhtml）を閲覧している
And 注文ID 1001の詳細情報が以下の通り存在する
  | 注文ヘッダー | 値 |
  | orderTranId | 1001 |
  | orderDate | 2025-12-01 |
  | deliveryAddress | 東京都渋谷区神南1-1-1 |
  | deliveryPrice | 800 |
  | settlementCode | 2 (クレジットカード) |
And 注文明細が以下の通り存在する
  | orderDetailId | bookName | price | count | subtotal |
  | 1 | Java SEディープダイブ | 3400 | 1 | 3400 |
  | 2 | SQLの冒険～RDBの深層 | 2200 | 1 | 2200 |
When ユーザーが注文ID 1001の行をクリックする
Then 注文詳細画面（orderDetail.xhtml）に遷移する
And OrderBean.getOrderDetail(orderTranId=1001)が呼び出される
And OrderService.getOrderDetail(1001)が呼び出される（BR-042）
And OrderTranDao.findByIdWithDetails(1001)でJOIN FETCHを使用して注文と明細を取得
And 注文ヘッダー情報が表示される
  - 注文ID: 1001
  - 注文日: 2025-12-01
  - 配送先: 東京都渋谷区神南1-1-1
  - 決済方法: クレジットカード
And 注文明細テーブル（h:dataTable）が表示される
And 各明細行に以下が表示される
  | 書籍名 | 単価 | 数量 | 小計 |
  | Java SEディープダイブ | 3400円 | 1 | 3400円 |
  | SQLの冒険～RDBの深層 | 2200円 | 1 | 2200円 |
And 配送料金「500円」が表示される
And 総合計「6100円」が表示される
And 「注文履歴へ戻る」リンクが表示される
```

**受入基準:**
- [ ] 注文IDで正しく注文詳細が取得される（BR-042）
- [ ] JOIN FETCHでN+1問題を回避する
- [ ] 全ての注文明細が表示される
- [ ] 合計金額の計算が正しい（明細合計 + 配送料金）

### Scenario: 注文履歴が空の場合

```gherkin
Given ユーザー（customerId=99）がログイン済み
And データベースにcustomerId=99の注文レコードが存在しない
When ユーザーが「注文履歴」メニューをクリックする
Then 注文履歴画面（orderHistory.xhtml）に遷移する
And OrderBean.loadOrderHistory()が呼び出される
And OrderService.getOrderHistory(customerId=99)が空のリストを返す
And 画面に「注文履歴はありません」というメッセージが表示される
And 注文テーブルは表示されない
And 「書籍検索へ」リンクが表示される
```

**受入基準:**
- [ ] 注文がない場合、エラーではなく案内メッセージが表示される
- [ ] 空のテーブルではなく、ユーザーフレンドリーなメッセージが表示される
- [ ] ユーザーが書籍検索画面に戻れる

---

## 例外・エラーシナリオ

### 在庫管理

| シナリオ | 期待される動作 | エラーコード | テスト方法 |
|----------|-------------------|-------------|-----------|
| 在庫0の書籍を検索 | 検索結果に表示（BR-004）、「カートへ」ボタン無効化 | - | bookSelect.xhtml で在庫0の書籍を確認 |
| カート追加後に在庫0になった | 注文確定時に在庫不足エラー | BIZ-003 | 2ユーザーで同じ在庫5の書籍を5個ずつカートに追加し、1人が先に購入後、もう1人が購入試行 |
| 複数ユーザーが同時購入 | 先着順成功、後続ユーザーはOptimisticLockException | BIZ-004 | 2ブラウザで同時にカート追加→同時に注文確定 |
| 在庫数より多い数量を注文 | 注文確定時にエラー、カート保持 | BIZ-003 | カートに在庫5の書籍を10個追加して注文 |
| マイナス数量でカート追加 | クライアント検証エラー | VAL-004 | 数量フィールドに-1を入力 |
| 0数量でカート追加 | クライアント検証エラー | VAL-004 | 数量フィールドに0を入力 |

### 認証・セッション

| シナリオ | 期待される動作 | エラーコード | テスト方法 |
|----------|-------------------|-------------|-----------|
| セッションタイムアウト（60分） | ログイン画面にリダイレクト | - | 60分待機後に保護ページにアクセス |
| 直接URL入力（未ログイン） | AuthenticationFilterでログイン画面にリダイレクト（BR-034） | - | ブラウザで /bookSearch.xhtml に直接アクセス |
| ログアウト後のブラウザバック | AuthenticationFilterでログイン画面にリダイレクト | - | ログアウト後、ブラウザの戻るボタンをクリック |
| 重複ログイン | 最新のセッションのみ有効（古いセッションは無効化） | - | 2つのブラウザで同じアカウントでログイン |
| 不正なメールアドレスでログイン | 認証失敗 | BIZ-002 | 存在しないメールアドレスでログイン試行 |
| パスワード未入力でログイン | クライアント検証エラー | VAL-002 | パスワードフィールドを空にしてログイン |
| メールアドレス未入力でログイン | クライアント検証エラー | VAL-001 | メールアドレスフィールドを空にしてログイン |

### カート操作

| シナリオ | 期待される動作 | エラーコード | テスト方法 |
|----------|-------------------|-------------|-----------|
| 空カートで注文画面へ遷移 | エラーメッセージ表示、注文画面に遷移しない | BIZ-005 | カートをクリア後、直接 /bookOrder.xhtml にアクセス |
| カート内の書籍がデータベースから削除された | 注文時にエラー（書籍が見つからない） | SYS-001 | カート追加後、管理者がデータベースから書籍を削除 |
| カート内の価格が変更された | カート追加時の価格で注文（CartItemに保存済み） | - | カート追加後、管理者がデータベースで価格を変更 |
| セッション内のカートが破損 | システムエラー | SYS-001 | セッションデータを手動で改ざん |
| 同じ書籍を最大在庫数以上カート追加 | クライアント検証または注文時エラー | BIZ-003 | 在庫5の書籍を3個カートに追加後、さらに3個追加 |

### 入力検証

| シナリオ | 期待される動作 | エラーコード | テスト方法 |
|----------|-------------------|-------------|-----------|
| 顧客名未入力で登録 | クライアント検証エラー | VAL-001 | 新規登録フォームで顧客名を空にして送信 |
| メールアドレス形式不正で登録 | クライアント検証エラー | VAL-001 | "invalid-email" のような不正な形式で登録 |
| 配送先住所未入力で注文 | クライアント検証エラー | VAL-003 | 注文画面で配送先住所を空にして送信 |
| 決済方法未選択で注文 | クライアント検証エラー | - | 注文画面で決済方法を選択せずに送信 |
| 重複メールアドレスで登録 | ビジネスロジックエラー（BR-030） | BIZ-001 | 既存のメールアドレスで登録試行 |

### 配送料金計算

| シナリオ | 期待される動作 | 計算ロジック | テスト方法 |
|----------|-------------------|-------------|-----------|
| 購入金額4999円（東京都） | 配送料800円 | 5000円未満 & 非沖縄 | カート合計4999円で注文画面へ |
| 購入金額5000円（東京都） | 配送料0円（送料無料） | 5000円以上 | カート合計5000円で注文画面へ |
| 購入金額5001円（東京都） | 配送料0円（送料無料） | 5000円以上 | カート合計5001円で注文画面へ |
| 購入金額3000円（沖縄県） | 配送料1700円 | 5000円未満 & 沖縄 | カート合計3000円、住所「沖縄県那覇市」 |
| 購入金額5000円（沖縄県） | 配送料0円（送料無料） | 5000円以上優先 | カート合計5000円、住所「沖縄県那覇市」 |
| 購入金額3000円（大阪府） | 配送料800円 | 5000円未満 & 非沖縄 | カート合計3000円、住所「大阪府」 |

---

## エラーメッセージ一覧

### 検証エラー（VAL-xxx）

| コード | メッセージ | 発生条件 | 画面 |
|--------|-----------|---------|------|
| VAL-001 | {フィールド名}を入力してください | 必須フィールド未入力 | 全フォーム |
| VAL-002 | パスワードを入力してください | パスワード未入力 | index.xhtml, customerInput.xhtml |
| VAL-003 | 配送先住所を入力してください | 住所未入力 | bookOrder.xhtml |
| VAL-004 | 数量は1以上を入力してください | 数量が0以下 | bookSelect.xhtml |

### ビジネスエラー（BIZ-xxx）

| コード | メッセージ | 発生条件 | 例外クラス | 画面 |
|--------|-----------|---------|-----------|------|
| BIZ-001 | メールアドレスが既に登録されています | メールアドレス重複（BR-030） | EmailAlreadyExistsException | customerInput.xhtml |
| BIZ-002 | メールアドレスまたはパスワードが正しくありません | 認証失敗 | - | index.xhtml |
| BIZ-003 | 在庫が不足しています | 注文数 > 在庫数 | OutOfStockException | orderError.xhtml |
| BIZ-004 | 他のユーザーが購入済みです。カートを確認してください | 楽観的ロック競合（BR-024） | OptimisticLockException | orderError.xhtml |
| BIZ-005 | カートが空です | 空カートで注文試行 | - | cartView.xhtml |

### システムエラー（SYS-xxx）

| コード | メッセージ | 発生条件 | 画面 |
|--------|-----------|---------|------|
| SYS-001 | システムエラーが発生しました | 予期しないエラー | エラーページ |
| SYS-002 | データベース接続エラー | DB接続失敗 | エラーページ |
| SYS-003 | トランザクションエラー | トランザクション失敗 | エラーページ |

---

## ビジネスルール参照

### 検索機能（F-001）

| ルールID | 説明 | 影響するシナリオ |
|---------|-------------|-----------------|
| BR-001 | カテゴリ未選択の場合、全カテゴリが検索対象 | カテゴリで書籍を絞り込む |
| BR-002 | キーワード未入力の場合、書籍名と著者の両方を検索 | キーワードで書籍を検索する |
| BR-003 | 検索結果は書籍ID昇順でソート | 全検索シナリオ |
| BR-004 | 在庫0の書籍も表示（購入不可） | 在庫なしの書籍も表示される |

### カート管理（F-002）

| ルールID | 説明 | 影響するシナリオ |
|---------|-------------|-----------------|
| BR-010 | カート内容はセッション単位で保持（ログアウトまで） | 全カートシナリオ |
| BR-011 | 同じ書籍を追加した場合、数量を加算 | 同じ書籍を複数回追加する |
| BR-012 | カート追加時点の在庫バージョン番号を保存（楽観的ロック用） | 書籍をカートに追加する |
| BR-013 | カート内の合計金額は常に自動計算 | 全カートシナリオ |

### 注文処理（F-003）

| ルールID | 説明 | 詳細 | 影響するシナリオ |
|---------|-------------|------|-----------------|
| BR-020 | 配送料金計算ルール | 通常800円、沖縄県1700円、5000円以上無料 | 配送料金の自動計算 |
| BR-021 | 決済方法選択肢 | 銀行振込(1)、クレジットカード(2)、着払い(3) | 注文を確定する |
| BR-022 | 在庫チェックタイミング | 注文確定時に全書籍の在庫を確認 | 注文を確定する、在庫不足エラー |
| BR-023 | 在庫減算タイミング | 在庫チェック後、注文登録前に減算 | 注文を確定する |
| BR-024 | 楽観的ロック制御 | カート追加時のバージョン番号で在庫更新 | 同時注文による競合 |
| BR-025 | トランザクション範囲 | 在庫チェック〜注文登録〜在庫減算は単一トランザクション | 注文を確定する |

### 顧客管理・認証（F-004）

| ルールID | 説明 | 影響するシナリオ |
|---------|-------------|-----------------|
| BR-030 | メールアドレスは一意（重複不可） | メールアドレス重複エラー |
| BR-031 | パスワードは平文保存（学習用のみ、本番環境では非推奨） | 新規顧客登録、ログイン |
| BR-032 | セッションタイムアウト: 60分 | セッションタイムアウト |
| BR-033 | 公開ページ: ログイン画面、新規登録画面、登録完了画面 | 未ログインユーザーのアクセス制限 |
| BR-034 | 公開ページ以外は認証必須 | 未ログインユーザーのアクセス制限 |

### 注文履歴参照（F-005）

| ルールID | 説明 | 影響するシナリオ |
|---------|-------------|-----------------|
| BR-040 | 注文履歴は顧客IDでフィルタリング | 注文履歴一覧を表示する |
| BR-041 | 注文日降順（新しい順）でソート | 注文履歴一覧を表示する |
| BR-042 | 注文詳細は注文IDで取得 | 注文詳細を表示する |

---

## テスト実行チェックリスト

### 機能テスト

- [ ] **書籍検索・閲覧**: 全4シナリオ実行
- [ ] **ショッピングカート管理**: 全4シナリオ実行
- [ ] **注文処理**: 全6シナリオ実行（正常系、在庫不足、楽観的ロック、配送料金計算3パターン）
- [ ] **顧客管理・認証**: 全6シナリオ実行
- [ ] **注文履歴参照**: 全3シナリオ実行

### 例外・エラーテスト

- [ ] **在庫管理**: 全6ケース実行
- [ ] **認証・セッション**: 全7ケース実行
- [ ] **カート操作**: 全5ケース実行
- [ ] **入力検証**: 全5ケース実行
- [ ] **配送料金計算**: 全6ケース実行

### ブラウザ互換性テスト

- [ ] Chrome最新版
- [ ] Firefox最新版
- [ ] Edge最新版

### パフォーマンステスト

- [ ] 50同時ユーザーでの動作確認
- [ ] レスポンスタイム3秒以内の確認

