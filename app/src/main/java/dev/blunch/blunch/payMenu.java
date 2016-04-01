package dev.blunch.blunch;

import java.sql.Date;

/**
 * Created by Daniela Santos on 01-04-2016.
 */
public class payMenu  extends Menu{

    private float price;

    public payMenu(String nameAuth, String nameMenu, String type, Date createDate, String local) {
        super(nameAuth, nameMenu, type, createDate, local);
        if(checkPrice(price)){
            this.price = price;
        }
    }

    private boolean checkPrice(float price){
        return (price <= 0); //se el precio no es valido retuno false
    }

}
