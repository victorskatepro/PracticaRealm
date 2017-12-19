package com.victorsaico.practicarealm.activities.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import io.realm.Realm;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtregisternombre,edtregistercorreo,edtregistertelefono,edtregisterempresa,edtregisterpassword,edtregisterpassword2;
    Realm realm = Realm.getDefaultInstance();
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String nombre,correo,telefono, empresa,password;
    private Uri mediaFileUri;
    private ImageView imgprofile;
    private static final int CAPTURE_IMAGE_REQUEST = 300;
    //<--!! //Registrar los permisos --!>
    private static final int PERMISSIONS_REQUEST = 200;

    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //iniciar el shared preferences

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        edtregisternombre = (EditText) findViewById(R.id.edtregisternombre);
        edtregistercorreo = (EditText) findViewById(R.id.edtregistercorreo);
        edtregistertelefono = (EditText) findViewById(R.id.edtregistertelefono);
        edtregisterempresa = (EditText) findViewById(R.id.edtregisterempresa);
        edtregisterpassword = (EditText) findViewById(R.id.edtregisterpassword);
        edtregisterpassword2 = (EditText) findViewById(R.id.edtregisterpassword2);
        imgprofile = (ImageView) findViewById(R.id.imgprofile2);

    }
    public void Register(View view) {

        nombre = edtregisternombre.getText().toString();
        correo = edtregistercorreo.getText().toString();
        telefono = edtregistertelefono.getText().toString();
        empresa = edtregisterempresa.getText().toString();
        password = edtregisterpassword.getText().toString();
        String password2 = edtregisterpassword2.getText().toString();
        //funcion para validar el correo
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean rptcorreo = pattern.matcher(correo).matches();

        if (nombre.isEmpty()) {
            edtregisternombre.setError("Se olvido ingresar el nombre");
        } else if (correo.isEmpty()) {
            edtregistercorreo.setError("Se olvido su correo");
        } else if (rptcorreo == false) {
            edtregistercorreo.setError("Correo invalido");
        } else if (telefono.toString().length() == 0) {

            edtregistertelefono.setError("Se olvido ingresar su telefono");
        } else if (empresa.isEmpty()) {
            edtregisterempresa.setError("Se olvido ingresar su empresa");
        } else if (password.isEmpty()) {
            edtregisterpassword.setError("Se olvido su contraseña");
        } else if (password2.isEmpty()) {
            edtregisterpassword2.setError("Se olvido su contraseña");
        } else if (!password.equals(password2)) {
            edtregisterpassword.setError("Las contraseñas no son iguales");
            edtregisterpassword2.setError("Las contraseñas no son iguales");
        } else {
//            Bitmap bitmap = BitmapFactory.decodeFile(mediaFileUri.getPath());
//            bitmap = scaleBitmapDown(bitmap, 800);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();

            final Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setTelefono(Integer.parseInt(telefono));
            usuario.setEmpresa(empresa);
            usuario.setContrasena(password);
            usuario.setImagenprofile(String.valueOf(mediaFileUri));
            crearUsuario(usuario);
        }
    }
//método para guardar el usuario en el sqlita
    public void crearUsuario(final Usuario usuario)
    {
        Usuario userrpt = realm.where(Usuario.class).equalTo("correo", correo).findFirst();
        if(userrpt == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(usuario);
                }
            });
            Toast.makeText(this, "Se registró correctamente", Toast.LENGTH_SHORT).show();
            guardarShared();
        }else {
            Toast.makeText(this,"Intente con otro correo", Toast.LENGTH_SHORT).show();
        }
    }
    public void guardarShared()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor
                .putString("nombre",nombre)
                .putString("correo", correo)
                .putBoolean("islogged", true)
                .commit();
        goMain();
    }
    //método para redirigirnos al main
    public void goMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void takePicture(View view) {
        try {

            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
                return;
            }
            // Creando el directorio de imágenes (si no existe)
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new Exception("Failed to create directory");
                }
            }
            // Definiendo la ruta destino de la captura (Uri)
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            mediaFileUri = Uri.fromFile(mediaFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                try {
                    Log.d(TAG,"Resultado:ok ");
                    Context applicationContext = RegisterActivity.this.getApplicationContext();
                    Log.d(TAG,"file:"+mediaFileUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);
                    bitmap = scaleBitmapDown(bitmap, 800);
                    imgprofile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(this, "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ResultCode: RESULT_CANCELED");
            } else {
                Log.d(TAG, "ResultCode: " + resultCode);
            }
        }
    }

    //<--!! //si los permisos con conedidos --!>
    private boolean permissionsGranted() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
    //<--!! // enviar el resultado de los permisos --!>
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d(TAG, "" + grantResults[i]);
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, PERMISSIONS_LIST[i] + " permiso rechazado!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permisos concedidos, intente nuevamente.", Toast.LENGTH_LONG).show();
            }
        }
    }
    //<--!! Redimensionar una imagen bitmap --!>
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
