package edu.buffalo.www.cse4562;

import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;

public class FromObject {
	private FromItem body;
	
	public FromObject(FromItem body) {
		this.body = body;
	}
	
	public Reader GetTable() {
		
	}
	
	public String GetTableName() {
		
	}
	// get table
}
