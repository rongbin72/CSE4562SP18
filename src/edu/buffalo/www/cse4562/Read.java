package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.PrimitiveValue;
import net.sf.jsqlparser.schema.Table;

import java.io.*;
import java.util.*;


public class Read extends Operator {
    private String tableName;
    private String path;
    private boolean eof = false;
    private boolean isFirstLine = true;
    private Integer bufferSize = 10000;
    private BufferedReader br;
    private double factor = 0.5;
    private Queue<List<PrimitiveValue>> buffer = new LinkedList<>();
    private List<List<PrimitiveValue>> table = new ArrayList<>();
    private Iterator<List<PrimitiveValue>> tableIterator;

    public Read(Table table) {
        this.tableName = table.getName();
        this.path = Schema.getPath(tableName);
    }

    Read(String tableName) {
        this.tableName = tableName;
        this.path = Schema.getPath(tableName);
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
            } else {
                return new Tuple(tableName, this.buffer.poll());
            }
        }
    }

    public Tuple result(PrimitiveValue value) throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.path, "r");
        HashMap<String, TreeMap<PrimitiveValue, ArrayList<Long>>> index = Schema.getIndex(this.tableName);
        ArrayList<Long> indexList = index.get("L_PARTKEY").get(value);
        List<String> res = new ArrayList<>();
        for (Long i : indexList) {
            file.seek(i);
            res.add(file.readLine());
        }
        return null;
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
