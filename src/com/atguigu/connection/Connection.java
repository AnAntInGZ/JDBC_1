package com.atguigu.connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection {
    @Test
    public void testConnection1() throws SQLException {

        Driver driver = new com.mysql.jdbc.Driver();

        //test为数据库名
        //jdbc:mysql:协议

        String url = "jdbc:mysql://localhost:3306/test";
        //将用户名和密码封装再Properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","xwj0922");

        java.sql.Connection conn =driver.connect(url,info);

        System.out.println(conn);
    }
    @Test
    public void testConnection2() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        //1.获取Driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";


        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","xwj0922");

        java.sql.Connection conn =driver.connect(url,info);

        System.out.println(conn);
    }
    //方式三：使用DriverManager
    @Test
    public void testConnection3() throws Exception{
        //1.获取Driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "xwj0922";

        java.sql.Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
    @Test
    //方式四：
    public void testConnection4() throws Exception{
        //1.获取Driver实现类对象，使用反射
        Class.forName("com.mysql.jdbc.Driver");
        //Driver driver = (Driver) clazz.newInstance();

        //DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "xwj0922";

        java.sql.Connection conn =DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }
    /*
    好处  1.实现了数据和代码的分离，实现了解耦
         2.如果需要修改配置文件信息，就可以避免程序重新打包
     */
    //最终版:将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    @Test
    public void getConnection5() throws Exception {
        //1.读取配置文件中的4个基本信息
        InputStream is = Connection.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        java.sql.Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);
    }

}
