package com.victorsaico.practicarealm.activities.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import io.realm.Realm;

public class EditarPerfilActivity extends AppCompatActivity {
    private Toolbar tolbarEdite;
    private ImageView imgperfil;
    private EditText edtnombre,edtcorreo,edttelefono,edtempresa,edtpassword1,edtpassword2,edtpassword3;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private ImageView imbback;
    private int Idusuario;
    private Uri mediaFileUri;
    private String imagenprofile;
    private String nombre,correo,empresa,telefono,password2;
    private static final int CAPTURE_IMAGE_REQUEST = 300;
    //<--!! //Registrar los permisos --!>
    private static final int PERMISSIONS_REQUEST = 200;
    private static final int SELECT_PICTURE = 100;
    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    Realm realm = Realm.getDefaultInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
     tolbarEdite = findViewById(R.id.profile_edit_toolbar);
     setSupportActionBar(tolbarEdite);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        edtnombre = findViewById(R.id.edtupdatenombre);
        edtnombre.clearFocus();
     edtcorreo = findViewById(R.id.edtupdatecorreo);
     edtempresa = findViewById(R.id.edtupdateempresa);
     edttelefono = findViewById(R.id.edtupdatetelefono);
     edtpassword1 = findViewById(R.id.edtupdatepassword1);
     edtpassword2 = findViewById(R.id.edtupdatepassword2);
     edtpassword3 = findViewById(R.id.edtupdatepassword3);
     imgperfil = findViewById(R.id.imgupdateprofile);
     imbback = findViewById(R.id.imbback);

