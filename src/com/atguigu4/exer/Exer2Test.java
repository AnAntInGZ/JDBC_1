package com.atguigu4.exer;

import com.atguigu2.util.com.atguigu2.JDBCUtils;
import com.atguigu3.bean.Customer;
import com.atguigu3.bean.Order;
import com.atguigu3.bean.examstudent;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

public class Exer2Test {


    @Test
    public void InsertIntoStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--------四六级英语考生信息录入--------");
        System.out.print("Type: ");
        String type = scanner.next();
        System.out.print("IDCard: ");
        String idcard = scanner.next();
        System.out.print("ExamCard: ");
        String examcard = scanner.next();
        System.out.print("StudentName: ");
        String studentname = scanner.next();
        System.out.print("Location: ");
        String location = scanner.next();
        System.out.print("Grade: ");
        String grade = scanner.next();
        int typeint = Integer.parseInt(type);
        int gradeint = Integer.parseInt(grade);
        String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";
        int insertcount = update(sql, typeint, idcard, examcard, studentname, location, gradeint);
        if (insertcount > 0)
            System.out.println("录入成功");
        else System.out.println("录入失败");
    }

    public int update(String sql, Object... args) {
        java.sql.Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符,sql中占位符的个数与可变形参的长度相同！
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //ps.execute();//查询操作是true，增删改操作为false
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }


    @Test
    public void findstudent() {
        System.out.println("请输入您要输入的类型：");
        System.out.println("a:准考证号");
        System.out.println("b:身份证号");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        if (choice.equals("a")) {
            afindstudent();
        } else if (choice.equals("b")) {
            bfindstudent();
        } else System.out.println("您的输入有误，请重新进入系统");
    }
    @Test
    public void afindstudent() {
        System.out.println("请输入准考证号：");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.next();
        String sql = "select FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where ExamCard = ?";
        examstudent student = queryForexamstudent(sql,id);
        if(student==null){
            System.out.println("查无此人！请重新进入程序");
        }else{
            System.out.println("==========查询结果==========");
            System.out.println("流水号：    "+student.getFlowID());
            System.out.println("四级/六级："+student.getType());
            System.out.println("身份证号："+student.getIDCard());
            System.out.println("准考证号："+student.getExamCard());
            System.out.println("学生姓名："+student.getStudentName());
            System.out.println("区域：     "+student.getLocation());
            System.out.println("成绩： "+student.getGrade());
        }
    }

    public void bfindstudent() {
        System.out.println("请输入身份证号：");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.next();
        String sql = "select FlowID,Type,IDCard,ExamCard,StudentName,Location,Grade from examstudent where IDCard = ?";
        examstudent student = queryForexamstudent(sql,id);
        if(student==null){
            System.out.println("查无此人！请重新进入程序");
        }else {
            System.out.println("==========查询结果==========");
            System.out.println("流水号：  " + student.getFlowID());
            System.out.println("四级/六级：" + student.getType());
            System.out.println("身份证号：" + student.getIDCard());
            System.out.println("准考证号：" + student.getExamCard());
            System.out.println("学生姓名：" + student.getStudentName());
            System.out.println("区域：  " + student.getLocation());
            System.out.println("成绩： " + student.getGrade());
        }
    }


    //针对于Coustomers表的通用操作
    public examstudent queryForexamstudent(String sql, Object... args) {
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
                examstudent student = new examstudent();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给cust对象指定的columnName属性赋值为value，通过反射
                    Field field = examstudent.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(student, columnValue);
                }
                return student;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;



    }
    @Test
    public void deletestudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入学生的考号：");
        String id= scanner.next();
        String sql = "delete from examstudent where ExamCard = ?";
        if(update(sql,id)>0){
            System.out.println("删除成功！");
        }else{
            System.out.println("查无此人，请重新输入！");
        }
    }
}


