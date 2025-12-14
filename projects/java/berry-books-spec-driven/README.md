# berry-books-spec-driven プロジェクト

## 📖 概要

Jakarta EE 10によるオンライン書店「**berry-books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、完成したSPECから生成AIによる実装を実践します。

---

## 🎯 このプロジェクトの位置付け

### 完成したSPECが存在する
**このプロジェクト（berry-books）は、完成したSPECが `specs/` に用意されています。**

仕様駆動開発の実装フェーズに焦点を当てており、**生成AIにSPECを読ませて実装させる**ことを目的としています。

### 2つの利用シナリオ

#### シナリオ1: berry-booksの実装（完成したSPECから）
完成したSPECを使って、生成AIにアプリケーション全体を実装させます。

**対象:**
- 仕様駆動開発の実装フェーズの学習
- 生成AIによるコード生成の実践
- 完全なSPECからの実装体験

**実装の開始方法:** 下記の「シナリオ1: 実装フェーズの実行」を参照

#### シナリオ2: 既存プロジェクトへの機能追加
既に稼働しているberry-booksに新機能を追加する場合。既存のアーキテクチャを前提に、新機能のSPECを作成し、実装します。

**対象:**
- 新機能の追加（例: 在庫アラート機能）
- 既存機能の改善・拡張
- 新しいユーザーストーリーの実装

**SPECの作成方法:** 下記の「シナリオ2: 機能追加の実行」を参照

**SPECの配置:** `specs/002-feature-name/` のような個別フォルダに配置

---

## 📦 シナリオ1: 実装フェーズの実行（完成したSPECから）

**このプロジェクト（berry-books）は、完成したSPECが `specs/` に用意されています。**

### 既存のSPECファイル

```
specs/
├── requirements.md         # 要件定義書（What & Why）
├── architecture_design.md  # アーキテクチャ設計書
├── functional_design.md    # 機能設計書（基本設計、クラス設計含む）
├── behaviors.md            # 振る舞い仕様書（Acceptance Criteria）
├── data_model.md           # データモデル仕様書（ER図、テーブル定義）
├── screen_design.md        # 画面設計書（PlantUML形式）
├── external_interface.md   # 外部インターフェース仕様書
└── tasks.md                # 実装タスク分解（100以上のタスク）
```

### 実装の実行手順

**ステップ1: タスク分解の実行（必要に応じて）**

既に `tasks.md` が存在しますが、SPECを修正した場合や新規プロジェクトの場合は、タスク分解を実行します。

**生成AIへの指示:**
```
@prompts/tasks.md このプロンプトに従って、
@specs/ のSPECからタスクリストを生成してください。

生成したタスクリストは specs/tasks.md に保存してください。
```

**ステップ2: 実装の実行**

タスクリストに従って、生成AIに実装を依頼します。

**生成AIへの指示:**
```
@prompts/implement.md このプロンプトに従って、
@specs/tasks.md のタスクリストに基づいて実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

**重要なポイント:**
- 生成AIは `prompts/implement.md` を読み、そこに記載された指示に従います
- `specs/` 配下の全てのSPECを参照しながら実装を進めます
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
- `specs/requirements.md` - 要件定義書
- `specs/architecture_design.md` - アーキテクチャ設計書
- `specs/functional_design.md` - 機能設計書
- `specs/behaviors.md` - 振る舞い仕様書
- `specs/data_model.md` - データモデル仕様書
- `specs/screen_design.md` - 画面設計書
- `specs/external_interface.md` - 外部インターフェース仕様書

**注意:** `tasks.md`の「フェーズ1: インフラストラクチャセットアップ」は、仕様駆動開発の対象外です。このフェーズは事前にセットアップ済みであることを前提としています

---

## 🚀 シナリオ2: 機能追加の実行（新規featureの追加）

既存のberry-booksプロジェクトに新機能を追加する場合のワークフローです。

### ワークフロー

```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

**重要:** SPECの作成は手動で行います。`templates/` フォルダのテンプレートか、既存の `specs/` のSPECを参考にしてください。

### 重要な違い

| 項目 | ベースプロジェクト（001） | 機能追加（002以降） |
|------|------------|---------|
| 憲章 | 既に存在 | 既存を参照 |
| アーキテクチャ | 全体設計 | 既存に準拠 |
| SPECの配置 | `specs/` | `specs/002-feature-name/` |
| 実装範囲 | 全レイヤー | 必要な部分のみ |
| SPEC作成 | テンプレート参照 | テンプレート参照 |

### 個別featureのSPEC配置

新機能ごとに専用のフォルダを作成します：

