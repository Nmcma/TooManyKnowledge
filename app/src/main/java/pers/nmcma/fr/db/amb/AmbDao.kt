package pers.nmcma.fr.db.amb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AmbDao {
    @Insert
    suspend fun insert(ambSaveObj: AmbSaveObj)

    @Update
    suspend fun update(ambSaveObj: AmbSaveObj)

    @Delete
    suspend fun delete(ambSaveObj: AmbSaveObj)

    @Query("SELECT * FROM amb")
    fun observeAll(): Flow<List<AmbSaveObj>>

    @Query("SELECT * FROM amb WHERE name LIKE :strMatchWord")
    fun observeContainsCharSetting(strMatchWord:String): Flow<List<AmbSaveObj>>

    @Query("SELECT * FROM amb WHERE name =:name")
    suspend fun getAmb(name:String): AmbSaveObj?
}