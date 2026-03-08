package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class fotos2 extends AppCompatActivity {
    //---------------- Variables ----------------//

    private String RespuestaCorrecta;
    private int RespuestaCorrectaContador = 0;
    private int QuizzContador = 1;
    static final private int RondasDelQuizz = 3;
    private int niveles_superados;


    ArrayList<String> QuizzArray = new ArrayList<>();

    //---------------- Botones y textos ----------------//
    String Texto_de_Boton="Sin seleccion";
    private Button RespuestaBoton1;
    private Button RespuestaBoton2;
    private Button RespuestaBoton3;
    private Button RespuestaBoton4;
    private TextView ContadorTexto;
    private TextView PreguntasTexto;
    Dialog dialogo;
    private int usu;
    private Typeface fuente1;

    SoundPool sp;
    int sonido_acierto,sonido_error;
    //---------------- Matriz de Strings con los datos del Quizz ----------------//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos2);
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
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        RespuestaCorrectaContador = extras.getInt("Respuestas_Correctas", 0);
        //Cargar niveles
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        ContadorTexto = (TextView)findViewById(R.id.contadorTexto2);
        PreguntasTexto = (TextView)findViewById(R.id.preguntasTexto2);
        RespuestaBoton1 = (Button)findViewById(R.id.respuestaBoton);
        RespuestaBoton2 = (Button)findViewById(R.id.respuestaBoton2);
        RespuestaBoton3 = (Button)findViewById(R.id.respuestaBoton3);
        RespuestaBoton4 = (Button)findViewById(R.id.respuestaBoton4);

        //---------------- Sonido de botones ------------------//
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sonido_acierto = sp.load(this,R.raw.acierto,1);
        sonido_error = sp.load(this,R.raw.fallo,2);

        //---------------- Creacion de un ArrayList temporal ----------------//


           QuizzArray.add("Liberal Animation");    //-- Opcion1 --//
           QuizzArray.add("First Ditch Effort");    //-- Opcion2 --//
           QuizzArray.add("Punk in Drublic");    //-- Opcion3 --//
           QuizzArray.add("SL&TAS");    //-- Opcion4 --//*/


        //Activa el Dialogo
        dialogo=new Dialog(this);
        dialogo.setCanceledOnTouchOutside(false);
       TextView textoResultado =(TextView)findViewById(R.id.seleccion2);
        textoResultado.setText("Seleccionado: " + Texto_de_Boton);


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

        ContadorTexto.setTypeface(fuente1);
        PreguntasTexto.setTypeface(fuente1);
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


        MostrarNextQuizz(); //-- Funcion que muestra las pantallas del Quizz --//
    }
    //---------------- Respuesta de la segunda actividad ----------------//

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

    public void  SeleccionarRespuesta(View vista){
        limpiar();
        TextView textoResultado =(TextView)findViewById(R.id.seleccion2);

        Button  Respuesta_de_Boton = (Button) findViewById(vista.getId());

        //---------------- Cuando se pulsa la opcion, guardamos el resultado en esta variable ----------------//
        Texto_de_Boton = Respuesta_de_Boton.getText().toString();
        textoResultado.setText("Seleccionado: " +Texto_de_Boton);
        Respuesta_de_Boton.setTextColor(Color.BLACK);

    }
    public void seleccion(View vista){
        Switch sw= (Switch) findViewById(R.id.switchseleccion);
        TextView textoResultado =(TextView)findViewById(R.id.seleccion2);
        if(sw.isChecked())
        {
            textoResultado.setVisibility(View.VISIBLE);
        }else{
            textoResultado.setVisibility(View.GONE);

        }
    }


    public void limpiar(){
        Button  Boton1= (Button) findViewById(R.id.respuestaBoton);
        Button  Boton2= (Button) findViewById(R.id.respuestaBoton2);
        Button  Boton3= (Button) findViewById(R.id.respuestaBoton3);
        Button  Boton4= (Button) findViewById(R.id.respuestaBoton4);
        Boton1.setTextColor(Color.parseColor("#FF616161"));
        Boton2.setTextColor(Color.parseColor("#FF616161"));
        Boton3.setTextColor(Color.parseColor("#FF616161"));
        Boton4.setTextColor(Color.parseColor("#FF616161"));

    }

    public void ComprobarRespuesta(View vista){
        //---------------- Cuadro de alerta ----------------//
        limpiar();
        String AlertaTitulo;
        boolean acierto;

        if(Texto_de_Boton.equals(RespuestaCorrecta)){
            //----- correcto ----//
            sp.play(sonido_acierto,1,1,1,0,0);
            acierto=true;
            RespuestaCorrectaContador = RespuestaCorrectaContador + 3;
        }
        else{
            //----- Incorrecto -----//
            sp.play(sonido_error,1,1,1,0,0);
            if(RespuestaCorrectaContador < 3 && RespuestaCorrectaContador >= 0)
                RespuestaCorrectaContador = 0;
            else{
                RespuestaCorrectaContador = RespuestaCorrectaContador - 2;
            }
            acierto=false;
        }

        TextView textoResultado =(TextView)findViewById(R.id.seleccion2);
        dialogo.setContentView(R.layout.popupimg2);
        dialogo.setCanceledOnTouchOutside(false);
        Button bot= (Button) dialogo.findViewById(R.id.button2);
        Button reiniciar= (Button) dialogo.findViewById(R.id.button3);
        reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                Intent intent = new Intent(getApplicationContext(), Actividad1.class);
                Bundle extras = new Bundle();
                extras.putInt("Usu", usu);
                extras.putInt("Niveles", niveles_superados);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });

        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();


                    //-------- Iniciamos la segunda actividad --------//

                     Intent intent = new Intent(getApplicationContext(),PreguntaconFoto.class);
                     Bundle extras = new Bundle();
                     extras.putInt("Respuestas_Correctas",RespuestaCorrectaContador);
                     extras.putInt("Niveles", niveles_superados);

                extras.putInt("Usu", usu);
                intent.putExtras(extras);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(0,0);


            }
        });

        TextView tx=(TextView) dialogo.findViewById(R.id.resultadoPalabra);
        if(acierto) {
            tx.setText("GANASTE\nLleva una puntuación de "+RespuestaCorrectaContador+ "\nla respuesta era " + RespuestaCorrecta);
        }else{
            tx.setText("PERDISTE\nLleva una puntuación de "+RespuestaCorrectaContador+ "\nla respuesta era " + RespuestaCorrecta);
        }
        Texto_de_Boton="Sin seleccion";
        textoResultado.setText("Seleccionado: " +Texto_de_Boton);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }


    public void MostrarNextQuizz(){

        //----- Pregunta por pantalla y se da valor a la respuesta correcta -----//
        RespuestaCorrecta = QuizzArray.get(1);

        //----- Posibles respuestas que aparecen por pantalla -----//
        RespuestaBoton1.setText(QuizzArray.get(0));
        RespuestaBoton2.setText(QuizzArray.get(1));
        RespuestaBoton3.setText(QuizzArray.get(2));
        RespuestaBoton4.setText(QuizzArray.get(3));

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



