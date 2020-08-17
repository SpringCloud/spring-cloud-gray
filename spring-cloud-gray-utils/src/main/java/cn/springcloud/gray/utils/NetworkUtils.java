package cn.springcloud.gray.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author saleson
 * @date 2020-08-16 03:39
 */
@Slf4j
public class NetworkUtils {


    public static String getLocalIp() {
        try {
            return findLocalIp();
        } catch (SocketException e) {
            log.error("无法获取到本机ip", e);
            return null;
        }
    }

    public static String findLocalIp() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = (InetAddress) addresses.nextElement();
                if (ip != null
                        && ip instanceof Inet4Address
                        && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        && ip.getHostAddress().indexOf(":") == -1) {
                    System.out.println("本机的IP = " + ip.getHostAddress());
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }
}