```
specs/
├── requirements.md              # 001: ベースプロジェクト（berry-books本体）
├── architecture_design.md
├── functional_design.md
├── behaviors.md
├── data_model.md
├── screen_design.md
├── external_interface.md
├── tasks.md
└── 002-inventory-alert/         # 002: 在庫アラート機能
    ├── requirements.md          # 新機能の要件定義
    ├── architecture_design.md   # アーキテクチャの差分
    ├── functional_design.md     # 機能設計
    ├── behaviors.md             # 受入基準
    ├── data_model.md            # データモデル仕様書の差分（必要に応じて）
    ├── screen_design.md         # 画面設計（必要に応じて）
    └── tasks.md                 # 実装タスク
└── 003-message-properties/     # 003: メッセージのプロパティ化
    ├── requirements.md
    ├── functional_design.md
    └── tasks.md
```

### SPECの作成方法

**ステップ1: SPECを作成**

`templates/` フォルダのテンプレートを使って、新機能のSPECを作成します。

例えば、`specs/002-inventory-alert/` に以下のファイルを作成：
- `requirements.md` - 要件定義書
- `functional_design.md` - 機能設計書
- `behaviors.md` - 振る舞い仕様書
- その他、必要に応じて

**ステップ2: テンプレートを参考に記述**

`templates/` のテンプレートの構成を参考に、新機能の仕様を記述します。

**参考資料:**
- `templates/` - 各SPECのテンプレート（構成と記述例）
- `specs/` - ベースプロジェクトの完成したSPEC（記述の参考）

**ステップ3: 既存SPECとの整合性確認**

新機能のSPECが既存のアーキテクチャと整合していることを確認します：
- ベースプロジェクトの `specs/architecture_design.md` に準拠
- 既存のクラスやテーブルとの連携を考慮
- 命名規則やコーディング規約を踏襲

---

## 📋 生成AIへの指示方法

このプロジェクトでは、`prompts/` フォルダにある3つのプロンプトファイルを使用して、生成AIに指示を出します。

### 利用可能なプロンプトファイル

```
prompts/
├── constitution.md  # プロジェクト憲章の作成・更新
├── tasks.md         # SPECから実装タスクへの分解
└── implement.md     # タスクリストに基づく実装
```

---

### プロンプト1: Constitution（憲章の作成）

**目的:** プロジェクトの開発原則と憲章を作成・更新します。

**生成AIへの指示:**
```
@prompts/constitution.md このプロンプトに従って、
プロジェクトの憲章を memory/constitution.md に作成してください。
```

**生成されるファイル:**
- `memory/constitution.md` - プロジェクトの開発原則と憲章

**注意:** 既にベースプロジェクトの憲章が存在する場合は、このステップはスキップできます。

---

### プロンプト2: Tasks（タスク分解）

**目的:** 完成したSPECから具体的な実装タスクに分解します。

**生成AIへの指示:**
```
@prompts/tasks.md このプロンプトに従って、
@specs/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/tasks.md に保存してください。
```

または、機能追加の場合：

```
@prompts/tasks.md このプロンプトに従って、
@specs/002-inventory-alert/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/002-inventory-alert/tasks.md に保存してください。
```

**生成されるファイル:**
- **ベースプロジェクト**: `specs/tasks.md`
- **機能追加**: `specs/002-inventory-alert/tasks.md`（例）

**プロンプトが参照するSPEC:**
- `requirements.md` - 要件定義書
- `architecture_design.md` - アーキテクチャ設計書
- `functional_design.md` - 機能設計書
- `data_model.md` - データモデル仕様書
- `screen_design.md` - 画面設計書
- `behaviors.md` - 振る舞い仕様書
- `external_interface.md` - 外部インターフェース仕様書

**既存のタスク例:**
- `specs/tasks.md` - berry-books（001）の100以上の実装タスク、依存関係付き

---

### プロンプト3: Implement（実装）

**目的:** タスクリストに従って実装を進めます。

**生成AIへの指示:**
```
@prompts/implement.md このプロンプトに従って、
@specs/tasks.md のタスクリストに基づいて実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

または、機能追加の場合：

```
@prompts/implement.md このプロンプトに従って、
@specs/002-inventory-alert/tasks.md のタスクリストに基づいて実装を進めてください。

注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

**実装対象:**
- ソースコード（Java、XHTML、CSS等）
- テストコード
- 設定ファイル

**プロンプトが参照するSPEC:**
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
1. **SPECは既に完成している** - `specs/` フォルダに全てのSPECが存在
2. **実装だけを生成AIに依頼** - タスク分解と実装のみを生成AIが担当
3. **SPECの作成は手動** - 新規featureの場合、テンプレートを使って人間が作成

