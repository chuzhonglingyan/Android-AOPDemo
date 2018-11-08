package com.yuntian.plugin.javassist

import javassist.ClassPool
import javassist.CtMethod

class Utils {


    /**
     * 事先载入相关类
     * @param pool
     */
    static void importBaseClass(ClassPool pool) {
        pool.importPackage(LogTimeHelper.LogTimeAnnotation);
    }

    /**
     * 获取方法名
     * @param ctmethod
     * @return
     */
    static String getSimpleName(CtMethod ctmethod) {
        def methodName = ctmethod.getName();
        return methodName.substring(
                methodName.lastIndexOf('.') + 1, methodName.length());
    }

    /**
     * 获取类名
     * @param index
     * @param filePath
     * @return
     */
    static String getClassName(int index, String filePath) {
        int end = filePath.length() - 6 // .class = 6
        return filePath.substring(index, end).replace('\\', '.').replace('/', '.')
    }
}