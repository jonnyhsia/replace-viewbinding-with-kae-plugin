package com.github.jonnyhsia.synthetics2viewbinding.action

import com.github.jonnyhsia.synthetics2viewbinding.Const
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtImportList

/**
 * Created by JonnyHsia on 22/4/1.
 * Description
 */

class Synthetics2ViewBindingAction : BaseAction() {

    override fun actionPerformed(e : AnActionEvent) {
        super.actionPerformed(e)

        var foundSynthetic = false
        file.children.forEach { element ->
            when (element) {
                is KtImportList -> {
                    parseImportList(element)
                }
                else            -> {
                    println(element.toString())
                }
            }
        }
    }

    private fun parseImportList(element : KtImportList) {
        // 遍历 import 语句, 找出删除 synthetics 导包, 并推断出 ViewBinding 类型
        element.imports.forEach {
            // kotlinx.synthetics.main_activity.*
            // kotlinx.synthetics.main_activity.tvName
            val expression = it.importedFqName?.asString()
                ?: return@forEach
            if (expression.startsWith(Const.SYNTHETICS_IMPORT)) {

            }
            // TODO: => MainActivityBinding
        }
    }
}