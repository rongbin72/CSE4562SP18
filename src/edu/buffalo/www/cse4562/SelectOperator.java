package edu.buffalo.www.cse4562;

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
		this.son.result();
		for(SelectItem item:this.items) {
			item.accept(this);
		}
		return this.resultTuple;
	}

	@Override
	public void visit(AllColumns allCol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllTableColumns allTableCol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SelectExpressionItem exp) {
		// TODO Auto-generated method stub
		
	}

}
