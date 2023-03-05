package com.jun.chatgpt.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jun.chatgpt.model.Message
import com.jun.template.common.Constants

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDBInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = createRoomBuilder(context)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        private fun createRoomBuilder(context: Context): Builder<AppDatabase> {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            ).allowMainThreadQueries()
//                .addMigrations(MigrationDb1To2())
//                .addMigrations(MigrationDb2To3())
        }
    }
}