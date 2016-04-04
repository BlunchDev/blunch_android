package dev.blunch.blunch;

import java.sql.Date;

/**
 * Created by Daniela Santos on 01-04-2016.
 */
public class payMenu  extends Menu{

    private float dishPrice;
    private Dish[] listDishs;

    public payMenu(String nameAuth, String nameMenu, String type, Date createDate, String local) {
        super(nameAuth, nameMenu, type, createDate, local);
        if(checkPrice(dishPrice)){
            this.dishPrice = dishPrice;
        }
    }

    private boolean checkPrice(float dishPrice){
        return (dishPrice <= 0); //se el precio no es valido retuno false
    }

    private float totalPrice(Dish[] listDishs){
        int sum = 0;
        for(int i = 0; i < listDishs.length; i++ ){
            sum += listDishs[i].getPrice();
        }
        return sum;
    }
}
