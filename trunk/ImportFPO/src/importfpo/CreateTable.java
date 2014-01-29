/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfpo;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author evstratovskijos
 */
public class CreateTable {

    private static Connection connect = null;

    /**
     * инициализация конекта к базе
     */
    public void init() {
        try {
           // Class.forName("org.apache.derby.jdbc.ClientDriver"); //вызов драйвера
                Class.forName("org.postgresql.Driver");
            //String URL = "jdbc:derby://localhost:1527/rozisk"; //подключение к базе intradb-rsk
                String URL ="jdbc:postgresql://localhost:5432/oracle";
            String user = "test";
            String psw = "test";
            connect = DriverManager.getConnection(URL, user, psw);
            System.out.println("init");
//            Statement statement = con.createStatement();
//            String sql = "select * from test";

//            ResultSet rs;
//            rs = statement.executeQuery(sql);
//            ResultSetMetaData rsmd = rs.getMetaData();

//            while (rs.next()) {
//                String name = rsmd.getColumnName(1);
//                String type = rsmd.getColumnTypeName(1);
//                System.out.println("nmae " + name + " type " + type);
//            }

//            con.close();

        } catch (Exception ex) {
       //     ex.printStackTrace();
        }

    }

    /**
     * метод закрывает конект к базе
     * @throws SQLException
     */
    public void close() throws SQLException {
        connect.close();
        System.out.println("close connect");
    }

    /**
     * Sets this connection's auto-commit mode to the given state. If a connection is in auto-commit mode, then all its SQL statements will be executed and committed as individual transactions. Otherwise, its SQL statements are grouped into transactions that are terminated by a call to either the method commit or the method rollback. By default, new connections are in auto-commit mode.
    The commit occurs when the statement completes. The time when the statement completes depends on the type of SQL Statement:
    For DML statements, such as Insert, Update or Delete, and DDL statements, the statement is complete as soon as it has finished executing.
    For Select statements, the statement is complete when the associated result set is closed.
    For CallableStatement objects or for statements that return multiple results, the statement is complete when all of the associated result sets have been closed, and all update counts and output parameters have been retrieved.
    NOTE: If this method is called during a transaction and the auto-commit mode is changed, the transaction is committed. If setAutoCommit is called and the auto-commit mode is not changed, the call is a no-op.
    Parameters:
    autoCommit - true to enable auto-commit mode; false to disable it
    Throws:
    SQLException - if a database access error occurs, setAutoCommit(true) is called while participating in a distributed transaction, or this method is called on a closed connection
     * @param vkl
     * @throws SQLException
     */
    public void setAutoCommit(boolean vkl) throws SQLException {
        connect.setAutoCommit(vkl);
    }

    public void commit() throws SQLException {
        connect.commit();
    }
/**
 * проверяет есть ли в базе таблица с таким названием
 * @param nameTable название талицы
 * @return если есть false если нет true
 */
    public boolean checkOpenNameTable(String nameTable) {
        boolean test = true;
        if (!nameTable.trim().equals("")) {
            try {
                Statement st = connect.createStatement();
                String sql = "select * from " + nameTable + " ";
                st.executeQuery(sql);
                test = false;
                st.close();
            } catch (Exception e) {
                return true;
            }//ResultSetMetaData rsmd = rs.getMetaData();
        }

        return test;
    }

