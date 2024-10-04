package io.github.mtlabdo.escprinterlib.connection.tcp


import io.github.mtlabdo.escprinterlib.exceptions.PrintingException
import io.github.mtlabdo.escprinterlib.exceptions.onException

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


/**
 * Create un instance of TcpConnection.
 *
 * @param address IP address of the device
 * @param port    Port of the device
 */
class TcpConnection(private val address: String, private val port: Int) : TcpDeviceConnection() {
    private var socket: Socket? = null

    /**
     * Check if the TCP device is connected by socket.
     *
     * @return true if is connected
     */

    override fun isConnected(): Boolean =
        socket != null && !socket!!.isClosed && super.isConnected()

    /**
     * Start socket connection with the TCP device.
     */
    override suspend fun connect(): TcpConnection {
        if (this.isConnected()) return this
        try {
            val selectorManager = SelectorManager(Dispatchers.IO)
            socket = aSocket(selectorManager).tcp().connect(address, port)
            byteWriteChannel = socket!!.openWriteChannel(autoFlush = true)
            val myMessage = "4444"
            //byteWriteChannel!!.writeStringUtf8("$myMessage\n")
            data = ByteArray(0)

        } catch (e: IOException) {
            e.printStackTrace()
            socket = null
            byteWriteChannel = null
            onException(PrintingException.FINISH_PRINTER_DISCONNECTED)
        }

  /*      if (this.isConnected()) return this

        try {
            socket = Socket()
            socket!!.connect(InetSocketAddress(InetAddress.getByName(address), port))
            stream = socket!!.getOutputStream()
            data = ByteArray(0)

        } catch (e: IOException) {
            e.printStackTrace()
            socket = null
            stream = null
            onException(context, FINISH_PRINTER_DISCONNECTED)
//            throw EscPosConnectionException("Unable to connect to TCP device.")
        }*/
        return this
    }

    /**
     * Close the socket connection with the TCP device.
     */
    override suspend fun disconnect(): TcpConnection {
        data = ByteArray(0)
        if (byteWriteChannel != null) {
            try {
                byteWriteChannel!!.close()
                byteWriteChannel = null
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