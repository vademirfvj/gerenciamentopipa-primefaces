package opp.dto;

import java.sql.Date;

public class ParametrosRelatorio {
	
	private String funcionalidade;
	private String acao;
	private String usuario;
	private String obs;
	private Date data;
	
	private String nomePipeiro;
	private double capacidadePipa;
	private int qtdPessoas;
	private int qtdPA;
	private double qtqAgua;
	private int qtdViagens;
	private double qtdViagensIdeal;
	private int qtdViagensDiferenca;
	private int kmPecorrido;
	private double valorBruto;
	

	public ParametrosRelatorio(){
		
	}

	public String getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getNomePipeiro() {
		return nomePipeiro;
	}

	public void setNomePipeiro(String nomePipeiro) {
		this.nomePipeiro = nomePipeiro;
	}

	public double getCapacidadePipa() {
		return capacidadePipa;
	}

	public void setCapacidadePipa(double capacidadePipa) {
		this.capacidadePipa = capacidadePipa;
	}

	public int getQtdPessoas() {
		return qtdPessoas;
	}

	public void setQtdPessoas(int qtdPessoas) {
		this.qtdPessoas = qtdPessoas;
	}

	public int getQtdPA() {
		return qtdPA;
	}

	public void setQtdPA(int qtdPA) {
		this.qtdPA = qtdPA;
	}

	public double getQtqAgua() {
		return qtqAgua;
	}

	public void setQtqAgua(double qtqAgua) {
		this.qtqAgua = qtqAgua;
	}

	public int getQtdViagens() {
		return qtdViagens;
	}

	public void setQtdViagens(int qtdViagens) {
		this.qtdViagens = qtdViagens;
	}

	public double getQtdViagensIdeal() {
		return qtdViagensIdeal;
	}

	public void setQtdViagensIdeal(double qtdViagensIdeal) {
		this.qtdViagensIdeal = qtdViagensIdeal;
	}

	public int getQtdViagensDiferenca() {
		return qtdViagensDiferenca;
	}

	public void setQtdViagensDiferenca(int qtdViagensDiferenca) {
		this.qtdViagensDiferenca = qtdViagensDiferenca;
	}

	public int getKmPecorrido() {
		return kmPecorrido;
	}

	public void setKmPecorrido(int kmPecorrido) {
		this.kmPecorrido = kmPecorrido;
	}

	public double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(double valorBruto) {
		this.valorBruto = valorBruto;
	}
	
	
}
