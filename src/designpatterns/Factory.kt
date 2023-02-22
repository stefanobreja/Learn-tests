package designpatterns

import jdk.jshell.spi.ExecutionControl.NotImplementedException
import org.junit.Test
import java.io.File

/**
 * Factory pattern
 * Provides a ready to use object to its client
 * Hides the complexity of creating and selecting the right object for the job
 * */

interface FileParser
class XmlFileParser : FileParser
class JsonFileParser : FileParser

interface FileParserFactory {
    fun createFromFileName(fileName: String): FileParser
}

class StandardFileParserFactory {
    companion object : FileParserFactory {
        override fun createFromFileName(fileName: String): FileParser =
            when (fileName.substringAfterLast(".")) {
                "xml" -> XmlFileParser()
                "json" -> JsonFileParser()
                else -> throw NotImplementedException("There is no implementation for this type of file")
            }
    }
}

class FactoryImplementation {
    @Test
    fun test() {
        val file = "test.xml"
        val parser = StandardFileParserFactory.createFromFileName(file)
        assert(parser is XmlFileParser)
    }
}