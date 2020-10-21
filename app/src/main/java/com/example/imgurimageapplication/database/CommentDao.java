package com.example.imgurimageapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.imgurimageapplication.model.CommentModel;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM comment_tbl")
    CommentModel getAll();

    @Insert
    void insert(CommentModel commentModel);
}