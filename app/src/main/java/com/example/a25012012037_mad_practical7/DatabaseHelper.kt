package com.example.a25012012037_mad_practical7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1
        // Database Name
        private const val DATABASE_NAME = "persons_db"
    }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        // create persons table
        db.execSQL(PersonDbTableData.CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + PersonDbTableData.TABLE_NAME)
        // Create tables again
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        val db = this.writableDatabase
        val values = getValues(person)
        val id = db.insertWithOnConflict(
            PersonDbTableData.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
        return id
    }

    private fun getValues(person: Person): ContentValues {
        val values = ContentValues()
        values.put(PersonDbTableData.COLUMN_ID, person.id)
        values.put(PersonDbTableData.COLUMN_PERSON_NAME, person.name)
        values.put(PersonDbTableData.COLUMN_PERSON_EMAIL_ID, person.emailId)
        values.put(PersonDbTableData.COLUMN_PERSON_PHONE_NO, person.phoneNo)
        values.put(PersonDbTableData.COLUMN_PERSON_ADDRESS, person.address)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LAT, person.latitude)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LONG, person.longitude)
        return values
    }

    fun getPerson(id: String): Person? {
        val db = this.readableDatabase
        val cursor = db.query(
            PersonDbTableData.TABLE_NAME,
            arrayOf(
                PersonDbTableData.COLUMN_ID,
                PersonDbTableData.COLUMN_PERSON_NAME,
                PersonDbTableData.COLUMN_PERSON_EMAIL_ID,
                PersonDbTableData.COLUMN_PERSON_PHONE_NO,
                PersonDbTableData.COLUMN_PERSON_ADDRESS,
                PersonDbTableData.COLUMN_PERSON_GPS_LAT,
                PersonDbTableData.COLUMN_PERSON_GPS_LONG
            ),
            "${PersonDbTableData.COLUMN_ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
        var person: Person? = null
        if (cursor.moveToFirst()) {
            person = getPerson(cursor)
        }
        cursor.close()
        db.close()
        return person
    }

    private fun getPerson(cursor: Cursor): Person {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_NAME))
        val emailId = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_EMAIL_ID))
        val phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_PHONE_NO))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_ADDRESS))
        val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LAT))
        val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LONG))
        return Person(id, name, emailId, phoneNo, address, latitude, longitude)
    }

    // Select All Query
    val allPersons: ArrayList<Person>
        get() {
            val personList = ArrayList<Person>()
            val selectQuery = "SELECT * FROM ${PersonDbTableData.TABLE_NAME}"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    personList.add(getPerson(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return personList
        }

    val personsCount: Int
        get() {
            val countQuery = "SELECT * FROM ${PersonDbTableData.TABLE_NAME}"
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()
            db.close()
            return count
        }

    fun updatePerson(person: Person): Int {
        val db = this.writableDatabase
        val values = getValues(person)
        val count = db.update(
            PersonDbTableData.TABLE_NAME,
            values,
            "${PersonDbTableData.COLUMN_ID} = ?",
            arrayOf(person.id)
        )
        db.close()
        return count
    }

    fun deletePerson(person: Person): Int {
        val db = this.writableDatabase
        val count = db.delete(
            PersonDbTableData.TABLE_NAME,
            "${PersonDbTableData.COLUMN_ID} = ?",
            arrayOf(person.id)
        )
        db.close()
        return count
    }
}

class PersonDbTableData {
    companion object {
        const val TABLE_NAME = "persons"

        const val COLUMN_ID = "id"
        const val COLUMN_PERSON_NAME = "person_name"
        const val COLUMN_PERSON_EMAIL_ID = "person_email_id"
        const val COLUMN_PERSON_PHONE_NO = "person_phone_no"
        const val COLUMN_PERSON_ADDRESS = "person_address"
        const val COLUMN_PERSON_GPS_LAT = "person_lat"
        const val COLUMN_PERSON_GPS_LONG = "person_long"

        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_PERSON_NAME + " TEXT,"
                + COLUMN_PERSON_EMAIL_ID + " TEXT,"
                + COLUMN_PERSON_PHONE_NO + " TEXT,"
                + COLUMN_PERSON_ADDRESS + " TEXT,"
                + COLUMN_PERSON_GPS_LAT + " REAL,"
                + COLUMN_PERSON_GPS_LONG + " REAL"
                + ")")
    }
}
