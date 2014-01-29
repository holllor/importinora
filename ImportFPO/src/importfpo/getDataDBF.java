/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfpo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Vector;
import org.xBaseJ.DBF;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.DateField;
//import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.MemoField;
import org.xBaseJ.fields.NumField;

/**
 *
 * @author evstratovskijos
 */
public class getDataDBF {

    /**
     * Структура таблицы,
     * возвращает четыре массива:
     * 1 - 2 элемента, в первом название таблицы(использовать только его) в 2ом разрешение дбф
     * 2 - название столбцов
     * 3 - типы столбцов
     * 4 - размеры столбцов
     * @param path путь к файлу
     * @param file имя файла дбф
     * @return вектор
     */
    public ArrayList getDbfField(String path, String file) {

        try {
            //exist  dbf file
            //  System.out.println("" + path + "\\" + file + "");
            DBF dbfFile = new DBF("" + path + "\\" + file + "", "cp866");
            // +2 доплнительных поля для кода ррэс и фэс
            int index = dbfFile.getFieldCount() + 2;
            //   System.out.println(index);
            String[] nameField = new String[index];
            String[] typeField = new String[index];
            String[] sizeField = new String[index];
            String[] decimalSizeField = new String[index];
            //  String[] typField = new String[index];
//получаем название всех полей в файле
            // +2 доплнительных поля для кода ррэс и фэс
            for (int i = 1; i <= dbfFile.getFieldCount() + 2; i++) {
                if (i <= dbfFile.getFieldCount()) {
                    //название поля
                    nameField[i - 1] = dbfFile.getField(i).getName().trim();
                    if (nameField[i - 1].equalsIgnoreCase("comment")) {
                        nameField[i - 1] = "coment";
                    }
                    // тип поля
                    typeField[i - 1] = String.valueOf(dbfFile.getField(i).getType());
                    //size field
                    sizeField[i - 1] = String.valueOf(dbfFile.getField(i).getLength());
                    decimalSizeField[i - 1] = String.valueOf(dbfFile.getField(i).getDecimalPositionCount());
                } else if (i == dbfFile.getFieldCount() + 1) {
                    nameField[i - 1] = "kodfes";
                    // тип поля
                    typeField[i - 1] = "N";
                    //size field
                    sizeField[i - 1] = "3";
                    decimalSizeField[i - 1] = "0";
                } else if (i == dbfFile.getFieldCount() + 2) {
                    nameField[i - 1] = "kodres";
                    // тип поля
                    typeField[i - 1] = "N";
                    //size field
                    sizeField[i - 1] = "3";
                    decimalSizeField[i - 1] = "0";
                }
            }

            for (int i = 0; i < sizeField.length; i++) {

                if (!decimalSizeField[i].trim().equals("0")) {
                    // необходимо сложение до запятой и после запятой чтобы учесть размер числового поля для оракла
                    try {
                        int one = Integer.parseInt(sizeField[i].trim());
                        int two = Integer.parseInt(decimalSizeField[i].trim());
                        sizeField[i] = String.valueOf(one + two);
                    } catch (Exception e) {
                      //  e.printStackTrace();
                    }

                    sizeField[i] = sizeField[i] + "," + decimalSizeField[i];
                    //System.out.println("size decimal = "+sizeField[i]);

                }

            }

            dbfFile.close();

            String[] nameFile = file.split("\\.");
            //    System.out.println("nameFile " + nameFile[0]);
            ArrayList data = new ArrayList();
            data.add(nameFile);
            data.add(nameField);
            data.add(typeField);
            data.add(sizeField);
            return data;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * метод в строке заменяет одинарную кавычку на две одинарные кавычки
     * @param value строка
     * @return изменненая строка
     */
    private String odinKavich(String value) {

        value = value.replaceAll("'", "''");
        return value;
    }

    /**
     * возвращает данные из таблицы в виде вектора содержащего стринговые векторы строк
     * @param path путь к файлу
     * @param file имя файла
     * @return содержимое файла
     */
    public ArrayList getDbfData(String path, String file, String kodFes, String kodRes) {
        try {
            //exist  dbf file
            DBF dbfFile = new DBF("" + path + "\\" + file + "", "cp866");
            // +2 доплнительных поля для кода ррэс и фэс
            int index = dbfFile.getFieldCount() + 2;
            //   System.out.println(index);
            String[] nameField = new String[index];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");
            //получаем название всех полей в файле
            //+2....дополнительные поля для кода fes and res
            for (int i = 1; i <= dbfFile.getFieldCount() + 2; i++) {
                if (i <= dbfFile.getFieldCount()) {
                    //название поля
                    nameField[i - 1] = dbfFile.getField(i).getName().trim();
                } else if (i == dbfFile.getFieldCount() + 1) {
                    nameField[i - 1] = "kodfes";

                } else if (i == dbfFile.getFieldCount() + 2) {
                    nameField[i - 1] = "kodres";

                }
            }
            ArrayList data = new ArrayList();
            for (int i = 1; i <= dbfFile.getRecordCount(); i++) {
                dbfFile.read();
                ArrayList<String> row = new ArrayList<String>();
                //перебираются все поля в строке
                for (int j = 0; j < nameField.length; j++) {
                    //+2....дополнительные поля для кода fes and res

                    if (j < nameField.length - 2) {
                        switch (dbfFile.getField(nameField[j]).getType()) {
                            case 'N':
                                NumField field1 = (NumField) dbfFile.getField(nameField[j]);

                               if(field1.get().trim().equals("*") || field1.get().trim().equals("**") ||
                                        field1.get().trim().equals("***") || field1.get().trim().equals("****")){
                                   System.out.println("nomer filed "+j+" nomer row "+i+" значение "+field1.get());
                                    row.add("0");
                               }else if (field1.get().trim().equals("")) {
                                 //   System.out.println(field1.get().trim()+" замена пустого значения на 0");
                                    row.add("0");
                                }

                                else {
                                    row.add(field1.get().trim());

                                }
                                break;
                            case 'D':
                                DateField field2 = (DateField) dbfFile.getField(nameField[j]);
                                String val2 = field2.get().trim();
                                if (!val2.trim().equals("")) {
                                    try {
                                        Date dat = sdf.parse(val2);
                                        val2 = sdf2.format(dat);
                                    } catch (Exception e) {
                                  //      e.printStackTrace();
                                    }


                                } else {
                                    val2 = null;
                                    row.add(val2);
                                    break;
                                }
                                val2 = "'" + val2 + "'";
                                // System.out.println("data = " + val2);
                                row.add(val2);
                                break;
                            case 'C':
                                CharField field3 = (CharField) dbfFile.getField(nameField[j]);
                                String replase = odinKavich(field3.get().trim()); // это для экранирования одинарной кавычки в строке
                                String val3 = "'" + replase + "'";

                                row.add(val3);
                                break;
                            case 'L':
                                LogicalField field4 = (LogicalField) dbfFile.getField(nameField[j]);
                                String val4 = "'" + field4.get().trim() + "'";
                                row.add(val4);
                                break;
                            case 'F':
                                FloatField field6 = (FloatField) dbfFile.getField(nameField[j]);
                                String val6 = "'" + field6.get().trim() + "'";
                                row.add(val6);

                                System.out.println("XYI");

                                break;
                            case 'M':
                                MemoField field5 = (MemoField) dbfFile.getField(nameField[j]);
                                String val5 = "'" + field5.get().trim() + "'";
                                row.add(val5);
                                break;
                        }
                    } else if (j < nameField.length - 1) {
                        //  System.out.println("kodfes");
                        row.add(kodFes);

                    } else if (j < nameField.length) {
                        // System.out.println("kodres");
                        row.add(kodRes);

                    }
                }



                data.add(row);
                // row.clear();
            }

            dbfFile.close();

            return data;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws SQLException {
        //  Vector<String[]> v = new getDataDBF().getDbfData("c:\\fposerv\\fpo\\1\\1", "sobbit.dbf");
        getDataDBF g = new getDataDBF();
        ArrayList<ArrayList<String>> v = g.getDbfData("c:\\FOXPRO\\DBFS\\forms", "form1.dbf", "1", "2");
        for (int i = 320; i < 321; i++) {
            for (int j = 150; j < 151; j++) {
                System.out.print(v.get(i).get(j) + " ");

            }
            System.out.println("==========================");
        }
////
////        CreateTable ct = new CreateTable();
////        getDataDBF get = new getDataDBF();
////        ct.init();
////        //получение структуры таблицы дбф
////        Vector<String[]> data = get.getDbfField("c:\\fposerv\\fpo\\2\\1", "sobbit.DBF");
////        //извлечение данных из таблицы дбф
////        Vector<Vector> value = get.getDbfData("c:\\fposerv\\fpo\\2\\1", "sobbit.DBF", "2", "1");
////        //создание аналога в оракл
////                ct.creatTable(data.get(0)[0], data.get(1), data.get(2), data.get(3));
////        // инсерт извлеченных данных во вновь созданную таблицу
////        ct.setAutoCommit(false);
////        int count = 1;
////        for (int i = 0; i < value.size(); i++) {
////            Vector row = value.get(i);
////            //  System.out.println("row "+row);
////            ct.insertRow(data.get(0)[0], data.get(1), row);
////            //commit каждые 10000 записей
////            if ((i / 10000) == count) {
////                ct.commit();
////                count++;
////            }
////        }
////        // коммит последних записей
////        ct.commit();
////        ct.close();


    }
}
