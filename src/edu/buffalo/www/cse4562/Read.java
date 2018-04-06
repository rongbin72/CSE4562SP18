package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Read extends Operator{
	private String tableNames;
	private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();

	
	public Read(Table table) {
		this.tableNames = table.getName();
		String path = Schema.getPath(tableNames);

	}

	@Override
	public Tuple result() {
		return new Tuple(tableNames, buffer.poll());
	}
}
