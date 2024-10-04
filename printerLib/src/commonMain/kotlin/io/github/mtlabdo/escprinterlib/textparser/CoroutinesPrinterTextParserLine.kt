package io.github.mtlabdo.escprinterlib.textparser


import kotlin.math.floor

class CoroutinesPrinterTextParserLine(val textParser: CoroutinesPrinterTextParser, textLine: String) {
    val nbrColumns: Int
    var nbrCharColumn: Int
        private set
    var nbrCharForgetted: Int
        private set
    var nbrCharColumnExceeded: Int
        private set
    val columns: Array<CoroutinesPrinterTextParserColumn?>
    fun setNbrCharColumn(newValue: Int): CoroutinesPrinterTextParserLine {
        nbrCharColumn = newValue
        return this
    }

    fun setNbrCharForgetted(newValue: Int): CoroutinesPrinterTextParserLine {
        nbrCharForgetted = newValue
        return this
    }

    fun setNbrCharColumnExceeded(newValue: Int): CoroutinesPrinterTextParserLine {
        nbrCharColumnExceeded = newValue
        return this
    }

    init {
        val nbrCharactersPerLine = textParser.printer.printerNbrCharactersPerLine
        val regex = Regex(PrinterTextParser.regexAlignTags!!) // Use Kotlin's Regex class
        val columnsList = ArrayList<String>()
        var lastPosition = 0

        // Find all matches using Kotlin's Regex
        regex.findAll(textLine).forEach { matchResult ->
            val startPosition = matchResult.range.first
            if (startPosition > 0) {
                columnsList.add(textLine.substring(lastPosition, startPosition))
            }
            lastPosition = startPosition
        }

        // Add remaining part of the text after the last match
        columnsList.add(textLine.substring(lastPosition))

        nbrColumns = columnsList.size
        nbrCharColumn = floor((nbrCharactersPerLine.toFloat() / nbrColumns.toFloat()).toDouble()).toInt()
        nbrCharForgetted = nbrCharactersPerLine - nbrCharColumn * nbrColumns
        nbrCharColumnExceeded = 0
        columns = arrayOfNulls(nbrColumns)

        // Fill the columns array
        for ((i, column) in columnsList.withIndex()) {
            columns[i] = CoroutinesPrinterTextParserColumn(this, column)
        }
    }



}
