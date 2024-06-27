package br.com.sortech.app.model;


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutrosDebitos implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private String CPFCNPJ;
	@JsonProperty
	private List<Debito> lDebitosICMSISS;
	@JsonProperty
	private List<Debito> lDebitosITBIITCD;
	@JsonProperty
	private List<Debito> lDebitosTaxas;
	@JsonProperty
	private List<Debito> lDebitosDat;
	
	public void setlDebitosICMSISS(List<Debito> lDebitos)
	{
		this.lDebitosICMSISS = lDebitos;
	}
	
	public void setlDebitosITBIITCD(List<Debito> lDebitos)
	{
		this.lDebitosITBIITCD = lDebitos;
	}

	public void setlDebitosTaxas(List<Debito> lDebitos)
	{
		this.lDebitosTaxas = lDebitos;
	}
	
	public void setlDebitosDat(List<Debito> lDebitos)
	{
		this.lDebitosDat = lDebitos;
	}
	
	
	
	public void setCPFCNPJ(String CPFCNPJ) {
		this.CPFCNPJ = CPFCNPJ;
	}	

}
