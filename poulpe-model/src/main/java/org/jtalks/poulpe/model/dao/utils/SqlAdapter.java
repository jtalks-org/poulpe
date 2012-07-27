package org.jtalks.poulpe.model.dao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anton
 * Date: 27.07.12
 * Time: 3:58
 * To change this template use File | Settings | File Templates.
 */
public class SqlAdapter {
    private static List<String> ctrlSymbols = new ArrayList<String>();

    public SqlAdapter(){
        ctrlSymbols.add("%");//anything
        ctrlSymbols.add("_");//anyone
        ctrlSymbols.add("!");//not
        ctrlSymbols.add("^");//not
        ctrlSymbols.add("[");//array_start
        ctrlSymbols.add("]");//array_finish
    }

    public static String escapeCtrlCharacters(String s) {
        for (String c : ctrlSymbols) {
            s.replace(c, "\\" + c);
        }
        return s;
    }

    public static void setCtrlSymbols2Escape(List<String> newSymbols) {
        ctrlSymbols = newSymbols;
    }
}
