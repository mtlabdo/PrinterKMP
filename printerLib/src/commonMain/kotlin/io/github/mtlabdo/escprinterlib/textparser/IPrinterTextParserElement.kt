package io.github.mtlabdo.escprinterlib.textparser

import io.github.mtlabdo.escprinterlib.EscPosPrinterCommands
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException

interface IPrinterTextParserElement {
    @Throws(EscPosEncodingException::class)
    fun length(): Int

    @Throws(EscPosEncodingException::class)
    fun print(printerSocket: EscPosPrinterCommands?): IPrinterTextParserElement?
}
