package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.PrimitiveValue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Read {
	private List<List<List<PrimitiveValue>>> tables = new ArrayList<>();
	private List<String> tableNames;
	private java.util.Iterator<List<Integer>> itor;
	private List<Integer> length = new ArrayList<Integer>();
	
	public Read(List<String> tablenames) {
		this.tableNames = tablenames;
		for(String name:tableNames) {
			List<List<PrimitiveValue>> tmpTable = Schema.getTableContent(name);
			if(tmpTable == null) {
				//this is a table without content in file
				try {
		            File file  = new File(Schema.getPath(name));
		            FileInputStream fis = new FileInputStream(file);
		            BufferedInputStream bis = new BufferedInputStream(fis);
		            byte[] buffer = new byte[1024];
		            int cnt = 0;
		            tmpTable = new ArrayList<List<PrimitiveValue>>();
		            String bluck = "";
		            while((cnt=bis.read(buffer)) != -1) {
		              bluck += new String(buffer, 0, cnt);  
		            }
		            bis.close();
		            String[] textTable = bluck.split(System.getProperty("line.separator"));
		            for(String i:textTable) {
		            	tmpTable.add(Helper.toPrimitive(name, i));
		            }
		            this.tables.add(tmpTable);
		            // pass value not pointer
		            Schema.setTableContent(name, new ArrayList<>(tmpTable));
		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
			}
			else {
				//this is a table with content in file
				this.tables.add(tmpTable);
			}
		}
		

//		this.itor = this.getRowIndex(this.length).iterator();
		
	}
	
	public void optimizeTables(Expression e) throws SQLException {
		Optimizer opt = new Optimizer(e,this.tables,this.tableNames);
		this.tables = opt.getOptimizedTable();
		
	}

	public void buildIndex() {
		for(int i = 0;i<this.tableNames.size();i++) {
			this.length.add(this.tables.get(i).size());
		}
		this.itor = this.getRowIndex(this.length).iterator();
	}

	private List<List<Integer>> getRowIndex(List<Integer> length){
		List<List<Integer>> ans = new ArrayList<List<Integer>>();
		for(int i = 0;i < length.get(0);i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(i);
			ans.add(tmp);
		}
		if(length.size() == 1) {
			return ans;
		}
		else {
			length.remove(0);
			List<List<Integer>> ret = this.getRowIndex(length);
			List<List<Integer>> result = new ArrayList<List<Integer>>();
			for(int i = 0;i < ans.size();i++) {
				for(int j = 0;j < ret.size();j++) {
					List<Integer> tmp = new ArrayList<>();
					tmp.addAll(ans.get(i));
					tmp.addAll(ret.get(j));
					result.add(tmp);
				}
			}
			return result;
		}
	}

	public HashMap <String, List<PrimitiveValue>> ReadLine(){
		HashMap <String, List<PrimitiveValue>> result = new HashMap <String, List<PrimitiveValue>>();
		if(this.itor.hasNext()) {
			List<Integer> tmp = this.itor.next();
			for(int i = 0;i < tmp.size();i++) {
				int line = tmp.get(i);
				// put deep copy into result
				result.put(this.tableNames.get(i), new ArrayList<>(this.tables.get(i).get(line)));
			}
			return result;
		}
		else {
			return null;
		}
	}
}
