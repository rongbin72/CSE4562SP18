package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.util.HashMap;
import java.util.List;

public class Tuple {
	private List<PrimitiveValue> tuple;
	private String tableName;
	private HashMap<String, Integer> indexHash;
	
	public Tuple(String tableName, List<PrimitiveValue> tuple) {
		this.tuple = tuple;
		this.tableName = tableName;
		indexHash = Schema.getIndxHash(tableName);
	}
	
	public void combineTuples(Tuple extend) {
		
	}
	
	public Tuple subTuple(String table) {
		return null; //subtuple with tablename 'table'
	}
	
	public void addCol(PrimitiveValue p) {
		
	}
	
	public List<PrimitiveValue> getTuple() {
		return tuple;
	}

	public String getTableName() {
		return tableName;
	}

	public HashMap<String, Integer> getIndexHash() {
		return indexHash;
	}
}
