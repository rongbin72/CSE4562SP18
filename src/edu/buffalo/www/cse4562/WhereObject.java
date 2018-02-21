package edu.buffalo.www.cse4562;

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

public class WhereObject implements ExpressionVisitor {
	private Expression where;
	private boolean bool;
	private List<String> tuple;
	
	public WhereObject(Expression where, List<String> tuple){
		this.where = where;
		this.tuple = tuple;
		this.where.accept(this);
	}

	public boolean Result() {
		return bool;
	}

	@Override
	public void visit(NullValue nullValue) {

	}

	@Override
	public void visit(Function function) {

	}

	@Override
	public void visit(InverseExpression inverseExpression) {

	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {

	}

	@Override
	public void visit(DoubleValue doubleValue) {

	}

	@Override
	public void visit(LongValue longValue) {

	}

	@Override
	public void visit(DateValue dateValue) {

	}

	@Override
	public void visit(TimeValue timeValue) {

	}

	@Override
	public void visit(TimestampValue timestampValue) {

	}

	@Override
	public void visit(BooleanValue booleanValue) {

	}

	@Override
	public void visit(StringValue stringValue) {

	}

	@Override
	public void visit(Addition addition) {

	}

	@Override
	public void visit(Division division) {

	}

	@Override
	public void visit(Multiplication multiplication) {

	}

	@Override
	public void visit(Subtraction subtraction) {

	}

	@Override
	public void visit(AndExpression andExpression) {

	}

	@Override
	public void visit(OrExpression orExpression) {

	}

	@Override
	public void visit(Between between) {

	}

	@Override
	public void visit(EqualsTo equalsTo) {

	}

	@Override
	public void visit(GreaterThan greaterThan) {

	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {

	}

	@Override
	public void visit(InExpression inExpression) {

	}

	@Override
	public void visit(IsNullExpression isNullExpression) {

	}

	@Override
	public void visit(LikeExpression likeExpression) {

	}

	@Override
	public void visit(MinorThan minorThan) {

	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {

	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {

	}

	@Override
	public void visit(Column column) {

	}

	@Override
	public void visit(SubSelect subSelect) {

	}

	@Override
	public void visit(CaseExpression caseExpression) {

	}

	@Override
	public void visit(WhenClause whenClause) {

	}

	@Override
	public void visit(ExistsExpression existsExpression) {

	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {

	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {

	}

	@Override
	public void visit(Concat concat) {

	}

	@Override
	public void visit(Matches matches) {

	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {

	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {

	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {

	}
}
