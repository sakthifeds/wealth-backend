-- ============================================================
-- Stock Catalog Seed Data
-- Auto-loaded on Spring Boot startup
-- ============================================================

-- STOCKS (5 items)
INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'AAPL', 'Apple Inc.', 'STOCK', 192.50, 'US large-cap tech — iPhone, Mac, Services'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'AAPL');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'RELIANCE', 'Reliance Industries Ltd.', 'STOCK', 2850.00, 'India largest conglomerate — Oil, Telecom, Retail'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'RELIANCE');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'TCS', 'Tata Consultancy Services', 'STOCK', 3920.00, 'India leading IT services company'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'TCS');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'INFY', 'Infosys Ltd.', 'STOCK', 1480.00, 'Global IT and consulting services'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'INFY');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'HDFCBANK', 'HDFC Bank Ltd.', 'STOCK', 1620.00, 'India largest private sector bank'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'HDFCBANK');

-- MUTUAL FUNDS (5 items)
INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'VTSAX', 'Vanguard Total Stock Market Index', 'MUTUAL_FUND', 115.00, 'Diversified US equity index fund'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'VTSAX');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'MIRAE_LC', 'Mirae Asset Large Cap Fund', 'MUTUAL_FUND', 98.50, 'Large-cap focused Indian equity fund'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'MIRAE_LC');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'AXIS_BF', 'Axis Bluechip Fund', 'MUTUAL_FUND', 54.20, 'Top 30 Indian blue-chip companies'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'AXIS_BF');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'SBI_EQ', 'SBI Equity Fund', 'MUTUAL_FUND', 210.00, 'Diversified Indian equity mutual fund'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'SBI_EQ');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'HDFC_BAF', 'HDFC Balanced Advantage Fund', 'MUTUAL_FUND', 330.00, 'Dynamic asset allocation fund'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'HDFC_BAF');

-- BONDS (5 items)
INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'GOI2034', 'Govt of India Bond 2034', 'BOND', 1020.00, 'Sovereign bond maturing in 2034, ~7.1% yield'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'GOI2034');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'REC_BOND', 'REC Infrastructure Bond', 'BOND', 1005.00, 'AAA-rated infrastructure bond'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'REC_BOND');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'NHAI_BOND', 'NHAI Sovereign Bond', 'BOND', 998.00, 'National Highway Authority sovereign bond'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'NHAI_BOND');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'SGB2026', 'Sovereign Gold Bond 2026', 'BOND', 6150.00, 'Gold-backed government security'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'SGB2026');

INSERT INTO stock_catalog (symbol, name, asset_type, current_price, description)
SELECT 'TAXSAVE_FD', 'Tax Saver Fixed Deposit', 'BOND', 10000.00, '5-year tax-saving FD under 80C'
WHERE NOT EXISTS (SELECT 1 FROM stock_catalog WHERE symbol = 'TAXSAVE_FD');
