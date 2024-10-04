package io.github.mtlabdo.escprinterlib.coroutines

import io.github.mtlabdo.escprinterlib.EscPosCharsetEncoding
import io.github.mtlabdo.escprinterlib.connection.tcp.TcpDeviceConnection
import io.github.mtlabdo.escprinterlib.exceptions.PrintingException
import io.github.mtlabdo.escprinterlib.exceptions.onException


class CoroutinesEscPosPrint() {

    suspend fun execute(vararg printersData: CoroutinesEscPosPrinter) {
        if (printersData.isEmpty())
            return onException(PrintingException.FINISH_NO_PRINTER)

        val printerData = printersData[0]

        var deviceConnection: TcpDeviceConnection? = printerData.printerConnection
//            if (deviceConnection == null)
//                deviceConnection = BluetoothPrintersConnections.selectFirstPaired()

        if (deviceConnection == null) return onException(PrintingException.FINISH_NO_PRINTER)

        val printer = io.github.mtlabdo.escprinterlib.CoroutinesEscPosPrinter(
            deviceConnection,
            printerData.printerDpi,
            printerData.printerWidthMM,
            printerData.printerNbrCharactersPerLine,
            EscPosCharsetEncoding("Arabic", 22)
        )

        printer.printFormattedTextAndCut(printerData.textToPrint)
            .apply { disconnectPrinter() }
    }
}
