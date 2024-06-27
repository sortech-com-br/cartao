package br.com.sortech.app.model;

import programas.ConsultarParcelamento;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.softwareag.entirex.aci.Broker;
import com.softwareag.entirex.aci.BrokerException;

import br.com.sortech.app.service.App;
import br.com.sortech.app.util.Funcoes;


public class ConsultarParcelamentos {

	private static EmpresaCartao empresacartao;
	
	final static public Logger logger = Logger.getLogger( App.class.getName());

	public ConsultarParcelamentos(final EmpresaCartao d) {

		empresacartao = d;

	};

	public Optional<List<Parcelamento>> ConsultarSITAF()
	{

		int contador = 0;
		while (contador < 4) {
			try {
			//	Broker broker = new Broker("fazendasrv393v:1971", "RPC/MAQF6X/CALLNAT");		 
			//	ConsultarParcelamento a = new ConsultarParcelamento(broker,"RPC/MAQF6X/CALLNAT");	
				
				Broker broker = new Broker(App.BROKER, App.RPC);		 
				ConsultarParcelamento a = new ConsultarParcelamento(broker,App.RPC);
				
				
				a.n40017ln(empresacartao.getIdDevedor().getCPFCNPJ(),"");

				// tratar a mensagem DAR NAO PODE SER IMPRESSO

				if (a.get_s_mensagem().trim().equals("CONTRIBUINTE SEM PARCELAMENTO PASSIVEL DE EMISSAO") ||
						a.get_s_mensagem().trim().equals("CONTRIBUINTE SEM PARCELAMENTO PASSIVEL DE EMISSAO")) 
				{ 
					App.logger.error("Consulta parcelamento: cpfcnpj:" + empresacartao.getIdDevedor().getCPFCNPJ() + " msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}

				if (a.get_s_mensagem().trim().length() != 0)
				{	
					App.logger.error("Consulta parcelamento: cpfcnpj:" + empresacartao.getIdDevedor().getCPFCNPJ() +
							" msg rotina: " + a.get_s_mensagem());
					broker.disconnect();
					return Optional.empty();
				}


				if (a.get_s_dar_cod_barras().length == 0)
				{
					broker.disconnect();
					return Optional.empty();
				}


				List<Parcelamento> lvd = new ArrayList<Parcelamento>();

				Date data = new Date();
				
				Format formatter = new SimpleDateFormat("dd/MM/yyyy");

				data = Funcoes.localDateToDate(LocalDate.parse(formatter.format(data), DateTimeFormatter.ofPattern("dd/MM/yyyy")));  


				for (int i = 0; i <  a.get_s_dar_cod_barras().length; i++) {
					if (a.get_s_dar_cod_barras()[i].trim().length()!=0)
					{
						App.logger.info("parcsortech: i " + String.valueOf(i) );
						App.logger.info("parcsortech: inf " +a.get_s_informacoes()[i]);
						App.logger.info("parcsortech: cb " +a.get_s_dar_cod_barras()[i]);
						App.logger.info("parcsortech: val " +a.get_s_dar_valor()[i]);

						Parcelamento vd = new Parcelamento();

						//App.logger.info("parcsortech: cb 1 " );
						vd.setdar_cod_barras(a.get_s_dar_cod_barras()[i]);
						//App.logger.info("parcsortech: cb 2 " );
						vd.setdar_valor((double)Integer.valueOf(a.get_s_dar_valor()[i])/100);
						//App.logger.info("parcsortech: cb 3 " );
						if (!a.get_s_dar_venc()[i].equals("00000000") ) 
						{
							
							vd.setdar_venc(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dar_venc()[i], DateTimeFormatter.ofPattern("yyyyMMdd"))));
						}
						
						//App.logger.info("parcsortech: cb 4 " );
						List<String> items = Arrays.asList(a.get_s_informacoes()[i].toString().split(";"));

						//App.logger.info("parcsortech: cb 7 " );
						if (!a.get_s_dar_venc()[i].equals("00000000") && vd.getdar_venc().before(data))
						{
							//App.logger.info("veisortech: cb 8 " );
							//vd.setAliquota("ALIQUOTA 0 %");
							vd.setVlOrigem(items.get(0).replace("VL PARC", "VL ORIG"));
							vd.setVLMulta(items.get(1).trim());
							vd.setVLJuros(items.get(2).trim());
							vd.setVLOutros(items.get(3).trim());
						} else
						{
							//App.logger.info("veisortech: cb 9 " );
							//vd.setAliquota("ALIQUOTA 0 %");
							vd.setVlOrigem(items.get(0).trim().replace("VL PARC", "VL ORIG"));
							vd.setVLMulta("MULTA: R$ 0,00");
							vd.setVLJuros("JUROS: R$ 0,00");
							vd.setVLOutros("OUTROS: R$ 0,00");
						}	

						//App.logger.info("parcsortech: cb 11 " );
						vd.setnumParcela(Integer.valueOf(a.get_s_nu_parcela()[i]));
						vd.settotalParcela(Integer.valueOf(a.get_s_qtd_parcelas()[i].trim()));
						vd.setCPFCNPJ(empresacartao.getIdDevedor().getCPFCNPJ());
						vd.setDataCriacao(Funcoes.localDateToDate(LocalDate.parse(a.get_s_dt_abertura()[i], DateTimeFormatter.ofPattern("yyyyMMdd"))));
						vd.setnumParcelamento(a.get_s_nu_parcelamento()[i].trim());	
						vd.setparcOrigem(a.get_s_origem_parc()[i]);
						
						if (a.get_s_nu_parcelamento()[i].contains(
								empresacartao.getNumParcelamento()))
						{
							lvd.add(vd);
						}
						
						vd = null;
					}
				}


				broker.disconnect();

				return Optional.of(lvd);

			}
			catch (BrokerException e) {
				contador ++;
				App.logger.error("parsortech: tentativa" + String.valueOf(contador)  + " msg de erro:" + e.toString() + " CPFCNPJ " + empresacartao.getIdDevedor().getCPFCNPJ());

				if (!e.toString().contains("DEREGISTER") && !e.toString().contains("0003 0010"))
				{
					throw new RuntimeException(e);

				} 


			}

		}

		return Optional.empty();

	}


}



