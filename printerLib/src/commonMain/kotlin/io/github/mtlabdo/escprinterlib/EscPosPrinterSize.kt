package io.github.mtlabdo.escprinterlib

import kotlin.math.roundToInt

open class EscPosPrinterSize constructor(
    /**
     * Get the printer DPI
     *
     * @return int
     */
    var printerDpi: Int,
    /**
     * Get the printing width in millimeters
     *
     * @return float
     */
    var printerWidthMM: Float,
    /**
     * Get the maximum number of characters that can be printed on a line.
     *
     * @return int
     */
    var printerNbrCharactersPerLine: Int
) {

    /**
     * Get the printing width in dot
     *
     * @return int
     */
    var printerWidthPx: Int
        protected set

    /**
     * Get the number of dot that a printed character contain
     *
     * @return int
     */
    var printerCharSizeWidthPx: Int
        protected set

    /**
     * Convert from millimeters to dot the mmSize variable.
     *
     * @param mmSize Distance in millimeters to be converted
     * @return int
     */
    fun mmToPx(mmSize: Float): Int = (mmSize * printerDpi.toFloat() / INCH_TO_MM).roundToInt()


    companion object {
        const val INCH_TO_MM = 25.4f
    }

    init {
        val printingWidthPx = mmToPx(printerWidthMM)
        printerWidthPx = printingWidthPx + printingWidthPx % 8
        printerCharSizeWidthPx = printingWidthPx / printerNbrCharactersPerLine
    }
}