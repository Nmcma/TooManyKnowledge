package pers.nmcma.fr.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import pers.nmcma.fr.ui.classes.KeywordItem
import pers.nmcma.fr.R

@Composable
fun AddKeywordItemDialog(
    visible:Boolean,
    close:()->Unit,
    keywordItemList:SnapshotStateList<KeywordItem>
) {
    SingleValueEditDialog(
        title = stringResource(R.string.add_new_keyword),
        visible =visible,
        close = close,
        checkValue ={s->
            s.isNotBlank()
                    &&keywordItemList.find { it.content==s }==null
        },
        valueEdited = {
            keywordItemList.add(KeywordItem(true,it))
            close()
        })
}

@Composable
fun AddKeywordStringDialog(
    visible:Boolean,
    close:()->Unit,
    keywordStringItemList:SnapshotStateList<String>
) {
    SingleValueEditDialog(
        title = stringResource(R.string.add_new_keyword),
        visible =visible,
        close = close,
        checkValue ={s->
            s.isNotBlank()
                    &&keywordStringItemList.find { it==s }==null
        },
        valueEdited = {
            keywordStringItemList.add(it)
            close()
        })
}