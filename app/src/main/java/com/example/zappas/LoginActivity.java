package com.example.zappas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText etLoginEmail,etLoginPass;
    private Button btnLoginRegistro,btnLoginAcede;

    private String emilio;
    private String contrasenia;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = (EditText) findViewById(R.id.etEmailLogin);
        etLoginPass = (EditText) findViewById(R.id.etContraseniaLogin);
        btnLoginRegistro = (Button) findViewById(R.id.btnRegistroLogin);
        btnLoginAcede = (Button) findViewById(R.id.btnAccedeLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLoginRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emilio = etLoginEmail.getText().toString();
                contrasenia = etLoginPass.getText().toString();
                if( !emilio.isEmpty() && !contrasenia.isEmpty()){

                    if(contrasenia.length() >= 6){

                        registrarUsuario();

                    }else{
                        Toast.makeText(LoginActivity.this, "La contraseña tiene que tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Debes rellenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void registrarUsuario() {
        mAuth.createUserWithEmailAndPassword(emilio,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //validamos si la tarea (task) fue exitosa osea si el registro del usuario se realizo correctamente
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    Map<String, Object> map = new HashMap<>();
                    map.put("email", emilio);
                    map.put("contrasenia", contrasenia);

                    String id = mAuth.getCurrentUser().getUid();

                }else {
                    Toast.makeText(LoginActivity.this, "No se pudo realizar el registro correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}