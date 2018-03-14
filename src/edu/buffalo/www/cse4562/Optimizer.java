package edu.buffalo.www.cse4562;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class Optimizer implements ExpressionVisitor{
	
	private List<List<List<PrimitiveValue>>> tables;
	private List<String> tablenames;
	private List<Expression> filter = new ArrayList<Expression>();
	private List<String> filtedTables = new ArrayList<>();
	
	public Optimizer(Expression exp, List<List<List<PrimitiveValue>>> tables, List<String> tablenames) {
		this.tables = tables;
		this.tablenames = tablenames;
		if(exp!=null) {
			exp.accept(this);
		}
	}
	
	public List<List<List<PrimitiveValue>>> getOptimizedTable() throws SQLException {
		if(this.filter.size()!=0) {
			for(int i = 0;i < filter.size();i++) {
				String tablename = this.filtedTables.get(i);
				Expression e = this.filter.get(i);
				int index = this.tablenames.indexOf(tablename);
				for(int j = 0;j < this.tables.get(index).size();j++) {
					HashMap <String, List<PrimitiveValue>> h = new HashMap<String, List<PrimitiveValue>>();
					h.put(tablename, this.tables.get(index).get(j));
					Evaluation ev = new Evaluation(h);
					if(!ev.eval(e).toBool()) {
						this.tables.get(index).remove(j);
					}
				}
			}
		}
		return this.tables;
	}
	
	@Override
	public void visit(NullValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LongValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DateValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimeValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimestampValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BooleanValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Addition arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AndExpression andExp) {
		Expression eL = andExp.getLeftExpression();
		Expression eR = andExp.getRightExpression();
		eL.accept(this);
		eR.accept(this);
	}

	@Override
	public void visit(OrExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Between arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(EqualsTo ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(GreaterThan ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(GreaterThanEquals ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(InExpression ex) {
		Column c = (Column) ex.getLeftExpression();
		String name = c.getTable().getName();
		if(name != null) {
			this.filter.add(ex);
			this.filtedTables.add(name);
		}	
	}

	@Override
	public void visit(IsNullExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(MinorThan ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(MinorThanEquals ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(NotEqualsTo ex) {
		Expression eL = ex.getLeftExpression();
		Expression eR = ex.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				Column c = (Column) eL;
				String name = c.getTable().getName();
				if(name != null) {
					this.filter.add(ex);
					this.filtedTables.add(name);
				}			
			}
		}
		
	}

	@Override
	public void visit(Column arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}

}
