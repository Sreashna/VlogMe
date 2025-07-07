package sree.ddukk.vlogapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vlogs")
data class VlogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val mood: String,
    val date: String,
    val videoPath: String
)