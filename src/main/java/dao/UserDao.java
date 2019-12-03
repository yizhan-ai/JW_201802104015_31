package dao;

import domain.User;
import service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}


	public void changePassword(User user,String newPassword) throws SQLException {
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = "UPDATE user SET password = ? where id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,newPassword);
		//为预编译参数赋值
		pstmt.setInt(2,user.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}


	public User login(String username,String password) throws SQLException {
		//声明一个User类型的变量
		User user = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String login_sql = "SELECT * FROM user where username = ? AND password = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(login_sql);
		//为预编译参数赋值
		pstmt.setString(1,username);
		//为预编译参数赋值
		pstmt.setString(2,password);
		//执行预编译语句
		ResultSet resultSet = pstmt.executeQuery();
		if (resultSet.next()){
			user = UserService.getInstance().find(resultSet.getInt("id"));

		}

//		if (resultSet.next()){
//			user = new User(
//					resultSet.getInt("id"),
//					resultSet.getString("username"),
//					resultSet.getString("password"),
//					resultSet.getDate("password"),
//					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
//			);
//		}
		return user;
	}

	//返回结果集对象
	public Collection<User> findAll() throws SQLException {
		Collection<User> users = new TreeSet<User>();
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement stmt = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = stmt.executeQuery("select * from user");
		//若结果存在下一条，执行循环体
		while(resultSet.next()){
			User user = new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);

			users.add(user);
		}
		return users;
	}


	public User find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		User user = new User(
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("loginTime"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		return user;
	}

	public User findByUsername(String username) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user WHERE username = ?";
		PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
		pstmt.setString(1,username);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		User user = new User(
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("loginTime"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		return user;
	}


	public void update(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "UPDATE user SET username = ?,password = ?,loginTime = ?,teacher_id = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setString(1,user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		pstmt.setInt(5,user.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}

	public void add(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
				" (?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);

		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());

		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");

		JdbcHelper.close(pstmt,connection);
	}

	public void add(Connection connection,User user) throws SQLException {
		String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
				" (?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);

		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());

		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");

		pstmt.close();
	}



	public void delete(Integer id) throws SQLException {
		User user = this.find(id);
		this.delete(user);
	}

	public void delete(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteUser_sql = "DELETE FROM user WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
		pstmt.setInt(1,user.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 " + affectedRowNum +" 行记录");

		JdbcHelper.close(pstmt,connection);
	}



	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}


}
