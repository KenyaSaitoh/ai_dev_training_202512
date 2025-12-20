# berry-books-mvc-sdd プロジェクト

## 📖 概要

Jakarta EE 10によるオンライン書店「**Berry Books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

**主な機能:**
- 📚 書籍検索・閲覧（カバー画像表示対応）
- 🛒 ショッピングカート管理
- 💳 注文処理（配送先・決済方法選択）
- 👤 顧客管理・認証
- 📋 注文履歴参照

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、完成したSPECから生成AIによる実装を実践します。

---

## 🎯 このプロジェクトの位置付け

### 完成したSPECが存在する
**このプロジェクト（berry-books-mvc-sdd）は、完成したベースラインSPECが `specs/baseline/system/` に用意されています。**

仕様駆動開発の実装フェーズに焦点を当てており、**生成AIにSPECを読ませて実装させる**ことを目的としています。

### ベースラインSPECの構造
- **`specs/baseline/system/`**: システム全体のアーキテクチャ・設計・要件を定義したSPEC（概要）
- **`specs/baseline/features/`**: 個別機能のSPEC（F_001～F_005の詳細設計）

### 2つの利用シナリオ

#### シナリオ1: berry-books-mvc-sddの実装（完成したSPECから）
完成したSPECを使って、生成AIにアプリケーション全体を実装させます。

**対象:**
- 仕様駆動開発の実装フェーズの学習
- 生成AIによるコード生成の実践
- 完全なSPECからの実装体験

**実装の開始方法:** 下記の「シナリオ1: 実装フェーズの実行」を参照

#### シナリオ2: 既存プロジェクトへの拡張
既に稼働しているberry-books-mvc-sddを拡張する場合。既存のアーキテクチャを前提に、拡張内容のSPECを作成し、実装します。

**対象:**
- 新機能の追加（例: 在庫アラート機能）
- 既存機能の改善・拡張
- バグ修正
- セキュリティ対策
- パフォーマンス改善

**SPECの作成方法:** 下記の「シナリオ2: 拡張の実行」を参照

**SPECの配置:** `specs/enhancements/YYYYMM_案件名/` のような個別フォルダに配置

---

## 🧹 成果物のクリーンアップ（仕様駆動開発の再実行）

このプロジェクトは仕様駆動開発（SDD）により、何度でも再実装できます。既存の成果物をクリーンアップして、SPECから再実装する場合は以下のタスクを実行してください。

### クリーンアップタスク

```bash
# 成果物をクリーンアップ（ディレクトリ構造は保持）
./gradlew :projects:java:berry-books-mvc-sdd:cleanSddArtifacts
```

**このタスクが削除するもの:**
- `src/` 配下のすべてのファイル（Java、XHTML、CSS、設定ファイルなど）
- `sql/hsqldb/` 配下のすべてのSQLファイル

**このタスクが保持するもの:**
- 以下のディレクトリ構造（空のディレクトリも含む）：
  - `src/main/java`
  - `src/main/resources/META-INF`
  - `src/main/webapp/resources`
  - `src/main/webapp/WEB-INF`
  - `src/test/java`
  - `src/test/resources`
  - `sql/hsqldb`
- `specs/` フォルダ（SPEC）
- `instructions/` フォルダ（インストラクション）
- `memory/` フォルダ（憲章）
- その他のプロジェクト設定ファイル（`build.gradle`、`README.md`など）

**クリーンアップ後の状態:**
- 空のディレクトリには `.gitkeep` ファイルが自動配置されます
- ディレクトリ構造が保持されるため、再実装がスムーズに進められます

**クリーンアップ後の再実装手順:**
1. `@instructions/generate_tasks.md` を使ってタスクリストを生成（`tasks/` に複数ファイルとして保存）
2. `@instructions/generate_code.md` を使ってタスクに従って実装
   - タスク1から順に実行（setup_tasks.md → common_tasks.md → F_001_*.md ～ F_005_*.md → integration_tasks.md）
   - タスクの依存関係を考慮して、並行作業可能なタスクは分担可能

---

## 📦 シナリオ1: 実装フェーズの実行（完成したSPECから）

**このプロジェクト（berry-books-mvc-sdd）は、完成したSPECが `specs/` に用意されています。**

### 既存のSPECファイル

