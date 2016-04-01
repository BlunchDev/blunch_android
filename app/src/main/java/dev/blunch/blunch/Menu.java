package dev.blunch.blunch;

import java.sql.Date;

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
}
