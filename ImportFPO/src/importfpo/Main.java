/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfpo;

import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Vector;


/**
 *
 * @author evstratovskijos
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        CreateTable ct = new CreateTable();
        getDataDBF get = new getDataDBF();
        //String path = "c:\\fposerv\\fpo";//\\Balans-kd1\FPO\FPO\
        String path = "c:\\\\FOXPRO\\DBFS\\forms";
        //указываем расположение базы
        GetFile gf = new GetFile(path);
        //инициализация конекшена
        ct.init();
        //String[] fes = gf.getAllFes();
       
                String[] file = gf.getDbfFile();

                  System.out.println("file lenth "+file.length);
//                  for (int i = 0; i < file.length; i++) {
//                      System.out.println(file[i]);
//
//        }
                //перечисление файлов
                for (int k = 0; k < file.length; k++) {
                  //  System.out.println(file[k]+ " form1.dbf");
                    if (file[k].equalsIgnoreCase("form1.dbf") || file[k].equalsIgnoreCase("form1.fpt")) {
                        System.out.println("PROSHEL " + file[k]);
                        //получение структуры таблицы дбф
                      
                        ArrayList<String[]> data = get.getDbfField( path  , file[k]);
                        //извлечение данных из таблицы дбф
                      

                        ArrayList<ArrayList> value = get.getDbfData( path , file[k], "1", "2");

                    //создание аналога в оракл
                    if (ct.checkOpenNameTable(data.get(0)[0])) {
                        ct.creatTable(data.get(0)[0], data.get(1), data.get(2), data.get(3));
                    }

                        // инсерт извлеченных данных во вновь созданную таблицу
                        ct.setAutoCommit(true);
                        int count = 1;
                        for (int ii = 0; ii < value.size(); ii++) {
                            ArrayList row = value.get(ii);
                            
                            ct.insertRow(data.get(0)[0], data.get(1), row);
                            count++;
                            //commit каждые 10000 записей
//                            if ((ii / 10000) == count) {
//                                ct.commit();
//                                count++;
//                            }
                        }
                        // коммит последних записей
                     //   ct.commit();
                    }
                }




        ct.close();
    }
}
