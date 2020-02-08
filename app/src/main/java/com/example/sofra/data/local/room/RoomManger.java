package com.example.sofra.data.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OrderItem.class}, version = 4, exportSchema = false)

public abstract class RoomManger extends RoomDatabase {
    public abstract RoomDao roomDao();

    private static RoomManger roomManger;

    public static synchronized RoomManger getInstance(Context context) {
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class,
                    "sofraDatabase").fallbackToDestructiveMigration().build();
        }
        return roomManger;
    }
}
