package dev.mtlabdo.label.printer

import dev.mtlabdo.label.printer.exception.PrinterConnectionException
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

abstract class PrinterConnection {

    protected var channel: ByteWriteChannel?
    protected var data: ByteArray

    init {
        channel = null
        data = ByteArray(0)
    }

    abstract suspend fun connect(): PrinterConnection
    abstract fun disconnect(): PrinterConnection

    /**
     * Check if the printer is connected
     */
    open fun isConnected(): Boolean = this.channel != null

    /**
     * Write data that will be send to the printer
     */
    fun write(bytes: ByteArray) {
        val data = ByteArray(bytes.size + data.size)
        this.data.copyInto(data, 0, 0, this.data.size)
        bytes.copyInto(data, this.data.size, 0, bytes.size)
        this.data = data
    }

    /**
     * Send data to the printer
     */
    suspend fun send() {
        if (!isConnected()) {
            throw PrinterConnectionException("Unable to send data to device.")
        }
        try {
            withContext(Dispatchers.IO) {
                channel!!.writeFully(data, 0, 0)
                channel!!.flush()
                data = ByteArray(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw PrinterConnectionException(e.message)
        } catch (e: PrinterConnectionException) {
            e.printStackTrace()
            throw PrinterConnectionException(e.message)
        }
    }

    fun clearData() {
        data = ByteArray(0)
    }


}