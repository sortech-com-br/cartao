package br.com.sortech.app.model;



import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.softwareag.entirex.aci.Broker;
import com.softwareag.entirex.aci.BrokerException;

import br.com.sortech.app.service.App;
import br.com.sortech.app.util.Funcoes;
import programas.ConsultarDividaAtiva;
import programas.ConsultarICMSISS;
import programas.ConsultarITBIITCD;
import programas.ConsultarTaxas;


public class ConsultarOutrosDebitos {


	private  EmpresaCartao empresacartao;

	public ConsultarOutrosDebitos(final EmpresaCartao d) {

		empresacartao = d;

	};	


	public Optional<OutrosDebitos> ConsultarITBIITCD() throws Throwable {

		//App.logger.info("DAT:1");
		OutrosDebitos outros = new OutrosDebitos();

		
		outros.setCPFCNPJ(empresacartao.getIdDevedor().getCPFCNPJ());

		// ITCDITBI
		List<Debito> ltempITBIITCD =  new ArrayList<Debito>();
		//App.logger.info("DAT:19");
		ConsultarITBITCD().ifPresent(list -> list.forEach(i -> 
			ltempITBIITCD.add(i)
		));
		//App.logger.info("DAT:20");
		if (!ltempITBIITCD.isEmpty())
		{
			outros.setlDebitosITBIITCD(ltempITBIITCD);
		}
		//App.logger.info("DAT:21");
		return Optional.of(outros);

	}



