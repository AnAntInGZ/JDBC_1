package com.atguigu.preparedstatement.crud;

/*
针对Customers表的查询操作
 */

import com.atguigu2.util.com.atguigu2.JDBCUtils;
import com.atguigu3.bean.Customer;
import org.junit.Test;

import java.beans.Customizer;
import java.lang.reflect.Field;
import java.sql.*;

public class CustomerForQuery {

    @Test
    public void testQueryForcustomers() {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);

        sql = "select name,email from customers where name = ?";
        Customer customer1 = queryForCustomers(sql, "周杰伦");
        System.out.println(customer1);
    }

    //针对于Coustomers表的通用操作
    public Customer queryForCustomers(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust对象指定的columnName属性赋值为value，通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;


}


    @Test
    public void testQuery1(){
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            //执行，并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            if (resultSet.next()){  //判断结果集的下一条是否有数据，有数据返回true，并指针下移

                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式一
                //System.out.println("id = " + id + ",name = " + name + ",email = " + email + ",birth = " + birth);
                //方式二
                //Object[] data = new Object[]{id,name,email,birth};
                //方式三:
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtils.closeResource(conn,ps,resultSet);
        }

    }

}