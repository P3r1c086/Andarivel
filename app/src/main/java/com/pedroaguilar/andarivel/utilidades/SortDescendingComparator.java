package com.pedroaguilar.andarivel.utilidades;

import java.util.Comparator;

/**
 * Comparador personalizado, que compara Strings con orden Descendente.
 * Se usa para dotar de criterio de ordenación el TreeMap.
 */
public class SortDescendingComparator implements Comparator<String> {
    /**
     * Metodo que indica la manera de comparar los parametros Strings
     * @param s1: primer argumento
     * @param s2: segundo argumento
     * @return 0: igualdad, 1: primer argumento mayor y -1: segudno argumento mayor todo: esto no seria al reves?
     */
    @Override
    public int compare(String s1, String s2) {
        if (s1.length() == s2.length()) {
            return (-1) * s1.compareTo(s2); //todo:  si es igual a cero, ¿por que se le multiplica por -1?
        }
        if (s1.length() > s2.length()) {
            return -1;
        } else {
            return 1;
        }
    }
}
