import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.v1.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.v1.DialogProperties
import org.jetbrains.skija.Image
import java.io.File
import kotlin.math.ln

val customPriorityQueue = CustomPriorityQueue<Item>()
const val dialogWidth = 500
const val dialogHeight = 200

private val darkColorPalette = darkColors(
    primary = Color(0xff263238),
    primaryVariant = Color(0xff4f5b62),
    onPrimary = Color.White,
    secondary = Color(0xffffffff),
    surface = Color(0xff2d3940),
    onSurface = Color.DarkGray,
    onBackground = Color(0xff4f5b62)
)

@ExperimentalComposeUiApi
fun main() = Window(
    title = "Alpha Go Hospital Priority Queues",
    size = IntSize(1000, 650)
) {
    MaterialTheme(
        colors = darkColorPalette
    ) {
        var patientName by remember { mutableStateOf("") }
        var severity by remember { mutableStateOf(0) }
        var tempSeverity by remember { mutableStateOf("") }
        var empty by remember { mutableStateOf(true) }
        var toOrder by remember { mutableStateOf(true) }
        var (showPeekDialog, setShowPeekDialog) = remember { mutableStateOf(false) }
        var (showRemoveDialog, setShowRemoveDialog) = remember { mutableStateOf(false) }
        val requester = FocusRequester()
        val requester2 = FocusRequester()


        dialogRemoveItem(showRemoveDialog, setShowRemoveDialog)

        dialogPeekItem(showPeekDialog, setShowPeekDialog)



        Row(
            modifier = Modifier.wrapContentHeight()
                .background(Color.LightGray)
                .fillMaxWidth()
        ) {
            val customCanvas =
                CustomCanvas(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.95f),
                    priorityQueue = customPriorityQueue
                )
            customCanvas.drawCanvas()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(5.dp)
            ) {
                OutlinedTextField(
                    patientName,
                    { value ->
                        patientName = value
                    },
                    label = { Text("Patient Name") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        requester2.requestFocus()
                    }),
                    modifier = Modifier
                        .onPreviewKeyEvent {
                            when {
                                ((it.key == Key.Enter || it.key == Key.Tab) && patientName.isNotEmpty()) -> {
                                    requester2.requestFocus()
                                    true
                                }
                                else -> false
                            }
                        }
                        .focusRequester(requester)
                )

                OutlinedTextField(
                    tempSeverity,
                    { value ->
                        tempSeverity = try {
                            value.filter {
                                it.isDigit()
                            }
                        } catch (e: Exception) {
                            ""
                        }
                        severity = if (tempSeverity.isNotEmpty()) Integer.parseInt(tempSeverity)
                        else 0
                    },
                    label = { Text("Condition Severity") },
                    maxLines = 1,
                    modifier = Modifier
                        .onPreviewKeyEvent {
                            when {
                                (it.key == Key.Enter) -> {
                                    if ((patientName.isNotEmpty() && severity > 0)) {
                                        addItem(Item(patientName = patientName, severity = severity), toOrder)
                                        requester.requestFocus()
                                        empty = isEmpty()
                                        patientName = ""
                                        severity = 0
                                        tempSeverity = ""
                                    }
                                    true
                                }
                                (it.key == Key.Tab) -> {
                                    if (severity >= 1)
                                        requester.requestFocus()
                                    true
                                }
                                else -> false
                            }
                        }
                        .focusRequester(requester2)
                )
                Button(modifier = Modifier
                    .wrapContentHeight()
                    .padding(10.dp)
                    .fillMaxWidth(0.7f),
                    onClick = {
                        //TODO: implement patient addition
                        requester.requestFocus()
                        if (patientName.isNotEmpty() && severity > 0)
                            addItem(Item(patientName = patientName, severity = severity), true)
                        empty = isEmpty()
                        patientName = ""
                        severity = 0
                        tempSeverity = ""
                    }
                ) {
                    Text("Add Patient")
                }
                Button(modifier = Modifier
                    .wrapContentHeight()
                    .padding(10.dp)
                    .border(1.5.dp, Color.DarkGray)
                    .fillMaxWidth(0.7f),
                    onClick = {
//                        requester.requestFocus()
//                        if (patientName.isNotEmpty() && severity > 0)
//                            addItem(Item(patientName = patientName, severity = severity), false)
//                        empty = isEmpty()
//                        patientName = ""
//                        severity = 0
//                        tempSeverity = ""
                        toOrder = !toOrder
                    }
                ) {
                    Text(buildAnnotatedString {
                        if (toOrder)
                            withStyle(style = SpanStyle(color = Color.Green)) {
                                append("Ordered")
                            } else withStyle(style = SpanStyle(color = Color.Red)) {
                            append("Unordered")
                        }
                    })
                }
                Button(modifier = Modifier
                    .wrapContentHeight()
                    .padding(10.dp)
                    .fillMaxWidth(0.7f),
                    onClick = {
                        //TODO: do ordering
//                        println("Im here")
                        if (customPriorityQueue.size() > 2) {
                            customPriorityQueue.add(Item(-1, "NULL"), ordered = true)
//                            println("This has been done")
                        }
//                        println("Now Im here")
                    }) {
                    Text("Order")
                }


                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .weight(0.9f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Is Empty? ")
                            if (empty)
                                withStyle(style = SpanStyle(color = Color.Red)) {
                                    append("True")
                                } else withStyle(style = SpanStyle(color = Color.Green)) {
                                append("False")
                            }
                        },
                        fontSize = 20.sp,
                        modifier = Modifier.wrapContentHeight().wrapContentWidth().align(Alignment.CenterHorizontally)
                    )

                    Button(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.7f)
                        .padding(10.dp),
                        onClick = {
                            //TODO: implement patient peek
                            setShowPeekDialog(true)
                        }
                    ) {
                        Text("Peek")
                    }
                    Button(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.7f)
                        .padding(10.dp),
                        onClick = {
                            //TODO: implement patient removal
                            empty = isEmpty()
                            //for (patient in customPriorityQueue.arrayList) println(patient)
                            setShowRemoveDialog(true)
                        }
                    ) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}

