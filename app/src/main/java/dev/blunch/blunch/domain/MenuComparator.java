package dev.blunch.blunch.domain;

import java.util.Comparator;

/**
 * Created by jmotger on 26/04/16.
 */
public class MenuComparator implements Comparator<Menu> {
    @Override
    public int compare(Menu lhs, Menu rhs) {
        return lhs.getDateStart().compareTo(rhs.getDateStart());
    }
}
