package pers.nmcma.fr.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pers.nmcma.fr.ui.classes.DataItem

@Entity( tableName = "data")
data class DataSaveObj(
    @PrimaryKey var name: String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name="keyword_array_str") val keywordArrayStr:String){
    constructor(dataItem:DataItem):this(dataItem.name,dataItem.description,dataItem.keywords.run {
        val strBuilder=StringBuilder()
        for(it in this){
            strBuilder.append(it).append(',')
        }
        return@run if (strBuilder.endsWith(',')) strBuilder.trimEnd(',').toString()
        else strBuilder.toString()
    })
    fun dataItem()= DataItem(name,description,keywordArrayStr.split(',').filterNot { it.isBlank() })
}