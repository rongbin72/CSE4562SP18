package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

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
		this.resultTuple.combineTuples(resultofSon);
	}

	@Override
	public void visit(AllTableColumns allTableCol) {
		String table = allTableCol.getTable().getName();
		this.resultTuple.combineTuples(this.resultofSon.subTuple(table));
	}

	@Override
	public void visit(SelectExpressionItem exp) {
		Expression e = exp.getExpression();
		try {
			this.resultTuple.addCol(this.eval.eval(e));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

}
