package com.example.zappas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zappas.modelo.Zapatilla;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {//-----------------------------------------------------------------------
// aqui declaramos las variables
    private List<Zapatilla> zapatillaList = new ArrayList<Zapatilla>();
    ArrayAdapter<Zapatilla> zapatillaArrayAdapter;

    EditText nombreP, marcaP, colorP;
    ListView lvZapas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Zapatilla zapaSeleccionada;

//METODO ON CREATE---------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreP = (EditText) findViewById(R.id.etNombre);
        marcaP = (EditText) findViewById(R.id.etMarca);
        colorP = (EditText) findViewById(R.id.etColor);
        lvZapas = (ListView) findViewById(R.id.lvDatosZapas);
        // con este metodo iniciamos Firebase
        inicializarFirebase();
        // con este metodo listamos los datos
        listarDatos();
        // creamos este metodo para al pinchar en el item listado nos rellene los campos con sus atributos
        lvZapas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zapaSeleccionada = (Zapatilla) parent.getItemAtPosition(position);
                nombreP.setText(zapaSeleccionada.getNombre());
                marcaP.setText(zapaSeleccionada.getMarca());
                colorP.setText(zapaSeleccionada.getColor());
            }
        });

    }
//creamos este metodo para listar los datos-----------------------------------------------------------------------------------
    private void listarDatos() {
        databaseReference.child("Zapatilla").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 //le decimos que nos limpie la lista por si hay almacenado en cache por la persistencia
                  zapatillaList.clear();
              for (DataSnapshot objSnapshot : snapshot.getChildren()){
                Zapatilla z = objSnapshot.getValue(Zapatilla.class);
                zapatillaList.add(z);
                zapatillaArrayAdapter = new ArrayAdapter<Zapatilla>(MainActivity.this, android.R.layout.simple_list_item_1,zapatillaList);
                lvZapas.setAdapter(zapatillaArrayAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //creamos este metodo para iniciar Firebase con una instancia y una referencia-------------------------------------------
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // Creamos este metodo para implementar el menu en el layout main---------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //agregamos esta linea para vincular el menu que hicimos nuevo
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Metodo que gestiona los items del menu en base a su seleccion-----------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre = nombreP.getText().toString();
        String marca = marcaP.getText().toString();
        String color = colorP.getText().toString();
    // depende de que icono pinchamos asi hacemos
        switch (item.getItemId()){
            case R.id.iconAñadir:{
                if(nombre.equals("") || (marca.equals("") || (color.equals(""))) ){
                    validacion();
                }else{
                    //creamos un objeto zapatilla y enviamos los atributos
                    Zapatilla z = new Zapatilla();
                    z.setId(UUID.randomUUID().toString());// agregamos un ramdomidazor en el id
                    z.setNombre(nombre);
                    z.setMarca(marca);
                    z.setColor(color);
                    // con esto creamos la tabla zapatilla que contendra ids aleatorias de las diferentes zapas
                    databaseReference.child("Zapatilla").child(z.getId()).setValue(z);
                    Toast.makeText(this, "Agregado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.iconEditar:{
                Zapatilla z = new Zapatilla();
                z.setId(zapaSeleccionada.getId());//cogemos el id de la zapatilla seleccionada
                z.setNombre(nombreP.getText().toString().trim());
                z.setMarca(marcaP.getText().toString().trim());
                z.setColor(colorP.getText().toString().trim());
                //con esta llamada sobreescribimos el objeto seleccionado en la database
                databaseReference.child("Zapatilla").child(z.getId()).setValue(z);
                Toast.makeText(this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                //limpiamos al actualizar con nuestro metodo
                limpiarCajas();
                break;
            }
            case R.id.iconEliminar:{
                Zapatilla z = new Zapatilla();
                z.setId(zapaSeleccionada.getId());
                // con esta llamada eliminamos el objeto seleccionado
                databaseReference.child("Zapatilla").child(z.getId()).removeValue();
                Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    //Creamos un metodo para limpiar los campos de los editText--------------------------------------------------------------------
    private void limpiarCajas() {
        nombreP.setText("");
        marcaP.setText("");
        colorP.setText("");
    }

    //Creamos un metodo para validar------------------------------------------------------------------------------------------------
    private void validacion() {
        String nombre = nombreP.getText().toString();
        String marca = marcaP.getText().toString();
        String color = colorP.getText().toString();
        if(nombre.equals("")){
            nombreP.setError("Se necesita");
        }
        else if(marca.equals("")){
            marcaP.setError("Se necesita");
        }
        else if(color.equals("")){
            colorP.setError("Se necesita");
        }
    }
}