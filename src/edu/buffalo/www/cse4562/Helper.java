package edu.buffalo.www.cse4562;

import javafx.scene.Scene;
import net.sf.jsqlparser.expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {

    /**
     * Always call <code>flsuh()</code> after <code>println()</code>
     * 
     * @param str The <code>String</code> to be printed
     */
    public static void print(String str) {
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
     * Print out result
     * 
     * @param table iterator of two dimentional list of output table
     */
    public static void output(java.util.Iterator<List<String>> table) {
        for(;table.hasNext();) {
            List<String> row = table.next();
            print(String.join("|", row));
        }
    }

    /**
     *
     * @param tableName table name of current table
     * @param line string returned by <code>readline</code>
     * @return tuple of <code>PrimitiveValue</code>
     */
    public static List<PrimitiveValue> toPrimitive(String tableName, String line) {
        List<PrimitiveValue> tuple = new ArrayList<>();
        List<String> lineSplit = Arrays.asList(line.split("\\|"));
        int index = 0;
        for(String cell : lineSplit) {
            String type = Schema.getColType(tableName, index);
            // keep the type as lower case
            switch (type.toLowerCase()) {
                case "int":
                    tuple.add(new LongValue(cell));
                    break;
                case "string":
                    tuple.add(new StringValue(cell));
                    break;
                case "decimal":
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
     * @param a string
     * @param b string
     * @return true if equals else false
     */
    public static boolean equals(String a, String b) {
        return a.toLowerCase().equals(b.toLowerCase());
    }
}
