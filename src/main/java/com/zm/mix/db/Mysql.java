package com.zm.mix.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangmin on 2015/8/11.
 */
public class Mysql {

    public static void main(String[] args){
        //Mysql mysql = new Mysql("192.168.200.25", "dbuserinfo", "root", "");

        //String sql = "";

        /*
        for(int i=1; i<101; i++)
        {
            sql = "drop table t_service_paas_"+i;
            mysql.excute(sql);
            sql = "drop table t_service_saas_"+i;
            mysql.excute(sql);
        }
        */

/*
        for(int i=4; i<101; i++){
            sql = "create table t_service_live_"+i+" like t_service_live_1";
            mysql.execute(sql);
            sql = "create table t_service_reallive_"+i+" like t_service_reallive_1";
            mysql.execute(sql);
            System.out.println(i);
        }*/
    }

    public boolean execute(String sql){
        boolean ret = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            stmt.execute(sql);
            ret = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    public Mysql(String ip, String database, String user, String password){
        this.ip = ip;
        this.database = database;
        this.user = user;
        this.password = password;
        this.url = "jdbc:mysql://"+ip+":3306/"+database;
    }

    private String ip = "";
    private String database = "";
    private String user = "";
    private String password = "";

    private String url = "";
    private Connection conn = null;
    private Statement stmt = null;
}
