# berry-books-spec-driven プロジェクト

## 📖 概要

Jakarta EE 10とJSF (Jakarta Server Faces) 4.0を使用したオンライン書店「**berry-books**」のWebアプリケーションです。
書籍検索、ショッピングカート、注文処理などのEC機能を実装しています。

このプロジェクトは**仕様駆動開発（SDD: Specification-Driven Development）** アプローチを採用し、GitHub Spec Kitを使用して仕様駆動開発を実践します。

## 🚀 仕様駆動開発ワークフロー

このプロジェクトは以下のSpec Kitワークフローに従います：

```
/constitution → /specify → /plan → /tasks → /implement
```

### 実際の使い方

Spec Kitは**Cline AIとの対話型ワークフロー**です。以下の手順で進めます：

#### 1. Constitution（憲章）- 開発原則の確認

```bash
npm run spec:constitution
```

**何が起こるか:**
- VS Codeで`.workflows/commands/constitution.md`が開きます
- そのファイルをCline AIに読み込ませて、憲章の作成/確認を依頼します
- AIが`memory/constitution.md`を作成または更新します

**使い方:**
1. コマンド実行でプロンプトファイルを開く
2. Cline AIを起動
3. プロンプトファイルの内容を参考に、AIに指示を出す
4. 例: 「@.workflows/commands/constitution.md このプロンプトに従って憲章を作成してください」

#### 2. Specify（仕様化）- 機能仕様の作成

```bash
npm run spec:specify
```

**何が起こるか:**
- `.workflows/commands/specify.md`が開きます
- AIに機能説明を渡すと、`specs/XXX-feature-name/spec.md`を生成します

**使い方:**
```
「新しい機能：ユーザープロフィール編集画面
- ユーザーは自分のプロフィール情報を編集できる
- 名前、メール、アバター画像を変更可能」

という機能の仕様を作成してください
```

#### 3. Plan（計画）- 技術実装計画

```bash
npm run spec:plan
```

**何が起こるか:**
- spec.mdを読み込み、技術的な実装計画（plan.md）を生成

#### 4. Tasks（タスク化）- 実装タスクの分解

```bash
npm run spec:tasks
```

**何が起こるか:**
- plan.mdを読み込み、具体的なタスクリスト（tasks.md）を生成

#### 5. Implement（実装）- コード実装

```bash
npm run spec:implement
```

**何が起こるか:**
- tasks.mdを読み込み、順次実装を進める

### 💡 重要なポイント

- **自動実行ではない**: npmコマンドはプロンプトファイルを開くだけ
- **AI対話型**: Cline AI（Claude等）と対話しながら手動で進める
- **LLM指定**: Clineの設定で使用するLLM（Claude 3.5 Sonnet推奨）を設定
- **逐次実行**: 各フェーズを完了してから次に進む

## 🛠️ Spec Kit コマンド

### 環境チェック
```bash
npm run spec:check
```
Spec Kit環境が正しく設定されているか確認します。

### プロンプト一覧表示
```bash
npm run spec:prompts
```
利用可能な全プロンプト（コマンド）を一覧表示します。

## 🎯 実際の開発例

### 例：新機能「パスワードリセット」を追加する場合

```bash
# 1. 仕様化フェーズのプロンプトを開く
npm run spec:specify

# 2. Cline AIに以下のように指示
```
「@.workflows/commands/specify.md

新機能：パスワードリセット機能
- ユーザーはログイン画面から「パスワードを忘れた」をクリック
- メールアドレスを入力してリセットリンクを送信
- リンクから新しいパスワードを設定

この仕様を specs/002-password-reset/spec.md に作成してください」
```

# 3. 生成されたspec.mdを確認

# 4. 実装計画を作成
npm run spec:plan
「@specs/002-password-reset/spec.md この仕様から plan.md を作成してください」

# 5. タスク分解
npm run spec:tasks
「@specs/002-password-reset/plan.md このプランから tasks.md を作成してください」

# 6. 実装
npm run spec:implement
「@specs/002-password-reset/tasks.md このタスクリストに従って実装してください」
```

### LLMの設定方法

