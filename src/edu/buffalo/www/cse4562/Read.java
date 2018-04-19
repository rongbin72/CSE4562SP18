package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.*;
import java.util.*;


public class Read extends Operator {
    private String tableName;
    private String path;
    private boolean eof = false;
    private boolean isFirstLine = true;
    private Integer bufferSize = 100;
    private BufferedReader br;
    private double factor = 0.5;
    private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();
    private List<List<PrimitiveValue>> table = new ArrayList<>();
    private Iterator<List<PrimitiveValue>> tableIterator;

    public Read(Table table) {
        this.tableName = table.getName();
        this.path = Schema.getPath(tableName);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(new File(this.path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.br = new BufferedReader(new InputStreamReader(fs));
        try {
            fillBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillBuffer() throws IOException {
        String line;
        while ((line = this.br.readLine()) != null) {
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

    private void init() throws IOException {
        if (!this.tableName.equals("PLAYERS")) {
            FileInputStream fs = new FileInputStream(new File(this.path));
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String line;
            while ((line = br.readLine()) != null) {
                this.table.add(Helper.toPrimitive(this.tableName, line));
            }
            fs.close();
            br.close();
            this.tableIterator = this.table.iterator();
        } else {
            FileInputStream fs = new FileInputStream(new File(this.path));
            this.br = new BufferedReader(new InputStreamReader(fs));
            fillBuffer();
        }
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
        if (!this.tableName.equals("PLAYERS")) {
            if (isFirstLine) {
                try {
                    init();
                    isFirstLine = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (this.tableIterator.hasNext()) {
                    return new Tuple(tableName, new ArrayList<>(tableIterator.next()));
                }
            }

            if (this.tableIterator.hasNext()) {
                return new Tuple(tableName, new ArrayList<>(tableIterator.next()));
            } else {
                this.tableIterator = this.table.iterator();
                return null;
            }
        } else {
//			 fill buffer
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
            }
            return new Tuple(tableName, this.buffer.poll());
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
