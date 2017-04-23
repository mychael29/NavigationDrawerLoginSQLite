package com.michael.jared.navigationdrawerloginsqlite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.michael.jared.navigationdrawerloginsqlite.database.DatabaseManagerUser;
import com.michael.jared.navigationdrawerloginsqlite.entity.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseManagerUser databaseManagerUser;
    private User itemUsuario;
    private String ident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //se agrego codigo del 39 al 68
        Bundle b = getIntent().getExtras();

        ident = b.getString("IDENT");

        databaseManagerUser= new DatabaseManagerUser(getApplicationContext());
        itemUsuario = databaseManagerUser.getUsuario(ident); // encuentra al usuario registrado en la bbdd

        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        ((TextView) header.findViewById(R.id.tv_nombre_usuario_menu)).setText(itemUsuario.getNombre());
        ((TextView) header.findViewById(R.id.tv_correo_menu)).setText(itemUsuario.getCorreo());

        Bitmap bitmapsinfoto = BitmapFactory.decodeResource(getResources(),R.drawable.imagen);
        RoundedBitmapDrawable roundedBitmapDrawablesinfoto = RoundedBitmapDrawableFactory.create(getResources(), bitmapsinfoto);
        roundedBitmapDrawablesinfoto.setCircular(true);

        ((ImageView) header.findViewById(R.id.imageView)).setImageDrawable(roundedBitmapDrawablesinfoto);

        if(itemUsuario.getBytes()!=null){
            byte[] foodImage = itemUsuario.getBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);

            ((ImageView) header.findViewById(R.id.imageView)).setImageBitmap(bitmap);

            Bitmap bitmap2 = ((BitmapDrawable)((ImageView) header.findViewById(R.id.imageView)).getDrawable()).getBitmap();
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap2);
            roundedBitmapDrawable.setCircular(true);

            ((ImageView) header.findViewById(R.id.imageView)).setImageDrawable(roundedBitmapDrawable);
        }


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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
