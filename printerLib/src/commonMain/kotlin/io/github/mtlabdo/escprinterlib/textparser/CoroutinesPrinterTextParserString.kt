package io.github.mtlabdo.escprinterlib.textparser

import io.github.mtlabdo.escprinterlib.CoroutinesEscPosPrinter
import io.github.mtlabdo.escprinterlib.CoroutinesEscPosPrinterCommands
import io.github.mtlabdo.escprinterlib.EscPosPrinterCommands
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.charsets.forName
import io.ktor.utils.io.core.toByteArray
import kotlin.coroutines.cancellation.CancellationException


class CoroutinesPrinterTextParserString(
    printerTextParserColumn: CoroutinesPrinterTextParserColumn,
    var text: String,
    var textSize: ByteArray,
    var textColor: ByteArray,
    private var textReverseColor: ByteArray,
    private var textBold: ByteArray,
    private var textUnderline: ByteArray,
    private var textDoubleStrike: ByteArray
) : CoroutinesIPrinterTextParserElement {
    private val printer: CoroutinesEscPosPrinter = printerTextParserColumn.line.textParser.printer

    @Throws(EscPosEncodingException::class)
    override fun length(): Int {
        val charsetEncoding = printer.encoding
        val coef = if (textSize.contentEquals(EscPosPrinterCommands.TEXT_SIZE_DOUBLE_WIDTH) ||
            textSize.contentEquals(EscPosPrinterCommands.TEXT_SIZE_BIG)) 2 else 1

        return try {
            text.toByteArray(Charsets.forName(charsetEncoding.name)).size * coef
        } catch (e: Exception) {
            throw EscPosEncodingException(e.message)
        }
    }

    /**
     * Print text
     *
     * @param printerSocket Instance of EscPosPrinterCommands
     * @return this Fluent method
     */
    @Throws(EscPosEncodingException::class, CancellationException::class)
    override suspend fun print(printerSocket: CoroutinesEscPosPrinterCommands?): CoroutinesPrinterTextParserString {
        printerSocket!!.printText(
            text,
            textSize,
            textColor,
            textReverseColor,
            textBold,
            textUnderline,
            textDoubleStrike
        )
        return this
    }

}
