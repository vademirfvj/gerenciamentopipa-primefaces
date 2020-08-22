package opp.dto;

import java.io.Serializable;

public class VeiculoDTO implements Serializable {
	
	private static final long serialVersionUID = 6358157450124466438L;
	
	private int id;
	private String placa;
	private String renavam;
	private double capacidadePipa;
	private String marcaModelo;
	private String status;
	
	public VeiculoDTO() {
	}
	
	public VeiculoDTO(String placa, String renavam, double capacidadePipa,
			String marcaModelo, String status) {
		this.placa = placa;
		this.renavam = renavam;
		this.capacidadePipa = capacidadePipa;
		this.marcaModelo = marcaModelo;
		this.status = status;
	}



	public VeiculoDTO(int id, String placa, String renavam,
			double capacidadePipa, String marcaModelo, String status) {
		super();
		this.id = id;
		this.placa = placa;
		this.renavam = renavam;
		this.capacidadePipa = capacidadePipa;
		this.marcaModelo = marcaModelo;
		this.status = status;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getRenavam() {
		return renavam;
	}
	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}
	public double getCapacidadePipa() {
		return capacidadePipa;
	}
	public void setCapacidadePipa(double capacidadePipa) {
		this.capacidadePipa = capacidadePipa;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
