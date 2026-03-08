package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Update;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class Actividad2 extends AppCompatActivity {
    private int puntuacion;
    private int usu;
    private TextView name;
    private ImageView imagen;
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private String nombre="No encontrado";
    private ViewModel PersonasViewModel;
    private boolean ya =false;
    private Typeface fuente1;
    private int niveles_superados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);
        //MUSICA
        Button Boton_de_sonido;
        Boton_de_sonido = (Button)findViewById(R.id.boton_sonido);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);
        if(Globales.musica) {
            startService(music);
            Boton_de_sonido.setBackgroundResource(R.drawable.pausa);
        }else{
            Boton_de_sonido.setBackgroundResource(R.drawable.reproducir);
        }
        TextView resultado = (TextView) findViewById(R.id.Resultado);
         name= findViewById(R.id.Nombre);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");

        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }
        niveles_superados++;

        puntuacion = extras.getInt("Respuestas_Correctas", 0);
        imagen = findViewById(R.id.profile_image);
        PersonasViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        PersonasViewModel.getallPerson().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                if(ya==false) {
                    for (Nota n : notas) {
                        if (n.getId() == usu) {
                            nombre = n.getNombre();
                            name.setText(nombre);
                            n.setPuntuacion(n.getPuntuacion()+puntuacion);
                            n.setJugadas(n.getJugadas()+1);
                            PersonasViewModel.update(n);
                            Bitmap image = getImage(CogerFoto(nombre));
                            imagen.setImageBitmap(image);
                            ya=true;
                            break;
                        }
                    }
                }
                Toast.makeText(Actividad2.this, "OnChanged", Toast.LENGTH_SHORT).show();
            }
        });


        resultado.setText("Puntuación : "+puntuacion);


        // PREFERENCES
        String fuenteS1 = new String();
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
        LinearLayout Layout01 =  findViewById(R.id.Layout01);


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

        name.setTypeface(fuente1);
        resultado.setTypeface(fuente1);
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

    //---------- Funcion que invoca a la primera actividad ----------//
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
    public void JugarOtraVez(View view){

        Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        extras.putInt("Niveles", niveles_superados);
        intent.putExtras(extras);
        startActivity(intent);

    }

    public void Tabla(View v){
        Intent intent = new Intent(getApplicationContext(), Recycled.class);
        Bundle extras = new Bundle();
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Usu", usu);
        extras.putInt("Niveles", niveles_superados);

        intent.putExtras(extras);
        startActivity(intent);
    }
    public void alLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivityForResult(intent, 1);
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public byte[] CogerFoto (String nombre){
        byte[] image = getBytes(BitmapFactory.decodeResource(this.getResources(), R.drawable.p1));
        SQLHelperIMage admin = new SQLHelperIMage(this,"imagenUsuario",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();
        Cursor fila = bbdd.rawQuery("select " +KEY_IMAGE+" from table_image where "+ KEY_NAME +" = \"" + nombre + "\"", null);
         if(fila.moveToFirst()) {
            image = fila.getBlob(0);
         }
        fila.close();
        return image;
    }

    Sonido_BG_Service mServ;
    boolean mIsBound = false;

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((Sonido_BG_Service.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

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
