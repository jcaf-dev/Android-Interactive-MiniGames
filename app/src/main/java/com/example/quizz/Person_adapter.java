package com.example.quizz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Person_adapter extends RecyclerView.Adapter <Person_adapter.PersonHolder>{
    private List<Nota> personas = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    public Person_adapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person,parent,false);

        return new PersonHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        Nota notaa = personas.get(position);
        holder.textViewname.setText(notaa.getNombre());
        double t;
        if(notaa.getJugadas()==0){
            t=0;
        }else{
            t= (notaa.getPuntuacion()*1.0)/(notaa.getJugadas()*15);
        }

        DecimalFormat df = new DecimalFormat("#.00");
        holder.textViewprop.setText(""+ df.format(t));
        holder.textViewjugadas.setText(""+ notaa.getJugadas());
        holder.imagen.setImageBitmap(getImage(CogerFoto(notaa.getNombre())));
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }
    public void setNotes(List<Nota>personas){
        this.personas = personas;
        notifyDataSetChanged();
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
        byte[] image = getBytes(BitmapFactory.decodeResource(context.getResources(), R.drawable.p1));
        SQLHelperIMage admin = new SQLHelperIMage(context,"imagenUsuario",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();
        Cursor fila = bbdd.rawQuery("select " +KEY_IMAGE+" from table_image where "+ KEY_NAME +" = \"" + nombre + "\"", null);
        if(fila.moveToFirst()) {
            image = fila.getBlob(0);
        }
        fila.close();
        return image;
    }

    class PersonHolder extends RecyclerView.ViewHolder{
        private TextView textViewname;
        private TextView textViewjugadas;
        private TextView textViewprop;
        private ImageView imagen;

        public PersonHolder(@NonNull View itemView) {
            super(itemView);
            textViewname= itemView.findViewById(R.id.text_view_nombre);
            textViewjugadas= itemView.findViewById(R.id.text_view_puntuacion);
            textViewprop= itemView.findViewById(R.id.text_view_prop);
            imagen=itemView.findViewById(R.id.profile_image2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if(listener != null && posicion!= RecyclerView.NO_POSITION) {
                        listener.onItemClick(personas.get(posicion));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Nota persona);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener= listener;
    }
}
