package pers.nmcma.fr.ui.dialog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pers.nmcma.fr.R
import pers.nmcma.fr.db.amb.AmbDao
import pers.nmcma.fr.db.amb.AmbSaveObj
import pers.nmcma.fr.ui.controls.KeywordItemView
import pers.nmcma.fr.ui.controls.TextEdit
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.ui.theme.cycleColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmbSelectorDialog(
    visible:Boolean,
    close:()->Unit,
    initSearchText: String?,
    canvasDataSelected:(List<AmbSaveObj>)->Unit,
    dao: AmbDao
){
    if (visible) Dialog(onDismissRequest = { close() }) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            var searchContent by remember { mutableStateOf(initSearchText?:"") }
            val selectedItems = remember { mutableStateListOf<AmbSaveObj>() }
            val searchResult by dao
                .observeContainsCharSetting("%${
                    searchContent.ifBlank { "#" }
                }%").collectAsState(initial = listOf())
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTitleHeight)
                    .background(MaterialTheme.colorScheme.primary),
                text = stringResource(R.string.import_keywords),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            TextEdit(modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
                .height(Dimensions.StandardTextEditHeight)
                .border(1.dp, MaterialTheme.colorScheme.primary),
                valueString = searchContent,
                updateText = {
                    searchContent = it
                })
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTitleHeight),
                text = stringResource(R.string.selected_items), style = MaterialTheme.typography.titleLarge
            )
            LazyRow(
                modifier = Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight)
            ){
                itemsIndexed(selectedItems){i,it->
                    KeywordItemView(
                        modifier = Modifier.height(Dimensions.StandardTextEditHeight),
                        content =it.name,
                        color = cycleColor(1),
                        selected =true,
                        setSelected ={
                            selectedItems.removeAt(i)
                        },
                        requestDelete = { selectedItems.removeAt(i) })
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight * 5)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                itemsIndexed(searchResult) { _, it ->
                    var itemOperationMenuExpended by remember { mutableStateOf(false) }
                    var isEnsureDeleteDialogVisible by remember { mutableStateOf(false) }
                    var renameItemDialogVisible by remember { mutableStateOf(false) }
                    Box(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                        .height(Dimensions.StandardTextEditHeight)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .combinedClickable(
                            onClick = {
                                if (selectedItems.find { r -> it.name == r.name } == null) {
                                    selectedItems.add(it)
                                }
                            },
                            onLongClick = {
                                itemOperationMenuExpended = true
                            }
                        )) {
                        Text(text = it.name, color = MaterialTheme.colorScheme.onPrimary)
                        DropdownMenu(expanded = itemOperationMenuExpended, onDismissRequest = { itemOperationMenuExpended=false }) {
                            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.rename)) }, onClick = {
                                renameItemDialogVisible=true
                            })
                            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete)) }, onClick = {
                                isEnsureDeleteDialogVisible=true
                            })
                        }
                        EnsureDialog(
                            title = "${stringResource(R.string.delete_amb)}${it.name}",
                            content = buildAnnotatedString {
                                append(stringResource(R.string.do_you_want_to_delete_the_amb))
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                                    append(it.name)
                                }
                                append(stringResource(R.string.query_end_signal))
                            },
                            visible = isEnsureDeleteDialogVisible, close ={isEnsureDeleteDialogVisible=false}, doWhat = {
                                dao.delete(it)
                                close()
                            })
                        SingleValueEditDialog(
                            title = stringResource(R.string.rename),
                            initValueString = it.name,
                            visible =renameItemDialogVisible, close = { renameItemDialogVisible=false },
                            checkValue ={
                                dao.getAmb(it)?.let {
                                    false
                                }?:true
                            },
                            valueEdited = { newName ->
                                dao.getAmb(newName)?.let { oldObj -> dao.delete(oldObj) }
                                dao.insert(it.apply{name=newName})
                                close()
                            }
                        )
                    }
                }
            }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight)
            ) {
                val maxWidth=maxWidth
                Row {
                    Button(modifier = Modifier
                        .padding(1.dp)
                        .width(maxWidth / 2)
                        , shape = RectangleShape, onClick = {
                            canvasDataSelected(selectedItems)
                            close()
                        }) {
                        Text(text = stringResource(R.string.apply), color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Button(modifier = Modifier
                        .padding(1.dp)
                        .width(maxWidth / 2), shape = RectangleShape, onClick = {
                        close()
                    }) {
                        Text(text = stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

        }
    }
}