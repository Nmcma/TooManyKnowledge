package pers.nmcma.fr.ui.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import pers.nmcma.fr.R
@Composable
fun SingleValueEditDialog(
    title:String="",
    showTitle:Boolean=true,
    initValueString: String="",
    visible:Boolean,
    close:()->Unit,
    checkValue:suspend (String)->Boolean,
    valueEdited:suspend (String)->Unit,
    additionTools:@Composable ()->Unit={}){
    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    var selectLocation by remember { mutableIntStateOf(0) }
    var inputCondition by remember { mutableIntStateOf(0) }
    var valueString by remember { mutableStateOf(initValueString) }
    val checkerScope = rememberCoroutineScope()
    if (visible){
        Dialog(onDismissRequest = { close() }) {
            BoxWithConstraints (modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)){
                val maxWidth=maxWidth
                Column {
                    if (showTitle) Text(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),text = title,style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                    AndroidView(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary),factory = { context->
                        object: EditText(context){
                            val textWatcher: TextWatcher =object: TextWatcher {
                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    selectLocation=start+count
                                }
                                override fun afterTextChanged(s: Editable?) {
                                    inputCondition=0
                                    valueString=s?.toString()?:""
                                }
                            }
                            init {
                                setText(initValueString)
                                setSelection(initValueString.length)
                                valueString=initValueString
                                selectLocation=initValueString.length
                                setBackgroundColor(backgroundColor.toArgb())
                                setTextColor(textColor.toArgb())
                                gravity= Gravity.TOP+ Gravity.START
                                addTextChangedListener(textWatcher)
                            }
                        }
                    }, update = {
                        valueString.let {text->
                            it.isEnabled=inputCondition!=1
                            it.removeTextChangedListener(it.textWatcher)
                            it.setText(text)
                            it.setSelection(selectLocation)
                            it.addTextChangedListener(it.textWatcher)
                        }
                    })
                    additionTools()
                    Row {
                        Button(modifier = Modifier
                            .padding(1.dp)
                            .width(maxWidth / 2)
                            , shape = RectangleShape, onClick = {
                                inputCondition=1
                                checkerScope.launch {
                                    if (checkValue(valueString)){
                                        inputCondition=0
                                        valueEdited(valueString)
                                        close()
                                    }else{
                                        inputCondition=-1
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = when (inputCondition) {
                                    -1-> MaterialTheme.colorScheme.error
                                    else-> MaterialTheme.colorScheme.primary
                                }
                            ), enabled = inputCondition==0) {
                            Text(text = stringResource(id = R.string.apply), color = if (inputCondition==-1) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary)
                        }
                        Button(modifier = Modifier
                            .padding(1.dp)
                            .width(maxWidth / 2), shape = RectangleShape, onClick = {
                            close()
                        }) {
                            Text(text = stringResource(id = R.string.cancel), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}