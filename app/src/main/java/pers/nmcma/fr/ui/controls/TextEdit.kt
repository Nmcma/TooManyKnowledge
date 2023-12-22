package pers.nmcma.fr.ui.controls

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.min

@Composable
fun TextEdit(
    modifier: Modifier = Modifier,
    valueString:String,
    updateText:(String)->Unit,
    hint:String=""){
    var selectLocation by remember { mutableIntStateOf(0) }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    AndroidView(modifier = modifier,factory = { context->
        object: EditText(context){
            val textWatcher: TextWatcher =object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    selectLocation=start+count
                }
                override fun afterTextChanged(s: Editable?) {
                    updateText(s?.toString()?:"")
                }
            }
            init {
                setHint(hint)
                setHintTextColor(Color.argb(0.5f,textColor.red,textColor.green,textColor.blue))
                setText(valueString)
                setSelection(valueString.length)
                selectLocation=valueString.length
                setBackgroundColor(backgroundColor.toArgb())
                setTextColor(textColor.toArgb())
                gravity= Gravity.TOP+ Gravity.START
                addTextChangedListener(textWatcher)
            }
        }
    }, update = { textView ->
        textView.removeTextChangedListener(textView.textWatcher)
        textView.setText(valueString)
        textView.setSelection(min(valueString.length,selectLocation))
        textView.addTextChangedListener(textView.textWatcher)
    })

}