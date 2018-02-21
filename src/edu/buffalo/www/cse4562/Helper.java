package edu.buffalo.www.cse4562;

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
     * @param output two dimentional list of output table
     */
    public static void output(List<List<String>> output) {

        for(List<String> row : output) {
            String line ="";
            for(String cell : row) {
                line = line + cell + " ";
            }
            line = line.trim().replace(" ", "|");
            print(line);
        }
    }
}
