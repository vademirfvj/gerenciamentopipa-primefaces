package opp.dto;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7050389209330996788L;

	private int id;
	private String nome;
	private int postGrad;
	private String email;
	private String login;
	private String senha;
	private String status;
	
	public UsuarioDTO() {
	}
	
	public UsuarioDTO(int id, String nome, int postGrad, String email,
			String login, String senha, String status) {
		this.id = id;
		this.nome = nome;
		this.postGrad = postGrad;
		this.email = email;
		this.login = login;
		this.senha = senha;
		this.status = status;
	}

	public UsuarioDTO(String nome, int postGrad, String email, String login,
			String senha, String status) {
		this.nome = nome;
		this.postGrad = postGrad;
		this.email = email;
		this.login = login;
		this.senha = senha;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
}
