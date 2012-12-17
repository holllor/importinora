/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importAccess;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Администратор
 */
public class Test {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Test t = new Test();
        t.connectionAcces();
    }

    public List<String> connectionPS() throws ClassNotFoundException, SQLException {
        System.out.println("org.postgresql.Driver ");
        Class.forName("org.postgresql.Driver");

        Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "root");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT zvan FROM spr_zvan");

        List<String> data = new ArrayList<String>();

        while (rs.next()) {
            // System.out.println(rs.getString("zvan"));
            data.add(rs.getString("zvan"));
        }
        // if (con != null) {
        // System.out.println("You made it, take control your database now!");
        // } else {
        // System.out.println("Failed to make connection!");
        // }
        System.out.println("test vizov proshel " + data.get(0));
        return data;
    }

    public List<String> connectionAcces() throws ClassNotFoundException, SQLException {
        //System.out.println("sun.jdbc.odbc.JdbcOdbcDriver ");
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        // установка нужной кодировки
        java.util.Properties prop = new java.util.Properties();
        prop.put("charSet", "cp1251");

        Connection con = DriverManager.getConnection("jdbc:odbc:MSADB", prop);
        DatabaseMetaData dbmd = con.getMetaData();
       ResultSet res =  dbmd.getTables(null, null, null, new String[] {"TABLE"});
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT nam FROM main");
System.out.println("List of tables: "); 
      while (res.next()) {
         System.out.println(res.getString("TABLE_NAME"));
        
      }
      res.close();

         List<String> data = new ArrayList<String>();

        return data;
    }
}
