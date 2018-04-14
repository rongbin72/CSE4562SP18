package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Optimizer implements ExpressionVisitor{

	private List<BinaryExpression> filter = new ArrayList<>();
	private Operator tree;

	public Optimizer(Expression exp, Operator tree) {
		if (exp != null) {
            exp.accept(this);
		}
		this.tree = tree;
		
	}
	
	public void searchTree(Operator tree) {
		if(!(tree instanceof Read) && !(tree instanceof RenameOperator) 
				&& !(tree instanceof CrossProductOP) && !(tree instanceof JoinOperator)) {
			//is select, where, group by etc.
			this.searchTree(tree.getSon());
		}
		if(tree instanceof CrossProductOP) {//or join
			CrossProductOP cop = (CrossProductOP) tree;
			cop.relatedT();//search the after tree by crossproduct itself
		}
	}
	
	private boolean containTables(Operator op,String t1,String t2) {
		if(op instanceof CrossProductOP) {
			CrossProductOP cross = (CrossProductOP)op;
			if(cross.relatedGetter().contains(t1) && cross.relatedGetter().contains(t2)) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	
	private void twoConditions(Operator tree, BinaryExpression exp, String t1, String t2) {
		if(tree instanceof CrossProductOP) {
			CrossProductOP cross = (CrossProductOP)tree;
			if(this.containTables(cross, t1, t2)) {
				if(!this.containTables(cross.getSon(), t1, t2) &&
						!this.containTables(cross.getRhson(), t1, t2)) {
					//lsh and rhs are all not include these tables, then add condition
					if(tree instanceof JoinOperator) {
						JoinOperator join = (JoinOperator)tree;
						join.addCondition(exp);
					}
					else {
						JoinOperator j = new JoinOperator(cross.getSon(),cross.getRhson(),exp);
						tree = j;
					}
				}
				else {
					if(this.containTables(cross.getSon(), t1, t2)) {
						this.twoConditions(cross.getSon(),exp , t1, t2);
					}
					if(this.containTables(cross.getRhson(), t1, t2)) {
						this.twoConditions(cross.getRhson(),exp , t1, t2);
					}
				}
			}
		}
		else {
			this.twoConditions(tree.getSon(), exp, t1, t2);
		}
	}

	private void oneCondition(Operator tree, BinaryExpression exp, String tablename) {
		if(tree instanceof CrossProductOP) {
			CrossProductOP cross = (CrossProductOP) tree;
			if(cross.getSon() instanceof Read) {
				Read read = (Read)cross.getSon();
				if(tablename.equals(read.getTablename())) {
					WhereOperator where = new WhereOperator(read,exp);
					cross.setSon(where);
				}
			}
			else if(cross.getSon() instanceof RenameOperator) {
				RenameOperator rename = (RenameOperator)cross.getSon();
				if(tablename.equals(rename.nameGetter())) {
					WhereOperator where = new WhereOperator(rename,exp);
					cross.setSon(where);
				}
			}
			else if(cross.getRhson() instanceof Read) {
				Read read = (Read)cross.getRhson();
				if(tablename.equals(read.getTablename())) {
					WhereOperator where = new WhereOperator(read,exp);
					cross.setRhS(where);
				}
			}
			else if(cross.getRhson() instanceof RenameOperator) {
				RenameOperator rename = (RenameOperator)cross.getRhson();
				if(tablename.equals(rename.nameGetter())) {
					WhereOperator where = new WhereOperator(rename,exp);
					cross.setRhS(where);
				}
			}
			else { 
				if((cross.getSon() instanceof JoinOperator) ||
				(cross.getSon() instanceof CrossProductOP) ||
				(cross.getRhson() instanceof JoinOperator) ||
				(cross.getRhson() instanceof CrossProductOP)) {
					this.oneCondition(cross, exp, tablename);
				}
			}
		}
		else {
			while(!(tree.getSon() instanceof Read) && !(tree.getSon() instanceof RenameOperator) &&
					!(tree.getSon() instanceof CrossProductOP)) {
				tree = tree.getSon();
				
			}
			if(tree.getSon() instanceof Read) {
				Read read = (Read)tree.getSon();
				if(tablename.equals(read.getTablename())) {
					WhereOperator where = new WhereOperator(read,exp);
					tree.setSon(where);
				}
			}
			else if(tree.getSon() instanceof RenameOperator) {
				RenameOperator rename = (RenameOperator)tree.getSon();
				if(tablename.equals(rename.nameGetter())) {
					WhereOperator where = new WhereOperator(rename,exp);
					tree.setSon(where);
				}
			}
			else if(tree.getSon() instanceof CrossProductOP) {
				CrossProductOP cross = (CrossProductOP)tree.getSon();
				this.oneCondition(cross, exp, tablename);
			}
		}
	}
	
	
	public Operator resultTree() {
		for(BinaryExpression exp:this.filter) {
			if(exp.getLeftExpression() instanceof Column) {
				Column lhs = (Column)exp.getLeftExpression();
				if(lhs.getTable() == null) {
					continue;
				}
				else {
					if(exp.getRightExpression() instanceof Column) {
						Column rhs = (Column)exp.getRightExpression();
						if(rhs.getTable() == null) {
							continue;
						}
						else {
							String t1 = lhs.getTable().getName();
							String t2 = rhs.getTable().getName();
							this.twoConditions(this.tree, exp, t1, t2);
						}
					}
					else {
						String t1 = lhs.getTable().getName();
						this.oneCondition(this.tree, exp, t1);
					}
				}
			}
		}
		return this.tree;
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
	public void visit(EqualsTo equ) {
		this.filter.add(equ);
	}

	@Override
	public void visit(GreaterThan gt) {
    	this.filter.add(gt);
	}

	@Override
	public void visit(GreaterThanEquals gte) {
	    this.filter.add(gte);
	}

	@Override
	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub

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
	public void visit(MinorThan mt) {
	    this.filter.add(mt);
	}

	@Override
	public void visit(MinorThanEquals mte) {
	    this.filter.add(mte);
	}

	@Override
	public void visit(NotEqualsTo nequ) {
	    this.filter.add(nequ);
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
