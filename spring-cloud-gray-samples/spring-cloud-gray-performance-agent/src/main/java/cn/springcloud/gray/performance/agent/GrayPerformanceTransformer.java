package cn.springcloud.gray.performance.agent;

import javassist.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class GrayPerformanceTransformer implements ClassFileTransformer {

    private static final Logger log = LoggerFactory.getLogger(GrayPerformanceTransformer.class);

    public static final String PACKAGE_NAME = "cn/springcloud/gray";


    private String[] packageNames;

    public GrayPerformanceTransformer(String packageNames) {
        List<String> names = new ArrayList<>();
        if (StringUtils.isNotEmpty(packageNames)) {
            String[] ns = packageNames.split(",");
            for (String n : ns) {
                names.add(n);
            }
        }
        if (!names.contains(PACKAGE_NAME)) {
            names.add(PACKAGE_NAME);
        }
        this.packageNames = names.toArray(new String[names.size()]);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        System.out.println("-- " + className);
        if (!isPrefixPackage(className)) {
            return classfileBuffer;
        }

        try {
            ClassInfo classInfo = new ClassInfo(className, classfileBuffer, loader);
            CtClass ctClass = classInfo.getCtClass();
            if (ctClass.isInterface()) {
                return classfileBuffer;
            }
            System.out.println("loading " + className);
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            for (CtMethod ctMethod : ctMethods) {
                aroundRecordRunTime(classInfo, ctMethod);
            }
//            if (classInfo.isModified()) {
            return classInfo.getCtClass().toBytecode();
//            }
        } catch (Exception e) {
            log.error("发生异常", e);
            e.printStackTrace();
            return classfileBuffer;
        }
    }


    private void aroundRecordRunTime(ClassInfo classInfo, CtMethod ctMethod) {
        try {
//            insertBeforeCode(ctMethod);
//            insertAfterCode(ctMethod);

            String beforeCode = getBeforeCode();
            String finallyCode = getAfterCode(ctMethod);

            doTryFinallyForMethod(ctMethod, beforeCode, finallyCode);
        } catch (Exception e) {
            log.error("{} 添加代码失败", classInfo.getClassName(), e);
            e.printStackTrace();
        }

    }


    static String renamedMethodNameByTtl(CtMethod method) {
        return method.getName() + "$ori";
    }

    static void doTryFinallyForMethod(CtMethod method, String beforeCode, String finallyCode) throws CannotCompileException, NotFoundException {
        doTryFinallyForMethod(method, renamedMethodNameByTtl(method), beforeCode, finallyCode);
    }

    static void doTryFinallyForMethod(CtMethod method, String renamedMethodName, String beforeCode, String finallyCode) throws CannotCompileException, NotFoundException {
        final CtClass clazz = method.getDeclaringClass();
        String oriName = method.getName();
        method.setName(renamedMethodName);
        final CtMethod new_method = CtNewMethod.copy(method, oriName, clazz, null);
        // rename original method, and set to private method(avoid reflect out renamed method unexpectedly)

//        method.setModifiers(method.getModifiers()
//                & ~java.lang.reflect.Modifier.PUBLIC /* remove public */
//                & ~java.lang.reflect.Modifier.PROTECTED /* remove protected */
//                | Modifier.PRIVATE /* add private */);


        StringBuilder code = new StringBuilder();
        code.append("{\n")
                .append(beforeCode)
                .append("try {\n");
        CtClass returnType = new_method.getReturnType();
        if (returnType != null && returnType != CtClass.voidType) {
            code.append("    return ");
        }

        code.append(renamedMethodName);
        CtClass[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            code.append("();\n");
        } else {
            code.append("($$);\n");
        }

        code.append("} finally {\n")
                .append(finallyCode)
                .append("\n")
                .append("} }");
        // set new method implementation
        new_method.setBody(code.toString());
        clazz.addMethod(new_method);
    }

    private void insertBeforeCode(CtMethod ctMethod) throws CannotCompileException {
        ctMethod.insertBefore(getBeforeCode() + "try{");
    }

    private void insertAfterCode(CtMethod ctMethod) throws CannotCompileException {
        ctMethod.insertAfter("}finally{" + getAfterCode(ctMethod) + "}");
    }


    private String getBeforeCode() {
        StringBuilder str = new StringBuilder();
        str.append("long d$start = System.currentTimeMillis();");
        return str.toString();
    }

    private String getAfterCode(CtMethod ctMethod) {
        String longName = ctMethod.getLongName();
        StringBuilder str = new StringBuilder();
        str.append("long d$ut = System.currentTimeMillis() - d$start;\n")
                .append("cn.springcloud.gray.performance.PerformanceLogger.printMethodUsedTime(\"")
                .append(longName)
                .append("\", d$ut);\n");
        return str.toString();
    }

    private boolean isPrefixPackage(String className) {
        for (String p : packageNames) {
            if (StringUtils.startsWith(className, p)) {
                return true;
            }
        }
        return false;
    }
}
