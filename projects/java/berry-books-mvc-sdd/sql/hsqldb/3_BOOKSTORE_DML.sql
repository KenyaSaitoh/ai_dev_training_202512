-- ============================================================================
-- Berry Books Database - DML Script
-- ============================================================================
-- Purpose: Insert sample data for all tables
-- Database: HSQLDB 2.7.x
-- Encoding: UTF-8
-- ============================================================================

-- ============================================================================
-- Master Data - PUBLISHER
-- ============================================================================
INSERT INTO PUBLISHER (PUBLISHER_ID, PUBLISHER_NAME) VALUES
(1, 'デジタルフロンティア出版'),
(2, 'コードブレイクプレス'),
(3, 'ネットワークノード出版'),
(4, 'クラウドキャスティング社'),
(5, 'データドリフト社');

-- ============================================================================
-- Master Data - CATEGORY
-- ============================================================================
INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME) VALUES
(1, 'Java'),
(2, 'SpringBoot'),
(3, 'SQL'),
(4, 'HTML/CSS'),
(5, 'JavaScript'),
(6, 'Python'),
(7, '生成AI'),
(8, 'クラウド'),
(9, 'AWS');

-- ============================================================================
-- Book Data (50 books)
-- ============================================================================
INSERT INTO BOOK (BOOK_ID, BOOK_NAME, AUTHOR, CATEGORY_ID, PUBLISHER_ID, PRICE) VALUES
-- Java Books (Category 1)
(1, 'Java SEディープダイブ', 'Michael Johnson', 1, 3, 3400),
(2, 'JVMとバイトコードの探求', 'James Lopez', 1, 1, 4200),
(3, 'Javaアーキテクトのための設計原理', 'Robert Martinez', 1, 2, 3800),
(4, 'Javaでのエレガントなコード設計', 'David Anderson', 1, 3, 3200),
(5, 'コンカレントプログラミング in Java SE', 'Christopher Taylor', 1, 1, 4500),

-- SpringBoot Books (Category 2)
(6, 'SpringBoot in Cloud', 'Paul Martin', 2, 3, 3000),
(7, 'SpringBootアーキテクチャの深層', 'Daniel Thomas', 2, 2, 3600),
(8, 'SpringBootによるエンタープライズ開発', 'Matthew Jackson', 2, 1, 4000),
(9, 'SpringBootでのAPI実践', 'Anthony White', 2, 3, 3400),
(10, 'SpringBoot魔法のレシピ', 'Mark Harris', 2, 2, 3200),

-- SQL Books (Category 3)
(11, 'SQLの冒険～RDBの深層', 'William Garcia', 3, 4, 3600),
(12, 'SQLデザインパターン～効率的なクエリ構築', 'Richard Rodriguez', 3, 5, 3800),
(13, 'データベースの科学', 'Joseph Wilson', 3, 4, 4200),
(14, 'SQLアナリティクス実践ガイド', 'Thomas Moore', 3, 5, 3400),
(15, '実践！SQLパフォーマンス最適化の秘訣', 'Charles Lee', 3, 4, 4000),

-- HTML/CSS Books (Category 4)
(16, 'HTMLとCSS実践ガイド', 'Jennifer Brown', 4, 2, 2800),
(17, 'HTML5エッセンス～Webの未来', 'Linda Davis', 4, 3, 3000),
(18, 'HTMLとCSSハンズオンプロジェクト', 'Barbara Miller', 4, 2, 2600),
(19, 'Webアクセシビリティ基礎', 'Susan Wilson', 4, 3, 3200),

-- JavaScript Books (Category 5)
(20, 'JavaScriptマジック', 'Jessica Martinez', 5, 1, 3400),
(21, 'ES6＋完全ガイド', 'Sarah Anderson', 5, 2, 3600),
(22, 'JavaScriptアルゴリズム実践集', 'Karen Taylor', 5, 1, 3800),
(23, 'JSアーキテクチャパターンの探求', 'Nancy Thomas', 5, 3, 4000),
(24, 'Node.jsによるサーバーサイド開発', 'Betty Jackson', 5, 2, 3400),
(25, 'Vue・React・Angular徹底比較入門', 'Helen White', 5, 1, 3200),
(26, 'フロントエンドのためのテスト入門', 'Dorothy Harris', 5, 3, 3000),

