package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MiBaseDeDatos extends AppCompatActivity {

    ArrayList<ArrayList<String>> QuizzArray = new ArrayList<>();

    //----------------- BBDD -------------------//

    private EditText numeroP, opcion1,opcion2, opcion3, opcion4, opcion5;
    private TextView info_pregunta;
    private TextView contador_de_preguntas;

    Sonido_BG_Service mServ;
    //---------------- Matriz de Strings con los datos del Quizz ----------------//

    String QuizzDatos[][] = {
            {"Ghost","Year Zero"," Whisper "," Haunted "," Going Under "},
            {"Metallica","Whiskey in The Jar"," Year Zero "," Idolatrine "," Rats "},
            {"Iron Maiden","The Trooper"," Feelings "," Americana "," Du hast "},
            {"Pentakill", "Last Whisper"," Divide "," Torn "," Inside the Fire "},
            {"AC DC ", "Black in black", " Gonna Fly Now ", " Fever "," Alone "},
            {"Avenged Sevenfold","Nightmare"," Faded "," Zero "," Man or Animal "},
            {"The Offspring"," The Kids Arent't Alringt"," Faith "," Life Eternal "," Warriors "},
            {"In flames","Bullet Ride"," My Demons "," Awaken "," Fallen "}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_base_de_datos);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);
        if(Globales.musica) {
            startService(music);
        }

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //---------------- Creacion de un ArrayList temporal ----------------//

        for(int i = 0; i<QuizzDatos.length; i++){
            ArrayList<String> ArrayTemporal = new ArrayList<>();

            ArrayTemporal.add(QuizzDatos[i][0]);    //-- Pregunta musica --//
            ArrayTemporal.add(QuizzDatos[i][1]);    //-- Opcion1 --//
            ArrayTemporal.add(QuizzDatos[i][2]);    //-- Opcion2 --//
            ArrayTemporal.add(QuizzDatos[i][3]);    //-- Opcion3 --//
            ArrayTemporal.add(QuizzDatos[i][4]);    //-- Opcion4 --//*/

            QuizzArray.add(ArrayTemporal);
        }

        //------------------------------- BBDD -------------------------------//

        numeroP = (EditText) findViewById(R.id.preguntaQ);
        opcion1 = (EditText) findViewById(R.id.txt_opcion1);
        opcion2 = (EditText) findViewById(R.id.txt_opcion2);
        opcion3 = (EditText) findViewById(R.id.txt_opcion3);
        opcion4 = (EditText) findViewById(R.id.txt_opcion4);
        opcion5 = (EditText) findViewById(R.id.txt_opcion5);

        info_pregunta = (TextView)findViewById(R.id.info_pregunta);

        contador_de_preguntas = findViewById(R.id.contador);

        //-------------------------------Guardar Preguntas de la BBDD------------------------//

        //GuardadoAutomatico();
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
        contador_de_preguntas.setText("Num preguntas: "+resultado);

        bbdd.close();
        Toast.makeText(this,"Preguntas guardadas",Toast.LENGTH_SHORT).show();
    }

    public void Añadir(View v){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();

        String NumeroP = numeroP.getText().toString();
        String Opcion1 = opcion1.getText().toString();
        String Opcion2 = opcion2.getText().toString();
        String Opcion3 = opcion3.getText().toString();
        String Opcion4 = opcion4.getText().toString();
        String Opcion5 = opcion5.getText().toString();

        if(!NumeroP.isEmpty() && !Opcion1.isEmpty()&& !Opcion2.isEmpty() && !Opcion3.isEmpty()&&!Opcion4.isEmpty()&&!Opcion5.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("numero",NumeroP);
            registro.put("opcion1", Opcion1);
            registro.put("opcion2",Opcion2);
            registro.put("opcion3",Opcion3);
            registro.put("opcion4",Opcion4);
            registro.put("opcion5",Opcion5);

            bbdd.insert("preguntas",null,registro);

            //----------Contar preguntas--------//
            String sql = "SELECT numero FROM preguntas";
            int resultado = bbdd.rawQuery(sql,null).getCount();
            contador_de_preguntas.setText("Num preguntas: "+resultado);

            bbdd.close();
            numeroP.setText("");
            opcion1.setText("");
            opcion2.setText("");
            opcion3.setText("");
            opcion4.setText("");
            opcion5.setText("");


            Toast.makeText(this,"Pregunta guardada"+ resultado,Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Falta por rellenar",Toast.LENGTH_SHORT).show();

        }
    }

    public void Buscar(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();

        String Numero = numeroP.getText().toString();
        if(!Numero.isEmpty()){

            Cursor fila = bbdd.rawQuery("select opcion1,opcion2 from preguntas where numero ="+Numero, null);
            if(fila.moveToFirst()){
                info_pregunta.setText("Qué cancion es del grupo "+fila.getString(0) +"\n"
                        +"Respuesta :"+ fila.getString(1));
            }
            else{
                Toast.makeText(this,"La pregunta no existe", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(this,"No has introducido un numero", Toast.LENGTH_SHORT).show();
        }

        bbdd.close();
    }

    public void Modificar(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();

        String NumeroP = numeroP.getText().toString();
        String Opcion1 = opcion1.getText().toString();
        String Opcion2 = opcion2.getText().toString();
        String Opcion3 = opcion3.getText().toString();
        String Opcion4 = opcion4.getText().toString();
        String Opcion5 = opcion5.getText().toString();

        if(!NumeroP.isEmpty() && !Opcion1.isEmpty()&& !Opcion2.isEmpty() && !Opcion3.isEmpty()&&!Opcion4.isEmpty()&&!Opcion5.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("numero", NumeroP);
            registro.put("opcion1", Opcion1);
            registro.put("opcion2", Opcion2);
            registro.put("opcion3", Opcion3);
            registro.put("opcion4", Opcion4);
            registro.put("opcion5", Opcion5);

            int aux = bbdd.update("preguntas",registro,"numero="+NumeroP,null);
            if(aux == 1){
                Toast.makeText(this,"La pregunta fue modificada", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this,"La pregunta no fue modificada", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Rellena los campos", Toast.LENGTH_SHORT).show();

        }
        bbdd.close();
    }

    public void Eliminar(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();

        String Numero = numeroP.getText().toString();

        if(!Numero.isEmpty()){
            int cantidad = bbdd.delete("preguntas","numero="+Numero,null);
            numeroP.setText("");
            opcion1.setText("");
            opcion2.setText("");
            opcion3.setText("");
            opcion4.setText("");
            opcion5.setText("");
            info_pregunta.setText("");

            //----------Contar preguntas--------//
            String sql = "SELECT numero FROM preguntas";
            int resultado = bbdd.rawQuery(sql,null).getCount();
            contador_de_preguntas.setText("Num preguntas: "+resultado);

            if(cantidad == 1) {
                Toast.makeText(this, "La pregunta fue eliminada", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"No existe la pregunta", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Introduce el numero", Toast.LENGTH_SHORT).show();
        }
        bbdd.close();



    }

    public void Terminar(View v){
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
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
                Scon, Context.BIND_AUTO_CREATE);
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
