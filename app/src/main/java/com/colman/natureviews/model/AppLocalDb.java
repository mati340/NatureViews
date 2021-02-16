package com.colman.natureviews.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.colman.natureviews.NatureViewsApplication;

@Database(entities = {Post.class ,Comment.class}, version = 13)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();
}

public class AppLocalDb {
    static public com.colman.natureviews.model.AppLocalDbRepository db =
            Room.databaseBuilder(
                    NatureViewsApplication.context,
                    com.colman.natureviews.model.AppLocalDbRepository.class,
                    "AppLocalDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

