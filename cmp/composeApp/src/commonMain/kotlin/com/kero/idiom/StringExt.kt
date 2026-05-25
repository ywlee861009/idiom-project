package com.kero.idiom

private val htmlTagRegex = Regex("<[^>]+>")

fun String.stripHtml(): String =
    this.replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace("&apos;", "'")
        .replace(htmlTagRegex, "")
