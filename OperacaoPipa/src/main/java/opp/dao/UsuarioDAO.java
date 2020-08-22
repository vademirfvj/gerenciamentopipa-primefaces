package opp.dao;

import java.util.List;

import opp.dto.ParametrosRelatorio;
import opp.dto.PostoGraduDTO;
import opp.dto.UsuarioDTO;

public interface UsuarioDAO {
	
	UsuarioDTO recuperaUsuarioPorNome(String nome) throws Exception;
	
	List<UsuarioDTO> recuperaUsuariosPorNome(String nome,String status) throws Exception;
	
	void atualizaUsuario(UsuarioDTO usuario) throws Exception;
	
	void insereUsuario(UsuarioDTO usuario) throws Exception;
	
	List<UsuarioDTO> recuperaTodosUsuarios() throws Exception;
	
	UsuarioDTO recuperaUsuarioPorId(int id) throws Exception;

	UsuarioDTO recuperaUsuarioPorLogin(String usuario) throws Exception;

	List<PostoGraduDTO> recuperaTodasPostos() throws Exception;

	void atualizarAssinaturaDistrPieiro(PostoGraduDTO posto)throws Exception;

	List<ParametrosRelatorio> recuperarInteracaoUsuario() throws Exception;

	List<ParametrosRelatorio> recuperarCidadePipeiro(int codCidade, int ano, int mes)throws Exception;
}
