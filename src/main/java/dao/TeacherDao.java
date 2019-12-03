
package dao;

import domain.Teacher;
import domain.User;
import service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class TeacherDao {
	private static TeacherDao teacherDao=
			new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	//返回结果集对象
	public Collection<Teacher> findAll(){
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		try{
			//获得数据库连接对象
			Connection connection = JdbcHelper.getConn();
			//在该连接上创建语句盒子对象
			Statement stmt = connection.createStatement();
			//执行SQL查询语句并获得结果集对象
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM Teacher");
			//若结果存在下一条，执行循环体
			while (resultSet.next()) {
				//打印结果集中记录的id字段
				System.out.print(resultSet.getInt("id"));
				System.out.print(",");
				//打印结果集中记录的name字段
				System.out.print(resultSet.getString("no"));
				System.out.print(",");
				//打印结果集中记录的name字段
				System.out.print(resultSet.getString("name"));
				System.out.print(",");
				//打印结果集中记录的profTitle字段
				System.out.print(resultSet.getString("profTitle_id"));
				System.out.print(",");
				//打印结果集中记录的degree字段
				System.out.print(resultSet.getString("degree_id"));
				System.out.print(",");
				//打印结果集中记录的department字段
				System.out.print(resultSet.getString("department_id"));
				//根据数据库中的数据,创建Teacher类型的对象
				Teacher teacher = new Teacher(resultSet.getInt("id"),
						resultSet.getString("no"),
						resultSet.getString("name"),
						ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
						DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
						DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
				//添加到集合teachers中
				teachers.add(teacher);
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return teachers;
	}
	public Teacher find(Integer id) throws SQLException{
		//声明一个Teacher类型的变量
		Teacher teacher = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}
//
public void add(Teacher teacher) throws SQLException {
	Connection connection = null;
	PreparedStatement pstmt = null;
	try {

		connection = JdbcHelper.getConn();
		connection.setAutoCommit(false);
		String addTeacher_sql = "INSERT INTO teacher(name,no,proftitle_id,degree_id,department_id) VALUES" +
				" (?,?,?,?,?)";
		pstmt = connection.prepareStatement(addTeacher_sql,Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, teacher.getName());
		pstmt.setString(2, teacher.getNo());
		pstmt.setInt(3,teacher.getTitle().getId());
		pstmt.setInt(4,teacher.getDegree().getId());
		pstmt.setInt(5,teacher.getDepartment().getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");

		ResultSet resultSet = pstmt.getGeneratedKeys();
		resultSet.next();
		int teacherId = resultSet.getInt(1);
		teacher.setId(teacherId);

		java.util.Date date_util = new java.util.Date();
		Long date_long = date_util.getTime();
		Date date_sql = new Date(date_long);

		User user = new User(
				teacher.getNo(),
				teacher.getNo(),
				date_sql,
				teacher
		);
		UserService.getInstance().add(connection,user);


//			String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
//					" (?,?,?,?)";
//			pstmt = connection.prepareStatement(addUser_sql);
//			pstmt.setString(1,teacher.getNo());
//			pstmt.setString(2,teacher.getNo());
//			pstmt.setDate(3,date_sql);
//			pstmt.setInt(4,teacherId);
//			int affectedRowNum1 = pstmt.executeUpdate();
//			System.out.println("添加了 " + affectedRowNum1 +" 行记录");


	} catch (SQLException e) {
		System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
		try {
			if (connection != null){
				connection.rollback();
			}
		} catch (SQLException e1){
			e.printStackTrace();
		}

	} finally {
		try {
			if (connection != null){
				//恢复自动提交
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//关闭资源
		JdbcHelper.close(pstmt,connection);
	}

}

	public void delete(Integer id) throws SQLException {
		Teacher teacher = this.find(id);
		this.delete(teacher);
	}

	public void delete(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {

			connection = JdbcHelper.getConn();
			String deleteUser_sql = "DELETE FROM user WHERE teacher_id = ?";
			pstmt = connection.prepareStatement(deleteUser_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum +" 行记录");

			String deleteTeacher_sql = "DELETE FROM teacher WHERE id = ?";
			pstmt = connection.prepareStatement(deleteTeacher_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum1 = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum1 +" 行记录");


		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}
	}

	//delete方法，根据teacher的id值，删除数据库中对应的degree对象

	public void update(Teacher teacher) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update teacher set no =?,name=?,proftitle_id=?,degree_id=?,department_id=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);

	}
	//创建main方法，查询数据库中的对象，并输出
	public static void main(String[] args) throws ClassNotFoundException,SQLException{
		//删
		//teacherDao.delete(2);
		//TeacherDao.getInstance().findAll();
		//查找id为1的teacher对象
		Teacher teacher1 = TeacherDao.getInstance().find(1);
		System.out.println(teacher1);
		//修改teacher1对象的description字段值
		teacher1.setName("李四");
		//修改数据库中的对应记录
		TeacherDao.getInstance().update(teacher1);
		//查找id为1的teacher对象
		Teacher teacher2 = TeacherDao.getInstance().find(1);
		//打印修改后的description字段的值
		System.out.println(teacher2.getName());
		Teacher teacher = new Teacher("张三");
		//System.out.println(TeacherService.getInstance().add(teacher));
	}
}