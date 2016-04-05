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
    private String local;
    private String nameMenu;

    public Menu(String nameAuth, String nameMenu, String type, Date createDate, String local) {
        this.nameAuth = nameAuth;
        this.nameMenu = nameMenu;
        this.type = type;

        //if date of
            this.createDate = createDate;
        this.local = local;
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

    public String getLocal() {
        return local;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    boolean VerifiyType(String type){
        return (type == "pay" || type == "colaborative");
    }

    /*Menu ShowMenuByType(String type){
        if(type == "pay")
            return showAllPaysMenus();
        else if(type == "colaborative")
            return showAllcolaborativeMenus();
    }*/

    //permite borrar un menu
    public void deleteMenu(){
      /*  if(){

        }else
            //hace un excepcion
        */

    }

    //permite add nuevos platos al menu
    public void AddDish(Dish dish){

    }
    //permite cambiar datos del menu como el local por ej
    public void ChangeDataMenu(String data){

    }

    /*public static boolean isToday(Date date) {
        if( date == null)
            throw new IllegalArgumentException("la Data no puede ser null");
        return date.equals(Calendar.getInstance().getTime());
    }*/
}
