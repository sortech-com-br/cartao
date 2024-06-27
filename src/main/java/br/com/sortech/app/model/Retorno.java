package br.com.sortech.app.model;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Retorno implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private long codigo;
	
	@JsonProperty
	private String mensagem;
	
	public long getcodigo() {
		return codigo;
	}
	
	public String getmensagem() {
		return mensagem;
	}
	
	public void setcodigo(long codigo) {
		this.codigo = codigo;
	}
	
	public void setmensagem( String mensagem) {
		this.mensagem=mensagem;
	}

}
