package pers.nmcma.fr.ui.controls

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.ui.theme.cycleColor

@Composable
fun KeywordAppendView(
    modifier: Modifier,
    keywordList:SnapshotStateList<String>
){
    Row(
        modifier
            .padding(2.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface)) {
        BoxWithConstraints(modifier=Modifier.fillMaxWidth()) {
            LazyRow(modifier= Modifier.fillMaxSize()){
                itemsIndexed(keywordList) {i,it->
                    KeywordItemView(
                        modifier = Modifier.height(Dimensions.StandardTextEditHeight),
                        content = it,
                        color = cycleColor(i),
                        selected = true,
                        setSelected = {},
                        requestDelete = {keywordList.removeAt(i)})
                }
            }
        }
    }
}

@Composable
fun ReadOnlyKeywordAppendView(
    modifier: Modifier,
    keywordList:List<String>
){
    Row(
        modifier
            .padding(2.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface)) {
        BoxWithConstraints(modifier=Modifier.fillMaxWidth()) {
            LazyRow(modifier= Modifier.fillMaxSize()){
                itemsIndexed(keywordList) {i,it->
                    KeywordItemView(
                        modifier = Modifier.height(Dimensions.StandardTextEditHeight),
                        content = it,
                        color = cycleColor(i),
                        selected = true,
                        setSelected = {},
                        requestDelete = {},
                        enableOperations = false)
                }
            }
        }
    }
}