```
specs/
├── baseline/                   # ベースラインSPEC（001）
│   ├── system/                 # システム全体のSPEC（概要）
│   │   ├── requirements.md         # 要件定義書（What & Why）
│   │   ├── architecture_design.md  # アーキテクチャ設計書
│   │   ├── functional_design.md    # 機能設計書（概要、リンク集）
│   │   ├── behaviors.md            # 振る舞い仕様書（概要、リンク集）
│   │   ├── data_model.md           # データモデル仕様書（ER図、テーブル定義）
│   │   ├── screen_design.md        # 画面設計書（概要、リンク集）
│   │   └── external_interface.md   # 外部インターフェース仕様書
│   └── features/               # 個別機能のSPEC
│       ├── F_001_book_search/      # 書籍検索・閲覧
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F_002_shopping_cart/    # ショッピングカート管理
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F_003_order_processing/ # 注文処理
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F_004_customer_auth/    # 顧客管理・認証
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       └── F_005_order_history/    # 注文履歴参照
│           ├── functional_design.md
│           ├── screen_design.md
│           └── behaviors.md
├── tasks/                      # 実装タスク（並行作業対応・複数ファイルに分割）
│   ├── tasks.md                    # メインタスクリスト（全体概要と担当割り当て）
│   ├── setup_tasks.md              # セットアップタスク
│   ├── common_tasks.md             # 共通機能タスク（共通エンティティ・サービス）
│   ├── F_001_book_search.md        # F_001: 書籍検索・閲覧
│   ├── F_002_shopping_cart.md      # F_002: ショッピングカート管理
│   ├── F_003_order_processing.md   # F_003: 注文処理
│   ├── F_004_customer_auth.md      # F_004: 顧客管理・認証
│   ├── F_005_order_history.md      # F_005: 注文履歴参照
│   └── integration_tasks.md        # 結合・テストタスク
├── enhancements/               # 拡張SPEC（002以降）
│   └── (拡張時に作成)
└── templates/                  # SPECテンプレート
```

### 実行手順

#### タスク分解フェーズ

SPECから実装タスクへ分解します。SPECを修正した場合や新規プロジェクトの場合に実行します。

**生成AIへの指示:**
```
@instructions/generate_tasks.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
SPEC: @projects/java/berry-books-mvc-sdd/specs/baseline/system/
から実装タスクリストを生成してください。

タスクリストは projects/java/berry-books-mvc-sdd/tasks/ ディレクトリに複数ファイルとして保存してください。
```

**生成されるタスクファイル（複数人での並行作業用に分割）:**
- `tasks/tasks.md` - メインタスクリスト（全体概要と担当割り当て）
- `tasks/setup_tasks.md` - セットアップタスク（全員が実行）
- `tasks/common_tasks.md` - 共通機能タスク（共通機能チーム：1-2名）
- `tasks/F_001_book_search.md` - F_001: 書籍検索・閲覧（担当者A）
- `tasks/F_002_shopping_cart.md` - F_002: ショッピングカート管理（担当者B）
- `tasks/F_003_order_processing.md` - F_003: 注文処理（担当者C）
- `tasks/F_004_customer_auth.md` - F_004: 顧客管理・認証（担当者D）
- `tasks/F_005_order_history.md` - F_005: 注文履歴参照（担当者E）
- `tasks/integration_tasks.md` - 結合・テストタスク（全員参加）

#### 実装フェーズ

タスクリストに従って、生成AIに実装を依頼します。以下の順序で各タスクを**1つずつ**実行してください。

**重要:** 各タスクは独立した作業単位です。1つのタスクが完了したら、次のタスクに進む前に内容を確認してください。

##### タスク1: セットアップタスクの実行

**目的:** プロジェクト初期化、開発環境構築、データベース設定

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/setup_tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 開発環境のセットアップ
- データベース初期化（DDL、初期データ）
- アプリケーションサーバー設定
- ログ設定

##### タスク2: 共通機能タスクの実行

**目的:** 複数機能で共有される共通コンポーネントの実装

**依存:** タスク1（setup_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/common_tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 共通エンティティ（Book, Category, Publisher, Stock等）
- 共通サービス（DeliveryFeeService等）
- 共通ユーティリティ
- 認証フィルター

##### タスク3: F_001 書籍検索・閲覧の実装

**依存:** タスク2（common_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_001_book_search.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 書籍検索機能
- 書籍詳細表示機能
- カバー画像表示対応

**注意:** タスク4～7と並行実行可能

##### タスク4: F_002 ショッピングカート管理の実装

**依存:** タスク2（common_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_002_shopping_cart.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- カートアイテム追加・削除・数量変更
- セッションベースのカート管理
- カート内容表示

**注意:** タスク3,5～7と並行実行可能

##### タスク5: F_003 注文処理の実装

**依存:** タスク2（common_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_003_order_processing.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 注文確定処理
- 配送先・決済方法選択
- 在庫減算（楽観的ロック対応）
- 送料計算

**注意:** タスク3,4,6,7と並行実行可能

##### タスク6: F_004 顧客管理・認証の実装

**依存:** タスク2（common_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_004_customer_auth.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 顧客登録機能
- ログイン・ログアウト機能
- 認証フィルター
- セッション管理

**注意:** タスク3～5,7と並行実行可能

##### タスク7: F_005 注文履歴参照の実装

**依存:** タスク2（common_tasks.md）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_005_order_history.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 注文履歴一覧表示
- 注文詳細表示
- 顧客ごとのフィルタリング

