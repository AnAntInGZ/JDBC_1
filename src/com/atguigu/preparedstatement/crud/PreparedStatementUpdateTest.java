package com.atguigu.preparedstatement.crud;

import com.atguigu.connection.Connection;
import com.atguigu2.util.com.atguigu2.JDBCUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Date;

import java.io.IOException;
import java.io.InputStream;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Properties;

/*
使用PreparedStatement来替换Statement，实现对数据表的crud操作

增删改：没有返回
查：有返回
 */
public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate(){
 //       String sql = "delete from customers where id = ?";
  //      update(sql,3);
        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql,"DD","2");

    }
    //通用的增删改操作

    public void update(String sql,Object ...args){
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
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn,ps);
        }

    }
    //修改costomers表的一条记录
    @Test
    public void testUpdate(){
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            //2.预编译sql语句，返回PreparedStatement实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3。填充占位符
            ps.setString(1,"莫扎特");
            ps.setInt(2,18);
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }

    }


    //向customers表中添加一条记录
    @Test
    public void testInsert() throws Exception {
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.读取配置文件中的4个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3.获取连接
            conn = DriverManager.getConnection(url,user,password);
            // System.out.println(conn);

            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            ps = conn.prepareStatement(sql);

            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");

            ps.setDate(3,  new Date(date.getTime()));

            //执行sql
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
