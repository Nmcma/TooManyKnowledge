package pers.nmcma.fr.ui.controls

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeywordItemView(modifier: Modifier, content:String, color: Color, selected:Boolean, setSelected:(Boolean)->Unit, requestDelete:()->Unit,enableOperations:Boolean=true) {
    var isDeleteMenuExpended by remember { mutableStateOf(false) }
    @Composable
    fun renderText():String{
        val replaceContent=if (content.startsWith('^')) content.trimStart('^')
        else if(content.endsWith('$'))  content.trimEnd('$')
        else content

        val replacedSentence= if (content.startsWith('^')) stringResource(id = R.string.start_with_what)
        else if(content.endsWith('$')) stringResource(R.string.end_with_what)
        else content

        return replacedSentence.replace("@",replaceContent)
    }
    Box(modifier = modifier
        .defaultMinSize(minWidth = Dimensions.StandardKeywordMinWidth)
        .height(Dimensions.StandardKeywordHeight)
        .padding(4.dp)
        .border(1.dp, color)
        .background(if (selected) color else MaterialTheme.colorScheme.surface)
        .combinedClickable(
            onLongClick = {
                isDeleteMenuExpended = true
            },
            onClick = {
                setSelected(!selected)
            }
        )
    ){
        Text(modifier= modifier.align(Alignment.Center)
            , text = renderText(),color=if (selected) MaterialTheme.colorScheme.surface else color)
        DropdownMenu(
            expanded = enableOperations&&isDeleteMenuExpended,
            onDismissRequest = { isDeleteMenuExpended = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete )) }, onClick = { requestDelete();isDeleteMenuExpended=false })
        }
    }


}