**注意:** タスク3～6と並行実行可能

##### タスク8: 結合・テストタスクの実行

**依存:** タスク3～7（全機能）完了後

```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/integration_tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**内容:**
- 機能間結合テスト
- エンドツーエンドテスト
- パフォーマンステスト
- 最終検証

---

**実装の依存関係:**
```
タスク1（setup_tasks.md）
    ↓
タスク2（common_tasks.md）
    ↓
    ├─→ タスク3（F_001_book_search.md）─┐
    ├─→ タスク4（F_002_shopping_cart.md）├─→ 並行実行可能
    ├─→ タスク5（F_003_order_processing.md）│
    ├─→ タスク6（F_004_customer_auth.md）│
    └─→ タスク7（F_005_order_history.md）─┘
                ↓
    タスク8（integration_tasks.md）
```

**重要なポイント:**
- **依存関係の遵守**: セットアップ → 共通機能 → 機能別実装 → 結合・テストの順序を守る
- **並行作業の判断**: タスク3～7は並行実行可能。人員配分は作業者が判断
- **進捗の追跡**: 各タスクファイルのチェックボックスで進捗を管理
- **SPEC参照**: 各タスク実行時、必要に応じて対応するSPEC（`specs/baseline/system/`、`specs/baseline/features/`）を参照

---

## 🚀 シナリオ2: 拡張の実行（機能追加・改善・修正）

既存のberry-booksプロジェクトを拡張する場合のワークフローです。

### ワークフロー

```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

**重要:** SPECの作成は手動で行います。`templates/` フォルダのテンプレートか、既存の `specs/` のSPECを参考にしてください。

### 重要な違い

| 項目 | ベースプロジェクト（001） | 拡張（002以降） |
|------|------------|---------|
| 憲章 | 既に存在 | 既存を参照 |
| アーキテクチャ | 全体設計 | 既存に準拠 |
| SPECの配置 | `specs/baseline/` | `specs/enhancements/YYYYMM_案件名/` |
| 実装範囲 | 全レイヤー | 必要な部分のみ |
| SPEC作成 | テンプレート参照 | テンプレート参照 |

### 個別拡張のSPEC配置

拡張内容ごとに専用のフォルダを作成します：

```
specs/
├── baseline/                        # 001: ベースプロジェクト（berry-books-mvc-sdd本体）
│   ├── system/                      # システム全体のSPEC
│   │   ├── requirements.md
│   │   ├── architecture_design.md
│   │   ├── functional_design.md
│   │   ├── behaviors.md
│   │   ├── data_model.md
│   │   ├── screen_design.md
│   │   └── external_interface.md
│   └── features/                    # 個別機能のSPEC
│       ├── F_001_book_search/
│       ├── F_002_shopping_cart/
│       ├── F_003_order_processing/
│       ├── F_004_customer_auth/
│       └── F_005_order_history/
├── tasks/                           # 実装タスク（ベースプロジェクト用）
│   ├── tasks.md                     # メインタスクリスト
│   ├── setup_tasks.md
│   ├── common_tasks.md
│   ├── F_001_book_search.md
│   ├── F_002_shopping_cart.md
│   ├── F_003_order_processing.md
│   ├── F_004_customer_auth.md
│   ├── F_005_order_history.md
│   └── integration_tasks.md
├── enhancements/                    # 拡張SPEC（機能追加・改善・修正）
│   ├── 202512_inventory_alert/     # 在庫アラート機能（新機能）
│   │   ├── requirements.md          # 拡張の要件定義
│   │   ├── architecture_design.md   # アーキテクチャの差分
│   │   ├── functional_design.md     # 機能設計
│   │   ├── behaviors.md             # 受入基準
│   │   ├── data_model.md            # データモデル仕様書の差分（必要に応じて）
│   │   ├── screen_design.md         # 画面設計（必要に応じて）
│   │   └── tasks/                   # 実装タスク（複数ファイルに分割可能）
│   │       ├── tasks.md             # メインタスクリスト
│   │       ├── setup_tasks.md       # セットアップタスク
│   │       └── feature_tasks.md     # 機能実装タスク
│   └── 202512_security_patch/      # セキュリティパッチ（修正）
│       ├── requirements.md
│       ├── functional_design.md
│       └── tasks/
│           └── tasks.md             # 実装タスク（小規模な場合は1ファイルでも可）
└── templates/                       # SPECテンプレート
    ├── requirements.md
    ├── architecture_design.md
    ├── functional_design.md
    ├── behaviors.md
    ├── data_model.md
    ├── screen_design.md
    ├── external_interface.md
    └── tasks.md
```

### 設計フェーズにおけるSPECの作成方法

**ステップ1: SPECを作成**

`specs/templates/` フォルダのテンプレートを使って、拡張内容のSPECを作成します。

