package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Column;

import java.sql.SQLException;
import java.util.*;

public class Tuple {
    private List<PrimitiveValue> tuple = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> indexHash = new HashMap<>();
    private HashMap<String, String> tableAliasMap;
    private HashMap<String, Expression> colAliasMap;
    private List<Column> groups = new ArrayList<>();

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

    /**
     * Used by Read to init tuple
     * @param tableName table name
     * @param tuple tuple
     */
    public Tuple(String tableName, List<PrimitiveValue> tuple) {
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
        this.indexHash.put(tableName, new HashMap<>());
        this.colAliasMap = Schema.getColAliasMap();
        this.tableAliasMap = Schema.getTableAliasMap();
    }

//    public Tuple(String tableName, List<PrimitiveValue> tuple, HashMap<String, Integer> indexHash) {
//        this.tuple = tuple;
//        this.indexHash.put(tableName, indexHash);
//        this.colAliasMap = Schema.getColAliasMap();
//        this.tableAliasMap = Schema.getTableAliasMap();
//    }

    public void addColumn(String colName, PrimitiveValue value) {
        String tableName = "*";
        this.indexHash.get(tableName).put(colName, this.tuple.size());
        this.tuple.add(value);
    }

    public void addAllColumn(String tableName, Tuple tuple) {
//        tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
        HashMap<String, Integer> index = tuple.getIndexHash().get(tableName);
        List<PrimitiveValue> line = tuple.getTuple();
        List<String> colList = tuple.getColList(tableName);
        for (String colName : colList) {
            int i = index.get(colName);
            addColumn(colName, line.get(i));
        }
    }

    public void addTable(Tuple tuple) {
        for (String tableName : tuple.getIndexHash().keySet()) {
//            tableName = this.tableAliasMap.getOrDefault(tableName, tableName);
            List<String> colList = tuple.getColList(tableName);
            HashMap<String, Integer> index = tuple.getIndexHash().get(tableName);
            List<PrimitiveValue> line = tuple.getTuple();
            this.indexHash.put(tableName, new HashMap<>());
            for (String colName : colList) {
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
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(this.indexHash.get(tableName).entrySet());
        entryList.sort(Comparator.comparing(Map.Entry::getValue));
        List<String> colList = new ArrayList<>();
        for (Map.Entry<String, Integer> kv : entryList) {
            colList.add(kv.getKey());
        }
        return colList;
    }

//	public void mergeTable(Tuple merge) {
//		List<PrimitiveValue> tuple = this.tuple.get(this.tableName);
//		HashMap<String, Integer> indexHash = this.indexHash.get(this.tableName);
//		for (String key : merge.getTuple().keySet()) {
//			int size = tuple.size();
//			tuple.addAll(merge.getTuple().get(key));
//			merge.getIndexHash().get(key).forEach((k, v) -> indexHash.put(k, v + size));
//		}
//	}

//    	public Tuple subTuple(String tableName) {
//		String origin = Schema.getTableAlias(tableName);
//		origin = origin == null ? tableName:origin;
////	    Tuple a =  new Tuple(tableName, this.tuple.get(origin), this.indexHash.get(origin));
//		return new Tuple(tableName, this.tuple.get(origin), this.indexHash.get(origin));
//	}
//
//	public void addCol(PrimitiveValue p, String colName) {
//		this.tuple.get(this.tableName).add(p);
//		indexHash.get(tableName).put(colName, indexHash.get(this.tableName).size());
//	}
//
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
        Evaluation eval = new Evaluation(this);
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

    public HashMap<String, HashMap<String, Integer>> getIndexHash() {
        return indexHash;
    }
}
