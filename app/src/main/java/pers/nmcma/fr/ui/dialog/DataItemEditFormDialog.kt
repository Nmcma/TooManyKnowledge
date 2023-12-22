package pers.nmcma.fr.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pers.nmcma.fr.db.data.DataDao
import pers.nmcma.fr.db.data.DataSaveObj
import pers.nmcma.fr.ui.controls.KeywordAppendView
import pers.nmcma.fr.ui.controls.ReadOnlyKeywordAppendView
import pers.nmcma.fr.ui.controls.TextEdit
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.R

const val UNCHECKED=0
const val CHECKING=1
const val ERROR_COUNTERED=2

@Composable
fun DataItemEditFormDialog(
    modifier: Modifier,
    title:String,
    visible:Boolean,
    close:()->Unit,
    initDataItem:DataSaveObj,
    scope:CoroutineScope,
    dao:DataDao,
    allowObjCover:Boolean
) {
    if (visible) Dialog(onDismissRequest = {}) {
        val dataItem by remember {
            mutableStateOf(initDataItem.dataItem())
        }
        var nameCheckCondition by remember {
            mutableIntStateOf(UNCHECKED)
        }
        var requestAddKeywordDialogVisible by remember { mutableStateOf(false) }
        BoxWithConstraints(modifier.background(MaterialTheme.colorScheme.surface)) {
            val maxWidth=maxWidth
            Column {
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight)
                    .background(MaterialTheme.colorScheme.primary), textAlign = TextAlign.Justify,text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(
                    R.string.name
                ), style = MaterialTheme.typography.titleLarge)
                TextEdit(modifier= Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight),valueString =dataItem.name, updateText ={ dataItem.name=it;nameCheckCondition= UNCHECKED })
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight)) {
                    Text(modifier= Modifier
                        .width(maxWidth - Dimensions.StandardTextEditHeight)
                        .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(
                        R.string.keywords), style = MaterialTheme.typography.titleLarge)
                    IconButton(
                        modifier= Modifier
                            .padding(2.dp)
                            .size(Dimensions.StandardTextEditHeight),
                        onClick = { requestAddKeywordDialogVisible=true }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(R.string.add_new_keyword), tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
                KeywordAppendView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.StandardTextEditHeight),
                    keywordList =dataItem.keywords
                )
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(
                    R.string.descriptions
                ), style = MaterialTheme.typography.titleLarge)
                TextEdit(modifier= Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight * 5),valueString =dataItem.description, updateText ={ dataItem.description=it })
                Row (
                    modifier=Modifier.height(Dimensions.StandardTextEditHeight)
                ){
                    Button(modifier = Modifier
                        .padding(1.dp)
                        .width(maxWidth / 2)
                        , shape = RectangleShape,
                        onClick = {
                            if (nameCheckCondition== UNCHECKED){
                                nameCheckCondition= CHECKING
                                scope.launch {
                                    dao.getData(dataItem.name)?.let {
                                        nameCheckCondition = if (allowObjCover){
                                            dao.update(DataSaveObj(dataItem))
                                            close()
                                            UNCHECKED
                                        }else ERROR_COUNTERED
                                    }?:run {
                                        nameCheckCondition= UNCHECKED
                                        dao.insert(DataSaveObj(dataItem))
                                        close()
                                    }
                                }
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = if (nameCheckCondition== ERROR_COUNTERED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )) {
                        Text(text = stringResource(R.string.apply), color = if (nameCheckCondition== ERROR_COUNTERED) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary)
                    }
                    Button(modifier = Modifier
                        .padding(1.dp)
                        .width(maxWidth / 2), shape = RectangleShape, onClick = {
                        close()
                    }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
            AddKeywordStringDialog(visible = requestAddKeywordDialogVisible, close = { requestAddKeywordDialogVisible=false }, keywordStringItemList = dataItem.keywords)
        }
    }
}

@Composable
fun ReadOnlyDataItemFormDialog(
    modifier: Modifier,
    title:String,
    visible:Boolean,
    close:()->Unit,
    dataItem:DataSaveObj
) {
    if (visible) Dialog(onDismissRequest = { close() }) {
        BoxWithConstraints(modifier.background(MaterialTheme.colorScheme.surface)) {
            Column {
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight)
                    .background(MaterialTheme.colorScheme.primary), textAlign = TextAlign.Justify,text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(R.string.name), style = MaterialTheme.typography.titleLarge)
                Text(modifier= Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight),text =dataItem.name)
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(R.string.keywords), style = MaterialTheme.typography.titleLarge)
                ReadOnlyKeywordAppendView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.StandardTextEditHeight),
                    keywordList =dataItem.keywordArrayStr.split(',').filterNot { it.isBlank() }
                )
                Text(modifier= Modifier
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(R.string.descriptions), style = MaterialTheme.typography.titleLarge)
                LazyColumn(modifier= Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight * 5)
                ){
                    item {
                        Text(text =dataItem.description)
                    }
                }
                Button(modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
                    .height(Dimensions.StandardTextEditHeight), shape = RectangleShape, onClick = {
                    close()
                }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        }
    }
}