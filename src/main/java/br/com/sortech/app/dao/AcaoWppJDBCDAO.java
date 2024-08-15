package br.com.sortech.app.dao;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import br.com.sortech.app.model.AcaoWpp;
import br.com.sortech.app.service.App;
import br.com.sortech.componentes.conn.DBConnection;

public class AcaoWppJDBCDAO implements AcaoWppDAO {
    
    private DBConnection conn = new DBConnection(0);

    public AcaoWppJDBCDAO() {}

    @Override
    public void closeConnDispositivo() throws Throwable {
        if (conn != null) {
            conn.closeConn();
        }
        super.finalize();
    }

    @Override
    public List<AcaoWpp> verificarAcoesPendentes() throws SQLException {
        App.logger.info("Entrou no /verificarAcoesPendentes");
        List<AcaoWpp> acoes = new ArrayList<>();
        String sql = "SELECT ACTION_SEQ_ID, CUSTOMER_ID, TELEFONE, CPFCNPJ, TRIBUTO, ANO, NUMERO_PARCELAMENTO, ACTION_STATUS, RESPONSE_MESSAGE FROM SEF_X_T_ACOES_WPP WHERE ACTION_STATUS = 'PENDING'";

        try (PreparedStatement pst = conn.prepareSTM(sql);
             ResultSet rs = conn.executeSelect(pst)) {

            while (rs.next()) {
                long actionSeqId = rs.getLong("ACTION_SEQ_ID");
                long customerId = rs.getLong("CUSTOMER_ID");
                String telefone = rs.getString("TELEFONE");
                String cpfcnpj = rs.getString("CPFCNPJ");
                String tributo = rs.getString("TRIBUTO");
                String ano = rs.getString("ANO");
                String numeroParcelamento = rs.getString("NUMERO_PARCELAMENTO");
                String actionStatus = rs.getString("ACTION_STATUS");
                String responseMessage = rs.getString("RESPONSE_MESSAGE");
                AcaoWpp acao = new AcaoWpp(actionSeqId, customerId, telefone, cpfcnpj, tributo, ano, numeroParcelamento, actionStatus, responseMessage);
                acoes.add(acao);
                App.logger.info("ação criada " + acao.getCpfcnpj());
            }
        } catch (SQLException e) {
            App.logger.info("Erro ao executar a querry", e);
            throw new SQLException("Erro ao verificar ações pendentes", e);
        }

        return acoes;
    }

    @Override
    public void atualizarActionStatus(List<AcaoWpp> updates) throws SQLException {
        String sql = "UPDATE SEF_X_T_ACOES_WPP SET ACTION_STATUS = ?, MODIFIED_DATE = sysdate, RESPONSE_MESSAGE = ? WHERE ACTION_SEQ_ID = ?";        

        try (PreparedStatement pst = conn.prepareSTM(sql)) {
            for (AcaoWpp update : updates) {
                pst.setString(1, update.getActionStatus());
                pst.setString(2, update.getResponseMessage());
                pst.setLong(3, update.getActionSeqId());
                pst.addBatch();
            }
            int[] updateCounts = pst.executeBatch();
            App.logger.info("Atualizações realizadas: " + updateCounts.length);
        } catch (SQLException e) {
            App.logger.info("Erro ao executar a atualização", e);
            throw new SQLException("Erro ao atualizar ACTION_STATUS", e);
        }
    }
    
    public void atualizarStatusId(List<AcaoWpp> updates) throws SQLException {
        String sql = "UPDATE ICS_CR_E_ACTION_MANAGEMENT SET STATUS_ID = ? WHERE ACTION_SEQ_ID = ?";

        try (PreparedStatement pst = conn.prepareSTM(sql)) {
            for (AcaoWpp update : updates) {
                String statusId = "OK".equals(update.getActionStatus()) ? "OK" : "ERROR";
                pst.setString(1, statusId);
                pst.setLong(2, update.getActionSeqId());
                pst.addBatch();
            }
            int[] updateCounts = pst.executeBatch();
            App.logger.info("Atualizações realizadas na tabela ICS_CR_E_ACTION_MANAGEMENT: " + updateCounts.length);
        } catch (SQLException e) {
            App.logger.info("Erro ao executar a atualização na tabela ICS_CR_E_ACTION_MANAGEMENT", e);
            throw new SQLException("Erro ao atualizar STATUS_ID na tabela ICS_CR_E_ACTION_MANAGEMENT", e);
        }
    }

}