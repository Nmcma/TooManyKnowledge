package pers.nmcma.fr.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pers.nmcma.fr.R
import pers.nmcma.fr.db.amb.AmbDao
import pers.nmcma.fr.db.amb.AmbSaveObj
import pers.nmcma.fr.toSaveFormatString

@Composable
fun SaveNewSettingDialog(
    visible:Boolean,
    close:()->Unit,
    keywordItemStringList:List<String>,
    scope:CoroutineScope,
    dao: AmbDao,
    allowCover:Boolean
) {
    SingleValueEditDialog(
        title = stringResource(R.string.save_as_new_amb),
        visible =visible,
        close = close,
        checkValue ={s->
            s.isNotBlank()
            &&scope.async {
                dao.getAmb(s)?.let {
                    allowCover
                }?:true
            }.await()
        },
        valueEdited = {
            dao.getAmb(it)?.let { _->
                dao.update(AmbSaveObj(it,keywordItemStringList.toSaveFormatString()))
            }?:run{
                dao.insert(AmbSaveObj(it,keywordItemStringList.toSaveFormatString()))
            }
            close()
        })
}