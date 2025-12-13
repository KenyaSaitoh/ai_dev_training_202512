# berry-books-spec-driven プロジェクト

## 📖 概要

Jakarta EE 10によるオンライン書店「**berry-books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、GitHub Spec Kitを使用して仕様駆動開発を実践します。

---

## 🎯 すでにSpecが完成している場合の手順

**このプロジェクトには、すでに完成したSpec（仕様書）が `specs/001-berry-books/` に用意されています。**

### 既存のSpecファイル

```
specs/001-berry-books/
├── spec.md          # 機能仕様（What & Why）
├── plan.md          # 技術実装計画（How）
├── data-model.md    # データモデル（ER図、テーブル定義）
├── wireframes.md    # ワイヤーフレーム（PlantUML形式）
└── tasks.md         # 実装タスク分解（100以上のタスク）
```

### 実装を開始する手順

既存のSpecから実装を開始する場合は、**Implementフェーズから直接開始**できます。

**注意:** tasks.mdの「フェーズ1: インフラストラクチャセットアップ」は、仕様駆動開発の対象外です。このフェーズは事前にセットアップ済みであることを前提としています。

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/001-berry-books/tasks.md のタスクリストに従って
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
- `/projects/java/berry-books-spec-driven/specs/001-berry-books/spec.md` - 機能要件とビジネスルール
- `/projects/java/berry-books-spec-driven/specs/001-berry-books/plan.md` - アーキテクチャと技術設計
- `/projects/java/berry-books-spec-driven/specs/001-berry-books/data-model.md` - データベーススキーマ
- `/projects/java/berry-books-spec-driven/specs/001-berry-books/wireframes.md` - UI設計

---

## 🚀 SpecKit ワークフロー（新機能追加時）

このプロジェクトは以下のSpec Kitワークフローに従います：

```
/constitution → /specify → /plan → /tasks → /implement
```

### ワークフローの概要

SpecKitは**AIエージェントとの対話型ワークフロー**です。各フェーズで、AIエージェントに適切なプロンプトを読み込ませて指示を出します。

---

## 📋 各フェーズの実行方法

### フェーズ 1: Constitution（憲章）- 開発原則の確認

**目的:** プロジェクトの開発原則と憲章を作成・確認します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/constitution.md このプロンプトに従って、
プロジェクトの憲章を /projects/java/berry-books-spec-driven/memory/constitution.md に作成してください。
```

**生成されるファイル:**
- `memory/constitution.md` - プロジェクトの開発原則と憲章

---

### フェーズ 2: Specify（仕様化）- 機能仕様の作成

**目的:** 新機能の仕様書を作成します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/specify.md このプロンプトに従って、
以下の機能の仕様を作成してください：

【機能説明】
新機能：ユーザープロフィール編集画面
- ユーザーは自分のプロフィール情報を編集できる
- 名前、メール、アバター画像を変更可能
- 変更履歴を保存
```

**生成されるファイル:**
- `specs/XXX-feature-name/spec.md` - 機能仕様書（What & Why）

**既存の仕様例:**
- `specs/001-berry-books/spec.md` - berry-books機能の完全な仕様

---

### フェーズ 3: Plan（計画）- 技術実装計画

**目的:** 仕様書から技術的な実装計画を作成します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/plan.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/001-berry-books/spec.md の仕様から
技術実装計画を作成してください。
```

**生成されるファイル:**
- `specs/XXX-feature-name/plan.md` - 技術実装計画（How）
- `specs/XXX-feature-name/data-model.md` - データモデル（必要に応じて）
- `specs/XXX-feature-name/wireframes.md` - ワイヤーフレーム（必要に応じて）

**既存の計画例:**
- `specs/001-berry-books/plan.md` - 技術スタック、アーキテクチャ設計
- `specs/001-berry-books/data-model.md` - ER図、テーブル定義
- `specs/001-berry-books/wireframes.md` - UI設計（PlantUML形式）

---

### フェーズ 4: Tasks（タスク化）- 実装タスクの分解

**目的:** 実装計画を具体的なタスクに分解します。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/tasks.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/001-berry-books/plan.md の実装計画から
タスクリストを作成してください。
```

**生成されるファイル:**
- `specs/XXX-feature-name/tasks.md` - 実装タスクの段階的分解

**既存のタスク例:**
- `specs/001-berry-books/tasks.md` - 100以上の実装タスク、依存関係付き

---

### フェーズ 5: Implement（実装）- コード実装

**目的:** タスクリストに従って実装を進めます。

**AIエージェントへの指示例:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/001-berry-books/tasks.md のタスクリストに従って
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

### SpecKitの特徴

1. **AI対話型ワークフロー**: AIエージェント（Cline、Cursor、Windsurf等）と対話しながら手動で進める
2. **プロンプトベース**: 各フェーズに対応するプロンプトファイルを使用
3. **段階的実行**: 各フェーズを完了してから次に進む
4. **ツール非依存**: npmコマンドは補助的、主な作業はAIエージェントとの対話

### AIエージェントへの指示の基本パターン

```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/<フェーズ名>.md このプロンプトに従って、
<具体的な指示内容>
```

