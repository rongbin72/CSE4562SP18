package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PrimitiveIterator;

public class Schema {
    private static HashMap<String, TableDef> schema = new HashMap<>();
    private static List<String> extraTable = new ArrayList<>();
    private static HashMap<String, String> extraCol = new HashMap<>();

    /**
     * Add a table definition to schema
     * @param table
     */
    public static void addTable(CreateTable table) {
        schema.put(table.getTable().getName().toUpperCase(), new TableDef(table));
    }

    public static void addTable(String tableName, HashMap<String, Integer> colIndex, List<List<PrimitiveValue>> table) {
        schema.put(tableName.toUpperCase(), new TableDef(tableName, colIndex, table));
        extraTable.add(tableName.toUpperCase());
    }

    public static void addTable(String oldName, String newName) {
        schema.put(newName.toUpperCase(), schema.get(oldName));
        extraTable.add(newName.toUpperCase());
    }

    public static void addTable(String tableName, HashMap<String, Integer> colIndex) {
        schema.put(tableName.toUpperCase(), new TableDef(tableName, colIndex));
    }

    public static void addColumn(String tableName, String colName) {
        schema.get(tableName.toUpperCase()).addColumn(colName.toUpperCase());
        extraCol.put(tableName.toUpperCase(), colName.toUpperCase());
    }

    public static void setTableContent(String tableName, List<List<PrimitiveValue>> tableContent) {
        schema.get(tableName).setTableContent(tableContent);
    }

    public static List<PrimitiveValue> getLine(String tableName) {
        return schema.get(tableName).getLine();
    }

    public static HashMap<String, Integer> getIndxHash(String tableName) {
        return schema.get(tableName.toUpperCase()).getIndexHash();
    }

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
            for (String c : extraCol.keySet()) {
                schema.get(c.toUpperCase()).removeCol(extraCol.get(c.toUpperCase()));
            } 
            extraCol.clear();
    	}
    }
}
