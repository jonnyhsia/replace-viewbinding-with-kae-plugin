package com.github.jonnyhsia.replaceviewbindingwithkaeplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import org.jetbrains.kotlin.psi.KtImportList

/**
 * Created by JonnyHsia on 22/4/1.
 * Description
 */
private const val SYNTHETICS_IMPORT = "kotlinx.synthetics"

class ReplaceAction : AnAction() {

    override fun actionPerformed(e : AnActionEvent) {
        val project = e.getData(PlatformDataKeys.PROJECT)
        val editor = e.getData(PlatformDataKeys.EDITOR)
        val psiFile = e.getData(PlatformDataKeys.PSI_FILE)

        Messages.showInfoMessage(
            project,
            "Replace synthetics with view binding",
            "Place Check Code"
        )
        psiFile?.children?.forEach { element ->
            when (element) {
                is KtImportList -> {
                    // 遍历 import 语句, 找出删除 synthetics 导包, 并推断出 ViewBinding 类型
                    element.imports.forEach {
                        // import kotlinx.synthetics.main_activity.*
                        val importParentName = it.importedFqName?.parent()?.asString()
                        if (SYNTHETICS_IMPORT == importParentName) {
                            WriteCommandAction.runWriteCommandAction(project) {
                                it.delete()
                                println("删除了 synthetics 的导包")
                            }
                        }
                        // TODO: => MainActivityBinding
                    }
                }
                else            -> {
                    println(element.toString())
                }
            }
        }
    }
}