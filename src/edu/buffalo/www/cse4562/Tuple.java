package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tuple {
    private List<PrimitiveValue> tuple = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> indexHash = new HashMap<>();
//    private HashMap<String, String> tableAliasMap;
//    private HashMap<String, Expression> colAliasMap;
    private List<Column> groups = new ArrayList<>();
//    private Evaluation eval = new Evaluation();
    private String tableName;

    // TODO add a construction method, only has tuple, do not initialize other variables
    /**
     * Used by SelectOperator
     */
    public Tuple() {
        String tableName = "*";
        this.tableName = tableName;
        this.indexHash.put(tableName, new HashMap<>());
//        this.colAliasMap = Schema.getColAliasMap();
//        this.tableAliasMap = Schema.getTableAliasMap();
    }

//    private HashMap<String, String> getTableAliasMap() {
//        return tableAliasMap;
//    }
//
//    public void setTableAliasMap(HashMap<String, String> tableAliasMap) {
//        this.tableAliasMap = tableAliasMap;
//    }
//
//    private HashMap<String, Expression> getColAliasMap() {
//        return colAliasMap;
//    }
//
//    void setColAliasMap(HashMap<String, Expression> colAliasMap) {
//        this.colAliasMap = colAliasMap;
//    }

    private String getTableName() {
        return tableName;
    }

    public void setTuple(List<PrimitiveValue> tuple) {
        this.tuple = tuple;
    }

    boolean haveGroups() {
        return this.groups != null;
    }

    List<Column> getGroups() {
        return groups;
    }

    void setGroups(List<Column> groups) {
        this.groups = groups;
    }

    public Tuple(Tuple tuple) {
        this.tableName = tuple.getTableName();
        this.indexHash = tuple.getIndexHash();
//        this.tableAliasMap = tuple.getTableAliasMap();
//        this.colAliasMap = tuple.getColAliasMap();
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
//        this.tableAliasMap = Schema.getTableAliasMap();
//        this.colAliasMap = Schema.getColAliasMap();
        this.tuple = tuple;
    }

    void addAllColumn(String tableName, Tuple tuple) {
//        tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
        HashMap<String, Integer> index = new HashMap<>(tuple.getIndexHash().get(tableName));
        List<PrimitiveValue> line = tuple.getTuple();
        int size = this.tuple.size();
        index.replaceAll((k, v) -> v += size);
        this.indexHash.put("*", index);
        this.tuple.addAll(line);
    }

    void addColumn(String colName, PrimitiveValue value) {
        String tableName = "*";
        this.indexHash.get(tableName).put(colName, this.tuple.size());
        this.tuple.add(value);
    }

    void addTable(Tuple tuple) {
        for (String tableName : tuple.getIndexHash().keySet()) {
//            tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
            HashMap<String, Integer> index = new HashMap<>(tuple.getIndexHash().get(tableName));
            List<PrimitiveValue> line = tuple.getTuple();
            int size = this.tuple.size();
            index.replaceAll((k, v) -> v += size);
            this.indexHash.put(tableName, index);
            this.tuple.addAll(line);
        }
    }

    HashMap<String, HashMap<String, Integer>> getIndexHash() {
        return indexHash;
    }

    void rename(String tableName) {
        //combine all items in hash map in one line with key value is name
        assert this.indexHash.size() == 1;
        String oldName = "";
        for (String o : this.indexHash.keySet()) {
            oldName = o;
        }
        this.indexHash.put(tableName, this.indexHash.get(oldName));
        this.indexHash.remove(oldName);
    }

    PrimitiveValue getItem(String tableName, String colName) {
//        if (colAliasMap.containsKey(colName)) {
//            this.eval.init(this);
//            Expression exp = colAliasMap.get(colName);
//            return eval.eval(exp);
//        }

//        String realName = this.tableAliasMap.getOrDefault(tableName, tableName);
        int i = this.indexHash.get(tableName).get(colName);
        return tuple.get(i);
    }

    public List<PrimitiveValue> getTuple() {
        return tuple;
    }

//    public void setIndexHash(HashMap<String, HashMap<String, Integer>> indexHash) {
//        this.indexHash = indexHash;
//    }
}
