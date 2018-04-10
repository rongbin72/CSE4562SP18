package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tuple {
    // TODO can use HashMap to Store tuple => {tableName: {colName: data}}
    private List<PrimitiveValue> tuple = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> indexHash = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> tableAliasMap = new HashMap<>();
    private HashMap<String, Expression> colAliasMap = new HashMap<>();

    public Tuple(String tableName, List<PrimitiveValue> tuple) {
        indexHash.put(tableName, Schema.getIndexHash(tableName));
        this.colAliasMap = Schema.getColAliasMap();
        this.tuple = tuple;
    }

    public Tuple() {
        String tableName = "*";
        this.indexHash.put(tableName, new HashMap<>());
    }

    public Tuple(String tableName, List<PrimitiveValue> tuple, HashMap<String, Integer> indexHash) {
        this.tuple = tuple;
        this.indexHash.put(tableName, indexHash);
        this.colAliasMap = Schema.getColAliasMap();
    }

    public void addColumn(String tableName, String colName, PrimitiveValue value) {
        this.tuple.add(value);
        this.indexHash.get(tableName).put(colName, this.tuple.size());
    }

    public void setTableAliasMap(String alias, String origin) {
        this.tableAliasMap.put(alias, this.indexHash.get(origin));
    }

    //	public void addTable(Tuple extend) {
//		this.tuple.putAll(extend.getTuple());
//		this.indexHash.putAll(extend.getIndexHash());
//	}
//
//
//	public void mergeTable(Tuple merge) {
//		List<PrimitiveValue> tuple = this.tuple.get(this.tableName);
//		HashMap<String, Integer> indexHash = this.indexHash.get(this.tableName);
//		for (String key : merge.getTuple().keySet()) {
//			int size = tuple.size();
//			tuple.addAll(merge.getTuple().get(key));
//			merge.getIndexHash().get(key).forEach((k, v) -> indexHash.put(k, v + size));
//		}
//	}

    //	public Tuple subTuple(String tableName) {
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
        assert this.tuple.size() == 1;
        String oldName = "";
        for (String n : this.indexHash.keySet()) {
            oldName = n;
        }
        this.tableAliasMap.put(tableName, this.indexHash.get(oldName));
    }

    public PrimitiveValue getItem(String tableName, String colName) throws SQLException {
        Evaluation eval = new Evaluation(this);
        if (colAliasMap.containsKey(colName)) {
            Expression exp = colAliasMap.get(colName);
            return eval.eval(exp);
        } else if (this.tableAliasMap.containsKey(tableName)) {
            int i = this.tableAliasMap.get(tableName).get(colName);
            return tuple.get(i);
        } else {
            int i = this.indexHash.get(tableName).get(colName);
            return tuple.get(i);
        }
    }

    public List<PrimitiveValue> getTuple() {
        return tuple;
    }

    public HashMap<String, HashMap<String, Integer>> getIndexHash() {
        return indexHash;
    }
}
