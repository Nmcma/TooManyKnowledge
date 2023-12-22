package pers.nmcma.fr.db.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert
    suspend fun insert(dataSaveObj: DataSaveObj)

    @Update
    suspend fun update(dataSaveObj: DataSaveObj)

    @Delete
    suspend fun delete(dataSaveObj: DataSaveObj)

    @Query("SELECT * FROM data")
    fun observeAll(): Flow<List<DataSaveObj>>

    @Query("SELECT * FROM data WHERE name LIKE :strMatchWord")
    fun observeContainsNameData(strMatchWord:String): Flow<List<DataSaveObj>>

    @Query("SELECT * FROM data WHERE description LIKE :strMatchWord")
    fun observeContainsDescriptionData(strMatchWord:String): Flow<List<DataSaveObj>>

    @Query("SELECT * FROM data WHERE keyword_array_str LIKE :strMatchWord")
    fun observeContainsKeywordData(strMatchWord:String): Flow<List<DataSaveObj>>

    @Query("SELECT * FROM data WHERE name =:name")
    suspend fun getData(name:String): DataSaveObj?
}