# berry-books-mvc-sdd プロジェクト

## 📖 概要

Jakarta EE 10によるオンライン書店「**Berry Books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、完成したSPECから生成AIによる実装を実践します。

---

## 🎯 このプロジェクトの位置付け

### 完成したSPECが存在する
**このプロジェクト（berry-books-mvc-sdd）は、完成したベースラインSPECが `specs/baseline/system/` に用意されています。**

仕様駆動開発の実装フェーズに焦点を当てており、**生成AIにSPECを読ませて実装させる**ことを目的としています。

### ベースラインSPECの構造
- **`specs/baseline/system/`**: システム全体のアーキテクチャ・設計・要件を定義したSPEC（概要）
- **`specs/baseline/features/`**: 個別機能のSPEC（F-001～F-005の詳細設計）

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

**SPECの配置:** `specs/enhancements/YYYY-MM-案件名/` のような個別フォルダに配置

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
│   │   ├── external_interface.md   # 外部インターフェース仕様書
│   │   └── tasks.md                # 実装タスク分解（100以上のタスク）
│   └── features/               # 個別機能のSPEC
│       ├── F-001-book-search/      # 書籍検索・閲覧
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F-002-shopping-cart/    # ショッピングカート管理
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F-003-order-processing/ # 注文処理
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       ├── F-004-customer-auth/    # 顧客管理・認証
│       │   ├── functional_design.md
│       │   ├── screen_design.md
│       │   └── behaviors.md
│       └── F-005-order-history/    # 注文履歴参照
│           ├── functional_design.md
│           ├── screen_design.md
│           └── behaviors.md
├── enhancements/               # 拡張SPEC（002以降）
│   └── (拡張時に作成)
└── templates/                  # SPECテンプレート
```

### 実装の実行手順

**ステップ1: タスク分解の実行（必要に応じて）**

既に `specs/baseline/system/tasks.md` が存在しますが、SPECを修正した場合や新規プロジェクトの場合は、タスク分解を実行します。

**生成AIへの指示:**
```
@instructions/tasks.md このインストラクションに従って、
@specs/baseline/system/ のSPECからタスクリストを生成してください。

生成したタスクリストは specs/baseline/system/tasks.md に保存してください。
```

**ステップ2: 実装の実行**

タスクリストに従って、生成AIに実装を依頼します。

**生成AIへの指示:**
```
@instructions/implement.md このインストラクションに従って、
@specs/baseline/system/tasks.md のタスクリストに基づいて実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

**重要なポイント:**
- 生成AIは `instructions/implement.md` を読み、そこに記載された指示に従います
- `specs/baseline/` 配下の全てのSPECを参照しながら実装を進めます
- `tasks.md` のタスクを上から順に実行し、完了したタスクにチェックマークを付けます

**実装の進め方:**
1. **フェーズ2（永続化レイヤー）から開始** - データベーススキーマとJPAエンティティの実装
2. `tasks.md`のタスクを上から順に実行
3. `[P]`マークのタスクは並行実行可能
4. 各タスク完了後、`tasks.md`のチェックボックスを更新
5. フェーズごとに動作確認を実施

**実装フェーズの概要:**
- **フェーズ2**: 永続化レイヤー（データベーススキーマ、JPAエンティティ）
- **フェーズ3**: データアクセスレイヤー（DAO実装）
- **フェーズ4**: ビジネスロジックレイヤー（サービス実装）
- **フェーズ5**: プレゼンテーションレイヤー（Managed Bean実装）
- **フェーズ6**: ビューレイヤー（XHTML、CSS実装）
- **フェーズ7**: テスト（ユニットテスト、結合テスト）
- **フェーズ8**: デプロイとドキュメント

**生成AIが参照するSPEC:**
- `specs/baseline/system/requirements.md` - 要件定義書
- `specs/baseline/system/architecture_design.md` - アーキテクチャ設計書
- `specs/baseline/system/functional_design.md` - 機能設計書
- `specs/baseline/system/behaviors.md` - 振る舞い仕様書
- `specs/baseline/system/data_model.md` - データモデル仕様書
- `specs/baseline/system/screen_design.md` - 画面設計書
- `specs/baseline/system/external_interface.md` - 外部インターフェース仕様書

**注意:** `tasks.md`の「フェーズ1: インフラストラクチャセットアップ」は、仕様駆動開発の対象外です。このフェーズは事前にセットアップ済みであることを前提としています

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
| SPECの配置 | `specs/baseline/` | `specs/enhancements/YYYY-MM-案件名/` |
| 実装範囲 | 全レイヤー | 必要な部分のみ |
| SPEC作成 | テンプレート参照 | テンプレート参照 |

