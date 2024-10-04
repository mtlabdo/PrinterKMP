package io.github.mtlabdo.escprinterlib

import io.github.mtlabdo.escprinterlib.connection.DeviceConnection
import io.github.mtlabdo.escprinterlib.exceptions.EscPosConnectionException
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.core.toByteArray
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.JvmOverloads
import io.ktor.utils.io.charsets.*

class EscPosPrinterCommands @JvmOverloads constructor(
    private val printerConnection: DeviceConnection,
    charsetEncoding: EscPosCharsetEncoding? = null
) {
    /**
     * @return Charset encoding
     */
    val charsetEncoding: EscPosCharsetEncoding = charsetEncoding ?: EscPosCharsetEncoding(
        "windows-1252",
        6
    )

    /**
     * Create new instance of EscPosPrinterCommands.
     *
     * @param printerConnection an instance of a class which implement DeviceConnection
     * @param charsetEncoding   Set the charset encoding.
     */

    /**
     * Start socket connection and open stream with the device.
     */
    @Throws(EscPosConnectionException::class)
    fun connect(): EscPosPrinterCommands {
        printerConnection.connect()
        return this
    }

    /**
     * Close the socket connection and stream with the device.
     */
    fun disconnect() {
        printerConnection.disconnect()
    }

    /**
     * Reset printers parameters.
     */
    fun reset() {
        printerConnection.write(RESET_PRINTER)
    }

    /**
     * Set the alignment of text and barcodes.
     * Don't works with image.
     *
     * @param align Set the alignment of text and barcodes. Use EscPosPrinterCommands.TEXT_ALIGN_... constants
     * @return Fluent interface
     */
    fun setAlign(align: ByteArray): EscPosPrinterCommands {
        if (!printerConnection.isConnected()) {
            return this
        }
        printerConnection.write(align)
        return this
    }

    /**
     * Print text with the connected printer.
     *
     * @param text          Text to be printed
     * @param textSize      Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @param textColor        Set the text color. Use EscPosPrinterCommands.TEXT_COLOR_... constants
     * @param textReverseColor Set the background and text color. Use EscPosPrinterCommands.TEXT_COLOR_REVERSE_... constants
     * @param textBold      Set the text weight. Use EscPosPrinterCommands.TEXT_WEIGHT_... constants
     * @param textUnderline Set the underlining of the text. Use EscPosPrinterCommands.TEXT_UNDERLINE_... constants
     * @return Fluent interface
     */
    /**
     * Print text with the connected printer.
     *
     * @param text     Text to be printed
     * @param textSize Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @param textColor        Set the text color. Use EscPosPrinterCommands.TEXT_COLOR_... constants
     * @param textReverseColor Set the background and text color. Use EscPosPrinterCommands.TEXT_COLOR_REVERSE_... constants
     * @param textBold Set the text weight. Use EscPosPrinterCommands.TEXT_WEIGHT_... constants
     * @return Fluent interface
     */
    /**
     * Print text with the connected printer.
     *
     * @param text     Text to be printed
     * @param textSize Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @param textColor        Set the text color. Use EscPosPrinterCommands.TEXT_COLOR_... constants
     * @param textReverseColor Set the background and text color. Use EscPosPrinterCommands.TEXT_COLOR_REVERSE_... constants
     * @return Fluent interface
     */
    /**
     * Print text with the connected printer.
     *
     * @param text     Text to be printed
     * @param textSize Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @param textColor        Set the text color. Use EscPosPrinterCommands.TEXT_COLOR_... constants
     * @return Fluent interface
     */
    /**
     * Print text with the connected printer.
     *
     * @param text     Text to be printed
     * @param textSize Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @return Fluent interface
     */
    /**
     * Print text with the connected printer.
     *
     * @param text Text to be printed
     * @return Fluent interface
     */
    @JvmOverloads
    @Throws(EscPosEncodingException::class)
    fun printText(
        text: String,
        textSize: ByteArray? = null,
        textColor: ByteArray? = null,
        textReverseColor: ByteArray? = null,
        textBold: ByteArray? = null,
        textUnderline: ByteArray? = null
    ): EscPosPrinterCommands {
        return this.printText(
            text,
            textSize,
            textColor,
            textReverseColor,
            textBold,
            textUnderline,
            null
        )
    }

    /**
     * Print text with the connected printer.
     *
     * @param text             Text to be printed
     * @param textSize         Set the text size. Use EscPosPrinterCommands.TEXT_SIZE_... constants
     * @param textColor        Set the text color. Use EscPosPrinterCommands.TEXT_COLOR_... constants
     * @param textReverseColor Set the background and text color. Use EscPosPrinterCommands.TEXT_COLOR_REVERSE_... constants
     * @param textBold         Set the text weight. Use EscPosPrinterCommands.TEXT_WEIGHT_... constants
     * @param textUnderline    Set the underlining of the text. Use EscPosPrinterCommands.TEXT_UNDERLINE_... constants
     * @param textDoubleStrike Set the double strike of the text. Use EscPosPrinterCommands.TEXT_DOUBLE_STRIKE_... constants
     * @return Fluent interface
     */
    @Throws(EscPosEncodingException::class)
    fun printText(
        text: String,
        textSize: ByteArray?,
        textColor: ByteArray?,
        textReverseColor: ByteArray?,
        textBold: ByteArray?,
        textUnderline: ByteArray?,
        textDoubleStrike: ByteArray?
    ): EscPosPrinterCommands {
        if (!printerConnection.isConnected()) {
            return this
        }

        try {
            val textBytes: ByteArray = text.toByteArray(
                Charset.forName(charsetEncoding.name)
            )
            printerConnection.write(charsetEncoding.command)

            //this.printerConnection.write(EscPosPrinterCommands.TEXT_FONT_A);
            if (textSize != null) {
                printerConnection.write(textSize)
            } else {
                printerConnection.write(TEXT_SIZE_NORMAL)
            }

            if (textDoubleStrike != null) {
                printerConnection.write(textDoubleStrike)
            } else {
                printerConnection.write(TEXT_DOUBLE_STRIKE_OFF)
            }

            if (textUnderline != null) {
                printerConnection.write(textUnderline)
            } else {
                printerConnection.write(TEXT_UNDERLINE_OFF)
            }

            if (textBold != null) {
                printerConnection.write(textBold)
            } else {
                printerConnection.write(TEXT_WEIGHT_NORMAL)
            }

            if (textColor != null) {
                printerConnection.write(textColor)
            } else {
                printerConnection.write(TEXT_COLOR_BLACK)
            }

            if (textReverseColor != null) {
                printerConnection.write(textReverseColor)
            } else {
                printerConnection.write(TEXT_COLOR_REVERSE_OFF)
            }

            printerConnection.write(textBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw EscPosEncodingException(e.message)
        }

        return this
    }


    /**
     * Forces the transition to a new line with the connected printer.
     *
     * @return Fluent interface
     */
    @Throws(EscPosConnectionException::class, CancellationException::class)
    suspend fun newLine(): EscPosPrinterCommands {
        return this.newLine(null)
    }

    /**
     * Forces the transition to a new line and set the alignment of text and barcodes with the connected printer.
     *
     * @param align Set the alignment of text and barcodes. Use EscPosPrinterCommands.TEXT_ALIGN_... constants
     * @return Fluent interface
     */
    @Throws(EscPosConnectionException::class, CancellationException::class)
    suspend fun newLine(align: ByteArray?): EscPosPrinterCommands {
        if (!printerConnection.isConnected()) {
            return this
        }

        printerConnection.write(byteArrayOf(LF))
        printerConnection.send()

        if (align != null) {
            printerConnection.write(align)
        }
        return this
    }

    /**
     * Feed the paper
     *
     * @param dots Number of dots to feed (0 <= dots <= 255)
     * @return Fluent interface
     */
    @Throws(EscPosConnectionException::class, CancellationException::class)
    suspend fun feedPaper(dots: Int): EscPosPrinterCommands {
        if (!printerConnection.isConnected()) {
            return this
        }

        if (dots > 0) {
            printerConnection.write(byteArrayOf(0x1B, 0x4A, dots.toByte()))
            printerConnection.send(dots)
        }

        return this
    }

    /**
     * Cut the paper
     *
     * @return Fluent interface
     */
    @Throws(EscPosConnectionException::class, CancellationException::class)
    suspend fun cutPaper(): EscPosPrinterCommands {
        if (!printerConnection.isConnected()) {
            return this
        }

        printerConnection.write(byteArrayOf(0x1D, 0x56, 0x01))
        printerConnection.send(100)
        return this
    }

    companion object {
        const val LF: Byte = 0x0A

        val RESET_PRINTER: ByteArray = byteArrayOf(0x1B, 0x40)

        val TEXT_ALIGN_LEFT: ByteArray = byteArrayOf(0x1B, 0x61, 0x00)
        val TEXT_ALIGN_CENTER: ByteArray = byteArrayOf(0x1B, 0x61, 0x01)
        val TEXT_ALIGN_RIGHT: ByteArray = byteArrayOf(0x1B, 0x61, 0x02)

        val TEXT_WEIGHT_NORMAL: ByteArray = byteArrayOf(0x1B, 0x45, 0x00)
        val TEXT_WEIGHT_BOLD: ByteArray = byteArrayOf(0x1B, 0x45, 0x01)

        val TEXT_FONT_A: ByteArray = byteArrayOf(0x1B, 0x4D, 0x00)
        val TEXT_FONT_B: ByteArray = byteArrayOf(0x1B, 0x4D, 0x01)
        val TEXT_FONT_C: ByteArray = byteArrayOf(0x1B, 0x4D, 0x02)
        val TEXT_FONT_D: ByteArray = byteArrayOf(0x1B, 0x4D, 0x03)
        val TEXT_FONT_E: ByteArray = byteArrayOf(0x1B, 0x4D, 0x04)

        val TEXT_SIZE_NORMAL: ByteArray = byteArrayOf(0x1D, 0x21, 0x00)
        val TEXT_SIZE_DOUBLE_HEIGHT: ByteArray = byteArrayOf(0x1D, 0x21, 0x01)
        val TEXT_SIZE_DOUBLE_WIDTH: ByteArray = byteArrayOf(0x1D, 0x21, 0x10)
        val TEXT_SIZE_BIG: ByteArray = byteArrayOf(0x1D, 0x21, 0x11)

        val TEXT_UNDERLINE_OFF: ByteArray = byteArrayOf(0x1B, 0x2D, 0x00)
        val TEXT_UNDERLINE_ON: ByteArray = byteArrayOf(0x1B, 0x2D, 0x01)
        val TEXT_UNDERLINE_LARGE: ByteArray = byteArrayOf(0x1B, 0x2D, 0x02)

        val TEXT_DOUBLE_STRIKE_OFF: ByteArray = byteArrayOf(0x1B, 0x47, 0x00)
        val TEXT_DOUBLE_STRIKE_ON: ByteArray = byteArrayOf(0x1B, 0x47, 0x01)

        val TEXT_COLOR_BLACK: ByteArray = byteArrayOf(0x1B, 0x72, 0x00)
        val TEXT_COLOR_RED: ByteArray = byteArrayOf(0x1B, 0x72, 0x01)

        val TEXT_COLOR_REVERSE_OFF: ByteArray = byteArrayOf(0x1D, 0x42, 0x00)
        val TEXT_COLOR_REVERSE_ON: ByteArray = byteArrayOf(0x1D, 0x42, 0x01)


        const val BARCODE_TYPE_UPCA: Int = 65
        const val BARCODE_TYPE_UPCE: Int = 66
        const val BARCODE_TYPE_EAN13: Int = 67
        const val BARCODE_TYPE_EAN8: Int = 68
        const val BARCODE_TYPE_ITF: Int = 70
        const val BARCODE_TYPE_128: Int = 73

        const val BARCODE_TEXT_POSITION_NONE: Int = 0
        const val BARCODE_TEXT_POSITION_ABOVE: Int = 1
        const val BARCODE_TEXT_POSITION_BELOW: Int = 2
    }
}
