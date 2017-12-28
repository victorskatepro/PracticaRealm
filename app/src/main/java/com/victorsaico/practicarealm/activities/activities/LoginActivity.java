package com.victorsaico.practicarealm.activities.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {
    private EditText edtCorreo;
    private EditText edtpassword;
    Realm realm = Realm.getDefaultInstance();
    private SharedPreferences sharedPreferences;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hideStatusBar(this);
        edtCorreo = (EditText) findViewById(R.id.edtcorreo);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        initShared();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void goRegistrar(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goEntrar(View view) {
        String correo = edtCorreo.getText().toString();
        String password = edtpassword.getText().toString();
        //validar el correo.
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean rptcorreo = pattern.matcher(correo).matches();

        if(correo.isEmpty())
        {
            edtCorreo.setError("Se olvido su correo :)");
        }
        else if(password.isEmpty())
        {
            edtpassword.setError("Se olvido su contraseña");
        }
        else if(rptcorreo == false)
        {
            edtCorreo.setError("Correo no valido");
        }
        Usuario usuario = realm.where(Usuario.class).equalTo("correo", correo).equalTo("contrasena", password).findFirst();
        RealmResults<Usuario> user = realm.where(Usuario.class).findAll();
        Log.d(TAG,"usuarios"+user);
        if(usuario == null )
        {
           edtCorreo.setError("Correo y/o password incorrecto");
        }else {
            Log.d(TAG, "usuario" + usuario);
            guardarShared(usuario);
            finish();
            goMain();
        }
    }
    public void guardarShared(Usuario usuario)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor
                .putString("nombre",usuario.getNombre())
                .putString("correo", usuario.getCorreo())
                .putString("imageprofile", usuario.getImagenprofile())
                .putString("telefono", String.valueOf(usuario.getTelefono()))
                .putInt("id", usuario.getId())
                .putString("empresa", usuario.getEmpresa())
                .putBoolean("islogged", true)
                .commit();
        goMain();
    }
    public void goMain()
    {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    }
    //Método para poder ocultar la barra de información.
    public static void hideStatusBar(Activity context)
    {

        if (Build.VERSION.SDK_INT < 16) {
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = context.getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    //Método para validar la sesion
    public void initShared()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("islogged", false)){
            goMain();
        }
    }
}
