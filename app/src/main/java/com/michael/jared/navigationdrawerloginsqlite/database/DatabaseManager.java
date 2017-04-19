package com.michael.jared.navigationdrawerloginsqlite.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Maycol Meza on 15/04/2017.
 */

public abstract class DatabaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseManager(Context ctx){
        helper = new DatabaseHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public void cerrar(){
        db.close();
    }

    abstract public void eliminar(String id);
    abstract public void eliminarTodo();
    abstract public Cursor cargarCursor();
    abstract boolean comprobarRegistro(String id);

    public DatabaseHelper getHelper() {
        return helper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setHelper(DatabaseHelper helper) {
        this.helper = helper;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

}

