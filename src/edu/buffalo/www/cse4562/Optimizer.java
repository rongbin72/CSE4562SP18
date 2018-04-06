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

	private List<List<List<PrimitiveValue>>> tables;
	private List<String> tablenames;
	private List<Expression> filter = new ArrayList<Expression>();
	private List<String> filtedTables = new ArrayList<>();

	public Optimizer(Expression exp, List<List<List<PrimitiveValue>>> tables, List<String> tablenames) {
		this.tables = tables;
		this.tablenames = tablenames;
		if (exp != null) {
            exp.accept(this);
		}
	}

	private void binaryFilter(BinaryExpression exp) {
		Expression eL = exp.getLeftExpression();
		Expression eR = exp.getRightExpression();
		if(eL instanceof Column) {
			if(!(eR instanceof Column)) {
				String name  = ((Column) eL).getTable().getName();
				// if there is only one table, use tablenames.get(0)
				assert tablenames.size() == 1 ^ name != null;
				name = tablenames.size() == 1 ? tablenames.get(0) : name;
                this.filter.add(exp);
                this.filtedTables.add(name);
			}
		}
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
		binaryFilter(equ);
	}

	@Override
	public void visit(GreaterThan gt) {
    	binaryFilter(gt);
	}

	@Override
	public void visit(GreaterThanEquals gte) {
	    binaryFilter(gte);
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
	    binaryFilter(mt);
	}

	@Override
	public void visit(MinorThanEquals mte) {
	    binaryFilter(mte);
	}

	@Override
	public void visit(NotEqualsTo nequ) {
	    binaryFilter(nequ);
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
