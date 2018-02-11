package edu.buffalo.www.cse4562;

import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.parser.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException{

        System.out.println("Hello, World");

        // Test CommonsCSV lib
        Reader in = new FileReader("data/FB.csv");
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        for (CSVRecord record : records) {
            String high = record.get("High");
            String low = record.get("Low");

            System.out.println(high);
            System.out.println(low);
        }

        // Test Jsqlparser
        Reader input = new StringReader("SELECT * FROM A\nSELECT * FROM B");
        CCJSqlParser parser = new CCJSqlParser(input);
        Statement statement = parser.Statement();

        while (statement != null) {
            System.out.println(statement.toString());
            statement = parser.Statement();
        }

    }
}
