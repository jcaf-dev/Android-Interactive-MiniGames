package com.example.quizz;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Login extends AppCompatActivity {
    private ViewModel PersonasViewModel;
    private String NombrePre;
    private  String PassPre;
    private EditText usuario;
    private EditText pass;
    private boolean encontrado;
    private TextView usuc;
    private TextView titulo;
    private TextView apendice;
    private Button bot;
    private Button reg;
    private LinkedList<Nota> usu;
    private boolean cont;
    private boolean cont2;
    Sonido_BG_Service mServ;
    private String QuizzDatos[][] = {
            {"Ghost","Year Zero"," Whisper "," Haunted "," Going Under "},
            {"Metallica","Whiskey in The Jar"," Year Zero "," Idolatrine "," Rats "},
            {"Iron Maiden","The Trooper"," Feelings "," Americana "," Du hast "},
            {"Pentakill", "Last Whisper"," Divide "," Torn "," Inside the Fire "},
            {"AC DC ", "Black in black", " Gonna Fly Now ", " Fever "," Alone "},
            {"Avenged Sevenfold","Nightmare"," Faded "," Zero "," Man or Animal "},
            {"The Offspring"," The Kids Arent't Alringt"," Faith "," Life Eternal "," Warriors "},
            {"In flames","Bullet Ride"," My Demons "," Awaken "," Fallen "}
    };
    ArrayList<ArrayList<String>> QuizzArray;
    private Typeface fuente1;
    private int pistas;
    private TextView pistaText;
    private ImageButton tienda;
    private long backPressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //Pistas
        try{
            SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
            String info = preferencias.getString("tienda","");
            pistas = Integer.parseInt(info);
        }catch (Exception e){
            pistas = 0;
        }

        //------Tienda------//
        tienda = (ImageButton) findViewById(R.id.boton_tienda);



        QuizzArray=new ArrayList<>();
        usuario = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        bot= findViewById(R.id.login);
        reg= findViewById(R.id.reg);
        titulo = findViewById(R.id.Titulo);
        apendice = findViewById(R.id.apendice1);
        usuc=findViewById(R.id.usuc);
        usu= new LinkedList<>();
        cont=false;
        cont2=false;
        for(int i = 0; i<QuizzDatos.length; i++){
            ArrayList<String> ArrayTemporal = new ArrayList<>();

            ArrayTemporal.add(QuizzDatos[i][0]);    //-- Pregunta musica --//
            ArrayTemporal.add(QuizzDatos[i][1]);    //-- Opcion1 --//
            ArrayTemporal.add(QuizzDatos[i][2]);    //-- Opcion2 --//
            ArrayTemporal.add(QuizzDatos[i][3]);    //-- Opcion3 --//
            ArrayTemporal.add(QuizzDatos[i][4]);    //-- Opcion4 --//*/

            QuizzArray.add(ArrayTemporal);
        }
        GuardadoAutomatico();


        PersonasViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        PersonasViewModel.getallPerson().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                usu.addAll(notas);

                Toast.makeText(Login.this, "OnChanged", Toast.LENGTH_SHORT).show();
            }
        });
            usuario.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String u = usuario.getText().toString();
                    if (u.length()==0) {
                        cont =false;
                    }
                    else{cont= true;}
                    if(cont && cont2){ bot.setEnabled(true);}
                    else{bot.setEnabled(false);}
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
                        cont2=false;
                    }
                    else{
                        cont2=true;
                    }
                    if(cont && cont2){ bot.setEnabled(true);}
                    else{bot.setEnabled(false);}
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            bot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u = usuario.getText().toString();
                    String p = pass.getText().toString();
                    for (Nota n : usu) {
                            if (u.equals(n.getNombre())) {
                                if (p.equals(n.getContraseya())) {

                                   // Intent intent = new Intent(getApplicationContext(), Actividad1.class);
                                    Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
                                    Bundle extras = new Bundle();
                                    extras.putInt("Usu", n.getId());
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    encontrado=true;
                                }
                            break;
                        }
                    }
                    if(!encontrado){usuc.setVisibility(View.VISIBLE);}
                }
            });
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Register.class);
                    startActivityForResult(intent, 1);
                }
            });

            //---------- Pista ----------//
            pistaText = (TextView) findViewById(R.id.contador_pistas);
            pistaText.setText(pistas+"");

            //---------- PREFERENCES ----------//
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


      /*  switch (aux2)
        {
            case 1:
                fuenteS1 = "fuentes/Agatha.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 2:
                fuenteS1 = "fuentes/Athenic.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 3:*/
                this.fuente1 = Typeface.SANS_SERIF;
               /* break;
            default:
        }*/

        apendice.setTypeface(fuente1);
        //titulo.setTypeface(fuente1);
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

    //------------------------------------------------------------------------------------------//

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

    public void tienda (View view){
        Intent intent = new Intent(getApplicationContext(),tienda.class);
        startActivity(intent);
    }

    public void BotonDeFuentes(View Vista){
        Intent intent = new Intent(getApplicationContext(), BotonModificador.class);
        startActivity(intent);
    }
    public void BotonBBDD(View v){
        Intent intent = new Intent(getApplicationContext(),MiBaseDeDatos.class);
        startActivity(intent);
    }


    // "back" para salir, 2 veces
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getApplicationContext(),"¿Pulsa otra vez para salir?",Toast.LENGTH_SHORT).show();
        }

        backPressedTime =  System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode==1){
        if(resultCode==1){
            Bundle extras = data.getExtras();
            PassPre= extras.getString("Pass usu");
            NombrePre = extras.getString("Nombre usu");
            pass.setText(PassPre);
            usuario.setText(NombrePre);
        }}
    }
    public void GuardadoAutomatico(){  //--Guarda las preguntas y respuestas que ya tenemos--//

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();

        for( int i =0; i < QuizzArray.size();i++){

            ArrayList<String> Quizz = QuizzArray.get(i);
            ContentValues registro = new ContentValues();
            String num = "" +i;
            registro.put("numero",num);
            registro.put("opcion1",Quizz.get(0));
            registro.put("opcion2",Quizz.get(1));
            registro.put("opcion3",Quizz.get(2));
            registro.put("opcion4",Quizz.get(3));
            registro.put("opcion5",Quizz.get(4));

            bbdd.insert("preguntas",null,registro);
        }

        String sql = "SELECT numero FROM preguntas";
        int resultado = bbdd.rawQuery(sql,null).getCount();

        bbdd.close();
        Toast.makeText(this,"Preguntas guardadas",Toast.LENGTH_SHORT).show();
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





