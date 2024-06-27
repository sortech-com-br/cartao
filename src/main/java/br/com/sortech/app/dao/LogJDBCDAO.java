package br.com.sortech.app.dao;

import java.sql.SQLException;
import java.sql.Clob;
import java.sql.PreparedStatement;

import br.com.sortech.app.service.App;
import br.com.sortech.app.model.Log;
import br.com.sortech.componentes.conn.DBConnection;

public class LogJDBCDAO implements LogDAO {

	private DBConnection conn = new DBConnection(0);

	public LogJDBCDAO() {};
	@Override		
	public void closseConnDispositivo() throws Throwable
	{
		if (conn != null) {
			conn.closeConn();
		}

		super.finalize();

	}

	@Override		
	public void save(Log entity)  throws SQLException {


		PreparedStatement insertLog = null;
		
		try
		{
			

			String insert_sql = "INSERT INTO CB8COREADM.APP_CARTAO_LOG " +
					"(CPFCNPJ, " +
					"Request, " +
					"Response, " +
					"Acao, Token,Ambiente,DataInclusao) " +
					"VALUES " +
					"(?,?,?,?,?,?,sysdate)";
			
			
			
			insertLog = conn.prepareSTMInsert(insert_sql,"IDLOG");	

			insertLog.setString(1, entity.getCPFCNPJ());


			if (entity.getRequest().length()!= 0)
			{
				Clob a = new javax.sql.rowset.serial.SerialClob(entity.getRequest().toString().toCharArray());

				insertLog.setCharacterStream(2, a.getCharacterStream(),a.length());

				a.free();
			} else
			{
				insertLog.setClob(2, (Clob) null);
			}
			if (entity.getResponse().length()!= 0)
			{
				Clob b = new javax.sql.rowset.serial.SerialClob(entity.getResponse().toString().toCharArray());

				insertLog.setCharacterStream(3, b.getCharacterStream(),b.length());

				b.free();
			}
			else
			{
				insertLog.setClob(3,(java.sql.Clob) null );
			}
			

			insertLog.setString (4, entity.getAcao());

			insertLog.setString (5, entity.getToken());
			
			if (App.AMBIENTE.equalsIgnoreCase("PRODUCAO")) {
				insertLog.setString(6, "P");}
			else {
				insertLog.setString(6, "T");
			}	
			


			conn.executeInsert(insertLog);

			conn.commitConn();


		} catch (SQLException e) {
			App.logger.error("Erro no metodo incluir log:"+ e.getMessage());
		
			if (conn != null) {
		            try {
		            	App.logger.error("Erro na transação incluir log, fazendo rollback.");
		                conn.rollbacktConn();
		            } catch(SQLException excep) {
		           

		            	throw new DAOException("Operação não realizada com sucesso. Metodo incluir log.", e);
		            }
		        }
			else

		throw new DAOException("Operação não realizada com sucesso. Metodo incluir log.", e);	


		} 
	 	 finally {
	        if (insertLog != null) {
	        	insertLog.close();
	        }
	 }

	};

}
