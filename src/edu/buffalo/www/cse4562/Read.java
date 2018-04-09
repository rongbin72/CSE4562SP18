package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Read extends Operator{
	private String tableNames;
	private BufferedReader br;
	private boolean eof = false;
	private Integer bufferSize = 100;
	private double factor = 0.5;
	private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();

	private void fillBuffer() throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			this.buffer.add(Helper.toPrimitive(this.tableNames, line));
			if (this.buffer.size() == this.bufferSize) {
				break;
			}
		}
		if (this.buffer.size() < bufferSize) {
			this.eof = true;
		}
	}

	public Read(Table table) throws IOException {
		this.tableNames = table.getName();
		String path = Schema.getPath(tableNames);
		this.br = new BufferedReader(new FileReader(path));
		fillBuffer();
	}

	private void init() throws IOException {
		String path = Schema.getPath(tableNames);
		this.br = new BufferedReader(new FileReader(path));
		fillBuffer();
	}

	@Override
	public Tuple result() {

		if (!eof && this.buffer.size() <= this.bufferSize * this.factor) {
			try {
				fillBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (eof && buffer.size() == 0) {
			try {
				this.br.close();
				init();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		} else {
			return new Tuple(tableNames, buffer.poll());
		}
	}
}
