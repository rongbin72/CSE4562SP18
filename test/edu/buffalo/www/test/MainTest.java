package edu.buffalo.www.test;

import net.sf.jsqlparser.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    private String getSchema() throws IOException {
        List<String> schema = Files.readAllLines(Paths.get("data/schema.sql"));
        String creatTable = "";
        for(String line : schema) {
            creatTable += line;
        }
        return creatTable;
    }

    private String getQuery(int index) throws IOException {
        List<String> queries = Files.readAllLines(Paths.get("data/queries.sql"));
        return queries.get(index);
    }

    private String getAnswer(String sql) throws SQLException {
        String answer = "";
        Connection con = DriverManager.getConnection("jdbc:sqlite:data/test.db");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            String line = String.join("|", row);
            answer = answer + line + "\r\n";
        }
        stmt.close();
        con.close();
        return answer;
    }

    /**
     *
     * @param sql SQL queries except create table
     * @param expected expected queries result
     * @throws ParseException
     * @throws SQLException
     * @throws IOException
     */
    private void testFlow(String sql, String expected) throws ParseException, SQLException, IOException {
        String create = getSchema();
        sql = create + sql;

        // init stdin and stdout
        ByteArrayInputStream in = new ByteArrayInputStream(sql.getBytes());
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        String[] args = {"args"};  // Meaningless args pass to main to run
        System.setIn(in);  // set stdin
        System.setOut(printStream);

        edu.buffalo.www.cse4562.Main.main(args);

        String actual = out.toString();
        // remove unnecessary chars
        actual = actual.replaceAll("\\$> \r\n", "");
        actual = actual.replaceAll("'", "");
        assertEquals(expected, actual);
    }

    /**
     * Test subselect with where statement
     */
    @Test
    @DisplayName("Query 1")
    void query1() throws ParseException, SQLException, IOException {
        String sql = getQuery(0);
        String expected = getAnswer(sql);
        testFlow(sql, expected);
    }

    @Test
    void query2() throws ParseException, SQLException, IOException {
        String sql = getQuery(1);
        String expected = getAnswer(sql);
        testFlow(sql, expected);
    }

    @Test
    void query3() throws ParseException, SQLException, IOException {
        String sql = getQuery(2);
        String expected = getAnswer(sql);
        testFlow(sql, expected);
    }

    @Test
    void query4() throws ParseException, SQLException, IOException {
        String sql = getQuery(3);
        String expected = getAnswer(sql);
        testFlow(sql, expected);
    }

    @Test
    void query5() throws ParseException, SQLException, IOException {
        String sql = getQuery(4);
        String expected = getAnswer(sql);
        testFlow(sql, expected);
    }

}