例えば、`specs/enhancements/202512_inventory_alert/` に以下のファイルを作成：
- `requirements.md` - 要件定義書
- `functional_design.md` - 機能設計書
- `behaviors.md` - 振る舞い仕様書
- その他、必要に応じて

**ステップ2: テンプレートを参考に記述**

`specs/templates/` のテンプレートの構成を参考に、拡張内容の仕様を記述します。

**参考資料:**
- `specs/templates/` - 各SPECのテンプレート（構成と記述例）
- `specs/baseline/system/` - システム全体のSPEC（概要）
- `specs/baseline/features/` - 個別機能のSPEC（詳細設計の参考）

**ステップ3: 既存SPECとの整合性確認**

拡張内容のSPECが既存のアーキテクチャと整合していることを確認します：
- ベースプロジェクトの `specs/baseline/system/architecture_design.md` に準拠
- 既存機能の設計（`specs/baseline/features/`）を参考に
- 既存のクラスやテーブルとの連携を考慮
- 命名規則やコーディング規約を踏襲

---

## 📋 生成AIへの指示方法

このプロジェクトでは、`instructions/` フォルダにある3つのインストラクションファイルを使用して、生成AIに指示を出します。

### 利用可能なインストラクションファイル

```
instructions/
├── generate_tasks.md  # SPECから実装タスクへの分解
└── generate_code.md   # タスクリストに基づく実装
```

**注意:** `memory/` 配下の憲章ファイルは人間が作成しますが、AIはタスク生成・コード生成時にこれらを参照し、開発原則と品質基準を遵守します。

---

### インストラクション1: Tasks（タスク分解）

**目的:** 完成したSPECから具体的な実装タスクに分解します。複数人での並行作業を考慮して、複数のタスクファイルに分割されます。

**生成AIへの指示（ベースプロジェクト）:**
```
@instructions/generate_tasks.md このインストラクションに従って、
@specs/baseline/system/ のSPECから実装タスクリストを生成してください。

タスクリストは specs/tasks/ ディレクトリに複数ファイルとして保存してください。
```

**生成AIへの指示（拡張）:**
```
@instructions/generate_tasks.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
SPEC: @projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/
から実装タスクリストを生成してください。

タスクリストは projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/tasks/ ディレクトリに保存してください。
```

**生成されるファイル（ベースプロジェクト）:**
- `tasks/tasks.md` - メインタスクリスト（全体概要と担当割り当て）
- `tasks/setup_tasks.md` - セットアップタスク
- `tasks/common_tasks.md` - 共通機能タスク
- `tasks/F_001_book_search.md` - F_001: 書籍検索・閲覧
- `tasks/F_002_shopping_cart.md` - F_002: ショッピングカート管理
- `tasks/F_003_order_processing.md` - F_003: 注文処理
- `tasks/F_004_customer_auth.md` - F_004: 顧客管理・認証
- `tasks/F_005_order_history.md` - F_005: 注文履歴参照
- `tasks/integration_tasks.md` - 結合・テストタスク

**生成されるファイル（拡張の例）:**
- `specs/enhancements/202512_inventory_alert/tasks/tasks.md` - メインタスクリスト
- `specs/enhancements/202512_inventory_alert/tasks/setup_tasks.md` - セットアップタスク
- `specs/enhancements/202512_inventory_alert/tasks/feature_tasks.md` - 機能実装タスク

**インストラクションが参照するSPEC:**
- `requirements.md` - 要件定義書
- `architecture_design.md` - アーキテクチャ設計書
- `functional_design.md` - 機能設計書
- `data_model.md` - データモデル仕様書
- `screen_design.md` - 画面設計書
- `behaviors.md` - 振る舞い仕様書
- `external_interface.md` - 外部インターフェース仕様書
- `features/` - 個別機能の詳細設計

**タスク分割の利点:**
- **並行作業**: 複数人が同時に異なる機能を実装可能
- **明確な担当**: 各担当者が自分のタスクファイルに集中できる
- **進捗管理**: 機能ごとに進捗を追跡しやすい
- **柔軟性**: 規模に応じてタスクファイルの分割数を調整可能

---

### インストラクション2: Implement（実装）

**目的:** タスクリストに従って実装を進めます。

**重要:** プロジェクトルートとタスクリストのパスを明示的に指定します。

**基本的な指示方法:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @<プロジェクトルートのパス>
タスクリスト: @<タスクファイルのパス>
に基づいて実装を進めてください。
```

#### パターンA: 単独での実装（1人で全て実装）

**ベースプロジェクト:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/tasks.md
に基づいて実装を進めてください。

セットアップタスクから順に実行してください。
```

**拡張:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/tasks/tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

#### パターンB: チームでの並行実装（複数人で分担）

各担当者が自分のタスクファイルを指定して実装します。

