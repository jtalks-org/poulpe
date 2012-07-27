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
    private static List<Character> ctrlSymbols = new ArrayList<Character>();

    static {
        ctrlSymbols.add('%');//anything
        ctrlSymbols.add('_');//anyone
        ctrlSymbols.add('!');//not
        ctrlSymbols.add('^');//not
        ctrlSymbols.add('[');//array_start
        ctrlSymbols.add(']');//array_finish

    }

    public static String escapeCtrlCharacters(String s) {
        s=s.trim();
        StringBuffer sbuf = new StringBuffer( );
        int i=0;
            while (i<s.length()){
                for (Character c : ctrlSymbols) {
                if (s.charAt(i)==c){
                    sbuf.append("\\");
                }
            }
               sbuf.append(s.charAt(i));
               i++;
        }
        return sbuf.toString();
    }

    /**
     *
     * @param newSymbols
     *
     */
    public static void setCtrlSymbols2Escape(List<Character> newSymbols) {
        ctrlSymbols = newSymbols;
    }
}
