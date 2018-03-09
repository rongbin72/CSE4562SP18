package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private String tableName;

	public WhereObject(Expression where) {
		this.where = where;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean Result(List<PrimitiveValue> tuple) throws SQLException {
		Evaluation eval = new Evaluation(this.tableName, tuple);
		PrimitiveValue result = eval.eval(this.where);
		return result.toBool();
	}
}
