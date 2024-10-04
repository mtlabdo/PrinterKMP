package io.github.mtlabdo.escprinterlib.textparser

import io.github.mtlabdo.escprinterlib.CoroutinesEscPosPrinterCommands
import io.github.mtlabdo.escprinterlib.exceptions.EscPosEncodingException
import kotlin.coroutines.cancellation.CancellationException

interface CoroutinesIPrinterTextParserElement {
    @Throws(EscPosEncodingException::class)
    fun length(): Int

    @Throws(EscPosEncodingException::class, CancellationException::class)
    suspend fun print(printerSocket: CoroutinesEscPosPrinterCommands?): CoroutinesIPrinterTextParserElement?
}
