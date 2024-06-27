package br.com.sortech.app.dao;

import br.com.sortech.app.model.Devedor;

import java.sql.SQLException;


public interface DevedorDAO {
	
	public int save(Devedor entity, String CNPJEmpresa) throws SQLException;
	public void closseConnDispositivo() throws Throwable;
	
	
}