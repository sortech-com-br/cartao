package br.com.sortech.app.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AcaoWpp implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty
    private long actionSeqId;
    @JsonProperty
    private long customerId;
    @JsonProperty
    private String telefone;
    @JsonProperty
    private String cpfcnpj;
    @JsonProperty
    private String tributo;
    @JsonProperty
    private String ano;
    @JsonProperty
    private String numeroParcelamento;
    @JsonProperty
    private String actionStatus;
    @JsonProperty
    private String responseMessage;

    // Construtor padrão
    public AcaoWpp() {
    }

    // Construtor com argumentos
    public AcaoWpp(long actionSeqId, long customerId, String telefone, String cpfcnpj, String tributo, String ano, String numeroParcelamento, String actionStatus, String responseMessage) {
        this.actionSeqId = actionSeqId;
        this.customerId = customerId;
        this.telefone = telefone;
        this.cpfcnpj = cpfcnpj;
        this.tributo = tributo;
        this.ano = ano;
        this.numeroParcelamento = numeroParcelamento;
        this.actionStatus = actionStatus;
        this.responseMessage = responseMessage;
    }

    // Getters
    public long getActionSeqId() {
        return actionSeqId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public String getTributo() {
        return tributo;
    }

    public String getAno() {
        return ano;
    }

    public String getNumeroParcelamento() {
        return numeroParcelamento;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
