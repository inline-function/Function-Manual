package com.fangshen.manual.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 5,
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
            )
                .addMigrations(MIGRATION_4_5)
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }

        /** v3.1：custom_drugs 增加 withdrawalNote（戒断反应描述），增量迁移不丢数据 */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE custom_drugs ADD COLUMN withdrawalNote TEXT")
            }
        }
    }
}
