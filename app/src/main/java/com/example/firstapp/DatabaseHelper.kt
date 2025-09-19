package com.example.firstapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class BMIRecord(
    val id: Long = 0,
    val userName: String,
    val height: Double,
    val weight: Double,
    val bmi: Double,
    val dateTime: String
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bmi_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_BMI_RECORDS = "bmi_records"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_HEIGHT = "height"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_BMI = "bmi"
        private const val COLUMN_DATE_TIME = "date_time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_BMI_RECORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME TEXT NOT NULL,
                $COLUMN_HEIGHT REAL NOT NULL,
                $COLUMN_WEIGHT REAL NOT NULL,
                $COLUMN_BMI REAL NOT NULL,
                $COLUMN_DATE_TIME TEXT NOT NULL
            )
        """.trimIndent()

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BMI_RECORDS")
        onCreate(db)
    }

    fun insertBMIRecord(record: BMIRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, record.userName)
            put(COLUMN_HEIGHT, record.height)
            put(COLUMN_WEIGHT, record.weight)
            put(COLUMN_BMI, record.bmi)
            put(COLUMN_DATE_TIME, record.dateTime)
        }

        return db.insert(TABLE_BMI_RECORDS, null, values)
    }

    fun getBMIRecordsByUser(userName: String): List<BMIRecord> {
        val records = mutableListOf<BMIRecord>()
        val db = readableDatabase

        /*SELECT * FROM TABLE_BMI_RECORDS
                WHERE COLUMN_USER_NAME = 'valor_de_userName'
        ORDER BY COLUMN_DATE_TIME DESC;*/
        val cursor = db.query(
            TABLE_BMI_RECORDS,
            null,
            "$COLUMN_USER_NAME = ?",
            arrayOf(userName),
            null,
            null,
            "$COLUMN_DATE_TIME DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val record = BMIRecord(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    userName = it.getString(it.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                    height = it.getDouble(it.getColumnIndexOrThrow(COLUMN_HEIGHT)),
                    weight = it.getDouble(it.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    bmi = it.getDouble(it.getColumnIndexOrThrow(COLUMN_BMI)),
                    dateTime = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE_TIME))
                )
                records.add(record)
            }
        }

        return records
    }
}