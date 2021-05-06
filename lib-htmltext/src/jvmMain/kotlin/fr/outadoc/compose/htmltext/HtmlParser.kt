package fr.outadoc.compose.htmltext

import org.jsoup.Jsoup

actual class HtmlParser {

    actual fun parse(html: String) {
        val document = Jsoup.parse(html)

    }
}