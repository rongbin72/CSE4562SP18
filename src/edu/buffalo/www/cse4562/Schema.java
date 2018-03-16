package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schema {
    private static HashMap<String, TableDef> schema = new HashMap<>();
    private static List<String> extraTable = new ArrayList<>();
    private static List<String> extraCol = new ArrayList<>(); // store names of table which has extra column

    /**
     * Add a table definition to schema when create table
     * @param table
     */
    public static void addTable(CreateTable table) {
        schema.put(table.getTable().getName().toUpperCase(), new TableDef(table));
    }

    /**
     * Call this method when subselect, will be deleted by reset
     * @param tableName subselect table name
     * @param colIndex column index
     * @param table subselect table content
     */
    public static void addTable(String tableName, HashMap<String, Integer> colIndex, List<List<PrimitiveValue>> table) {
        schema.put(tableName.toUpperCase(), new TableDef(tableName, colIndex, table));
        extraTable.add(tableName.toUpperCase());
    }

    /**
     * When table alias, will be deleted by reset.
     * Will copy every attributes except for table name and table content
     * @param oldName the name of table to be copy
     * @param newName new table name -> alias
     */
    public static void addTable(String oldName, String newName) {
        // copy old tableDef except table
        String path = schema.get(oldName).getTablePath();
        HashMap<String, Integer> colIndex = schema.get(oldName).getIndexHash();
        HashMap<Integer, String> colType = schema.get(oldName).getTypeHash();
        TableDef t = new TableDef(newName, path, colIndex, colType);
        schema.put(newName.toUpperCase(), t);
        extraTable.add(newName.toUpperCase());
    }

    /**
     * Schema of final output table, will be deleted by reset
     * @param tableName *
     * @param colIndex column index of final output table
     */
    public static void addTable(String tableName, HashMap<String, Integer> colIndex) {
        schema.put(tableName.toUpperCase(), new TableDef(tableName, colIndex));
        extraTable.add(tableName);
    }

    /**
     * Add new column when alias, will be deleted by reset
     * @param tableName table name
     * @param colName alias
     */
    public static void addColumn(String tableName, String colName) {
        schema.get(tableName.toUpperCase()).addColumn(colName.toUpperCase());
        extraCol.add(tableName.toUpperCase());
    }

    public static void setTableContent(String tableName, List<List<PrimitiveValue>> tableContent) {
        schema.get(tableName.toUpperCase()).setTableContent(tableContent);
    }

    public static List<PrimitiveValue> getLine(String tableName) {
        return schema.get(tableName).getLine();
    }

    public static HashMap<String, Integer> getIndxHash(String tableName) {
        return schema.get(tableName.toUpperCase()).getIndexHash();
    }

    /**
     * Return pointer not value
     * @param tableName table name
     */
    public static List<List<PrimitiveValue>> getTableContent(String tableName) {
        return schema.get(tableName.toUpperCase()).getTableContent();
    }

    public static int getColIndex(String tableName, String colName) {
        return schema.get(tableName.toUpperCase()).getColIndex(colName);
    }

    public static String getColType(String tableName, int index) {
        return schema.get(tableName.toUpperCase()).getColType(index);
    }

    /**
     * Get relative path of the table
     *
     * @param tableName name of table
     * @return table path
     */
    public static String getPath(String tableName) {
        return schema.get(tableName.toUpperCase()).getTablePath();
    }

    /**
     * Reset schema, remove extra table and extra column
     */
    public static void reset(int i) {
    	if(i == 1) {//remove table
    		for (String t : extraTable) {
                schema.remove(t.toUpperCase());
            }
    		extraTable.clear();
    	}        
    	else {
            for (String c : extraCol) {
                schema.get(c.toUpperCase()).removeCol();
            } 
            extraCol.clear();
    	}
    }
}
