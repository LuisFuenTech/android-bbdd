package com.example.luisfuentes.basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText codigo, descripcion, precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codigo = (EditText) findViewById(R.id.codigoText);
        descripcion = (EditText) findViewById(R.id.descripcionText);
        precio = (EditText) findViewById(R.id.precioText);
    }

    public  void registrar(View view)
    {
        ConectorSQLite admin = new ConectorSQLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigoP = codigo.getText().toString(),
                descripcionP = descripcion.getText().toString(),
                precioP = precio.getText().toString();

        if(!codigoP.isEmpty() & !descripcionP.isEmpty() & !precioP.isEmpty())
        {
            if(!existe(admin, BaseDeDatos, codigoP))
            {
                ContentValues registro = new ContentValues();
                registro.put("codigo", codigoP);
                registro.put("descripcion", descripcionP);
                registro.put("precio", precioP);

                BaseDeDatos.insert("articulos", null, registro);
                BaseDeDatos.close();

                codigo.setText(null);
                descripcion.setText(null);
                precio.setText(null);

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "El producto ya existe", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Llene los datos", Toast.LENGTH_SHORT).show();
    }

    public void buscar(View view)
    {
        ConectorSQLite admin = new ConectorSQLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getReadableDatabase();

        String codigoP = codigo.getText().toString();

        if(!codigoP.isEmpty())
        {
            Cursor fila = BaseDeDatos.rawQuery
                    ("select descripcion, precio from articulos where codigo="+codigoP, null);

            if(fila.moveToFirst())
            {
                descripcion.setText(fila.getString(0));
                precio.setText(fila.getString(1));
                BaseDeDatos.close();
            }
            else
                Toast.makeText(this, "El producto no existe", Toast.LENGTH_SHORT).show();
            BaseDeDatos.close();
        }
        else
            Toast.makeText(this, "Debes introducir el código", Toast.LENGTH_SHORT).show();
    }

    public boolean existe(ConectorSQLite conexion, SQLiteDatabase BBDD, String codigoP)
    {
        Cursor fila = BBDD.rawQuery
                ("select descripcion, precio from articulos where codigo="+codigoP, null);

        if(fila.moveToFirst())
            return true;
        else
            return false;
    }

    public  void eliminar(View view)
    {
        ConectorSQLite admin = new ConectorSQLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codidoP = codigo.getText().toString();

        if(!codidoP.isEmpty())
        {
            if(existe(admin, BaseDeDatos, codidoP))
            {
                int cantidad = BaseDeDatos.delete("articulos", "codigo="+codidoP, null);
                BaseDeDatos.close();

                codigo.setText(null);
                descripcion.setText(null);
                precio.setText(null);

                if(cantidad != 0)
                    Toast.makeText(this, "Artículo borrado exitosamente", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Artículo no existe", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Artículo no existe", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Ingrese el código", Toast.LENGTH_SHORT).show();
    }

    public void modificar(View view)
    {
        ConectorSQLite admin = new ConectorSQLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigoP = codigo.getText().toString(),
                descripcionP = descripcion.getText().toString(),
                precioP = precio.getText().toString();

        if(!codigoP.isEmpty() & !descripcionP.isEmpty() & !precioP.isEmpty())
        {
            if(existe(admin, BaseDeDatos, codigoP))
            {
                ContentValues registro = new ContentValues();
                registro.put("codigo",codigoP);
                registro.put("descripcion", descripcionP);
                registro.put("precio", precioP);

                int cantidad = BaseDeDatos.update("articulos", registro, "codigo="+codigoP, null);
                BaseDeDatos.close();

                if(cantidad != 0)
                    Toast.makeText(this, "Artículo modificado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Error al modificar", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "El artículo no existe. Regístrelo", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
    }

    public void limpiar(View view)
    {
        codigo.setText(null);
        descripcion.setText(null);
        precio.setText(null);
    }
}

