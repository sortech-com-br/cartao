package br.com.sortech.app.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DevedorWpp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	@JsonProperty
	private String ActionSeqId;
	@JsonProperty
	private String CustomerId;
	@JsonProperty
	private String Telefone;
	@JsonProperty
	private String CpfCnpj;
	@JsonProperty
	private String Tributo;
	@JsonProperty
	private String Ano;
	@JsonProperty
	private String NumeroParcelamento;
	@JsonProperty
	private String ActionStatus;
	
	
	public String getActionSeqId() {
		return ActionSeqId;
	}
	
	public String getCustomerId() {
		return CustomerId;
	}
	
	public String getTelefone() {
		return Telefone;
	}
	
	public String getCpfCnpj() {
		return CpfCnpj;
	}
	
	public String getTributo() {
		return Tributo;
	}
	
	public String getAno() {
		return Ano;
	}
	
	public String getNumeroParcelamento() {
		return NumeroParcelamento;
	}
	
	public String getActionStatus() {
		return ActionStatus;
	}


}
