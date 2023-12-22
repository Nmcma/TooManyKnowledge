package pers.nmcma.fr.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pers.nmcma.fr.db.amb.AmbDao
import pers.nmcma.fr.db.amb.AmbSaveObj
import pers.nmcma.fr.db.data.DataDao
import pers.nmcma.fr.db.data.DataSaveObj
import pers.nmcma.fr.db.setting.SettingDao
import pers.nmcma.fr.db.setting.SettingSaveObj

@Database(entities = [DataSaveObj::class,AmbSaveObj::class,SettingSaveObj::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
    abstract fun contextDao(): AmbDao
    abstract fun settingDao():SettingDao
}