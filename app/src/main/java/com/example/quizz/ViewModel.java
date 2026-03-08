package com.example.quizz;

import android.app.Application;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModel extends AndroidViewModel {
    private PersonRepesetory repesetory;
    private LiveData<List<Nota>> allPerson;

    public ViewModel(@NonNull Application application) {
        super(application);
        repesetory = new PersonRepesetory(application);
        allPerson=repesetory.cogeTodaPersona();
    }
    public void insert(Nota persona){
        repesetory.insert(persona);
    }
    public void update(Nota persona){
        repesetory.update(persona);
    }
    public void delete(Nota persona){
        repesetory.delete(persona);
    }
    public void  deleteAll(){
        repesetory.deleteall();
    }
    public LiveData<List<Nota>> getallPerson(){
        return allPerson;
    }
}
