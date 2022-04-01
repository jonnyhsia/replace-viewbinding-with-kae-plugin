package com.github.jonnyhsia.synthetics2viewbinding.util

import com.github.jonnyhsia.synthetics2viewbinding.Const
import java.util.*

fun CharSequence.toStringBuilder(): StringBuilder {
    if (this is StringBuilder) {
        return this
    }
    return StringBuilder(this)
}

/**
 * 转换为驼峰样式
 */
fun String.toCamelHumpsStyle(
    delimiter: Char = '_',
    capitalizeFirstWord: Boolean = true,
    source: StringBuilder = StringBuilder()
): CharSequence {
    val elements = this.split(delimiter)
    if (elements.size == 1) {
        return elements[0]
    } else {
        val start: Int
        if (capitalizeFirstWord) {
            start = 0
        } else {
            start = 1
            source.append(elements[0])
        }
        for (i in start until elements.size) {
            source.append(elements[i].capitalizeFirstChar())
        }
        return source
    }
}

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }
}

fun buildViewBindingImportPath(pkg: String, name: String): String {
    return "${pkg}.dataBinding.${name}"
}

/**
 * 布局文件名转换为 Binding
 * activity_main => ActivityMainBinding
 */
fun convertToBindingName(layoutName: String): String =
    layoutName.toCamelHumpsStyle().toStringBuilder().append(Const.POSTFIX_BINDING).toString()