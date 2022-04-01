package com.github.jonnyhsia.synthetics2viewbinding.util

import com.github.jonnyhsia.synthetics2viewbinding.Const
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeFirstWord

fun CharSequence.toStringBuilder() : StringBuilder {
    if (this is StringBuilder) {
        return this
    }
    return StringBuilder(this)
}

/**
 * 转换为驼峰样式
 */
fun String.toCamelHumpsStyle(
    delimiter : Char = '_', source : StringBuilder = StringBuilder()
) : CharSequence {
    val elements = this.split(delimiter)
    if (elements.size == 1) {
        return elements[0]
    } else {
        source.append(elements[0])
        for (i in 1 until elements.size) {
            source.append(elements[i].capitalizeFirstWord())
        }
        return source
    }
}

/**
 * 布局文件名转换为 Binding
 * activity_main => ActivityMainBinding
 */
fun covertToBindingName(layoutName : String) : String =
    layoutName.toCamelHumpsStyle()
        .toStringBuilder()
        .append(Const.POSTFIX_BINDING)
        .toString()