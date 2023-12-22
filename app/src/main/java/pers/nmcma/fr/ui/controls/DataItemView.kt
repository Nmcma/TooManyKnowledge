package pers.nmcma.fr.ui.controls

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pers.nmcma.fr.db.data.DataDao
import pers.nmcma.fr.db.data.DataSaveObj
import pers.nmcma.fr.ui.dialog.DataItemEditFormDialog
import pers.nmcma.fr.ui.dialog.EnsureDialog
import pers.nmcma.fr.ui.dialog.ReadOnlyDataItemFormDialog
import pers.nmcma.fr.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DataItemSearchResultView(
    modifier: Modifier,
    containedText:String,
    enableDescriptionFinding:Boolean,
    dataItem:DataSaveObj,
    dao:DataDao
){
    var isDetailDialogExpended by remember { mutableStateOf(false) }
    var isItemEditDialogExpended by remember { mutableStateOf(false) }
    var isDropDownMenuExpended by remember { mutableStateOf(false) }
    var isEnsureDeleteDialogVisible by remember { mutableStateOf(false) }
    val editScope = rememberCoroutineScope()
    fun renderName():AnnotatedString{
        return if (containedText.startsWith('^')) {
            buildAnnotatedString {
                containedText.trimStart('^').let { head->
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                        append(head)
                    }
                    append(dataItem.name.substring(head.length))
                }
            }
        }
        else if(containedText.endsWith('$')) {
            buildAnnotatedString {
                containedText.trimEnd('$').let { tail->
                    append(dataItem.name.substring(0,dataItem.name.length-tail.length))
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                        append(tail)
                    }
                }
            }
        }
        else buildAnnotatedString {
            dataItem.name.split(containedText).apply {
                if (size==2&&filter { it.isEmpty() }.size==2)withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                    append(containedText)
                }else forEachIndexed { i, it->
                    append(it)
                    if(i!=lastIndex) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                            append(containedText)
                        }
                    }
                }
            }
        }
    }
    Column(modifier
        .padding(2.dp)
        .combinedClickable(
            onClick = {
                isDetailDialogExpended = true
            },
            onLongClick = {
                isDropDownMenuExpended = true
            }
        )) {
        Text(text = renderName(), color = MaterialTheme.colorScheme.onSurface)
        Text(text = if (enableDescriptionFinding){
            dataItem.description
        }else {
            ""
        })
        DropdownMenu(
            expanded = isDropDownMenuExpended,
            onDismissRequest = { isDropDownMenuExpended = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.edit)) }, onClick = { isItemEditDialogExpended=true;isDropDownMenuExpended=false })
            DropdownMenuItem(text = { Text(text = stringResource(R.string.delete)) }, onClick = { isEnsureDeleteDialogVisible=true;isDropDownMenuExpended=false })
        }
        DataItemEditFormDialog(
            modifier =Modifier,
            title = stringResource(R.string.edit_data_information),
            visible = isItemEditDialogExpended,
            close = { isItemEditDialogExpended=false },
            initDataItem = dataItem,
            scope = editScope,
            dao = dao,
            allowObjCover = true
        )
        ReadOnlyDataItemFormDialog(
            modifier = Modifier,
            title = stringResource(R.string.data_information),
            visible = isDetailDialogExpended,
            close = { isDetailDialogExpended=false },
            dataItem = dataItem
        )
        EnsureDialog(
            title = stringResource(R.string.delete_this_item),
            content = buildAnnotatedString {
                append(stringResource(R.string.do_you_want_to_delete_the_item))
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                    append(dataItem.name)
                }
                append(stringResource(id = R.string.query_end_signal))
            },
            visible = isEnsureDeleteDialogVisible, close = {isEnsureDeleteDialogVisible=false}, doWhat = {
                dao.delete(dataItem)
                isEnsureDeleteDialogVisible=false
        })
    }
}