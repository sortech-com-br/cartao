package br.com.sortech.app.dao;

import br.com.sortech.app.model.DevedorWpp;
import java.sql.SQLException;

public interface AcaoWppDAO {

    public int save(DevedorWpp entity) throws SQLException;
    public void closseConnDispositivo() throws Throwable;

}