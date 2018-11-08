package com.yuntian.plugin.aspectj

import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectjLibPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        final def log = project.logger
        final def variants = project.android.libraryVariants

        log.error "========================";
        log.error "AspectjLibPlugin开始修改Class!";
        log.error "========================";

        variants.all{ variant ->

            LibraryPlugin plugin = project.plugins.getPlugin(LibraryPlugin)
            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", plugin.project.android.bootClasspath.join(
                        File.pathSeparator)]

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler)

                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
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

