package io.github.mtlabdo.escprinterlib

import io.github.mtlabdo.escprinterlib.exceptions.EscPosBarcodeException
import io.github.mtlabdo.escprinterlib.connection.DeviceConnection
import io.github.mtlabdo.escprinterlib.exceptions.EscPosConnectionException
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException
import io.github.mtlabdo.escprinterlib.exceptions.EscPosParserException
import io.github.mtlabdo.escprinterlib.textparser.IPrinterTextParserElement
import io.github.mtlabdo.escprinterlib.textparser.PrinterTextParser
import io.github.mtlabdo.escprinterlib.textparser.PrinterTextParserColumn
import io.github.mtlabdo.escprinterlib.textparser.PrinterTextParserLine
import io.github.mtlabdo.escprinterlib.textparser.PrinterTextParserString
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.JvmOverloads


class EscPosPrinter(
    printer: EscPosPrinterCommands?,
    printerDpi: Int,
    printerWidthMM: Float,
    printerNbrCharactersPerLine: Int
) : EscPosPrinterSize(printerDpi, printerWidthMM, printerNbrCharactersPerLine) {
    private var printer: EscPosPrinterCommands? = null

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printerConnection           Instance of class which implement DeviceConnection
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     */
    constructor(
        printerConnection: DeviceConnection?,
        printerDpi: Int,
        printerWidthMM: Float,
        printerNbrCharactersPerLine: Int
    ) : this(
        if (printerConnection != null) EscPosPrinterCommands(printerConnection) else null,
        printerDpi,
        printerWidthMM,
        printerNbrCharactersPerLine
    )

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printerConnection           Instance of class which implement DeviceConnection
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     * @param charsetEncoding             Set the charset encoding.
     */
    constructor(
        printerConnection: DeviceConnection?,
        printerDpi: Int,
        printerWidthMM: Float,
        printerNbrCharactersPerLine: Int,
        charsetEncoding: EscPosCharsetEncoding?
    ) : this(
        if (printerConnection != null) EscPosPrinterCommands(
            printerConnection, charsetEncoding
        ) else null, printerDpi, printerWidthMM, printerNbrCharactersPerLine
    )

    /**
     * Create new instance of EscPosPrinter.
     *
     * @param printer                     Instance of EscPosPrinterCommands
     * @param printerDpi                  DPI of the connected printer
     * @param printerWidthMM              Printing width in millimeters
     * @param printerNbrCharactersPerLine The maximum number of characters that can be printed on a line.
     */
    init {
        if (printer != null) {
            this.printer = printer.connect()
        }
    }

    /**
     * Close the connection with the printer.
     *
     * @return Fluent interface
     */
    fun disconnectPrinter(): EscPosPrinter {
        if (this.printer != null) {
            printer!!.disconnect()
            this.printer = null
        }
        return this
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text        Formatted text to be printed.
     * @param mmFeedPaper millimeter distance feed paper at the end.
     * @return Fluent interface
     */
    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text Formatted text to be printed.
     * @return Fluent interface
     */
    @JvmOverloads
    @Throws(
        EscPosConnectionException::class,
        EscPosParserException::class,
        EscPosEncodingException::class,
        EscPosBarcodeException::class,
        CancellationException::class
    )
    suspend fun printFormattedText(text: String?, mmFeedPaper: Float = 20f): EscPosPrinter {
        return this.printFormattedText(text, this.mmToPx(mmFeedPaper))
    }

    /**
     * Print a formatted text. Read the README.md for more information about text formatting options.
     *
     * @param text          Formatted text to be printed.
     * @param dotsFeedPaper distance feed paper at the end.
     * @return Fluent interface
     */
    @Throws(
        EscPosConnectionException::class,
        EscPosParserException::class,
        EscPosEncodingException::class,
        EscPosBarcodeException::class,
        CancellationException::class
    )
    suspend fun printFormattedText(text: String?, dotsFeedPaper: Int): EscPosPrinter {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this
        }

        val textParser: PrinterTextParser = PrinterTextParser(this)
        val linesParsed: Array<PrinterTextParserLine?> =
            textParser.setFormattedText(text ?: "").parse()

        printer!!.reset()

        for (line in linesParsed) {
            val columns: Array<PrinterTextParserColumn?> = line?.columns ?: emptyArray()

            var lastElement: IPrinterTextParserElement? = null
            for (column in columns) {
                val elements: Array<IPrinterTextParserElement?> = column?.elements ?: emptyArray()
                for (element in elements) {
                    element?.print(this.printer)
                    lastElement = element
                }
            }

            if (lastElement is PrinterTextParserString) {
                printer!!.newLine()
            }
        }

        printer!!.feedPaper(dotsFeedPaper)
        return this
    }

    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text        Formatted text to be printed.
     * @param mmFeedPaper millimeter distance feed paper at the end.
     * @return Fluent interface
     */
    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text Formatted text to be printed.
     * @return Fluent interface
     */
    @JvmOverloads
    @Throws(
        EscPosConnectionException::class,
        EscPosParserException::class,
        EscPosEncodingException::class,
        EscPosBarcodeException::class, CancellationException::class
    )
    suspend fun printFormattedTextAndCut(text: String?, mmFeedPaper: Float = 20f): EscPosPrinter {
        return this.printFormattedTextAndCut(text, this.mmToPx(mmFeedPaper))
    }

    /**
     * Print a formatted text and cut the paper. Read the README.md for more information about text formatting options.
     *
     * @param text          Formatted text to be printed.
     * @param dotsFeedPaper distance feed paper at the end.
     * @return Fluent interface
     */
    @Throws(
        EscPosConnectionException::class,
        EscPosParserException::class,
        EscPosEncodingException::class,
        EscPosBarcodeException::class,
        CancellationException::class
    )
    suspend fun printFormattedTextAndCut(text: String?, dotsFeedPaper: Int): EscPosPrinter {
        if (this.printer == null || this.printerNbrCharactersPerLine == 0) {
            return this
        }

        this.printFormattedText(text, dotsFeedPaper)

        //this.printer.cutPaper();
        return this
    }

    val encoding: EscPosCharsetEncoding
        /**
         * @return Charset encoding
         */
        get() = printer!!.charsetEncoding
}
