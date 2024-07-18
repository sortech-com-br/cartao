package br.com.sortech.app.dao;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import br.com.sortech.componentes.conn.DBConnection;

public class AcaoWppJDBCDAO {

    private DBConnection conn = new DBConnection(0);

    public AcaoWppJDBCDAO() {}

    public void closeConnDispositivo() throws Throwable {
        if (conn != null) {
            conn.closeConn();
        }
        super.finalize();
    }
    
    public void verificarAcoesPendentes() throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;

        String query = "SELECT ACTION_STATUS FROM SEF_X_T_ACOES_WPP WHERE ACTION_STATUS = 'pending'";

        try {
            pst = conn.prepareSTM(query);
            rs = conn.executeSelect(pst);

            while (rs.next()) {
                String actionStatus = rs.getString("ACTION_STATUS");
                enviarMensagemAcaoPendente(actionStatus);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar ações pendentes: " + e.getMessage());
            throw new DAOException("Operação não realizada com sucesso. Metodo verificarAcoesPendentes.", e);
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
        }
    }

    private void enviarMensagemAcaoPendente(String actionStatus) {
        System.out.println("Ação pendente, enviada para o serviço de mensageira. Status: " + actionStatus);
    }   
}
