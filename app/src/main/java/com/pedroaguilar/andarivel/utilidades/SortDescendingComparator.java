package com.pedroaguilar.andarivel.utilidades;

import java.util.Comparator;

/**
 * Comparador personalizado, que compara Strings con orden Descendente.
 * Se usa para dotar de criterio de ordenación el TreeMap.
 * Invertimos todos los signos por defecto del metodo compare de string, para hacer que el metodo sea descendente.
 */
public class SortDescendingComparator implements Comparator<String> {
    /**
     * Metodo que indica la manera de comparar los parametros Strings
     * @param s1: primer argumento
     * @param s2: segundo argumento
     * @return 0: igualdad, -1: primer argumento mayor y 1: segudno argumento mayor
     */
    @Override
    public int compare(String s1, String s2) {
        if (s1.length() == s2.length()) {
            return (-1) * s1.compareTo(s2);
        }
        if (s1.length() > s2.length()) {
            return -1;
        } else {
            return 1;
        }
    }
}