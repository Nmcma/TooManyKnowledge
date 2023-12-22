package pers.nmcma.fr.db.amb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "amb")
data class AmbSaveObj(
    @PrimaryKey var name: String,
    @ColumnInfo(name="keyword_array_str") val keywordArrayStr:String)