package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;

	public WhereObject(Expression where) {
		this.where = where;
	}

	public boolean Result(HashMap<String, List<PrimitiveValue>> tuple) throws SQLException {
		if(this.where == null) {
			return true;
		}
		Evaluation eval = new Evaluation(tuple);
		PrimitiveValue result = eval.eval(this.where);
		return result.toBool();
	}
}
