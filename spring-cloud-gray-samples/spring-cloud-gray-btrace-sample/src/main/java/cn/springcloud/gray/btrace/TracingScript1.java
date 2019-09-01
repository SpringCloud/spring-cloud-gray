package cn.springcloud.gray.btrace;

/* BTrace Script Template */

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;

import java.util.List;

import static com.sun.btrace.BTraceUtils.*;

@BTrace(unsafe = true)
public class TracingScript1 {
    /* put your code here */



    @OnMethod(clazz = "+cn.springcloud.gray.GrayManager", method = "hasGray", location = @Location(Kind.RETURN))
    public static void hasGray(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, String serviceId, @Self Object self, @Return boolean result) {
        println(clsName + "#" + methodName);
        print("args:");
        println(serviceId);
        print("result:");
        println(result);
        printlnEnd();
    }


    @OnMethod(clazz = "cn.springcloud.gray.utils.ConfigurationUtils", method = "bind", location = @Location(Kind.RETURN))
    public static void bindConfigurationUtils(AnyType[] args) {
        println("cn.springcloud.gray.utils.ConfigurationUtils#bind");
        printFields(args[0]);
        println(args[1]);
        printlnEnd();
    }



    @OnMethod(clazz = "+cn.springcloud.gray.servernode.ServerListProcessor", method = "process", location = @Location(Kind.RETURN))
    public static void processServerListProcessor(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, String serviceId, List servers, @Self Object self, @Return List result) {
        println(clsName + "#" + methodName);
        print("serviceId:");
        print(serviceId);
        print(", servers size:");
        print(size(servers));
        if (result == null) {
            println("result list is null");
            printlnEnd();
            return;
        }
        println(", result size:" + size(result));
        printlnEnd();
    }


    @OnMethod(clazz = "+cn.springcloud.gray.client.netflix.eureka.EurekaServerListProcessor", method = "getUnUpServers", location = @Location(Kind.RETURN))
    public static void getUnUpServersEurekaServerListProcessor(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, String serviceId, @Self Object self, @Return List result) {
        println(clsName + "#" + methodName);
        print("serviceId:");
        print(serviceId);
        if (result == null) {
            println("result list is null");
            printlnEnd();
            return;
        }
        println(", result size:" + size(result));
        if(size(result)>0){
            print("reslut[0] is ");
            printFields(result.get(0));
        }
        printlnEnd();
    }

    @OnMethod(clazz = "+cn.springcloud.gray.GrayManager", method = "getGrayDecision", location = @Location(Kind.RETURN))
    public static void getGrayDecision(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, String serviceId, String instanceId, @Self Object self, @Return List<Object> list) {
        println(clsName + "#" + methodName);
        print("self field is:");
        printFields(self);
            print("serviceId:");
            print(serviceId);
            print(", instanceId:");
            print(instanceId);
        if (list == null) {
            println("result list is null");
            printlnEnd();
            return;
        }
        println(", size:" + size(list));
        if (compare(instanceId, "192.168.60.236:service-a:20104")) {
            if(size(list)>0){
                println(str(list));
            }
        }
        printlnEnd();
    }

    @OnMethod(clazz = "+cn.springcloud.gray.decision.GrayDecisionInputArgs", method = "<init>")
    public static void initGrayDecisionInputArgs(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, AnyType[] args) {
        println(clsName + "#" + methodName);
        if (args[0] != null) {
            printFields(args[0]);
        }
        if (args[1] != null) {
            printFields(args[1]);
        }
        printlnEnd();
    }

    @OnMethod(clazz = "+cn.springcloud.gray.client.netflix.ribbon.GrayDecisionPredicate", method = "apply", location = @Location(Kind.RETURN))
    public static void applyGrayDecisionPredicate(
            @ProbeClassName String clsName, @ProbeMethodName String methodName, Object input, @Return boolean result) {
        println(clsName + "#" + methodName);
        printFields(input);
        println("result:" + result);
        printlnEnd();
    }

    @OnMethod(clazz = "cn.springcloud.gray.client.netflix.ribbon.GrayLoadBalanceRule", method = "choose", location = @Location(Kind.RETURN))
    public static void choose(@Return Object result) {
        println("cn.springcloud.gray.client.netflix.ribbon.GrayLoadBalanceRule#choose");
        if (result != null) {
            println("choose result:" + result);
        } else {
            println("choose result is null");
        }
        printlnEnd();
    }



    private static void printlnEnd() {
        println("\n\n");
    }


}