package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;

public class WhereObject {
	private Expression where;
	private Schema schema;
	
	public WhereObject(Expression where, Schema schema) {
		this.where = where;
		this.schema = schema;
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public boolean Result(List<String> tuple) throws SQLException{
	    Evaluation eval = new Evaluation(this.schema, tuple);
	    PrimitiveValue result = eval.eval(this.where);
	    if(result.toString().equals("TRUE")) {
	        return true;
        } else {
	        return false;
        }
	}
}
