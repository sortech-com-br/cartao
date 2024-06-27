package br.com.sortech.app.util;


import org.apache.commons.codec.binary.Base64;
import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



public class TokenVerify {

	
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	private static final String HMAC_256 = "HmacSHA256";
	
	private static String header;
	
	private static String data;
	
	private static String signature;
	
	  
	public TokenVerify(String token)  {
		
		   String[] tokenFragments = token.split("\\.");
		   header = tokenFragments[0];
		   data = tokenFragments[1];
		   signature = tokenFragments[2];
	}
	
public boolean validateSignature(String secretKey ) throws Exception {

	    SecretKeySpec secret = new SecretKeySpec( secretKey.getBytes(),HMAC_256);
	    
	    Mac mac = Mac.getInstance(HMAC_256);
	    
	    mac.init( secret );

	    String body = header + "." + data;
	    
	    byte[] hmacDataBytes = mac.doFinal( body.getBytes(UTF8_CHARSET));
	    
	    String hmacData = Base64.encodeBase64URLSafeString(hmacDataBytes );

	    return hmacData.equals( signature ); 
	}	
	
	
}
