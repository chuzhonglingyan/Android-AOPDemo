package com.yuntian.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.task('hello') {
            doFirst{
                println"任务执行之前"
            }

            doLast{
                println"任务执行之后"
            }
        }

    }
}