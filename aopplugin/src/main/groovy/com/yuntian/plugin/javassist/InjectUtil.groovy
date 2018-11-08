package com.yuntian.plugin.javassist

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.JarClassPath
import org.gradle.api.Project

import java.lang.annotation.Annotation

class InjectUtil {

    static def classPathList = new ArrayList<JarClassPath>()

    static void removeClassPath(Project project) {
        if (classPathList != null) {
            def pool = ClassPool.getDefault()
            classPathList.each {
                try {

                    pool.removeClassPath(it)
                } catch (Exception e) {
                    project.logger.error(e.getMessage())
                }
            }
            classPathList.clear()
        }
    }

    /**
     * 注入jar
     * @param path
     * @param packageName
     * @param project
     */
    static void injectJar(String path, String packageName, Project project) {
        ClassPool pool = ClassPool.getDefault()
        def classPath = new JarClassPath(path)
        classPathList.add(classPath)
        pool.appendClassPath(classPath)

        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());
        Utils.importBaseClass(pool);
    }

    /**
     * 注入文件
     * @param path
     * @param packageName
     * @param project
     */
    static void injectDir(String path, String packageName, Project project) {
        ClassPool pool = ClassPool.getDefault()
        pool.appendClassPath(path)

        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());
        Utils.importBaseClass(pool);
        File dir = new File(path)
        if (!dir.isDirectory()) {
            return;
        }

        //遍历目录的文件
        dir.eachFileRecurse { File file ->
            String filePath = file.absolutePath//确保当前文件是class文件，并且不是系统自动生成的class文件
            if (filePath.endsWith(".class") && !filePath.contains('R$') && !filePath.contains('$')//代理类
                    && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")) {

                // 判断当前目录是否是在我们的应用包里面
                int index = filePath.indexOf(packageName);
                boolean isSelfPackage = index != -1;

                if (isSelfPackage) {
                    String className = Utils.getClassName(index, filePath);
                    CtClass ctClass = pool.getCtClass(className)

                    if (ctClass.isFrozen()) ctClass.defrost()

                    //getDeclaredMethods获取自己申明的方法，ctClass.getMethods()会把所有父类的方法都加上
                    for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {

                        String methodName = Utils.getSimpleName(ctMethod)//获取方法名
                        for (Annotation mAnnotation : ctMethod.getAnnotations()) { //遍历注解
                            if (mAnnotation.annotationType().canonicalName.equals(LogTimeHelper.LogTimeAnnotation)) {
                                project.logger.error " method:" + ctClass.getName() + " -" + ctMethod.getName()
                                LogTimeHelper.initLogTime(project,methodName,className,ctMethod,ctClass,path)
                            }
                        }
                    }

                    ctClass.detach()//用完一定记得要卸载，否则pool里的永远是旧的代码
                }
            }
        }
    }
}