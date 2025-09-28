package com.example.feedprocessor.service;

import com.example.feedprocessor.config.FeedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamicTableService {
    
    private static final Logger logger = LoggerFactory.getLogger(DynamicTableService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final int BATCH_SIZE = 1000;
    
    public void createTableIfNotExists(String tableName, List<String> headers, String idColumn) {
        createTableIfNotExists(tableName, headers, idColumn, null);
    }
    
    public void createTableIfNotExists(String tableName, List<String> headers, String idColumn, 
                                     FeedConfiguration.SchemaConfig schema) {
        try {
            if (!tableExists(tableName)) {
                String createTableSql = buildCreateTableSql(tableName, headers, idColumn, schema);
                logger.info("Creating table: {}", tableName);
                logger.debug("SQL: {}", createTableSql);
                jdbcTemplate.execute(createTableSql);
            } else {
                addMissingColumns(tableName, headers, schema);
            }
        } catch (Exception e) {
            logger.error("Error creating/updating table {}: {}", tableName, e.getMessage(), e);
            throw new RuntimeException("Failed to create/update table: " + tableName, e);
        }
    }
    
    private boolean tableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = UPPER(?)";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            logger.debug("Error checking if table exists: {}", e.getMessage());
            return false;
        }
    }
    
    private String buildCreateTableSql(String tableName, List<String> headers, String idColumn) {
        return buildCreateTableSql(tableName, headers, idColumn, null);
    }
    
    private String buildCreateTableSql(String tableName, List<String> headers, String idColumn, 
                                     FeedConfiguration.SchemaConfig schema) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName).append(" (");
        
        Map<String, FeedConfiguration.ColumnConfig> columnConfigMap = null;
        if (schema != null && schema.getColumns() != null) {
            columnConfigMap = schema.getColumns().stream()
                    .collect(Collectors.toMap(
                            col -> sanitizeColumnName(col.getName()),
                            col -> col
                    ));
        }
        
        for (int i = 0; i < headers.size(); i++) {
            String column = sanitizeColumnName(headers.get(i));
            sql.append(column);
            
            FeedConfiguration.ColumnConfig columnConfig = columnConfigMap != null ? 
                    columnConfigMap.get(column) : null;
            
            if (columnConfig != null) {
                sql.append(" ").append(columnConfig.getType());
                
                if (columnConfig.isPrimaryKey()) {
                    sql.append(" PRIMARY KEY");
                } else {
                    if (!columnConfig.isNullable()) {
                        sql.append(" NOT NULL");
                    }
                    if (columnConfig.isUnique()) {
                        sql.append(" UNIQUE");
                    }
                    if (columnConfig.getDefaultValue() != null) {
                        sql.append(" DEFAULT ").append(columnConfig.getDefaultValue());
                    }
                }
            } else {
                if (column.equalsIgnoreCase(idColumn)) {
                    sql.append(" VARCHAR(255) PRIMARY KEY");
                } else {
                    sql.append(" VARCHAR(1000)");
                }
            }
            
            if (i < headers.size() - 1) {
                sql.append(", ");
            }
        }
        
        sql.append(", created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        sql.append(", updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        sql.append(")");
        
        return sql.toString();
    }
    
    private void addMissingColumns(String tableName, List<String> headers) {
        addMissingColumns(tableName, headers, null);
    }
    
    private void addMissingColumns(String tableName, List<String> headers, 
                                 FeedConfiguration.SchemaConfig schema) {
        try {
            List<String> existingColumns = getExistingColumns(tableName);
            
            Map<String, FeedConfiguration.ColumnConfig> columnConfigMap = null;
            if (schema != null && schema.getColumns() != null) {
                columnConfigMap = schema.getColumns().stream()
                        .collect(Collectors.toMap(
                                col -> sanitizeColumnName(col.getName()),
                                col -> col
                        ));
            }
            
            for (String header : headers) {
                String columnName = sanitizeColumnName(header);
                if (!existingColumns.contains(columnName.toUpperCase())) {
                    FeedConfiguration.ColumnConfig columnConfig = columnConfigMap != null ? 
                            columnConfigMap.get(columnName) : null;
                    
                    StringBuilder alterSql = new StringBuilder();
                    alterSql.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ").append(columnName);
                    
                    if (columnConfig != null) {
                        alterSql.append(" ").append(columnConfig.getType());
                        if (!columnConfig.isNullable()) {
                            alterSql.append(" NOT NULL");
                        }
                        if (columnConfig.getDefaultValue() != null) {
                            alterSql.append(" DEFAULT ").append(columnConfig.getDefaultValue());
                        }
                    } else {
                        alterSql.append(" VARCHAR(1000)");
                    }
                    
                    logger.info("Adding missing column {} to table {}", columnName, tableName);
                    jdbcTemplate.execute(alterSql.toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error adding missing columns to table {}: {}", tableName, e.getMessage(), e);
        }
    }
    
    private List<String> getExistingColumns(String tableName) {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = UPPER(?)";
        return jdbcTemplate.queryForList(sql, String.class, tableName);
    }
    
    public String sanitizeColumnName(String columnName) {
        return columnName.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();
    }
    
    public boolean recordExists(String tableName, String idColumn, String idValue) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + sanitizeColumnName(idColumn) + " = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idValue);
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Error checking if record exists: {}", e.getMessage());
            return false;
        }
    }
    
    public void insertRecord(String tableName, Map<String, String> record) {
        try {
            List<String> columns = record.keySet().stream()
                    .map(this::sanitizeColumnName)
                    .collect(Collectors.toList());
            
            String columnsSql = String.join(", ", columns) + ", created_at, updated_at";
            String valuesSql = columns.stream().map(c -> "?").collect(Collectors.joining(", ")) + ", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP";
            
            String sql = "INSERT INTO " + tableName + " (" + columnsSql + ") VALUES (" + valuesSql + ")";
            
            Object[] values = new Object[record.size()];
            int i = 0;
            for (String key : record.keySet()) {
                values[i++] = record.get(key);
            }
            
            jdbcTemplate.update(sql, values);
        } catch (Exception e) {
            logger.error("Error inserting record into {}: {}", tableName, e.getMessage(), e);
            throw new RuntimeException("Failed to insert record", e);
        }
    }
    
    public void insertRecordsBatch(String tableName, List<Map<String, String>> records) {
        if (records.isEmpty()) return;
        
        try {
            Map<String, String> firstRecord = records.get(0);
            List<String> columns = firstRecord.keySet().stream()
                    .map(this::sanitizeColumnName)
                    .collect(Collectors.toList());
            
            String columnsSql = String.join(", ", columns) + ", created_at, updated_at";
            String valuesSql = columns.stream().map(c -> "?").collect(Collectors.joining(", ")) + ", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP";
            
            String sql = "INSERT INTO " + tableName + " (" + columnsSql + ") VALUES (" + valuesSql + ")";
            
            List<Object[]> batchArgs = new ArrayList<>();
            for (Map<String, String> record : records) {
                Object[] values = new Object[record.size()];
                int i = 0;
                for (String key : firstRecord.keySet()) {
                    values[i++] = record.get(key);
                }
                batchArgs.add(values);
            }
            
            jdbcTemplate.batchUpdate(sql, batchArgs);
            logger.info("Batch inserted {} records into {}", records.size(), tableName);
        } catch (Exception e) {
            logger.error("Error batch inserting records into {}: {}", tableName, e.getMessage(), e);
            throw new RuntimeException("Failed to batch insert records", e);
        }
    }
    
    public void updateRecord(String tableName, String idColumn, String idValue, Map<String, String> record) {
        try {
            List<String> setClauses = record.keySet().stream()
                    .filter(key -> !key.equalsIgnoreCase(idColumn))
                    .map(key -> sanitizeColumnName(key) + " = ?")
                    .collect(Collectors.toList());
            
            String setClausesStr = String.join(", ", setClauses) + ", updated_at = CURRENT_TIMESTAMP";
            
            String sql = "UPDATE " + tableName + " SET " + setClausesStr + 
                        " WHERE " + sanitizeColumnName(idColumn) + " = ?";
            
            List<Object> valuesList = new ArrayList<>();
            for (String key : record.keySet()) {
                if (!key.equalsIgnoreCase(idColumn)) {
                    valuesList.add(record.get(key));
                }
            }
            valuesList.add(idValue);
            
            jdbcTemplate.update(sql, valuesList.toArray());
        } catch (Exception e) {
            logger.error("Error updating record in {}: {}", tableName, e.getMessage(), e);
            throw new RuntimeException("Failed to update record", e);
        }
    }
}
