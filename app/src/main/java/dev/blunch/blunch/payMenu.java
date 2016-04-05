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
        return (dishPrice <= 0); //se el precio no es valido retun false
    }

    private float totalPrice(Dish[] listDishs) {

        if(listDishs == null)
            throw new IllegalArgumentException("Debe existir por lo menos un plato");

        int sum = 0;
        for (int i = 0; i < listDishs.length; i++)
            sum += listDishs[i].getPrice();
        return sum;
    }

    /*public String showPrice(){
        return "The total price is" + totalPrice();
    }*/

   //Lista todos los menus de pago falta verificar se la lista esta vazia
    public Menu[] showAllPaysMenus(Menu[] listMenus){
        if(listMenus == null)
            throw new IllegalArgumentException("Debe existir por lo menos un menu");

        Menu AllPayMenus[] = null;
        int j = 0;
        for(int i = 0; i < listMenus.length; i++) {
            if (listMenus[i].getType() == "pay"){
                AllPayMenus[j] = listMenus[i];
                j++;
            }
        }
        if(AllPayMenus == null)
            throw new IllegalArgumentException("Debe existir por lo menos un menu de pago");

        return AllPayMenus;
   }
}
