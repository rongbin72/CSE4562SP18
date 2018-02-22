package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;

public class Schema {
    private String tableName;
    private String tablePath;
    private HashMap<String, HashMap<String, Integer>> colIndex = new HashMap<String, HashMap<String, Integer>>();
    private HashMap<String, HashMap<String, String>> colType = new HashMap<String, HashMap<String, String>>();

    public void init(CreateTable schema) throws NullPointerException {
        this.tableName = schema.getTable().getName();
        this.tablePath = "data/" + this.tableName + ".dat";
        int columnIndex = 0;  // column index, starting from 0
        for(ColumnDefinition col : schema.getColumnDefinitions()) {
            colIndex.put(col.getColumnName(), new Hashmap<String, Integer>{});
            colType.put(col.getColumnName(), col.getColDataType().getDataType());
            columnIndex++;
        }
    }
    
    public void init(HashMap<String, Integer> colIndex, HashMap<String, String> colType, String name) {
    	this.tableName = name;
    	this.colIndex.put(name, colIndex);
    	this.colType.put(name, colType);
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
    public String getPath(String tableName) {
        return this.tablePath;
    }

    /**
     * Get column type by column name
     * @param colName: name of the column
     * @return type of the column
     */
    public String getColType(String tableName, String colName) {

        return this.colType.get(colName);
    }

    /**
     * Get column index by column name
     * @param colName: name of the column
     * @return index of the column
     */
    public int getColIndex(String tableName, String colName) {
        int i = this.colIndex.get(colName);
        return i;

    }
    
    public HashMap<String, Integer> getIndex(String tableName) {
    	return this.colIndex.get(tableName);
    }
    
    public HashMap<String, String> getType(String tableName) {
    	return this.colType.get(tableName);
    }
    
}
