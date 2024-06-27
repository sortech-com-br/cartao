package br.com.sortech.app.model;

import java.io.Serializable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmpresaCartao implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private long  IdEmpresaCartao;
	@JsonProperty 
	private Devedor Devedor;
	@JsonProperty 
	private String GuiaITBIITCD;
	@JsonProperty 
	private String NumeroCDA;
	@JsonProperty 
	private String NumParcelamento;
	@JsonProperty 
	private String NumLancamentoTaxa;
	@JsonProperty 
	private String NumLancamentoICMSISS;
	@JsonProperty 
	private String CFDFISSAutonomo;
	@JsonProperty
	private String CNPJ;
	@JsonProperty
	private String Token;
	@JsonProperty
	private String NomeEmpresaCartao;


	public String getNomeEmpresaCartao() {
		return NomeEmpresaCartao;
	}
	
	public long getIdEmpresaCartao() {
		return IdEmpresaCartao;
	}


	public String getCNPJ() {
		return CNPJ;
	}


	public String getToken() {
		return Token;
	}
	
	public Devedor getIdDevedor() {
		return Devedor;	}
	
	public void setIdEmpresaCartao(long IdEmpresaCartao) {
		this.IdEmpresaCartao =  IdEmpresaCartao;
	}
	
	public void setCNPJ( String CNPJ) {
		this.CNPJ = CNPJ;
	}	

	
	

	public void setToken(String Token) {
		this.Token = Token;
	}


	public void setNomeEmpresaCartao(String NomeEmpresaCartao) {
		this.NomeEmpresaCartao = NomeEmpresaCartao;
	}

	
	
	public void setGuiaITBIITCD(String GuiaITBIITCD) {
		this.GuiaITBIITCD = GuiaITBIITCD;
	}
	public void setNumeroCDA(String NumeroCDA) {
		this.NumeroCDA = NumeroCDA;
	}
	public void setNumParcelamento(String NumParcelamento) {
		this.NumParcelamento = NumParcelamento;
	}
	public void setNumLancamentoTaxa(String NumLancamentoTaxa) {
		this.NumLancamentoTaxa = NumLancamentoTaxa;
	}
	public void setNumLancamentoICMSISS(String NumLancamentoICMSISS) {
		this.NumLancamentoICMSISS = NumLancamentoICMSISS;
	}
	public void setCFDFISSAutonomo(String CFDFISSAutonomo) {
		this.CFDFISSAutonomo = CFDFISSAutonomo;
	}
	
	
	
	public String getGuiaITBIITCD() {
		return GuiaITBIITCD;
	}
	
	public String getNumeroCDA() {
		return NumeroCDA;
	}
	public String getNumParcelamento() {
		return NumParcelamento;
	}
	
	public String getNumLancamentoTaxa() {
		return NumLancamentoTaxa;
	}
	
	public String getNumLancamentoICMSISS() {
		return NumLancamentoICMSISS;
	}
	
	public String getCFDFISSAutonomo() {
		return CFDFISSAutonomo;
	}
	
		
	public void popularEmpresaCartao(
			final long IdEmpresaCartao, final String CPFCNPJ, final String Nome,
			 final String Email,
			 final Date DataAberturaEmpresaCFI,
			 final String CNPJ,
			 final String Token,
			 final String Telefone,
			 final String NomeEmpresaCartao) {
			 this.IdEmpresaCartao = IdEmpresaCartao;
		     this.Devedor.setCPFCNPJ(CPFCNPJ);
		     this.Devedor.setNome(Nome);
		     this.Devedor.setEmail(Email);
		     this.Devedor.setTelefone(Telefone);
		     this.CNPJ=CNPJ;
		     this.NomeEmpresaCartao=NomeEmpresaCartao;
		 	 this.Token=Token;
		}
	

}


