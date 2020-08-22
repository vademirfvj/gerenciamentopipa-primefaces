package opp.dto;

public class InteracaoPipeiroPADTO {
	
	private PontoAbastecimentoDTO pontoAbastecimentoDTO;
	private double totalQtdAgua;
	private double totalViagemFormula;
	private int totalviagemReal;
	private double valorBruto;
	private int kmPecorrido;
	private String obs;
	private double indice;
	
	
	public InteracaoPipeiroPADTO() {
		pontoAbastecimentoDTO = new PontoAbastecimentoDTO();
	}
	
	public PontoAbastecimentoDTO getPontoAbastecimentoDTO() {
		return pontoAbastecimentoDTO;
	}
	public void setPontoAbastecimentoDTO(PontoAbastecimentoDTO pontoAbastecimentoDTO) {
		this.pontoAbastecimentoDTO = pontoAbastecimentoDTO;
	}
	public double getTotalQtdAgua() {
		return totalQtdAgua;
	}
	public void setTotalQtdAgua(double totalQtdAgua) {
		this.totalQtdAgua = totalQtdAgua;
	}
	public double getTotalViagemFormula() {
		return totalViagemFormula;
	}
	public void setTotalViagemFormula(double totalViagemFormula) {
		this.totalViagemFormula = totalViagemFormula;
	}
	public int getTotalviagemReal() {
		return totalviagemReal;
	}
	public void setTotalviagemReal(int totalviagemReal) {
		this.totalviagemReal = totalviagemReal;
	}
	public double getValorBruto() {
		return valorBruto;
	}
	public void setValorBruto(double valorBruto) {
		this.valorBruto = valorBruto;
	}
	public int getKmPecorrido() {
		return kmPecorrido;
	}
	public void setKmPecorrido(int kmPecorrido) {
		this.kmPecorrido = kmPecorrido;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public double getIndice() {
		return indice;
	}
	public void setIndice(double indice) {
		this.indice = indice;
	}
	
}
