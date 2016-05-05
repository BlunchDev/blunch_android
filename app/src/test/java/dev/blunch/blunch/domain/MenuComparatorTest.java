package dev.blunch.blunch.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by albert on 5/05/16.
 */
public class MenuComparatorTest {

    MenuComparator menuComparator;
    Menu firstMenu, secondMenu;

    @Before
    public void before() {
        menuComparator = new MenuComparator();
        firstMenu = new CollaborativeMenu();
        secondMenu = new CollaborativeMenu();
    }

    @Test
    public void test() throws Exception {
        firstMenu.setDateStart(toDate(1, 1, 2016, 10, 0));
        secondMenu.setDateStart(toDate(1, 1, 2016, 10, 0));
        firstMenu.setDateStart(toDate(2, 1, 2016, 10, 0));
        assertTrue(menuComparator.compare(firstMenu, secondMenu) > 0);
        secondMenu.setDateStart(toDate(3, 1, 2016, 10, 0));
        assertTrue(menuComparator.compare(firstMenu, secondMenu) < 0);
    }

    private Date toDate(Integer day, Integer month, Integer year, Integer hour, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

}
