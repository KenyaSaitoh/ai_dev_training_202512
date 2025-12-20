# F-001: 書籍検索・閲覧 - 機能タスク

**タスクファイル:** feature_f001_book_search.md  
**担当者:** 担当者A（1名）  
**推奨スキル:** JSF, JPA, Jakarta EE  
**想定工数:** 12時間  
**依存タスク:** common_tasks.md

---

## 概要

カテゴリやキーワードで書籍を検索し、検索結果を一覧表示する機能を実装します。

**参照SPEC:**
- [F_001_book_search/functional_design.md](../baseline/features/F_001_book_search/functional_design.md)
- [F_001_book_search/behaviors.md](../baseline/features/F_001_book_search/behaviors.md)
- [F_001_book_search/screen_design.md](../baseline/features/F_001_book_search/screen_design.md)

---

## ビジネスルール

| ルールID | 説明 |
|---------|-------------|
| BR-001 | カテゴリ未選択の場合、全カテゴリが検索対象 |
| BR-002 | キーワード未入力の場合、書籍名と著者の両方を検索 |
| BR-003 | 検索結果は書籍ID昇順でソート |
| BR-004 | 在庫0の書籍も表示（購入不可） |
| BR-005 | カバー画像ファイル名は書籍名 + ".jpg" で生成 |
| BR-006 | カバー画像のパスは`resources/covers/{書籍名}.jpg` |
| BR-007 | 画像ファイルが存在しない場合は`no-image.jpg`を表示 |

---

## タスクリスト

### セクション1: サービス層

- [ ] **T-F001-001**: BookServiceの作成
  - **目的**: 書籍検索のビジネスロジックを実装する
  - **対象**: service/book/BookService.java
  - **参照SPEC**: functional_design.md#52-ビジネスロジック層
  - **注意事項**: 
    - @ApplicationScoped
    - @Inject BookDao, CategoryDao, StockDao
    - 主要メソッド:
      - searchBooks(Integer categoryId, String keyword) - 書籍検索
      - getAllBooks() - 全書籍取得
      - getBookById(Integer bookId) - 書籍IDで取得
    - ビジネスルール（BR-001, BR-002, BR-003）を実装
    - 在庫情報を含めて返却

---

### セクション2: プレゼンテーション層（Managed Bean）

- [ ] **T-F001-002**: SearchParamクラスの作成
  - **目的**: 検索パラメータを保持するDTOを実装する
  - **対象**: web/book/SearchParam.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - POJO（アノテーション不要）
    - フィールド: categoryId, keyword
    - getter/setter

- [ ] **T-F001-003**: BookSearchBeanの作成
  - **目的**: 書籍検索画面のコントローラーを実装する
  - **対象**: web/book/BookSearchBean.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @ViewScoped, implements Serializable
    - @Inject BookService, CategoryService
    - フィールド:
      - searchParam (SearchParam)
      - books (List<Book>)
      - categories (List<Category>)
    - 主要メソッド:
      - init() - @PostConstruct: カテゴリ一覧を取得
      - search() - 検索実行
      - navigateToCart() - カート画面に遷移
    - ビジネスルール（BR-001, BR-002）を考慮

---

### セクション3: ビュー層（XHTML）

- [ ] **T-F001-004**: bookSearch.xhtmlの作成（書籍検索画面）
  - **目的**: 書籍検索画面を実装する
  - **対象**: webapp/bookSearch.xhtml
  - **参照SPEC**: screen_design.md#1-書籍検索画面
  - **注意事項**: 
    - カテゴリドロップダウン（h:selectOneMenu）
    - キーワード入力欄（h:inputText）
    - 検索ボタン（h:commandButton action="#{bookSearchBean.search}"）
    - ナビゲーションリンク（注文履歴、カート、ログアウト）
    - 検索条件の初期値設定

- [ ] **T-F001-005**: bookSelect.xhtmlの作成（検索結果画面）
  - **目的**: 検索結果一覧画面を実装する
  - **対象**: webapp/bookSelect.xhtml
  - **参照SPEC**: screen_design.md#2-検索結果画面
  - **注意事項**: 
    - 検索結果テーブル（h:dataTable）
    - 表示項目: カバー画像, 書籍ID, 書籍名, 著者, 価格, 在庫数
    - カバー画像表示（h:graphicImage value="resources/covers/#{book.imageFileName}"）
      - BookエンティティのgetImageFileName()メソッドで書籍名 + ".jpg" を返す（BR-005）
    - 画像ファイルが存在しない場合、no-image.jpgを表示（BR-007）
    - 画像サイズ: 最大幅80px、高さ自動調整
    - 在庫0の場合、「カートへ」ボタンを無効化（BR-004）
    - 「カートへ」ボタン（h:commandButton action="#{cartBean.addToCart}"）
    - 「検索に戻る」リンク

---

### セクション4: ナビゲーション設定

