package com.atguigu.preparedstatement.crud;
import com.atguigu2.util.com.atguigu2.JDBCUtils;
import com.atguigu3.bean.Order;
import org.junit.Test;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
针对order表的通用的查询操作
 */
public class OrderForQuery {
    /*
    针对于表的字段表与类的属性名不相同的情况：
        1.必须声明sql时，使用类的属性名来命名字段的别名
        2.使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName()获取列的别名
     */
    @Test
    public void testOrderForQuery(){
        String sql="select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = orderForQuery(sql,1);
        System.out.println(order);
    }
    public Order orderForQuery(String sql,Object...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if(rs.next()){
                Order order = new Order();
                for(int i=0;i<columnCount;i++){
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i+1);
                    //获取每个列的列值
                    //获取列的别名：getColumnLabel(),getColumnName()不推荐使用
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);

                }
                return order;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

        return null;
    }
    @Test
    public void testQuery1()  {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);

            rs = ps.executeQuery();
            if(rs.next()){
                int id =(int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date)rs.getObject(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }

    }
}
