package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;

public class Schema {
    private String tableName;
    private String tablePath;
    private HashMap<String, Integer> colIndex = new HashMap<String, Integer>();
    private HashMap<String, String> colType = new HashMap<String, String>();

    public void init(CreateTable schema) throws NullPointerException {
        this.tableName = schema.getTable().getName();
        this.tablePath = "data/" + this.tableName + ".dat";
        int columnIndex = 0;  // column index, starting from 0
        for(ColumnDefinition col : schema.getColumnDefinitions()) {
            colIndex.put(col.getColumnName(), columnIndex);
            colType.put(col.getColumnName(), col.getColDataType().getDataType());
            columnIndex++;
        }
    }
    
    public void init(String table, HashMap<String, Integer> colIndex, HashMap<String, String> colType, String name) {
    	this.tableName = name;
    	this.colIndex = colIndex;
    	this.colType = colType;
    }

    /**
     *
     * @return table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * 
     * @return table path
     */
    public String getPath() {
        return this.tablePath;
    }

    /**
     * Get column type by column name
     * @param colName: name of the column
     * @return type of the column
     */
    public String getColType(String colName) {

        return this.colType.get(colName);
    }

    /**
     * Get column index by column name
     * @param colName: name of the column
     * @return index of the column
     */
    public int getColIndex(String colName) {
        int i = this.colIndex.get(colName);
        return i;

    }
    
    public HashMap<String, Integer> getIndex() {
    	return this.colIndex;
    }
    
    public HashMap<String, String> getType() {
    	return this.colType;
    }
    
}
