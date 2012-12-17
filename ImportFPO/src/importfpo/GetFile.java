/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfpo;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author evstratovskijos
 */

public class GetFile {

//    public GetFile() {
//    }
    private String path = "";

    /**
     * указываешь путь к базе ФПО
     * @param path путь к базе
     */
    public GetFile(String path) {
        this.path = path;
    }

    /**
     * возвращает список фес в указанной папке
     * @return массив
     */
    public String[] getAllFes() {
        File file = new File(path);
        FilenameFilter ff = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.equalsIgnoreCase("b") || name.equalsIgnoreCase("g")
                        || name.equalsIgnoreCase("1") || name.equalsIgnoreCase("2")
                        || name.equalsIgnoreCase("3") || name.equalsIgnoreCase("4")
                        || name.equalsIgnoreCase("5") || name.equalsIgnoreCase("6")
                        || name.equalsIgnoreCase("7") || name.equalsIgnoreCase("8")
                        || name.equalsIgnoreCase("9") || name.equalsIgnoreCase("10")
                        || name.equalsIgnoreCase("11") || name.equalsIgnoreCase("12")) {
                    return true;
                }
                return false;
            }
        };
        String[] spisok = file.list(ff);
        for (int i = 0; i < spisok.length; i++) {
            System.out.println("fes: " + spisok[i]);

        }
        return spisok;
    }

    /**
     * возвращает список ррэсов
     * @param fes фэс в котором ищатся ррэс
     * @return массив
     */
    public String[] getRes(String fes) {
        File file = null;
        if (!fes.trim().equalsIgnoreCase("b") && !fes.trim().equalsIgnoreCase("g")) {
            file = new File(path + "\\" + fes + "");
        } else {
            file = new File(path + "\\" + fes + "\\7");

        }

        FilenameFilter ff = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.equalsIgnoreCase("1") || name.equalsIgnoreCase("2")
                        || name.equalsIgnoreCase("3") || name.equalsIgnoreCase("4")
                        || name.equalsIgnoreCase("5") || name.equalsIgnoreCase("6")
                        || name.equalsIgnoreCase("7") || name.equalsIgnoreCase("8")
                        || name.equalsIgnoreCase("9") || name.equalsIgnoreCase("10")
                        || name.equalsIgnoreCase("11") || name.equalsIgnoreCase("12")) {
                    return true;
                }
                return false;
            }
        };
        String[] spisok = file.list(ff);
//        for (int i = 0; i < spisok.length; i++) {
//            System.out.println("res " + spisok[i]);
//
//        }
        return spisok;
    }
/**
 * Возвращает список файлов в папке с базой РРЭС
 * @param fes номер фэс
 * @param res номер РРэс
 * @return массив
 */
    public String[] getDbfFile(String fes, String res) {
        File file = null; //= new File(path + "\\" + fes + "\\" + res);
        if (!fes.trim().equalsIgnoreCase("b") && !fes.trim().equalsIgnoreCase("g")) {
            file = new File(path + "\\" + fes + "\\"+res);
           //System.out.println("ttt");
        } else {
            file = new File(path + "\\" + fes + "\\7\\"+res);
            
        }
       // System.out.println(fes+" "+res);
        FilenameFilter ff = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                String[] dbf = name.split("\\.");
                if (dbf.length < 2) {
                    return false;
                }
                // System.out.println(dbf.length+" dbf "+dbf[1]);
                if (dbf[1].equalsIgnoreCase("dbf")) {
                    if (dbf[0].equalsIgnoreCase("AACTION")
                            || dbf[0].equalsIgnoreCase("ACTION") || dbf[0].equalsIgnoreCase("ENTERH")
                            || dbf[0].equalsIgnoreCase("Operator") || dbf[0].equalsIgnoreCase("PMarsh")
                            || dbf[0].equalsIgnoreCase("RBMMGGGG") || dbf[0].equalsIgnoreCase("RGMMGGGG")
                            || dbf[0].equalsIgnoreCase("setup")|| dbf[0].equalsIgnoreCase("FOXUSER")
                            || dbf[0].equalsIgnoreCase("KLADREF")) {

                        return false;
                    }
                    return true;
                }




                return false;

            }
        };

        String[] spisok = file.list(ff);
//        for (int i = 0; i < spisok.length; i++) {
//            System.out.println("file: " + spisok[i]);
//
//        }
        return spisok;
    }

    /**
 * Возвращает список файлов в папке с базой 
 * @return массив
 */
    public String[] getDbfFile() {
        File file = new File(path); //= new File(path + "\\" + fes + "\\" + res);
//        if (!fes.trim().equalsIgnoreCase("b") && !fes.trim().equalsIgnoreCase("g")) {
//            file = new File(path + "\\" + fes + "\\"+res);
//           //System.out.println("ttt");
//        } else {
//            file = new File(path + "\\" + fes + "\\7\\"+res);
//
//        }
       // System.out.println(fes+" "+res);
        FilenameFilter ff = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                String[] dbf = name.split("\\.");
                if (dbf.length < 2) {
                    return false;
                }
                // System.out.println(dbf.length+" dbf "+dbf[1]);
                if (dbf[1].equalsIgnoreCase("dbf")) {
                    if (dbf[0].equalsIgnoreCase("AACTION")
                            || dbf[0].equalsIgnoreCase("ACTION") || dbf[0].equalsIgnoreCase("ENTERH")
                            || dbf[0].equalsIgnoreCase("Operator") || dbf[0].equalsIgnoreCase("PMarsh")
                            || dbf[0].equalsIgnoreCase("RBMMGGGG") || dbf[0].equalsIgnoreCase("RGMMGGGG")
                            || dbf[0].equalsIgnoreCase("setup")|| dbf[0].equalsIgnoreCase("FOXUSER")
                            || dbf[0].equalsIgnoreCase("KLADREF")) {

                        return false;
                    }
                    return true;
                }




                return false;

            }
        };

        String[] spisok = file.list(ff);
//        for (int i = 0; i < spisok.length; i++) {
//            System.out.println("file: " + spisok[i]);
//
//        }
        return spisok;
    }

    public static void main(String[] args) {

        GetFile gf = new GetFile("c:\\fposerv\\fpo");
        gf.getAllFes();
    }
}
