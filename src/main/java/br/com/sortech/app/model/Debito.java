package br.com.sortech.app.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Debito implements Serializable{

	private static final long serialVersionUID = 1L;	
	

	@JsonProperty
	private String identificador;
	@JsonProperty
	private String dar_cod_barras;
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
	private String Referencia;
	@JsonProperty
	private String Tributo;


	
	public void setReferencia(String ref) {
		this.Referencia = ref;
	}
	
	public void setidentificador(String identificador) {
		this.identificador = identificador;
	}

	
	public String getIdentificador() {
		return this.identificador;
	}
	
	public void setdar_cod_barras(String dar_cod_barras) {
		this.dar_cod_barras = dar_cod_barras;
	}

	public String getdar_cod_barras() {
		return this.dar_cod_barras;
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
		
	public void setTributo(String Tributo) {
		this.Tributo = Tributo;
	}
	
	
}
