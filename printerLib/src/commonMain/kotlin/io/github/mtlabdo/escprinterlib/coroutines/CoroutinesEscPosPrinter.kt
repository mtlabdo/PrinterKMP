package io.github.mtlabdo.escprinterlib.coroutines

import io.github.mtlabdo.escprinterlib.EscPosPrinterSize
import io.github.mtlabdo.escprinterlib.connection.tcp.TcpDeviceConnection

class CoroutinesEscPosPrinter(
    val printerConnection: TcpDeviceConnection,
    printerDpi: Int,
    printerWidthMM: Float,
    printerNbrCharactersPerLine: Int
) : EscPosPrinterSize(printerDpi, printerWidthMM, printerNbrCharactersPerLine) {
    var textToPrint = ""

    fun setTextToPrint(textToPrint: String): CoroutinesEscPosPrinter {
        //this.textToPrint = textToPrint
        this.textToPrint = "text"
        return this
    }
}
