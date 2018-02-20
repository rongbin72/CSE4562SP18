package edu.buffalo.www.cse4562;

import java.util.List;

import net.sf.jsqlparser.statement.select.*;

public class SelectObject {
	private List<SelectItem> items;
	
	public SelectObject(List<SelectItem> list) {
		this.items = list;
	}
	
	public List<String> Result(List<String> tuple) {
		
	}
}
