package service;

import dao.TeacherDao;
import domain.Teacher;

import java.sql.SQLException;
import java.util.Collection;

public final class TeacherService {
	private static TeacherDao teacherDao= TeacherDao.getInstance();
	private static TeacherService teacherService=new TeacherService();
	private TeacherService(){}

	public static TeacherService getInstance(){
		return teacherService;
	}

	public Collection<Teacher> findAll() throws SQLException {
		return teacherDao.findAll();
	}

	public Teacher find(Integer id) throws SQLException {
		return teacherDao.find(id);
	}

	public void update(Teacher teacher) throws SQLException, ClassNotFoundException {
		teacherDao.update(teacher);
	}

	public void add(Teacher teacher) throws SQLException {
		teacherDao.add(teacher);
	}

	public void delete(Integer id) throws SQLException {
		Teacher teacher = this.find(id);
		teacherDao.delete(teacher);
	}

	public void delete(Teacher teacher) throws SQLException {

		teacherDao.delete(teacher);
	}
}
