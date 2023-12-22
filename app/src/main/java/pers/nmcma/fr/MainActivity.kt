package pers.nmcma.fr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import pers.nmcma.fr.db.AppDatabase
import pers.nmcma.fr.ui.classes.MainViewModel
import pers.nmcma.fr.ui.controls.AppBar
import pers.nmcma.fr.ui.pages.MainPage
import pers.nmcma.fr.ui.theme.DBDependsTheme
import pers.nmcma.fr.ui.theme.Dimensions

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "main-database"
        ).build()
        setContent {
            DBDependsTheme(vm = viewModel, settingDao = db.settingDao()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val maxHeight =maxHeight
                        Column(modifier = Modifier.fillMaxSize()) {
                            AppBar(modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .fillMaxWidth()
                                .height(Dimensions.StandardTitleHeight),vm=viewModel,dao=db.settingDao())
                            MainPage(modifier = Modifier
                                .fillMaxWidth()
                                .height(maxHeight - Dimensions.StandardTitleHeight), vm = viewModel,db=db)
                        }
                    }
                }
            }
        }
    }
}
