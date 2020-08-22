package opp.dto;

import java.io.Serializable;
import java.util.Date;

public class PipeiroDTO implements Serializable {

	private static final long serialVersionUID = 8831177556593787529L;
	
	private int id;
	private String nome;
	private int codCidade;
	private String nomeCidade;
	private int cnh;
	private Date validadeCnh;
	private int identidade;
	private String orgaoEmissor;
	private String nit;
	private String cpf;
	private String codBanco;
	private String banco;
	private String agencia;
	private String conta;
	private int codVeiculo;
	private double saldoSiaf;
	private double recursoAno;
	private double restoPagar;
	private String status;
	
	private VeiculoDTO veiculoDTO;
	
	public PipeiroDTO(){
		veiculoDTO = new VeiculoDTO();
		
	}

	public PipeiroDTO(String nome, int codCidade, int cnh,
			Date validadeCnh, int identidade, String orgaoEmissor, String nit,
			String cpf, String codBanco, String banco, String agencia,
			String conta, int codVeiculo, double saldoSiaf, double recursoAno,
			double restoPagar, String status) {
		super();
		this.nome = nome;
		this.codCidade = codCidade;
		this.cnh = cnh;
		this.validadeCnh = validadeCnh;
		this.identidade = identidade;
		this.orgaoEmissor = orgaoEmissor;
		this.nit = nit;
		this.cpf = cpf;
		this.codBanco = codBanco;
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.codVeiculo = codVeiculo;
		this.saldoSiaf = saldoSiaf;
		this.recursoAno = recursoAno;
		this.restoPagar = restoPagar;
		this.status = status;
	}
	
	public PipeiroDTO(int id, String nome, int codCidade, int cnh,
			Date validadeCnh, int identidade, String orgaoEmissor, String nit,
			String cpf, String codBanco, String banco, String agencia,
			String conta, int codVeiculo, double saldoSiaf, double recursoAno,
			double restoPagar, String status) {
		super();
		this.id = id;
		this.nome = nome;
		this.codCidade = codCidade;
		this.cnh = cnh;
		this.validadeCnh = validadeCnh;
		this.identidade = identidade;
		this.orgaoEmissor = orgaoEmissor;
		this.nit = nit;
		this.cpf = cpf;
		this.codBanco = codBanco;
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.codVeiculo = codVeiculo;
		this.saldoSiaf = saldoSiaf;
		this.recursoAno = recursoAno;
		this.restoPagar = restoPagar;
		this.status = status;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCodCidade() {
		return codCidade;
	}

	public void setCodCidade(int codCidade) {
		this.codCidade = codCidade;
	}

	public int getCnh() {
		return cnh;
	}

	public void setCnh(int cnh) {
		this.cnh = cnh;
	}

	public Date getValidadeCnh() {
		return validadeCnh;
	}

	public void setValidadeCnh(Date validadeCnh) {
		this.validadeCnh = validadeCnh;
	}

	public int getIdentidade() {
		return identidade;
	}

	public void setIdentidade(int identidade) {
		this.identidade = identidade;
	}

	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}


	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public int getCodVeiculo() {
		return codVeiculo;
	}

	public void setCodVeiculo(int codVeiculo) {
		this.codVeiculo = codVeiculo;
	}

	public double getSaldoSiaf() {
		return saldoSiaf;
	}

	public void setSaldoSiaf(double saldoSiaf) {
		this.saldoSiaf = saldoSiaf;
	}

	public double getRecursoAno() {
		return recursoAno;
	}

	public void setRecursoAno(double recursoAno) {
		this.recursoAno = recursoAno;
	}

	public double getRestoPagar() {
		return restoPagar;
	}

	public void setRestoPagar(double restoPagar) {
		this.restoPagar = restoPagar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public VeiculoDTO getVeiculoDTO() {
		return veiculoDTO;
	}

	public void setVeiculoDTO(VeiculoDTO veiculoDTO) {
		this.veiculoDTO = veiculoDTO;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}
	
}
