package com.github.jonnyhsia.synthetics2viewbinding.util

import com.intellij.psi.PsiElement

/**
 * Created by JonnyHsia on 22/4/1.
 * Description
 */
interface PsiElementStore {
    var pkg: PsiElement?
    var imports: PsiElement?
    var kclass: PsiElement?
}