**Clineでの推奨設定:**

1. Clineの設定を開く
2. **API Provider** → `Anthropic`を選択
3. **Model** → `claude-3.5-sonnet-20241022`を選択
4. APIキーを設定

**推奨LLM:**
- 🥇 **Claude 3.5 Sonnet** - 最も優れたコード生成・仕様書理解
- 🥈 Claude 3.7 Sonnet (New) - 最新バージョン
- 🥉 GPT-4o - OpenAI使用の場合

## 📁 プロジェクト構成

```
berry-books-spec-driven/
├── .workflows/
│   └── commands/          # Cline用のSpec Kitコマンド
│       ├── constitution.md
│       ├── specify.md
│       ├── plan.md
│       ├── tasks.md
│       └── implement.md
├── memory/                # プロジェクトの記憶（憲章など）
│   └── constitution.md
├── templates/             # 仕様書テンプレート
├── docs/
│   └── specs/            # 生成された仕様書
├── spec/                 # 既存の仕様書（Excel等）
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

## 📖 ドキュメント

### Constitution（開発憲章）
- [Constitution（開発憲章）](memory/constitution.md) - プロジェクト全体の開発原則

### Specifications（仕様書）
**Feature 001: berry-books**
- [spec.md](specs/001-berry-books/spec.md) - 機能仕様（What & Why）
  - ユーザーストーリー、受容基準（Given-When-Then形式）
  - ビジネスルール、ユーザーフロー
- [plan.md](specs/001-berry-books/plan.md) - 技術実装計画（How）
  - 技術スタック、アーキテクチャ設計
  - デザインパターン、データフロー
- [data-model.md](specs/001-berry-books/data-model.md) - データモデル
  - ER図（Mermaid）、テーブル定義
  - リレーションシップ、制約
- [wireframes.md](specs/001-berry-books/wireframes.md) - ワイヤーフレーム
  - PlantUML形式（draw.ioインポート可能）
  - 全13画面のUI設計
- [tasks.md](specs/001-berry-books/tasks.md) - タスク分解
  - 実装タスクの段階的分解
  - 依存関係、並列実行可能タスク

## 🎯 仕様駆動開発の進め方

### 新機能開発の流れ

1. **憲章の確認**: `npm run spec:constitution`で開発原則を確認
2. **仕様化**: `npm run spec:specify`で機能仕様を作成
3. **計画**: `npm run spec:plan`で実装計画を立案
4. **タスク化**: `npm run spec:tasks`でタスクに分解
5. **実装**: `npm run spec:implement`で実装を進める

### Clineでの実行方法

```bash
# 1. ターミナルでコマンド実行
npm run spec:specify

# 2. Clineチャットで指示
「@.workflows/commands/specify.md このプロンプトに従って仕様を作成してください」

# 3. 生成された仕様書を確認・修正
# 4. 次のフェーズへ進む
```

### リバースエンジニアリング完了

本プロジェクトの仕様書は、berry-books-fnプロジェクトの既存実装からリバースエンジニアリングで作成されました。

**生成された仕様書:**
- ✅ spec.md - 機能要件（ソースコード不含、日本語）
- ✅ plan.md - 技術設計（Mermaid図使用、日本語）
- ✅ data-model.md - データモデル（ER図、日本語）
- ✅ wireframes.md - UI設計（PlantUML形式、draw.io互換、日本語）
- ✅ tasks.md - 実装タスク分解（100以上のタスク、日本語）

**特徴:**
- 📝 全ドキュメント日本語化完了（変数名・技術用語は英語のまま）
- 🎨 Mermaid/PlantUMLによる視覚化
- 🤖 Cline等のAIエージェントで生成可能な形式
- 📐 Spec Kit標準準拠
- 🔄 仕様駆動開発の完全なサイクルを実証

これらの仕様書を元に、Clineが同じアプリケーションを完全に再実装できます。

## 📝 ライセンス

このプロジェクトは教育目的で作成されています。

## 🔗 関連リンク

- [Spec Kit Documentation](https://github.com/github/spec-kit)
- [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/)
- [Payara Server Documentation](https://docs.payara.fish/)
