package opp.util;

import java.io.InputStream;
import java.util.Properties;

public class Propriedades {
	
	private Properties props = new Properties();
	private static Propriedades _instance = new Propriedades();
	private String file = "operacaopipa.properties";

	private  Propriedades() {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(file);
		try {
			props.load( input );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/*metodo para leitura de uma propriedade*/
	public String buscaPropriedade(String prop){
		return  props.getProperty( prop );
	}

	/*metodo responsavel pela instanciação do objeto singleton*/
	public static synchronized Propriedades getInstance() {
		if (_instance==null) {
			_instance = new Propriedades();
		}
		return _instance;
	}
}
