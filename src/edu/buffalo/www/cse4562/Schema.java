package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;

public class Schema {
    private String tableName;
    private HashMap<String, String> tablePath = new HashMap<String, String>();
    private HashMap<String, HashMap<String, Integer>> colIndex = new HashMap<String, HashMap<String, Integer>>();
    private HashMap<String, HashMap<String, String>> colType = new HashMap<String, HashMap<String, String>>();

    /**
     * Construction method
     * @param schema 
     */
    public void init(CreateTable schema) throws NullPointerException {
        this.tableName = schema.getTable().getName();
        this.tablePath.put(this.tableName, "data/" + this.tableName + ".dat");

        int columnIndex = 0;  // column index, starting from 0
        HashMap<String, Integer> tempColIndex = new HashMap<String, Integer>();
        HashMap<String, String> tempColType = new HashMap<String, String>();
        for(ColumnDefinition col : schema.getColumnDefinitions()) {
            // temp variables to be put
            tempColIndex.put(col.getColumnName(), columnIndex);           
            tempColType.put(col.getColumnName(), col.getColDataType().getDataType());
            columnIndex++;
        }
        colIndex.put(tableName, tempColIndex);
        colType.put(tableName, tempColType);
    }
    
    /**
     * Construction overload
     * @param colIndex map of <code>{column_name: column_index}</code>
     * @param colType map of <code>{column_name: column_type}</code>
     * @param tableName table name
     */
    public void init(HashMap<String, Integer> colIndex, HashMap<String, String> colType, String tableName) {
    	this.colIndex.put(tableName, colIndex);
    	this.colType.put(tableName, colType);
    }

    /**
     *
     * @return table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Get relative path of the table
     * @param tableName name of table
     * @return table path
     */
    public String getPath(String tableName) {
        return this.tablePath.get(tableName);
    }

    /**
     * Get column type by table name and column name
     * @param tableName name of table
     * @param colName name of the column
     * @return type of the column
     */
    public String getColType(String tableName, String colName) {

        return this.colType.get(tableName).get(colName);
    }

    /**
     * Get column index by table name and column name
     * @param tableName name of table
     * @param colName name of the column
     * @return index of the column
     */
    public int getColIndex(String tableName, String colName) {
        int i = this.colIndex.get(tableName).get(colName);
        return i;

    }
    
    /**
     * Return the map of <code>{column_name: column_type}</code> by table name
     * @param tableName table name
     * @return map of <code>{column_name: column_index}</code>
     */
    public HashMap<String, Integer> getIndex(String tableName) {
    	return this.colIndex.get(tableName);
    }

    /**
     * Return the map of <code>{column_name: column_type}</code> by table name
     * @param tableName table name
     * @return map of <code>{column_name: column_type}</code>
     */
    public HashMap<String, String> getType(String tableName) {
    	return this.colType.get(tableName);
    }
    
}
