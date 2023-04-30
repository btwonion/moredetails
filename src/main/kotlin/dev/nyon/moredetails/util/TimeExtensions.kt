package dev.nyon.moredetails.util

fun Int.assertMissingNull(): String = if (this < 10) "0$this" else this.toString()