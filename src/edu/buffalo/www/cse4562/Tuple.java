package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;

import java.util.HashMap;
import java.util.List;

public class Tuple {
	private HashMap<String, List<PrimitiveValue>> tuple;
	private String tableName;
	private HashMap<String, Integer> indexHash;
	
	public Tuple(String tableName, List<PrimitiveValue> tuple) {
		this.tableName = tableName;
		this.indexHash = Schema.getIndxHash(tableName);
		HashMap<String, List<PrimitiveValue>> tmp = new HashMap<>();
		tmp.put(tableName, tuple);
		this.tuple = tmp;
	}
	
	public void combineTuples(Tuple extend) {
		
	}
	
	public Tuple subTuple(String table) {
		return null; //subtuple with tablename 'table'
	}
	
	public void addCol(PrimitiveValue p) {
		
	}

	public PrimitiveValue getItem(String tableName, String colName) {
		int index = indexHash.get(tableName);
		return tuple.get(tableName).get(index);
	}

	public HashMap<String, List<PrimitiveValue>> getTuple() {
		return tuple;
	}

	public String getTableName() {
		return tableName;
	}

	public HashMap<String, Integer> getIndexHash() {
		return indexHash;
	}
}
