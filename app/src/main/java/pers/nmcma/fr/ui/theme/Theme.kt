package pers.nmcma.fr.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch
import pers.nmcma.fr.db.setting.SettingDao
import pers.nmcma.fr.db.setting.SettingSaveObj
import pers.nmcma.fr.ui.classes.MainViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

private val LightGreenColorScheme = lightColorScheme(
    primary = Color(0xFF79C13B),
    secondary = Color(0xFF9EFA4F),
    tertiary = Color(0xFF00B047)
)

private val DarkGreenColorScheme = darkColorScheme(
    primary = Color(0xFF79C13B),
    secondary = Color(0xFF9EFA4F),
    tertiary = Color(0xFF00B047),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF)

)

private val LightBlueColorScheme = lightColorScheme(
    primary = Color(0xFF00A9F9),
    secondary = Color(0xFF26FEDE),
    tertiary = Color(0xFF0078D3)

)

private val DarkBlueColorScheme = darkColorScheme(
    primary = Color(0xFF00A9F9),
    secondary = Color(0xFF26FEDE),
    tertiary = Color(0xFF0078D3),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
)

private val LightOrangeColorScheme = lightColorScheme(
    primary = Color(0xFFFF9800),
    secondary = Color(0xFFFFC107),
    tertiary = Color(0xFFFF5722)

)

private val DarkOrangeColorScheme = darkColorScheme(
    primary = Color(0xFFFF9800),
    secondary = Color(0xFFFFC107),
    tertiary = Color(0xFFFF5722),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
)

private val LightPurpleColorScheme = lightColorScheme(
    primary = Color(0xFFA668F3),
    secondary = Color(0xFFBC89FF),
    tertiary = Color(0xFF6E1FCE)
)

private val DarkPurpleColorScheme = darkColorScheme(
    primary = Color(0xFFA668F3),
    secondary = Color(0xFFBC89FF),
    tertiary = Color(0xFF6E1FCE),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF)
)

private val LightYellowColorScheme = lightColorScheme(
    primary = Color(0xFFF3E700),
    secondary = Color(0xFFFFFC2D),
    tertiary = Color(0xFFFFC107)

)

private val DarkYellowColorScheme = darkColorScheme(
    primary = Color(0xFFF3E700),
    secondary = Color(0xFFFFFC2D),
    tertiary = Color(0xFFFFC107),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
)

private val LightPinkColorScheme = lightColorScheme(
    primary = Color(0xFFFF58E3),
    secondary = Color(0xFFFF91ED),
    tertiary = Color(0xFFD517B5)

)

private val DarkPinkColorScheme = darkColorScheme(
    primary = Color(0xFFFF58E3),
    secondary = Color(0xFFFF91ED),
    tertiary = Color(0xFFD517B5),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
)

val themeMap= linkedMapOf(
    SettingSaveObj.DEFAULT_THEME to Pair(LightColorScheme, DarkColorScheme),
    SettingSaveObj.BLUE_THEME to Pair(LightBlueColorScheme, DarkBlueColorScheme),
    SettingSaveObj.GREEN_THEME to Pair(LightGreenColorScheme, DarkGreenColorScheme),
    SettingSaveObj.ORANGE_THEME to Pair(LightOrangeColorScheme, DarkOrangeColorScheme),
    SettingSaveObj.PURPLE_THEME to Pair(LightPurpleColorScheme, DarkPurpleColorScheme),
    SettingSaveObj.YELLOW_THEME to Pair(LightYellowColorScheme, DarkYellowColorScheme),
    SettingSaveObj.PINK_THEME to Pair(LightPinkColorScheme, DarkPinkColorScheme)
)

@Composable
fun DBDependsTheme(
    vm:MainViewModel,
    settingDao:SettingDao,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val settingLoaderScope = rememberCoroutineScope()
    fun initThemeDb(){
        settingLoaderScope.launch {
            (settingDao.getSetting("default") ?: run {
                SettingSaveObj("default", true,SettingSaveObj.DEFAULT_THEME).apply {
                    settingDao.insert(this)
                }
            }).let {
                vm.currentTheme=it.themeType
            }
        }
    }
    initThemeDb()
    val colorScheme = if(vm.usingSystemDynamicColor){
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }else when {
        darkTheme -> themeMap[vm.currentTheme]?.second ?: DarkColorScheme
        else -> themeMap[vm.currentTheme]?.first ?: LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

