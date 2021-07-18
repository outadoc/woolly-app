package fr.outadoc.woolly.common.htmltext

import fr.outadoc.woolly.common.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.common.htmltext.model.FlatParagraph
import fr.outadoc.woolly.common.htmltext.model.FlatTextNode
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
            FlatLinkNode(
                href = "https://website/test",
                children = listOf(FlatTextNode("Hello World!"))
            )
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
                    FlatLinkNode(
                        href = "https://website/test",
                        children = listOf(FlatTextNode("World"))
                    ),
                    FlatTextNode("!")
                )
            )
        )

        assertEquals(expected, parser.parse(input))
    }

    @Test
    fun testTwitterLinkReplacement() {
        val input = """
            Take a look at this, @outadoc@twitter.com!
        """.trimIndent()

        val expected = listOf(
            FlatTextNode("Take a look at this, "),
            FlatLinkNode(
                href = "https://twitter.com/outadoc",
                children = listOf(FlatTextNode("@outadoc@twitter.com"))
            ),
            FlatTextNode("!")
        )

        assertEquals(expected, parser.parse(input))
    }
}
