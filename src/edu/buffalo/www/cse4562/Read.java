package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.*;
import java.util.*;


public class Read extends Operator{
	private String tableName;
	private BufferedReader br;
	private boolean eof = false;
	private Integer bufferSize = 10000000;
	private double factor = 0.5;
	private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();
	private List<List<PrimitiveValue>> table = new ArrayList<>();
	private Iterator<List<PrimitiveValue>> tableIterator;

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
	
	public String getTableName() {
		return this.tableName;
	}

	public Read(Table table) throws IOException {
		this.tableName = table.getName();
		String path = Schema.getPath(tableName);
		FileInputStream fs = new FileInputStream(new File(path));
		this.br = new BufferedReader(new InputStreamReader(fs));
		String line;
		while ((line = this.br.readLine()) != null) {
			this.table.add(Helper.toPrimitive(this.tableName, line));
		}
		fs.close();
		br.close();
		this.tableIterator = this.table.iterator();
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
		if (this.tableIterator.hasNext()) {
			return new Tuple(tableName, tableIterator.next());
		} else {
			this.tableIterator = this.table.iterator();
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
