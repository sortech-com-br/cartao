package br.com.sortech.app.util;


import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.Charset;


public class JWTDecoder {


	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	private static String header;

	private static String data;

	public JWTDecoder(String token)  {
		
		String[] tokenFragments = token.split("\\.");
		
		header = tokenFragments[0];
		
		data = tokenFragments[1];
		
		byte[] hmacDataBytes =header.getBytes(UTF8_CHARSET);

		String hmacData = String.valueOf(Base64.decodeBase64(hmacDataBytes));

		header = hmacData; 
		
		hmacDataBytes = data.getBytes(UTF8_CHARSET);
		
		byte[] decodedBytes	=	Base64.decodeBase64(hmacDataBytes);
		
		hmacData = new String(decodedBytes, Charset.forName("UTF-8"));
		
		data = hmacData;
		

	}

	
	public String getuid() throws Exception {

		
		ObjectMapper mapper = new ObjectMapper();
		
	    JsonNode actualObj = mapper.readTree(data);

	    JsonNode jsonNode1 = actualObj.get("d").get("uid");
	    
		return jsonNode1.textValue(); 
	}
	
	public String geNome() throws Exception {

		
		ObjectMapper mapper = new ObjectMapper();
		
	    JsonNode actualObj = mapper.readTree(data);

	    JsonNode jsonNode1 = actualObj.get("d").get("nome");
	    
		return jsonNode1.textValue(); 
	}
	
	public String geAmbiente() throws Exception {

		
		ObjectMapper mapper = new ObjectMapper();
		
	    JsonNode actualObj = mapper.readTree(data);

	    JsonNode jsonNode1 = actualObj.get("d").get("ambiente");
	    
		return jsonNode1.textValue(); 
	}
	

}