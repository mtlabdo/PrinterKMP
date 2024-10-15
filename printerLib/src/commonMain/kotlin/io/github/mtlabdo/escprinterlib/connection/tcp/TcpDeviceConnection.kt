package io.github.mtlabdo.escprinterlib.connection.tcp


import io.github.mtlabdo.escprinterlib.exceptions.PrintingException
import io.github.mtlabdo.escprinterlib.exceptions.onException
import io.ktor.utils.io.*
import kotlinx.coroutines.delay
import kotlin.math.floor

abstract class TcpDeviceConnection {

    protected var sendChannel: ByteWriteChannel? = null

    protected var data: ByteArray

    abstract suspend fun connect(): TcpDeviceConnection?
    abstract suspend fun disconnect(): TcpDeviceConnection?

    /**
     * Check if OutputStream is open.
     *
     * @return true if is connected
     */
    open fun isConnected(): Boolean = this.sendChannel != null

    /**
     * Add data to send.
     */
    fun write(bytes: ByteArray) {
        val newData = data.copyOf(data.size + bytes.size) // Create a new array with enough space
        bytes.copyInto(newData, data.size) // Copy the new bytes into the new array
        data = newData // Update the data property
    }

    /**
     * Send data to the device.
     */
    open suspend fun send() {
        this.send( 0)
    }

    /**
     * Send data to the device.
     */
    open suspend fun send(addWaitingTime: Int) {
        if (!isConnected()) {
            onException(PrintingException.FINISH_PRINTER_DISCONNECTED)
            return
        }
        try {
            sendChannel!!.writeFully(data)
            sendChannel!!.flush()
            data = ByteArray(0)
            val waitingTime = addWaitingTime + floor((data.size / 16f).toDouble())
                .toInt()
            if (waitingTime > 0) {
                delay(waitingTime.toLong())
            }
        } catch (e: kotlinx.io.IOException) {
            e.printStackTrace()
            onException(PrintingException.FINISH_PRINTER_DISCONNECTED)
        } catch (e: kotlinx.io.IOException) {
            e.printStackTrace()
            onException(PrintingException.FINISH_PRINTER_DISCONNECTED)
        }
    }

    init {
        data = ByteArray(0)
    }
}