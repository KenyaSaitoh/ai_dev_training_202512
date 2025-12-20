# F-003: 注文処理 - 画面設計書

**機能ID:** F-003  
**機能名:** 注文処理  
**バージョン:** 1.0.0  
**最終更新日:** 2025-12-16  
**フォーマット:** PlantUML (draw.io インポート可能)

---

## 画面一覧

1. [注文入力画面 (bookOrder.xhtml)](#1-注文入力画面)
2. [注文完了画面 (orderSuccess.xhtml)](#2-注文完了画面)
3. [注文エラー画面 (orderError.xhtml)](#3-注文エラー画面)

---

## 1. 注文入力画面

**ファイル名:** `bookOrder.xhtml`  
**目的:** 配送先・決済方法入力

### PlantUML

```plantuml
@startsalt
{+
  {/ <b>berry-books</b> | <&person> Aliceさん | <&account-logout> ログアウト }
  ..
  {
    <b>注文内容確認</b>
    ==
    <b>▼ 注文書籍</b>
    {#
      . 画像   | 書籍名                    | 単価      | 数量 | 小計
      --
      . [img] | Java SEディープダイブ      | 3,400円   | 1   | 3,400円
      . [img] | SpringBoot in Cloud      | 3,000円   | 1   | 3,000円
      . [img] | SQLの冒険～RDBの深層       | 2,200円   | 1   | 2,200円
    }
    ..
    <b>▼ 配送先情報</b>
    配送先住所 | "東京都渋谷区道玄坂1-2-3               "
               | "                                     "
    ..
    <b>▼ 決済方法</b>
    ( ) 銀行振り込み
    (X) クレジットカード
    ( ) 着払い
    ..
    <b>▼ 料金</b>
    {
      . | 商品合計   | 8,600円
      . | 配送料     | <b>800円</b>
      . | <b>総合計</b> | <b>9,400円</b>
    }
    ==
    [  カートに戻る  ]  [    注文を確定する    ]
  }
  ..
  {/ © 2025 berry-books. All rights reserved. }
}
@endsalt
```

### 画像表示ルール

- **画像ファイル名**: 書籍名（BOOK_NAME）+ ".jpg"
  - 例: `Java SEディープダイブ` → `Java SEディープダイブ.jpg`
- **画像パス**: `resources/covers/#{book.imageFileName}`
  - BookエンティティのgetImageFileName()メソッドで生成
- **サイズ**: サムネイル表示（最大幅60px、高さ自動調整）
- **画像なし**: ファイルが存在しない場合、`no-image.jpg`を表示
- **Alt属性**: 書籍名を設定

### 配送料金計算ルール

```
IF 商品合計 >= 5,000円
  → 配送料 = 0円（送料無料）
ELSE IF 配送先住所 が "沖縄県" で始まる
  → 配送料 = 1,700円
ELSE
  → 配送料 = 800円
```

### 動作

- **カートに戻る**: cartView.xhtmlへ遷移
- **注文を確定する**: OrderBean.placeOrder()
  - 成功 → orderSuccess.xhtml
  - 在庫不足 → エラーメッセージ表示、同画面に留まる
  - 楽観的ロック競合 → エラーメッセージ表示

---

## 2. 注文完了画面

**ファイル名:** `orderSuccess.xhtml`  
**目的:** 注文確定完了通知

### PlantUML

```plantuml
@startsalt
{+
  {/ <b>berry-books</b> | <&person> Aliceさん | <&account-logout> ログアウト }
  ..
  {
    .
    <&circle-check><size:24> <b>ご注文ありがとうございます！</b>
    .
    ご注文を承りました。
    .
    {
      注文番号: <b>#1001</b>
      注文日時: 2025-12-16 14:30:25
      総合計:   <b>9,400円</b>
    }
    .
    配送先住所に商品をお届けします。
    .
    ==
    [  注文履歴を見る  ]  [  買い物を続ける  ]
  }
  ..
  {/ © 2025 berry-books. All rights reserved. }
}
@endsalt
```

---

## 3. 注文エラー画面

**ファイル名:** `orderError.xhtml`  
**目的:** 注文エラー表示

### PlantUML

```plantuml
@startsalt
{+
  {/ <b>berry-books</b> | <&person> Aliceさん | <&account-logout> ログアウト }
  ..
  {
    .
    <&warning><size:24> <b>エラーが発生しました</b>
    .
    {-
      <b>在庫不足エラー</b>
      .
      申し訳ございません。
      以下の書籍の在庫が不足しています。
      .
      • Java SEディープダイブ（在庫: 1, 注文: 2）
      .
      カートを確認して注文数量を調整してください。
    }
    .
    ==
    [  カートに戻る  ]  [  書籍検索  ]
  }
  ..
  {/ © 2025 berry-books. All rights reserved. }
}
@endsalt
```

### エラーメッセージパターン

| エラー種別 | メッセージ |
|----------|----------|
| 在庫不足 | "在庫が不足しています。書籍名（在庫: X, 注文: Y）" |
| 楽観的ロック競合 | "他のユーザーが同時に購入しました。カートを確認してください。" |
| 空カート | "カートに商品がありません。" |

