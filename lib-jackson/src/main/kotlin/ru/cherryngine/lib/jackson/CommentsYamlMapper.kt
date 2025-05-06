package ru.cherryngine.lib.jackson

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.DataOutput
import java.io.File
import java.io.OutputStream
import java.io.Writer
import java.util.regex.Pattern

class CommentsYamlMapper(
    private val mapper: YAMLMapper,
    private val suffix: String = "_",
) : YAMLMapper(mapper) {
    override fun writeValue(resultFile: File, value: Any) {
        resultFile.writeText(writeValueAsString(value))
    }

    override fun writeValue(out: OutputStream, value: Any) {
        out.write(writeValueAsBytes(value))
    }

    override fun writeValue(out: DataOutput, value: Any) {
        out.write(writeValueAsBytes(value))
    }

    override fun writeValue(w: Writer, value: Any) {
        w.write(writeValueAsString(value))
    }

    override fun writeValueAsString(value: Any): String {
        val pattern = Pattern.compile("(?<spaces>\\s*)\\S+${suffix}: \"(?<comment>.+)\"")
        val result = ArrayList<String>()
        mapper.writeValueAsString(value).lineSequence().forEach { line ->
            val matcher = pattern.matcher(line)
            if (matcher.matches()) {
                val spaces = matcher.group("spaces")
                val comment = matcher.group("comment").split("\\n")
                comment.forEach { result.add("$spaces# $it") }
            } else {
                result.add(line)
            }
        }
        return result.joinToString("\n")
    }

    override fun writeValueAsBytes(value: Any): ByteArray {
        return writeValueAsString(value).toByteArray()
    }
}