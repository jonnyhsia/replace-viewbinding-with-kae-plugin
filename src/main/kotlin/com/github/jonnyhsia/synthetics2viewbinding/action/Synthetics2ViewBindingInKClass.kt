package com.github.jonnyhsia.synthetics2viewbinding.action

import com.github.jonnyhsia.synthetics2viewbinding.Const
import com.github.jonnyhsia.synthetics2viewbinding.util.IReplaceClass
import com.github.jonnyhsia.synthetics2viewbinding.util.IReplaceFile
import com.github.jonnyhsia.synthetics2viewbinding.util.buildViewBindingImportPath
import com.github.jonnyhsia.synthetics2viewbinding.util.convertToBindingName
import com.intellij.openapi.command.WriteCommandAction
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.getOrCreateBody
import org.jetbrains.kotlin.resolve.ImportPath

class Synthetics2ViewBindingInKClass(
    private val file: IReplaceFile, private val element: KtClass
) : IReplaceClass {

    private var layoutName: String? = null

    private var bindingPackage: String? = null
    private var bindingName: String? = null

    override fun collectPageLayout() {
        val classBody = element.children[0] as KtClassBody
        val layoutFunc = classBody.functions.firstOrNull { func ->
            // com.jonnyhsia.demo.DemoActivity.getLayoutId
            func.fqName?.asString()?.endsWith(Const.FUNC_LAYOUT_ID) == true
        }
        // R.layout.demo_activity => demo_activity
        layoutName = layoutFunc?.bodyExpression?.text?.split('.')?.lastOrNull()

        WriteCommandAction.runWriteCommandAction(file.project) {
            layoutFunc?.delete()
        }
    }

    override fun guessBindingName() {
        if (layoutName != null) {
            // 找到布局文件就直接根据文件名推断 Binding 名称
            bindingName = convertToBindingName(layoutName!!)
        } else {
            // 没有布局文件, 则根据 Synthetics 导包推断 Binding 名称
            // [kotlinx, android, synthetic, demo_activity, textView] => demo_activity
            // FIXME: 如果去重后仍多个 Synthetics 导包
            val syntheticsLayoutName = file.syntheticsNames.first().split('.')[3]
            bindingName = convertToBindingName(syntheticsLayoutName)
        }
    }

    override fun addViewBindingImport() {
        // TODO: 从 module manifest 中获取 package
        // com.jonnyhsia.demo.view.login => com.jonnyhsia.demo
        val filePkg = (file.pkg as KtPackageDirective).fqName.asString()
        // 暂定 module 的 package 是前三项
        val expiredCount = 3
        var meetDotCount = 0
        // 包名截断的索引值
        var subIndex = filePkg.length

        for (i in filePkg.indices) {
            val c = filePkg[i]
            if (c == '.' && ++meetDotCount == expiredCount) {
                subIndex = i
                break
            }
        }

        // com.jonnyhsia.demo
        bindingPackage = filePkg.substring(0, subIndex)

        val importPath = buildViewBindingImportPath(bindingPackage!!, bindingName!!)
        val import = file.ktPsiFactory.createImportDirective(ImportPath.fromString(importPath))
        WriteCommandAction.runWriteCommandAction(file.project) {
            file.imports?.add(import)
        }
    }

    override fun addViewBindingProperty() {
        val bindingProperty = file.ktPsiFactory.createProperty("private val binding by viewBinding { $bindingName() }")
        // val block = file.ktPsiFactory.createBlock("val binding by viewBinding { $bindingName() }")

        WriteCommandAction.runWriteCommandAction(file.project) {
            element.addBefore(bindingProperty, element.body?.declarations?.firstOrNull())
//            element.addBefore(block, element.body?.declarations?.firstOrNull())
        }
    }

    override fun replaceSyntheticsViewWithViewBinding() {
    }
}