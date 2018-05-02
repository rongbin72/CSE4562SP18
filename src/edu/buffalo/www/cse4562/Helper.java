package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Helper {

    /**
     * Always call <code>flush()</code> after <code>println()</code>
     *
     * @param str The <code>String</code> to be printed
     */
    private static void print(String str) {
        System.out.println(str);
        System.out.flush();
    }

    /**
     * Print the prompt <code>$> </code>
     */
    static void prompt() {
        System.out.println("$> ");
        System.out.flush();
    }

    static void output(Operator tree) {
        Tuple tuple;
        while ((tuple = tree.result()) != null) {
            List<PrimitiveValue> row = tuple.getTuple();
            List<String> line = new ArrayList<>();
            for (PrimitiveValue cell : row) {
                line.add(cell.toString());
            }
            print(String.join("|", line));
        }
    }

    static void sort(List<List<PrimitiveValue>> table, List<Integer> orderList, List<Boolean> isAscList) {
        table.sort((a, b) -> {
            int res = 0;
            for (int i = 0; i < orderList.size(); i++) {
                PrimitiveValue lhs = a.get(orderList.get(i));
                PrimitiveValue rhs = b.get(orderList.get(i));
                String type = lhs.getType().name();
                Boolean isAsc = isAscList.get(i);

                switch (type) {
                    case "LONG":
                        try {
                            Long left = lhs.toLong();
                            Long right = rhs.toLong();
                            res = isAsc ? left.compareTo(right) : right.compareTo(left);
                        } catch (PrimitiveValue.InvalidPrimitive throwable) {
                            throwable.printStackTrace();
                        }

                        if (res != 0) return res;
                        break;

                    case "STRING":
                        String left = lhs.toString();
                        String right = rhs.toString();
                        res = isAsc ? left.compareTo(right) : right.compareTo(left);

                        if (res != 0) return res;
                        break;

                    case "DOUBLE":
                        try {
                            Double l = lhs.toDouble();
                            Double r = rhs.toDouble();
                            res = isAsc ? l.compareTo(r) : r.compareTo(l);
                        } catch (PrimitiveValue.InvalidPrimitive throwable) {
                            throwable.printStackTrace();
                        }

                        if (res != 0) return res;
                        break;

                    case "DATE":
                        Date l = ((DateValue) lhs).getValue();
                        Date r = ((DateValue) rhs).getValue();
                        res = isAsc ? l.compareTo(r) : r.compareTo(l);

                        if (res != 0) return res;
                        break;
                }
            }
            return res;
        });
    }

    /**
     * @param tableName table name of current table
     * @param line      string returned by <code>readLine</code>
     * @return tuple of <code>PrimitiveValue</code>
     */
    static List<PrimitiveValue> toPrimitive(String tableName, String line) {
        List<PrimitiveValue> tuple = new ArrayList<>();
        String[] lineSplit = line.split("\\|");
        int index = 0;
        for (String cell : lineSplit) {
            String type = Schema.getColType(tableName, index);
            // keep the type as lower case
            switch (type) {
                case "INTEGER":
                    tuple.add(new LongValue(cell));
                    break;
                case "STRING":
                    tuple.add(new StringValue(cell));
                    break;
                case "DOUBLE":
                    tuple.add(new DoubleValue(cell));
                    break;
                case "DATE":
                    tuple.add(new DateValue(cell));
                    break;
            }
            index++;
        }
        return tuple;
    }
}
