package opp.dto;

import java.io.Serializable;

public class MilitarDTO implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3139498392556668486L;
	
	private int id;
	private String nome;   
	private int postGrad;       
	private String cpf;
	private String banco;    
	private String numBanco;
	private String agencia;
	private String conta;
	private String telefone;
	private String celular;
	private String email;
	private String status;

	public MilitarDTO(){
		
	}

	public MilitarDTO(int id, String nome, int postGrad, String cpf,
			String banco, String numBanco, String agencia, String conta,
			String telefone, String celular, String email, String status) {
		super();
		this.id = id;
		this.nome = nome;
		this.postGrad = postGrad;
		this.cpf = cpf;
		this.banco = banco;
		this.numBanco = numBanco;
		this.agencia = agencia;
		this.conta = conta;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
		this.status = status;
	}
	
	public MilitarDTO(String nome, int postGrad, String cpf, String banco,
			String numBanco, String agencia, String conta, String telefone,
			String celular, String email, String status) {
		super();
		this.nome = nome;
		this.postGrad = postGrad;
		this.cpf = cpf;
		this.banco = banco;
		this.numBanco = numBanco;
		this.agencia = agencia;
		this.conta = conta;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
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

	public int getPostGrad() {
		return postGrad;
	}

	public void setPostGrad(int postGrad) {
		this.postGrad = postGrad;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNumBanco() {
		return numBanco;
	}

	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}