package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private Schema schema;
	private String tableName;

	public WhereObject(Expression where, Schema schema) {
		this.where = where;
		this.schema = schema;
	}

	public void setTable(String newTable) {
		this.tableName = newTable;
	}

	public boolean Result(List<PrimitiveValue> tuple) throws SQLException {
		Evaluation eval = new Evaluation(this.schema, this.tableName, tuple);
		PrimitiveValue result = eval.eval(this.where);
		return result.toBool();
	}
}
