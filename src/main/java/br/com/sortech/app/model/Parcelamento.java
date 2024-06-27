package br.com.sortech.app.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Parcelamento implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private String CPFCNPJ;
	@JsonProperty
	private String numParcelamento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , locale = "pt-BR", timezone = "Brazil/East")
	@JsonProperty
	private Date DataCriacao;		
	@JsonProperty
	private Integer numParcela;	
	@JsonProperty
	private String Situacao;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , locale = "pt-BR", timezone = "Brazil/East")
	@JsonProperty
	private Date vctoParcela;		
	@JsonProperty
	private String dar_cod_barras;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , locale = "pt-BR", timezone = "Brazil/East")
	@JsonProperty
	private Date dar_venc;
	@JsonProperty
	private double dar_valor;
	@JsonProperty
	private String informacoes;
	@JsonProperty
	private String BaseCalculo;
	@JsonProperty
	private String Aliquota;
	@JsonProperty
	private String VlOrigem;
	@JsonProperty
	private String VLMulta;
	@JsonProperty
	private String VLJuros;
	@JsonProperty
	private String VLOutros;
	@JsonProperty
	private String parcOrigem;
	@JsonProperty
	private Integer totalParcela;

	public void setparcOrigem(String parcOrigem) {
		this.parcOrigem = parcOrigem;
	}
	
	public String getparcOrigem() {
		return this.parcOrigem;
	}	
	
	public void settotalParcela(Integer totalParcela) {
		this.totalParcela = totalParcela;
	}
	
	public Integer gettotalParcela() {
		return this.totalParcela;
	}	
	
	
	public void setCPFCNPJ(String CPFCNPJ) {
		this.CPFCNPJ = CPFCNPJ;
	}
	
	
	public String getCPFCNPJ() {
		return this.CPFCNPJ;
	}
	
	
	public void setDataCriacao(Date DataCriacao) {
		this.DataCriacao = DataCriacao;
	}

	
	public Date getDataCriacao() {
		return this.DataCriacao;
	}

	
	public void setnumParcelamento(String numParcelamento) {
		this.numParcelamento = numParcelamento;
	}
	
	public String getnumParcelamento() {
		return this.numParcelamento;
	}
	
	public void setSituacao(String Situacao) {
		this.Situacao = Situacao;
	}
	
	public String getSituacao() {
		return this.Situacao;
	}
	
	
	public void setnumParcela(Integer numParcela) {
		this.numParcela = numParcela;
	}
	
	public Integer getnumParcela() {
		return this.numParcela;
	}
		
	public void setdar_cod_barras(String dar_cod_barras) {
		this.dar_cod_barras = dar_cod_barras;
	}

	public String getdar_cod_barras() {
		return this.dar_cod_barras;
	}

	public void setvctoParcela(Date vctoParcela) {
		this.vctoParcela = vctoParcela;
	}

	public Date getvctoParcela() {
		return this.vctoParcela;
	}
	
	public void setdar_venc(Date dar_venc) {
		this.dar_venc = dar_venc;
	}

	public Date getdar_venc() {
		return this.dar_venc;
	}

	public void setdar_valor(double dar_valor) {
		this.dar_valor = dar_valor;
	}

	public double getdar_valor() {
		return this.dar_valor;
	}

	public void setinformacoes(String informacoes) {
		this.informacoes = informacoes;
	}

	public String getinformacoes() {
		return this.informacoes;
	}
	
	public String getAliquota() {
		return this.Aliquota;
	}
	
	public String getBaseCalculo() {
		return this.BaseCalculo;
	}
	
	
	public void setAliquota(String Aliquota) {
		this.Aliquota = Aliquota;
	}
	
	public void setBaseCalculo(String BaseCalculo) {
		this.BaseCalculo = BaseCalculo;
	}

	public String getVlOrigem() {
		return this.VlOrigem;
	}

	public void setVlOrigem(String VlOrigem) {
		this.VlOrigem = VlOrigem;
	}

	public String getVLMulta() {
		return this.VLMulta;
	}
	
	public void setVLMulta(String VLMulta) {
		this.VLMulta = VLMulta;
	}
	
	public String getVLJuros() {
		return this.VLJuros;
	}
	
	public void setVLJuros(String VLJuros) {
		this.VLJuros = VLJuros;
	}
	
	public String getVLOutros() {
		return this.VLOutros;
	}
	
	public void setVLOutros(String VLOutros) {
		this.VLOutros = VLOutros;
	}
	
	
	

}
