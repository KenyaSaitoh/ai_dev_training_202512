# berry-books-spec-driven プロジェクト

## 📖 概要

Jakarta EE 10によるオンライン書店「**berry-books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、仕様駆動開発を実践します。

---

## 🎯 2つの開発シナリオ

このプロジェクトでは、以下の2つの開発シナリオをサポートしています：

### シナリオ1: フルスクラッチ開発（新規プロジェクト）
ゼロからアプリケーション全体を構築する場合。憲章作成から要件定義、設計、実装まで全フェーズを実行します。

**対象:**
- 新規プロジェクトの立ち上げ
- 既存システムの全面リプレイス
- プロトタイプの開発

**仕様書の配置:** `specs/` 直下に全ドキュメントを配置

### シナリオ2: 既存プロジェクトへの機能追加
既に稼働しているプロジェクトに新機能を追加する場合。既存のアーキテクチャを前提に、新機能の要件定義から実装まで実行します。

**対象:**
- 新機能の追加（例: パスワードリセット機能）
- 既存機能の改善・拡張
- 新しいユーザーストーリーの実装

**仕様書の配置:** `specs/002-feature-name/` のような個別フォルダに配置

---

## 📦 シナリオ1: フルスクラッチ開発（新規プロジェクト）

**このプロジェクト（berry-books）は、シナリオ1のサンプルとして完成したSpec（仕様書）が `specs/` に用意されています。**

### 既存のSpecファイル

```
specs/
├── requirements.md      # 要件定義書（What & Why）
├── architecture.md      # アーキテクチャ設計書
├── functional-design.md # 機能設計書（基本設計、クラス設計含む）
├── behaviors.md         # 振る舞い仕様（Acceptance Criteria）
├── data-model.md        # データモデル（ER図、テーブル定義）
├── wireframes.md        # ワイヤーフレーム（PlantUML形式）
└── tasks.md             # 実装タスク分解（100以上のタスク）
```

### 実装を開始する手順

既存のSpecから実装を開始する場合は、**Implementフェーズから直接開始**できます。

**注意:** tasks.mdの「フェーズ1: インフラストラクチャセットアップ」は、仕様駆動開発の対象外です。このフェーズは事前にセットアップ済みであることを前提としています。

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/tasks.md のタスクリストに従って
実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

**重要:** このプロジェクトは `projects/java/berry-books-spec-driven/` ディレクトリにあります。
AIエージェントに指示を出す際は、リポジトリルートからの絶対パス（`/projects/java/berry-books-spec-driven/...`）を使用してください。

**実装の進め方:**
1. **フェーズ2（永続化レイヤー）から開始** - データベーススキーマとJPAエンティティの実装
2. tasks.mdのタスクを上から順に実行
3. [P]マークのタスクは並行実行可能
4. 各タスク完了後、tasks.mdのチェックボックスを更新
5. フェーズごとに動作確認を実施

**実装フェーズの概要:**
- **フェーズ2**: 永続化レイヤー（データベーススキーマ、JPAエンティティ）
- **フェーズ3**: データアクセスレイヤー（DAO実装）
- **フェーズ4**: ビジネスロジックレイヤー（サービス実装）
- **フェーズ5**: プレゼンテーションレイヤー（Managed Bean実装）
- **フェーズ6**: ビューレイヤー（XHTML、CSS実装）
- **フェーズ7**: テスト（ユニットテスト、統合テスト）
- **フェーズ8**: デプロイとドキュメント

**参考資料:**
- `/projects/java/berry-books-spec-driven/specs/requirements.md` - 要件定義（機能要件とビジネスルール）
- `/projects/java/berry-books-spec-driven/specs/architecture.md` - アーキテクチャ設計（技術スタックとアーキテクチャ）
- `/projects/java/berry-books-spec-driven/specs/functional-design.md` - 機能設計（クラス設計と詳細仕様）
- `/projects/java/berry-books-spec-driven/specs/behaviors.md` - 振る舞い仕様（テスト仕様）
- `/projects/java/berry-books-spec-driven/specs/data-model.md` - データベーススキーマ
- `/projects/java/berry-books-spec-driven/specs/wireframes.md` - UI設計

---

## 🚀 シナリオ2: 既存プロジェクトへの機能追加

既存のberry-booksプロジェクトに新機能を追加する場合のワークフローです。

### ワークフロー

```
/constitution → /requirements → /architecture → /design → /tasks → /implement
```

このワークフローは、日本のウォーターフォール開発手法に沿っています：
- **要件定義** → **アーキテクチャ設計** → **基本設計** → **実装**

