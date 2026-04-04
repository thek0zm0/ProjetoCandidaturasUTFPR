package com.example.projetocandidaturas.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetocandidaturas.modelo.Candidatura;

import java.util.List;

@Dao
public interface CandidaturaDao {

    @Insert
    long inserir(Candidatura candidatura);

    @Delete
    int delete(Candidatura candidatura);

    @Update
    int update(Candidatura candidatura);

    @Query("SELECT * FROM candidatura ORDER BY nomeCargo ASC")
    List<Candidatura> queryAllAscending();

    @Query("SELECT * FROM candidatura WHERE id=:id")
    Candidatura queryForId(Long id);
}
