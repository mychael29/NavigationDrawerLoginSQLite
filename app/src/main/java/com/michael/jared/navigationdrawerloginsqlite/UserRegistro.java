package com.michael.jared.navigationdrawerloginsqlite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.michael.jared.navigationdrawerloginsqlite.database.DatabaseManagerUser;
import java.io.ByteArrayOutputStream;

/**
 * Created by Maycol Meza on 15/04/2017.
 */

public class UserRegistro extends AppCompatActivity {

    private TextView loginLink;
    private ImageView imageView;
    private EditText password;
    private EditText nombre;
    private EditText email;
    private Button registrar;
    private DatabaseManagerUser managerUsuario;
    private String sPassword, sNombre, sEmail;
    private int request_code = 1;
    private Bitmap bitmap_foto;
    private RoundedBitmapDrawable roundedBitmapDrawable;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        imageView = (ImageView) findViewById(R.id.usuario_imagen_registro);
        loginLink = (TextView)findViewById(R.id.link_login);
        email = (EditText)findViewById(R.id.correo_registro);
        password = (EditText)findViewById(R.id.password_registro);
        nombre = (EditText)findViewById(R.id.nombre_registro);
        registrar = (Button)findViewById(R.id.btn_registro_usuario);
        bitmap_foto = BitmapFactory.decodeResource(getResources(),R.drawable.imagen);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap_foto);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                //verificacion de la version de plataforma
                if(Build.VERSION.SDK_INT < 19){
                    //android 4.3  y anteriores
                    i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                }else {
                    //android 4.4 y superior
                    i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                }
                i.setType("image/*");
                startActivityForResult(i, request_code);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void registrar(){

        if (!validar()) return;

        sEmail = email.getText().toString();
        sPassword = password.getText().toString();
        sNombre = nombre.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(UserRegistro.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        managerUsuario = new DatabaseManagerUser(this);

        email.getText().clear();
        password.getText().clear();
        nombre.getText().clear();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(managerUsuario.comprobarRegistro(sEmail)){
                            progressDialog.dismiss();
                            password.setText(sPassword);
                            nombre.setText(sNombre);
                            String mesg = String.format("El correo electrónico que has introducido ya se encuentra registrado", null);
                            Toast.makeText(getApplicationContext(),mesg, Toast.LENGTH_LONG).show();
                        }else {
                            managerUsuario.insertar_parametros(null, sEmail, sPassword, bytes, sNombre);
                            String mesg = String.format("%s ha sido guardado en la BBDD", sNombre);
                            Toast.makeText(getBaseContext(),mesg, Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("IDENT",sEmail);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }

    private boolean validar() {
        boolean valid = true;

        String sNombre = nombre.getText().toString();
        String sPassword = password.getText().toString();
        String sEmail = email.getText().toString();

        if (sNombre.isEmpty() || sNombre.length() < 3) {
            nombre.setError("Ingrese al menos 3 caracteres");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (sEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("Dirección de correo electrónico no válida");
            valid = false;
        } else {
            email.setError(null);
        }

        if (sPassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("Ingrese entre 4 a 10 caracteres alfanuméricos");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private byte[] imageToByte(ImageView image) {
        Bitmap bitmapFoto = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapFoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == request_code){
            imageView.setImageURI(data.getData());
            bytes = imageToByte(imageView);

            // para que se vea la imagen en circulo
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}