### 重要な違い

| 項目 | フルスクラッチ | 機能追加 |
|------|------------|---------|
| 憲章 | 新規作成 | 既存を参照 |
| アーキテクチャ | 全体設計 | 既存に準拠 |
| 仕様書の配置 | `specs/` | `specs/002-feature-name/` |
| 実装範囲 | 全レイヤー | 必要な部分のみ |

### 個別featureの仕様書配置

新機能ごとに専用のフォルダを作成します：

```
specs/
├── requirements.md           # 001: ベースプロジェクト（berry-books本体）
├── architecture.md
├── functional-design.md
├── ...
└── 002-inventory-alert/      # 002: 在庫管理機能の改善
    ├── requirements.md
    ├── architecture.md
    ├── functional-design.md
    ├── behaviors.md
    ├── tasks.md
    └── ...
└── 003-message-properties/  # 003: メッセージのプロパティ対応
    ├── requirements.md
    └── ...
```

### ワークフローの概要

このプロジェクトは**AIエージェントとの対話型ワークフロー**です。各フェーズで、AIエージェントに適切なプロンプトを読み込ませて指示を出します。

---

## 📋 各フェーズの実行方法（両シナリオ共通）

### フェーズ 1: Constitution（憲章）- 開発原則の確認

**目的:** プロジェクトの開発原則と憲章を作成・確認します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/constitution.md このプロンプトに従って、
プロジェクトの憲章を /projects/java/berry-books-spec-driven/memory/constitution.md に作成してください。
```

**生成されるファイル:**
- `memory/constitution.md` - プロジェクトの開発原則と憲章

---

### フェーズ 2: Requirements（要件定義）- 要件定義書の作成

**目的:** 新機能の要件定義書を作成します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/requirements.md このプロンプトに従って、
以下の機能の要件定義を作成してください：

【機能説明】
新機能：ユーザープロフィール編集画面
- ユーザーは自分のプロフィール情報を編集できる
- 名前、メール、アバター画像を変更可能
- 変更履歴を保存
```

**生成されるファイル:**
- **フルスクラッチ**: `specs/requirements.md`
- **機能追加**: `specs/002-inventory-alert/requirements.md`（例）

**既存の要件定義例:**
- `specs/requirements.md` - berry-books（001）の完全な要件定義

---

### フェーズ 3: Architecture（アーキテクチャ設計）- 技術設計書の作成

**目的:** 要件定義から技術アーキテクチャを設計します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/architecture.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/requirements.md の要件から
アーキテクチャ設計書を作成してください。
```

**生成されるファイル:**
- **フルスクラッチ**: `specs/architecture.md`
- **機能追加**: `specs/002-inventory-alert/architecture.md`（差分のみ、例）

**既存のアーキテクチャ設計例:**
- `specs/architecture.md` - berry-books（001）の完全なアーキテクチャ設計

---

### フェーズ 4: Design（基本設計）- 機能設計書の作成

**目的:** アーキテクチャに基づいて機能詳細とクラス設計を行います。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/design.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/requirements.md と
/projects/java/berry-books-spec-driven/specs/architecture.md を参照して
機能設計書を作成してください。
```

**生成されるファイル:**
- **フルスクラッチ**: 
  - `specs/functional-design.md`
  - `specs/data-model.md`
  - `specs/wireframes.md`
  - `specs/behaviors.md`
- **機能追加**:
  - `specs/002-inventory-alert/functional-design.md`（例）
  - `specs/002-inventory-alert/data-model.md`（必要に応じて）
  - `specs/002-inventory-alert/wireframes.md`（必要に応じて）
  - `specs/002-inventory-alert/behaviors.md`

**既存の機能設計例:**
- `specs/functional-design.md` - berry-books（001）の完全な機能設計
- `specs/data-model.md` - ER図、テーブル定義
- `specs/wireframes.md` - UI設計（PlantUML形式）
- `specs/behaviors.md` - 振る舞い仕様（Acceptance Criteria）

---

### フェーズ 5: Tasks（タスク化）- 実装タスクの分解

**目的:** 設計ドキュメントから具体的な実装タスクに分解します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/tasks.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/ の設計ドキュメントから
タスクリストを作成してください。
```

**生成されるファイル:**
- **フルスクラッチ**: `specs/tasks.md`
- **機能追加**: `specs/002-inventory-alert/tasks.md`（例）

**既存のタスク例:**
- `specs/tasks.md` - berry-books（001）の100以上の実装タスク、依存関係付き

---

### フェーズ 6: Implement（実装）- コード実装

**目的:** タスクリストに従って実装を進めます。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/tasks.md のタスクリストに従って
実装を進めてください。

フェーズ2の永続化レイヤーから開始してください。
（フェーズ1のインフラストラクチャセットアップは事前完了済み）
```

