package fr.outadoc.woolly.htmltext

import fr.outadoc.woolly.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.htmltext.model.FlatParagraph
import fr.outadoc.woolly.htmltext.model.FlatTextNode
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

        val expected = listOf(
            FlatTextNode("Hello World!")
        )

        assertEquals(expected, parser.parse(input))
    }

    @Test
    fun testOuterParagraph() {
        val input = """
            <p>Hello World!</p>
        """.trimIndent()

        val expected = listOf(
            FlatParagraph(
                listOf(FlatTextNode("Hello World!"))
            )
        )

        assertEquals(expected, parser.parse(input))
    }

    @Test
    fun testOuterLink() {
        val input = """
            <a href="https://website/test">Hello World!</a>
        """.trimIndent()

        val expected = listOf(
            FlatLinkNode("Hello World!", href = "https://website/test")
        )

        assertEquals(expected, parser.parse(input))
    }

    @Test
    fun testOuterParagraphWithLink() {
        val input = """
            <p>Hello <a href="https://website/test">World</a>!</p>
        """.trimIndent()

        val expected = listOf(
            FlatParagraph(
                listOf(
                    FlatTextNode("Hello "),
                    FlatLinkNode("World", href = "https://website/test"),
                    FlatTextNode("!")
                )
            )
        )

        assertEquals(expected, parser.parse(input))
    }
}
