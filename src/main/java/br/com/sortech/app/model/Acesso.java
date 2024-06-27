package br.com.sortech.app.model;

import java.util.HashMap;
import java.util.Map;

import br.com.sortech.app.util.TokenGenerator;
import br.com.sortech.app.service.App;
public class Acesso {
	
	private  EmpresaCartao empresacartao;
	
	public Acesso(final EmpresaCartao d) {

		empresacartao = d;

	};	

	
	
	public String GerarToken()
	{
		Map<String, Object> payload = new HashMap<String, Object>();

		payload.put("uid", String.valueOf(empresacartao.getCNPJ()));

		payload.put("nome",empresacartao.getNomeEmpresaCartao());
		
		payload.put("ambiente",App.AMBIENTE);

		TokenGenerator tokenGenerator = new TokenGenerator(App.AMBIENTE+App.FIREBASE_SUPER_SECRET_KEY);

		String token = tokenGenerator.createToken(payload);

		tokenGenerator = null;

		return token;
	}
	

}
