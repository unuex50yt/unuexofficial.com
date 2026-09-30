package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartItemEntity::class,
        WishlistItemEntity::class,
        OrderEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UnuexDatabase : RoomDatabase() {

    abstract fun unuexDao(): UnuexDao

    companion object {
        @Volatile
        private var INSTANCE: UnuexDatabase? = null

        fun getDatabase(context: Context): UnuexDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnuexDatabase::class.java,
                    "unuex_2050_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