**共通機能チーム:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/common_tasks.md
に基づいて実装を進めてください。
```

**担当者A（F_001: 書籍検索・閲覧）:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_001_book_search.md
に基づいて実装を進めてください。

注意: 共通機能タスク（common_tasks.md）の完了を待ってから開始してください。
```

**担当者B（F_002: ショッピングカート管理）:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_002_shopping_cart.md
に基づいて実装を進めてください。

注意: 共通機能タスク（common_tasks.md）の完了を待ってから開始してください。
```

**担当者C～E:** 同様に`F_003_order_processing.md`～`F_005_order_history.md`を指定

**実装対象:**
- ソースコード（Java、XHTML、CSS等）
- テストコード
- 設定ファイル

**インストラクションが参照するSPEC:**
- `tasks/` - 実装タスク分解（複数ファイル）
- `requirements.md` - 要件定義書
- `architecture_design.md` - アーキテクチャ設計書
- `functional_design.md` - 機能設計書
- `data_model.md` - データモデル仕様書
- `screen_design.md` - 画面設計書
- `behaviors.md` - 振る舞い仕様書
- `external_interface.md` - 外部インターフェース仕様書
- `features/` - 個別機能の詳細設計

**実装の進め方:**
1. **プロジェクトルートの明示**: 必ず`プロジェクトルート: @<パス>`を指定
2. **単独実装**: メインタスクリスト（`tasks/tasks.md`）を上から順に実行
3. **並行実装**: 各担当者が自分のタスクファイルを実行（共通機能完了後）
4. `[P]`マークのタスクは並行実行可能
5. 各タスク完了後、タスクファイルのチェックボックスを更新
6. フェーズごとに動作確認を実施

---

## 💡 重要なポイント

### 仕様駆動開発の実装フェーズに特化

このプロジェクトは、**完成したSPECを使って生成AIに実装させる**ことに焦点を当てています。

**重要な前提:**
1. **SPECは既に完成している** - `specs/baseline/` フォルダに全てのSPECが存在
2. **実装だけを生成AIに依頼** - タスク分解と実装のみを生成AIが担当
3. **SPECの作成は手動** - 拡張の場合、テンプレートを使って人間が作成

### インストラクションファイルの使い方

**3つのインストラクションファイル:**
- `instructions/constitution.md` - プロジェクト憲章の作成（初回のみ）
- `instructions/generate_tasks.md` - SPECから実装タスクへの分解
- `instructions/generate_code.md` - タスクリストに基づく実装

**基本的な実行フロー:**
```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

**重要:** プロジェクトルートを必ず明示的に指定してください。

**タスク分解の例:**
```
@instructions/generate_tasks.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
SPEC: @projects/java/berry-books-mvc-sdd/specs/baseline/system/
から実装タスクリストを生成してください。

タスクリストは projects/java/berry-books-mvc-sdd/tasks/ ディレクトリに複数ファイルとして保存してください。
```

**実装の例:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/tasks.md
に基づいて実装を進めてください。
```

### 拡張のSPEC作成

既存プロジェクトを拡張する場合、SPECは手動で作成します：

1. **テンプレートを参考に作成**: `specs/templates/` フォルダのテンプレートを参考に、拡張内容のSPECを作成
2. **既存SPECを参考**: `specs/baseline/system/` （概要）と `specs/baseline/features/` （詳細設計）を参考に記述スタイルを合わせる
3. **整合性を確認**: 既存のアーキテクチャと整合していることを確認

---

## 🎯 実際の開発例（シナリオ2: 拡張）

### 例1: 在庫アラート機能の追加（新機能）

**前提条件:**
- berry-books-mvc-sddプロジェクトが既に稼働している
- 既存の在庫管理機能（Stock エンティティ、楽観的ロック）がある
- 新機能は `specs/enhancements/202512_inventory_alert/` に配置

#### ステップ1: SPECの作成（手動）

`specs/templates/` フォルダのテンプレートを使って、`specs/enhancements/202512_inventory_alert/` に新機能のSPECを作成します：

**`specs/enhancements/202512_inventory_alert/requirements.md`:**
- 在庫数が閾値（例：5冊）を下回ったら管理者に通知
- 管理者画面で在庫アラート一覧を表示
- 書籍ごとに閾値を設定可能
- 在庫補充後、アラートを解除

**`specs/enhancements/202512_inventory_alert/functional_design.md`:**
- 既存のStockエンティティに閾値フィールドを追加
- 新規InventoryAlertエンティティとInventoryAlertDaoを設計
- 既存のOrderServiceでの在庫減少時にアラートチェックを追加

**`specs/enhancements/202512_inventory_alert/behaviors.md`:**
- Given-When-Then形式で受入基準を記述

**参考:** `specs/baseline/system/` （概要）、`specs/baseline/features/` （詳細設計）と `specs/templates/` のテンプレートを参考に記述してください。

#### ステップ2: タスク分解（生成AI）

**生成AIへの指示:**
```
@instructions/generate_tasks.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
SPEC: @projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/
から実装タスクリストを生成してください。

