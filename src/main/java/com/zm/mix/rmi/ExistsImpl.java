package com.zm.mix.rmi;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by zhangmin on 2016/4/12.
 */
public class ExistsImpl extends UnicastRemoteObject implements Exists {
    protected ExistsImpl() throws RemoteException {
    }

    @Override
    public boolean fileExist(String filePath) throws RemoteException {
        File file = new File(filePath);
        if(file.isDirectory())
            return false;
        return file.exists();
    }

    @Override
    public boolean directoryExist(String filePath) throws RemoteException {
        File file = new File(filePath);
        if(!file.isDirectory())
            return false;
        return file.exists();
    }

    @Override
    public boolean directoryHasFiles(String filePath) throws RemoteException {
        boolean result = false;
        File file = new File(filePath);
        if(file.isDirectory()){
            File[]  files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                if(files[i].isFile() && files[i].exists()){
                    result = true;
                    break;
                }
                if(files[i].isDirectory())
                    result = directoryHasFiles(files[i].getPath());
            }
        }
        return result;
    }

    @Override
    public String listDir(String filePath) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        list(0, filePath, sb);
        return sb.toString();
    }

    private void list(int level, String filePath, StringBuilder sb) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            printFileName(level, file.getName() + "(目录)", sb);
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++) {
                if(files[i].isDirectory()) {
                    list(level + 1, files[i].getPath(), sb);
                }else{
                    printFileName(level + 1, files[i].getName(), sb);
                }
            }
        } else if(file.isFile())
            printFileName(level, file.getName(), sb);
    }

    private void printFileName(int level, String fileName, StringBuilder sb) {
        for(int i = 0; i < level; i++)
            sb.append('\t');
        sb.append("└── " + fileName + "\r\n");
    }
}
