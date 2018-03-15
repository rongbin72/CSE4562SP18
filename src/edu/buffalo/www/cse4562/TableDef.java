package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableDef {
    private String tableName;
    private String tablePath;
    private HashMap<String, Integer> colIndex = new HashMap<>();
    private HashMap<Integer, String> colType = new HashMap<>();
    private List<List<PrimitiveValue>> table;
    private List<String> extraCol = new ArrayList<>();
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

    public TableDef(String tableName, String tablePath, HashMap<String, Integer> colIndex, HashMap<Integer, String> colType) {
        this.tableName = tableName.toUpperCase();
        this.tablePath = tablePath;
        this.colIndex = colIndex;
        this.colType = colType;
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
     * Return deep copy of Index hash
     * @return colIndex
     */
    public HashMap<String, Integer> getIndexHash()
    {
        return new HashMap<String, Integer>(this.colIndex);
    }

    /**
     * Return deep copy of Type Hash
     * @return colType
     */
    public HashMap<Integer, String> getTypeHash() {
        return new HashMap<Integer, String>(this.colType);
    }

    /**
     * Add a new column, update colIndex
     * @param colName name of new column
     */
    public void addColumn(String colName)
    {
        this.colIndex.put(colName.toUpperCase(), this.colIndex.size());
        extraCol.add(colName);
    }

    /**
     * Delete all column in extraCol
     */
    public void removeCol() {
        if (!extraCol.isEmpty()) {
            for (String n : extraCol) {
                this.colIndex.remove(n);
            }
            extraCol.clear();
        }
    }
}
