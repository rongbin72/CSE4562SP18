package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tuple {
    private List<PrimitiveValue> tuple = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> indexHash;
//    private HashMap<String, String> tableAliasMap;
//    private HashMap<String, Expression> colAliasMap;
    private List<Column> groups = new ArrayList<>();
//    private Evaluation eval = new Evaluation();
    private String tableName;
    private List<String> nameList = new ArrayList<>();
    private String id;

    public Tuple(Tuple tuple) {
        if (Schema.indexHash.size() != 0) {
            this.tableName = tuple.getTableName();
            this.id = tuple.getId();
            this.nameList = tuple.getNameList();
            this.tuple = new ArrayList<>(tuple.getTuple());
        } else {
            this.tableName = tuple.getTableName();
            this.indexHash = tuple.getIndexHash();
            this.tuple = new ArrayList<>(tuple.getTuple());
        }

    }

    /**
     * Used by Read to init tuple
     *
     * @param tableName table name
     * @param tuple     tuple
     */
    public Tuple(String tableName, List<PrimitiveValue> tuple) {
        if (tableName.equals("PLAYERS")) {
            this.tableName = Schema.indexHash.containsKey(tableName) ? tableName + "~" : tableName;
            this.id = this.tableName;
            Schema.indexHash.put(this.id, new HashMap<>());
            Schema.indexHash.get(this.id).put(this.tableName, Schema.getIndexHash(tableName));
            this.nameList.add(this.tableName);
            this.tuple = tuple;
        } else {
            this.tableName = tableName;
            this.indexHash = new HashMap<>();
            this.indexHash.put(tableName, Schema.getIndexHash(tableName));
            this.tuple = tuple;
        }

    }
    /**
     * Used by SelectOperator
     */
    public Tuple() {
        if (Schema.indexHash.size() != 0) {
            String tableName = "*";
            this.tableName = tableName;
            this.id = this.tableName;
            Schema.indexHash.put(tableName, new HashMap<>());
            Schema.indexHash.get(this.id).put(tableName, new HashMap<>());
        } else {
            String tableName = "*";
            this.tableName = tableName;
            this.indexHash = new HashMap<>();
            this.indexHash.put(tableName, new HashMap<>());
        }
    }

    public List<String> getNameList() {
        return nameList;
    }

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

    void addAllColumn(String tableName, Tuple tuple) {
//        tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
        HashMap<String, Integer> index = new HashMap<>(tuple.getIndexHash().get(tableName));
        List<PrimitiveValue> line = tuple.getTuple();
        int size = this.tuple.size();
        index.replaceAll((k, v) -> v += size);
        Schema.indexHash.get(this.id).put("*", index);
        this.tuple.addAll(line);
    }

    void addColumn(String colName, PrimitiveValue value) {
        if (Schema.indexHash.size() != 0) {
            Schema.indexHash.get(this.id).get(this.tableName).put(colName, this.tuple.size());
            this.tuple.add(value);
        } else {
            String tableName = "*";
            this.indexHash.get(tableName).put(colName, this.tuple.size());
            this.tuple.add(value);
        }
    }

    void addTable(Tuple tuple) {
        if (Schema.indexHash.size() != 0) {
            for (String tableName : tuple.getNameList()) {
//            tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
                HashMap<String, Integer> index = new HashMap<>(Schema.indexHash.get(tuple.getId()).get(tableName));
                List<PrimitiveValue> line = tuple.getTuple();
                int size = this.tuple.size();
                index.replaceAll((k, v) -> v += size);
                Schema.indexHash.get(this.id).put(tableName, index);
                this.tuple.addAll(line);
                this.nameList.add(tableName);
                HashMap<String, HashMap<String, Integer>> oldIndex = Schema.indexHash.get(this.id);
                String oldID = this.id;
                this.id = this.nameList.toString();
                Schema.indexHash.put(this.id, oldIndex);
                Schema.indexHash.remove(oldID);
            }
        } else {
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

    }

    HashMap<String, HashMap<String, Integer>> getIndexHash() {
        if (Schema.indexHash.size() != 0) {
            return Schema.indexHash.get(this.id);
        } else {
            return this.indexHash;
        }
    }

    void rename(String tableName) {
        if (Schema.indexHash.size() != 0) {
            if (!Schema.indexHash.get(this.id).containsKey(tableName)) {
                String oldName = this.tableName;
                if ("*".equals(oldName)) {  // subSelect
                    Schema.indexHash.put(tableName, new HashMap<>());
                    Schema.indexHash.get(tableName).put(tableName, Schema.indexHash.get(this.id).get(oldName));
                    this.id = tableName;
                    this.tableName = tableName;
                    this.nameList.add(tableName);
                } else {
                    Schema.indexHash.get(this.id).put(tableName, Schema.indexHash.get(this.id).get(oldName));
                    Schema.indexHash.get(this.id).remove(oldName);
                    this.nameList.clear();
                    this.nameList.add(tableName);
                }
            }
        } else {
            //combine all items in hash map in one line with key value is name
            assert this.indexHash.size() == 1;
            String oldName = "";
            for (String o : this.indexHash.keySet()) {
                oldName = o;
            }
            this.indexHash.put(tableName, this.indexHash.get(oldName));
            this.indexHash.remove(oldName);
        }

    }

    PrimitiveValue getItem(String tableName, String colName) {
//        if (colAliasMap.containsKey(colName)) {
//            this.eval.init(this);
//            Expression exp = colAliasMap.get(colName);
//            return eval.eval(exp);
//        }

//        String realName = this.tableAliasMap.getOrDefault(tableName, tableName);
        if (Schema.indexHash.size() != 0) {
            int i = Schema.indexHash.get(this.id).get(tableName).get(colName);
            return tuple.get(i);
        } else {
            int i = this.indexHash.get(tableName).get(colName);
            return tuple.get(i);
        }
    }

    public String getId() {
        return id;
    }

    public List<PrimitiveValue> getTuple() {
        return tuple;
    }
}
