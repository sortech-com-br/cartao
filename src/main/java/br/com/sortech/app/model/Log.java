package br.com.sortech.app.model;

	import java.io.Serializable;

	import java.util.Date;

	public class Log implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		
		private long IdLog;
		
		private String CPFCNPJ;

		private StringBuilder Request;
		
		private StringBuilder Response;
		
		private Date DataInclusao;
		
		private String Acao;
		
		private String Token;

		public long getIdLog() {
			return IdLog;
		}

		public void setIdLog(long IdLog) {
			this.IdLog = IdLog;
		}
		
		public String getCPFCNPJ() {
			return CPFCNPJ;
		}

		public void setCPFCNPJ(String CPFCNPJ) {
			this.CPFCNPJ = CPFCNPJ;
		}


		public StringBuilder getRequest() {
			return Request;
		}

		public void setRequest(StringBuilder Request) {
			this.Request = Request;
		}
		
		public StringBuilder getResponse() {
			return Response;
		}

		public void setResponse(StringBuilder Response) {
			this.Response = Response;
		}
		
		public String getAcao() {
			return Acao;
		}

		public void setAcao(String Acao) {
			this.Acao = Acao;
		}

		public String getToken() {
			return Token;
		}
		
		public void setToken(String Token ) {
			this.Token = Token;
		}
		
		public Date getDataInclusao() {
			return DataInclusao;
		}

		public void setDataInclusao(Date DataInclusao) {
			this.DataInclusao = DataInclusao;
		}

}