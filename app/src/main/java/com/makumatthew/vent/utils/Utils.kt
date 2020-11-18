package com.makumatthew.vent.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

fun isHashtagValid(hashtag: String?): Boolean {
    val expression = "^#[a-zA-Z]+$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(hashtag)
    return matcher.matches()
}