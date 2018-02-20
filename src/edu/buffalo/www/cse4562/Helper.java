package edu.buffalo.www.cse4562;

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

    public static void prompt() {
        System.out.println("$> ");
        System.out.flush();
    }
    
    public static void output(List<List<String>> output) {
    	
    }
}
