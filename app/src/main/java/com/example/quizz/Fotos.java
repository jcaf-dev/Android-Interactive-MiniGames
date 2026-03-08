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

public class Fotos extends AppCompatActivity {

    public int solucion;
    public int solReal=3;
    public int puntuacion=0;
    public int turno=1;
    Dialog dialogo;
    private int usu;
    private TextView ContadorTexto;
    private TextView PreguntasTexto;
    private Typeface fuente1;
    SoundPool sp;
    int sonido_acierto,sonido_error;
    private int niveles_superados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);

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
        mostrarResultado();
        ContadorTexto = (TextView)findViewById(R.id.contadorTexto);
        PreguntasTexto = (TextView)findViewById(R.id.preguntasTexto);
        dialogo=new Dialog(this);
        dialogo.setCanceledOnTouchOutside(false);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        puntuacion = extras.getInt("Respuestas_Correctas", 0);
        //Cargar niveles
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        //---------------- Sonido de botones ------------------//
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sonido_acierto = sp.load(this,R.raw.acierto,1);
        sonido_error = sp.load(this,R.raw.fallo,2);

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

    public void seleccion(View vista){
        Switch sw= (Switch) findViewById(R.id.Switchseleccion);
        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        if(sw.isChecked())
        {
            textoResultado.setVisibility(View.VISIBLE);
        }else{
            textoResultado.setVisibility(View.GONE);
        }
    }

    public void showpop(View v){
        turno++;
        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        textoResultado.setText("Resultado: ");
        dialogo.setContentView(R.layout.popupimg);
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
                Intent intent = new Intent(getApplicationContext(), Actividad2.class);
                Bundle extras = new Bundle();
                extras.putInt("Respuestas_Correctas", puntuacion);
                extras.putInt("Usu", usu);
                extras.putInt("Niveles", niveles_superados);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        TextView tx=(TextView) dialogo.findViewById(R.id.resultadoPalabra);
        if (solucion==solReal) {
            puntuacion = puntuacion +3;
            sp.play(sonido_acierto,1,1,1,0,0);
            tx.setText("GANASTE\nLleva una puntuación de " + puntuacion +"\nLa respuesta era:" );
        } else {
            if(puntuacion<3 && puntuacion>=0)
                puntuacion = 0;
            else{
                puntuacion = puntuacion -2;
            }
            sp.play(sonido_error,1,1,1,0,0);
            tx.setText("PERDISTE\nLleva una puntuación de " + puntuacion +"\nLa respuesta era:");
        }
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }

    public void seleccionaA(View view) {
        solucion=1;
        mostrarResultado();
    }

    public void seleccionaB(View vista){
        solucion=2;
        mostrarResultado();
    }

    public void seleccionaC(View vista){
        solucion=3;
        mostrarResultado();
    }

    public void seleccionaD(View vista){
        solucion=4;
        mostrarResultado();
    }

    public void  mostrarResultado(){
        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        String disco="Sin selección";
        if(solucion==1){disco="A)So Long and Thanks for All the Shoes";}
        else if(solucion==2){disco="B) First Ditch Effort";}
        else if(solucion==3){disco="C) Liberal Animation";}
        else if(solucion==4){disco="D) Punk in Drublic";}
        textoResultado.setText("Resultado: "+ disco);
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
