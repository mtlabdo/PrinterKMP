package io.github.mtlabdo.escprinterlib.connection


import io.github.mtlabdo.escprinterlib.exceptions.EscPosConnectionException
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.errors.IOException
import io.ktor.utils.io.writeAvailable
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.floor

abstract class DeviceConnection {
    protected var byteWriteChannel: ByteWriteChannel? = null

    protected var data: ByteArray

    @Throws(EscPosConnectionException::class)
    abstract fun connect(): DeviceConnection?
    abstract fun disconnect(): DeviceConnection?

    /**
     * Check if OutputStream is open.
     *
     * @return true if is connected
     */
//    open val isConnected: Boolean
//        get() = stream != null
    open fun isConnected(): Boolean = this.byteWriteChannel != null

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
    @Throws(EscPosConnectionException::class, CancellationException::class)
    open suspend fun send() {
        this.send(0)
    }

    /**
     * Send data to the device.
     */
    @Throws(EscPosConnectionException::class, CancellationException::class)
    open suspend fun send(addWaitingTime: Int) {
        if (!isConnected()) {
            throw EscPosConnectionException("Unable to send data to device.")
        }
        try {
            byteWriteChannel!!.writeAvailable(data)
            byteWriteChannel!!.flush()
            data = ByteArray(0)
            val waitingTime = addWaitingTime + floor((data.size / 16f).toDouble())
                .toInt()
            if (waitingTime > 0) {
                delay(waitingTime.toLong())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw EscPosConnectionException(e.message)
        } catch (e: Exception) {
            e.printStackTrace()
            throw EscPosConnectionException(e.message)
        }
    }

    init {
        data = ByteArray(0)
    }
}