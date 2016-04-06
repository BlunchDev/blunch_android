package dev.blunch.blunch.domain;

import android.util.Pair;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmotger on 5/04/16.
 */
public class PaymentMenu extends Menu {

    //A list of the dishes and the respective prices
    //private List<Pair<Dish, Double>> dishes;
    private List<Dish> dishes;
    private Date dateStart, dateEnd;

    public PaymentMenu() {}

    public PaymentMenu(String id, String name, String author, String description, String localization,
                             Date dateStart, Date dateEnd, List<Dish> dishes) {
        super(name, author, description, localization, dateStart, dateEnd);

        if(dishes == null)
            throw new IllegalArgumentException("El menu tiene que tener por lo menos un plato");
        this.dishes = dishes;

        if(dateEnd.before(dateStart))
            throw new IllegalArgumentException("The date its invalid");
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;


    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish dish) {
        if(!checkPrice(dish.getPrice()))
            throw new IllegalArgumentException("El precio tiene que ser superior a 0");

        else if(dishes.contains(dish)) {
            throw new IllegalArgumentException("no se puede colocar dos platos iguales en el mismo menu");
        }else
            this.dishes.add(dish);
    }
    //verfica se el precio es valido es decir se no es negativo o 0.
    private boolean checkPrice(double dishPrice){
        return (dishPrice > 0);
    }

    //Lista todos los menus de pago falta verificar se la lista esta vazia
   /* public Menu[] showAllPaysMenus(Menu[] listMenus){
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
    }*/

//Hice un cambio aqui
    private double totalPrice(List<Dish> dishes) {
        if(dishes.isEmpty())
            throw new IllegalArgumentException("Debe existir por lo menos un plato");
        double sum = 0;
        sum  += dishes.listIterator().next().getPrice();
        return sum;
    }

}
