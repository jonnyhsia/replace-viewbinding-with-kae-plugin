package com.github.jonnyhsia.synthetics2viewbinding.action

import com.github.jonnyhsia.synthetics2viewbinding.Const
import com.github.jonnyhsia.synthetics2viewbinding.util.IReplaceClass
import com.github.jonnyhsia.synthetics2viewbinding.util.IReplaceFile
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.util.containers.mapSmartSet
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.resolve.ImportPath

/**
 * Created by JonnyHsia on 22/4/1.
 * Description
 */

class Synthetics2ViewBindingAction : BaseAction(), IReplaceFile {

    override var syntheticsImports: List<KtImportDirective> = emptyList()
    override var syntheticsNames: Set<String> = emptySet()

    private var replaceInClassImplList: List<IReplaceClass> = emptyList()

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        replaceInClassImplList = kclass.map {
            Synthetics2ViewBindingInKClass(file = this, element = it as KtClass)
        }

        collectSynthetics()                     // ok
        collectPageLayout()                     // ok
        guessBindingName()                      // ok
        addViewBindingImport()                  // ok
        addViewBindingProperty()                // not yet
        replaceSyntheticsViewWithViewBinding()  // not yet
        clearSynthetics()                       // ok
    }

    /**
     * 收集 Synthetics 导包
     */
    override fun collectSynthetics() {
        syntheticsImports = (imports as KtImportList).imports.filter {
            // [import kotlinx.android.synthetic.demo_activity]
            it.text.startsWith(Const.SYNTHETICS_IMPORT)
        }
        syntheticsNames = syntheticsImports.mapSmartSet {
            // kotlinx.android.synthetic.demo_activity
            // kotlinx.android.synthetic.demo_activity.textView
            it.importedFqName.toString()
        }
        WriteCommandAction.runWriteCommandAction(project) {
            val i = ktPsiFactory.createImportDirective(ImportPath.fromString("com.jonnyhsia.demo.dataBinding.viewBinding"))
            (imports as KtImportList).add(i)
        }
    }

    /**
     * 收集页面内 Layout 信息
     */
    override fun collectPageLayout() {
        replaceInClassImplList.forEach {
            it.collectPageLayout()
        }
    }

    /**
     * 通过 Synthetics 导包与 Layout 信息推断 ViewBinding 类型
     */
    override fun guessBindingName() {
        replaceInClassImplList.forEach {
            it.guessBindingName()
        }
    }

    /**
     * 导入 ViewBinding 包名
     */
    override fun addViewBindingImport() {
        replaceInClassImplList.forEach {
            it.addViewBindingImport()
        }

    }

    /**
     * 添加 ViewBinding 声明
     */
    override fun addViewBindingProperty() {
        replaceInClassImplList.forEach {
            it.addViewBindingProperty()
        }
    }

    /**
     * 将通过 Synthetics 访问 View 的代码替换成 ViewBinding
     */
    override fun replaceSyntheticsViewWithViewBinding() {
        replaceInClassImplList.forEach {
            it.replaceSyntheticsViewWithViewBinding()
        }
    }


    /**
     * 移除 Synthetics 导包代码
     */
    override fun clearSynthetics() {
        runWriteCommandAction {
            syntheticsImports.forEach {
//                it.delete()
            }
        }
    }
}