-- Python Books (Category 6)
(27, 'Pythonプログラミング実践入門', 'Emily Clark', 6, 5, 3200),
(28, 'Pythonで学ぶアルゴリズムとデータ構造', 'Michelle Lewis', 6, 4, 3600),
(29, 'Pythonデータ分析パターン', 'Amanda Robinson', 6, 5, 3800),
(30, 'Pythonで学ぶ並列処理と最適化', 'Melissa Walker', 6, 4, 4000),
(31, 'テスト自動化のためのPython', 'Stephanie Hall', 6, 5, 3400),
(32, '高速Web開発のためのPythonフレームワーク', 'Rebecca Allen', 6, 4, 3600),

-- 生成AI Books (Category 7)
(33, 'プロンプトエンジニアリング実践', 'Patricia Young', 7, 1, 3800),
(34, 'LLMアプリケーションアーキテクチャ', 'Sandra Hernandez', 7, 2, 4200),
(35, 'ベクトル検索とRAG入門', 'Donna King', 7, 1, 3600),
(36, '生成AIシステム設計ガイド', 'Carol Wright', 7, 2, 4000),
(37, 'マルチモーダルAI実践ハンドブック', 'Ruth Lopez', 7, 1, 4400),
(38, '生成AIの評価と監視', 'Sharon Hill', 7, 2, 3800),

-- クラウド Books (Category 8)
(39, 'クラウドアーキテクチャ実践パターン', 'Laura Scott', 8, 4, 3600),
(40, 'コンテナとオーケストレーション入門', 'Kimberly Green', 8, 3, 3400),
(41, 'サーバーレス実装ガイド', 'Deborah Adams', 8, 4, 3800),
(42, 'SREとクラウド運用ハンドブック', 'Lisa Baker', 8, 3, 4000),

-- AWS Books (Category 9)
(43, 'AWS設計原則とベストプラクティス', 'Mary Nelson', 9, 4, 3600),
(44, 'AWSネットワークとセキュリティ入門', 'Margaret Carter', 9, 3, 3400),
(45, 'AWSサーバーレスアーキテクチャ実践', 'Elizabeth Mitchell', 9, 4, 3800),
(46, 'IaCで進めるAWSインフラ構築', 'Jennifer Perez', 9, 3, 4000),
(47, 'AWS監視とコスト最適化ガイド', 'Maria Roberts', 9, 4, 3400),

-- Jakarta EE Books (Category 1 - Java)
(48, 'Jakarta EEによるアーキテクチャ設計', 'Patricia Turner', 1, 2, 4200),
(49, 'Jakarta EEパターンライブラリ', 'Linda Phillips', 1, 3, 3800),
(50, 'Jakarta EE究極テストガイド', 'Barbara Campbell', 1, 2, 4000);

-- ============================================================================
-- Stock Data (corresponding to all books)
-- ============================================================================
INSERT INTO STOCK (BOOK_ID, QUANTITY, VERSION) VALUES
-- Java Books
(1, 10, 0),
(2, 15, 0),
(3, 8, 0),
(4, 12, 0),
(5, 5, 0),

-- SpringBoot Books
(6, 20, 0),
(7, 10, 0),
(8, 7, 0),
(9, 15, 0),
(10, 18, 0),

-- SQL Books
(11, 12, 0),
(12, 9, 0),
(13, 6, 0),
(14, 14, 0),
(15, 11, 0),

-- HTML/CSS Books
(16, 25, 0),
(17, 20, 0),
(18, 30, 0),
(19, 15, 0),

