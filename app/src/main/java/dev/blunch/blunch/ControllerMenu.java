package dev.blunch.blunch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ControllerMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
