package com.fangshen.manual.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

const val N_DRUG_ID = "drugId"

/** 用户自定义药物 */
@Entity(tableName = "custom_drugs")
data class CustomDrugEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorArgb: Long,
    /** 单片剂量：BigDecimal 的 toPlainString，精确存储（如 "0.4"） */
    val tabletDoseMg: String?,
    val doseUnit: String,
    val managementClass: String,
    val subjectiveTags: String?,
    val effectTags: String?,
    val moleculeTag: String,
    val halfLifeHours: Double,
    val bioavailabilityPct: Int,
    val route: String,
    val tmaxHours: Double?,
    val substrates: String?,
    val inhibitors: String?,
    val inducers: String?,
    val createdAt: Long,
)

/** 自定义药物的效应线：服用剂量超过阈值时施加权重 */
@Entity(
    tableName = "custom_drug_rules",
    foreignKeys = [
        ForeignKey(
            entity = CustomDrugEntity::class,
            parentColumns = ["id"],
            childColumns = [N_DRUG_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(N_DRUG_ID)],
)
data class CustomDrugRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @androidx.room.ColumnInfo(name = N_DRUG_ID) val drugId: Long,
    /** 剂量阈值：BigDecimal 的 toPlainString，精确存储 */
    val threshold: String,
    val effectName: String,
    val weight: Int,
)

/** 自定义药物的神经递质权重改变 */
@Entity(
    tableName = "custom_drug_neuros",
    foreignKeys = [
        ForeignKey(
            entity = CustomDrugEntity::class,
            parentColumns = ["id"],
            childColumns = [N_DRUG_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(N_DRUG_ID)],
)
data class CustomDrugNeuroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @androidx.room.ColumnInfo(name = N_DRUG_ID) val drugId: Long,
    val neurotransmitter: String,
    val delta: Int,
)

/** 台账条目：服药记录、笔记、照片、语音、文件、日程 */
@Entity(tableName = "ledger_entries")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val title: String,
    val content: String?,
    val drugId: String?,
    /** 服药剂量：BigDecimal 的 toPlainString，精确存储（如 "0.4"） */
    val doseAmount: String?,
    val doseUnit: String?,
    val route: String?,
    val attachmentUri: String?,
    val durationMs: Long?,
    val eventTime: Long,
    val notify: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)

/** 个人画像键值（暂未实现的功能占位） */
@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val key: String,
    val valueText: String,
    val updatedAt: Long,
)
