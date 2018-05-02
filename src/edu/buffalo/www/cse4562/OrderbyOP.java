package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator;

public class OrderbyOP extends Operator {
    private Operator son;
    private List<OrderByElement> orderBy;
    private List<List<PrimitiveValue>> buffer;
    private List<String> typeList;
    private int bufferSize;
    private boolean isReadAll = false;

    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {
        this.bufferSize = 100;
        this.son = op;
        this.orderBy = orderBy;
        this.buffer = new ArrayList<>(bufferSize);
        // make dir for sort cache
        File f = new File("data/sort_cache/");
        if (f.exists()) {
            f.delete();
            f.mkdir();
        } else {
            f.mkdir();
        }
    }

    private void writeLine(DataOutputStream dataOut) throws PrimitiveValue.InvalidPrimitive, IOException {
        for (List<PrimitiveValue> line : buffer) {
            for (int i = 0; i < typeList.size(); i++) {
                switch (typeList.get(i)) {
                    case "LONG":
                        dataOut.writeLong(line.get(i).toLong());
                        break;
                    case "DOUBLE":
                        dataOut.writeDouble(line.get(i).toDouble());
                        break;
                    default:
                        dataOut.writeUTF(line.get(i).toRawString());
                        break;
                }
            }
        }
    }

    private void readLine(List<PrimitiveValue> line, DataInputStream dataIn) throws IOException {
        line.clear();
        for (String type : typeList) {
            switch (type) {
                case "LONG":
                    line.add(new LongValue(dataIn.readLong()));
                    break;
                case "DOUBLE":
                    line.add(new DoubleValue(dataIn.readDouble()));
                    break;
                case "STRING":
                    line.add(new StringValue(dataIn.readUTF()));
                    break;
                case "DATE":
                    line.add(new DateValue(dataIn.readUTF()));
                    break;
            }
        }
    }

    private void merge(List<String> fileNames) throws IOException {
        List<PrimitiveValue> line = new ArrayList<>();
        FileInputStream in = new FileInputStream(fileNames.get(0));
        DataInputStream dataIn = new DataInputStream(in);
        readLine(line, dataIn);
        System.out.println();
    }

    private void fillBuffer() {

    }

    private void updateBufferSize() {

    }

    @Override
    public Tuple result() {
        if (!this.isReadAll) {
            // In case table has no content
            Tuple tuple = this.son.result();
            if (tuple == null) return null;
            // TODO get the size of one tuple and available memory => bufferSize
            List<Integer> colIndexList = new ArrayList<>();
            List<Boolean> isAscList = new ArrayList<>();
            for (OrderByElement ob : this.orderBy) {
                isAscList.add(ob.isAsc());
                String colName = ob.getExpression().toString();
                colIndexList.add(tuple.getIndexHash().get("*").get(colName));
            }
            typeList = new ArrayList<>();
            for (PrimitiveValue v : tuple.getTuple()) {
                typeList.add(v.getType().name());
            }


            // Read buffer
            List<String> fileNames = new ArrayList<>();
            int cnt = 0;
            while (tuple != null) {
                // read tuples, unpack, output to file
                for (int i = 0; i < bufferSize && tuple != null; i++) {
                    buffer.add(tuple.getTuple());
                    tuple = this.son.result();
                }

                Helper.sort(buffer, colIndexList, isAscList);
                String path = "data/sort_cache/" + String.valueOf(cnt);
                File newFile = new File(path);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream out = new FileOutputStream(path);
                    DataOutputStream dataOut = new DataOutputStream(out);
                    writeLine(dataOut);
                    out.close();
                    dataOut.close();
                } catch (IOException | PrimitiveValue.InvalidPrimitive e) {
                    e.printStackTrace();
                }

                buffer.clear();
                fileNames.add(path);
                cnt++;
            }
            this.isReadAll = true;
            // TODO after write all unpacked tuple to disk , recalculate bufferSize

            try {
                merge(fileNames);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

	@Override
	public Operator getSon() {
		return this.son;
	}

	@Override
	public void setSon(Operator son) {
		// TODO Auto-generated method stub
		
	}
}
