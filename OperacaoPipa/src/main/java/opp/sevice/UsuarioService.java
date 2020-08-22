package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.UsuarioDAO;
import opp.dto.ParametrosRelatorio;
import opp.dto.PostoGraduDTO;
import opp.dto.UsuarioDTO;
import opp.impl.UsuarioDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4203648475237006898L;
	
	@Autowired
	private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
	
	public void atualizaUsuario(UsuarioDTO usuario) throws Exception{
		usuarioDAO.atualizaUsuario(usuario);
	}
	
	public void insereUsuario(UsuarioDTO usuario) throws Exception{
		usuarioDAO.insereUsuario(usuario);
	}
 
	public UsuarioDTO recuperaUsuarioPorNome(String nome) throws Exception{
		return usuarioDAO.recuperaUsuarioPorNome(nome);
	}
	
	public List<UsuarioDTO> recuperaTodosUsuarios() throws Exception{
		return usuarioDAO.recuperaTodosUsuarios();
	}
	
	
	public List<UsuarioDTO> recuperaUsuariosPorNome(String nome, String status) throws Exception{
		return usuarioDAO.recuperaUsuariosPorNome(nome,status);
	}

	public UsuarioDTO recuperaUsuarioPorLogin(String usuario)throws Exception {
		return usuarioDAO.recuperaUsuarioPorLogin(usuario);
	}

	public List<PostoGraduDTO> recuperaTodasPostos() throws Exception{
		return usuarioDAO.recuperaTodasPostos();
	}

	public void atualizarAssinaturaDistrPieiro(PostoGraduDTO posto)  throws Exception{
		 usuarioDAO.atualizarAssinaturaDistrPieiro(posto);
		
	}

	public List<ParametrosRelatorio> recuperarInteracaoUsuario()throws Exception {
		return usuarioDAO.recuperarInteracaoUsuario() ;
	}

	public List<ParametrosRelatorio> recuperarCidadePipeiro(int codCidade, int ano, int mes)throws Exception {
		return usuarioDAO.recuperarCidadePipeiro(codCidade,ano,mes) ;
	}
}
