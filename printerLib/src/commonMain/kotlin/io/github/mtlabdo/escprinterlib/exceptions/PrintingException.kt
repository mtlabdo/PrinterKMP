package io.github.mtlabdo.escprinterlib.exceptions


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun onException(result: Int) {

    runBlocking(Dispatchers.Main) {
        when (result) {
            PrintingException.FINISH_SUCCESS ->
                // "Success", "Congratulation ! The text is printed !"
                null
            PrintingException.FINISH_NO_PRINTER ->
                // "No printer", "The application can't find any printer connected."
                null
            PrintingException. FINISH_PRINTER_DISCONNECTED ->
                // "Disconnected", "The printer is disconnected."
                null
            PrintingException.FINISH_PARSER_ERROR ->
                // "Invalid formatted text", "It seems to be an invalid syntax problem."
                null
            PrintingException.FINISH_ENCODING_ERROR ->
                // "Bad selected encoding", "The selected encoding character returning an error."
                null
            PrintingException.FINISH_BARCODE_ERROR ->
                // "Invalid barcode", "Data send to be converted to barcode or QR code seems to be invalid."
                null
            else ->
                // "Unknown error", "Unknown error."
                null
        }
    }
}

object PrintingException {

    const val FINISH_SUCCESS = 1
    const val FINISH_NO_PRINTER = 2
    const val FINISH_PRINTER_DISCONNECTED = 3
    const val FINISH_PARSER_ERROR = 4
    const val FINISH_ENCODING_ERROR = 5
    const val FINISH_BARCODE_ERROR = 6
}