package br.com.sortech.app.filter;

import static spark.Spark.afterAfter;
import static spark.Spark.before;
import static spark.Spark.options;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sortech.app.dao.LogJDBCDAO;
import br.com.sortech.app.model.EmpresaCartao;
import br.com.sortech.app.model.Log;
import br.com.sortech.app.service.App;
import br.com.sortech.app.util.TokenVerify;
import spark.utils.StringUtils;
import br.com.sortech.app.dao.DevedorJDBCDAO;





public final class CorsFilter {


	private static ObjectMapper omfilter = new ObjectMapper();

	//private static LogJDBCDAO logDAO = new LogJDBCDAO();


	private static String[] acao;

	private static void gravarLog(EmpresaCartao empresacartao,
			String reqbody,String resbody,String spath, String token) {
		try {
			LogJDBCDAO logDAO = new LogJDBCDAO();
			Log log = new Log();
			log.setRequest(new StringBuilder(reqbody));
			log.setResponse(new StringBuilder(resbody));
			log.setCPFCNPJ(empresacartao.getCNPJ());
			log.setAcao(spath);
			log.setToken(token);
			logDAO.save(log);
			logDAO.closseConnDispositivo();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			App.logger.error("Erro ao gravar log: " + e.getMessage());
			//e.printStackTrace();
		}

	}
	
	private static void gravarDevedor(EmpresaCartao empresacartao) {
		try {
			DevedorJDBCDAO devedorDAO = new DevedorJDBCDAO();
			devedorDAO.save(empresacartao.getIdDevedor(),empresacartao.getCNPJ());
			devedorDAO.closseConnDispositivo();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			App.logger.error("Erro ao gravar devedor: " + e.getMessage());
			//e.printStackTrace();
		}

	}
	

	public static void enableCORS() {

		omfilter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}
			return "OK";
		});

		afterAfter((request, response) -> {
			try {

				response.type("application/json");
				if (StringUtils.isNotEmpty(request.body()))
						{		
					EmpresaCartao	dispositivo = omfilter.readValue(request.body(), EmpresaCartao.class);


						gravarLog(dispositivo, request.body().toString(), response.body().toString(),request.pathInfo(),request.headers("Authorization"));

					} 

				else
					 
				{
					App.logger.info("Erro no afterAfter request.body igual a null  acao " + request.pathInfo());
				}
				

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				App.logger.error("Erro no afterAfter: " + e.getMessage() + " acao " + request.pathInfo());
				//e.printStackTrace();
			}
		});

	 
		
		before((request, response) -> {

					response.header("Access-Control-Allow-Origin", "*");
					response.header("Access-Control-Request-Method", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
					response.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
					response.type("application/json");
					EmpresaCartao empresacartao = null;

					acao = request.pathInfo().split("/");
					App.logger.error("Chegou aqui " + acao);
					
					if (!acao[acao.length-1].equals("erro")&&!acao[acao.length-1].equals("teste")&&!acao[acao.length-1].equals("errojson")&& !acao[acao.length-1].equals("verificarAcoesPendentes")&& !acao[acao.length-1].equals("atualizarActionStatus")){

						if (request.headers("Authorization")==null) {
							response.redirect("/erro");
						}

						TokenVerify validar = new TokenVerify(request.headers("Authorization"));

						//	App.logger.info("validar token ");


						if (!validar.validateSignature(App.FIREBASE_SUPER_SECRET_KEY))
						{
							if (acao[acao.length-1].equals("gerartoken"))
							{
								validar = null;
								response.redirect("/erro");
							}
							if (!validar.validateSignature(App.AMBIENTE+App.FIREBASE_SUPER_SECRET_KEY))
							{
								validar = null;
								response.redirect("/erro");
							}
						}
						
//						JWTDecoder decode = new JWTDecoder(request.headers("Authorization"));

				
						empresacartao = omfilter.readValue(request.body(), EmpresaCartao.class);
						
						
						if ((empresacartao.getCNPJ().length() != 14)||
								(empresacartao.getNomeEmpresaCartao().trim().length()==0)||
								((empresacartao.getIdDevedor().getCPFCNPJ().length()!=11)&&
										(empresacartao.getIdDevedor().getCPFCNPJ().length()!=14))||
								(empresacartao.getIdDevedor().getNome().trim().length()==0))
						{
							validar = null;
							response.redirect("/errojson");
							
						}
						//	App.logger.info("gravar log ");
						gravarLog(empresacartao, request.body().toString(),"",request.pathInfo(),request.headers("Authorization"));
						gravarDevedor(empresacartao);
						
						validar = null;
					}
				}	)
				;
	}




}