package pers.nmcma.fr.ui.pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pers.nmcma.fr.R
import pers.nmcma.fr.db.AppDatabase
import pers.nmcma.fr.db.data.DataSaveObj
import pers.nmcma.fr.ui.classes.KeywordItem
import pers.nmcma.fr.ui.classes.MainViewModel
import pers.nmcma.fr.ui.classes.SearchMode.SEARCH_DESCRIPTION_MODE
import pers.nmcma.fr.ui.classes.SearchMode.SEARCH_KEYWORD_MODE
import pers.nmcma.fr.ui.classes.SearchMode.SEARCH_NAME_MODE
import pers.nmcma.fr.ui.controls.DataItemSearchResultView
import pers.nmcma.fr.ui.controls.KeywordItemView
import pers.nmcma.fr.ui.controls.TextEdit
import pers.nmcma.fr.ui.dialog.AddKeywordItemDialog
import pers.nmcma.fr.ui.dialog.AmbSelectorDialog
import pers.nmcma.fr.ui.dialog.DataItemEditFormDialog
import pers.nmcma.fr.ui.dialog.SaveNewSettingDialog
import pers.nmcma.fr.ui.theme.Dimensions
import pers.nmcma.fr.ui.theme.cycleColor

@Composable
fun MainPage(modifier: Modifier,vm:MainViewModel,db:AppDatabase){
    val dbAccessScope = rememberCoroutineScope()
    var requestSaveSettingDialogVisible by remember { mutableStateOf(false) }
    var requestImportSettingDialogVisible by remember { mutableStateOf(false) }
    var requestAddKeywordDialogVisible by remember { mutableStateOf(false) }
    var requestNewDataDialogVisible by remember { mutableStateOf(false) }
    val itemNameSearchResult by db.dataDao().observeContainsNameData("%${vm.dataItemSearchText.let{ it.ifBlank { "#" } }}%").collectAsState(initial = listOf())
    val itemDescriptionSearchResult by db.dataDao().observeContainsDescriptionData("%${vm.dataItemSearchText.let{ it.ifBlank { "#" } }}%").collectAsState(initial = listOf())
    val itemKeywordSearchResult by db.dataDao().observeContainsKeywordData("%${vm.dataItemSearchText.let{ it.ifBlank { "#" } }}%").collectAsState(initial = listOf())
    val currentSelectedKeywords=vm.currentKeywordList.filter { it.selected }.map { it.content }
    BoxWithConstraints(modifier) {
        val maxWidth=maxWidth
        val maxHeight=maxHeight
        var isKeywordSelectorFolded by remember { mutableStateOf(false) }
        Column(modifier=Modifier.fillMaxSize()) {
            Row(modifier= Modifier
                .padding(2.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(Dimensions.StandardTextEditHeight)){
                TextEdit(modifier= Modifier
                    .padding(2.dp)
                    .width(maxWidth-Dimensions.StandardTextEditHeight)
                    .height(Dimensions.StandardTextEditHeight),valueString =vm.dataItemSearchText, updateText ={ vm.dataItemSearchText=it }, hint = stringResource(id = R.string.main_search_input))
                IconButton(
                    modifier= Modifier
                        .size(Dimensions.StandardTextEditHeight),
                    onClick = { vm.dataItemSearchText="" }) {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = stringResource(
                        R.string.delete
                    ), tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Row(modifier= Modifier
                .fillMaxWidth()
                .height(Dimensions.StandardTextEditHeight)) {
                Row(modifier= Modifier
                    .animateContentSize { _, _ -> }
                    .width(maxWidth - Dimensions.StandardTextEditHeight)
                    .height(Dimensions.StandardTextEditHeight)) {
                    Text(modifier= Modifier
                        .width(maxWidth - Dimensions.StandardTextEditHeight * (if (isKeywordSelectorFolded) 1 else 4))
                        .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(id = R.string.keywords), style = MaterialTheme.typography.titleLarge)

                    Row(modifier= Modifier
                        .animateContentSize { _, _ -> }
                        .width(if (isKeywordSelectorFolded) 0.dp else Dimensions.StandardTextEditHeight * 3)
                        .height(Dimensions.StandardTextEditHeight)) {
                        IconButton(
                            modifier= Modifier
                                .padding(2.dp)
                                .size(Dimensions.StandardTextEditHeight),
                            onClick = { requestSaveSettingDialogVisible=true }) {
                            Icon(imageVector = Icons.Outlined.Email, contentDescription = stringResource(
                                R.string.save
                            ), tint = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(
                            modifier= Modifier
                                .padding(2.dp)
                                .size(Dimensions.StandardTextEditHeight),
                            onClick = { requestImportSettingDialogVisible=true }) {
                            Icon(imageVector = Icons.Outlined.ExitToApp, contentDescription = stringResource(
                                R.string.import_amb
                            ), tint = MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(
                            modifier= Modifier
                                .padding(2.dp)
                                .size(Dimensions.StandardTextEditHeight),
                            onClick = { requestAddKeywordDialogVisible=true }) {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(
                                R.string.add
                            ), tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                IconButton(
                    modifier= Modifier
                        .padding(2.dp)
                        .width(Dimensions.StandardTextEditHeight)
                        .height(Dimensions.StandardTextEditHeight),
                    onClick = { isKeywordSelectorFolded=!isKeywordSelectorFolded }) {
                    Icon(modifier=Modifier.rotate(if (isKeywordSelectorFolded) 180f else 0f), imageVector = Icons.Outlined.ArrowDropDown, contentDescription = stringResource(
                        R.string.unfold
                    ), tint = MaterialTheme.colorScheme.onSurface)
                }
            }

            LazyRow(modifier= Modifier
                .animateContentSize { _, _ -> }
                .padding(2.dp)
                .fillMaxWidth()
                .height(if (isKeywordSelectorFolded) 0.dp else Dimensions.StandardTextEditHeight)
                .border(1.dp, MaterialTheme.colorScheme.primary)){
                itemsIndexed(vm.currentKeywordList){i,item->
                    KeywordItemView(
                        modifier = Modifier,
                        content = item.content,
                        color = cycleColor(i),
                        selected = item.selected,
                        setSelected = { item.selected = it },
                        requestDelete = {vm.currentKeywordList.removeAt(i)}
                    )
                }
            }
            var displayDescription by remember {mutableStateOf(false)}
            Row(modifier= Modifier
                .fillMaxWidth()
                .height(Dimensions.StandardTextEditHeight)) {
                Text(modifier= Modifier
                    .width(maxWidth - Dimensions.StandardTextEditHeight * 3)
                    .height(Dimensions.StandardTextEditHeight), textAlign = TextAlign.Justify,text = stringResource(
                    R.string.matched_items
                ), style = MaterialTheme.typography.titleLarge)
                IconButton(
                    modifier= Modifier
                        .padding(2.dp)
                        .size(Dimensions.StandardTextEditHeight),
                    onClick = { vm.dataItemSearchMode=vm.dataItemSearchMode.next() }) {
                    Icon(imageVector =
                    when(vm.dataItemSearchMode){
                        SEARCH_NAME_MODE->Icons.Outlined.AccountCircle
                        SEARCH_KEYWORD_MODE->Icons.Outlined.Share
                        SEARCH_DESCRIPTION_MODE->Icons.Outlined.Menu
                    }, contentDescription = stringResource(R.string.switch_search_mode), tint = MaterialTheme.colorScheme.onSurface)
                }
                IconButton(
                    modifier= Modifier
                        .padding(2.dp)
                        .size(Dimensions.StandardTextEditHeight),
                    onClick = { displayDescription=!displayDescription }) {
                    Icon(imageVector =
                        if (displayDescription) Icons.Filled.Email else Icons.Outlined.Email
                        , contentDescription = stringResource(R.string.show_description), tint = MaterialTheme.colorScheme.onSurface)
                }
                IconButton(
                    modifier= Modifier
                        .padding(2.dp)
                        .size(Dimensions.StandardTextEditHeight),
                    onClick = { requestNewDataDialogVisible=true }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(R.string.add_new_item), tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            LazyColumn(modifier= Modifier
                .animateContentSize { _, _ -> }
                .padding(2.dp)
                .fillMaxWidth()
                .height(maxHeight - Dimensions.StandardTextEditHeight * (if (isKeywordSelectorFolded) 3 else 4))
                .border(1.dp, MaterialTheme.colorScheme.primary)){
                items((when(vm.dataItemSearchMode){
                            SEARCH_NAME_MODE ->itemNameSearchResult
                            SEARCH_DESCRIPTION_MODE ->itemDescriptionSearchResult
                            SEARCH_KEYWORD_MODE ->itemKeywordSearchResult
                        }).filter { saveObj->
                    currentSelectedKeywords.forEach { keywordItem ->
                        if (keywordItem.startsWith('^')){
                            if (!saveObj.name.startsWith(keywordItem.trimStart('^'))) return@filter false
                        }else if(keywordItem.endsWith('$')){
                            if (!saveObj.name.endsWith(keywordItem.trimEnd('$'))) return@filter false
                        }else{
                            if (!saveObj.keywordArrayStr.contains(keywordItem)) return@filter false
                        }
                    }
                    true
                }){
                    DataItemSearchResultView(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.secondary)
                            .fillMaxWidth()
                            .height(Dimensions.StandardTextEditHeight),
                        containedText = vm.dataItemSearchText,
                        enableDescriptionFinding=displayDescription,
                        dataItem= it,
                        dao = db.dataDao()
                    )
                }
            }
        }
    }
    AddKeywordItemDialog(visible = requestAddKeywordDialogVisible, close = { requestAddKeywordDialogVisible=false }, keywordItemList = vm.currentKeywordList)
    SaveNewSettingDialog(
        visible = requestSaveSettingDialogVisible,
        close = { requestSaveSettingDialogVisible=false },
        keywordItemStringList = vm.currentKeywordList.map { it.content },
        scope = dbAccessScope,
        dao = db.contextDao(),
        allowCover = true
    )
    AmbSelectorDialog(
        visible = requestImportSettingDialogVisible,
        close = { requestImportSettingDialogVisible = false },
        initSearchText = "",
        canvasDataSelected = { ambSaveObjList ->
            vm.currentKeywordList.clear()
            ambSaveObjList.forEach { amb->
                vm.currentKeywordList.addAll(
                    amb.keywordArrayStr.split(',').filter {
                        it.isNotBlank()
                                &&vm.currentKeywordList.find {r-> r.content==it }==null
                    }.map { KeywordItem(true,it) }
                )
            }
        },
        dao = db.contextDao()
    )
    DataItemEditFormDialog(
        modifier = Modifier,
        title = stringResource(R.string.new_data_item),
        visible = requestNewDataDialogVisible,
        close = { requestNewDataDialogVisible=false },
        initDataItem = DataSaveObj(vm.dataItemSearchText,"",
            vm.currentKeywordList.filter { it.selected }.map { it.content }.run {
                val strBuilder=StringBuilder()
                for(it in this){
                    strBuilder.append(it).append(',')
                }
                return@run if (strBuilder.endsWith(',')) strBuilder.trimEnd(',').toString()
                else strBuilder.toString()
            }
        ),
        scope = dbAccessScope,
        dao = db.dataDao(),
        allowObjCover = true
    )
}