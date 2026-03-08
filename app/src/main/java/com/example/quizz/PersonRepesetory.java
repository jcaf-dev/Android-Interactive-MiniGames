package com.example.quizz;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;
public class PersonRepesetory {
    private NoteDao noteDao;
    private LiveData<List<Nota>> todaPersona;

    public PersonRepesetory (Application application){
        PersonDatabase database = PersonDatabase.getInstance(application);
        noteDao= database.noteDao();
        todaPersona= noteDao.getAll();
    }
    public  void insert(Nota persona){
        new InsertNoteAsyncTask(noteDao).execute(persona);

    }

    public void update(Nota persona){
        new UpdateNoteAsyncTask(noteDao).execute(persona);

    }

    public void delete(Nota persona){
        new deleteNoteAsyncTask(noteDao).execute();
    }
    public void deleteall (){
        new deleteAllNoteAsyncTask(noteDao).execute();
    }

    public LiveData<List<Nota>> cogeTodaPersona(){
        return todaPersona;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Nota, Void, Void>{
        private NoteDao noteDao;
        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground (Nota... notas){
                noteDao.insert(notas[0]);
                return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Nota, Void, Void>{
        private NoteDao noteDao;
        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground (Nota... notas){
            noteDao.update(notas[0]);
            return null;
        }
    }
    private static class deleteNoteAsyncTask extends AsyncTask<Nota, Void, Void>{
        private NoteDao noteDao;
        private deleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground (Nota... notas){
            noteDao.delete(notas[0]);
            return null;
        }
    }
    private static class deleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;
        private deleteAllNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground (Void... voids){
            noteDao.deleteall();
            return null;
        }
    }



}
