package opp.dto;

import java.io.Serializable;

public class PontoAbastecimentoDTO implements Serializable {
	
	private static final long serialVersionUID = -3650433573303271529L;
	
	private int id;
	private String comunidade;
	private int populacao;
	private double distancia;
	private String momento;
	private int codcidade;
	private String nomeCidade;
	private String georeferenciamento;
	private double volume;
	private double asfalto;
	private double terra;
	private double indice;
	private String apontador;
	private String substituto;
	private String status;
	
	private int codGcda;
	private int litrosDiario;
	private int populacaoDefault;
	private int populacao5L;
	private int codIndice;
	
	private double qtdAgua;
	
	private double capacidadePipa;
	
	private double qtdViagensFormula;
	
	private int qtdViagensReal;
	
	private double valorBruto;
	
	private int kmPercorridoPA;
	
	private DistribuicaoDiasPipeiroDTO distribuicaoDiasPipeiroDTO;
	
	private int qtdCarradas;
	
	private String obs;
	
	private int reducao;
	
	public PontoAbastecimentoDTO(){
		distribuicaoDiasPipeiroDTO = new DistribuicaoDiasPipeiroDTO();
		this.obs = "";
		this.qtdViagensReal = new Integer(0);
	}
	
	public PontoAbastecimentoDTO(int id, String comunidade, int populacao,
			double distancia, String momento, int codcidade,
			String georeferenciamento, double volume, double asfalto,
			double terra, double indice, String apontador, String substituto,
			String status, int populacao5L, int populacaoDefault, int codGcda,int litrosDiario, int codIndice){
		
		this.id = id;
		this.comunidade = comunidade;
		this.populacao = populacao;
		this.distancia = distancia;
		this.momento = momento;
		this.codcidade = codcidade;
		this.georeferenciamento = georeferenciamento;
		this.volume = volume;
		this.asfalto = asfalto;
		this.terra = terra;
		this.indice = indice;
		this.apontador = apontador;
		this.substituto = substituto;
		this.status = status;
		this.populacao5L = populacao5L;
		this.populacaoDefault = populacaoDefault;
		this.codGcda = codGcda;
		this.litrosDiario = litrosDiario;
		this.codIndice = codIndice;
	}
	
	public PontoAbastecimentoDTO(String comunidade, int populacao,
			double distancia, String momento, int codcidade,
			String georeferenciamento, double volume, double asfalto,
			double terra, double indice, String apontador, String substituto,
			String status, int populacao5L, int populacaoDefault, int codGcda, int codIndice,int litrosDiario){
		
		this.comunidade = comunidade;
		this.populacao = populacao;
		this.distancia = distancia;
		this.momento = momento;
		this.codcidade = codcidade;
		this.georeferenciamento = georeferenciamento;
		this.volume = volume;
		this.asfalto = asfalto;
		this.terra = terra;
		this.indice = indice;
		this.apontador = apontador;
		this.substituto = substituto;
		this.status = status;
		this.populacao5L = populacao5L;
		this.populacaoDefault = populacaoDefault;
		this.codGcda = codGcda;
		this.codIndice = codIndice;
		this.litrosDiario = litrosDiario;
	}
	
