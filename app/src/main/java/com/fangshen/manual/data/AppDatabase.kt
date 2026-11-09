package com.fangshen.manual.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.data.entity.ProfileEntity

@Database(
    entities = [
        CustomDrugEntity::class,
        CustomDrugRuleEntity::class,
        CustomDrugNeuroEntity::class,
        EntryEntity::class,
        ProfileEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customDrugDao(): CustomDrugDao
    abstract fun customDrugRuleDao(): CustomDrugRuleDao
    abstract fun customDrugNeuroDao(): CustomDrugNeuroDao
    abstract fun entryDao(): EntryDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "fangshen_manual.db",
            ).fallbackToDestructiveMigration().build().also { instance = it }
        }
    }
}
