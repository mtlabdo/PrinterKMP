package dev.mtlabdo.label.printer.tcp

import dev.mtlabdo.label.printer.PrinterConnection
import dev.mtlabdo.label.printer.exception.PrinterConnectionException
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.isClosed
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class TcpPrinterConnection(private val address: String, private val port: Int) :
    PrinterConnection() {

    private var socket: Socket? = null

    /**
     * Check if the printer is connected
     */
    override fun isConnected(): Boolean =
        socket != null && !socket!!.isClosed && super.isConnected()

    /**
     * Connect to the printer
     */
    override suspend fun connect(): PrinterConnection {
        if (this.isConnected()) return this
        try {
            val selectorManager = SelectorManager(Dispatchers.IO)
            socket = aSocket(selectorManager).tcp().connect(address, port)
            channel = socket!!.openWriteChannel(autoFlush = true)
            clearData()
        } catch (e: Exception) {
            e.printStackTrace()
            socket = null
            channel = null
            throw PrinterConnectionException(e.message)
        }
        return this
    }

    /**
     * Disconnect from the printer
     */
    override fun disconnect(): PrinterConnection {
        clearData()
        if (channel != null) {
            try {
                channel!!.close()
                channel = null
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (socket != null) {
            try {
                socket!!.close()
                socket = null
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return this
    }
}