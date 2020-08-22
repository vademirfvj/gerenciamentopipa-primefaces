package opp.dto;

import java.io.Serializable;

public class CidadeDTO implements Serializable  {
	
	private static final long serialVersionUID = 3076308164857891828L;
	
	private int id;
	private int codPontoColeta;
	private String cidade;   
	private String uf;       
	private String geolocalizacao;
	private int numHabitantes;
	private String email;    
	private int carr_max;     
	private String coordSopMunicipal;
	private String foneSopMunicipal; 
	private int numPipeiros; 
	private String status;

	//Flag que indica se no mes e ano selecionado já ocorreu distribuição 1 - SIM 0 - NÃO
	private int flagDistribuicaoOk;	

	private PontoColetaDTO pontoColetaDTO;
	
	public CidadeDTO(){
		pontoColetaDTO = new PontoColetaDTO();
		
	}
	
	public CidadeDTO(int codPontoColeta, String cidade, String uf,
			String geolocalizacao, int numHabitantes, String email,
			int carr_max, String coordSopMunicipal, String foneSopMunicipal,
			int numPipeiros, String status) {
		this.codPontoColeta = codPontoColeta;
		this.cidade = cidade;
		this.uf = uf;
		this.geolocalizacao = geolocalizacao;
		this.numHabitantes = numHabitantes;
		this.email = email;
		this.carr_max = carr_max;
		this.coordSopMunicipal = coordSopMunicipal;
		this.foneSopMunicipal = foneSopMunicipal;
		this.numPipeiros = numPipeiros;
		this.status = status;
	}
	
	public CidadeDTO(int id, int codPontoColeta, String cidade, String uf,
			String geolocalizacao, int numHabitantes, String email,
			int carr_max, String coordSopMunicipal, String foneSopMunicipal,
			int numPipeiros, String status) {
		this.id = id;
		this.codPontoColeta = codPontoColeta;
		this.cidade = cidade;
		this.uf = uf;
		this.geolocalizacao = geolocalizacao;
		this.numHabitantes = numHabitantes;
		this.email = email;
		this.carr_max = carr_max;
		this.coordSopMunicipal = coordSopMunicipal;
		this.foneSopMunicipal = foneSopMunicipal;
		this.numPipeiros = numPipeiros;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodPontoColeta() {
		return codPontoColeta;
	}

	public void setCodPontoColeta(int codPontoColeta) {
		this.codPontoColeta = codPontoColeta;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getGeolocalizacao() {
		return geolocalizacao;
	}

	public void setGeolocalizacao(String geolocalizacao) {
		this.geolocalizacao = geolocalizacao;
	}

	public int getNumHabitantes() {
		return numHabitantes;
	}

	public void setNumHabitantes(int numHabitantes) {
		this.numHabitantes = numHabitantes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCarr_max() {
		return carr_max;
	}

	public void setCarr_max(int carr_max) {
		this.carr_max = carr_max;
	}

	public String getCoordSopMunicipal() {
		return coordSopMunicipal;
	}

	public void setCoordSopMunicipal(String coordSopMunicipal) {
		this.coordSopMunicipal = coordSopMunicipal;
	}

	public String getFoneSopMunicipal() {
		return foneSopMunicipal;
	}

	public void setFoneSopMunicipal(String foneSopMunicipal) {
		this.foneSopMunicipal = foneSopMunicipal;
	}

	public int getNumPipeiros() {
		return numPipeiros;
	}

	public void setNumPipeiros(int numPipeiros) {
		this.numPipeiros = numPipeiros;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PontoColetaDTO getPontoColetaDTO() {
		return pontoColetaDTO;
	}

	public void setPontoColetaDTO(PontoColetaDTO pontoColetaDTO) {
		this.pontoColetaDTO = pontoColetaDTO;
	}  
	
	public int getFlagDistribuicaoOk() {
		return flagDistribuicaoOk;
	}

	public void setFlagDistribuicaoOk(int flagDistribuicaoOk) {
		this.flagDistribuicaoOk = flagDistribuicaoOk;
	} 
	
}
