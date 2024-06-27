package br.com.sortech.app.util;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;	


public class Funcoes {
	
    
    
	public static java.sql.Date getSqlDateFormat1(Date data) {
		if(data == null){
			return null;
		}
		return java.sql.Date.valueOf(dateToStr(data, "yyyy-MM-dd"));
	}
	public static String dateToStr(Date date, String format) {
		String retorno = null;
		if ((null != date) && (null != format)) {
			SimpleDateFormat formater = new SimpleDateFormat(format);
			retorno = formater.format(date);
		}
		return retorno;
	}


	public static boolean compareDates(Date psDate1, Date psDate2,String format) throws ParseException{

		String date1 = dateToStr(psDate1,format);
		String date2 = dateToStr(psDate2,format);



		if (date1.equals(date2)) {
			return true;
		} else {
			return false;
		}
	}

	public static Date localDateToDate(LocalDate localDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		//assuming start of day
		calendar.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
		return calendar.getTime();
	}	


	public static String MD5 (String chave) throws Exception{

		MessageDigest m=MessageDigest.getInstance("MD5");
		
		m.update(chave.getBytes("UTF-8"),0,chave.length());
		
		//System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));

		String hashtext = new BigInteger(1,m.digest()).toString(16);
		
		while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
		
		return hashtext;
		
	}

	public static String removeAcentos(final String str) {
		String strSemAcentos = Normalizer.normalize(str, Normalizer.Form.NFD);
		strSemAcentos = strSemAcentos.replaceAll("[^\\p{ASCII}]", "");
		return strSemAcentos;
	}
	

}
