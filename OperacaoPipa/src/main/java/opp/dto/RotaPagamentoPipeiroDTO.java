package opp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RotaPagamentoPipeiroDTO implements Serializable {
	
	
	private static final long serialVersionUID = -6372708818443335727L;

	private List pontoAbastecimentoDTOList;
	
	private CidadeDTO cidadeDTO;
	
	private PipeiroDTO pipeiroDTO;
	
	private int numDiasMes;
	
	private int litrosPessoa;
	
	private double valorTotalPA;
	
	private int kmTotal;
	
	private double totalViagensFormula;
	
	private int totalApontadores;
	
	private int totalPessoas;
	
	private double totalQtdAgua;
	
	private int totalViagensReal;
	
	private int totalDistacia;
	
	private String mesAno;
	
	private int totalDia1;
	private int totalDia2;
	private int totalDia3;
	private int totalDia4;
	private int totalDia5;
	private int totalDia6;
	private int totalDia7;
	private int totalDia8;
	private int totalDia9;
	private int totalDia10;
	private int totalDia11;
	private int totalDia12;
	private int totalDia13;
	private int totalDia14;
	private int totalDia15;
	private int totalDia16;
	private int totalDia17;
	private int totalDia18;
	private int totalDia19;
	private int totalDia20;
	private int totalDia21;
	private int totalDia22;
	private int totalDia23;
	private int totalDia24;
	private int totalDia25;
	private int totalDia26;
	private int totalDia27;
	private int totalDia28;
	private int totalDia29;
	private int totalDia30;
	private int totalDia31;
	
	private int distribuicaoDiasOK;
	
	
	public RotaPagamentoPipeiroDTO(){
		cidadeDTO = new CidadeDTO();
		pipeiroDTO = new PipeiroDTO();
		pontoAbastecimentoDTOList = new ArrayList();
		totalViagensFormula = new Double(0);
		totalApontadores = new Integer(0);
		totalPessoas = new Integer(0);
		totalQtdAgua = new Double(0);
		totalViagensReal = new Integer(0);
		totalDistacia = new Integer(0);
		distribuicaoDiasOK = new Integer(0);
	}


	public List getPontoAbastecimentoDTOList() {
		return pontoAbastecimentoDTOList;
	}

	public void setPontoAbastecimentoDTOList(List pontoAbastecimentoDTOList) {
		this.pontoAbastecimentoDTOList = pontoAbastecimentoDTOList;
	}



	public CidadeDTO getCidadeDTO() {
		return cidadeDTO;
	}

	public void setCidadeDTO(CidadeDTO cidadeDTO) {
		this.cidadeDTO = cidadeDTO;
	}

	public PipeiroDTO getPipeiroDTO() {
		return pipeiroDTO;
	}

	public void setPipeiroDTO(PipeiroDTO pipeiroDTO) {
		this.pipeiroDTO = pipeiroDTO;
	}
	
	public int getNumDiasMes() {
		return numDiasMes;
	}

	public void setNumDiasMes(int numDiasMes) {
		this.numDiasMes = numDiasMes;
	}

	public int getLitrosPessoa() {
		return litrosPessoa;
	}

	public void setLitrosPessoa(int litrosPessoa) {
		this.litrosPessoa = litrosPessoa;
	}

	public int getKmTotal() {
		return kmTotal;
	}

	public void setKmTotal(int kmTotal) {
		this.kmTotal = kmTotal;
	}


	public double getValorTotalPA() {
		return valorTotalPA;
	}


	public void setValorTotalPA(double valorTotalPA) {
		this.valorTotalPA = valorTotalPA;
	}


	public double getTotalViagensFormula() {
		return totalViagensFormula;
	}


	public void setTotalViagensFormula(double totalViagensFormula) {
		this.totalViagensFormula = totalViagensFormula;
	}


	public int getTotalApontadores() {
		return totalApontadores;
	}


	public void setTotalApontadores(int totalApontadores) {
		this.totalApontadores = totalApontadores;
	}


	public int getTotalPessoas() {
		return totalPessoas;
	}


	public void setTotalPessoas(int totalPessoas) {
		this.totalPessoas = totalPessoas;
	}


	public double getTotalQtdAgua() {
		return totalQtdAgua;
	}


	public void setTotalQtdAgua(double totalQtdAgua) {
		this.totalQtdAgua = totalQtdAgua;
	}


	public int getTotalViagensReal() {
		return totalViagensReal;
	}


	public void setTotalViagensReal(int totalViagensReal) {
		this.totalViagensReal = totalViagensReal;
	}


	public int getTotalDistacia() {
		return totalDistacia;
	}


	public void setTotalDistacia(int totalDistacia) {
		this.totalDistacia = totalDistacia;
	}


	public String getMesAno() {
		return mesAno;
	}


	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}


	public int getTotalDia1() {
		return totalDia1;
	}


	public void setTotalDia1(int totalDia1) {
		this.totalDia1 = totalDia1;
	}


	public int getTotalDia2() {
		return totalDia2;
	}


	public void setTotalDia2(int totalDia2) {
		this.totalDia2 = totalDia2;
	}


	public int getTotalDia3() {
		return totalDia3;
	}


	public void setTotalDia3(int totalDia3) {
		this.totalDia3 = totalDia3;
	}


	public int getTotalDia4() {
		return totalDia4;
	}


	public void setTotalDia4(int totalDia4) {
		this.totalDia4 = totalDia4;
	}


	public int getTotalDia5() {
		return totalDia5;
	}


	public void setTotalDia5(int totalDia5) {
		this.totalDia5 = totalDia5;
	}


	public int getTotalDia6() {
		return totalDia6;
	}


	public void setTotalDia6(int totalDia6) {
		this.totalDia6 = totalDia6;
	}


	public int getTotalDia7() {
		return totalDia7;
	}


	public void setTotalDia7(int totalDia7) {
		this.totalDia7 = totalDia7;
	}


	public int getTotalDia8() {
		return totalDia8;
	}


	public void setTotalDia8(int totalDia8) {
		this.totalDia8 = totalDia8;
	}


	public int getTotalDia9() {
		return totalDia9;
	}


	public void setTotalDia9(int totalDia9) {
		this.totalDia9 = totalDia9;
	}


	public int getTotalDia10() {
		return totalDia10;
	}


	public void setTotalDia10(int totalDia10) {
		this.totalDia10 = totalDia10;
	}


	public int getTotalDia11() {
		return totalDia11;
	}


	public void setTotalDia11(int totalDia11) {
		this.totalDia11 = totalDia11;
	}


	public int getTotalDia12() {
		return totalDia12;
	}


	public void setTotalDia12(int totalDia12) {
		this.totalDia12 = totalDia12;
	}


	public int getTotalDia13() {
		return totalDia13;
	}


	public void setTotalDia13(int totalDia13) {
		this.totalDia13 = totalDia13;
	}


	public int getTotalDia14() {
		return totalDia14;
	}


	public void setTotalDia14(int totalDia14) {
		this.totalDia14 = totalDia14;
	}


	public int getTotalDia15() {
		return totalDia15;
	}


	public void setTotalDia15(int totalDia15) {
		this.totalDia15 = totalDia15;
	}


	public int getTotalDia16() {
		return totalDia16;
	}


	public void setTotalDia16(int totalDia16) {
		this.totalDia16 = totalDia16;
	}


	public int getTotalDia17() {
		return totalDia17;
	}


	public void setTotalDia17(int totalDia17) {
		this.totalDia17 = totalDia17;
	}


	public int getTotalDia18() {
		return totalDia18;
	}


	public void setTotalDia18(int totalDia18) {
		this.totalDia18 = totalDia18;
	}


	public int getTotalDia19() {
		return totalDia19;
	}


	public void setTotalDia19(int totalDia19) {
		this.totalDia19 = totalDia19;
	}


	public int getTotalDia20() {
		return totalDia20;
	}


	public void setTotalDia20(int totalDia20) {
		this.totalDia20 = totalDia20;
	}


	public int getTotalDia21() {
		return totalDia21;
	}


	public void setTotalDia21(int totalDia21) {
		this.totalDia21 = totalDia21;
	}


	public int getTotalDia22() {
		return totalDia22;
	}


	public void setTotalDia22(int totalDia22) {
		this.totalDia22 = totalDia22;
	}


	public int getTotalDia23() {
		return totalDia23;
	}


	public void setTotalDia23(int totalDia23) {
		this.totalDia23 = totalDia23;
	}


	public int getTotalDia24() {
		return totalDia24;
	}


	public void setTotalDia24(int totalDia24) {
		this.totalDia24 = totalDia24;
	}


	public int getTotalDia25() {
		return totalDia25;
	}


	public void setTotalDia25(int totalDia25) {
		this.totalDia25 = totalDia25;
	}


	public int getTotalDia26() {
		return totalDia26;
	}


	public void setTotalDia26(int totalDia26) {
		this.totalDia26 = totalDia26;
	}


	public int getTotalDia27() {
		return totalDia27;
	}


	public void setTotalDia27(int totalDia27) {
		this.totalDia27 = totalDia27;
	}


	public int getTotalDia28() {
		return totalDia28;
	}


	public void setTotalDia28(int totalDia28) {
		this.totalDia28 = totalDia28;
	}


	public int getTotalDia29() {
		return totalDia29;
	}


	public void setTotalDia29(int totalDia29) {
		this.totalDia29 = totalDia29;
	}


	public int getTotalDia30() {
		return totalDia30;
	}


	public void setTotalDia30(int totalDia30) {
		this.totalDia30 = totalDia30;
	}


	public int getTotalDia31() {
		return totalDia31;
	}


	public void setTotalDia31(int totalDia31) {
		this.totalDia31 = totalDia31;
	}

	public int getDistribuicaoDiasOK() {
		return distribuicaoDiasOK;
	}

	public void setDistribuicaoDiasOK(int distribuicaoDiasOK) {
		this.distribuicaoDiasOK = distribuicaoDiasOK;
	}
	
}
