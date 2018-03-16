package edu.buffalo.www.cse4562;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Helper {

    /**
     * Always call <code>flsuh()</code> after <code>println()</code>
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
    public static void prompt() {
        System.out.println("$> ");
        System.out.flush();
    }

    /**
     * Print table without limit
     *
     * @param table List of tuple
     */
    private static void printTable(List<List<PrimitiveValue>> table) {
        for (List<PrimitiveValue> row : table) {
            List<String> line = new ArrayList<>();
            for (PrimitiveValue cell : row) {
                line.add(cell.toString());
            }
            print(String.join("|", line));
        }
    }

    /**
     * Print table with limit
     *
     * @param table List of tuple
     * @param limit number of rows
     */
    private static void printTable(List<List<PrimitiveValue>> table, int limit) {
        limit = limit <= table.size() ? limit : table.size();
        for (List<PrimitiveValue> row : table.subList(0, limit)) {
            List<String> line = new ArrayList<>();
            for (PrimitiveValue cell : row) {
                line.add(cell.toString());
            }
            print(String.join("|", line));
        }
    }

    /**
     * Print
     *
     * @param table   List of tuple
     * @param orderBy OrderByElement
     * @param limit   Limit
     */
    public static void output(List<List<PrimitiveValue>> table, List<OrderByElement> orderBy, Limit limit) {
        // no order by, no limit, print all
        if (orderBy == null && limit == null) {
            printTable(table);
        } else if (orderBy != null && limit == null) {
            // order by without limit
            boolean isAsc = orderBy.get(0).isAsc();
            Column col = (Column) orderBy.get(0).getExpression();
            String colName = col.getColumnName();
            int colIndex = Schema.getColIndex("*", colName);

            cmp(table, colIndex, isAsc);
            printTable(table);

        } else if (orderBy == null && limit != null) {
            // limit without order by
            printTable(table, (int) limit.getRowCount());

        } else if (orderBy != null && limit != null) {
            // order by with limit
            boolean isAsc = orderBy.get(0).isAsc();
            Column col = (Column) orderBy.get(0).getExpression();
            String colName = col.getColumnName();
            int colIndex = Schema.getColIndex("*", colName);

            cmp(table, colIndex, isAsc);
            printTable(table, (int) limit.getRowCount());

        }
    }

    private static void cmp(List<List<PrimitiveValue>> table, int colIndex, boolean isAsc) {
        table.sort((a, b) -> {
            PrimitiveValue lhs = a.get(colIndex);
            PrimitiveValue rhs = b.get(colIndex);
            String type = a.get(colIndex).getType().name();
            switch (type) {
                case "LONG":
                    try {
                        Long left = lhs.toLong();
                        Long right = rhs.toLong();
                        if (isAsc) {
                            return left.compareTo(right);
                        } else {
                            return right.compareTo(left);
                        }
                    } catch (PrimitiveValue.InvalidPrimitive throwables) {
                        throwables.printStackTrace();
                    }

                case "STRING":
                    String left = lhs.toString();
                    String right = rhs.toString();
                    if (isAsc) {
                        return left.compareTo(right);
                    } else {
                        return right.compareTo(left);
                    }

                case "DOUBLE":
                    try {
                        Double l = lhs.toDouble();
                        Double r = rhs.toDouble();
                        if (isAsc) {
                            return l.compareTo(r);
                        } else {
                            return r.compareTo(l);
                        }
                    } catch (PrimitiveValue.InvalidPrimitive throwables) {
                        throwables.printStackTrace();
                    }

                case "DATE":
                    Date l = ((DateValue) lhs).getValue();
                    Date r = ((DateValue) rhs).getValue();
                    if (isAsc) {
                        return l.compareTo(r);
                    } else {
                        return r.compareTo(l);
                    }
            }
            return 0;
        });
    }

    /**
     * @param tableName table name of current table
     * @param line      string returned by <code>readline</code>
     * @return tuple of <code>PrimitiveValue</code>
     */
    public static List<PrimitiveValue> toPrimitive(String tableName, String line) {
        List<PrimitiveValue> tuple = new ArrayList<>();
        List<String> lineSplit = Arrays.asList(line.split("\\|"));
        int index = 0;
        for (String cell : lineSplit) {
            String type = Schema.getColType(tableName, index);
            // keep the type as lower case
            switch (type.toLowerCase()) {
                case "integer":
                    tuple.add(new LongValue(cell));
                    break;
                case "string":
                    tuple.add(new StringValue(cell));
                    break;
                case "double":
                    tuple.add(new DoubleValue(cell));
                    break;
                case "date":
                    tuple.add(new DateValue(cell));
                    break;
            }
            index++;
        }
        return tuple;
    }

    /**
     * If two string equals (case insensitive)
     * <code>abc == ABC</code>
     *
     * @param a string
     * @param b string
     * @return true if equals else false
     */
    public static boolean equals(String a, String b) {
        return a.toLowerCase().equals(b.toLowerCase());
    }
}
