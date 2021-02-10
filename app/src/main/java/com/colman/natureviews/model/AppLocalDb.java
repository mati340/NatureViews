package com.colman.natureviews.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.colman.natureviews.NatureViewsApplication;

@Database(entities = {Post.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(
                    NatureViewsApplication.context,
                    AppLocalDbRepository.class,
                    "AppLocalDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}