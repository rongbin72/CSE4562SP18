package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private HashMap<String, Integer> colIndex;

	public WhereObject(Expression where) {
		this.where = where;
	}
	
	public void setIndex(HashMap<String, Integer> index) {
		this.colIndex = index;
	}

	public boolean Result(List<PrimitiveValue> tuple) throws SQLException {
		Evaluation eval = new Evaluation(this.colIndex, tuple);
		PrimitiveValue result = eval.eval(this.where);
		return result.toBool();
	}
}