### 個別拡張のSPEC配置

拡張内容ごとに専用のフォルダを作成します：

```
specs/
├── baseline/                        # 001: ベースプロジェクト（berry-books-mvc-sdd本体）
│   ├── requirements.md
│   ├── architecture_design.md
│   ├── functional_design.md
│   ├── behaviors.md
│   ├── data_model.md
│   ├── screen_design.md
│   ├── external_interface.md
│   └── tasks.md
├── enhancements/                    # 拡張SPEC（機能追加・改善・修正）
│   ├── 2025-12-inventory-alert/     # 在庫アラート機能（新機能）
│   │   ├── requirements.md          # 拡張の要件定義
│   │   ├── architecture_design.md   # アーキテクチャの差分
│   │   ├── functional_design.md     # 機能設計
│   │   ├── behaviors.md             # 受入基準
│   │   ├── data_model.md            # データモデル仕様書の差分（必要に応じて）
│   │   ├── screen_design.md         # 画面設計（必要に応じて）
│   │   └── tasks.md                 # 実装タスク
│   └── 2025-12-security-patch/      # セキュリティパッチ（修正）
│       ├── requirements.md
│       ├── functional_design.md
│       └── tasks.md
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

### SPECの作成方法

**ステップ1: SPECを作成**

`specs/templates/` フォルダのテンプレートを使って、拡張内容のSPECを作成します。

例えば、`specs/enhancements/2025-12-inventory-alert/` に以下のファイルを作成：
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
├── constitution.md  # プロジェクト憲章の作成・更新
├── tasks.md         # SPECから実装タスクへの分解
└── implement.md     # タスクリストに基づく実装
```

---

### インストラクション1: Constitution（憲章の作成）

**目的:** プロジェクトの開発原則と憲章を作成・更新します。

**生成AIへの指示:**
```
@instructions/constitution.md このインストラクションに従って、
プロジェクトの憲章を memory/constitution.md に作成してください。
```

**生成されるファイル:**
- `memory/constitution.md` - プロジェクトの開発原則と憲章

**注意:** 既にベースプロジェクトの憲章が存在する場合は、このステップはスキップできます。

---

### インストラクション2: Tasks（タスク分解）

**目的:** 完成したSPECから具体的な実装タスクに分解します。

**生成AIへの指示:**
```
@instructions/tasks.md このインストラクションに従って、
@specs/baseline/system/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/baseline/system/tasks.md に保存してください。
```

または、拡張の場合：

```
@instructions/tasks.md このインストラクションに従って、
@specs/enhancements/2025-12-inventory-alert/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/enhancements/2025-12-inventory-alert/tasks.md に保存してください。
```

**生成されるファイル:**
- **ベースプロジェクト**: `specs/baseline/system/tasks.md`
- **拡張**: `specs/enhancements/2025-12-inventory-alert/tasks.md`（例）

**インストラクションが参照するSPEC:**
- `requirements.md` - 要件定義書
- `architecture_design.md` - アーキテクチャ設計書
- `functional_design.md` - 機能設計書
- `data_model.md` - データモデル仕様書
- `screen_design.md` - 画面設計書
- `behaviors.md` - 振る舞い仕様書
- `external_interface.md` - 外部インターフェース仕様書

**既存のタスク例:**
- `specs/baseline/system/tasks.md` - berry-books-mvc-sdd（001）の100以上の実装タスク、依存関係付き

---

### インストラクション3: Implement（実装）

**目的:** タスクリストに従って実装を進めます。

**生成AIへの指示:**
```
@instructions/implement.md このインストラクションに従って、
@specs/baseline/system/tasks.md のタスクリストに基づいて実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

または、拡張の場合：

```
@instructions/implement.md このインストラクションに従って、
@specs/enhancements/2025-12-inventory-alert/tasks.md のタスクリストに基づいて実装を進めてください。

注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

**実装対象:**
- ソースコード（Java、XHTML、CSS等）
- テストコード
- 設定ファイル

**インストラクションが参照するSPEC:**
- `tasks.md` - 実装タスク分解
- `requirements.md` - 要件定義書
- `architecture_design.md` - アーキテクチャ設計書
- `functional_design.md` - 機能設計書
- `data_model.md` - データモデル仕様書
- `screen_design.md` - 画面設計書
- `behaviors.md` - 振る舞い仕様書
- `external_interface.md` - 外部インターフェース仕様書