**実装対象:**
- ソースコード（Java、XHTML、CSS等）
- テストコード
- 設定ファイル

---

## 💡 重要なポイント

### ワークフローの特徴

1. **AI対話型ワークフロー**: AIエージェント（Cline、Cursor、Windsurf等）と対話しながら手動で進める
2. **プロンプトベース**: 各フェーズに対応するプロンプトファイルを使用
3. **段階的実行**: 各フェーズを完了してから次に進む

### AIエージェントへの指示の基本パターン

```
/projects/java/berry-books-spec-driven/prompts/<フェーズ名>.md このプロンプトに従って、
<具体的な指示内容>
```

**パス指定のルール:**
- リポジトリルートからの絶対パスを使用（`/projects/java/berry-books-spec-driven/...`）
- プロジェクト内の相対パスは避ける（AIエージェントが混乱する可能性があるため）

---

## 🎯 実際の開発例（シナリオ2: 機能追加）

### 例1: 在庫管理機能の改善

**前提条件:**
- berry-booksプロジェクトが既に稼働している
- 既存の在庫管理機能（Stock エンティティ、楽観的ロック）がある
- 新機能は `specs/002-inventory-alert/` に配置

#### ステップ 0: ディレクトリ作成

```bash
mkdir -p specs/002-inventory-alert
```

#### ステップ 1: 要件定義

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/requirements.md このプロンプトに従って、
以下の機能の要件定義を作成してください：

新機能：在庫アラート機能
- 在庫数が閾値（例：5冊）を下回ったら管理者に通知
- 管理者画面で在庫アラート一覧を表示
- 書籍ごとに閾値を設定可能
- 在庫補充後、アラートを解除

要件定義書は /projects/java/berry-books-spec-driven/specs/002-inventory-alert/requirements.md に作成してください。
```

#### ステップ 2: アーキテクチャ設計

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/architecture.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-inventory-alert/requirements.md の要件から
アーキテクチャ設計書を作成してください。

注意: 既存のberry-booksアーキテクチャ（/projects/java/berry-books-spec-driven/specs/architecture.md）
に準拠し、追加・変更点のみを記述してください。
既存のStock、StockDao、BookServiceとの連携を考慮してください。
```

#### ステップ 3: 機能設計

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/design.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-inventory-alert/requirements.md と
/projects/java/berry-books-spec-driven/specs/002-inventory-alert/architecture.md を参照して
機能設計書を作成してください。

注意: 
- 既存のStockエンティティに閾値フィールドを追加
- 新規InventoryAlertエンティティとInventoryAlertDaoを設計
- 既存のOrderServiceでの在庫減少時にアラートチェックを追加
```

#### ステップ 4: タスク分解

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/tasks.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-inventory-alert/ の設計ドキュメントから
タスクリストを作成してください。

注意: 既存ファイルの修正タスクと新規ファイル作成タスクを明確に区別してください。
```

#### ステップ 5: 実装

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-inventory-alert/tasks.md のタスクリストに従って
実装を進めてください。

注意: 既存コードの修正は慎重に行い、既存機能に影響を与えないようにしてください。
```

---

### 例2: メッセージのプロパティ対応

**前提条件:**
- berry-booksプロジェクトが既に稼働している
- 現在はJavaコード内にハードコードされたエラーメッセージがある
- 新機能は `specs/003-message-properties/` に配置

#### ステップ 0: ディレクトリ作成

```bash
mkdir -p specs/003-message-properties
```

#### ステップ 1: 要件定義

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/prompts/requirements.md このプロンプトに従って、
以下の機能の要件定義を作成してください：

新機能：メッセージのプロパティ化
- Javaコード内のハードコードされたメッセージを messages.properties に外部化
- エラーメッセージ、警告メッセージ、情報メッセージを対象
- MessageUtilクラスを使用したメッセージの取得
- 既存のコードを修正してプロパティファイルからメッセージを取得するように変更
- メッセージIDの命名規則を定義（例: error.validation.required）

要件定義書は /projects/java/berry-books-spec-driven/specs/003-message-properties/requirements.md に作成してください。
```

#### ステップ 2以降

同様のワークフローで、アーキテクチャ設計、機能設計、タスク分解、実装を進めます。

**注意点:**
- 既存のmessages.propertiesファイルがある場合は、それを拡張する
- MessageUtilクラスが既に存在する場合は、その使い方に従う
- 全てのハードコードメッセージを洗い出し、段階的に外部化する

