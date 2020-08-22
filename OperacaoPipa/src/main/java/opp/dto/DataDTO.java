package opp.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DataDTO implements Serializable {
	
	private static final long serialVersionUID = 3741911768497797844L;
	
	private Date data;
	private String datasExtenso;
	
	private int indice;
	
	private int dias;
	private boolean ehFimDeSemana;
	private boolean exibirColuna;
	
	public DataDTO(){
		this.data = new Date();
		this.datasExtenso = "";
		this.indice = new Integer(0);
		this.ehFimDeSemana = false;
		this.dias = new Integer(0);
		this.exibirColuna = false;
	}
	
	public DataDTO(Date data, String datasExtenso, int indice, boolean ehFimDeSemana,boolean exibirColuna) {
		super();
		this.data = data;
		this.datasExtenso = datasExtenso;
		this.indice = indice;
		this.ehFimDeSemana = ehFimDeSemana;
		this.dias = new Integer(0);
		this.exibirColuna = exibirColuna;
	}

	public Date getData() {
		return data;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	public boolean isEhFimDeSemana() {
		return ehFimDeSemana;
	}

	public void setEhFimDeSemana(boolean ehFimDeSemana) {
		this.ehFimDeSemana = ehFimDeSemana;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public void setData(Calendar periodo1) {
		this.data = periodo1.getTime();
	}
	
	public String getDatasExtenso() {
		return datasExtenso;
	}
	
	public void setDatasExtenso(String datasExtenso) {
		this.datasExtenso = datasExtenso;
	}

	public boolean isExibirColuna() {
		return exibirColuna;
	}

	public void setExibirColuna(boolean exibirColuna) {
		this.exibirColuna = exibirColuna;
	}
	

}