**実装の進め方:**
1. タスクリストを上から順に実行
2. `[P]`マークのタスクは並行実行可能
3. 各タスク完了後、`tasks.md`のチェックボックスを更新
4. フェーズごとに動作確認を実施

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
- `instructions/tasks.md` - SPECから実装タスクへの分解
- `instructions/implement.md` - タスクリストに基づく実装

**基本的な実行フロー:**
```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

```
@instructions/<インストラクション名>.md このインストラクションに従って、
@specs/baseline/ のSPECから<具体的な指示内容>
```

**例:**
```
@instructions/tasks.md このインストラクションに従って、
@specs/baseline/ のSPECから実装タスクリストを生成してください。
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
- 新機能は `specs/enhancements/2025-12-inventory-alert/` に配置

#### ステップ1: SPECの作成（手動）

`specs/templates/` フォルダのテンプレートを使って、`specs/enhancements/2025-12-inventory-alert/` に新機能のSPECを作成します：

**`specs/enhancements/2025-12-inventory-alert/requirements.md`:**
- 在庫数が閾値（例：5冊）を下回ったら管理者に通知
- 管理者画面で在庫アラート一覧を表示
- 書籍ごとに閾値を設定可能
- 在庫補充後、アラートを解除

**`specs/enhancements/2025-12-inventory-alert/functional_design.md`:**
- 既存のStockエンティティに閾値フィールドを追加
- 新規InventoryAlertエンティティとInventoryAlertDaoを設計
- 既存のOrderServiceでの在庫減少時にアラートチェックを追加

**`specs/enhancements/2025-12-inventory-alert/behaviors.md`:**
- Given-When-Then形式で受入基準を記述

**参考:** `specs/baseline/system/` （概要）、`specs/baseline/features/` （詳細設計）と `specs/templates/` のテンプレートを参考に記述してください。

#### ステップ2: タスク分解（生成AI）

**生成AIへの指示:**
```
@instructions/tasks.md このインストラクションに従って、
@specs/enhancements/2025-12-inventory-alert/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/enhancements/2025-12-inventory-alert/tasks.md に保存してください。

注意: 既存ファイルの修正タスクと新規ファイル作成タスクを明確に区別してください。
```

#### ステップ3: 実装（生成AI）

**生成AIへの指示:**
```
@instructions/implement.md このインストラクションに従って、
@specs/enhancements/2025-12-inventory-alert/tasks.md のタスクリストに基づいて実装を進めてください。

注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

---

## 📁 プロジェクト構成

```
berry-books-mvc-sdd/
├── instructions/             # 生成AI用インストラクションファイル
│   ├── constitution.md       # 憲章作成インストラクション
│   ├── tasks.md              # タスク分解インストラクション
│   └── implement.md          # 実装インストラクション
├── memory/                   # プロジェクトの記憶
│   └── constitution.md       # 開発憲章
├── specs/                    # SPEC（仕様書）
│   ├── baseline/             # 001: ベースラインSPEC
│   │   ├── system/           # システム全体のSPEC
│   │   │   ├── requirements.md       # 要件定義書
│   │   │   ├── architecture_design.md  # アーキテクチャ設計書
│   │   │   ├── functional_design.md  # 機能設計書
│   │   │   ├── behaviors.md          # 振る舞い仕様書
│   │   │   ├── data_model.md         # データモデル仕様書
│   │   │   ├── screen_design.md      # 画面設計書
│   │   │   ├── external_interface.md # 外部インターフェース仕様書
│   │   │   └── tasks.md              # 実装タスク分解
│   │   └── features/         # 個別機能のSPEC（F-001～F-005）
│   │       ├── F-001-book-search/      # 書籍検索・閲覧
│   │       ├── F-002-shopping-cart/    # ショッピングカート管理
│   │       ├── F-003-order-processing/ # 注文処理
│   │       ├── F-004-customer-auth/    # 顧客管理・認証
│   │       └── F-005-order-history/    # 注文履歴参照
│   ├── enhancements/         # 拡張SPEC（日付ベース命名）
│   │   └── (拡張時に作成)
│   │       └── 2025-12-inventory-alert/  # 例: 在庫アラート機能（新機能）
│   │           ├── requirements.md
│   │           ├── functional_design.md
│   │           ├── behaviors.md
│   │           └── tasks.md
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

