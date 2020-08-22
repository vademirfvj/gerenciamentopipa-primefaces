package opp.dto;

import java.io.Serializable;

public class InteracaoDistribuicaoDTO implements Serializable  {
	
	private static final long serialVersionUID = 3076308164857891828L;
	
	private int totalApontadores;
	private int codHistoricoDistr;
	private PipeiroDTO pipeiroDTO;
	private PontoColetaDTO pontoColetaDTO;
	private VeiculoDTO veiculoDTO;
	private CidadeDTO cidadeDTO;
	private int qtdDias;
	private int qtdLitrosPessoa;
	private double valorTotal;
	private int kmTotal;
	private double totalViagensFormula;
	private int totalViagensReal;
	private int totalPessoas;
	private double totalQdtAgua;
	private int totalDistancia;
	
	
	
	public InteracaoDistribuicaoDTO(){
		pipeiroDTO = new PipeiroDTO();
		pontoColetaDTO = new PontoColetaDTO();
		veiculoDTO = new VeiculoDTO();
		cidadeDTO = new CidadeDTO();
	}

	public int getTotalApontadores() {
		return totalApontadores;
	}

	public void setTotalApontadores(int totalApontadores) {
		this.totalApontadores = totalApontadores;
	}

	public int getCodHistoricoDistr() {
		return codHistoricoDistr;
	}

	public void setCodHistoricoDistr(int codHistoricoDistr) {
		this.codHistoricoDistr = codHistoricoDistr;
	}

	public PipeiroDTO getPipeiroDTO() {
		return pipeiroDTO;
	}

	public void setPipeiroDTO(PipeiroDTO pipeiroDTO) {
		this.pipeiroDTO = pipeiroDTO;
	}

	public PontoColetaDTO getPontoColetaDTO() {
		return pontoColetaDTO;
	}

	public void setPontoColetaDTO(PontoColetaDTO pontoColetaDTO) {
		this.pontoColetaDTO = pontoColetaDTO;
	}

	public VeiculoDTO getVeiculoDTO() {
		return veiculoDTO;
	}

	public void setVeiculoDTO(VeiculoDTO veiculoDTO) {
		this.veiculoDTO = veiculoDTO;
	}

	public int getQtdDias() {
		return qtdDias;
	}

	public void setQtdDias(int qtdDias) {
		this.qtdDias = qtdDias;
	}

	public int getQtdLitrosPessoa() {
		return qtdLitrosPessoa;
	}

	public void setQtdLitrosPessoa(int qtdLitrosPessoa) {
		this.qtdLitrosPessoa = qtdLitrosPessoa;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public int getKmTotal() {
		return kmTotal;
	}

	public void setKmTotal(int kmTotal) {
		this.kmTotal = kmTotal;
	}

	public double getTotalViagensFormula() {
		return totalViagensFormula;
	}

	public void setTotalViagensFormula(double totalViagensFormula) {
		this.totalViagensFormula = totalViagensFormula;
	}

	public int getTotalViagensReal() {
		return totalViagensReal;
	}

	public void setTotalViagensReal(int totalViagensReal) {
		this.totalViagensReal = totalViagensReal;
	}

	public int getTotalPessoas() {
		return totalPessoas;
	}

	public void setTotalPessoas(int totalPessoas) {
		this.totalPessoas = totalPessoas;
	}

	public double getTotalQdtAgua() {
		return totalQdtAgua;
	}

	public void setTotalQdtAgua(double totalQdtAgua) {
		this.totalQdtAgua = totalQdtAgua;
	}

	public int getTotalDistancia() {
		return totalDistancia;
	}

	public void setTotalDistancia(int totalDistancia) {
		this.totalDistancia = totalDistancia;
	}

	public CidadeDTO getCidadeDTO() {
		return cidadeDTO;
	}

	public void setCidadeDTO(CidadeDTO cidadeDTO) {
		this.cidadeDTO = cidadeDTO;
	}
	
}
