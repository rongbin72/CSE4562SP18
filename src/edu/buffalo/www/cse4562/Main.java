package edu.buffalo.www.cse4562;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Main {
    public static void main(String[] args) throws IOException{

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
    }
}
