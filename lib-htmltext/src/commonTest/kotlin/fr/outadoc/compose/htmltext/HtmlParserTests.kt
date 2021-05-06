package fr.outadoc.compose.htmltext

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlParserTests {

    private lateinit var parser: HtmlParser

    @BeforeTest
    fun setup() {
        parser = HtmlParser()
    }

    @Test
    fun testPlainText() {
        val input = """
            Hello World!
        """.trimIndent()

        assertEquals(null, parser.parse(input))
    }
}