-- JavaScript Books
(20, 18, 0),
(21, 16, 0),
(22, 12, 0),
(23, 10, 0),
(24, 14, 0),
(25, 22, 0),
(26, 19, 0),

-- Python Books
(27, 20, 0),
(28, 15, 0),
(29, 12, 0),
(30, 10, 0),
(31, 17, 0),
(32, 13, 0),

-- 生成AI Books
(33, 25, 0),
(34, 8, 0),
(35, 20, 0),
(36, 15, 0),
(37, 5, 0),
(38, 18, 0),

-- クラウド Books
(39, 14, 0),
(40, 16, 0),
(41, 12, 0),
(42, 10, 0),

-- AWS Books
(43, 18, 0),
(44, 20, 0),
(45, 15, 0),
(46, 12, 0),
(47, 22, 0),

-- Jakarta EE Books
(48, 8, 0),
(49, 10, 0),
(50, 0, 0);  -- Out of stock for testing

-- ============================================================================
-- Test Customer Data
-- ============================================================================
INSERT INTO CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME, EMAIL, PASSWORD, BIRTHDAY, ADDRESS) VALUES
(1, 'Alice', 'alice@gmail.com', 'password', '1990-01-01', '東京都渋谷区渋谷1-1-1'),
(2, 'Bob', 'bob@gmail.com', 'password', '1985-05-15', '大阪府大阪市北区梅田2-2-2'),
(3, 'Charlie', 'charlie@gmail.com', 'password', '1992-12-25', '沖縄県那覇市おもろまち3-3-3'),
(4, 'Diana', 'diana@gmail.com', 'password', '1988-07-20', '北海道札幌市中央区北1条西4-4-4'),
(5, 'Eve', 'eve@gmail.com', 'password', '1995-03-10', '福岡県福岡市博多区博多駅前5-5-5');

-- ============================================================================
-- Test Order Data
-- ============================================================================
-- Order 1: Alice's order
INSERT INTO ORDER_TRAN (ORDER_TRAN_ID, ORDER_DATE, CUSTOMER_ID, TOTAL_PRICE, DELIVERY_PRICE, DELIVERY_ADDRESS, SETTLEMENT_TYPE) VALUES
(1, '2025-12-01', 1, 7000, 800, '東京都渋谷区渋谷1-1-1', 1);

INSERT INTO ORDER_DETAIL (ORDER_TRAN_ID, ORDER_DETAIL_ID, BOOK_ID, PRICE, COUNT) VALUES
(1, 1, 1, 3400, 1),
(1, 2, 6, 3000, 1),
(1, 3, 16, 2800, 1);

-- Order 2: Bob's order
INSERT INTO ORDER_TRAN (ORDER_TRAN_ID, ORDER_DATE, CUSTOMER_ID, TOTAL_PRICE, DELIVERY_PRICE, DELIVERY_ADDRESS, SETTLEMENT_TYPE) VALUES
(2, '2025-12-05', 2, 10600, 800, '大阪府大阪市北区梅田2-2-2', 2);

INSERT INTO ORDER_DETAIL (ORDER_TRAN_ID, ORDER_DETAIL_ID, BOOK_ID, PRICE, COUNT) VALUES
(2, 1, 11, 3600, 1),
(2, 2, 20, 3400, 2);

-- Order 3: Charlie's order (Okinawa - higher delivery fee)
INSERT INTO ORDER_TRAN (ORDER_TRAN_ID, ORDER_DATE, CUSTOMER_ID, TOTAL_PRICE, DELIVERY_PRICE, DELIVERY_ADDRESS, SETTLEMENT_TYPE) VALUES
(3, '2025-12-10', 3, 7200, 1700, '沖縄県那覇市おもろまち3-3-3', 3);

INSERT INTO ORDER_DETAIL (ORDER_TRAN_ID, ORDER_DETAIL_ID, BOOK_ID, PRICE, COUNT) VALUES
(3, 1, 33, 3800, 1),
(3, 2, 27, 3200, 1);

-- ============================================================================
-- End of DML Script
-- ============================================================================
