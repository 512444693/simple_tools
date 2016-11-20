package com.zm.mix.rmi;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiClientTest {
    public static void main(String args[]) throws RemoteException, MalformedURLException, NotBoundException, SocketException {
        String url="rmi://192.168.202.81/exists";
        //String url="rmi://127.0.0.1/exists";
        Exists exists;
        exists = (Exists)Naming.lookup(url);

        System.out.println(exists.fileExist("/root/9099.cap"));
        System.out.println(exists.directoryExist("/root/9099.cap"));
        System.out.println(exists.directoryHasFiles("/root"));
        System.out.println(exists.listDir("/root/java"));
        }
}  