- **`instructions/`**: 生成AIに指示を出すためのインストラクションファイル（3つ）
- **`memory/`**: プロジェクトの憲章（開発原則）
- **`specs/baseline/system/`**: システム全体のベースラインSPEC（001: berry-books-mvc-sdd本体）
- **`specs/baseline/features/`**: 個別機能のSPEC（将来用）
- **`specs/enhancements/`**: 拡張SPEC（002以降: 機能追加・改善・修正）
- **`specs/templates/`**: 拡張のSPECを作成する際のテンプレート
- **`src/`**: 実装コード（生成AIが生成）
- **`sql/`**: SQL（生成AIが生成）

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

---

## 📖 ドキュメント

### Constitution（開発憲章）
- [Constitution（開発憲章）](memory/constitution.md) - プロジェクト全体の開発原則

### Specifications（SPEC）

**Feature 001: berry-books-mvc-sdd（ベースプロジェクト）**
- [requirements.md](specs/baseline/system/requirements.md) - 要件定義書
- [architecture_design.md](specs/baseline/system/architecture_design.md) - アーキテクチャ設計書
- [functional_design.md](specs/baseline/system/functional_design.md) - 機能設計書（基本設計）
- [behaviors.md](specs/baseline/system/behaviors.md) - 振る舞い仕様書（Acceptance Criteria）
- [data_model.md](specs/baseline/system/data_model.md) - データモデル仕様書
- [screen_design.md](specs/baseline/system/screen_design.md) - 画面設計書
- [external_interface.md](specs/baseline/system/external_interface.md) - 外部インターフェース仕様書
- [tasks.md](specs/baseline/system/tasks.md) - タスク分解

**Feature 002以降: 個別拡張**
- 個別拡張は `specs/enhancements/YYYY-MM-案件名/` のようなフォルダで管理（日付ベース命名）
- 例: `specs/enhancements/2025-12-inventory-alert/`（在庫アラート機能）, `specs/enhancements/2025-12-security-patch/`（セキュリティパッチ）など

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
- [constitution.md](instructions/constitution.md) - プロジェクト憲章作成インストラクション
- [tasks.md](instructions/tasks.md) - タスク分解インストラクション
- [implement.md](instructions/implement.md) - 実装インストラクション

---

## 🎯 ワークフローのまとめ

### 基本的な流れ

**シナリオ1: ベースプロジェクトの実装（完成したSPECから）**

```
SPEC（既存） → タスク分解（AI） → 実装（AI）
```

1. **SPECの確認**: `specs/baseline/system/` フォルダの完成したSPECを確認
2. **Tasks（AI）**: `@instructions/tasks.md` を使って実装タスクに分解
3. **Implement（AI）**: `@instructions/implement.md` を使ってタスクに従って実装

**シナリオ2: 拡張（機能追加・改善・修正）**

```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

1. **SPECの作成（手動）**: `specs/templates/` のテンプレートを参考に拡張内容のSPECを作成
2. **整合性確認**: 既存のSPECとの整合性を確認
3. **Tasks（AI）**: `@instructions/tasks.md` を使って実装タスクに分解
4. **Implement（AI）**: `@instructions/implement.md` を使ってタスクに従って実装

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

**タスク分解:**
```
@instructions/tasks.md このインストラクションに従って、
@specs/baseline/system/ のSPECから実装タスクリストを生成してください。
```

**実装:**
```
@instructions/implement.md このインストラクションに従って、
@specs/baseline/system/tasks.md のタスクリストに基づいて実装を進めてください。
```

### リバースエンジニアリング完了

本プロジェクトのベースラインSPECは、berry-books-mvcプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**完成したベースラインSPEC（`specs/baseline/system/`）:**
- ✅ requirements.md - 要件定義書（ビジネス要件、機能要件）
- ✅ architecture_design.md - アーキテクチャ設計書（技術スタック、アーキテクチャパターン、Mermaid図）
- ✅ functional_design.md - 機能設計書（クラス設計、ユーザーフロー）
- ✅ behaviors.md - 振る舞い仕様書（Acceptance Criteria、Gherkin形式）
- ✅ data_model.md - データモデル仕様書（ER図、テーブル定義）
- ✅ screen_design.md - 画面設計書（PlantUML形式、draw.io互換）
- ✅ external_interface.md - 外部インターフェース仕様書（テンプレート）
- ✅ tasks.md - 実装タスク分解（100以上のタスク、依存関係付き）

これらのSPECと生成AIインストラクションを使って、生成AIが同じアプリケーションを完全に再実装できます。

---

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

---

## 🔗 関連リンク

- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
- [PlantUML](https://plantuml.com/)
- [Mermaid](https://mermaid.js.org/)
