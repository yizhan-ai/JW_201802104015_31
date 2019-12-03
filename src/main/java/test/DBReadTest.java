
package test;
import java.sql.*;

public class DBReadTest {
    public static void main(String[] args) throws SQLException {
        //服务时区为中国上海
        String url = "jdbc:mysql://localhost:3306/bysj?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        //用户名
        String username = "root";
        //密码
        String password = "Yk001020";
        //获得连接对象
        Connection connection = DriverManager.getConnection(url,username,password);
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //执行SQL查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from Degree");
        //若结果存在下一条，执行循环体
        while (resultSet.next()){
            //打印结果集中记录的id字段
            System.out.print(resultSet.getInt("id"));
            System.out.print(",");
            //打印结果集中记录的no字段
            System.out.print(resultSet.getString("no"));
            System.out.print(",");
            //打印结果集中记录的description字段
            System.out.print(resultSet.getString("description"));
            System.out.print(",");
            //打印结果集中记录的remarks字段
            System.out.print(resultSet.getString("remarks"));
        }
    }
}