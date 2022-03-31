package com.github.jonnyhsia.replaceviewbindingwithkaeplugin.services

import com.intellij.openapi.project.Project
import com.github.jonnyhsia.replaceviewbindingwithkaeplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
