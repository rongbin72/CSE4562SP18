package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Iterator {
	//deal with each where and select
	private FromObject fromOB; 
	private WhereObject whereOB;
	private SelectObject selectOB;
	private Expression where;
	private List<List<PrimitiveValue>> resultTuples = new ArrayList<List<PrimitiveValue>>();
	
	public Iterator(PlainSelect body) {
		this.fromOB = new FromObject(body.getFromItem(),body.getJoins()); 
		this.where = body.getWhere();
		this.whereOB = new WhereObject(this.where);
		this.selectOB = new SelectObject(body.getSelectItems());
	}
	
	public List<List<PrimitiveValue>> Result() throws IOException, SQLException {

		Read tempIters = this.fromOB.GetTable();// the iteratorable table
		List<String> tableName = this.fromOB.getName();//the table name
		tempIters.optimizeTables(this.where);

		this.selectOB.setTable(tableName);
		//pass value to select
		//get value 
		//pass value to where
		//select
		HashMap<String, List<PrimitiveValue>> line = tempIters.ReadLine();
		while(line != null) {
			List<Integer> selectResult = this.selectOB.Result(line);
			line = this.selectOB.getTuple();
			if(this.whereOB.Result(line)) {
				List<PrimitiveValue> tempResult = new ArrayList<PrimitiveValue>();
				for(int i = 0;i < selectResult.size();i++) {
					tempResult.add(line.get(line.keySet().iterator().next()).get(selectResult.get(i)));
				}
				this.resultTuples.add(tempResult);
			}
			Schema.reset(2);
			this.selectOB.reset();
			line = tempIters.ReadLine();
		}
		return this.resultTuples;
	}
	
	public HashMap<String, Integer> getNewColindex(){
		return this.selectOB.colIndex();
	}
	


	
}
