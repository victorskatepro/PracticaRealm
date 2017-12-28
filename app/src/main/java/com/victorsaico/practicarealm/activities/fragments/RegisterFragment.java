package com.victorsaico.practicarealm.activities.fragments;

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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.activities.MainActivity;
import com.victorsaico.practicarealm.activities.models.Publicacion;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment {
    private EditText edttitulo;
    private Button btncapturarfoto;
    private Button btnregistrar;
    private Uri mediaFileUri;
    private ImageView imgphoto;
    private String usercorreo, usernombre;
    private int userId;
    private SharedPreferences sharedPreferences;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Date time;
    private Realm realm = Realm.getDefaultInstance();

    private static final int CAPTURE_IMAGE_REQUEST = 300;
    //<--!! //Registrar los permisos --!>
    private static final int PERMISSIONS_REQUEST = 200;
    private static final int SELECT_PICTURE = 100;
    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edttitulo = view.findViewById(R.id.edttitulo);
        btncapturarfoto = view.findViewById(R.id.btncapturarfoto);
        btnregistrar = view.findViewById(R.id.btnregistrar);
        imgphoto = view.findViewById(R.id.imgregister);

        imgphoto.setVisibility(View.GONE);
        btncapturarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getImage();
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                register();
            }
        });
        return view;
    }

    public void getProfile()
    {
        usernombre = sharedPreferences.getString("correo", null);
        userId = sharedPreferences.getInt("id", -1);
        Log.d(TAG,"USERID"+ userId);

    }
    public void takePhoto()
    {
        try {

            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LIST, PERMISSIONS_REQUEST);
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
            Toast.makeText(getActivity(), "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void getImage()
    {
        final Dialog dialog = new Dialog(getActivity());
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
    public void selecPhoto()
    {
        if(!permissionsGranted())
        {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LIST, PERMISSIONS_REQUEST);
            return;
        }
        Intent e = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(e, SELECT_PICTURE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                try {
                    Log.d(TAG,"Resultado:ok ");
                    Context applicationContext = getActivity().getApplicationContext();
                    Log.d(TAG,"file:"+mediaFileUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);
                    bitmap = scaleBitmapDown(bitmap, 800);
                    imgphoto.setVisibility(View.VISIBLE);
                    imgphoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(getActivity(), "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ResultCode: RESULT_CANCELED");
            } else {
                Log.d(TAG, "ResultCode: " + resultCode);
            }
        }else if(requestCode == SELECT_PICTURE)
        {
            if(resultCode == RESULT_OK)
            {
                mediaFileUri = data.getData();
                String[]projection={MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(mediaFileUri,projection,null,null,null);
                cursor.moveToFirst();
                Log.d(TAG,"imagenseleccionada:"+mediaFileUri);
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePhat = cursor.getString(columnIndex);
                cursor.close();
                Context applicationContext = getActivity().getApplicationContext();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);
                    bitmap = scaleBitmapDown(bitmap, 800);
                    imgphoto.setVisibility(View.VISIBLE);
                    imgphoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(getActivity(), "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void register() {
        String titulo = edttitulo.getText().toString();
        if (titulo.isEmpty()) {
            edttitulo.setError("Ingrese un titulo por favor");
        } else if (mediaFileUri == null) {
            Toast.makeText(getActivity(), "Capture una foto", Toast.LENGTH_SHORT).show();
        } else {
            getDate();

            //Obetener el tamaño de la tabla de datos de publicacion
            RealmResults<Publicacion> pubs = realm.where(Publicacion.class).findAll();
            int nextID = pubs.size();
            //obtener los datos del usuario
            getProfile();
            final Publicacion p = new Publicacion();
            p.setTitulo(titulo);
            p.setFecha(time);
            p.setImagen(String.valueOf(mediaFileUri));
            p.setId(nextID + 1);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Usuario user = realm.where(Usuario.class).equalTo("id", userId).findFirst();
                    if(user == null )
                    {
                        Toast.makeText(getActivity(), "no se encontro el id", Toast.LENGTH_SHORT).show();
                    }
                    user.getPublicacions().add(p);
                }
            });

            Toast.makeText(getActivity(), "Se creo correctamente", Toast.LENGTH_SHORT).show();
        }

    }
    public void getDate()
    {
        java.util.Date date = new Date();
        time = date;
    }
    //<--!! //si los permisos con conedidos --!>
    private boolean permissionsGranted() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
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
                        Toast.makeText(getActivity(), PERMISSIONS_LIST[i] + " permiso rechazado!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(getActivity(), "Permisos concedidos, intente nuevamente.", Toast.LENGTH_LONG).show();
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
