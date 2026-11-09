package com.fangshen.manual.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.data.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomDrugDao {
    @Query("SELECT * FROM custom_drugs ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<CustomDrugEntity>>

    @Query("SELECT * FROM custom_drugs ORDER BY createdAt ASC")
    suspend fun allOnce(): List<CustomDrugEntity>

    @Query("SELECT * FROM custom_drugs WHERE id = :id")
    suspend fun getById(id: Long): CustomDrugEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drug: CustomDrugEntity): Long

    @Update
    suspend fun update(drug: CustomDrugEntity)

    @Delete
    suspend fun delete(drug: CustomDrugEntity)
}

@Dao
interface CustomDrugRuleDao {
    @Query("SELECT * FROM custom_drug_rules")
    fun observeAll(): Flow<List<CustomDrugRuleEntity>>

    @Query("SELECT * FROM custom_drug_rules")
    suspend fun allOnce(): List<CustomDrugRuleEntity>

    @Query("SELECT * FROM custom_drug_rules WHERE drugId = :drugId")
    suspend fun getForDrug(drugId: Long): List<CustomDrugRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules: List<CustomDrugRuleEntity>)

    @Query("DELETE FROM custom_drug_rules WHERE drugId = :drugId")
    suspend fun deleteForDrug(drugId: Long)
}

@Dao
interface CustomDrugNeuroDao {
    @Query("SELECT * FROM custom_drug_neuros")
    fun observeAll(): Flow<List<CustomDrugNeuroEntity>>

    @Query("SELECT * FROM custom_drug_neuros")
    suspend fun allOnce(): List<CustomDrugNeuroEntity>

    @Query("SELECT * FROM custom_drug_neuros WHERE drugId = :drugId")
    suspend fun getForDrug(drugId: Long): List<CustomDrugNeuroEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(neuros: List<CustomDrugNeuroEntity>)

    @Query("DELETE FROM custom_drug_neuros WHERE drugId = :drugId")
    suspend fun deleteForDrug(drugId: Long)
}

@Dao
interface EntryDao {
    @Query("SELECT * FROM ledger_entries ORDER BY eventTime DESC")
    fun observeAll(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM ledger_entries")
    suspend fun allOnce(): List<EntryEntity>

    @Query("SELECT * FROM ledger_entries WHERE id = :id")
    suspend fun getById(id: Long): EntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: EntryEntity): Long

    @Update
    suspend fun update(entry: EntryEntity)

    @Delete
    suspend fun delete(entry: EntryEntity)

    @Query("DELETE FROM ledger_entries")
    suspend fun deleteAll()

    @Query("SELECT * FROM ledger_entries WHERE type = 'SCHEDULE' AND notify = 1 AND eventTime > :from")
    suspend fun schedulesFrom(from: Long): List<EntryEntity>
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    fun observeAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profile")
    suspend fun allOnce(): List<ProfileEntity>

    @Upsert
    suspend fun upsert(entry: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun deleteAll()
}
