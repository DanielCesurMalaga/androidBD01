package com.cesur.appbd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText nombreAlumno;
    private EditText apellidoAlumno;
    private Button insertarAlumno;
    private Button borrarAlumno;
    private Spinner spinnerAlumnos;
    private SQLiteDatabase alumnosBD;
    private List<String> listaAlumnos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreAlumno = (EditText) findViewById(R.id.nombreAlumno);
        apellidoAlumno = (EditText) findViewById(R.id.apellidoAlumno);
        insertarAlumno = (Button) findViewById(R.id.insertarAlumno);
        spinnerAlumnos = (Spinner) findViewById(R.id.spinnerAlumnos);
        borrarAlumno = (Button) findViewById(R.id.borrarAlumno);

        listaAlumnos = new ArrayList<String>();
        listaAlumnos = listarAlumnos();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaAlumnos);
        spinnerAlumnos.setAdapter(adapter);

        insertarAlumno.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alumnosBD = openOrCreateDatabase("alumnos", Context.MODE_PRIVATE,null);
                        alumnosBD.execSQL("CREATE TABLE IF NOT EXISTS alumno (NOMBRE VARCHAR, APELLIDO VARCHAR)");
                        if (camposCorrectos()){
                            alumnosBD.execSQL("INSERT INTO alumno VALUES ('"+
                                    nombreAlumno.getText().toString()+"','"+
                                    apellidoAlumno.getText().toString()+"')"
                            );
                            adapter.clear();
                            adapter.addAll(listarAlumnos());
                            nombreAlumno.setText("Nombre");
                            apellidoAlumno.setText("Apellido");
                        }
                        alumnosBD.close();
                    }
                }
        );

        borrarAlumno.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alumnoSeleccionado = (String) spinnerAlumnos.getSelectedItem();
                        alumnoSeleccionado = alumnoSeleccionado.substring(0,alumnoSeleccionado.indexOf('-'));
                        alumnosBD = openOrCreateDatabase("alumnos", Context.MODE_PRIVATE,null);
                        alumnosBD.execSQL("DELETE FROM alumno WHERE nombre='"+alumnoSeleccionado+"'");
                        adapter.clear();
                        adapter.addAll(listarAlumnos());

                        alumnosBD.close();
                    }
                }
        );

    }

    private boolean camposCorrectos(){
        boolean correcto=true;
        if (nombreAlumno.getText().toString().equals("Nombre")){
            nombreAlumno.setBackgroundColor(Color.MAGENTA);
            correcto = false;
        } else {
            nombreAlumno.setBackgroundColor(Color.WHITE);
        }
        if (apellidoAlumno.getText().toString().equals("Apellido")){
            apellidoAlumno.setBackgroundColor(Color.MAGENTA);
            correcto = false;
        } else {
            apellidoAlumno.setBackgroundColor(Color.WHITE);
        }
        return correcto;
    }

    private List<String> listarAlumnos(){
        alumnosBD = openOrCreateDatabase("alumnos", Context.MODE_PRIVATE,null);
        List<String> listaAlumnos = new ArrayList<String>();
        Cursor cursor = alumnosBD.rawQuery("SELECT * FROM alumno",null);
        while (cursor.moveToNext()){
            listaAlumnos.add(cursor.getString(0)+"-"+cursor.getString(1));
        }
        cursor.close();
        alumnosBD.close();
        return listaAlumnos;
    }
}