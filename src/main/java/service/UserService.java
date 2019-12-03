package service;


import dao.UserDao;
import domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();

	public UserService() {
	}

	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll() throws SQLException {
		return userDao.findAll();
	}

	public User find(Integer id) throws SQLException {
		return userDao.find(id);
	}
	public User findByUsername(String username) throws SQLException {
		return userDao.findByUsername(username);
	}


	public void changePassword(User user,String newPassword) throws SQLException {
		userDao.changePassword(user,newPassword);
	}

	public void update(User user) throws SQLException {
		userDao.update(user);
	}

	public void add(User user) throws SQLException {
		userDao.add(user);
	}
	public void add(Connection collection, User user) throws SQLException {
		userDao.add(collection,user);
	}

	public void delete(Integer id) throws SQLException {
		userDao.delete(id);
	}

	public void delete(User user) throws SQLException {
		userDao.delete(user);
	}


	public User login(String username, String password) throws SQLException {
		return userDao.login(username,password);
	}
}
