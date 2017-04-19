package com.michael.jared.navigationdrawerloginsqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.michael.jared.navigationdrawerloginsqlite.entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maycol Meza on 15/04/2017.
 */

public class DatabaseManagerUser extends DatabaseManager {

    private static final String NOMBRE_TABLA = "demo";
    private static final String CN_ID = "_id";
    private static final String CN_CORREO = "correo";
    private static final String CN_PASSWORD = "password";
    private static final String CN_IMAGE = "imagen";
    private static final String CN_NOMBRE = "nombre";

    public static final String CREATE_TABLE = "create table " + NOMBRE_TABLA + " ("
            + CN_ID + " integer PRIMARY KEY AUTOINCREMENT, "
            + CN_CORREO + " text NULL, "
            + CN_PASSWORD + " text NULL, "
            + CN_IMAGE + " BLOB NULL, "
            + CN_NOMBRE + " text NOT NULL "
            + ");";

    public DatabaseManagerUser(Context ctx) {
        super(ctx);
    }

    @Override
    public void cerrar() {
        super.getDb().close();
    }

    private ContentValues generarContentValues(String id, String correo, String password, byte[] imagen, String nombre){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID, id);
        valores.put(CN_CORREO, correo);
        valores.put(CN_PASSWORD, password);
        valores.put(CN_IMAGE, imagen);
        valores.put(CN_NOMBRE, nombre);

        return valores;
    }


    public void insertar_parametros(String id, String correo, String password, byte[] imagen, String nombre) {
        Log.d("usuario_insertar", super.getDb().insert(NOMBRE_TABLA,null,generarContentValues(id, correo, password, imagen, nombre))+"");
    }

    public void actualizar_parametros(String id, String correo, String pass, byte[] imagen, String nombre) {

        ContentValues valores = new ContentValues();
        valores.put(CN_ID, id);
        valores.put(CN_CORREO, correo);
        valores.put(CN_PASSWORD, pass);
        valores.put(CN_IMAGE, imagen);
        valores.put(CN_NOMBRE, nombre);

        String [] args = new String[]{id};

        Log.d("actualizar", super.getDb().update(NOMBRE_TABLA, valores,"_ID=?", args)+"");
    }

    @Override
    public void eliminar(String id) {

        super.getDb().delete(NOMBRE_TABLA, CN_ID +"=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {

        super.getDb().execSQL("DELETE FROM "+ NOMBRE_TABLA+";");
    }

    @Override
    public Cursor cargarCursor() {

        String [] columnas = new String[]{CN_ID, CN_CORREO, CN_PASSWORD, CN_IMAGE, CN_NOMBRE};

        return super.getDb().query(NOMBRE_TABLA, columnas, null, null, null, null, null);
    }

    @Override
    public boolean comprobarRegistro(String correo) {

        boolean esta;
        Cursor resultSet = super.getDb().rawQuery("SELECT correo FROM demo" + " WHERE correo='"+correo+"'", null);

        if(resultSet.getCount()<=0){
            esta = false;
        }else{
            esta = true;
        }
        return esta;
    }

    public List<User> getUsuariosList(){

        List<User> list = new ArrayList<>();
        Cursor c = cargarCursor();

        while (c.moveToNext()){
            User usuario = new User();

            usuario.setId(c.getString(0));
            usuario.setCorreo(c.getString(1));
            usuario.setPassword(c.getString(2));
            usuario.setBytes(c.getBlob(3));
            usuario.setNombre(c.getString(4));
            //usuario.setActive(false);

            list.add(usuario);
        }

        return list;
    }

    public User getUsuario(String ident){

        Cursor c1 = super.getDb().rawQuery("SELECT _id, correo, password, imagen, nombre FROM demo WHERE correo" + "='" + ident+ "'", null);

        User user = new User();

        c1.moveToNext();

        user.setId(c1.getString(0));
        user.setCorreo(c1.getString(1));
        user.setPassword(c1.getString(2));
        user.setBytes(c1.getBlob(3));
        user.setNombre(c1.getString(4));
        return user;
    }
}
