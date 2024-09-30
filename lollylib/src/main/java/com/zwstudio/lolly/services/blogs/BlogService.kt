package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel

class BlogService {
    private fun html1With(s: String) =
        """<strong><span style="color:#0000ff;">$s</span></strong>"""
    private fun htmlWordWith(s: String) = html1With("$s：")
    private fun htmlBWith(s: String) = html1With(s)
    private fun htmlE1With(s: String) =
        """<span style="color:#006600;">$s</span>"""
    private fun html2With(s: String) =
        """<span style="color:#cc00cc;">$s</span>"""
    private fun htmlE2With(s: String) = html2With(s)
    private fun htmlIWith(s: String) = "<strong>${html2With(s)}</strong>"
    private val htmlEmptyLine = "<div><br></div>"
    private val regMarkedEntry = Regex("""(\*\*?)\s*(.*?)：(.*?)：(.*)""")
    private val regMarkedB = Regex("<B>(.+?)</B>")
    private val regMarkedI = Regex("<I>(.+?)</I>")
    fun markedToHtml(text: String): String {
        val lst = text.split("\n").toMutableList()
        var i = 0
        while (i < lst.size) {
            var s = lst[i]
            val m = regMarkedEntry.find(s)
            if (m != null) {
                val s1 = m.groupValues[1]
                val s2 = m.groupValues[2]
                val s3 = m.groupValues[3]
                val s4 = m.groupValues[4]
                s = htmlWordWith(s2) + (if (s3.isEmpty()) "" else htmlE1With(s3)) + (if (s4.isEmpty()) "" else htmlE2With(s4))
                lst[i] = (if (s1 == "*") "<li>" else "<br>") + s
                if (i == 0 || lst[i - 1].startsWith("<div>"))
                    lst.add(i++, "<ul>")
                val isLast = i == lst.size - 1
                val m2 = if (isLast) null else regMarkedEntry.find(lst[i + 1])
                if (isLast || m2 == null || m2.groupValues[1] != "**")
                    lst[i] += "</li>"
                if (isLast || m2 == null)
                    lst.add(++i, "</ul>")
            } else if (s.isEmpty())
                lst[i] = htmlEmptyLine
            else {
                s = regMarkedB.replace(s, htmlBWith("$1"))
                s = regMarkedI.replace(s, htmlIWith("$1"))
                lst[i] = "<div>$s</div>"
            }
            i++
        }
        return lst.joinToString("\n")
    }
    private val regLine = Regex("<div>(.*?)</div>")
    private val regHtmlB = Regex(htmlBWith("(.+?)"))
    private val regHtmlI = Regex(htmlIWith("(.+?)"))
    private val regHtmlEntry = Regex("(<li>|<br>)${htmlWordWith("(.*?)")}(?:${htmlE1With("(.*?)")})?(?:${htmlE2With("(.*?)")})?(?:</li>)?")
    fun htmlToMarked(text: String): String {
        val lst = text.split("\n").toMutableList()
        var i = 0
        while (i < lst.size) {
            var s = lst[i]
            if (s == "<!-- wp:html -->" || s == "<!-- /wp:html -->" || s == "<ul>" || s == "</ul>")
                lst.removeAt(i--)
            else if (s == htmlEmptyLine)
                lst[i] = ""
            else {
                var m = regLine.find(s)
                if (m != null) {
                    s = m.groupValues[1]
                    s = regHtmlB.replace(s, "<B>$1</B>")
                    s = regHtmlI.replace(s, "<I>$1</I>")
                    lst[i] = s
                } else {
                    m = regHtmlEntry.find(s)
                    if (m != null) {
                        val s1 = m.groupValues[1]
                        val s2 = m.groupValues[2]
                        val s3 = m.groupValues[3]
                        val s4 = m.groupValues[4]
                        s = (if (s1 == "<li>") "*" else "**") + " $s2：$s3：$s4"
                        lst[i] = s
                    }
                }
            }
            i++
        }
        return lst.joinToString("\n")
    }
    fun addTagB(text: String) = "<B>$text</B>"
    fun addTagI(text: String) = "<I>$text</I>"
    fun removeTagBI(text: String) = Regex("</?[BI]>").replace(text, "")
    fun exchangeTagBI(text: String): String {
        var text = Regex("<(/)?B>").replace(text, "<$1Temp>")
        text = Regex("<(/)?I>").replace(text, "<$1B>")
        text = Regex("<(/)?Temp>").replace(text, "<$1I>")
        return text
    }
    fun getExplanation(text: String) = "* $text：：\n"
    fun getPatternUrl(patternNo: String) = "http://viethuong.web.fc2.com/MONDAI/$patternNo.html"
    fun getPatternMarkDown(patternText: String) = "* [$patternText　文法](https://www.google.com/search?q=$patternText　文法)\n* [$patternText　句型](https://www.google.com/search?q=$patternText　句型)"
    private val bigDigits = "０１２３４５６７８９"
    suspend fun addNotes(vmSettings: SettingsViewModel, text: String, allComplete: (String) -> Unit) {
        fun f(s: String): String {
            var s = s
            for (i in 0 until 10)
                s = s.replace('0'.plus(i), bigDigits[i])
            return s
        }
        val items = text.split("\n").toMutableList()
        vmSettings.getNotes(items.size, {
            val m = regMarkedEntry.find(items[it]) ?: return@getNotes false
            val word = m.groupValues[2]
            return@getNotes word.all { it != '（' && !bigDigits.contains(it) }
        }, {
            val i = it
            val m = regMarkedEntry.find(items[i])!!
            val s1 = m.groupValues[1]
            val word = m.groupValues[2]
            val s3 = m.groupValues[3]
            val s4 = m.groupValues[4]
            val note = vmSettings.getNote(word)
            val j = note.indexOfFirst { it.isDigit() }
            val s21 = if (j == -1) note else note.substring(0, j)
            val s22 = if (j == -1) "" else f(note.substring(j))
            val s2 = word + (if (s21 == word || s21.isEmpty()) "" else "（$s21）") + s22
            items[i] = "$s1 $s2：$s3：$s4"
        }) { allComplete(items.joinToString("\n")) }
    }
}
