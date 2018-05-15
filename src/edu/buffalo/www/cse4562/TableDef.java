package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.io.*;
import java.util.*;

public class TableDef {
    private String tableName;
    private String tablePath;
    private int length;
    private HashMap<String, Integer> colIndex = new HashMap<>();
    private HashMap<Integer, String> colType = new HashMap<>();
    private Set<String> indexOn = new HashSet<>();
    private HashMap<String, TreeMap<String, ArrayList<Long>>> index = new HashMap<>();


    /**
     * Construction method
     *
     * @param def CreateTable instance
     */
    TableDef(CreateTable def) {
        this.tableName = def.getTable().getName();
        this.tablePath = "data/" + this.tableName + ".dat";

        int columnIndex = 0;
        for (ColumnDefinition col : def.getColumnDefinitions()) {
            // init colIndex, colType
            this.colIndex.put(col.getColumnName(), columnIndex);
            this.colType.put(columnIndex, col.getColDataType().getDataType());
            columnIndex++;

            // init indexOn
            if (col.getColumnSpecStrings() != null) {
                this.indexOn.add(col.getColumnName());
            }
        }
        // add indexOn column
        if (def.getIndexes() != null) {
            for (Index i : def.getIndexes()) {
                this.indexOn.addAll(i.getColumnsNames());
            }
        }

        switch (this.tableName) {
            case "PART":
                break;
            case "SUPPLIER":
                break;
            case "PARTSUPP":
                break;
            case "CUSTOMER":
                break;
            case "ORDERS":
                break;
            case "LINEITEM":
                break;
            case "NATION":
                break;
            case "REGION":
                break;
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
    String getTablePath() {
        return this.tablePath;
    }

    /**
     * Get column index by column name
     *
     * @param colName column name
     * @return column index
     */
    int getColIndex(String colName) {
        return this.colIndex.get(colName);
    }

    String getColType(int index) {
        return this.colType.get(index);
    }

    /**
     * Return deep copy of Index hash
     *
     * @return colIndex
     */
    HashMap<String, Integer> getIndexHash() {
        return this.colIndex;
    }

    boolean hasIndex(String colName) {
        return this.indexOn.contains(colName);
    }

    public int getLength() {
        return length;
    }


    List<Long> getIndexList(Expression e) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream("indexes/" + this.tableName);
        ObjectInputStream ob = new ObjectInputStream(in);
        this.index = (HashMap<String, TreeMap<String, ArrayList<Long>>>) ob.readObject();
        in.close();
        ob.close();


        return null;
    }

    void buildIndex() throws IOException {
        if (this.indexOn != null) {
            // init
            RandomAccessFile file = new RandomAccessFile(this.tablePath, "r");
            this.length = 0;
            String line;
            int index;
            long cursor = 0;
            String value;

            for (String on : this.indexOn) this.index.put(on, new TreeMap<>());
            while ((line = file.readLine()) != null) {
                for (String on : this.indexOn) {
                    index = this.colIndex.get(on);
                    value = line.split("\\|")[index];
                    if (this.index.get(on).containsKey(value)) {
                        this.index.get(on).get(value).add(cursor);
                    } else {
                        this.index.get(on).put(value, new ArrayList<>());
                        this.index.get(on).get(value).add(cursor);
                    }
                }
                cursor = file.getFilePointer();
                this.length++;
            }
            file.close();
            // save index to file
            FileOutputStream out = new FileOutputStream("indexes/" + this.tableName);
            ObjectOutputStream ob = new ObjectOutputStream(out);
            ob.writeObject(this.index);
            out.close();
            ob.close();
            this.index.clear();
        }
    }

    boolean hasColumn(String colName) {
        return this.colIndex.keySet().contains(colName);
    }
}
