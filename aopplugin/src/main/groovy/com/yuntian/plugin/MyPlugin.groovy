package com.yuntian.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "-----------this is MyPlugin plugin!----------"
        println "-----------this is MyPlugin plugin!----------"
        println "-----------this is MyPlugin plugin!----------"
        println "-----------this is MyPlugin plugin!----------"
        println "-----------this is MyPlugin plugin!----------"
        println "-----------哈哈哈----------"

    }
}