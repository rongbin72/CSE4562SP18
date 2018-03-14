package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;
import java.util.List;

public class TableDef {
    private String tableName;
    private String tablePath;
    private HashMap<String, Integer> colIndex = new HashMap<>();
    private HashMap<Integer, String> colType = new HashMap<>();
    private List<List<PrimitiveValue>> table;
    private java.util.Iterator<List<PrimitiveValue>> tableIter;

    /**
     * Construction method
     *
     * @param def CreateTable instance
     */
    public TableDef(CreateTable def) {
        this.tableName = def.getTable().getName().toUpperCase();
        this.tablePath = "data/" + this.tableName + ".dat";

        int columnIndex = 0;
        for (ColumnDefinition col : def.getColumnDefinitions()) {
            this.colIndex.put(col.getColumnName().toUpperCase(), columnIndex);
            this.colType.put(columnIndex, col.getColDataType().getDataType());
            columnIndex++;
        }
    }

    public TableDef(String tableName, HashMap<String, Integer> colIndex, List<List<PrimitiveValue>> table) {
        this.tableName = tableName.toUpperCase();
        this.table = table;
        this.colIndex = colIndex;
    }

    public TableDef(String tableName, HashMap<String, Integer> colIndex) {
        this.tableName = tableName.toUpperCase();
        this.colIndex = colIndex;
    }

    /**
     * Get table name
     *
     * @return table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Get table Path
     *
     * @return relative path
     */
    public String getTablePath() {
        return this.tablePath;
    }


    /**
     * Get column index by column name
     *
     * @param colName column name
     * @return column index
     */
    public int getColIndex(String colName) {
        return this.colIndex.get(colName.toUpperCase());
    }

    public String getColType(int index) {
        return this.colType.get(index);
    }

    public List<List<PrimitiveValue>> getTableContent() {
        return this.table;
    }

    public void setTableContent(List<List<PrimitiveValue>> table) {
        this.table = table;
        this.tableIter = table.iterator();
    }

    public List<PrimitiveValue> getLine() {
        if (this.tableIter.hasNext()) {
            return this.tableIter.next();
        } else {
            this.tableIter = table.iterator();
            return null;
        }
    }

    /**
     * Return colIndex
     * @return colIndex
     */
    public HashMap<String, Integer> getIndexHash() {
        return this.colIndex;
    }

    /**
     * Add a new column, update colIndex
     * @param colName name of new column
     */
    public void addColumn(String colName) {
        this.colIndex.put(colName.toUpperCase(), this.colIndex.size());
    }

    public void removeCol(String colName) {
        this.colIndex.remove(colName.toUpperCase());
    }

}
