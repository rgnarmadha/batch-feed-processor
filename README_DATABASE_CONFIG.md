# Database Configuration Guide

## Overview
The Spring Batch Feed Processor supports both H2 (in-memory) and MySQL databases with configurable connection parameters.

## Database Profiles

### H2 Database (Default)
```bash
# No additional configuration needed - uses in-memory H2 database
mvn spring-boot:run
```

### MySQL Database (Local Development)
```bash
export DATABASE_PROFILE=mysql
export DATABASE_URL=jdbc:mysql://localhost:3306/feedprocessor?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
export DATABASE_USERNAME=feeduser
export DATABASE_PASSWORD=feedpassword
mvn spring-boot:run
```

### MySQL Database (Production)
```bash
export DATABASE_PROFILE=mysql-prod
export DATABASE_URL=jdbc:mysql://your-mysql-server:3306/feedprocessor?useSSL=true&serverTimezone=UTC
export DATABASE_USERNAME=your_username
export DATABASE_PASSWORD=your_password
mvn spring-boot:run
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_PROFILE` | Database profile to use (h2, mysql, mysql-prod) | h2 |
| `DATABASE_URL` | JDBC connection URL | H2 in-memory URL |
| `DATABASE_USERNAME` | Database username | sa (H2), feeduser (MySQL) |
| `DATABASE_PASSWORD` | Database password | empty (H2), feedpassword (MySQL) |
| `DATABASE_POOL_SIZE` | Maximum connection pool size | 20 |
| `DATABASE_MIN_IDLE` | Minimum idle connections | 5 |
| `JPA_DDL_AUTO` | Hibernate DDL mode | update |
| `JPA_SHOW_SQL` | Show SQL statements in logs | false |

## MySQL Setup

### 1. Install MySQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 2. Create Database and User
```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE feedprocessor;

-- Create user and grant permissions
CREATE USER 'feeduser'@'localhost' IDENTIFIED BY 'feedpassword';
GRANT ALL PRIVILEGES ON feedprocessor.* TO 'feeduser'@'localhost';
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

### 3. Test Connection
```bash
mysql -u feeduser -p feedprocessor
```

## Delimiter Configuration

Each feed can be configured with custom delimiters:

```yaml
feeds:
  - name: "pipe_separated_feed"
    file-pattern: "data_*.psv"
    table-name: "pipe_data"
    id-column: "id"
    delimiter: "|"
    quote-char: "\""
    escape-char: "\\"
    skip-header-record: true

  - name: "semicolon_separated_feed"
    file-pattern: "data_*.ssv"
    table-name: "semicolon_data"
    id-column: "id"
    delimiter: ";"
    quote-char: "\""
    escape-char: "\\"
    skip-header-record: true

  - name: "tab_separated_feed"
    file-pattern: "data_*.tsv"
    table-name: "tab_data"
    id-column: "id"
    delimiter: "\t"
    quote-char: "\""
    escape-char: "\\"
    skip-header-record: true
```

## Supported Delimiters
- Comma (`,`) - Default CSV format
- Pipe (`|`) - Common in data warehousing
- Semicolon (`;`) - European CSV format
- Tab (`\t`) - TSV format
- Any single character delimiter

## Testing Different Configurations

Sample files are provided in the `sample-data/` directory:
- `customer_pipe_20241230.csv` - Pipe-separated values
- `product_semicolon_20241230.csv` - Semicolon-separated values
- `inventory_tab_20241230.csv` - Tab-separated values

To test with different delimiters, update the feed configuration in `application.yml` and place the corresponding files in the `input/` directory.
