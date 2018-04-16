package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Read extends Operator{
	private String tableName;
	private BufferedReader br;
	private boolean eof = false;
	private Integer bufferSize = 10000000;
	private double factor = 0.5;
	private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();
	private List<List<PrimitiveValue>> table = new ArrayList<>();
	private Iterator<List<PrimitiveValue>> tableIter;

	private void fillBuffer() throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			this.buffer.add(Helper.toPrimitive(this.tableName, line));
			if (this.buffer.size() == this.bufferSize) {
				break;
			}
		}
		if (this.buffer.size() < bufferSize) {
			this.eof = true;
		}
	}
	
	public String getTablename() {
		return this.tableName;
	}

	public Read(Table table) throws IOException {
		this.tableName = table.getName();
		String path = Schema.getPath(tableName);
		Path p = Paths.get(path);
		List<String> ls = Files.readAllLines(p);
		for (String i : ls) {
			this.table.add(Helper.toPrimitive(tableName, i));
		}
		this.tableIter = this.table.iterator();
//		FileReader fr = new FileReader(path);
//		fillBuffer();
	}

	private void init() throws IOException {
		String path = Schema.getPath(this.tableName);
		this.br = new BufferedReader(new FileReader(path));
		fillBuffer();
	}

	@Override
	public Tuple result() {
		// fill buffer
//		if (!eof && this.buffer.size() <= this.bufferSize * this.factor) {
//			try {
//				fillBuffer();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (eof && buffer.size() == 0) {
//			try {
//				this.br.close();
//				init();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return null;
		if (this.tableIter.hasNext()) {
			return new Tuple(tableName, tableIter.next());
		} else {
			this.tableIter = this.table.iterator();
			return null;
		}
	}

	@Override
	public Operator getSon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSon(Operator son) {
		
	}
}
