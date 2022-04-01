package com.github.jonnyhsia.synthetics2viewbinding.util

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPsiFactory

interface IReplace {
}

interface IReplaceClass : IReplace {
    fun collectPageLayout()
    fun guessBindingName()
    fun addViewBindingImport()
    fun addViewBindingProperty()
    fun replaceSyntheticsViewWithViewBinding()
}

interface IReplaceFile : IReplaceClass, PsiElementStore {
    var syntheticsImports: List<KtImportDirective>
    var syntheticsNames: Set<String>

    val ktPsiFactory: KtPsiFactory
    var project: Project

    fun collectSynthetics()
    fun clearSynthetics()
}