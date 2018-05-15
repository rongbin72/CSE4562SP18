package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderbyOP extends Operator {
    private Operator son;
    private List<OrderByElement> orderBy;
    private List<List<PrimitiveValue>> buffer;
    private List<String> typeList;
    private List<Integer> colIndexList = new ArrayList<>();
    private List<Boolean> isAscList = new ArrayList<>();
    private int bufferSize;
    private boolean isReadAll = false;
    private String cacheFolder = "data/sort_cache/";
    private Evaluation eval = new Evaluation();
    private GreaterThan greaterThan = new GreaterThan();
    private MinorThan minorThan = new MinorThan();
    private EqualsTo equalsTo = new EqualsTo();
    private DataInputStream result;

    public OrderbyOP(Operator op, List<OrderByElement> orderBy) {
        this.bufferSize = 10;
        this.son = op;
        this.orderBy = orderBy;
        this.buffer = new ArrayList<>(bufferSize);
        // make dir for sort cache
        File dir = new File(cacheFolder);
        if (dir.exists()) {
            // delete all files in this dir
            for (File f : dir.listFiles()) if (!f.isDirectory()) f.delete();
        } else {
            dir.mkdir();
        }
    }

    /**
     * compare two lines, determine which line to be merged first
     *
     * @param left  {@link List}
     * @param right {@link List}
     * @return true if left first else right
     */
    private boolean cmp(List<PrimitiveValue> left, List<PrimitiveValue> right) throws SQLException {
        for (int i = 0; i < colIndexList.size(); i++) {
            boolean isAsc = isAscList.get(i);
            PrimitiveValue lhs = left.get(colIndexList.get(i));
            PrimitiveValue rhs = right.get(colIndexList.get(i));


            equalsTo.setLeftExpression(lhs);
            equalsTo.setLeftExpression(rhs);
            if (eval.eval(equalsTo).toBool()) {
                continue;
            }

            if (isAsc) {
                minorThan.setLeftExpression(lhs);
                minorThan.setRightExpression(rhs);
                return eval.eval(minorThan).toBool();
            } else {
                greaterThan.setLeftExpression(lhs);
                greaterThan.setRightExpression(rhs);
                return eval.eval(greaterThan).toBool();
            }
        }
        // merge left if all equals
        return true;
    }

    /**
     * Write all lines from buffer to disk
     *
     * @param dataOut {@link DataOutputStream}
     */
    private void writeAll(DataOutputStream dataOut) throws PrimitiveValue.InvalidPrimitive, IOException {
        for (List<PrimitiveValue> line : buffer) {
            writeLine(line, dataOut);
        }
    }

    /**
     * Write a line from memory to disk
     *
     * @param line    {@link List}
     * @param dataOut {@link DataOutputStream}
     */
    private void writeLine(List<PrimitiveValue> line, DataOutputStream dataOut) throws PrimitiveValue.InvalidPrimitive, IOException {
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

    /**
     * readLine from file into line
     *
     * @param line   {@link List}
     * @param dataIn {@link DataOutputStream}
     * @return true if read succeed else false (already read all lines)
     */
    private boolean readLine(List<PrimitiveValue> line, DataInputStream dataIn) throws IOException {
        // determine if EOF, may have bug here
        if (dataIn.available() <= 0) return false;

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
        return true;
    }

    /**
     * Merge two sorted files into right file
     *
     * @param left  {@link String}
     * @param right {@link String}
     */
    private void merge(String left, String right) throws IOException, SQLException {
        List<PrimitiveValue> lhs = new ArrayList<>();
        List<PrimitiveValue> rhs;

        FileInputStream leftIn = new FileInputStream(left);
        FileInputStream rightIn = new FileInputStream(right);
        FileOutputStream out = new FileOutputStream(right);

        DataInputStream leftData = new DataInputStream(leftIn);
        DataInputStream rightData = new DataInputStream(rightIn);
        DataOutputStream outData = new DataOutputStream(out);

        fillBuffer(rightData);
        Iterator<List<PrimitiveValue>> bufferIterator = buffer.iterator();
        while (leftData.available() > 0 && bufferIterator.hasNext()) {
            readLine(lhs, leftData);
            rhs = bufferIterator.next();
            if (cmp(lhs, rhs)) {
                writeLine(lhs, outData);
                readLine(lhs, leftData);
            } else {
                writeLine(rhs, outData);
                rhs = bufferIterator.next();
            }

            if (leftData.available() > 0) {
                while (readLine(lhs, leftData)) {
                    writeLine(lhs, outData);
                }
            }

            while (bufferIterator.hasNext()) {
                writeLine(rhs, outData);
            }

        }

        buffer.clear();
        leftIn.close();
        rightIn.close();
        out.close();
        leftData.close();
        rightData.close();
        outData.close();
    }

    /**
     * Merge all sorted chunks into one file
     *
     * @param nameList {@link List}
     */
    private void externalSort(List<String> nameList) throws IOException, SQLException {
        for (int i = 0; i < nameList.size() - 1; i++) {
            merge(nameList.get(i), nameList.get(i + 1));
        }
    }

    private void fillBuffer(DataInputStream dataIn) throws IOException {
        buffer.clear();
        List<PrimitiveValue> line = new ArrayList<>();
        for (int i = 0; i < bufferSize; i++) {
            if (readLine(line, dataIn)) {
                buffer.add(line);
            } else {
                break;
            }
        }
    }

    private void updateBufferSize() {

    }

    // TODO overload List<PrimitiveValue> result()
    @Override
    public Tuple result() {
        return null;
    }

    public List<PrimitiveValue> result(boolean useless) throws IOException, SQLException {
        if (!this.isReadAll) {
            // In case table has no content
            Tuple tuple = this.son.result();
            if (tuple == null) return null;
            // TODO get the size of one tuple and available memory => bufferSize

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
                String path = cacheFolder + String.valueOf(cnt);
                File newFile = new File(path);
                newFile.createNewFile();

                FileOutputStream out = new FileOutputStream(path);
                DataOutputStream dataOut = new DataOutputStream(out);
                writeAll(dataOut);
                out.close();
                dataOut.close();

                buffer.clear();
                fileNames.add(String.valueOf(cnt));
                cnt++;
            }
            this.isReadAll = true;
            // TODO after write all unpacked tuple to disk , recalculate bufferSize

            externalSort(fileNames);

            String sortedFileName = fileNames.get(fileNames.size() - 1);
            result = new DataInputStream(new FileInputStream(cacheFolder + sortedFileName));
            List<PrimitiveValue> out = new ArrayList<>();

            return readLine(out, result) ? out : null;
        } else {
            List<PrimitiveValue> out = new ArrayList<>();

            return readLine(out, result) ? out : null;
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
