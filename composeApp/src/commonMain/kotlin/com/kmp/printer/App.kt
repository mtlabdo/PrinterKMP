package com.kmp.printer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.mtlabdo.escprinterlib.connection.tcp.TcpConnection
import io.github.mtlabdo.escprinterlib.coroutines.CoroutinesEscPosPrint
import io.github.mtlabdo.escprinterlib.coroutines.CoroutinesEscPosPrinter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import printerkmp.composeapp.generated.resources.Res
import printerkmp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    printTcp()
                }
                showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet()

                }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
private var printer: CoroutinesEscPosPrinter? = null

private suspend fun printTcp() {
    try {
        printer =
            CoroutinesEscPosPrinter(
                TcpConnection(
                    "192.168.1.92",
                    9100
                ).apply { connect() }, 203, 48f, 32
            )

        CoroutinesEscPosPrint().execute(
            printViaWifi(
                printer!!,
                45,
                body,
                34.98f,
                4,
                customer,
                "83125478455134567890",
            ), cutPaper = true
        ).apply { printer = null }

    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
}

private val body: String
    get() = "[L]\n" +
            "[L]    <b>Pizza</b>[R][R]3[R][R]55 $\n" +
            "[R] PAYMENT METHOD :[R]Visa\n"

private val customer: String
    get() =
        "[C]================================\n"

fun printViaWifi(
    printer: CoroutinesEscPosPrinter,
    orderId: Int,
    body: String,
    totalBill: Float,
    tax: Int,
    customer: String = "",
    barcode: String
): CoroutinesEscPosPrinter {

    var test =
        "[C]"+
                "[L]\n" +
                "[C]<u><font size='big'>ORDER N°$orderId</font></u>\n" +
                "[L]\n" +
                "[C]<u type='double'>---</u>\n" +
                "[C]================================\n" +
                "[L]\n" +
                "[L]    <b>Items</b>[R][R]<b>Qty</b>[R][R]<b>Price</b>\n" +
                "[L][R]\n" +
                "$body\n" +
                "[L][R]\n" +
                "[C]--------------------------------\n" +
                "[R] TOTAL :[R]${totalBill} $\n"

    test += if (tax != 0) "[R] TAX :[R]${tax} %\n" + "[R] GRAND TOTAL :[R]${totalBill * (tax / 100f) + totalBill} $\n" else ""

    test += "[L][R]\n" +
            "$customer\n" +
            "[C]<barcode type='128' height='10'>$barcode</barcode>\n" +
            "[L]\n" +
            "[C]<u><font size='big'>VISIT HIS SITE</font></u>\n" +
            "[L]\n" +
            "[L]\n" +
            "[C]<qrcode size='20'>http://www.developpeur-web.khairo.com/</qrcode>\n" +
            "[L]\n" +
            "[L]\n" +
            "[L]\n" +
            "[L]\n" +
            "[L]\n"

    return printer.setTextToPrint(test)
}
