package com.github.jonnyhsia.synthetics2viewbinding.action

import com.github.jonnyhsia.synthetics2viewbinding.util.PsiElementStore
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtPsiFactory


/**
 * Created by JonnyHsia on 22/4/1.
 * Description
 */
abstract class BaseAction : AnAction(), PsiElementStore {

    lateinit var project: Project
    protected lateinit var file: PsiFile

    override var pkg: PsiElement? = null
    override var imports: PsiElement? = null
    override var kclass: Set<PsiElement> = HashSet()

    val ktPsiFactory by lazy {
        KtPsiFactory(project)
    }

    override fun actionPerformed(e: AnActionEvent) {
        project = e.getData(PlatformDataKeys.PROJECT)!!
        file = e.getData(PlatformDataKeys.PSI_FILE)!!

        parseFile()
    }

    private fun parseFile() {
        file.accept(object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                // element is supposed to be a psi file object
                element.children.forEach {
                    when (it) {
                        is KtPackageDirective -> pkg = it
                        is KtImportList -> imports = it
                        // FIXME: 一个文件可能有多个 KClass
                        is KtClass -> (kclass as MutableSet).add(it)
                    }
                }
            }
        })
    }

    protected fun runWriteCommandAction(block: () -> Unit) {
        WriteCommandAction.runWriteCommandAction(project, block)
    }
}