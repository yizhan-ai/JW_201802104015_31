
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDeleteTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        //url为数据库连接字串
        //jdbc为协议，mysql为子协议
        //localhost:3306为数据库服务器的地址和端口
        //?后面指定编码为UTF-8
        //服务时区为中国上海
        String url = "jdbc:mysql://localhost:3306/bysj?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        //用户名
        String username = "root";
        //密码
        String password = "Yk001020";
        //获得连接对象
        Connection connection = DriverManager.getConnection(url,username,password);
        //创建sql语句
        String sql = "DELETE FROM Degree WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(sql);
        //为预编译参数赋值
        pstmt.setInt(1, 4);
        //执行预编译对象的executeUpdate方法,获取删除的记录行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了" + affectedRowNum + "条记录");
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象
        connection.close();
    }
}

