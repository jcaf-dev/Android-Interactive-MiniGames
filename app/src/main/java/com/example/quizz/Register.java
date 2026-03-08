package com.example.quizz;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class Register extends AppCompatActivity {
    private ViewModel PersonasViewModel;
    private EditText usuario;
    private EditText pass;
    private TextView mas;
    private TextView usuc;
    private Button bot;
    private TextView titulo;
    private TextView apendice;
    private Button log;
    private FloatingActionButton camara;
    private LinkedList<Nota> usu;
    private boolean cont;
    private boolean cont2;
    private Typeface fuente1;
    private ImageView imagen;
    Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private static final String DB_TABLE = "table_image";
    private boolean mIsBound = false;
    private Sonido_BG_Service mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //MUSICA
        Button Boton_de_sonido;
        Boton_de_sonido = (Button) findViewById(R.id.boton_sonido);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);
        if (Globales.musica) {
            startService(music);
            Boton_de_sonido.setBackgroundResource(R.drawable.pausa);
        } else {
            Boton_de_sonido.setBackgroundResource(R.drawable.reproducir);
        }

        camara = findViewById(R.id.camara);
        imagen = findViewById(R.id.foto);
        usuario = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        bot = findViewById(R.id.register2);
        log = findViewById(R.id.login2);
        titulo = findViewById(R.id.Titulo);
        apendice = findViewById(R.id.apendice1);
        mas = findViewById(R.id.mas);
        usuc = findViewById(R.id.usuc);
        usu = new LinkedList<Nota>();
        cont = false;
        cont2 = false;
        imageBitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        PersonasViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        PersonasViewModel.getallPerson().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                usu.addAll(notas);
                Toast.makeText(Register.this, "OnChanged", Toast.LENGTH_SHORT).show();
            }
        });
        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String u = usuario.getText().toString();
                boolean encontrado = false;
                if (u.length() == 0) {
                    usuc.setVisibility(View.GONE);
                } else {

                    for (Nota n : usu) {

                        if (u.equals(n.getNombre())) {
                            usuc.setText("Usuario ya existente");
                            usuc.setTextColor(Color.parseColor("#FFD81B60"));
                            usuc.setVisibility(View.VISIBLE);
                            encontrado = true;
                            cont = false;
                            break;
                        }
                    }
                    if (encontrado == false) {
                        usuc.setText("Usuario correcto");
                        usuc.setTextColor(Color.parseColor("#FF19D400"));
                        cont = true;
                        usuc.setVisibility(View.VISIBLE);
                    }


                }
                if (cont && cont2) {
                    bot.setEnabled(true);
                } else {
                    bot.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String p = pass.getText().toString();
                if (p.length() == 0) {
                    mas.setVisibility(View.GONE);
                } else if (p.length() < 4) {
                    mas.setText("La contraseya debe ser de minimo 4 caracateres");
                    mas.setTextColor(Color.parseColor("#FFD81B60"));
                    mas.setVisibility(View.VISIBLE);
                    cont2 = false;
                } else {
                    mas.setText("Contraseya valida");
                    mas.setTextColor(Color.parseColor("#FF19D400"));
                    mas.setVisibility(View.VISIBLE);
                    cont2 = true;
                }
                if (cont && cont2) {
                    bot.setEnabled(true);
                } else {
                    bot.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonasViewModel.insert(new Nota(usuario.getText().toString(), pass.getText().toString()));
                addEntry(usuario.getText().toString(), getBytes(imageBitmap));
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("Nombre usu", usuario.getText().toString());
                extras.putString("Pass usu", pass.getText().toString());
                setResult(1, intent);
                intent.putExtras(extras);
                finish();
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, 1);
            }
        });
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
                    if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, Manifest.permission.CAMERA)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.CAMERA}, 1);
                            // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                        }
                    } else { //have permissions
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                } else { // Pre-Marshmallow
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });



        // PREFERENCES
        String fuenteS1;
        int aux2;
        char aux3;
        SharedPreferences preferencias = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String info = preferencias.getString("ajustes1","");
        if(info.isEmpty()){
            aux2= 1;
            aux3= 'B';
        }else {
            aux2 = Integer.parseInt("" + info.charAt(0));
            aux3 = info.charAt(1);
        }
        ConstraintLayout Layout01 =  findViewById(R.id.Layout01);


        switch (aux2)
        {
            case 1:
                fuenteS1 = "fuentes/Agatha.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 2:
                fuenteS1 = "fuentes/Athenic.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 3:
                this.fuente1 = Typeface.SANS_SERIF;
                break;
            default:
        }

        apendice.setTypeface(fuente1);
        titulo.setTypeface(fuente1);
        switch (aux3)
        {
            case 'B':
                Layout01.setBackgroundColor(Color.WHITE);
                break;
            case 'R':
                Layout01.setBackgroundColor(Color.RED);
                break;
            case 'A':
                Layout01.setBackgroundColor(Color.parseColor("#FF00DDFF"));
                break;
            default:
        }




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
        }
    }
    public void addEntry( String name, byte[] image) throws SQLiteException {
        SQLHelperIMage admin = new SQLHelperIMage(this,"imagenUsuario",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,     name);
        cv.put(KEY_IMAGE,     image);
        bbdd.insert( DB_TABLE, null, cv );

    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 : {
                // Si la petición es cancelada, el array resultante estará vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso ha sido concedido.
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    // Permiso denegado, deshabilita la funcionalidad que depende de este permiso.
                }
                return;
            }
            // otros bloques de 'case' para controlar otros permisos de la aplicación
        }
    }
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((Sonido_BG_Service.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };
    public void Boton_de_sonido(View v){
        if (mServ != null && Globales.musica==false) {
            v.setBackgroundResource(R.drawable.pausa);
            mServ.resumeMusic();
            Globales.musica=true;
        }
        else if(mServ != null && Globales.musica){
            v.setBackgroundResource(R.drawable.reproducir);
            mServ.pauseMusic();
            Globales.musica=false;
        }
    }
    void doBindService(){
        bindService(new Intent(this,Sonido_BG_Service.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && Globales.musica) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


            if (mServ != null) {
                mServ.pauseMusic();
            }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,Sonido_BG_Service.class);
        stopService(music);

    }

}
