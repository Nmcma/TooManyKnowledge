package pers.nmcma.fr.ui.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pers.nmcma.fr.db.setting.SettingSaveObj

enum class SearchMode{
    SEARCH_NAME_MODE,
    SEARCH_DESCRIPTION_MODE,
    SEARCH_KEYWORD_MODE;
    fun next():SearchMode{
        return when(this){
            SEARCH_NAME_MODE->SEARCH_DESCRIPTION_MODE
            SEARCH_DESCRIPTION_MODE->SEARCH_KEYWORD_MODE
            SEARCH_KEYWORD_MODE->SEARCH_NAME_MODE
        }
    }
}
class MainViewModel:ViewModel() {
    var dataItemSearchText by mutableStateOf("")
    var dataItemSearchMode by mutableStateOf(SearchMode.SEARCH_NAME_MODE)
    var currentKeywordList = mutableStateListOf<KeywordItem>()
    var usingSystemDynamicColor by mutableStateOf(false)
    var currentTheme by mutableStateOf(SettingSaveObj.DEFAULT_THEME)
}