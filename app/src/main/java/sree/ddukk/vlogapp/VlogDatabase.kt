package sree.ddukk.vlogapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VlogEntry::class], version = 1)
abstract class VlogDatabase : RoomDatabase() {
    abstract fun vlogDao(): VlogDao
}