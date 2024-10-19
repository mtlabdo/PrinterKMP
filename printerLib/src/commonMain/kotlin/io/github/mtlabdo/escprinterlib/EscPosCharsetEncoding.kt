package io.github.mtlabdo.escprinterlib

class EscPosCharsetEncoding(val name: String, escPosCharsetId: Int) {
    val command: ByteArray = byteArrayOf(0x1B, 0x74, escPosCharsetId.toByte())


    companion object {
        // Predefined charset IDs for common charsets
        val CP437 = EscPosCharsetEncoding("CP437 (USA)", 0)
        val CP850 = EscPosCharsetEncoding("CP850 (Multilingual)", 2)
        val WINDOWS_1252 = EscPosCharsetEncoding("Windows-1252 (Western European)", 16)
        val UTF_8 = EscPosCharsetEncoding("UTF-8", 0)
    }
}
