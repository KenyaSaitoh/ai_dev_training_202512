-- ============================================================================
-- Berry Books Database - DROP Script
-- ============================================================================
-- Purpose: Drop all tables in reverse dependency order
-- Database: HSQLDB 2.7.x
-- Encoding: UTF-8
-- ============================================================================

-- Drop tables in reverse dependency order (child tables first)

-- Drop ORDER_DETAIL (depends on ORDER_TRAN and BOOK)
DROP TABLE IF EXISTS ORDER_DETAIL;

-- Drop ORDER_TRAN (depends on CUSTOMER)
DROP TABLE IF EXISTS ORDER_TRAN;

-- Drop STOCK (depends on BOOK)
DROP TABLE IF EXISTS STOCK;

-- Drop BOOK (depends on CATEGORY and PUBLISHER)
DROP TABLE IF EXISTS BOOK;

-- Drop CUSTOMER (no dependencies)
DROP TABLE IF EXISTS CUSTOMER;

-- Drop CATEGORY (no dependencies)
DROP TABLE IF EXISTS CATEGORY;

-- Drop PUBLISHER (no dependencies)
DROP TABLE IF EXISTS PUBLISHER;

-- ============================================================================
-- End of DROP Script
-- ============================================================================
