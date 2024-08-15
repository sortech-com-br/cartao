package br.com.sortech.app.service;


import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sortech.app.dao.AcaoWppJDBCDAO;
import br.com.sortech.app.dao.AcaoWppDAO;
import br.com.sortech.app.model.AcaoWpp;
import br.com.sortech.app.filter.CorsFilter;
import br.com.sortech.app.model.EmpresaCartao;
import br.com.sortech.app.model.ManterAcao;
import br.com.sortech.app.model.ConsultarOutrosDebitos;
import br.com.sortech.app.model.OutrosDebitos;
import br.com.sortech.app.model.ConsultarParcelamentos;
import br.com.sortech.app.model.Parcelamento;
import br.com.sortech.app.model.Retorno;
import br.com.sortech.app.model.Acesso;
import br.com.sortech.app.util.PropertiesLoader;
import spark.Spark;


public class App {

	static public int PORTA_SERVIDOR_HTTP;

	static public String NOME_SERVIDOR_HTTP;

	static public String URL_SMS;

	final static public Logger logger = Logger.getLogger( App.class.getName());

	private final ObjectMapper om = new ObjectMapper();

	static public String FIREBASE_SUPER_SECRET_KEY = "sortechsefdf";
	
	static public String BROKER;
	
	static public String RPC;
	
	static public String AMBIENTE;


	protected App (String porta) {


		PropertiesLoader props = new PropertiesLoader();
		NOME_SERVIDOR_HTTP = props.prop.getProperty("NomeServidor");
		PORTA_SERVIDOR_HTTP = Integer.parseInt(porta); //Integer.parseInt(props.prop.getProperty("PortaHTTP"));
		URL_SMS = props.prop.getProperty("URLSMS");
		BROKER = props.prop.getProperty("BROKER");
		RPC = props.prop.getProperty("RPC");
		AMBIENTE = props.prop.getProperty("AMBIENTE");
	}


	public static void main(String[] args) throws Throwable {


		App app = new App(args[0]);

		app.execAplicacao();

	}


	public void execAplicacao() throws IOException, TimeoutException, Exception {

		logger.info("Inicializando servidor. Nome:" + NOME_SERVIDOR_HTTP  + " Porta:"+ PORTA_SERVIDOR_HTTP);

		port(PORTA_SERVIDOR_HTTP);

		CorsFilter.enableCORS();

		get("/teste", (req, res) ->{return "Teste do servidor cartao";});

		get("/erro", (req, res) ->{return "Ocorreu um erro inesperado, por favor, tente mais tarde.";});
		
		
		get("/errojson", (req, res) ->{
			Retorno ret = new Retorno();
			res.type("application/json");
			om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
					.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
					.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
					.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
					.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
			
			ret.setcodigo(0);					
			ret.setmensagem("Ocorreu um erro no formato JSON, favor validar os tipos de dados, formato e obrigatoriedade, de acordo com o manual de integra��o.");
			res.status(479); 
			
			return om.writeValueAsString(ret);
	
		});
		
		

		logger.info("Servidor preparado.");
		
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		

		path("apic/", () -> {
		
		
			//Consultar débitos outros ICMSISS, TAXAS e ITBI

			post("itbiitcdseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarOutrosDebitos ConsultarOutros = new ConsultarOutrosDebitos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					Optional<OutrosDebitos> outros = ConsultarOutros.ConsultarITBIITCD();

					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					ConsultarOutros = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});	
			
		
			post("icmsissseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarOutrosDebitos ConsultarOutros = new ConsultarOutrosDebitos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					Optional<OutrosDebitos> outros = ConsultarOutros.ConsultarICMSISS();

					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					ConsultarOutros = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});	
			
	
			post("taxasseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarOutrosDebitos ConsultarOutros = new ConsultarOutrosDebitos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					Optional<OutrosDebitos> outros = ConsultarOutros.ConsultarTaxas();

					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					ConsultarOutros = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});	
			
			post("issautonseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarOutrosDebitos ConsultarOutros = new ConsultarOutrosDebitos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					Optional<OutrosDebitos> outros = ConsultarOutros.ConsultarISSAutonomo();

					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					ConsultarOutros = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});		
			post("datsseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarOutrosDebitos ConsultarOutros = new ConsultarOutrosDebitos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					Optional<OutrosDebitos> outros = Optional.empty();
					
					if (empresa.getNumeroCDA().length() != 0)
						outros = ConsultarOutros.ConsultarDAT();

					
					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					ConsultarOutros = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});	
			
			
			post("parcelamentoseec", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao empresa = null;
				try {

					res.type("application/json");
					
					empresa = om.readValue(req.body(), EmpresaCartao.class);

					ConsultarParcelamentos consultarParc = new ConsultarParcelamentos(empresa);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));


					Optional<List<Parcelamento>> outros = Optional.empty();
					
					if (empresa.getNumParcelamento().length() != 0)
					
				         outros = consultarParc.ConsultarSITAF();

					if (outros.isPresent())
					{

						String json = om.writeValueAsString(outros.get());

						ret.setcodigo(1);
						ret.setmensagem(json);
						res.status(200);
					} 
					else
					{
						ret.setcodigo(0);					
						res.status(479); 
					}

					consultarParc = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("parcsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
			});	
			
			
			
			post("gerartoken", (req, res) -> {
				Retorno ret = new Retorno();
				EmpresaCartao dispositivo = null;
				try {

					res.type("application/json");
					
					dispositivo = om.readValue(req.body(), EmpresaCartao.class);

					Acesso acesso = new Acesso(dispositivo);



					om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
							.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
							.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
							.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

					String token = acesso.GerarToken();

					ret.setcodigo(1);
					ret.setmensagem(token);
						
					acesso = null;
					return om.writeValueAsString(ret);



				} catch (Throwable e) {

					logger.error("outsortech: " + e.toString());
					ret.setcodigo(0);
					ret.setmensagem(e.toString());
					res.status(400);
					return om.writeValueAsString(ret); 
				}
				
			});
			
			 get("verificarAcoesPendentes", (req, res) -> {
	                
				 ManterAcao manterAcao = new ManterAcao();
	                res.type("application/json");

	                try {
	                    List<AcaoWpp> acoesPendentes = manterAcao.verificarAcoesPendentes();
	                    res.status(200);
	                    return om.writeValueAsString(acoesPendentes);
	                } catch (Throwable e) {
	                    res.status(500);
	                    return "{\"status\":\"ERROR\",\"message\":\"Erro ao verificar ações pendentes: " + e.getMessage() + "\"}";
	                }
	            });
			 
			 post("/atualizarActionStatus", (req, res) -> {
				    ManterAcao manterAcao = new ManterAcao();
				    res.type("application/json");

				    try {
				        List<AcaoWpp> updates = om.readValue(req.body(), om.getTypeFactory().constructCollectionType(List.class, AcaoWpp.class));
				        
				        manterAcao.atualizarActionStatus(updates);
				        
				        res.status(200);
				        
				        return "{\"status\":\"SUCCESS\",\"message\":\"Ações atualizadas com sucesso\"}";
				    } catch (IOException e) {
				        res.status(400);
				        return "{\"status\":\"ERROR\",\"message\":\"Erro ao ler o corpo da requisição: " + e.getMessage() + "\"}";
				    } catch (Throwable e) {
				    	res.status(500);
				        return "{\"status\":\"ERROR\",\"message\":\"Erro ao atualizar ACTION_STATUS: " + e.getMessage() + "\"}";
					} 
				});


			get("stop", (request, response) -> {
				Spark.stop();
				return "Servidor finalizado!";
			});

		});



	}
	

}