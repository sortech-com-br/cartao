package br.com.sortech.app.dao;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import br.com.sortech.app.service.App;
import br.com.sortech.app.model.Devedor;
import br.com.sortech.componentes.conn.DBConnection;

public class DevedorJDBCDAO implements DevedorDAO  {

	private DBConnection conn = new DBConnection(0);

	public DevedorJDBCDAO() {}


	@Override		
	public void closseConnDispositivo() throws Throwable
	{
		if (conn != null) {
			conn.closeConn();
		}

		super.finalize();

	}


	@Override	
	public int save(Devedor entity, String CNPJEmpresa) throws SQLException {

		PreparedStatement pstCredencial = null;
		int result = 0;
		try {

			String insert_sql = "INSERT INTO APP_CARTAO_DEVEDOR " + 
					"(" + 
					"CPFCNPJ," + 
					"Nome," + 
					"Email," +
					"Telefone,"+
					"DataInclusao," + 
					"CNPJEmpresa," + 
					"DataAlteracao, Ambiente)" + 
					" VALUES " + 
					"(?,?,?,?,SYSDATE ,?,SYSDATE,?)";

			String update_sql = "UPDATE APP_CARTAO_DEVEDOR "+
					"SET "+
					"Nome = ?, "+
					"Email = ?, "+
					"Telefone = ?,"+
					"DataAlteracao = Sysdate "+
					"WHERE CPFCNPJ = ? and CNPJEmpresa = ? and Ambiente=?" ;



			if (!this.ExisteCPFCNPJ(entity.getCPFCNPJ(),CNPJEmpresa)) {

				pstCredencial = conn.prepareSTM(insert_sql);
				pstCredencial.setString(1, entity.getCPFCNPJ());
				pstCredencial.setString(2, entity.getNome());
				pstCredencial.setString(3, entity.getEmail());
				pstCredencial.setString(4, entity.getTelefone());
				pstCredencial.setString(5, CNPJEmpresa);
				if (App.AMBIENTE=="PRODUCAO") {
					pstCredencial.setString(6, "P");}
				else {
					pstCredencial.setString(6, "T");
				}	

			} 
			else 
			{

				pstCredencial = conn.prepareSTM(update_sql);
				pstCredencial.setString(1, entity.getNome());
				pstCredencial.setString(2, entity.getEmail());
				pstCredencial.setString(3, entity.getTelefone());	
				pstCredencial.setString(4, CNPJEmpresa);
				pstCredencial.setString(5, entity.getCPFCNPJ());
				pstCredencial.setString(6, App.AMBIENTE);
		
			}

			pstCredencial.setString(1, entity.getCPFCNPJ());



				result = conn.executeInsertUpdate(pstCredencial);

	

			conn.commitConn();

		} catch (SQLException e) {

			App.logger.error("Erro no metodo incluir log:"+ e.getMessage());

			if (conn != null) {
				try {
					App.logger.error("Erro na transação save devedor, fazendo rollback.");
					conn.rollbacktConn();
				} catch(SQLException excep) {


					throw new DAOException("Operação não realizada com sucesso. Metodo save devedor.", e);
				}
			}


			throw new DAOException("Operação não realizada com sucesso. Metodo save devedor.", e);		

		} 

		finally {

			if (pstCredencial != null) pstCredencial.close();
		}


		return result;

	}

	private boolean  ExisteCPFCNPJ(String cpfcnpj, String CNPJEmpresa) throws SQLException
	{	
		PreparedStatement pstCredencial = null;	

		String sql =

				"Select " + 
						"Nome," + 
						"Email," + 
						"Telefone," + 
						"CNPJEmpresa," + 
						"DataInclusao," + 
						"DataAlteracao "+
						" from APP_CARTAO_DEVEDOR Where " + 
						" CPFCNPJ=? and CNPJEmpresa=? and Ambiente=?";
		try {
			pstCredencial = conn.prepareSTM(sql);

			pstCredencial.setString(1, cpfcnpj);
			pstCredencial.setString(2, CNPJEmpresa);
			
			if (App.AMBIENTE=="PRODUCAO") {
				pstCredencial.setString(3, "P");}
			else {
				pstCredencial.setString(3, "T");
			}	

			ResultSet resultSet = conn.executeSelect(pstCredencial); // NOSONAR

			if (resultSet.next())
			{
				return  true;

			}else
			{
				return false;
			}


		}catch (SQLException e) {
			
			App.logger.error("Erro na transação existe devedor.");
			throw new DAOException("Operação não realizada com sucesso. Metodo existe devedor", e);	

		} 
		finally {

			if (pstCredencial != null) pstCredencial.close();
		}

	}



}
