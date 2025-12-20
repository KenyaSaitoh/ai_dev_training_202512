# F-002: ショッピングカート管理 - 機能タスク

**タスクファイル:** feature_f002_shopping_cart.md  
**担当者:** 担当者B（1名）  
**推奨スキル:** JSF, セッション管理, Jakarta EE  
**想定工数:** 10時間  
**依存タスク:** common_tasks.md

---

## 概要

選択した書籍をカートに追加し、購入前に内容を確認・調整する機能を実装します。

**参照SPEC:**
- [F_002_shopping_cart/functional_design.md](../baseline/features/F_002_shopping_cart/functional_design.md)
- [F_002_shopping_cart/behaviors.md](../baseline/features/F_002_shopping_cart/behaviors.md)
- [F_002_shopping_cart/screen_design.md](../baseline/features/F_002_shopping_cart/screen_design.md)

---

## ビジネスルール

| ルールID | 説明 |
|---------|-------------|
| BR-010 | カート内容はセッション単位で保持（ログアウトまで） |
| BR-011 | 同じ書籍を追加した場合、数量を加算 |
| BR-012 | カート追加時点の在庫バージョン番号を保存（楽観的ロック用） |
| BR-013 | カート内の合計金額は常に自動計算 |

---

## タスクリスト

### セクション1: DTOクラス

- [ ] **T-F002-001**: CartItemクラスの作成
  - **目的**: カートアイテムDTOを実装する
  - **対象**: web/cart/CartItem.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - POJO, implements Serializable
    - フィールド:
      - book (Book) - 書籍情報
      - quantity (Integer) - 数量
      - price (BigDecimal) - 価格（追加時点の価格）
      - stockVersion (Long) - 在庫バージョン番号（BR-012）
    - getter/setter
    - getSubtotal() - 小計を計算（price × quantity）

---

### セクション2: プレゼンテーション層（Managed Bean）

- [ ] **T-F002-002**: CartSessionの作成
  - **目的**: カートセッションファサードを実装する
  - **対象**: web/cart/CartSession.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @SessionScoped, implements Serializable（BR-010）
    - フィールド:
      - cartItems (List<CartItem>)
    - 主要メソッド:
      - addItem(CartItem) - カートに追加（BR-011: 同じ書籍は数量加算）
      - removeItem(Integer bookId) - カートから削除
      - clear() - カート全体をクリア
      - getTotalPrice() - 合計金額を計算（BR-013）
      - isEmpty() - カートが空かチェック
      - getItemCount() - アイテム数を取得

- [ ] **T-F002-003**: CartBeanの作成
  - **目的**: カート画面のコントローラーを実装する
  - **対象**: web/cart/CartBean.java
  - **参照SPEC**: functional_design.md#51-プレゼンテーション層
  - **注意事項**: 
    - @Named, @SessionScoped, implements Serializable（リダイレクト後も状態を保持するため）
    - @Inject BookService, StockDao, CartSession, CustomerBean, DeliveryFeeService
    - フィールド:
      - globalErrorMessage (String) - グローバルエラーメッセージ
    - 主要メソッド:
      - addBook(Integer bookId, Integer count) - 書籍をカートに追加し、cartView画面へリダイレクト。在庫バージョン番号を保存（BR-012）
      - removeSelectedBooks() - 選択した書籍（remove=true）をカートから削除し、合計金額を再計算
      - clearCart() - カート全体をクリアし、cartClear画面へリダイレクト
      - proceedToOrder() - 配送先住所を設定し、配送料金を計算して注文画面に進む。カートが空の場合はnullを返す（BIZ-005）
      - viewCart() - カート画面を表示。カートが空の場合はglobalErrorMessageを設定
    - カート追加時に在庫バージョン番号を保存（BR-012）
    - 既存のCartItemと同じ書籍を追加する場合、数量を加算（BR-011）

---

### セクション3: ビュー層（XHTML）

- [ ] **T-F002-004**: cartView.xhtmlの作成（カート確認画面）
  - **目的**: カート確認画面を実装する
  - **対象**: webapp/cartView.xhtml
  - **参照SPEC**: screen_design.md#1-カート確認画面
  - **注意事項**: 
    - カート内容テーブル（ui:repeat）
    - 表示項目: カバー画像, 書籍名, 注文数, 価格, 削除チェックボックス
    - カバー画像表示:
      - `h:graphicImage library="images" name="covers/#{cartItem.bookName.replace(' ', '_')}.jpg"`
      - CSSクラス: `styleClass="book-thumbnail"`（高さ5cm、幅自動）
      - セルクラス: `.book-image-cell`（中央配置）
    - 画像ファイルが存在しない場合、onErrorでno-image.jpgを表示
    - 「削除」ボタン（h:commandButton action="#{cartBean.removeFromCart}"）
    - 「全クリア」ボタン（h:commandButton action="#{cartBean.clearCart}"）
    - 合計金額表示（#{cartSession.totalPrice}）
    - 「注文へ進む」ボタン（h:commandButton action="#{cartBean.navigateToOrder}"）
    - 「書籍検索に戻る」リンク
    - カートが空の場合、「注文へ進む」ボタンを無効化

