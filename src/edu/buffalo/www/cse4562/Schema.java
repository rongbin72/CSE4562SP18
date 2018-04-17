package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Schema {
    private static HashMap<String, TableDef> schema = new HashMap<>();
    private static HashMap<String, Expression> colAliasMap = new HashMap<>();
    private static HashMap<String, String> tableAliasMap = new HashMap<>();

    public static HashMap<String, String> getTableAliasMap() {
        return tableAliasMap;
    }

    public static HashMap<String, Expression> getColAliasMap() {
        return colAliasMap;
    }

    /**
     * add column alias to map
     * @param origin name
     * @param alias
     */
    public static void addColAlias(String alias, Expression origin) {
        colAliasMap.put(alias, origin);
    }
    
    /**
     * add table alias to map
     * @param origin name
     * @param alias
     */
    public static void addTableAlias(String alias, String origin) {
        tableAliasMap.put(alias, origin);
    }

    public static Expression getColAlias(String alias) {
        return colAliasMap.get(alias);
    }

    public static String getTableAlias(String alias) {
        return tableAliasMap.get(alias);
    }
    /**
     * Add a table definition to schema when create table
     * @param table
     */
    public static void addTable(CreateTable table) {
        schema.put(table.getTable().getName(), new TableDef(table));
    }

    public static LinkedHashMap<String, Integer> getIndexHash(String tableName) {
        return schema.get(tableName).getIndexHash();
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

}
