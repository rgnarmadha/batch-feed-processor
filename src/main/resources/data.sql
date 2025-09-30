INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('customer_feed', 'customer_*.csv', 'customers', 'customer_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('product_feed', 'product_*.csv', 'products', 'product_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('order_feed', 'order_*.csv', 'orders', 'order_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('inventory_feed', 'inventory_*.csv', 'inventory', 'item_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('supplier_feed', 'supplier_*.csv', 'suppliers', 'supplier_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('category_feed', 'category_*.csv', 'categories', 'category_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('employee_feed', 'employee_*.csv', 'employees', 'employee_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('transaction_feed', 'transaction_*.csv', 'transactions', 'transaction_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('location_feed', 'location_*.csv', 'locations', 'location_id', true, ',', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('customer_pipe_feed', 'customer_pipe_*.csv', 'customers_pipe', 'customer_id', true, '|', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('product_semicolon_feed', 'product_semicolon_*.csv', 'products_semicolon', 'product_id', true, ';', '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO feed_configs (name, file_pattern, table_name, id_column, enabled, delimiter, quote_char, escape_char, skip_header_record, created_at, updated_at) VALUES ('inventory_tab_feed', 'inventory_tab_*.csv', 'inventory_tab', 'item_id', true, CHAR(9), '"', '\\', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'customer_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'first_name', 'VARCHAR(100)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'last_name', 'VARCHAR(100)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'email', 'VARCHAR(255)', false, false, true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'phone', 'VARCHAR(20)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_feed'), 'address', 'VARCHAR(500)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'product_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'name', 'VARCHAR(255)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'description', 'VARCHAR(1000)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'price', 'DECIMAL(10,2)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'category', 'VARCHAR(100)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_feed'), 'supplier_id', 'VARCHAR(50)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'order_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'customer_id', 'VARCHAR(50)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'product_id', 'VARCHAR(50)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'quantity', 'INTEGER', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'unit_price', 'DECIMAL(10,2)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'total_amount', 'DECIMAL(12,2)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'order_date', 'DATE', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'order_feed'), 'status', 'VARCHAR(50)', true, false, false, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'customer_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'first_name', 'VARCHAR(100)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'last_name', 'VARCHAR(100)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'email', 'VARCHAR(255)', false, false, true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'phone', 'VARCHAR(20)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'customer_pipe_feed'), 'address', 'VARCHAR(500)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'product_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'name', 'VARCHAR(255)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'description', 'VARCHAR(1000)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'price', 'DECIMAL(10,2)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'category', 'VARCHAR(100)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'product_semicolon_feed'), 'supplier_id', 'VARCHAR(50)', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO feed_column_configs (feed_config_id, name, type, nullable, primary_key, unique_constraint, default_value, created_at, updated_at) VALUES
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'item_id', 'VARCHAR(50)', false, true, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'product_id', 'VARCHAR(50)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'location', 'VARCHAR(100)', false, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'quantity_on_hand', 'INTEGER', false, false, false, '0', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'reserved_quantity', 'INTEGER', false, false, false, '0', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'reorder_level', 'INTEGER', true, false, false, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM feed_configs WHERE name = 'inventory_tab_feed'), 'last_updated', 'TIMESTAMP', false, false, false, 'CURRENT_TIMESTAMP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
