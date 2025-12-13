# [PROJECT_NAME] - 振る舞い仕様（Acceptance Criteria）

**Feature ID:** [FEATURE_ID]  
**Version:** 1.0.0  
**Last Updated:** [DATE]  
**Status:** [STATUS]

---

## 概要

このドキュメントは、[PROJECT_NAME]システムの外形的な振る舞いを定義します。
各シナリオはGiven-When-Then形式で記述され、ブラックボックステストの基礎となります。

**関連ドキュメント:**
- [requirements.md](requirements.md) - 要件定義
- [architecture.md](architecture.md) - アーキテクチャ設計
- [functional-design.md](functional-design.md) - 機能設計

---

## 機能 [FEATURE_ID]: [FEATURE_NAME]

### Scenario: [SCENARIO_NAME_1]

```gherkin
Given [前提条件]
When [アクション]
Then [期待される結果]
And [追加の期待結果]
```

### Scenario: [SCENARIO_NAME_2]

```gherkin
Given [前提条件]
When [アクション]
Then [期待される結果]
But [除外される結果]
```

### Scenario: [SCENARIO_NAME_3] (異常系)

```gherkin
Given [前提条件]
When [エラーを引き起こすアクション]
Then [エラーメッセージが表示される]
And [システムの状態は変更されない]
```

---

[必要に応じて追加の機能セクションを追加]

---

## テスト実行ガイド

### 前提条件

- [前提条件1]
- [前提条件2]
- [前提条件3]

### テストデータ

| データタイプ | ID/名前 | 説明 |
|------------|--------|------|
| [TYPE] | [ID] | [DESCRIPTION] |
| [TYPE] | [ID] | [DESCRIPTION] |

### 実行手順

1. [手順1]
2. [手順2]
3. [手順3]

---

## 改訂履歴

| バージョン | 日付 | 作成者 | 変更内容 |
|-----------|------|--------|---------|
| 1.0.0 | [DATE] | [AUTHOR] | 振る舞い仕様を新規作成 |

---

**ドキュメント終了**

*この振る舞い仕様は、システムの受入基準を定義しています。実装詳細は functional-design.md を参照してください。*

