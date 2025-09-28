# Spring Batch Feed Processor

A configurable Spring Batch application for processing CSV feed files into H2 database tables with dynamic schema creation, upsert operations, and comprehensive reporting.

## Features

- **Dynamic Table Creation**: Automatically creates database tables based on CSV file headers
- **Configurable Feed Processing**: Support for multiple feeds with individual configuration
- **Upsert Operations**: Insert new records or update existing ones based on ID column
- **Comprehensive Reporting**: Detailed processing reports stored in separate table
- **Scheduled Processing**: Daily automated processing with configurable cron schedule
- **Error Handling**: Robust error handling with file movement to error directory
- **REST API**: Manual trigger and monitoring endpoints
- **H2 Database**: In-memory database for easy setup and testing

## Configuration

### Feed Configuration (application.yml)

```yaml
feed:
  processor:
    input-directory: ./input
    processed-directory: ./processed
    error-directory: ./error
    
    feeds:
      - name: "customer_feed"
        file-pattern: "customer_*.csv"
        table-name: "customers"
        id-column: "customer_id"
        enabled: true
```

### Scheduling Configuration

```yaml
scheduling:
  enabled: true
  cron: "0 0 2 * * ?" # Daily at 2 AM
```

## Directory Structure

```
spring-batch-feed-processor/
├── input/          # Place CSV files here for processing
├── processed/      # Successfully processed files moved here
├── error/          # Failed files moved here
└── src/
    └── main/
        ├── java/
        │   └── com/example/feedprocessor/
        │       ├── FeedProcessorApplication.java
        │       ├── config/
        │       ├── entity/
        │       ├── repository/
        │       ├── service/
        │       ├── batch/
        │       ├── scheduler/
        │       └── controller/
        └── resources/
            ├── application.yml
            └── data.sql
```

## Usage

### 1. Setup

```bash
# Clone or create the project
mkdir spring-batch-feed-processor
cd spring-batch-feed-processor

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 2. Prepare Feed Files

Create CSV files in the `input` directory following the configured patterns:

**Example: customer_20241228.csv**
```csv
customer_id,first_name,last_name,email,phone
CUST001,John,Doe,john.doe@email.com,555-1234
CUST002,Jane,Smith,jane.smith@email.com,555-5678
```

**Example: product_20241228.csv**
```csv
product_id,name,category,price,description
PROD001,Laptop,Electronics,999.99,High-performance laptop
PROD002,Mouse,Electronics,29.99,Wireless optical mouse
```

### 3. Processing

#### Automatic Processing
- Files are automatically processed daily at 2 AM (configurable)
- Processed files are moved to `processed` directory
- Failed files are moved to `error` directory

#### Manual Processing
```bash
# Trigger processing via REST API
curl -X POST http://localhost:8080/api/feed-processing/trigger
```

### 4. Monitoring

#### View Processing Reports
```bash
# Get all recent reports
curl http://localhost:8080/api/feed-processing/reports

# Get reports for specific feed
curl http://localhost:8080/api/feed-processing/reports?feedName=customer_feed

# Get reports by status
curl http://localhost:8080/api/feed-processing/reports?status=SUCCESS

# Get processing summary
curl http://localhost:8080/api/feed-processing/reports/summary
```

#### H2 Console
Access the H2 database console at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:feeddb`
- Username: `sa`
- Password: (empty)

## Database Schema

### Dynamic Tables
Tables are created automatically based on CSV headers:
- Column names are sanitized (special characters replaced with underscores)
- ID column is set as PRIMARY KEY with VARCHAR(255)
- Other columns are VARCHAR(1000)
- `created_at` and `updated_at` timestamps are added automatically

### Processing Reports Table
```sql
CREATE TABLE processing_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feed_name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    table_name VARCHAR(255) NOT NULL,
    total_records BIGINT,
    inserted_records BIGINT,
    updated_records BIGINT,
    failed_records BIGINT,
    processing_start_time TIMESTAMP,
    processing_end_time TIMESTAMP,
    status VARCHAR(50),
    error_message VARCHAR(1000)
);
```

## Configuration for 9 Feeds

The application comes pre-configured for 9 different feed types:

1. **customer_feed** - Customer data
2. **product_feed** - Product catalog
3. **order_feed** - Order transactions
4. **inventory_feed** - Inventory levels
5. **supplier_feed** - Supplier information
6. **category_feed** - Product categories
7. **employee_feed** - Employee data
8. **transaction_feed** - Financial transactions
9. **location_feed** - Location/store data

Each feed can be individually enabled/disabled and configured with:
- Custom file patterns
- Target table names
- ID column specification

## Customization

### Adding New Feeds

Add new feed configuration in `application.yml`:

```yaml
feeds:
  - name: "new_feed"
    file-pattern: "new_data_*.csv"
    table-name: "new_table"
    id-column: "id"
    enabled: true
```

### Changing Schedule

Modify the cron expression in `application.yml`:

```yaml
scheduling:
  cron: "0 30 1 * * ?" # Daily at 1:30 AM
```

### Custom Processing Logic

Extend or modify the following classes:
- `FeedItemProcessor` - Custom data transformation
- `FeedItemWriter` - Custom write logic
- `DynamicTableService` - Custom table creation logic

## Error Handling

- Invalid CSV files are moved to error directory
- Processing errors are logged and reported
- Failed records are counted in processing reports
- Partial success status for jobs with some failed records

## Logging

Logs are configured to show:
- Processing start/end times
- Record counts (total, inserted, updated, failed)
- Error details
- File movement operations

## Testing

```bash
# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring.profiles.active=test
```

## Production Considerations

1. **Database**: Replace H2 with production database (PostgreSQL, MySQL, etc.)
2. **File Storage**: Use network storage or cloud storage for feed files
3. **Monitoring**: Integrate with monitoring tools (Prometheus, Grafana)
4. **Security**: Add authentication and authorization
5. **Scaling**: Consider distributed processing for large files
6. **Backup**: Implement backup strategy for processed data

## API Endpoints

- `POST /api/feed-processing/trigger` - Manually trigger processing
- `GET /api/feed-processing/reports` - Get processing reports
- `GET /api/feed-processing/reports/summary` - Get processing summary
- `GET /api/feed-processing/health` - Health check
- `GET /h2-console` - H2 database console

## Dependencies

- Spring Boot 3.2.0
- Spring Batch 5.1.0
- Spring Data JPA
- H2 Database
- OpenCSV 5.8
- Spring Boot Actuator
