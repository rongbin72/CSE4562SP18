package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;
import java.util.List;

public class SelectOperator extends Operator implements SelectItemVisitor{

	private Operator son;
	private List<SelectItem> items;
	private List<Expression> selectExps;
	private Tuple resultTuple;
	private Tuple resultofSon;
	private Evaluation eval;
	
	public SelectOperator(Operator son, List<SelectItem> items) {
		this.son = son;
		this.items = items;
		for (SelectItem item : this.items) {
            if(item instanceof SelectExpressionItem) {
            	SelectExpressionItem exp = (SelectExpressionItem) item;
            	String alias = exp.getAlias();
            	if(alias != null) {
            		Schema.addcolAlias(alias, exp.getExpression());//add alias to alias map in schema
            	}
            }
        }
	}

	@Override
	public Tuple result() {
		this.resultTuple = new Tuple();
		this.resultofSon = this.son.result();
		if(this.resultofSon == null) {
			return null;
		}
		this.eval = new Evaluation(this.resultofSon);
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		return this.resultTuple;
	}

	@Override
	public void visit(AllColumns allCol) {
		this.resultTuple.mergeTable(resultofSon);
	}

	@Override
	public void visit(AllTableColumns allTableCol) {
		String table = allTableCol.getTable().getName();
		this.resultTuple.mergeTable(this.resultofSon.subTuple(table));
	}

	@Override
	public void visit(SelectExpressionItem exp) {
		Expression e = exp.getExpression();
		try {
			String colName = e.toString();
			String alias = exp.getAlias();
			if (alias != null) {
				colName = alias;
			}
			PrimitiveValue a = this.eval.eval(e);
			this.resultTuple.addCol(a, colName);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

}