タスクリストは projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/tasks/ ディレクトリに保存してください。

注意: 既存ファイルの修正タスクと新規ファイル作成タスクを明確に区別してください。
```

**生成されるタスクファイル（拡張の規模に応じて）:**
- **小規模な拡張**: `tasks/tasks.md` のみ（1ファイル）
- **中規模以上の拡張**: 複数ファイルに分割（`tasks.md`, `setup_tasks.md`, `feature_tasks.md`等）

#### ステップ3: 実装（生成AI）

**生成AIへの指示:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/specs/enhancements/202512_inventory_alert/tasks/tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

**複数人で実装する場合:**
各担当者が個別のタスクファイルを指定して並行作業が可能です。

---

## 📁 プロジェクト構成

```
berry-books-mvc-sdd/
├── instructions/             # 生成AI用インストラクションファイル
│   ├── constitution.md       # 憲章作成インストラクション
│   ├── generate_tasks.md     # タスク分解インストラクション
│   └── generate_code.md      # 実装インストラクション
├── memory/                   # プロジェクト憲章（AIが参照）
│   └── constitution.md       # 開発憲章（開発原則、品質基準、組織標準）
├── specs/                    # SPEC（仕様書）
│   ├── baseline/             # 001: ベースラインSPEC
│   │   ├── system/           # システム全体のSPEC
│   │   │   ├── requirements.md       # 要件定義書
│   │   │   ├── architecture_design.md  # アーキテクチャ設計書
│   │   │   ├── functional_design.md  # 機能設計書
│   │   │   ├── behaviors.md          # 振る舞い仕様書
│   │   │   ├── data_model.md         # データモデル仕様書
│   │   │   ├── screen_design.md      # 画面設計書
│   │   │   └── external_interface.md # 外部インターフェース仕様書
│   │   └── features/         # 個別機能のSPEC（F_001～F_005）
│   │       ├── F_001_book_search/      # 書籍検索・閲覧
│   │       ├── F_002_shopping_cart/    # ショッピングカート管理
│   │       ├── F_003_order_processing/ # 注文処理
│   │       ├── F_004_customer_auth/    # 顧客管理・認証
│   │       └── F_005_order_history/    # 注文履歴参照
│   ├── tasks/                # 実装タスク（並行作業対応・複数ファイルに分割）
│   │   ├── tasks.md                       # メインタスクリスト（全体概要）
│   │   ├── setup_tasks.md                 # セットアップタスク
│   │   ├── common_tasks.md                # 共通機能タスク
│   │   ├── F_001_book_search.md           # F_001: 書籍検索・閲覧
│   │   ├── F_002_shopping_cart.md         # F_002: ショッピングカート管理
│   │   ├── F_003_order_processing.md      # F_003: 注文処理
│   │   ├── F_004_customer_auth.md         # F_004: 顧客管理・認証
│   │   ├── F_005_order_history.md         # F_005: 注文履歴参照
│   │   └── integration_tasks.md           # 結合・テストタスク
│   ├── enhancements/         # 拡張SPEC（アンダースコア区切り命名）
│   │   └── (拡張時に作成)
│   │       └── 202512_inventory_alert/  # 例: 在庫アラート機能（新機能）
│   │           ├── requirements.md
│   │           ├── functional_design.md
│   │           ├── behaviors.md
│   │           └── tasks/            # 拡張の実装タスク
│   │               ├── tasks.md              # メインタスクリスト
│   │               ├── setup_tasks.md        # セットアップタスク
│   │               └── feature_tasks.md      # 機能実装タスク
│   └── templates/            # SPECテンプレート
│       ├── requirements.md
│       ├── architecture_design.md
│       ├── functional_design.md
│       ├── behaviors.md
│       ├── data_model.md
│       ├── screen_design.md
│       ├── external_interface.md
│       └── tasks.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── webapp/
│   └── test/
├── sql/
│   └── hsqldb/
└── README.md
```

**主要フォルダの説明:**

- **`instructions/`**: 生成AIに指示を出すためのインストラクションファイル（2つ：generate_tasks.md, generate_code.md）
- **`memory/`**: プロジェクトの憲章（開発原則、品質基準）※人間が作成、AIが参照
- **`specs/baseline/system/`**: システム全体のベースラインSPEC（001: berry-books-mvc-sdd本体）
- **`specs/baseline/features/`**: 個別機能のSPEC（F_001～F_005の詳細設計）
- **`specs/tasks/`**: ベースプロジェクトの実装タスク（複数ファイルに分割、並行作業対応）
- **`specs/enhancements/`**: 拡張SPEC（002以降: 機能追加・改善・修正）
- **`specs/templates/`**: 拡張のSPECを作成する際のテンプレート
- **`src/`**: 実装コード（生成AIが生成）
- **`sql/`**: SQL（生成AIが生成）

**タスクファイルの特徴:**
- **複数ファイルに分割**: 機能ごとにタスクファイルを分割し、並行作業を容易に
- **担当者割り当て**: 各タスクファイルに推奨担当者数と役割を記載
- **依存関係管理**: メインタスクリスト（`tasks.md`）で全体の実行順序を管理

---

## 🔧 セットアップ

### 前提条件

- JDK 21以上
- Gradle 8.x以上
- Payara Server 6
- HSQLDB

---

## 📊 技術スタック

| カテゴリ | 技術 | バージョン |
|---------|------|----------|
| **Java** | JDK | 21+ |
| **Jakarta EE** | Platform | 10.0 |
| **JSF** | Jakarta Faces | 4.0 |
| **JPA** | Jakarta Persistence | 3.1 |
| **アプリケーションサーバー** | Payara Server | 6 |
| **データベース** | HSQLDB | 2.7.x |
| **ビルドツール** | Gradle | 8.x+ |

## 🖼️ 画像リソース

**書籍カバー画像:**
- 画像ファイルは `images/covers/` に格納（約50冊分）
- セットアップ時に `webapp/resources/covers/` にコピー
- ファイル名は書籍名と対応（例: `Java SEディープダイブ.jpg`）
- 画像ファイルが存在しない場合は `no-image.jpg` を表示

**画像ファイル名の生成:**
- BookエンティティのgetImageFileName()メソッドで書籍名 + ".jpg" を返す
- JSFビューでは `#{book.imageFileName}` で参照
- 例: `<h:graphicImage value="resources/covers/#{book.imageFileName}"/>`

