package com.example.zappas;

import com.google.firebase.database.FirebaseDatabase;

//le ponemos que herede de android.app.Application
public class Persistencia extends android.app.Application {
//creamos su metodo onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        //instanciamos la persistencia de datos en la app
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
