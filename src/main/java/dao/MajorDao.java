
package dao;

import domain.Department;
import domain.Major;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class MajorDao {
	private static MajorDao majorDao=
			new MajorDao();
	private MajorDao(){}
	public static MajorDao getInstance(){
		return majorDao;
	}
	//返回结果集对象
	public Collection<Major> findAll(){
		Collection<Major> majors = new TreeSet<Major>();
		try{
			//获得数据库连接对象
			Connection connection = JdbcHelper.getConn();
			//在该连接上创建语句盒子对象
			Statement stmt = connection.createStatement();
			//执行SQL查询语句并获得结果集对象
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM Major");
			//若结果存在下一条，执行循环体
			while (resultSet.next()) {
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
				System.out.print(",");
				//打印结果集中记录的department_id字段
				System.out.print(DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
				//根据数据库中的数据,创建Major类型的对象
				Major major = new Major(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getString("no"), resultSet.getString("remarks"), DepartmentDao.getInstance().find(resultSet.getInt("departemnt_id")));
				//添加到集合majors中
				majors.add(major);
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return majors;
	}
	public Major find(Integer id) throws SQLException{
		//声明一个Major类型的变量
		Major major = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteMajor_sql = "SELECT * FROM major WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteMajor_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Major对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			major = new Major(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"), DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return major;
	}
	public boolean add(Major major) throws SQLException,ClassNotFoundException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String addDegree_sql = "INSERT INTO major (description,no,remarks) VALUES"+" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,major.getDescription());
		preparedStatement.setString(2,major.getNo());
		preparedStatement.setString(3,major.getRemarks());
		//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
		int affectedRowNum=preparedStatement.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
	//delete方法，根据major的id值，删除数据库中对应的degree对象
	public boolean delete(int id) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteMajor_sql = "DELETE FROM major WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteMajor_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	public boolean update(Major major) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update degree set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,major.getDescription());
		preparedStatement.setString(2,major.getNo());
		preparedStatement.setString(3,major.getRemarks());
		preparedStatement.setInt(4,major.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	//创建main方法，查询数据库中的对象，并输出
	public static void main(String[] args) throws ClassNotFoundException,SQLException{
		Department department = DepartmentDao.getInstance().find(1);
		//删
		//majorDao.delete(2);
		//MajorDao.getInstance().findAll();
		//查找id为1的major对象
		Major major1 = MajorDao.getInstance().find(1);
		System.out.println(major1);
		//修改major对象的description字段值
		major1.setDescription("高数");
		//修改数据库中的对应记录
		MajorDao.getInstance().update(major1);
		//查找id为1的major对象
		Major major2 = MajorDao.getInstance().find(1);
		//打印修改后的description字段的值
		System.out.println(major2.getDescription());
		Major major = new Major(5,"博士","05","",department);
		System.out.println(MajorDao.getInstance().add(major));
	}
}
