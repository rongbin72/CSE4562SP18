package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private String tablename;

	public WhereObject(Expression where) {
		this.where = where;
	}

	public void setTable(String newTable) {
		this.tablename = newTable;
	}

	public boolean Result(List<PrimitiveValue> tuple) throws SQLException {
		Evaluation eval = new Evaluation(this.tablename, tuple);
		PrimitiveValue result = eval.eval(this.where);
		return result.toBool();
	}
}
