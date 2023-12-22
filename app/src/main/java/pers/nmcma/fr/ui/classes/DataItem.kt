package pers.nmcma.fr.ui.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DataItem(initName:String,initDescription:String,initKeywords:List<String>) {
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var keywords = mutableStateListOf<String>()
    init {
        name=initName
        description=initDescription
        keywords.addAll(initKeywords)
    }
}