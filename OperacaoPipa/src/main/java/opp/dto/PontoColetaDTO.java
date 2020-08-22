package opp.dto;

import java.io.Serializable;

public class PontoColetaDTO implements Serializable  {
	
	private static final long serialVersionUID = -2309165719532242638L;
	
	private int id;
	private String pontoColeta;
	private String geoLocalizacao;
	private String status;
	private int codGcda;
	
	
	public PontoColetaDTO(){
		
	}
	
	public PontoColetaDTO(int id, String pontoColeta, String geoLocalizacao, String status) {
		this.id = id;
		this.pontoColeta = pontoColeta;
		this.geoLocalizacao = geoLocalizacao;
		this.status = status;
	}
	
	public PontoColetaDTO(String pontoColeta, String geoLocalizacao, String status) {
		this.pontoColeta = pontoColeta;
		this.geoLocalizacao = geoLocalizacao;
		this.status = status;
	}
	
	public PontoColetaDTO(String pontoColeta, String geoLocalizacao, String status, int codGcda) {
		this.pontoColeta = pontoColeta;
		this.geoLocalizacao = geoLocalizacao;
		this.status = status;
		this.codGcda = codGcda;
	}

	public PontoColetaDTO(int id, String pontoColeta, String geoLocalizacao, String status, int codGcda) {
		this.id = id;
		this.pontoColeta = pontoColeta;
		this.geoLocalizacao = geoLocalizacao;
		this.status = status;
		this.codGcda = codGcda;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPontoColeta() {
		return pontoColeta;
	}

	public void setPontoColeta(String pontoColeta) {
		this.pontoColeta = pontoColeta;
	}

	public String getGeoLocalizacao() {
		return geoLocalizacao;
	}

	public void setGeoLocalizacao(String geoLocalizacao) {
		this.geoLocalizacao = geoLocalizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCodGcda() {
		return codGcda;
	}

	public void setCodGcda(int codGcda) {
		this.codGcda = codGcda;
	}

}
