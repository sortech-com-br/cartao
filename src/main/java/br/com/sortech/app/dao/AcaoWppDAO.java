package br.com.sortech.app.dao;

import java.sql.SQLException;
import java.util.List;
import br.com.sortech.app.model.AcaoWpp;

public interface AcaoWppDAO {
    void closeConnDispositivo() throws Throwable;
    List<AcaoWpp> verificarAcoesPendentes() throws SQLException;
    void atualizarActionStatus(List<AcaoWpp> updates) throws SQLException;
	void atualizarStatusId(List<AcaoWpp> updates)throws SQLException;
}
