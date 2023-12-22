package pers.nmcma.fr.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import pers.nmcma.fr.R
import pers.nmcma.fr.ui.theme.Dimensions

@Composable
fun EnsureDialog(
    title:String="",
    content: AnnotatedString = buildAnnotatedString {  },
    visible:Boolean,
    close:()->Unit,
    doWhat:suspend ()->Unit){
    val scope = rememberCoroutineScope()
    if (visible){
        Dialog(onDismissRequest = { close() }){
            BoxWithConstraints (modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)){
                val maxWidth=maxWidth
                Column {
                    Text(modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.StandardTitleHeight)
                        .background(MaterialTheme.colorScheme.primary),text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = content, color = MaterialTheme.colorScheme.onSurface)
                    Row {
                        Button(modifier = Modifier
                            .padding(1.dp)
                            .width(maxWidth / 2)
                            , shape = RectangleShape, onClick = {
                                scope.launch{doWhat()}
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )) {
                            Text(text = stringResource(R.string.yes), color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Button(modifier = Modifier
                            .padding(1.dp)
                            .width(maxWidth / 2), shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ), onClick = {
                                close()
                            }) {
                            Text(text = stringResource(R.string.no), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}