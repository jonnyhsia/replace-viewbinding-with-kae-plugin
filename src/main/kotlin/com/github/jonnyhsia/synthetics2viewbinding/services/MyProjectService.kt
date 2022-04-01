package com.github.jonnyhsia.synthetics2viewbinding.services

import com.intellij.openapi.project.Project
import com.github.jonnyhsia.synthetics2viewbinding.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
