package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Read extends Operator{
	private String tableNames;
	
	public Read(Table table) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tuple result() {
		// TODO Auto-generated method stub
		return null;
	}
}