@Composable
fun dialogRemoveItem(showRemoveDialog: Boolean, setShowRemoveDialog: (Boolean) -> Unit) {
    if (showRemoveDialog) {
        AlertDialog(
            properties = DialogProperties(
                centered = true,
                size = IntSize(dialogWidth, dialogHeight),
                title = "Remove"
            ),
            onDismissRequest = {
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowRemoveDialog(false)
                    },
                ) {
                    Text("Okay")
                }
            },
            text = {
                if (customPriorityQueue.arrayList.size > 1)
                    Text("${customPriorityQueue.remove()}", fontSize = 20.sp)
                else Text("No Patients to remove!", fontSize = 20.sp)
            },
            modifier = Modifier
                .padding(5.dp),
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    }
}


@Composable
fun dialogPeekItem(showPeekDialog: Boolean, setShowPeekDialog: (Boolean) -> Unit) {
    if (showPeekDialog) {
        AlertDialog(
            modifier = Modifier
                .padding(5.0.dp),
            properties = DialogProperties(
                centered = true,
                size = IntSize(dialogWidth, dialogHeight),
                title = "Peek"
            ),
            onDismissRequest = {
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowPeekDialog(false)
                    },
                ) {
                    Text("Okay")
                }
            },
            text = {
                if (customPriorityQueue.arrayList.size > 1)
                    Text("${customPriorityQueue.peek()}", fontSize = 20.sp)
                else Text("No Patients to Peek!", fontSize = 20.sp)
            },
            contentColor = Color.Black,
            backgroundColor = Color.White
        )
    }
}

fun addItem(item: Item, ordered: Boolean) {
    customPriorityQueue.add(item = item, ordered = ordered)
//    for (m in customPriorityQueue.arrayList) {
//        println(m)
//    }
}

fun isEmpty(): Boolean {
    return customPriorityQueue.isEmpty
}

fun logBase2(num: Int): Int = (ln(num.toDouble()) / ln(2.0)).toInt()

fun imageFromFile(file: File): Image {
    return Image.makeFromEncoded(file.readBytes())
}