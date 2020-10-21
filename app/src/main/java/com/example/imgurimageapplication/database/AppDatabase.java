package com.example.imgurimageapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.imgurimageapplication.model.CommentModel;


@Database(entities = {CommentModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao getComments();
}