    /**
     * создает в оракле таблицы на основе дбф таблиц
     * @param nameTable название таблицы
     * @param nameColumn название колонки
     * @param typeColumn тип поля(из ДБФ)
     * @param sizeColumn размер поля(количество символов)
     * @return тру если все нормально или фолс если жопа
     * @throws SQLException проверить есть ли конекшен к базе
     */
    public boolean creatTable(String nameTable, String[] nameColumn, String[] typeColumn, String[] sizeColumn) throws SQLException {
        //   init();
        if (connect != null) {
            String razdelit = ", ";
            String[] allColumn = new String[nameColumn.length];
            String all = "";
            // StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nameColumn.length; i++) {
                // System.out.println("type: " + typeColumn[i].charAt(0));
                switch (typeColumn[i].charAt(0)) {
                    case 'C':
                        allColumn[i] = nameColumn[i] + " Varchar(" + sizeColumn[i] + ")";
                        break;
                    case 'N':
                        allColumn[i] = nameColumn[i] + " Numeric(" + sizeColumn[i] + ")";
                        break;
                    case 'L':
                        allColumn[i] = nameColumn[i] + " Varchar(1) default 'F'";
                        break;
                    case 'D':
                        allColumn[i] = nameColumn[i] + " Date";
                        // allColumn[i] = nameColumn[i] + " " + typeColumn[i];
                        //System.out.println("ttttttttttt " + allColumn[i]);
                        break;
                    case 'F':
                         allColumn[i] = nameColumn[i] + " Numeric(" + sizeColumn[i] + ")";
                        break;
                    case 'M':
                         allColumn[i] = nameColumn[i] + " Clob";
                         break;
                    default:
                        allColumn[i] = "xyi";
                        System.out.println("XYI");
                }

                if (nameColumn.length - (i + 1) != 0) {

                    allColumn[i] = allColumn[i] + razdelit;
                }
                //  sb.append(allColumn[i]);
                all = all + allColumn[i];
            }

          //  all = sb.toString();
            System.out.println("nameTable: " + nameTable);
            System.out.println("all: " + all);
            String sql = " create table " + nameTable + ""
                    + "( form_ID INT not null primary key"
       + " GENERATED ALWAYS AS IDENTITY "
       + " (START WITH 1, INCREMENT BY 1), " + all + " )" ;
//                    + " tablespace SYSTEM "
//                    + " pctfree 10 "
//                    + " pctused 40 "
//                    + " initrans 1 "
//                    + " maxtrans 255 "
//                    + " storage ( "
//                    + " initial 64 "
//                    + " minextents 1 "
//                    + " maxextents unlimited "
//                    + " ) ";
            PreparedStatement ps = connect.prepareStatement(sql);
            // ResultSet rs;
            ps.executeUpdate();
             ps.close();
            //  ResultSetMetaData rsmd = rs.getMetaData();
            // System.out.println("create: " + up);
//        while (rs.next()) {
//            String name = rsmd.getColumnName(1);
//            String type = rsmd.getColumnTypeName(1);
//            System.out.println("nmae " + name + " type " + type);
//        }
           
            return true;
        }
        return false;

    }

    /**
     * вставляет строки в созданные таблицы
     * применять сразу после создания
     * все типы кроме чисел вставляются в одинарных кавычках
     * @param tableName название таблицы
     * @param columnName список названий колонок
     * @param columnValues список значений
     * @throws SQLException проверь конект или что-то не сработало
     */
    public void insertRow(String tableName, String[] columnName, ArrayList columnValues) throws SQLException {
        // System.out.println(columnName.length+" "+columnValues.size());
        if (columnName.length == columnValues.size()) {
            String razdelit = ", ";
            String value = "";
            String name = "";
            for (int i = 0; i < columnName.length; i++) {
                value = value + columnValues.get(i);
                name = name + columnName[i];

                if (columnName.length - (i + 1) != 0) {
                    value = value + razdelit;
                    name = name + razdelit;
                }
            }
         //   System.out.println("t name: " + tableName);
//            System.out.println("name: " + name);
//            System.out.println(" value: " + value);
            String sql = "insert into " + tableName + " (" + name + ") "
                    + " values(" + value + ") ";
          // System.out.println("sql: "+sql);
            //Statement st = connect.createStatement();
            // connect.setAutoCommit(false);
            PreparedStatement pstmt = connect.prepareStatement(sql);
            //  st.executeUpdate(sql);

            pstmt.executeUpdate();
            pstmt.close();
            //connect.commit();
//            System.out.println("count = " + count);
        } else {
            System.out.println("Error, количесвто полей не соотвествует количесву значений для них или наоборот :)");
        }
    }

    public static void main(String[] args) throws SQLException {

        String nameTable = "test2";
        String[] columnName = new String[]{"t1", "t2", "t3", "t4"};
        String[] columnType = new String[]{"Character", "Number", "Logical", "Date"};
        String[] columnSize = new String[]{"30", "5", "true", "999"};
     //   String[] values = new String[]{"'red'", "76", "'12.04.2009'", "'T'"};
        ArrayList v = new ArrayList();
        v.add("'red'");
        v.add("76");
        v.add("'T'");
        v.add("'12.04.2009'");
       
        CreateTable ct = new CreateTable();
        ct.init();
        ct.creatTable(nameTable, columnName, columnType, columnSize);
        ct.insertRow(nameTable, columnName, v);
        //System.out.println(ct.checkOpenNameTable("sobbi"));
        ct.close();
    }
}
