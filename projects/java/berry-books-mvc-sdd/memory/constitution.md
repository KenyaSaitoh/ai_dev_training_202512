# berry-books Spec-Driven Development Constitution

**Version:** 1.0.0  
**Ratification Date:** 2025-12-13  
**Last Amended:** 2025-12-13

## Project Overview

**Project Name:** berry-books Spec-Driven  
**Description:** Jakarta EE 10とJSF (Jakarta Server Faces) 4.0を使用したオンライン書店アプリケーション  
**Technology Stack:** Jakarta EE 10, JSF 4.0, JPA 3.1, Payara Server 6, HSQLDB

## Development Principles

### Principle 1: Specification-First Development
すべての機能開発は、詳細な仕様書の作成から始めます。実装前に仕様を明確化し、レビューを経て承認されたものだけを実装します。

**Rationale:** 仕様駆動開発により、実装の手戻りを最小化し、品質の高いソフトウェアを効率的に開発します。

### Principle 2: Architecture Consistency
Jakarta EE 10のベストプラクティスに従い、レイヤーアーキテクチャ（Presentation、Business Logic、Data Access、Entity）を厳格に守ります。

**Rationale:** 一貫したアーキテクチャにより、保守性と拡張性を確保します。

### Principle 3: Test-Driven Quality
すべてのビジネスロジックに対してユニットテストを作成し、カバレッジ80%以上を維持します。

**Rationale:** 自動テストにより、リグレッションを防ぎ、継続的な品質を保証します。

### Principle 4: Documentation Excellence
コードだけでなく、アーキテクチャガイドライン、詳細設計書、API仕様書を常に最新の状態に保ちます。

**Rationale:** 優れたドキュメントは、チーム間の知識共有とオンボーディングを加速します。

## Governance

### Amendment Procedure
1. 提案者が変更内容をドキュメント化
2. チームレビュー（最低2名の承認が必要）
3. 承認後、バージョンを更新して適用

### Versioning Policy
- **MAJOR**: 後方互換性のない変更
- **MINOR**: 新機能の追加
- **PATCH**: バグ修正、ドキュメント改善

### Compliance Review
四半期ごとに、プロジェクトが本憲章の原則に従っているかレビューを実施します。

