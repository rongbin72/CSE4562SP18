package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private Schema schema;
	private String tablename;
	
	public WhereObject(Expression where, Schema schema) {
		this.where = where;
		this.schema = schema;
	}
	
	public void setTable(String newTable) {
		this.tablename = newTable;
	}

	public boolean Result(List<String> tuple) throws SQLException{
	    Evaluation eval = new Evaluation(this.schema,this.tablename, tuple);
	    PrimitiveValue result = eval.eval(this.where);
	    if(result.toString().equals("TRUE")) {
	        return true;
        } else {
	        return false;
        }
	}
}
