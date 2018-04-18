package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.*;

public class Tuple {
    private List<PrimitiveValue> tuple = new ArrayList<>();
    private HashMap<String, LinkedHashMap<String, Integer>> indexHash = new HashMap<>();
    private HashMap<String, String> tableAliasMap;
    private HashMap<String, Expression> colAliasMap;
    private List<Column> groups = new ArrayList<>();
    private Evaluation eval = new Evaluation();
    private String tableName;

    public void setIndexHash(HashMap<String, LinkedHashMap<String, Integer>> indexHash) {
        this.indexHash = indexHash;
    }

    public HashMap<String, String> getTableAliasMap() {
        return tableAliasMap;
    }

    public void setTableAliasMap(HashMap<String, String> tableAliasMap) {
        this.tableAliasMap = tableAliasMap;
    }

    public HashMap<String, Expression> getColAliasMap() {
        return colAliasMap;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setColAliasMap(HashMap<String, Expression> colAliasMap) {
        this.colAliasMap = colAliasMap;
    }

    public void setTuple(List<PrimitiveValue> tuple) {
        this.tuple = tuple;
    }

    public boolean haveGroups() {
        return this.groups != null;
    }

    public List<Column> getGroups() {
        return groups;
    }

    public void setGroups(List<Column> groups) {
        this.groups = groups;
    }

    public Tuple(Tuple tuple) {
        this.tableName = tuple.getTableName();
        this.indexHash = tuple.getIndexHash();
        this.tableAliasMap = tuple.getTableAliasMap();
        this.colAliasMap = tuple.getColAliasMap();
        this.tuple = new ArrayList<>(tuple.getTuple());
    }

    /**
     * Used by Read to init tuple
     *
     * @param tableName table name
     * @param tuple     tuple
     */
    public Tuple(String tableName, List<PrimitiveValue> tuple) {
        this.tableName = tableName;
        this.indexHash.put(tableName, Schema.getIndexHash(tableName));
        this.tableAliasMap = Schema.getTableAliasMap();
        this.colAliasMap = Schema.getColAliasMap();
        this.tuple = tuple;
    }

    /**
     * Used by SelectOperator
     */
    public Tuple() {
        String tableName = "*";
        this.tableName = tableName;
        this.indexHash.put(tableName, new LinkedHashMap<>());
        this.colAliasMap = Schema.getColAliasMap();
        this.tableAliasMap = Schema.getTableAliasMap();
    }

    public void addColumn(String colName, PrimitiveValue value) {
        String tableName = "*";
        this.indexHash.get(tableName).put(colName, this.tuple.size());
        this.tuple.add(value);
    }

    public void addAllColumn(String tableName, Tuple tuple) {
//        tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
        HashMap<String, Integer> index = tuple.getIndexHash().get(tableName);
        List<PrimitiveValue> line = tuple.getTuple();
        for (String colName : tuple.getIndexHash().get(tableName).keySet()) {
            int i = index.get(colName);
            addColumn(colName, line.get(i));
        }
    }

    public void addTable(Tuple tuple) {
        for (String tableName : tuple.getIndexHash().keySet()) {
//            tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
            LinkedHashMap<String, Integer> index = tuple.getIndexHash().get(tableName);
            List<PrimitiveValue> line = tuple.getTuple();
//            int size = this.indexHash.get(this.tableName).size();
//            index.replaceAll((k, v) -> v += size);
//            this.indexHash.put(tableName, index);
//            this.tuple.addAll(line);

            this.indexHash.put(tableName, new LinkedHashMap<>());
            for (String colName : tuple.getIndexHash().get(tableName).keySet()) {
                this.indexHash.get(tableName).put(colName, this.tuple.size());
                int i = index.get(colName);
                this.tuple.add(line.get(i));
            }
        }
    }

    public void setTableAliasMap(String alias, String origin) {
        this.tableAliasMap.put(alias, origin);
    }

    public List<String> getColList(String tableName) {
        return new ArrayList<>(this.indexHash.get(tableName).keySet());
    }

    public void rename(String tableName) {
        //combine all items in hash map in one line with key value is name
        assert this.indexHash.size() == 1;
        String oldName = "";
        for (String o : this.indexHash.keySet()) {
            oldName = o;
        }
        this.indexHash.put(tableName, this.indexHash.get(oldName));
        this.indexHash.remove(oldName);
    }

    public PrimitiveValue getItem(String tableName, String colName) throws SQLException {
        this.eval.init(this);
        if (colAliasMap.containsKey(colName)) {
            Expression exp = colAliasMap.get(colName);
            return eval.eval(exp);
        }

//        String realName = this.tableAliasMap.getOrDefault(tableName, tableName);
        int i = this.indexHash.get(tableName).get(colName);
        return tuple.get(i);
    }

    public List<PrimitiveValue> getTuple() {
        return tuple;
    }

    public HashMap<String, LinkedHashMap<String, Integer>> getIndexHash() {
        return indexHash;
    }
}