---

## 📁 プロジェクト構成

```
berry-books-spec-driven/
├── prompts/               # 仕様作成プロンプトファイル
│   ├── constitution.md   # 憲章作成プロンプト
│   ├── requirements.md   # 要件定義プロンプト
│   ├── architecture.md   # アーキテクチャ設計プロンプト
│   ├── design.md         # 機能設計プロンプト
│   ├── tasks.md          # タスク化プロンプト
│   └── implement.md      # 実装プロンプト
├── memory/               # プロジェクトの記憶
│   └── constitution.md   # 開発憲章
├── specs/                # 生成された仕様書
│   ├── requirements.md      # 001: ベースプロジェクト要件定義書
│   ├── architecture.md      # 001: アーキテクチャ設計書
│   ├── functional-design.md # 001: 機能設計書
│   ├── behaviors.md         # 001: 振る舞い仕様
│   ├── data-model.md        # 001: データモデル
│   ├── wireframes.md        # 001: ワイヤーフレーム
│   ├── tasks.md             # 001: 実装タスク分解
│   └── 002-inventory-alert/ # 002: 個別機能（例: 在庫アラート機能）
│       ├── requirements.md
│       ├── architecture.md
│       ├── functional-design.md
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

### Specifications（仕様書）

**Feature 001: berry-books（ベースプロジェクト）**
- [requirements.md](specs/requirements.md) - 要件定義書
- [architecture.md](specs/architecture.md) - アーキテクチャ設計書
- [functional-design.md](specs/functional-design.md) - 機能設計書（基本設計）
- [behaviors.md](specs/behaviors.md) - 振る舞い仕様（Acceptance Criteria）
- [data-model.md](specs/data-model.md) - データモデル
- [wireframes.md](specs/wireframes.md) - ワイヤーフレーム
- [tasks.md](specs/tasks.md) - タスク分解

**Feature 002以降: 個別機能追加**
- 個別機能は `specs/002-feature-name/` のようなフォルダで管理
- 例: `specs/002-inventory-alert/`（在庫アラート）, `specs/003-message-properties/`（メッセージ外部化）など

---

## 🎯 ワークフローのまとめ

### 基本的な流れ

**シナリオ1: フルスクラッチ開発**
1. **Constitution**: プロジェクトの開発原則を確立
2. **Requirements**: 要件定義書を作成（What & Why）
3. **Architecture**: アーキテクチャ設計を作成（技術スタック、全体設計）
4. **Design**: 機能設計書を作成（基本設計、クラス設計）
5. **Tasks**: 実装タスクに分解
6. **Implement**: タスクに従って実装（全レイヤー）

**シナリオ2: 機能追加**
1. **Constitution**: 既存の憲章を参照（新規作成不要）
2. **Requirements**: 新機能の要件定義書を作成（`specs/002-inventory-alert/` など）
3. **Architecture**: 既存アーキテクチャに準拠し、差分のみ記述
4. **Design**: 新機能の機能設計書を作成（既存クラスの拡張含む）
5. **Tasks**: 実装タスクに分解（既存ファイル修正/新規作成を区別）
6. **Implement**: タスクに従って実装（必要な部分のみ）

このワークフローは、日本のウォーターフォール開発手法に沿っています。

### AIエージェントとの対話方法

各フェーズで、以下のパターンでAIエージェントに指示を出します：

```
/projects/java/berry-books-spec-driven/prompts/<フェーズ名>.md このプロンプトに従って、
<具体的な指示内容>
```

### リバースエンジニアリング完了

本プロジェクトの仕様書は、berry-books-fnプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**生成された仕様書:**
- ✅ requirements.md - 要件定義（ビジネス要件、機能要件、日本語）
- ✅ architecture.md - アーキテクチャ設計（技術スタック、アーキテクチャパターン、Mermaid図使用、日本語）
- ✅ functional-design.md - 機能設計（クラス設計、ユーザーフロー、日本語）
- ✅ behaviors.md - 振る舞い仕様（Acceptance Criteria、Gherkin形式、日本語）
- ✅ data-model.md - データモデル（ER図、日本語）
- ✅ wireframes.md - UI設計（PlantUML形式、draw.io互換、日本語）
- ✅ tasks.md - 実装タスク分解（100以上のタスク、日本語）

これらの仕様書を元に、AIエージェントが同じアプリケーションを完全に再実装できます。

---

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

---

## 🔗 関連リンク

- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
- [PlantUML](https://plantuml.com/)
- [Mermaid](https://mermaid.js.org/)
