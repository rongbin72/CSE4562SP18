package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tuple {
	private HashMap<String, List<PrimitiveValue>> tuple = new HashMap<>();
	private String tableName;
	private HashMap<String, HashMap<String, Integer>> indexHash = new HashMap<>();
	
	public Tuple(String tableName, List<PrimitiveValue> tuple) {
		this.tableName = tableName;
		indexHash.put(this.tableName, Schema.getIndxHash(this.tableName));
		HashMap<String, List<PrimitiveValue>> tmp = new HashMap<>();
		tmp.put(tableName, tuple);
		this.tuple = tmp;
	}

	public Tuple() {
		this.tableName = "*";
		this.tuple.put(this.tableName, new ArrayList<>());
		this.indexHash.put(this.tableName, new HashMap<>());
	}

	public Tuple(String tableName, List<PrimitiveValue> tuple, HashMap<String, Integer> indexHash) {
		this.tableName = tableName;
		this.tuple.put(tableName, tuple);
		this.indexHash.put(tableName, indexHash);
	}

	public void addTable(Tuple extend) {
		this.tuple.putAll(extend.getTuple());
		this.indexHash.putAll(extend.getIndexHash());
	}
	
	public Tuple subTuple(String tableName) {
		return new Tuple(tableName, this.tuple.get(tableName), this.indexHash.get(tableName));
	}
	
	public void addCol(PrimitiveValue p, String colName) {
		this.tuple.get(this.tableName).add(p);
		indexHash.get(tableName).put(colName, indexHash.get(this.tableName).size());
	}
	
	public void rename(String tableName) {
		//combine all items in hash map in one line with key value is name
		assert this.tuple.size() == 1;
		this.tuple.put(tableName, this.tuple.get(this.tableName));
		this.indexHash.put(tableName, this.indexHash.get(this.tableName));
		this.tuple.remove(this.tableName);
		this.indexHash.remove(this.tableName);
		this.tableName = tableName;
	}

	public PrimitiveValue getItem(String tableName, String colName) throws SQLException {
		Evaluation eval = new Evaluation(this);
		if (Schema.getTableAlias(tableName) != null) {
			tableName = Schema.getTableAlias(tableName);
		}
		if (Schema.getColAlias(colName) != null) {
			Expression col = Schema.getColAlias(colName);
			return eval.eval(col);
		}

		int index = indexHash.get(tableName).get(colName);
		return this.tuple.get(tableName).get(index);
	}

	public HashMap<String, List<PrimitiveValue>> getTuple() {
		return tuple;
	}

	public String getTableName() {
		return tableName;
	}

	public HashMap<String, HashMap<String, Integer>> getIndexHash() {
		return indexHash;
	}
}
