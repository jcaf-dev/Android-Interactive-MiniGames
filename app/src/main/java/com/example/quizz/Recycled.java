package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Recycled extends AppCompatActivity {
    private ViewModel PersonasViewModel;
    private int usu;
    private String nombre;
    private int puntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycled);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);
        if(Globales.musica) {
            startService(music);
        }
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        puntuacion = extras.getInt("Respuestas_Correctas", 0);

        RecyclerView recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final Person_adapter adapter = new Person_adapter(this);
        recyclerView.setAdapter(adapter);

        PersonasViewModel = ViewModelProviders.of(this).get(ViewModel.class);
        PersonasViewModel.getallPerson().observe(this, new Observer<List<Nota>>() {
            @Override
            public void onChanged(List<Nota> notas) {
                adapter.setNotes(notas);
            }
        });
        adapter.setOnItemClickListener(new Person_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Nota persona) {
                Intent intent = new Intent (Recycled.this, Resultado.class);
                intent.putExtra("id",persona.getId());
                startActivity(intent);
            }
        });

    }

    public void resul(View v){
        Intent intent = new Intent(getApplicationContext(), Actividad2.class);
        Bundle extras = new Bundle();
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Usu", usu);
        intent.putExtras(extras);
        startActivity(intent);
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
