package pers.nmcma.fr.db.setting

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert
    suspend fun insert(settingSaveObj:SettingSaveObj)

    @Update
    suspend fun update(settingSaveObj:SettingSaveObj)

    @Delete
    suspend fun delete(settingSaveObj:SettingSaveObj)

    @Query("SELECT * FROM settings")
    fun observeAll(): Flow<List<SettingSaveObj>>

    @Query("SELECT * FROM settings WHERE name =:name")
    suspend fun getSetting(name:String): SettingSaveObj?
}