- [ ] **T-F001-006**: faces-config.xmlへのナビゲーション追加
  - **目的**: 画面遷移ルールを設定する
  - **対象**: webapp/WEB-INF/faces-config.xml
  - **参照SPEC**: screen_design.md#画面遷移図
  - **注意事項**: 
    - bookSearch → bookSelect (検索実行)
    - bookSelect → cartView (カート追加)
    - bookSelect → bookSearch (検索に戻る)

---

## 並行実行可能なタスク

### 並行実行可能
- T-F001-002 (SearchParam) と T-F001-004, T-F001-005 (XHTML)

### 順次実行が必要
- T-F001-001 (BookService) → T-F001-003 (BookSearchBean) → T-F001-004, T-F001-005 (XHTML)

---

## ユニットテスト

### サービステスト
- [ ] **T-F001-UT-001**: BookServiceTestの作成
  - **目的**: BookServiceのユニットテストを作成する
  - **対象**: test/java/service/book/BookServiceTest.java
  - **参照SPEC**: behaviors.md (F-001のシナリオ)
  - **テストケース**:
    - testSearchBooksByCategory() - カテゴリで絞り込み
    - testSearchBooksByKeyword() - キーワードで検索
    - testSearchBooksByCategoryAndKeyword() - カテゴリ+キーワード
    - testSearchBooksWithoutCategory() - カテゴリ未選択（BR-001）
    - testSearchBooksWithoutKeyword() - キーワード未入力（BR-002）
    - testGetBookById() - 書籍IDで取得
    - testSearchResultsAreSorted() - 検索結果がID昇順（BR-003）
    - testBooksWithZeroStockAreDisplayed() - 在庫0の書籍も表示（BR-004）

### 結合テスト（手動）
- [ ] **T-F001-IT-001**: 書籍検索画面の表示確認
  - **目的**: 書籍検索画面が正しく表示されることを確認する
  - **確認項目**:
    - カテゴリドロップダウンに全カテゴリが表示される
    - キーワード入力欄が表示される
    - 検索ボタンが表示される

- [ ] **T-F001-IT-002**: 検索機能の動作確認
  - **目的**: 検索機能が正しく動作することを確認する
  - **確認項目**:
    - カテゴリで絞り込みができる
    - キーワードで検索ができる
    - カテゴリ+キーワードで検索ができる
    - 検索結果がID昇順で表示される
    - 在庫0の書籍も表示される（「カートへ」ボタンは無効）

---

## 受入基準（behaviors.md参照）

### Scenario 1: カテゴリで書籍を絞り込む
**Given** ログイン済みユーザーが書籍検索画面にアクセス  
**When** カテゴリ「Java」を選択して検索  
**Then** カテゴリ「Java」の書籍のみが表示される

### Scenario 2: キーワードで書籍を検索する
**Given** ログイン済みユーザーが書籍検索画面にアクセス  
**When** キーワード「Spring」を入力して検索  
**Then** 書籍名または著者名に「Spring」を含む書籍が表示される

### Scenario 3: カテゴリとキーワードを組み合わせて検索する
**Given** ログイン済みユーザーが書籍検索画面にアクセス  
**When** カテゴリ「Java」を選択し、キーワード「Spring」を入力して検索  
**Then** カテゴリ「Java」かつ書籍名または著者名に「Spring」を含む書籍が表示される

### Scenario 4: 在庫なしの書籍も表示される
**Given** ログイン済みユーザーが書籍検索画面にアクセス  
**When** 在庫0の書籍を含むカテゴリで検索  
**Then** 在庫0の書籍も表示され、「カートへ」ボタンが無効化されている

---

## 完了条件

- [ ] 全タスクが完了している
- [ ] BookServiceが正しく動作している
- [ ] BookSearchBeanが正しく動作している
- [ ] 書籍検索画面が正しく表示される
- [ ] 検索結果画面が正しく表示される
- [ ] 画面遷移が正しく動作している
- [ ] 全受入基準が満たされている
- [ ] ユニットテストが作成され、カバレッジ80%以上を達成している

---

## トラブルシューティング

### 検索結果が表示されない
- BookServiceのsearchBooksメソッドが正しく実装されているか確認
- BookDaoのJPQLクエリが正しいか確認
- カテゴリIDとキーワードが正しく渡されているか確認

### カテゴリドロップダウンに値が表示されない
- CategoryServiceのgetAllCategoriesメソッドが正しく動作しているか確認
- bookSearch.xhtmlのh:selectOneMenuが正しく設定されているか確認

### 在庫0の書籍の「カートへ」ボタンが無効化されない
- bookSelect.xhtmlのh:commandButtonのdisabled属性が正しく設定されているか確認
- Stockエンティティのquantityが正しく取得されているか確認

---

**次のタスク:** 他の機能（F-002〜F-005）と並行実行可能

