package br.com.sortech.app.model;

import java.util.List;

import br.com.sortech.app.dao.AcaoWppJDBCDAO;



public class ManterAcao {
	
	
	public ManterAcao() {};

	public void atualizarActionStatus(List<AcaoWpp> updates)  throws Throwable {
		
		AcaoWppJDBCDAO dao = new AcaoWppJDBCDAO();

	    dao.atualizarActionStatus(updates);
	    
	    dao.atualizarStatusId(updates);

	    dao.closeConnAcao();;
		
	}
	
	public List<AcaoWpp> verificarAcoesPendentes()  throws Throwable {

		AcaoWppJDBCDAO dao = new AcaoWppJDBCDAO();

		List<AcaoWpp> acoesPendentes = dao.verificarAcoesPendentes();
	    
	    dao.closeConnAcao();;
	    
	    return acoesPendentes;
		
	}
}
