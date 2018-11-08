package com.yuntian.plugin.aspectj

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

class AspectjPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        final def log = project.logger
        final def variants = project.android.applicationVariants

        log.error "========================";
        log.error "AspectjPlugin开始修改Class!";
        log.error "========================";

        variants.all { variant ->

            //可以控制release是否加入Aspectj
//            if (!variant.buildType.isDebuggable()) {
//                log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
//                return;
//            }

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(), // class文件路径
                                 "-aspectpath", javaCompile.classpath.asPath, // 依赖包jar,arr路径
                                 "-d", javaCompile.destinationDir.toString(), // class文件路径
                                 "-classpath", javaCompile.classpath.asPath, // 依赖包jar,arr路径
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)] // android.jar路径
                log.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }

    }
}