---

## 📖 ドキュメント

### Constitution（開発憲章）
- [Constitution（開発憲章）](memory/constitution.md) - プロジェクト全体の開発原則
  - **重要**: AIはタスク生成・コード生成時にこの憲章を参照し、開発原則と品質基準を遵守します
  - `memory/` 配下には、組織やチームで共通的に使われる憲章ファイルも配置可能です

### Specifications（SPEC）

**Feature 001: berry-books-mvc-sdd（ベースプロジェクト）**

*システム全体のSPEC:*
- [requirements.md](specs/baseline/system/requirements.md) - 要件定義書
- [architecture_design.md](specs/baseline/system/architecture_design.md) - アーキテクチャ設計書
- [functional_design.md](specs/baseline/system/functional_design.md) - 機能設計書（基本設計）
- [behaviors.md](specs/baseline/system/behaviors.md) - 振る舞い仕様書（Acceptance Criteria）
- [data_model.md](specs/baseline/system/data_model.md) - データモデル仕様書
- [screen_design.md](specs/baseline/system/screen_design.md) - 画面設計書
- [external_interface.md](specs/baseline/system/external_interface.md) - 外部インターフェース仕様書

*個別機能のSPEC:*
- [F_001: 書籍検索・閲覧](specs/baseline/features/F_001_book_search/)
- [F_002: ショッピングカート管理](specs/baseline/features/F_002_shopping_cart/)
- [F_003: 注文処理](specs/baseline/features/F_003_order_processing/)
- [F_004: 顧客管理・認証](specs/baseline/features/F_004_customer_auth/)
- [F_005: 注文履歴参照](specs/baseline/features/F_005_order_history/)

**実装タスク（並行作業対応）:**
- [tasks.md](tasks/tasks.md) - メインタスクリスト（全体概要と担当割り当て）
- [setup_tasks.md](tasks/setup_tasks.md) - セットアップタスク
- [common_tasks.md](tasks/common_tasks.md) - 共通機能タスク
- [F_001_book_search.md](tasks/F_001_book_search.md) - F_001: 書籍検索・閲覧
- [F_002_shopping_cart.md](tasks/F_002_shopping_cart.md) - F_002: ショッピングカート管理
- [F_003_order_processing.md](tasks/F_003_order_processing.md) - F_003: 注文処理
- [F_004_customer_auth.md](tasks/F_004_customer_auth.md) - F_004: 顧客管理・認証
- [F_005_order_history.md](tasks/F_005_order_history.md) - F_005: 注文履歴参照
- [integration_tasks.md](tasks/integration_tasks.md) - 結合・テストタスク

**Feature 002以降: 個別拡張**
- 個別拡張は `specs/enhancements/YYYYMM_案件名/` のようなフォルダで管理（日付は6桁、案件名とはアンダースコア区切り）
- 例: `specs/enhancements/202512_inventory_alert/`（在庫アラート機能）, `specs/enhancements/202512_security_patch/`（セキュリティパッチ）など

### Templates（テンプレート）

