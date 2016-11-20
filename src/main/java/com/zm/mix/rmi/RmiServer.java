package com.zm.mix.rmi;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Enumeration;

public class RmiServer {
    /**
     * 启动 RMI 注册服务并进行对象注册
     */
    public static void main(String[] args)
    {
        try
        {
            //启动RMI注册服务，指定端口为1099　（1099为默认端口）
            //也可以通过命令 ＄java_home/bin/rmiregistry 1099启动
            //这里用这种方式避免了再打开一个DOS窗口
            //而且用命令rmiregistry启动注册服务还必须事先用RMIC生成一个占位程序(stub类)为它所用
            LocateRegistry.createRegistry(1099);

            //创建远程对象的一个或多个实例，下面是hello对象
            //可以用不同名字注册不同的实例
            Exists exists = new ExistsImpl();

            InetAddress inetAddress = getAddress();
            if(inetAddress == null)
                throw new IllegalStateException("获取IPv4地址失败");
            //把hello注册到RMI注册服务器上，命名为Hello
            Naming.rebind("rmi://" + inetAddress.getHostAddress() + ":1099/exists", exists);

            //如果要把hello实例注册到另一台启动了RMI注册服务的机器上
            //Naming.rebind("//192.168.1.105:1099/Hello",hello);

            System.out.println("Server is ready.");
        }
        catch (Exception e)
        {
            System.out.println("Server failed: " + e);
        }
    }

    /**
     * 根据网卡，获得IPv4地址
     */
    private static InetAddress getAddress() throws SocketException {
        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                continue;
            }
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ret = addresses.nextElement();
                if(ret instanceof Inet4Address)
                    return ret;
            }
        }
        return null;
    }
}  