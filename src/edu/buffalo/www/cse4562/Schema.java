package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PrimitiveIterator;

public class Schema {
//    private String tableName;
//    private HashMap<String, String> tablePath = new HashMap<>();
//    private HashMap<String, HashMap<String, Integer>> colIndex = new HashMap<>();
//    private HashMap<String, HashMap<Integer, String>> typeByIndex = new HashMap<>();
//    private HashMap<String, HashMap<String, String>> typeByName = new HashMap<>();
    private static HashMap<String, TableDef> schema = new HashMap<>();
    private static List<String> extraTable = new ArrayList<>();
    private static HashMap<String, String> extraCol = new HashMap<>();

    /**
     * Add a table definition to schema
     * @param table
     */
    public static void addTable(CreateTable table) {
        schema.put(table.getTable().getName(), new TableDef(table));
    }

    public static void addTable(String tableName, HashMap<String, Integer> colIndex, List<List<PrimitiveValue>> table) {
        schema.put(tableName, new TableDef(tableName, colIndex, table));
        extraTable.add(tableName);
    }

    public static void addTable(String oldName, String newName) {
        schema.put(newName, schema.get(oldName));
        extraTable.add(newName);
    }

    public static void addColumn(String tableName, String colName) {
        schema.get(tableName).addColumn(colName);
        extraCol.put(tableName, colName);
    }

    public static HashMap<String, Integer> getIndxHash(String tableName) {
        return schema.get(tableName).getIndexHash();
    }

    public static List<List<PrimitiveValue>> getTableContent(String tableName) {
        return schema.get(tableName).getTableContent();
    }

    public static int getColIndex(String tableName, String colName) {
        return schema.get(tableName).getColIndex(colName);
    }

    public static String getColType(String tableName, int index) {
        return schema.get(tableName).getColType(index);
    }

    /**
     * Get relative path of the table
     *
     * @param tableName name of table
     * @return table path
     */
    public static String getPath(String tableName) {
        return schema.get(tableName).getTablePath();
    }

    /**
     * Reset schema, remove extra table and extra column
     */
    public static void reset() {
        for (String t : extraTable) {
            schema.remove(t);
        }

        for (String c : extraCol.keySet()) {
            schema.get(c).removeCol(extraCol.get(c));
        }
        extraTable.clear();
        extraCol.clear();
    }


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
//    public void init(CreateTable schema) throws NullPointerException {
//        this.tableName = schema.getTable().getName();
//        this.tablePath.put(this.tableName, "data/" + this.tableName + ".dat");
//
//        int columnIndex = 0;  // column index, starting from 0
//        HashMap<String, Integer> tempColIndex = new HashMap<>();
//        HashMap<Integer, String> tempTypeByIndex = new HashMap<>();
//        HashMap<String, String> tempTypeByName = new HashMap<>();
//        for (ColumnDefinition col : schema.getColumnDefinitions()) {
//            // temp variables to be put
//            String colName = col.getColumnName();
//            String colType = col.getColDataType().getDataType();
//            tempColIndex.put(colName, columnIndex);
//            tempTypeByIndex.put(columnIndex, col.getColDataType().getDataType());
//            tempTypeByName.put(colName, colType);
//            columnIndex++;
//        }
//        this.colIndex.put(tableName, tempColIndex);
//        this.typeByIndex.put(tableName, tempTypeByIndex);
//        this.typeByName.put(tableName, tempTypeByName);
//    }
//
//    /**
//     * Construction overload
//     *
//     * @param colIndex  map of <code>{column_name: column_index}</code>
//     * @param colType   map of <code>{column_index: column_type}</code>
//     * @param tableName table name
//     */
//    public void init(HashMap<String, Integer> colIndex, HashMap<String, String> colType, String tableName) {
//        this.colIndex.put(tableName, colIndex);
//        this.typeByName.put(tableName, colType);
//    }
//
//    /**
//     * @return table name
//     */
//    public String getTableName() {
//        return this.tableName;
//    }
//
//
//
//    /**
//     * Get column type by table name and column index
//     *
//     * @param tableName name of table
//     * @param colIndex  index of the column
//     * @return type of the column
//     */
//    public String getColType(String tableName, int colIndex) {
//
//        return this.typeByIndex.get(tableName).get(colIndex);
//    }
//
//    /**
//     * Overload
//     * Get column type by table name and column name
//     *
//     * @param tableName table name
//     * @param colName column name
//     * @return type of the column
//     */
//    public String getColType(String tableName, String colName) {
//        return this.typeByName.get(tableName).get(colName);
//    }
//
//    /**
//     * Get column index by table name and column name
//     *
//     * @param tableName name of table
//     * @param colName   name of the column
//     * @return index of the column
//     */
//    public int getColIndex(String tableName, String colName) {
//        int i = this.colIndex.get(tableName).get(colName);
//        return i;
//
//    }
//
//    /**
//     * Return the map of <code>{column_name: column_type}</code> by table name
//     *
//     * @param tableName table name
//     * @return map of <code>{column_name: column_index}</code>
//     */
//    public HashMap<String, Integer> getIndex(String tableName) {
//        return this.colIndex.get(tableName);
//    }
//
//    /**
//     * Return the map of <code>{column_index: column_type}</code> by table name
//     *
//     * @param tableName table name
//     * @return map of <code>{column_index: column_type}</code>
//     */
//    public HashMap<String, String> getType(String tableName) {
//        return this.typeByName.get(tableName);
//    }

}
