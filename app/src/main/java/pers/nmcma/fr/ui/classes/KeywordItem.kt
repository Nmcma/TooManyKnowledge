package pers.nmcma.fr.ui.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class KeywordItem(initSelected:Boolean,initContent:String) {
    var selected by mutableStateOf(false)
    var content by mutableStateOf("")
    init {
        selected=initSelected
        content=initContent
    }
}