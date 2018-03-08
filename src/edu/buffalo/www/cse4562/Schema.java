package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;
import java.util.List;

public class Schema {
    private String tableName;
    private HashMap<String, String> tablePath = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> colIndex = new HashMap<>();
    private HashMap<String, HashMap<Integer, String>> typeByIndex = new HashMap<>();
    private HashMap<String, HashMap<String, String>> typeByName = new HashMap<>();
    //===============================================
//    private static HashMap<String, TableDef> schema = new HashMap<>();
//
//    /**
//     * Add a table definition to schema
//     * @param table
//     */
//    public static void addTable(CreateTable table) {
//        TableDef t = new TableDef(table);
//        schema.put(t.getTableName(), t);
//    }
//
//    /**
//     * Get table path by table name
//     * @param tableName table name
//     * @return relative path
//     */
//    public static String getPath(String tableName) {
//        return schema.get(tableName).getTablePath();
//    }
//
//    /**
//     * Get the list of column type
//     * @param tableName table name
//     * @return column type list
//     */
//    public static List<String> getType(String tableName) {
//        return schema.get(tableName).getTypeList();
//    }
//
//    /**
//     * Get column index by column name
//     * @param tableName table name
//     * @param colName column name
//     * @return column index
//     */
//    public static int getIndex(String tableName, String colName) {
//        return schema.get(tableName).getColIndex(colName);
//    }

//===============================================

    /**
     * Construction method
     *
     * @param schema
     */
    public void init(CreateTable schema) throws NullPointerException {
        this.tableName = schema.getTable().getName();
        this.tablePath.put(this.tableName, "data/" + this.tableName + ".dat");

        int columnIndex = 0;  // column index, starting from 0
        HashMap<String, Integer> tempColIndex = new HashMap<>();
        HashMap<Integer, String> tempTypeByIndex = new HashMap<>();
        HashMap<String, String> tempTypeByName = new HashMap<>();
        for (ColumnDefinition col : schema.getColumnDefinitions()) {
            // temp variables to be put
            String colName = col.getColumnName();
            String colType = col.getColDataType().getDataType();
            tempColIndex.put(colName, columnIndex);
            tempTypeByIndex.put(columnIndex, col.getColDataType().getDataType());
            tempTypeByName.put(colName, colType);
            columnIndex++;
        }
        this.colIndex.put(tableName, tempColIndex);
        this.typeByIndex.put(tableName, tempTypeByIndex);
        this.typeByName.put(tableName, tempTypeByName);
    }

    /**
     * Construction overload
     *
     * @param colIndex  map of <code>{column_name: column_index}</code>
     * @param colType   map of <code>{column_index: column_type}</code>
     * @param tableName table name
     */
    public void init(HashMap<String, Integer> colIndex, HashMap<String, String> colType, String tableName) {
        this.colIndex.put(tableName, colIndex);
        this.typeByName.put(tableName, colType);
    }

    /**
     * @return table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Get relative path of the table
     *
     * @param tableName name of table
     * @return table path
     */
    public String getPath(String tableName) {
        return this.tablePath.get(tableName);
    }

    /**
     * Get column type by table name and column index
     *
     * @param tableName name of table
     * @param colIndex  index of the column
     * @return type of the column
     */
    public String getColType(String tableName, int colIndex) {

        return this.typeByIndex.get(tableName).get(colIndex);
    }

    /**
     * Overload
     * Get column type by table name and column name
     *
     * @param tableName table name
     * @param colName column name
     * @return type of the column
     */
    public String getColType(String tableName, String colName) {
        return this.typeByName.get(tableName).get(colName);
    }

    /**
     * Get column index by table name and column name
     *
     * @param tableName name of table
     * @param colName   name of the column
     * @return index of the column
     */
    public int getColIndex(String tableName, String colName) {
        int i = this.colIndex.get(tableName).get(colName);
        return i;

    }

    /**
     * Return the map of <code>{column_name: column_type}</code> by table name
     *
     * @param tableName table name
     * @return map of <code>{column_name: column_index}</code>
     */
    public HashMap<String, Integer> getIndex(String tableName) {
        return this.colIndex.get(tableName);
    }

    /**
     * Return the map of <code>{column_index: column_type}</code> by table name
     *
     * @param tableName table name
     * @return map of <code>{column_index: column_type}</code>
     */
    public HashMap<String, String> getType(String tableName) {
        return this.typeByName.get(tableName);
    }

}
