package br.com.sortech.app.model;



import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Devedor implements Serializable{


	
	private static final long serialVersionUID = 1L;


	@JsonProperty
	private String CPFCNPJ;
	@JsonProperty
	private String Nome;
	@JsonProperty
	private String Email;
	@JsonProperty
	private String Telefone;
	
	
	public String getEmail() {
		return Email;
	}
		
	public String getCPFCNPJ() {
		return CPFCNPJ;
	}
	
	public String getNome() {
		return Nome;
	}
	public String getTelefone() {
		return Telefone;
	}


	public void setCPFCNPJ(String CPFCNPJ) {
		this.CPFCNPJ = CPFCNPJ;
	}	

	public void setNome(String Nome) {
		this.Nome = Nome;
	}
	
	public void setEmail(String Email) {
		this.Email = Email;
	}
	public void setTelefone(String Telefone) {
		this.Telefone = Telefone;
	}
	public void popularDevedor(final long id, final String CPFCNPJ, final String Nome,
			 final String Email, 
			 final String Telefone) {
		    this.CPFCNPJ = CPFCNPJ;
		    this.Nome = Nome;
		    this.Email = Email;
		    this.Telefone = Telefone;
		    
		    
		}
	
	
}



