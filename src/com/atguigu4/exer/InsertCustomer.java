package com.atguigu4.exer;
import com.atguigu2.util.com.atguigu2.JDBCUtils;
import com.atguigu3.bean.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class InsertCustomer {

    @Test
    public  void InsertInto(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入邮箱：");
        String email = scanner.next();
        System.out.print("请输入生日：");
        String birth = scanner.next();//'1992-09-08'
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int insertcount = update(sql, name,email, birth);
        if(insertcount>0){
            System.out.println("添加成功");
        }else  System.out.println("添加失败");

    }
    public int update(String sql,Object ...args){
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符,sql中占位符的个数与可变形参的长度相同！
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1,args[i]);
            }
            //ps.execute();//查询操作是true，增删改操作为false
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }

}
