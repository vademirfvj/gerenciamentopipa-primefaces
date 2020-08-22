package opp.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import opp.dto.UsuarioDTO;

public class Funcoes {
	
	public static boolean isListaNulaOuVazia(List list) {
		if (list == null) {
			return true;
		}

		if (list.isEmpty()) {
			return true;
		}

		return false;
	}
	
	public static boolean isNuloOuVazio(Object obj) {
		if (obj == null) {
			return true;
		}

		if ("".equals(obj.toString().trim())) {
			return true;
		}

		return false;
	}
	
	public static boolean isNuloOuVazioZero(Object obj) {
		if (obj == null) {
			return true;
		}

		if ("".equals(obj.toString().trim())) {
			return true;
		}
		
		if(Integer.valueOf(obj.toString().trim()) == 0){
			return true;
		}

		return false;
	}

	public static boolean isNuloOuVazioOuDoubleZero(Object obj) {
		if (obj == null) {
			return true;
		}

		if ("".equals(obj.toString().trim())) {
			return true;
		}
		
		if (Double.valueOf(obj.toString().trim()) == 0.0) {
			return true;
		}

		return false;
	}
	
	public static double formatarDouble(double recuperarViagensFormula) {
		
		double numero = recuperarViagensFormula;
		
		DecimalFormat formato = new DecimalFormat("0.##");      
		numero = Double.valueOf(formato.format(numero));
		return numero;
	}

	public static int recuparaDiasMes(Date dataSelecionada, String ano, String mes) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataSelecionada);
		calendar.set(Integer.valueOf(ano), Integer.valueOf(mes), 1);
		
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static String obterAnoMesExtenso(int mes, int ano){  
	    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",   
	                      "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};  
	    return meses[mes]+" "+ano;  
	}

	public static String dataExtenso(Date date) {
		
	    String retorno = null;  
	    String mesExtenso = null;
	    
	    Calendar calendar = new GregorianCalendar();  
	    Date data = date;  
	    calendar.setTime(data);  
	    int mes = calendar.get(Calendar.MONTH);  
	    int dia = calendar.get(Calendar.DAY_OF_MONTH);  
	    int ano = calendar.get(Calendar.YEAR); 
		
	    
	    switch(mes) {  
	     case 0:  mesExtenso = "Janeiro"; break;  
	     case 1:  mesExtenso = "Fevereiro"; break;  
	     case 2:  mesExtenso = "Março"; break;
	     case 3:  mesExtenso = "Abril"; break;
	     case 4:  mesExtenso = "Maio"; break;
	     case 5:  mesExtenso = "Junho"; break;
	     case 6:  mesExtenso = "Julho"; break;
	     case 7:  mesExtenso = "Agosto"; break;
	     case 8:  mesExtenso = "Setembro"; break;
	     case 9: mesExtenso = "Outubro"; break;
	     case 10: mesExtenso = "Novembro"; break;
	     case 11: mesExtenso = "Dezembro";break;
	    }
	    
	    retorno = dia+" de "+mesExtenso+" de "+ano;
	    
		return retorno;
	}

	public static void setAttributeSession(String key,UsuarioDTO value) {
		getSession().setAttribute(key,value);
		
	}
	
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}
	
	public static void removeAttributeSession(String key) {
		getSession().removeAttribute(key);
	}
	
	public static boolean validarCpf(String cpfCnpj) {

		//1.Se o atributo possuir 14 caracteres é consiferado um cnpj;
		if(cpfCnpj.length() == 14){
			return false;
		}

		if(cpfCnpj.length() != 11 && cpfCnpj.length() != 14){
			return false;
		}

		//3. Se o atributo é composto por mais de 1 caractere
		int digitosIguais = 0;
		for (int i = 0; i < cpfCnpj.length() - 1; i++) {
			if (cpfCnpj.charAt(i) != cpfCnpj.charAt(i + 1)){
				break;
			}else{
				digitosIguais += 1;
			}
		}
		if (digitosIguais == 10 ){
			return false;
		}

		//4.Se o dígito verificador do CPF não é válido
		String numeros = cpfCnpj.substring(0,9);
		String digitos = cpfCnpj.substring(9);
		int somatoria = 0;
		int res = 0;

		//obtendo a somatoria dos  numeros multiplicado pela sequencia de 10 a 2
		for (int i = 10; i > 1; i--){
			int numero = Integer.parseInt(String.valueOf(numeros.charAt(10 - i)));
			somatoria +=  numero * i ;
		}

		//se o resto da divisao for menor que 2 o primeiro digito eh 0, se nao, subtrai de 11
		res = somatoria % 11 < 2 ? 0 : 11 - somatoria % 11;

		//o primeiro digito esta ok ?
		if (res != Integer.parseInt(String.valueOf(digitos.charAt(0)))){
			return false;
		}
		//agora utiliza-se o primeiro digo, ja validado, na validacao do segundo
		numeros = cpfCnpj.substring(0,10);
		somatoria = 0;

		//obtendo a somatoria dos  numeros multiplicado pela sequencia de 11 a 2
		for (int i = 11; i > 1; i--){
			int numero = Integer.parseInt(String.valueOf(numeros.charAt(11 - i)));
			somatoria +=  numero * i ;
		}
		res = somatoria % 11 < 2 ? 0 : 11 - somatoria % 11;

		//segundo digito ok?
		if (res != Integer.parseInt(String.valueOf(digitos.charAt(1)))){
			return false;
		}

		return true;
	}

	public static int recuperaDiasLicenca(String dataLicenca) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dateLicenca = df.parse(dataLicenca);
		Date dataAtual = new Date(System.currentTimeMillis());
		
		int intervalo = 0;  
		  
		Calendar periodo1 = Calendar.getInstance();  
		periodo1.setTime(dataAtual);   
		  
		Calendar periodo2 = Calendar.getInstance();  
		periodo2.setTime(dateLicenca);   
		  
		intervalo = (periodo2.get(periodo2.DAY_OF_YEAR) - periodo1.get(periodo1.DAY_OF_YEAR))+1;
		
		return intervalo;
	}

}
