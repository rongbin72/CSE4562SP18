package edu.buffalo.www.cse4562;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jsqlparser.expression.*;


public class Read {
	private List<List<List<PrimitiveValue>>> tables;
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
		            Schema.setTableContent(name, tmpTable);
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
		
		for(int i = 0;i<tablenames.size();i++) {
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
					List<Integer> tmp = ans.get(i);
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
				result.put(this.tableNames.get(i), this.tables.get(i).get(line));
			}
			return result;
		}
		else {
			return null;
		}
	}
}
