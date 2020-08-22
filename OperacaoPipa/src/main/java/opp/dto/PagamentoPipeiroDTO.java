package opp.dto;

import java.io.Serializable;

public class PagamentoPipeiroDTO implements Serializable {
	
	private static final long serialVersionUID = 7709056585243459321L;
	
	private String pontoAbastecimento;
	private String Apontador;
	private double momento;
	private int qtPessoas;
	private double qtAgua;
	private double capacPipa;
	private double quantViagensFormula;
	private int quantViagensReal;
	private int distanciaPcPa;
	private double valorBrutoPa;
	private int numDiasMes;
	private int litrosPorPessoa;
	private int KmPercorrido;
	private int totalPessoas;
	private int totalQtAgua;
	private double totalQuantViagensFormula;
	private int totalQuantViagensReal;
	private int totalDistanciaPcPa;
	private double totalValorBrutoPa;
	
	//Dias para distribuicao
	private String dia1;
	private String dia2;
	private String dia3;
	private String dia4;
	private String dia5;
	private String dia6;
	private String dia7;
	private String dia8;
	private String dia9;
	private String dia10;
	private String dia11;
	private String dia12;
	private String dia13;
	private String dia14;
	private String dia15;
	private String dia16;
	private String dia17;
	private String dia18;
	private String dia19;
	private String dia20;
	private String dia21;
	private String dia22;
	private String dia23;
	private String dia24;
	private String dia25;
	private String dia26;
	private String dia27;
	private String dia28;
	private String dia29;
	private String dia30;
	private String dia31;
	