	private Optional<List<Debito>> ConsultarITBITCD() {

		int contador = 0;
		while (contador < 4) {
			try {
				Broker broker = new Broker(App.BROKER, App.RPC);		 
				ConsultarITBIITCD a = new ConsultarITBIITCD(broker,App.RPC);
				//Broker broker = new Broker("10.69.1.196:1971", "RPC/MAQF6X/CALLNAT");		 
				//ConsultarITBIITCD a = new ConsultarITBIITCD(broker,"RPC/MAQF6X/CALLNAT");		
				//a.n40017m2("22895230110");
				a.n40017m2(this.empresacartao.getIdDevedor().getCPFCNPJ().trim());



				if (a.get_s_mensagem().trim().equals("CONTRIBUINTE NAO POSSUI GUIA DE ITBI/ITCD")) 
				{ 
					App.logger.error("Consulta ITBIITCD: Inscricao:" +
							empresacartao.getIdDevedor().getCPFCNPJ().trim() + " msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				if (a.get_s_mensagem().trim().length() != 0)
				{	
					App.logger.error("Consulta ITBIITCD: CPF:" + empresacartao.getIdDevedor().getCPFCNPJ().trim() +
							" msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				List<Debito> lvd = new ArrayList<Debito>();

				Date data = new Date();
				
				Format formatter = new SimpleDateFormat("dd/MM/yyyy");

				data = Funcoes.localDateToDate(LocalDate.parse(formatter.format(data), DateTimeFormatter.ofPattern("dd/MM/yyyy")));  


				for (int i = 0; i <  a.get_s_dar_cod_barras().length; i++) {
					if ((a.get_s_dar_cod_barras()[i].trim().length()!=0)&&(a.get_s_dar_venc()[i].trim().length()==8))
					{
						//App.logger.info("ITBIITCD: i " + String.valueOf(i) );
						//App.logger.info("ITBIITCD: inf " +a.get_s_informacoes()[i]);
						//App.logger.info("ITBIITCD: cb " +a.get_s_dar_cod_barras()[i]);

						Debito vd = new Debito();
						//App.logger.info("ITBIITCD: cb 1 " );
						vd.setdar_cod_barras(a.get_s_dar_cod_barras()[i]);
						//App.logger.info("ITBIITCD: cb 2 " );
						vd.setdar_valor((double)Integer.valueOf(a.get_s_dar_valor()[i])/100);
						//App.logger.info("ITBIITCD: cb 3 " );
						vd.setdar_venc(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dar_venc()[i], DateTimeFormatter.ofPattern("ddMMyyyy"))));
						//App.logger.info("ITBIITCD: cb 4 " );
						List<String> items = Arrays.asList(a.get_s_informacoes()[i].toString().split(";"));
						//App.logger.info("ITBIITCD: cb 5 " );
						vd.setinformacoes(items.get(2).trim());

						vd.setReferencia(items.get(1).trim());

						if (vd.getdar_venc().before(data) || vd.getinformacoes().contains("DAT") || vd.getinformacoes().contains("PAR 89"))
						{
							//App.logger.info("ITBIITCD: cb 8 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta(items.get(4).trim());
							vd.setVLJuros(items.get(5).trim());
							vd.setVLOutros(items.get(6).trim());
						} else
						{
							//App.logger.info("ITBIITCD: cb 9 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta("MULTA: R$ 0,00");
							vd.setVLJuros("JUROS: R$ 0,00");
							vd.setVLOutros("OUTROS: R$ 0,00");
						}

						vd.setidentificador(items.get(0).trim());
						if (items.get(0).trim().contains(this.empresacartao.getGuiaITBIITCD().trim()))
							lvd.add(vd);
						vd = null;

					}
				}


				broker.disconnect();

				return Optional.of(lvd);

			}
			catch (BrokerException e) {
				contador ++;
				App.logger.error("ITBIITCD: tentativa" + String.valueOf(contador)  + " msg de erro:" + e.toString());

				if (!e.toString().contains("DEREGISTER") && !e.toString().contains("0003 0010"))
				{
					throw new RuntimeException(e);

				} 


			}

		}

		return Optional.empty();

	}

	
	public Optional<OutrosDebitos> ConsultarICMSISS() throws Throwable {

		//App.logger.info("DAT:1");
		OutrosDebitos outros = new OutrosDebitos();

		
		outros.setCPFCNPJ(empresacartao.getIdDevedor().getCPFCNPJ());
		
		List<Debito> ltempicms = new ArrayList<Debito>();
		
		ConsultarICMSSS().ifPresent(list -> list.forEach(i -> 
		ltempicms.add(i)));
		
		//App.logger.info("DAT:5");

		if (!ltempicms.isEmpty())
		{
			outros.setlDebitosICMSISS(ltempicms);
		} 
		
		return Optional.of(outros);
	}
	
	private Optional<List<Debito>> ConsultarICMSSS() {

		int contador = 0;
		while (contador < 4) {
			try {
				Broker broker = new Broker(App.BROKER, App.RPC);		 
				ConsultarICMSISS a = new ConsultarICMSISS(broker,App.RPC);
				//Broker broker = new Broker("10.69.1.196:1971", "RPC/MAQF6X/CALLNAT");		 
				//ConsultarICMSISS a = new ConsultarICMSISS(broker,"RPC/MAQF6X/CALLNAT");

	
					//a.n40017m4("03127118163");
					//a.n40017m4("19651473649");
					a.n40017m4(this.empresacartao.getIdDevedor().getCPFCNPJ().trim());



				if (a.get_s_mensagem().trim().equals("CONTRIBUINTE NAO POSSUI DEBITO EM ABERTO")) 
				{ 
					App.logger.error("Consulta ICMS/ISS: Inscricao:" +
							this.empresacartao.getIdDevedor().getCPFCNPJ() + " msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				if (a.get_s_mensagem().trim().length() != 0)
				{	
					App.logger.error("Consulta ICMS/ISS: insricao:" +this.empresacartao.getIdDevedor().getCPFCNPJ() +
							" msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				List<Debito> lvd = new ArrayList<Debito>();


				Date data = new Date();
				
				Format formatter = new SimpleDateFormat("dd/MM/yyyy");

				data = Funcoes.localDateToDate(LocalDate.parse(formatter.format(data), DateTimeFormatter.ofPattern("dd/MM/yyyy")));  

				for (int i = 0; i <  a.get_s_dar_cod_barras().length; i++) {
					if ((a.get_s_dar_cod_barras()[i].trim().length()!=0)&&(a.get_s_dar_venc()[i].trim().length()==8))
					{
						//App.logger.info("icmsiss: i " + String.valueOf(i) );
						//App.logger.info("icmsiss: inf " +a.get_s_informacoes()[i]);
						//App.logger.info("icmsiss: cb " +a.get_s_dar_cod_barras()[i]);

						Debito vd = new Debito();
						//App.logger.info("icmsiss: cb 1 " );
						vd.setdar_cod_barras(a.get_s_dar_cod_barras()[i]);
						//App.logger.info("icmsiss: cb 2 " );
						vd.setdar_valor((double)Integer.valueOf(a.get_s_dar_valor()[i])/100);
						//App.logger.info("icmsiss: cb 3 " );
						vd.setdar_venc(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dar_venc()[i], DateTimeFormatter.ofPattern("ddMMyyyy"))));
						//App.logger.info("icmsiss: cb 4 " );
						List<String> items = Arrays.asList(a.get_s_informacoes()[i].toString().split(";"));
						//App.logger.info("icmsiss: cb 5 " );
						vd.setinformacoes(items.get(2).trim());


						vd.setReferencia(items.get(1).trim());

						if (vd.getdar_venc().before(data) || vd.getinformacoes().contains("DAT") || vd.getinformacoes().contains("PAR 89"))
						{
							//App.logger.info("ICMSISS: cb 8 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta(items.get(4).trim());
							vd.setVLJuros(items.get(5).trim());
							vd.setVLOutros("OUTROS: R$ 0,00");
						} else
						{
							//App.logger.info("ICMSISS: cb 9 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta("MULTA: R$ 0,00");
							vd.setVLJuros("JUROS: R$ 0,00");
							vd.setVLOutros("OUTROS: R$ 0,00");
						}

						vd.setTributo(items.get(6).trim());

						vd.setidentificador(items.get(0).trim());

						if (items.get(0).trim().contains(this.empresacartao.getNumLancamentoICMSISS().trim()))
							lvd.add(vd);
						vd = null;


					}
				}


				broker.disconnect();

				return Optional.of(lvd);

			}
			catch (BrokerException e) {
				contador ++;
				App.logger.info("icmsiss: tentativa" + String.valueOf(contador)  + " msg de erro:" + e.toString());

				if (!e.toString().contains("DEREGISTER") && !e.toString().contains("0003 0010"))
				{
					throw new RuntimeException(e);

				} 


			}

		}

		return Optional.empty();

	}


	
	public Optional<OutrosDebitos> ConsultarTaxas() throws Throwable {
	//Taxas	
	OutrosDebitos outros = new OutrosDebitos();
	//App.logger.info("DAT:8");
	List<Debito> ltemptaxas = new ArrayList<Debito>();
	//App.logger.info("DAT:9");
	ConsultarTaxa().ifPresent(list -> list.forEach(i -> 
	ltemptaxas.add(i)));
	//App.logger.info("DAT:10");
	

	if (!ltemptaxas.isEmpty())
	{
		
		outros.setlDebitosTaxas(ltemptaxas);
	} 
	//App.logger.info("DAT:13");
		return Optional.of(outros);
		
	}
	
	
	
	private Optional<List<Debito>> ConsultarTaxa() {

		int contador = 0;
		while (contador < 4) {
			try {
				Broker broker = new Broker(App.BROKER, App.RPC);		 
				ConsultarTaxas a = new ConsultarTaxas(broker,App.RPC);
				//				Broker broker = new Broker("10.69.1.196:1971", "RPC/MAQF6X/CALLNAT");		 
				//				ConsultarTaxas a = new ConsultarTaxas(broker,"RPC/MAQF6X/CALLNAT");


					//a.n40017m3("70278822134");
					//a.n40017m3("19651473649");
					a.n40017m3(this.empresacartao.getIdDevedor().getCPFCNPJ().trim());

			

				if (a.get_s_mensagem().trim().equals("CONTRIBUINTE SEM LANCAMENTOS DE TAXAS EM ABERTO")) 
				{ 
					App.logger.error("Consulta Taxas: Inscricao:" +
							this.empresacartao.getIdDevedor().getCPFCNPJ() + " msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				if (a.get_s_mensagem().trim().length() != 0)
				{	
					App.logger.error("Consulta Taxas: insricao:" +this.empresacartao.getIdDevedor().getCPFCNPJ() +
							" msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				List<Debito> lvd = new ArrayList<Debito>();


				Date data = new Date();
				
				Format formatter = new SimpleDateFormat("dd/MM/yyyy");

				data = Funcoes.localDateToDate(LocalDate.parse(formatter.format(data), DateTimeFormatter.ofPattern("dd/MM/yyyy")));  


				for (int i = 0; i <  a.get_s_dar_cod_barras().length; i++) {
					if ((a.get_s_dar_cod_barras()[i].trim().length()!=0)&&(a.get_s_dar_venc()[i].trim().length()==8))
					{
						//App.logger.info("Taxas: i " + String.valueOf(i) );
						//App.logger.info("Taxas: inf " +a.get_s_informacoes()[i]);
						//App.logger.info("Taxas: cb " +a.get_s_dar_cod_barras()[i]);

						Debito vd = new Debito();
						//App.logger.info("Taxas: cb 1 " );
						vd.setdar_cod_barras(a.get_s_dar_cod_barras()[i]);
						//App.logger.info("Taxas: cb 2 " );
						vd.setdar_valor((double)Integer.valueOf(a.get_s_dar_valor()[i])/100);
						//App.logger.info("Taxas: cb 3 " );
						vd.setdar_venc(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dar_venc()[i], DateTimeFormatter.ofPattern("ddMMyyyy"))));
						//App.logger.info("Taxas: cb 4 " );
						List<String> items = Arrays.asList(a.get_s_informacoes()[i].toString().split(";"));
						//App.logger.info("Taxas: cb 5 " );
						vd.setinformacoes(items.get(2).trim());


						vd.setReferencia(items.get(1).trim());

						if (vd.getdar_venc().before(data) || vd.getinformacoes().contains("DAT") || vd.getinformacoes().contains("PAR 89"))
						{
							//App.logger.info("Taxas: cb 8 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta(items.get(4).trim());
							vd.setVLJuros(items.get(5).trim());
							vd.setVLOutros("OUTROS: R$ 0,00");
						} else
						{
							//App.logger.info("Taxas: cb 9 " );
							vd.setVlOrigem(items.get(3).trim());
							vd.setVLMulta("MULTA: R$ 0,00");
							vd.setVLJuros("JUROS: R$ 0,00");
							vd.setVLOutros("OUTROS: R$ 0,00");
						}

						vd.setTributo(items.get(6).trim());

						vd.setidentificador(items.get(0).trim());
						if (items.get(0).trim().contains(this.empresacartao.getNumLancamentoTaxa().trim()))
							lvd.add(vd);
						vd = null;

					}
				}

				broker.disconnect();

				return Optional.of(lvd);

			}
			catch (BrokerException e) {
				contador ++;
				App.logger.info("Taxas: tentativa" + String.valueOf(contador)  + " msg de erro:" + e.toString());

				if (!e.toString().contains("DEREGISTER") && !e.toString().contains("0003 0010"))
				{
					throw new RuntimeException(e);

				} 


			}

		}

		return Optional.empty();

	}

	public Optional<OutrosDebitos> ConsultarDAT() throws Throwable {

	OutrosDebitos outros = new OutrosDebitos();	
	
	//DAT
	List<Debito> ltempdat =  new ArrayList<Debito>();
	//App.logger.info("DAT:14");
	ConsultarCDA().ifPresent(list -> list.forEach(i -> 
	ltempdat.add(i)));
	//App.logger.info("DAT:15");
	

	if (!ltempdat.isEmpty())
	{
		outros.setlDebitosDat(ltempdat);
	} 
	return Optional.of(outros);
	}
	
	private Optional<List<Debito>> ConsultarCDA() {
		int contador = 0;
		while (contador < 4) {
			try {
				Broker broker = new Broker(App.BROKER, App.RPC);		 
				ConsultarDividaAtiva a = new ConsultarDividaAtiva(broker,App.RPC);
				//Broker broker = new Broker("10.69.1.196:1971", "RPC/MAQF6X/CALLNAT");		 
				//ConsultarDividaAtiva a = new ConsultarDividaAtiva(broker,"RPC/MAQF6X/CALLNAT");



					

					a.n40017m1(this.empresacartao.getIdDevedor().getCPFCNPJ().trim(),"");

		

				if (a.get_s_mensagem().trim().equals("CONTRIBUINTE NAO POSSUI DEBITO EM ABERTO.")) 
				{ 
					App.logger.error("Consulta DAT: Inscricao:" +
							this.empresacartao.getIdDevedor().getCPFCNPJ().trim() + " msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				if (a.get_s_mensagem().trim().length() != 0 &&
						!a.get_s_mensagem().trim().equals("PROCURE A SECRETARIA DE FAZENDA DF"))
				{	
					App.logger.error("Consulta DAT: insricao:" +this.empresacartao.getIdDevedor().getCPFCNPJ().trim() +
							" msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				List<Debito> lvd = new ArrayList<Debito>();



				for (int i = 0; i <  a.get_s_dar_cod_barras().length; i++) {
					if ((a.get_s_dar_cod_barras()[i].trim().length()!=0)&&(a.get_s_dar_venc()[i].trim().length()==8))
					{
						//App.logger.info("DAT: i " + String.valueOf(i) );
						//App.logger.info("DAT: inf " +a.get_s_informacoes()[i]);
						//App.logger.info("DAT: cb " +a.get_s_dar_cod_barras()[i]);

						Debito vd = new Debito();
						//App.logger.info("DAT: cb 1 " );
						vd.setdar_cod_barras(a.get_s_dar_cod_barras()[i]);
						//App.logger.info("DAT: cb 2 " );
						vd.setdar_valor((double)Integer.valueOf(a.get_s_dar_valor()[i])/100);
						//App.logger.info("DAT: cb 3 " );
						vd.setdar_venc(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dar_venc()[i], DateTimeFormatter.ofPattern("ddMMyyyy"))));
						//App.logger.info("DAT: cb 4 " );
						List<String> items = Arrays.asList(a.get_s_informacoes()[i].toString().split(";"));
						//App.logger.info("DAT: cb 5 " );
						vd.setinformacoes(items.get(2).trim());

						vd.setReferencia(items.get(1).trim());

						//App.logger.info("DAT: cb 8 " );
						vd.setVlOrigem(items.get(3).trim());
						vd.setVLMulta(items.get(4).trim());
						vd.setVLJuros(items.get(5).trim());
						vd.setVLOutros(items.get(6).trim());

						vd.setidentificador(items.get(0).trim());

						if (items.get(0).trim().contains(this.empresacartao.getNumeroCDA().trim()))
							lvd.add(vd);
						vd = null;

					}
				}


				broker.disconnect();

				return Optional.of(lvd);

			}
			catch (BrokerException e) {
				contador ++;
				App.logger.info("DAT: tentativa" + String.valueOf(contador)  + " msg de erro:" + e.toString());

				if (!e.toString().contains("DEREGISTER") && !e.toString().contains("0003 0010"))
				{
					throw new RuntimeException(e);

				} 


			}

		}

		return Optional.empty();

	}
	
	
	public Optional<OutrosDebitos> ConsultarISSAutonomo() throws Throwable {

		//App.logger.info("DAT:1");
		OutrosDebitos outros = new OutrosDebitos();

	
		List<Debito> ltempiss = new ArrayList<Debito>();
		//App.logger.info("DAT:4");
		ConsultarISSAuton().ifPresent(list -> list.forEach(i -> 
		ltempiss.add(i)));
		//App.logger.info("DAT:5");


		if (!ltempiss.isEmpty())
		{
			outros.setlDebitosDat(ltempiss);
		} 
		return Optional.of(outros);

	}
	private Optional<List<Debito>> ConsultarISSAuton() {
	//	int contador = 0;
	return Optional.empty();

	}
	
}
