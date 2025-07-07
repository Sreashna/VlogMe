package sree.ddukk.vlogapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VlogDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVlog(vlog: VlogEntry)
    @Query("SELECT * FROM vlogs ORDER BY id DESC")
    fun getAllVlogs(): Flow<List<VlogEntry>>
    @Delete
    suspend fun delete(vlog: VlogEntry)


}