     //obtener el usuario e imprimirlo en la pantalla
        getProfilewrite();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.maineditprofile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getProfilewrite()
    {
        Context applicationContext = this.getApplicationContext();
        Bitmap bitmap = null;
        try {
            String nombre = sharedPreferences.getString("nombre", null);
            String correo = sharedPreferences.getString("correo", null);
            imagenprofile = sharedPreferences.getString("imageprofile", null);
            Idusuario = sharedPreferences.getInt("id", -1);
            String empresa = sharedPreferences.getString("empresa", null);
            String telefono = sharedPreferences.getString("telefono", null);

            edtnombre.setText(nombre);
            edtcorreo.setText(correo);
            edtempresa.setText(empresa);
            edttelefono.setText(telefono);

            Uri uri = Uri.parse(imagenprofile);
            bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);
            imgperfil.setImageBitmap(bitmap);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i)
        {
            case R.id.itm_ok:
                goUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goUpdate()
    {
        boolean validate = validate();
        Toast.makeText(this, "validar"+validate, Toast.LENGTH_SHORT).show();
        if(validate)
        {
            Toast.makeText(this, "respuesta"+validate, Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "respuesta"+validate, Toast.LENGTH_SHORT).show();
            final Usuario user = realm.where(Usuario.class)
                    .equalTo("id", Idusuario).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG,"actualizando");
                        user.setNombre(nombre);
                        user.setCorreo(correo);
                        user.setEmpresa(empresa);
                        user.setTelefono(Integer.parseInt(telefono));
                        user.setImagenprofile(imagenprofile);
                        if (password2.length() > 0) {
                            user.setContrasena(password2);
                            realm.copyToRealmOrUpdate(user);
                            Toast.makeText(EditarPerfilActivity.this, "Ejecutado con exito", Toast.LENGTH_SHORT).show();
                            editarShared(user);
                        }else {
                            realm.copyToRealmOrUpdate(user);
                            Toast.makeText(EditarPerfilActivity.this, "Ejecutado con exito", Toast.LENGTH_SHORT).show();
                            editarShared(user);
                        }
                    }
                });
        }
    }
    public void editarShared(Usuario usuario)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor
                .putString("nombre",usuario.getNombre())
                .putString("correo", usuario.getCorreo())
                .putString("imageprofile", usuario.getImagenprofile())
                .putString("telefono", String.valueOf(usuario.getTelefono()))
                .putString("empresa", usuario.getEmpresa())
                .commit();
        goMain();
    }
    public void goMain()
    {
        Intent intent = new Intent(EditarPerfilActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public Boolean validate()
    {
        boolean respuesta = false;

        nombre = edtnombre.getText().toString();
        correo = edtcorreo.getText().toString();
        empresa = edtempresa.getText().toString();

        telefono = edttelefono.getText().toString();

        String password1 = edtpassword1.getText().toString();
        password2 = edtpassword2.getText().toString();
        String password3 = edtpassword3.getText().toString();

        Usuario userrpt = realm.where(Usuario.class).equalTo("id", Idusuario).findFirst();
        Log.d(TAG,"usuario seleccionado"+userrpt);
        if(nombre.isEmpty())
        {
            edtnombre.setError("Se te olvido tu nombre");
            respuesta = true;
        }
        if (correo.isEmpty())
        {
            edtcorreo.setError("Correo vacío");
            respuesta = true;
        }
        if(empresa.isEmpty())
        {
            edtcorreo.setError("Empresa vacío");
            respuesta = true;
        }
        if(telefono.length() < 9)
        {
            edttelefono.setError("Campo requerido mayor a 9");
            respuesta = true;
        }
        if(!password1.isEmpty()) {
            if (userrpt.getContrasena().equals(password1)) {
                Toast.makeText(this, "contraseña correcta", Toast.LENGTH_SHORT).show();
                if (password2.isEmpty()) {
                    edtpassword3.setError("Se olvidó su contraseña");
                    respuesta = true;
                }
                if (password3.isEmpty()) {
                    edtpassword3.setError("Se olvidó su contraseña");
                    respuesta = true;
                }
                if (password2.length() < 6) {
                    edtpassword2.setError("la contraseña tiene que ser mayor a 6");
                    respuesta = true;
                }
                if (password3.length() < 6) {
                    edtpassword3.setError("la contraseña tiene que ser mayor a 6");
                    respuesta = true;
                }
                if (!password2.equals(password3)) {

                    edtpassword2.setError("las contraseñas no son iguales");
                    edtpassword3.setError("las contraseñas no son iguales");
                    respuesta = true;
                }
            } else {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();

            }
        }
        return respuesta;
    }
    public void guardarRealm(final Usuario usuario)
    {
        Log.d(TAG,"usuario nuevo"+usuario);
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.copyToRealmOrUpdate(usuario);
//                }
//            });
//            Toast.makeText(this, "Se registró correctamente", Toast.LENGTH_SHORT).show();
//            //guardarShared(usuario);
    }
    public void updatePhoto(View view)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_photo);

        ImageView btntake = (ImageView) dialog.findViewById(R.id.btncustomtake);
        ImageView btngalery = (ImageView) dialog.findViewById(R.id.btncustomgalery);


        btntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
                dialog.dismiss();
            }
        });
        btngalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecPhoto();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void takePhoto()
    {
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
            Toast.makeText(this, "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void selecPhoto()
    {
        if(!permissionsGranted())
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
            return;
        }
        Intent e = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(e, SELECT_PICTURE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                try {

                    Context applicationContext = EditarPerfilActivity.this.getApplicationContext();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);
                    bitmap = scaleBitmapDown(bitmap, 800);
                    imgperfil.setImageBitmap(bitmap);
                    imagenprofile = String.valueOf(mediaFileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }else if(requestCode == SELECT_PICTURE)
        {
            if(resultCode == RESULT_OK)
            {
                mediaFileUri = data.getData();
                String[]projection={MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(mediaFileUri,projection,null,null,null);
                cursor.moveToFirst();
                Log.d(TAG,"imagenseleccionada:"+mediaFileUri);
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePhat = cursor.getString(columnIndex);
                cursor.close();
                Context applicationContext = EditarPerfilActivity.this.getApplicationContext();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);
                    bitmap = scaleBitmapDown(bitmap, 800);

                    imgperfil.setImageBitmap(bitmap);
                    imagenprofile = String.valueOf(mediaFileUri);
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(this, "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }else if (resultCode == RESULT_CANCELED)
            {
                Log.d(TAG,"ResultCode: RESULT_CANCELED");
            }else
            {
                Log.d(TAG,"ResultCode: "+resultCode);
            }
        }
    }
    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
