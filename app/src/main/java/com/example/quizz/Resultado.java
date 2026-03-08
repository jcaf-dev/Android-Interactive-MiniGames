package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

public class Resultado extends AppCompatActivity {
    private ViewModel PersonasViewModel;
    private TextView name;
    private TextView puntuacion;
    private TextView prop;
    private TextView jugadas;
    private TextView titprop;
    private Nota usuario;
    private TextView puesto;
    private ProgressBar barra;
    private boolean ya = false;
    private ImageView imagen;
    private int usu;
    private Typeface fuente1;
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private Sonido_BG_Service mServ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

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

        imagen= findViewById(R.id.profile_image3);
        name = findViewById(R.id.Nombre);
        puntuacion = findViewById(R.id.Puntuacion);
        jugadas = findViewById(R.id.jugadas);
        prop= findViewById(R.id.prop);
        puesto= findViewById(R.id.puesto);
        barra= findViewById(R.id.progressBar2);
        titprop=findViewById(R.id.textView7);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("id");
        PersonasViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        PersonasViewModel.getallPerson().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                if(ya==false) {
                    for (Nota n : notas) {
                        if (n.getId() == usu) {
                            usuario = n;
                            name.setText(n.getNombre());
                            puntuacion.setText("Ha conseguido: " + n.getPuntuacion() + " puntos");
                            jugadas.setText("Ha jugado: "+ n.getJugadas() + " partidas");
                            double t = 0;
                            if(n.getJugadas()!=0){
                                t= (n.getPuntuacion()*1.0)/(n.getJugadas()*15);
                            }
                            double aux = 100.0 * t;
                            barra.setProgress((int) aux,true);
                            DecimalFormat df = new DecimalFormat("#.00");
                            prop.setText(""+ df.format(t));
                            Bitmap image = getImage(CogerFoto(n.getNombre()));
                            imagen.setImageBitmap(image);
                            ya=true;
                            break;
                        }
                    }
                    int pos = 1;
                    for (Nota n : notas) {
                           if(!n.equals(usuario)){
                               if(n.getPuntuacion()>usuario.getPuntuacion()){
                                   pos++;
                               }
                           }
                    }
                    puesto.setText("Estas en " +pos+"ª posicion");
                }
            }
        });



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

        name.setTypeface(fuente1);
        puntuacion.setTypeface(fuente1);
        titprop.setTypeface(fuente1);
        prop.setTypeface(fuente1);
        jugadas.setTypeface(fuente1);
        puesto.setTypeface(fuente1);
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
    public void atras(View v){
        finish();
    }

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