拡張のSPECを作成する際のテンプレート：
- [requirements.md](specs/templates/requirements.md) - 要件定義書テンプレート
- [architecture_design.md](specs/templates/architecture_design.md) - アーキテクチャ設計書テンプレート
- [functional_design.md](specs/templates/functional_design.md) - 機能設計書テンプレート
- [behaviors.md](specs/templates/behaviors.md) - 振る舞い仕様書テンプレート
- [data_model.md](specs/templates/data_model.md) - データモデル仕様書テンプレート
- [screen_design.md](specs/templates/screen_design.md) - 画面設計書テンプレート
- [external_interface.md](specs/templates/external_interface.md) - 外部インターフェース仕様書テンプレート
- [tasks.md](specs/templates/tasks.md) - タスク分解テンプレート

### Instructions（インストラクション）

生成AIに指示を出すためのインストラクションファイル：
- [generate_tasks.md](instructions/generate_tasks.md) - タスク分解インストラクション
- [generate_code.md](instructions/generate_code.md) - 実装インストラクション

**注意:** `memory/` 配下の憲章ファイルは人間が作成しますが、AIはこれらを参照して開発を行います。

---

## 🎯 ワークフローのまとめ

### 基本的な流れ

**シナリオ1: ベースプロジェクトの実装（完成したSPECから）**

```
SPEC（既存） → タスク分解（AI） → 実装（AI）
```

1. **SPECの確認**: `specs/baseline/system/` フォルダの完成したSPECを確認
2. **Tasks（AI）**: `@instructions/generate_tasks.md` を使って実装タスクに分解
3. **Implement（AI）**: `@instructions/generate_code.md` を使ってタスクに従って実装

**シナリオ2: 拡張（機能追加・改善・修正）**

```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

1. **SPECの作成（手動）**: `specs/templates/` のテンプレートを参考に拡張内容のSPECを作成
2. **整合性確認**: 既存のSPECとの整合性を確認
3. **Tasks（AI）**: `@instructions/generate_tasks.md` を使って実装タスクに分解
4. **Implement（AI）**: `@instructions/generate_code.md` を使ってタスクに従って実装

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

**タスク分解:**
```
@instructions/generate_tasks.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
SPEC: @projects/java/berry-books-mvc-sdd/specs/baseline/system/
から実装タスクリストを生成してください。

タスクリストは projects/java/berry-books-mvc-sdd/tasks/ ディレクトリに複数ファイルとして保存してください。
```

**実装（単独）:**
```
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/setup_tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**注意:** 各タスクファイル（setup_tasks.md, common_tasks.md, F_001_book_search.md等）を順番に1つずつ実行してください。

**実装（並行作業）:**
```
# 共通機能チーム
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/common_tasks.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。

# 担当者A（F_001）
@instructions/generate_code.md このインストラクションに従って、
プロジェクトルート: @projects/java/berry-books-mvc-sdd
タスクリスト: @projects/java/berry-books-mvc-sdd/tasks/F_001_book_search.md
に基づいて実装を進めてください。

このタスクのみを実行し、完了したら停止してください。
```

**注意:** 各タスクは独立した作業単位です。タスクが完了したら、次のタスクに進む前に内容を確認してください。

### リバースエンジニアリング完了

本プロジェクトのベースラインSPECは、berry-books-mvcプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**完成したベースラインSPEC（`specs/baseline/system/`）:**
- ✅ requirements.md - 要件定義書（ビジネス要件、機能要件）
- ✅ architecture_design.md - アーキテクチャ設計書（技術スタック、アーキテクチャパターン、Mermaid図）
- ✅ functional_design.md - 機能設計書（クラス設計、ユーザーフロー）
- ✅ behaviors.md - 振る舞い仕様書（Acceptance Criteria、Gherkin形式）
- ✅ data_model.md - データモデル仕様書（ER図、テーブル定義）
- ✅ screen_design.md - 画面設計書（PlantUML形式、draw.io互換）
- ✅ external_interface.md - 外部インターフェース仕様書（REST API連携）

**完成した個別機能SPEC（`specs/baseline/features/`）:**
- ✅ F_001_book_search/ - 書籍検索・閲覧
- ✅ F_002_shopping_cart/ - ショッピングカート管理
- ✅ F_003_order_processing/ - 注文処理
- ✅ F_004_customer_auth/ - 顧客管理・認証
- ✅ F_005_order_history/ - 注文履歴参照

**完成した実装タスク（`tasks/`・並行作業対応）:**
- ✅ tasks.md - メインタスクリスト（全体概要と担当割り当て）
- ✅ setup_tasks.md - セットアップタスク
- ✅ common_tasks.md - 共通機能タスク
- ✅ F_001_book_search.md ～ F_005_order_history.md - 機能別タスク（並行実行可能）
- ✅ integration_tasks.md - 結合・テストタスク

これらのSPECと生成AIインストラクションを使って、生成AIが同じアプリケーションを完全に再実装できます。また、複数人での並行作業にも対応しています。

---

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

---

## 🔗 関連リンク

- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
- [PlantUML](https://plantuml.com/)
- [Mermaid](https://mermaid.js.org/)