	public PagamentoPipeiroDTO(String pontoAbastecimento, String apontador,
			double momento, int qtPessoas, double qtAgua, double capacPipa,
			double quantViagensFormula, int quantViagensReal,
			int distanciaPcPa, double valorBrutoPa, int numDiasMes,
			int litrosPorPessoa, int kmPercorrido, int totalPessoas,
			int totalQtAgua, double totalQuantViagensFormula,
			int totalQuantViagensReal, int totalDistanciaPcPa,
			double totalValorBrutoPa) {
		super();
		this.pontoAbastecimento = pontoAbastecimento;
		Apontador = apontador;
		this.momento = momento;
		this.qtPessoas = qtPessoas;
		this.qtAgua = qtAgua;
		this.capacPipa = capacPipa;
		this.quantViagensFormula = quantViagensFormula;
		this.quantViagensReal = quantViagensReal;
		this.distanciaPcPa = distanciaPcPa;
		this.valorBrutoPa = valorBrutoPa;
		this.numDiasMes = numDiasMes;
		this.litrosPorPessoa = litrosPorPessoa;
		KmPercorrido = kmPercorrido;
		this.totalPessoas = totalPessoas;
		this.totalQtAgua = totalQtAgua;
		this.totalQuantViagensFormula = totalQuantViagensFormula;
		this.totalQuantViagensReal = totalQuantViagensReal;
		this.totalDistanciaPcPa = totalDistanciaPcPa;
		this.totalValorBrutoPa = totalValorBrutoPa;
	}
	public String getPontoAbastecimento() {
		return pontoAbastecimento;
	}
	public void setPontoAbastecimento(String pontoAbastecimento) {
		this.pontoAbastecimento = pontoAbastecimento;
	}
	public String getApontador() {
		return Apontador;
	}
	public void setApontador(String apontador) {
		Apontador = apontador;
	}
	public double getMomento() {
		return momento;
	}
	public void setMomento(double momento) {
		this.momento = momento;
	}
	public int getQtPessoas() {
		return qtPessoas;
	}
	public void setQtPessoas(int qtPessoas) {
		this.qtPessoas = qtPessoas;
	}
	public double getQtAgua() {
		return qtAgua;
	}
	public void setQtAgua(double qtAgua) {
		this.qtAgua = qtAgua;
	}
	public double getCapacPipa() {
		return capacPipa;
	}
	public void setCapacPipa(double capacPipa) {
		this.capacPipa = capacPipa;
	}
	public double getQuantViagensFormula() {
		return quantViagensFormula;
	}
	public void setQuantViagensFormula(double quantViagensFormula) {
		this.quantViagensFormula = quantViagensFormula;
	}
	public int getQuantViagensReal() {
		return quantViagensReal;
	}
	public void setQuantViagensReal(int quantViagensReal) {
		this.quantViagensReal = quantViagensReal;
	}
	public int getDistanciaPcPa() {
		return distanciaPcPa;
	}
	public void setDistanciaPcPa(int distanciaPcPa) {
		this.distanciaPcPa = distanciaPcPa;
	}
	public double getValorBrutoPa() {
		return valorBrutoPa;
	}
	public void setValorBrutoPa(double valorBrutoPa) {
		this.valorBrutoPa = valorBrutoPa;
	}
	public int getNumDiasMes() {
		return numDiasMes;
	}
	public void setNumDiasMes(int numDiasMes) {
		this.numDiasMes = numDiasMes;
	}
	public int getLitrosPorPessoa() {
		return litrosPorPessoa;
	}
	public void setLitrosPorPessoa(int litrosPorPessoa) {
		this.litrosPorPessoa = litrosPorPessoa;
	}
	public int getKmPercorrido() {
		return KmPercorrido;
	}
	public void setKmPercorrido(int kmPercorrido) {
		KmPercorrido = kmPercorrido;
	}
	public int getTotalPessoas() {
		return totalPessoas;
	}
	public void setTotalPessoas(int totalPessoas) {
		this.totalPessoas = totalPessoas;
	}
	public int getTotalQtAgua() {
		return totalQtAgua;
	}
	public void setTotalQtAgua(int totalQtAgua) {
		this.totalQtAgua = totalQtAgua;
	}
	public double getTotalQuantViagensFormula() {
		return totalQuantViagensFormula;
	}
	public void setTotalQuantViagensFormula(double totalQuantViagensFormula) {
		this.totalQuantViagensFormula = totalQuantViagensFormula;
	}
	public int getTotalQuantViagensReal() {
		return totalQuantViagensReal;
	}
	public void setTotalQuantViagensReal(int totalQuantViagensReal) {
		this.totalQuantViagensReal = totalQuantViagensReal;
	}
	public int getTotalDistanciaPcPa() {
		return totalDistanciaPcPa;
	}
	public void setTotalDistanciaPcPa(int totalDistanciaPcPa) {
		this.totalDistanciaPcPa = totalDistanciaPcPa;
	}
	public double getTotalValorBrutoPa() {
		return totalValorBrutoPa;
	}
	public void setTotalValorBrutoPa(double totalValorBrutoPa) {
		this.totalValorBrutoPa = totalValorBrutoPa;
	}
	public String getDia1() {
		return dia1;
	}
	public void setDia1(String dia1) {
		this.dia1 = dia1;
	}
	public String getDia2() {
		return dia2;
	}
	public void setDia2(String dia2) {
		this.dia2 = dia2;
	}
	public String getDia3() {
		return dia3;
	}
	public void setDia3(String dia3) {
		this.dia3 = dia3;
	}
	public String getDia4() {
		return dia4;
	}
	public void setDia4(String dia4) {
		this.dia4 = dia4;
	}
	public String getDia5() {
		return dia5;
	}
	public void setDia5(String dia5) {
		this.dia5 = dia5;
	}
	public String getDia6() {
		return dia6;
	}
	public void setDia6(String dia6) {
		this.dia6 = dia6;
	}
	public String getDia7() {
		return dia7;
	}
	public void setDia7(String dia7) {
		this.dia7 = dia7;
	}
	public String getDia8() {
		return dia8;
	}
	public void setDia8(String dia8) {
		this.dia8 = dia8;
	}
	public String getDia9() {
		return dia9;
	}
	public void setDia9(String dia9) {
		this.dia9 = dia9;
	}
	public String getDia10() {
		return dia10;
	}
	public void setDia10(String dia10) {
		this.dia10 = dia10;
	}
	public String getDia11() {
		return dia11;
	}
	public void setDia11(String dia11) {
		this.dia11 = dia11;
	}
	public String getDia12() {
		return dia12;
	}
	public void setDia12(String dia12) {
		this.dia12 = dia12;
	}
	public String getDia13() {
		return dia13;
	}
	public void setDia13(String dia13) {
		this.dia13 = dia13;
	}
	public String getDia14() {
		return dia14;
	}
	public void setDia14(String dia14) {
		this.dia14 = dia14;
	}
	public String getDia15() {
		return dia15;
	}
	public void setDia15(String dia15) {
		this.dia15 = dia15;
	}
	public String getDia16() {
		return dia16;
	}
	public void setDia16(String dia16) {
		this.dia16 = dia16;
	}
	public String getDia17() {
		return dia17;
	}
	public void setDia17(String dia17) {
		this.dia17 = dia17;
	}
	public String getDia18() {
		return dia18;
	}
	public void setDia18(String dia18) {
		this.dia18 = dia18;
	}
	public String getDia19() {
		return dia19;
	}
	public void setDia19(String dia19) {
		this.dia19 = dia19;
	}
	public String getDia20() {
		return dia20;
	}
	public void setDia20(String dia20) {
		this.dia20 = dia20;
	}
	public String getDia21() {
		return dia21;
	}
	public void setDia21(String dia21) {
		this.dia21 = dia21;
	}
	public String getDia22() {
		return dia22;
	}
	public void setDia22(String dia22) {
		this.dia22 = dia22;
	}
	public String getDia23() {
		return dia23;
	}
	public void setDia23(String dia23) {
		this.dia23 = dia23;
	}
	public String getDia24() {
		return dia24;
	}
	public void setDia24(String dia24) {
		this.dia24 = dia24;
	}
	public String getDia25() {
		return dia25;
	}
	public void setDia25(String dia25) {
		this.dia25 = dia25;
	}
	public String getDia26() {
		return dia26;
	}
	public void setDia26(String dia26) {
		this.dia26 = dia26;
	}
	public String getDia27() {
		return dia27;
	}
	public void setDia27(String dia27) {
		this.dia27 = dia27;
	}
	public String getDia28() {
		return dia28;
	}
	public void setDia28(String dia28) {
		this.dia28 = dia28;
	}
	public String getDia29() {
		return dia29;
	}
	public void setDia29(String dia29) {
		this.dia29 = dia29;
	}
	public String getDia30() {
		return dia30;
	}
	public void setDia30(String dia30) {
		this.dia30 = dia30;
	}
	public String getDia31() {
		return dia31;
	}
	public void setDia31(String dia31) {
		this.dia31 = dia31;
	}
	
	
}
