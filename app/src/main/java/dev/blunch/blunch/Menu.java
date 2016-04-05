package dev.blunch.blunch;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
//import java.util.Date;


/**
 * Created by Daniela Santos on 01-04-2016.
 */
public class Menu {

    private String nameAuth;
    private String type;
    private Date createDate;
    private String adress;
    private String nameMenu;

    public Menu(String nameAuth, String nameMenu, String type, Date createDate, String adress) {
        //if() el autor existe sino throw
            this.nameAuth = nameAuth;

        if(nameMenu != null)
            this.nameMenu = nameMenu;
        else
            throw new IllegalArgumentException("Falta el nombre del menu");

        if(VerifyType(type))
            this.type = type;
        else
            throw new IllegalArgumentException("tipo tiene que ser del tipo de pago o colaborativo");

        //if date of
            this.createDate = createDate;

        this.adress = adress;
    }

    public String getNameAuth() {
        return nameAuth;
    }

    public void setNameAuth(String nameAuth) {
        this.nameAuth = nameAuth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAdress() {
        return adress;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public boolean VerifyType(String type){
        return (type == "pay" || type == "colaborative");
    }

    /*public static boolean isToday(Date date) {
        if( date == null)
            throw new IllegalArgumentException("la Data no puede ser null");
        return date.equals(Calendar.getInstance().getTime());
    }*/
}
