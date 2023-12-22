package pers.nmcma.fr.db.setting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
class SettingSaveObj(
    @PrimaryKey val name:String,
    @ColumnInfo(name = "using_system_color") val usingSystemColor:Boolean,
    @ColumnInfo(name = "theme_type") val themeType:String
){
    companion object{
        const val DEFAULT_THEME="default"
        const val GREEN_THEME="green"
        const val BLUE_THEME="blue"
        const val ORANGE_THEME="orange"
        const val PURPLE_THEME="purple"
        const val YELLOW_THEME="yellow"
        const val PINK_THEME="pink"
    }
}