package pers.nmcma.fr.ui.controls

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import pers.nmcma.fr.db.setting.SettingDao
import pers.nmcma.fr.db.setting.SettingSaveObj
import pers.nmcma.fr.db.setting.SettingSaveObj.Companion.DEFAULT_THEME
import pers.nmcma.fr.ui.classes.MainViewModel
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.ui.theme.themeMap
import pers.nmcma.fr.R

@Composable
fun AppBar(modifier: Modifier,vm:MainViewModel,dao:SettingDao){
    var isThemeSelectorExpended by remember { mutableStateOf(false) }
    val saveSettingScope= rememberCoroutineScope()
    BoxWithConstraints(
        modifier = modifier
    ) {
        fun setAndSaveSelectedTheme(themeName:String){
            saveSettingScope.launch {
                val verifiedTheme= if (themeMap.contains(themeName)) themeName else DEFAULT_THEME
                vm.currentTheme=verifiedTheme
                dao.update(SettingSaveObj("default",vm.usingSystemDynamicColor,verifiedTheme))
            }
        }
        val maxWidth =maxWidth
        Row(modifier= Modifier
            .fillMaxWidth()
            .height(Dimensions.StandardTitleHeight)) {
            Text(modifier = Modifier.width(maxWidth- Dimensions.StandardTitleHeight),text = stringResource(
                R.string.app_correct_name
            ), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
            IconButton(onClick = {
                isThemeSelectorExpended=!isThemeSelectorExpended
            }) {
                Icon(imageVector = if (isThemeSelectorExpended) Icons.Filled.Settings else Icons.Outlined.Settings, contentDescription = stringResource(
                    R.string.change_theme
                ),tint= MaterialTheme.colorScheme.onPrimary)
                DropdownMenu(
                    expanded = isThemeSelectorExpended,
                    onDismissRequest = { isThemeSelectorExpended=false }) {
                    DropdownMenuItem(text = {
                        Row {
                            Icon(
                                imageVector = if (vm.usingSystemDynamicColor) Icons.Filled.CheckCircle else Icons.Filled.Info,
                                contentDescription = stringResource(R.string.select_this_theme),
                                tint = MaterialTheme.colorScheme.onSurface)
                            Text(text = stringResource(R.string.system_theme))
                        }
                    }, onClick = {
                        vm.usingSystemDynamicColor=true
                        setAndSaveSelectedTheme(DEFAULT_THEME)
                    })
                    themeMap.forEach { (themeName,themes) ->
                        DropdownMenuItem(text = {
                            Row {
                                Icon(
                                    imageVector = if (vm.currentTheme==themeName&&!vm.usingSystemDynamicColor) Icons.Filled.CheckCircle else Icons.Filled.Info,
                                    contentDescription = stringResource(id = R.string.select_this_theme),
                                    tint = if (isSystemInDarkTheme()) themes.first.primary else themes.second.primary)
                                Text(text = themeName)
                            }
                        }, onClick = {
                            vm.usingSystemDynamicColor=false
                            setAndSaveSelectedTheme(themeName)
                        })
                    }
                }
            }
        }
    }

}