### プロンプトファイルの使い方

**3つのプロンプトファイル:**
- `prompts/constitution.md` - プロジェクト憲章の作成（初回のみ）
- `prompts/tasks.md` - SPECから実装タスクへの分解
- `prompts/implement.md` - タスクリストに基づく実装

**基本的な実行フロー:**
```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

```
@prompts/<プロンプト名>.md このプロンプトに従って、
@specs/ のSPECから<具体的な指示内容>
```

**例:**
```
@prompts/tasks.md このプロンプトに従って、
@specs/ のSPECから実装タスクリストを生成してください。
```

### 新規featureのSPEC作成

新規featureを追加する場合、SPECは手動で作成します：

1. **テンプレートを参考に作成**: `templates/` フォルダのテンプレートを参考に、新機能のSPECを作成
2. **既存SPECを参考**: `specs/` の既存SPECを参考に記述スタイルを合わせる
3. **整合性を確認**: 既存のアーキテクチャと整合していることを確認

---

## 🎯 実際の開発例（シナリオ2: 機能追加）

### 例1: 在庫アラート機能の追加

**前提条件:**
- berry-booksプロジェクトが既に稼働している
- 既存の在庫管理機能（Stock エンティティ、楽観的ロック）がある
- 新機能は `specs/002-inventory-alert/` に配置

#### ステップ1: SPECの作成（手動）

`templates/` フォルダのテンプレートを使って、`specs/002-inventory-alert/` に新機能のSPECを作成します：

**`specs/002-inventory-alert/requirements.md`:**
- 在庫数が閾値（例：5冊）を下回ったら管理者に通知
- 管理者画面で在庫アラート一覧を表示
- 書籍ごとに閾値を設定可能
- 在庫補充後、アラートを解除

**`specs/002-inventory-alert/functional_design.md`:**
- 既存のStockエンティティに閾値フィールドを追加
- 新規InventoryAlertエンティティとInventoryAlertDaoを設計
- 既存のOrderServiceでの在庫減少時にアラートチェックを追加

**`specs/002-inventory-alert/behaviors.md`:**
- Given-When-Then形式で受入基準を記述

**参考:** `specs/` の既存SPECと `templates/` のテンプレートを参考に記述してください。

#### ステップ2: タスク分解（生成AI）

**生成AIへの指示:**
```
@prompts/tasks.md このプロンプトに従って、
@specs/002-inventory-alert/ のSPECから実装タスクリストを生成してください。

生成したタスクリストは specs/002-inventory-alert/tasks.md に保存してください。

注意: 既存ファイルの修正タスクと新規ファイル作成タスクを明確に区別してください。
```

#### ステップ3: 実装（生成AI）

**生成AIへの指示:**
```
@prompts/implement.md このプロンプトに従って、
@specs/002-inventory-alert/tasks.md のタスクリストに基づいて実装を進めてください。

注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

---

## 📁 プロジェクト構成

