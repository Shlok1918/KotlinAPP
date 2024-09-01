package com.shlokyadav.definelabskotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shlokyadav.definelabskotlin.ui.allmatches.Venue

class VenueDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "venues.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "saved_venues"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_CROSS_STREET = "cross_street"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID TEXT PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PHONE TEXT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_CROSS_STREET TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addVenue(venue: Venue): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, venue.id)
            put(COLUMN_NAME, venue.name)
            put(COLUMN_PHONE, venue.formattedPhone)
            put(COLUMN_ADDRESS, venue.address)
            put(COLUMN_CROSS_STREET, venue.crossStreet)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun getAllSavedVenues(): List<Venue> {
        val venues = mutableListOf<Venue>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val venue = Venue(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CROSS_STREET))
                )
                venues.add(venue)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return venues
    }

    fun removeVenue(venueId: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(venueId))
        db.close()
        return result > 0
    }

    fun isVenueSaved(venueId: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, "$COLUMN_ID = ?", arrayOf(venueId), null, null, null)
        val isSaved = cursor.count > 0
        cursor.close()
        db.close()
        return isSaved
    }
}