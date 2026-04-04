package com.example.projetocandidaturas.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projetocandidaturas.modelo.Candidatura;

@Database(entities = {Candidatura.class}, version = 1, exportSchema = false)
public abstract class CandidaturaDatabase extends RoomDatabase {

    public abstract CandidaturaDao getCandidaturaDao();

    private static CandidaturaDatabase INSTANCE;

    public static CandidaturaDatabase getInstance(final Context context) {

        if (INSTANCE == null) {

            synchronized (CandidaturaDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context, CandidaturaDatabase.class, "candidatura.db")
                            .allowMainThreadQueries().build();
                }
            }
        }

        return INSTANCE;
    }
}