	public PontoAbastecimentoDTO(int id, String comunidade, int populacao,
			double distancia, String momento, int codcidade,
			String georeferenciamento, double volume, double asfalto,
			double terra, double indice, String apontador, String substituto,
			String status) {
		this.id = id;
		this.comunidade = comunidade;
		this.populacao = populacao;
		this.distancia = distancia;
		this.momento = momento;
		this.codcidade = codcidade;
		this.georeferenciamento = georeferenciamento;
		this.volume = volume;
		this.asfalto = asfalto;
		this.terra = terra;
		this.indice = indice;
		this.apontador = apontador;
		this.substituto = substituto;
		this.status = status;
	}


	
	public PontoAbastecimentoDTO(String comunidade, int populacao,
			double distancia, String momento, int codcidade,
			String georeferenciamento, double volume, double asfalto,
			double terra, double indice, String apontador, String substituto,
			String status) {
		this.comunidade = comunidade;
		this.populacao = populacao;
		this.distancia = distancia;
		this.momento = momento;
		this.codcidade = codcidade;
		this.georeferenciamento = georeferenciamento;
		this.volume = volume;
		this.asfalto = asfalto;
		this.terra = terra;
		this.indice = indice;
		this.apontador = apontador;
		this.substituto = substituto;
		this.status = status;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCodcidade() {
		return codcidade;
	}
	public void setCodcidade(int codcidade) {
		this.codcidade = codcidade;
	}
	public String getGeoreferenciamento() {
		return georeferenciamento;
	}
	public void setGeoreferenciamento(String georeferenciamento) {
		this.georeferenciamento = georeferenciamento;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public int getPopulacao() {
		return populacao;
	}
	public void setPopulacao(int populacao) {
		this.populacao = populacao;
	}
	public double getAsfalto() {
		return asfalto;
	}
	public void setAsfalto(double asfalto) {
		this.asfalto = asfalto;
	}
	public double getTerra() {
		return terra;
	}
	public void setTerra(double terra) {
		this.terra = terra;
	}
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public String getMomento() {
		return momento;
	}
	public void setMomento(String momento) {
		this.momento = momento;
	}
	public double getIndice() {
		return indice;
	}
	public void setIndice(double indice) {
		this.indice = indice;
	}
	public String getApontador() {
		return apontador;
	}
	public void setApontador(String apontador) {
		this.apontador = apontador;
	}
	public String getSubstituto() {
		return substituto;
	}
	public void setSubstituto(String substituto) {
		this.substituto = substituto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public double getQtdAgua() {
		return qtdAgua;
	}

	public void setQtdAgua(double qtdAgua) {
		this.qtdAgua = qtdAgua;
	}

	public double getCapacidadePipa() {
		return capacidadePipa;
	}

	public void setCapacidadePipa(double capacidadePipa) {
		this.capacidadePipa = capacidadePipa;
	}

	public double getQtdViagensFormula() {
		return qtdViagensFormula;
	}

	public void setQtdViagensFormula(double qtdViagensFormula) {
		this.qtdViagensFormula = qtdViagensFormula;
	}

	public int getQtdViagensReal() {
		return qtdViagensReal;
	}

	public void setQtdViagensReal(int qtdViagensReal) {
		this.qtdViagensReal = qtdViagensReal;
	}

	public double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public int getKmPercorridoPA() {
		return kmPercorridoPA;
	}

	public void setKmPercorridoPA(int kmPercorridoPA) {
		this.kmPercorridoPA = kmPercorridoPA;
	}

	public DistribuicaoDiasPipeiroDTO getDistribuicaoDiasPipeiroDTO() {
		return distribuicaoDiasPipeiroDTO;
	}

	public void setDistribuicaoDiasPipeiroDTO(
			DistribuicaoDiasPipeiroDTO distribuicaoDiasPipeiroDTO) {
		this.distribuicaoDiasPipeiroDTO = distribuicaoDiasPipeiroDTO;
	}

	public int getQtdCarradas() {
		return qtdCarradas;
	}

	public void setQtdCarradas(int qtdCarradas) {
		this.qtdCarradas = qtdCarradas;
	}

	public String getComunidade() {
		return comunidade;
	}

	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public int getReducao() {
		return reducao;
	}

	public void setReducao(int reducao) {
		this.reducao = reducao;
	}

	public int getCodGcda() {
		return codGcda;
	}

	public void setCodGcda(int codGcda) {
		this.codGcda = codGcda;
	}

	public int getLitrosDiario() {
		return litrosDiario;
	}

	public void setLitrosDiario(int litrosDiario) {
		this.litrosDiario = litrosDiario;
	}

	public int getPopulacaoDefault() {
		return populacaoDefault;
	}

	public void setPopulacaoDefault(int populacaoDefault) {
		this.populacaoDefault = populacaoDefault;
	}

	public int getPopulacao5L() {
		return populacao5L;
	}

	public void setPopulacao5L(int populacao5l) {
		populacao5L = populacao5l;
	}

	public int getCodIndice() {
		return codIndice;
	}

	public void setCodIndice(int codIndice) {
		this.codIndice = codIndice;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}
	
	
	
}
