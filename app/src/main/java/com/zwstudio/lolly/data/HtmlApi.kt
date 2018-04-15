package com.zwstudio.lolly.data

fun extractTextFrom(html: String, transform: String, template: String, templateHandler: (String, String) -> String): String {
    val dic = mapOf("<delete>" to "", "\\t" to "\t", "\\r" to "\r", "\\n" to "\n")

    var text = ""
    do {
        if (transform.isEmpty()) break
        val arr = transform.split("\r\n")
        var regex = Regex(arr[0])
        val m = regex.find(html)
        if (m == null) break
        text = m.groupValues[0]

        fun f(replacer: String) {
            var replacer = replacer
            for ((key, value) in dic)
                replacer = replacer.replace(key, value)
            text = regex.replace(text, replacer)
        }

        f(arr[1])
        for (i in 2 until arr.size)
            if (i % 2 == 0)
                regex = Regex(arr[i])
            else
                f(arr[i])

        if (template.isEmpty()) break
        text = templateHandler(text, template)

    } while (false)
    return text
}