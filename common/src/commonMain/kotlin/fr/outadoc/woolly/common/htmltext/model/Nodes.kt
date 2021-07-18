package fr.outadoc.woolly.common.htmltext.model

sealed class FlatNode
data class FlatParagraph(val children: List<FlatNode>) : FlatNode()
data class FlatTextNode(val text: String) : FlatNode()
data class FlatLinkNode(val href: String, val children: List<FlatNode>) : FlatNode()
