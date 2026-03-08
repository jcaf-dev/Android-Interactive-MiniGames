package com.example.quizz;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BotonModificador extends AppCompatActivity {

    private RadioGroup radioGroup1;
    private String color = "basico";

    private RadioButton radioButton1;

    private TextView textView1, textView2;
    private ConstraintLayout Layout;
    private Typeface fuente1;
    private Typeface fuente2;

    Button Boton_de_sonido;

    private Sonido_BG_Service mServ;
    private boolean mIsBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boton_modificador);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);
        if(Globales.musica) {
        startService(music);
    }
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioButton1= findViewById(R.id.botonFuente_tres);

        textView1 = (TextView) findViewById(R.id.textFuente1);
        textView2 = (TextView) findViewById(R.id.textFuente2);

        String fuenteS1 = "fuentes/Agatha.ttf";
        String fuenteS2 = "fuentes/Athenic.ttf";
        Layout = findViewById(R.id.Layout01);
        this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
        this.fuente2 = Typeface.createFromAsset(getAssets(),fuenteS2);

        textView1.setTypeface(fuente1);
        textView2.setTypeface(fuente2);

        Boton_de_sonido = (Button)findViewById(R.id.Boton_sonido);
        Boton_de_sonido.setBackgroundResource(R.drawable.reproducir);






    }

    public void Boton_de_sonido(View v){
        if (mServ != null && Globales.musica==false) {
            Boton_de_sonido.setBackgroundResource(R.drawable.pausa);
            mServ.resumeMusic();
            Globales.musica=true;
        }
        else if(mServ != null && Globales.musica){
            Boton_de_sonido.setBackgroundResource(R.drawable.reproducir);
            mServ.pauseMusic();
            Globales.musica=false;
        }
    }



    public void Guardar(View v){

        String nombre = "ajustes1";
        String info = radioButton1.getText()+ "" +color;

        SharedPreferences preferencias = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString(nombre,info);
        obj_editor.commit();
    //    Toast.makeText(this, "Datos guardados"+ radioButton1.getText()+ " " + radioButton2.getText(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

    }

    public void actualizarFuente(View v){
            int radio1 = radioGroup1.getCheckedRadioButtonId();
            radioButton1 = findViewById(radio1);
            Toast.makeText(this,"Cambio de fuente "+ radioButton1.getText(),Toast.LENGTH_SHORT).show();
    }


    public void Blanco(View v){
            color = "Basico";
            Toast.makeText(this,"Cambio de color al basico",Toast.LENGTH_SHORT).show();
            Layout.setBackgroundColor(Color.WHITE);
    }
    public void Negro(View v){
        color = "Azul";
        Toast.makeText(this,"Cambio de color al negro",Toast.LENGTH_SHORT).show();
        Layout.setBackgroundColor(Color.parseColor("#FF00DDFF"));
    }
    public void Rojo(View v){
        color = "Rojo";
        Toast.makeText(this,"Cambio de color al rojo",Toast.LENGTH_SHORT).show();
        Layout.setBackgroundColor(Color.RED);
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
