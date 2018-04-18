package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TableDef {
    private String tableName;
    private String tablePath;
    private LinkedHashMap<String, Integer> colIndex = new LinkedHashMap<>();
    private HashMap<Integer, String> colType = new HashMap<>();

    /**
     * Construction method
     *
     * @param def CreateTable instance
     */
    public TableDef(CreateTable def) {
        this.tableName = def.getTable().getName();
        this.tablePath = "data/" + this.tableName + ".dat";

        int columnIndex = 0;
        for (ColumnDefinition col : def.getColumnDefinitions()) {
            this.colIndex.put(col.getColumnName(), columnIndex);
            this.colType.put(columnIndex, col.getColDataType().getDataType());
            columnIndex++;
        }
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
        return this.colIndex.get(colName);
    }

    public String getColType(int index) {
        return this.colType.get(index);
    }

    /**
     * Return deep copy of Index hash
     * @return colIndex
     */
    public LinkedHashMap<String, Integer> getIndexHash()
    {
        return this.colIndex;
    }

}
