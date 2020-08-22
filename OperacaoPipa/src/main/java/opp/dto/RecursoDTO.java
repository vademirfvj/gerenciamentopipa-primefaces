package opp.dto;

public class RecursoDTO {
	
	private int id;
	private String nc;
	private int mes;
	private int ano;
	private double valor;
	private String status;
	
	
	public RecursoDTO(){
		
	}
	
	public RecursoDTO(int id, String nc, int mes, int ano, double valor,String status) {
		super();
		this.id = id;
		this.nc = nc;
		this.mes = mes;
		this.ano = ano;
		this.valor = valor;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
