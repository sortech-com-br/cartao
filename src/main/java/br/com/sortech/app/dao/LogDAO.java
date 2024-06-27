package br.com.sortech.app.dao;


import java.sql.SQLException;

import br.com.sortech.app.model.Log;


public interface LogDAO {
	
	public void save(Log entity) throws SQLException;
	
	public void closseConnDispositivo() throws Throwable;

}
