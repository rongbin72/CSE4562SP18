package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.ArrayList;
import java.util.List;

public class Optimizer implements ExpressionVisitor{

	private List<BinaryExpression> filter = new ArrayList<>();
	private Operator tree;
	private Expression or;
	private Expression exp;

	public Optimizer(Expression exp, Operator tree) {
		if (exp != null) {
			this.exp = exp;
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
			CrossProductOP self = (CrossProductOP)tree;
			boolean left = this.containTables(self.getSon(), t1, t2);
			boolean right = this.containTables(self.getRhson(), t1, t2);
			if(left || right) {
				if(left) {
					//left must be a crossproduct and contains the tables
					CrossProductOP leftson = (CrossProductOP) self.getSon();
					Operator leftSonofLeft = leftson.getSon();
					Operator rightSonofLeft = leftson.getRhson();
					if(this.containTables(leftSonofLeft, t1, t2) || 
							this.containTables(rightSonofLeft, t1, t2)) {
						//continue push down
					}
					else {
						//stop
						if(self.getSon() instanceof JoinOperator) {
							JoinOperator join = (JoinOperator)self.getSon();
							join.addCondition(exp);
							self.setSon(join);
						}
						else {
							JoinOperator join = new JoinOperator(leftSonofLeft,rightSonofLeft,exp);
							self.setSon(join);//////////////////
						}
					}
					
				}
				if(right) {
					//right must be a crossproduct and contains the tables
					CrossProductOP rightson = (CrossProductOP) self.getRhson();
					Operator leftSonofRight = rightson.getSon();
					Operator rightSonofRight = rightson.getRhson();
					if(this.containTables(leftSonofRight, t1, t2) || 
							this.containTables(rightSonofRight, t1, t2)) {
						//continue push down
					}
					else {
						//stop
						if(self.getSon() instanceof JoinOperator) {
							JoinOperator join = (JoinOperator)self.getSon();
							join.addCondition(exp);
						}
						else {
							JoinOperator join = new JoinOperator(leftSonofRight,rightSonofRight,exp);
							self.setRhS(join);
						}
					}
				}
				
			}
			else {
				//do nothing
				System.out.println("there must be something wrong");
			}
			
		}
		else {
			if(!(tree instanceof Read) && !(tree instanceof RenameOperator)) {
				if(tree.getSon() instanceof CrossProductOP) {
					CrossProductOP cross = (CrossProductOP) tree.getSon();
					//try one step
					Operator lhsOfson = cross.getSon();
					Operator rhsOfson = cross.getRhson();
					boolean left = this.containTables(lhsOfson, t1, t2);
					boolean right = this.containTables(rhsOfson, t1, t2);
					if(left || right) {
						//move one step
						this.twoConditions(tree.getSon(), exp, t1, t2);
					}
					else if(this.containTables(cross, t1, t2)) {
						if(tree.getSon() instanceof JoinOperator) {
							JoinOperator join = (JoinOperator)cross;
							join.addCondition(exp);
							
						}
						else {
							//must be crossproduct
							JoinOperator join = new JoinOperator(cross.getSon(),cross.getRhson(),exp);
							tree.setSon(join);
						}
					}
				}
				else {
					this.twoConditions(tree.getSon(), exp, t1, t2);
				}
			}
		}
	}
	

	private void oneCondition(Operator tree, BinaryExpression exp, String tablename) {
		if(tree instanceof CrossProductOP) {
			CrossProductOP cross = (CrossProductOP) tree;
			if(cross.getSon() instanceof Read) {
				Read read = (Read)cross.getSon();
				if(tablename.equals(read.getTableName())) {
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
			else if(cross.getSon() instanceof CrossProductOP) {
				this.oneCondition(cross.getSon(), exp, tablename);
			}
			
			if(cross.getRhson() instanceof Read) {
				Read read = (Read)cross.getRhson();
				if(tablename.equals(read.getTableName())) {
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
			else if(cross.getRhson() instanceof CrossProductOP) { 
				this.oneCondition(cross.getRhson(), exp, tablename);
			}
		}
		else {
			while(!(tree.getSon() instanceof Read) && !(tree.getSon() instanceof RenameOperator) &&
					!(tree.getSon() instanceof CrossProductOP)) {
				tree = tree.getSon();
				
			}
			if(tree.getSon() instanceof Read) {
				Read read = (Read)tree.getSon();
				if(tablename.equals(read.getTableName())) {
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
	
	private void pushOr(Operator tree) {
		//must have where
		if(tree instanceof CrossProductOP) {
			if(tree.getSon() instanceof Read) {
				Read read = (Read) tree.getSon();
				if(read.getTableName().equals("LINEITEM")) {
					Operator where = new WhereOperator(read,this.or);
					tree.setSon(where);
				}
			}
			else if(!(tree.getSon() instanceof RenameOperator)){
				this.pushOr(tree.getSon());
			}
			if(((CrossProductOP) tree).getRhson() instanceof Read) {
				Read read = (Read) ((CrossProductOP) tree).getRhson();
				if(read.getTableName().equals("LINEITEM")) {
					Operator where = new WhereOperator(read,this.or);
					((CrossProductOP) tree).setRhS(where);
				}
			}
			else if(!(((CrossProductOP) tree).getRhson() instanceof RenameOperator)) {
				this.pushOr(((CrossProductOP) tree).getRhson());
			}
		}
		else {
			if(tree.getSon() instanceof Read) {
				Read read = (Read) tree.getSon();
				if(read.getTableName().equals("LINEITEM")) {
					Operator where = new WhereOperator(read,this.or);
					tree.setSon(where);
				}
			}
			else if(!(tree.getSon() instanceof RenameOperator)){
				this.pushOr(tree.getSon());
			}
		}
	}
	
	private void cutWhere(Operator tree) {
		//tree is parent of where
		if(!(tree.getSon() instanceof WhereOperator)) {
			this.cutWhere(tree.getSon());
		}
		else {
			Operator where = tree.getSon();
			if(!(where.getSon() instanceof Read) && !(where.getSon() instanceof RenameOperator)) {
				tree.setSon(where.getSon());
				if(this.or != null) {
					this.pushOr(this.tree);
				}
			}
		}
		
	}
	
	
	public Operator resultTree() {
		this.searchTree(this.tree);
		for(BinaryExpression exp:this.filter) {
			if(exp.getLeftExpression() instanceof Column) {
				Column lhs = (Column)exp.getLeftExpression();
				if(lhs.getTable().getName() == null) {
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
		if(this.exp != null) {
			this.cutWhere(this.tree);
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
		this.or = arg0;
		
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