```
berry-books-spec-driven/
├── prompts/                  # 生成AI用プロンプトファイル
│   ├── constitution.md       # 憲章作成プロンプト
│   ├── tasks.md              # タスク分解プロンプト
│   └── implement.md          # 実装プロンプト
├── templates/                # SPECテンプレート
│   ├── requirements_template.md
│   ├── architecture_design_template.md
│   ├── functional_design_template.md
│   ├── behaviors_template.md
│   ├── data_model_template.md
│   ├── screen_design_template.md
│   ├── external_interface_template.md
│   └── tasks_template.md
├── memory/                   # プロジェクトの記憶
│   └── constitution.md       # 開発憲章
├── specs/                    # 完成したSPEC（ベースプロジェクト）
│   ├── requirements.md       # 001: 要件定義書
│   ├── architecture_design.md  # 001: アーキテクチャ設計書
│   ├── functional_design.md  # 001: 機能設計書
│   ├── behaviors.md          # 001: 振る舞い仕様書
│   ├── data_model.md         # 001: データモデル仕様書
│   ├── screen_design.md      # 001: 画面設計書
│   ├── external_interface.md # 001: 外部インターフェース仕様書
│   ├── tasks.md              # 001: 実装タスク分解
│   └── 002-inventory-alert/  # 002: 個別機能（例: 在庫アラート機能）
│       ├── requirements.md
│       ├── functional_design.md
│       ├── behaviors.md
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

- **`prompts/`**: 生成AIに指示を出すためのプロンプトファイル（3つ）
- **`templates/`**: 新規featureのSPECを作成する際のテンプレート
- **`memory/`**: プロジェクトの憲章（開発原則）
- **`specs/`**: 完成したSPEC（ベースプロジェクト + 個別feature）
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

**Feature 001: berry-books（ベースプロジェクト）**
- [requirements.md](specs/requirements.md) - 要件定義書
- [architecture_design.md](specs/architecture_design.md) - アーキテクチャ設計書
- [functional_design.md](specs/functional_design.md) - 機能設計書（基本設計）
- [behaviors.md](specs/behaviors.md) - 振る舞い仕様書（Acceptance Criteria）
- [data_model.md](specs/data_model.md) - データモデル仕様書
- [screen_design.md](specs/screen_design.md) - 画面設計書
- [external_interface.md](specs/external_interface.md) - 外部インターフェース仕様書
- [tasks.md](specs/tasks.md) - タスク分解

**Feature 002以降: 個別機能追加**
- 個別機能は `specs/002-feature-name/` のようなフォルダで管理
- 例: `specs/002-inventory-alert/`（在庫アラート）, `specs/003-message-properties/`（メッセージ外部化）など

### Templates（テンプレート）

新規featureのSPECを作成する際のテンプレート：
- [requirements_template.md](templates/requirements_template.md) - 要件定義書テンプレート
- [architecture_design_template.md](templates/architecture_design_template.md) - アーキテクチャ設計書テンプレート
- [functional_design_template.md](templates/functional_design_template.md) - 機能設計書テンプレート
- [behaviors_template.md](templates/behaviors_template.md) - 振る舞い仕様書テンプレート
- [data_model_template.md](templates/data_model_template.md) - データモデル仕様書テンプレート
- [screen_design_template.md](templates/screen_design_template.md) - 画面設計書テンプレート
- [external_interface_template.md](templates/external_interface_template.md) - 外部インターフェース仕様書テンプレート
- [tasks_template.md](templates/tasks_template.md) - タスク分解テンプレート

### Prompts（プロンプト）

生成AIに指示を出すためのプロンプトファイル：
- [constitution.md](prompts/constitution.md) - プロジェクト憲章作成プロンプト
- [tasks.md](prompts/tasks.md) - タスク分解プロンプト
- [implement.md](prompts/implement.md) - 実装プロンプト

---

## 🎯 ワークフローのまとめ

### 基本的な流れ

**シナリオ1: ベースプロジェクトの実装（完成したSPECから）**

```
SPEC（既存） → タスク分解（AI） → 実装（AI）
```

1. **SPECの確認**: `specs/` フォルダの完成したSPECを確認
2. **Tasks（AI）**: `@prompts/tasks.md` を使って実装タスクに分解
3. **Implement（AI）**: `@prompts/implement.md` を使ってタスクに従って実装

**シナリオ2: 機能追加（新規featureの追加）**

```
SPEC作成（手動） → タスク分解（AI） → 実装（AI）
```

1. **SPECの作成（手動）**: `templates/` のテンプレートを参考に新機能のSPECを作成
2. **整合性確認**: 既存のSPECとの整合性を確認
3. **Tasks（AI）**: `@prompts/tasks.md` を使って実装タスクに分解
4. **Implement（AI）**: `@prompts/implement.md` を使ってタスクに従って実装

### 生成AIへの指示の基本パターン

Cursor等の生成AIツールで、`@` 記法を使ってファイルを参照します：

**タスク分解:**
```
@prompts/tasks.md このプロンプトに従って、
@specs/ のSPECから実装タスクリストを生成してください。
```

**実装:**
```
@prompts/implement.md このプロンプトに従って、
@specs/tasks.md のタスクリストに基づいて実装を進めてください。
```

### リバースエンジニアリング完了

本プロジェクトのSPECは、berry-books-fnプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**完成したSPEC:**
- ✅ requirements.md - 要件定義書（ビジネス要件、機能要件）
- ✅ architecture_design.md - アーキテクチャ設計書（技術スタック、アーキテクチャパターン、Mermaid図）
- ✅ functional_design.md - 機能設計書（クラス設計、ユーザーフロー）
- ✅ behaviors.md - 振る舞い仕様書（Acceptance Criteria、Gherkin形式）
- ✅ data_model.md - データモデル仕様書（ER図、テーブル定義）
- ✅ screen_design.md - 画面設計書（PlantUML形式、draw.io互換）
- ✅ external_interface.md - 外部インターフェース仕様書（テンプレート）
- ✅ tasks.md - 実装タスク分解（100以上のタスク、依存関係付き）

これらのSPECと生成AIプロンプトを使って、生成AIが同じアプリケーションを完全に再実装できます。

---

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

---

## 🔗 関連リンク

- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
- [PlantUML](https://plantuml.com/)
- [Mermaid](https://mermaid.js.org/)
