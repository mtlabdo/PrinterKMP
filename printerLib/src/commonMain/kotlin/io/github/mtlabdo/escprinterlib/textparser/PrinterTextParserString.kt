package io.github.mtlabdo.escprinterlib.textparser

import io.github.mtlabdo.escprinterlib.EscPosPrinter
import io.github.mtlabdo.escprinterlib.EscPosPrinterCommands
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.core.toByteArray

class PrinterTextParserString(
    printerTextParserColumn: PrinterTextParserColumn,
    var text: String,
    var textSize: ByteArray,
    var textColor: ByteArray,
    private var textReverseColor: ByteArray,
    private var textBold: ByteArray,
    private var textUnderline: ByteArray,
    private var textDoubleStrike: ByteArray
) : IPrinterTextParserElement {
    private val printer: EscPosPrinter = printerTextParserColumn.line.textParser.printer

    @Throws(EscPosEncodingException::class)
    override fun length(): Int {
        val charsetEncoding = printer.encoding
        val coef = if (textSize.contentEquals(EscPosPrinterCommands.TEXT_SIZE_DOUBLE_WIDTH) ||
            textSize.contentEquals(EscPosPrinterCommands.TEXT_SIZE_BIG)) 2 else 1

        return try {
            text.toByteArray(Charset.forName(charsetEncoding.name)).size * coef
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
    @Throws(EscPosEncodingException::class)
    override fun print(printerSocket: EscPosPrinterCommands?): PrinterTextParserString {
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
