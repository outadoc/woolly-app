package fr.outadoc.compose.htmltext.model

sealed class FlatNode
data class FlatParagraph(val children: List<FlatNode>) : FlatNode()
data class FlatTextNode(val text: String) : FlatNode()
data class FlatLinkNode(val text: String, val href: String) : FlatNode()
