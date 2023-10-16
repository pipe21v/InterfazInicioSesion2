package com.example.interfazusuario;

import android.content.ContentValues;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;
    private DatabaseHelper dbHelper;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbHelper = new DatabaseHelper(this);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(isValidInput(name, email, password)){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_NAME, name);
                    values.put(DatabaseHelper.COLUMN_EMAIL, email);
                    values.put(DatabaseHelper.COLUMN_PASSWORD, password);

                    long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);
                    db.close();

                    if (newRowId != -1) {
                        // Registro exitoso
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Redirige al usuario a la actividad de inicio de sesión
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Error en el registro
                        Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Agregar un ClickListener al botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidInput(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
