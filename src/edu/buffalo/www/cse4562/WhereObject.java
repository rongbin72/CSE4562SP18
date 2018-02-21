package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.eval.*;

public class WhereObject {
	private Expression where;
	private Schema schema;
	
	public WhereObject(Expression where, Schema schema) {
		this.where = where;
		this.schema = schema;
	}

	public boolean Result(List<String> tuple) throws SQLException{
	    Evaluation eval = new Evaluation(this.schema, tuple);
	    PrimitiveValue result = eval.eval(this.where);
	    if(result.toString().equals("true")) {
	        return true;
        } else {
	        return false;
        }
	}
}