**パス指定のルール:**
- リポジトリルートからの絶対パスを使用（`/projects/java/berry-books-spec-driven/...`）
- プロジェクト内の相対パスは避ける（AIエージェントが混乱する可能性があるため）

---

## 🛠️ Spec Kit コマンド（補助ツール）

以下のnpmコマンドは、プロンプトファイルを開くための補助ツールです：

```bash
# 環境チェック
npm run spec:check

# プロンプト一覧表示
npm run spec:prompts

# 各フェーズのプロンプトを開く
npm run spec:constitution
npm run spec:specify
npm run spec:plan
npm run spec:tasks
npm run spec:implement
```

**注意:** これらのコマンドはプロンプトファイルを開くだけです。実際の作業はAIエージェントに指示を出して行います。

---

## 🎯 実際の開発例

### 例：新機能「パスワードリセット」を追加する場合

#### ステップ 1: 仕様化

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/specify.md このプロンプトに従って、
以下の機能の仕様を作成してください：

新機能：パスワードリセット機能
- ユーザーはログイン画面から「パスワードを忘れた」をクリック
- メールアドレスを入力してリセットリンクを送信
- リンクから新しいパスワードを設定

仕様書は /projects/java/berry-books-spec-driven/specs/002-password-reset/spec.md に作成してください。
```

#### ステップ 2: 実装計画

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/plan.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-password-reset/spec.md の仕様から
技術実装計画を作成してください。
```

#### ステップ 3: タスク分解

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/tasks.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-password-reset/plan.md の実装計画から
タスクリストを作成してください。
```

#### ステップ 4: 実装

**AIエージェントへの指示:**
```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/implement.md このプロンプトに従って、
/projects/java/berry-books-spec-driven/specs/002-password-reset/tasks.md のタスクリストに従って
実装を進めてください。
```

---

## 📁 プロジェクト構成

```
berry-books-spec-driven/
├── .spec-kit/
│   └── prompts/           # Spec Kitプロンプトファイル
│       ├── constitution.md  # 憲章作成プロンプト
│       ├── specify.md       # 仕様化プロンプト
│       ├── plan.md          # 計画プロンプト
│       ├── tasks.md         # タスク化プロンプト
│       └── implement.md     # 実装プロンプト
├── memory/                # プロジェクトの記憶
│   └── constitution.md    # 開発憲章
├── specs/                 # 生成された仕様書
│   └── 001-berry-books/
│       ├── spec.md        # 機能仕様（What & Why）
│       ├── plan.md        # 技術実装計画（How）
│       ├── data-model.md  # データモデル
│       ├── wireframes.md  # ワイヤーフレーム
│       └── tasks.md       # 実装タスク分解
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── webapp/
│   └── test/
├── sql/
│   └── hsqldb/
├── package.json          # Spec Kit設定
└── README.md
```

---

## 🔧 セットアップ

### 前提条件

- JDK 21以上
- Gradle 8.x以上
- Node.js 18以上
- npm 9以上
- Payara Server 6
- HSQLDB

### インストール

```bash
# 依存パッケージのインストール
npm install

# Spec Kit環境チェック
npm run spec:check
```

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
| **仕様管理** | Spec Kit | 0.1.3 |

---

## 📖 ドキュメント

### Constitution（開発憲章）
- [Constitution（開発憲章）](memory/constitution.md) - プロジェクト全体の開発原則

### Specifications（仕様書）
**Feature 001: berry-books**
- [spec.md](specs/001-berry-books/spec.md) - 機能仕様（What & Why）
- [plan.md](specs/001-berry-books/plan.md) - 技術実装計画（How）
- [data-model.md](specs/001-berry-books/data-model.md) - データモデル
- [wireframes.md](specs/001-berry-books/wireframes.md) - ワイヤーフレーム
- [tasks.md](specs/001-berry-books/tasks.md) - タスク分解

---

## 🎯 SpecKitワークフローのまとめ

### 基本的な流れ

1. **Constitution**: プロジェクトの開発原則を確立
2. **Specify**: 機能の仕様書を作成（What & Why）
3. **Plan**: 技術実装計画を作成（How）
4. **Tasks**: 実装タスクに分解
5. **Implement**: タスクに従って実装

### AIエージェントとの対話方法

各フェーズで、以下のパターンでAIエージェントに指示を出します：

```
/projects/java/berry-books-spec-driven/.spec-kit/prompts/<フェーズ名>.md このプロンプトに従って、
<具体的な指示内容>
```

### リバースエンジニアリング完了

本プロジェクトの仕様書は、berry-books-fnプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**生成された仕様書:**
- ✅ spec.md - 機能要件（ソースコード不含、日本語）
- ✅ plan.md - 技術設計（Mermaid図使用、日本語）
- ✅ data-model.md - データモデル（ER図、日本語）
- ✅ wireframes.md - UI設計（PlantUML形式、draw.io互換、日本語）
- ✅ tasks.md - 実装タスク分解（100以上のタスク、日本語）

これらの仕様書を元に、AIエージェントが同じアプリケーションを完全に再実装できます。

---

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

---

## 🔗 関連リンク

- [Spec Kit Documentation](https://github.com/github/spec-kit)
- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
