package br.com.sortech.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {    

	public Properties prop = new Properties();

	public PropertiesLoader() {

		InputStream is = null;
		
		try {
			File f = new File("app.properties");
			is = new FileInputStream(f);

		}
		catch ( Exception e ) { is = null; }

		try {
			if ( is == null ) {
				is = this.getClass().getClassLoader().getResourceAsStream("app.properties");
				

			}
			prop.load(is);
			is.close();

		}
		catch ( Exception e ) {is = null; }

	}

}