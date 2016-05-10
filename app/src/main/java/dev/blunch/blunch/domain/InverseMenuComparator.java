package dev.blunch.blunch.domain;

import java.util.Comparator;

/**
 * Created by Eugenio on 05/05/2016.
 */
public class InverseMenuComparator implements Comparator<Menu> {
    @Override
    public int compare(Menu lhs, Menu rhs) {
        return rhs.getDateStart().compareTo(lhs.getDateStart());
    }
}
