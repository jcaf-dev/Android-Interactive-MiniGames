package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game_2 extends AppCompatActivity {

    TextView txt1,txt2,txt3,txt4;
    private Button boton_terminar;
    Button target1,target2,target3,target4;
    ImageView btn1,btn2,btn3,btn4;
    private  int usu;
    private long backPressedTime;
    private int aciertos,errores;
    private int niveles_superados;
    private int puntuacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_2);
        puntuacion=0;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        usu = extras.getInt("Usu");
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        boton_terminar = (Button) findViewById(R.id.button_final);

        aciertos=0;
        errores=0;

        target1 = (Button) findViewById(R.id.target1);
        target2 = (Button) findViewById(R.id.target2);
        target3 = (Button) findViewById(R.id.target3);
        target4 = (Button) findViewById(R.id.target4);

        btn1 = (ImageView) findViewById(R.id.boton1);
        btn2 = (ImageView) findViewById(R.id.boton2);
        btn3 = (ImageView) findViewById(R.id.boton3);
        btn4 = (ImageView) findViewById(R.id.boton4);

        target1.setOnLongClickListener(longClickListener);
        target2.setOnLongClickListener(longClickListener);
        target3.setOnLongClickListener(longClickListener);
        target4.setOnLongClickListener(longClickListener);

        btn1.setOnDragListener(dragListener);
        btn2.setOnDragListener(dragListener);
        btn3.setOnDragListener(dragListener);
        btn4.setOnDragListener(dragListener);

        boton_terminar = (Button) findViewById(R.id.button_final);
        boton_terminar.setVisibility(View.GONE);
    }

    // "back" para salir, 2 veces
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();

            Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);

            intent.putExtras(extras);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"¿Seguro que quieres salir?",Toast.LENGTH_SHORT).show();
        }

        backPressedTime =  System.currentTimeMillis();
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data,myShadowBuilder,v,0);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener(){
        @Override
        public boolean onDrag(View v, DragEvent event){

            if(errores !=3) {

                int dragEvent = event.getAction();
                final View view = (View) event.getLocalState();

                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:

                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DROP:  //Para mover una caja y dejarla en su lugar
                        if (view.getId() == R.id.target1 && v.getId() == R.id.boton1) {
                            target1.setVisibility(View.GONE);
                            Toast.makeText(Game_2.this, "Correcto:", Toast.LENGTH_SHORT).show();
                            aciertos++;
                        } else if (view.getId() == R.id.target2 && v.getId() == R.id.boton2) {
                            target2.setVisibility(View.GONE);
                            Toast.makeText(Game_2.this, "Correcto:", Toast.LENGTH_SHORT).show();
                            aciertos++;
                        } else if (view.getId() == R.id.target3 && v.getId() == R.id.boton3) {
                            target3.setVisibility(View.GONE);
                            Toast.makeText(Game_2.this, "Correcto:", Toast.LENGTH_SHORT).show();
                            aciertos++;
                        } else if (view.getId() == R.id.target4 && v.getId() == R.id.boton4) {
                            target4.setVisibility(View.GONE);
                            Toast.makeText(Game_2.this, "Correcto:", Toast.LENGTH_SHORT).show();
                            aciertos++;
                        } else {
                            errores++;
                            Toast.makeText(Game_2.this, "Incorrecto:", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            } else{

                Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
                Bundle extras = new Bundle();
                extras.putInt("Usu", usu);
                extras.putInt("Niveles", niveles_superados);
                intent.putExtras(extras);
                startActivity(intent);
            }
            if(aciertos == 4)
                boton_terminar.setVisibility(View.VISIBLE);
            return true;
        }
    };

    public void Terminar(View view){
        puntuacion = 4;
        Intent intent = new Intent(getApplicationContext(), Actividad2.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Niveles", niveles_superados);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
