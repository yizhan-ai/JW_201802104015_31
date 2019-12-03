package service;

import dao.ProfTitleDao;
import domain.ProfTitle;

import java.sql.SQLException;
import java.util.Collection;

public final class ProfTitleService {
	private static ProfTitleDao profTitleDao= ProfTitleDao.getInstance();
	private static ProfTitleService profTitleService=new ProfTitleService();
	private ProfTitleService(){}
	public static ProfTitleService getInstance(){
		return profTitleService;
	}
	public Collection<ProfTitle> findAll(){
		return profTitleDao.findAll();
	}
	public Collection<ProfTitle> getAll(){
		return profTitleDao.findAll();
	}

	public ProfTitle find(Integer id)throws SQLException,ClassNotFoundException{
		return profTitleDao.find(id);
	}

	public boolean update(ProfTitle profTitle)throws SQLException,ClassNotFoundException{
		return profTitleDao.update(profTitle);
	}

	public boolean add(ProfTitle profTitle)throws SQLException,ClassNotFoundException{
		return profTitleDao.add(profTitle);
	}

	public boolean delete(Integer id)throws SQLException,ClassNotFoundException {
		return profTitleDao.delete(id);
	}
}

