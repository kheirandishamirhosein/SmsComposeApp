package com.example.smscomposeapp.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE SmsModel ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
    }
}