---

### セクション4: ナビゲーション設定

- [ ] **T-F002-005**: faces-config.xmlへのナビゲーション追加
  - **目的**: 画面遷移ルールを設定する
  - **対象**: webapp/WEB-INF/faces-config.xml
  - **参照SPEC**: screen_design.md#画面遷移図
  - **注意事項**: 
    - bookSelect → cartView (カート追加)
    - cartView → bookSearch (書籍検索に戻る)
    - cartView → bookOrder (注文へ進む)

---

## 並行実行可能なタスク

### 並行実行可能
- T-F002-001 (CartItem) と T-F002-004 (XHTML)

### 順次実行が必要
- T-F002-001 (CartItem) → T-F002-002 (CartSession) → T-F002-003 (CartBean) → T-F002-004 (XHTML)

---

## ユニットテスト

### セッションファサードテスト
- [ ] **T-F002-UT-001**: CartSessionTestの作成
  - **目的**: CartSessionのユニットテストを作成する
  - **対象**: test/java/web/cart/CartSessionTest.java
  - **参照SPEC**: behaviors.md (F-002のシナリオ)
  - **テストケース**:
    - testAddItem() - カートに追加
    - testAddSameBookTwice() - 同じ書籍を複数回追加（BR-011）
    - testRemoveItem() - カートから削除
    - testClearCart() - カート全体をクリア
    - testGetTotalPrice() - 合計金額計算（BR-013）
    - testIsEmpty() - カートが空かチェック
    - testGetItemCount() - アイテム数取得

### Beanテスト
- [ ] **T-F002-UT-002**: CartBeanTestの作成
  - **目的**: CartBeanのユニットテストを作成する
  - **対象**: test/java/web/cart/CartBeanTest.java
  - **参照SPEC**: behaviors.md (F-002のシナリオ)
  - **テストケース**:
    - testAddToCart() - カート追加
    - testStockVersionIsSaved() - 在庫バージョン番号が保存される（BR-012）
    - testRemoveFromCart() - カート削除
    - testClearCart() - カート全体クリア
    - testNavigateToOrderWhenCartIsEmpty() - 空カートで注文画面への遷移をブロック（BIZ-005）

### 結合テスト（手動）
- [ ] **T-F002-IT-001**: カート機能の動作確認
  - **目的**: カート機能が正しく動作することを確認する
  - **確認項目**:
    - カートに書籍を追加できる
    - 同じ書籍を追加すると数量が加算される（BR-011）
    - カートから書籍を削除できる
    - カート全体をクリアできる
    - 合計金額が正しく計算される（BR-013）
    - 空カートで「注文へ進む」ボタンが無効化される

---

## 受入基準（behaviors.md参照）

### Scenario 1: 書籍をカートに追加する
**Given** ログイン済みユーザーが検索結果画面にいる  
**When** 書籍Aの「カートへ」ボタンをクリック  
**Then** カート画面が表示され、書籍Aが追加されている

### Scenario 2: カート内の書籍を削除する
**Given** カートに書籍Aが追加されている  
**When** 書籍Aの「削除」ボタンをクリック  
**Then** 書籍Aがカートから削除される

### Scenario 3: カート全体をクリアする
**Given** カートに複数の書籍が追加されている  
**When** 「全クリア」ボタンをクリック  
**Then** カート内の全書籍が削除される

### Scenario 4: 同じ書籍を複数回追加する
**Given** カートに書籍A（数量1）が追加されている  
**When** 書籍Aを再度カートに追加（数量1）  
**Then** 書籍Aの数量が2になる（BR-011）

---

## 完了条件

- [ ] 全タスクが完了している
- [ ] CartItemクラスが正しく動作している
- [ ] CartSessionが正しく動作している
- [ ] CartBeanが正しく動作している
- [ ] カート画面が正しく表示される
- [ ] 画面遷移が正しく動作している
- [ ] 全受入基準が満たされている
- [ ] ユニットテストが作成され、カバレッジ80%以上を達成している

---

## トラブルシューティング

### カートに追加されない
- CartBeanのaddToCartメソッドが正しく実装されているか確認
- CartSessionがセッションスコープに存在するか確認

### 同じ書籍が重複して追加される
- CartSessionのaddItemメソッドで、同じ書籍IDの場合に数量を加算しているか確認（BR-011）

### 合計金額が正しく計算されない
- CartItemのgetSubtotalメソッドが正しく実装されているか確認
- CartSessionのgetTotalPriceメソッドが正しく実装されているか確認（BR-013）

### 空カートで注文画面に遷移できてしまう
- CartBeanのnavigateToOrderメソッドで、カートが空の場合にエラーを返しているか確認（BIZ-005）
- cartView.xhtmlで「注文へ進む」ボタンのdisabled属性が正しく設定されているか確認

---

**次のタスク:** 他の機能（F-001, F-003〜F-005）と並行実行可能

