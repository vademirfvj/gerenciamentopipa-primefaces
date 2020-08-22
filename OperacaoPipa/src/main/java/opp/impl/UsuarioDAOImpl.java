package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.UsuarioDAO;
import opp.dto.ParametrosRelatorio;
import opp.dto.PostoGraduDTO;
import opp.dto.UsuarioDTO;
import opp.util.Constantes;

import org.apache.log4j.Logger;

public class UsuarioDAOImpl implements UsuarioDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3241178965539034945L;

	private static final Logger logger = Logger.getLogger(UsuarioDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public UsuarioDTO recuperaUsuarioPorId(int id) throws Exception {
		logger.info("Iniciando UsuarioDTO recuperaUsuarioPorId(int id)" + "Id: " + id);
		UsuarioDTO usuario = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                         "
					+ "  FROM usuario c			          "
					+ "	 WHERE c.id = '"+id+"'            "
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				usuario = new UsuarioDTO();

				usuario.setId(new Integer(resultSet.getInt("ID")));
				usuario.setNome(resultSet.getString("NOME"));
				usuario.setPostGrad(resultSet.getInt("POSTOGRAD"));
				usuario.setEmail(resultSet.getString("EMAIL"));
				usuario.setLogin(resultSet.getString("LOGIN"));
				usuario.setSenha(resultSet.getString("SENHA"));
				usuario.setStatus(resultSet.getString("STATUS"));
			}
			
			return usuario;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public UsuarioDTO recuperaUsuarioPorNome(String nome) throws Exception {
		logger.info("Iniciando UsuarioDTO recuperaUsuarioPorNome(String nome)" + "Nome: " + nome);
		UsuarioDTO usuario = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                         "
					+ "  FROM usuario c			          "
					+ "	 WHERE upper(c.nome) = upper('"+nome+"')   "
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				usuario = new UsuarioDTO();

				usuario.setId(new Integer(resultSet.getInt("ID")));
				usuario.setNome(resultSet.getString("NOME"));
				usuario.setPostGrad(resultSet.getInt("POSTOGRAD"));
				usuario.setEmail(resultSet.getString("EMAIL"));
				usuario.setLogin(resultSet.getString("LOGIN"));
				usuario.setSenha(resultSet.getString("SENHA"));
				usuario.setStatus(resultSet.getString("STATUS"));
			}
			
			return usuario;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<UsuarioDTO> recuperaUsuariosPorNome(String nome, String status) throws Exception {
		logger.info("Iniciando List<UsuarioDTO> recuperaUsuariosPorNome(String nome, String status)" 
					+ "Usuario: " + nome + "Status: " + status);
		
		List<UsuarioDTO> listaUsuario = new ArrayList<UsuarioDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                                 "
					+ "  FROM usuario c			                  "
					+ "	 WHERE c.nome LIKE '"+nome+"%'         "
			);
			
			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryUsuario.append("\n  AND c.status = '"+status+"'              ");
			}

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				UsuarioDTO usuario = new UsuarioDTO();

				usuario.setId(new Integer(resultSet.getInt("ID")));
				usuario.setNome(resultSet.getString("NOME"));
				usuario.setPostGrad(resultSet.getInt("POSTOGRAD"));
				usuario.setEmail(resultSet.getString("EMAIL"));
				usuario.setLogin(resultSet.getString("LOGIN"));
				usuario.setSenha(resultSet.getString("SENHA"));
				usuario.setStatus(resultSet.getString("STATUS"));

				listaUsuario.add(usuario);
			}

			return listaUsuario;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<UsuarioDTO> recuperaTodosUsuarios() throws Exception {
		logger.info("Iniciando List<UsuarioDTO> recuperaTodosUsuarios()");
		
		List<UsuarioDTO> listaUsuario = new ArrayList<UsuarioDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                                 "
					+ "  FROM usuario			                  "
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				UsuarioDTO usuario = new UsuarioDTO();

				usuario.setId(new Integer(resultSet.getInt("ID")));
				usuario.setNome(resultSet.getString("NOME"));
				usuario.setPostGrad(resultSet.getInt("POSTOGRAD"));
				usuario.setEmail(resultSet.getString("EMAIL"));
				usuario.setLogin(resultSet.getString("LOGIN"));
				usuario.setSenha(resultSet.getString("SENHA"));
				usuario.setStatus(resultSet.getString("STATUS"));

				listaUsuario.add(usuario);
			}

			return listaUsuario;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void insereUsuario(UsuarioDTO usuario) throws Exception{
		logger.info("Iniciando void insereUsuario(String usuario" + "Usuario: " + usuario);
		
		int id = 0;
		
		if(usuario.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = usuario.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryUsuario = new StringBuffer();
			
			queryUsuario.append(
					" INSERT INTO `usuario` (                 " +
					" `ID`,                                   " +
					" `NOME` ,                                " +
					" `POSTOGRAD` ,                        " +
					" `EMAIL` ,                            " +
					" `LOGIN` ,                            " +
					" `SENHA` ,                            " +							
					" `STATUS`                                " +
					")                                        " +
					" VALUES (                                " +
					" '"+ id                                  +"', "+
					" '"+ usuario.getNome().toUpperCase()                   +"', "+
					" '"+ usuario.getPostGrad()               +"', "+
					" '"+ usuario.getEmail()                  +"', "+
					" '"+ usuario.getLogin()                  +"', "+
					" '"+ usuario.getSenha()                  +"', "+
					" '"+ usuario.getStatus()	              +"'  "+	
					")                                        " 
					);
			
			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			statement.executeUpdate();
			
		}catch(Exception ex){
			throw new Exception(ex);
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public void atualizaUsuario(UsuarioDTO usuario) throws Exception{
		logger.info("Iniciando void atualizaUsuario(String usuario" + "Usuario: " + usuario);
		
		int id = 0;
		
		if(usuario.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = usuario.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryUsuario = new StringBuffer();

			queryUsuario.append(
					" UPDATE usuario  SET     " +
					" `NOME` =                " +  "'"+ usuario.getNome()      +"',"+
					" `POSTOGRAD` =           " +  "'"+ usuario.getPostGrad()  +"',"+
					" `EMAIL` =               " +  "'"+ usuario.getEmail()     +"',"+
					" `LOGIN` =               " +  "'"+ usuario.getLogin()     +"',"+
					" `SENHA` =               " +  "'"+ usuario.getSenha()     +"',"+
					" `STATUS` =              " +  "'"+ usuario.getStatus()    +"'"+
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			statement.executeUpdate();
			
		}catch(Exception ex){
			throw new Exception(ex);
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

	private int recuperaProximoId() throws Exception{
		
		int retorno=0;
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryUsuario = new StringBuffer();

			queryUsuario.append("select max(id) max_id from usuario");

			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();
			
			while ( resultSet.next() ){
				if(null != resultSet){
					String valor = resultSet.getString("max_id");
	
					if(null == valor){
						retorno = 1;
					}else{
						retorno = Integer.valueOf(valor)+1;
					}
				}
			}
			
		}catch(Exception ex){
		
			throw new Exception(ex);
				
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		return retorno;
	}

	@Override
	public UsuarioDTO recuperaUsuarioPorLogin(String usuarioDTO) throws Exception {
		logger.info("Iniciando UsuarioDTO recuperaUsuarioPorLogin(String nome)" + "Usuário: " + usuarioDTO);
		UsuarioDTO usuario = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                         "
					+ "  FROM usuario c			          "
					+ "	 WHERE c.login = '"+usuarioDTO+"'   "
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				usuario = new UsuarioDTO();

				usuario.setId(new Integer(resultSet.getInt("ID")));
				usuario.setNome(resultSet.getString("NOME"));
				usuario.setPostGrad(resultSet.getInt("POSTOGRAD"));
				usuario.setEmail(resultSet.getString("EMAIL"));
				usuario.setLogin(resultSet.getString("LOGIN"));
				usuario.setSenha(resultSet.getString("SENHA"));
				usuario.setStatus(resultSet.getString("STATUS"));
			}
			
			return usuario;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

	@Override
	public List<PostoGraduDTO> recuperaTodasPostos() throws Exception {
		logger.info("Iniciando UsuarioDTO recuperaTodasPostos()");
		PostoGraduDTO posto = null;
		List<PostoGraduDTO> postolista = new ArrayList();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "  SELECT *                         "
					+ "  FROM postograduacao	          "
					+ "  order by postograduacao          "
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				posto = new PostoGraduDTO();

				posto.setId(new Integer(resultSet.getInt("id")));
				posto.setPosto(resultSet.getString("postograduacao"));
				
				postolista.add(posto);
			}
			
			return postolista;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

	@Override
	public void atualizarAssinaturaDistrPieiro(PostoGraduDTO posto) throws Exception {
			logger.info("Iniciando void atualizarAssinaturaDistrPieiro(String usuario" + "posto: " + posto.getPosto());
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryUsuario = new StringBuffer();

			queryUsuario.append(
					" UPDATE postograduacao  SET     " +
					" postograduacao =           " +  "'"+ posto.getPosto()  +"' "+
					" WHERE id = "+ posto.getId() +"   "
					);
			
			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			statement.executeUpdate();
			
		}catch(Exception ex){
			throw new Exception(ex);
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}

	@Override
	public List<ParametrosRelatorio> recuperarInteracaoUsuario()throws Exception {
		logger.info("Iniciando UsuarioDTO recuperarInteracaoUsuario()");
		ParametrosRelatorio param = null;
		List<ParametrosRelatorio> parametros = new ArrayList();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "\n  SELECT user.NOME, fun.nome as funcionalidade, inte.acao, inte.obs, inte.data                        "
					+ "\n  FROM interacao_usuario inte, usuario user, funcionalidades fun	          "
					+ "\n  where inte.funcionalidade = fun.Id and inte.usuario = user.id          "
					+ "\n  order by data desc"
			);

			logger.info("Query: " + queryUsuario.toString());
			statement = conecction.prepareStatement(queryUsuario.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				param = new ParametrosRelatorio();

				param.setFuncionalidade(resultSet.getString("funcionalidade"));
				param.setUsuario(resultSet.getString("nome"));
				param.setAcao(resultSet.getString("acao"));
				param.setData(new Date(resultSet.getDate("data").getTime()));
				param.setObs(resultSet.getString("obs"));
				
				parametros.add(param);
			}
			
			return parametros;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}



	@Override
	public List<ParametrosRelatorio> recuperarCidadePipeiro(int codCidade,int ano, int mes) throws Exception {
		logger.info("Iniciando UsuarioDTO recuperarInteracaoUsuario()");
		ParametrosRelatorio param = null;
		List<ParametrosRelatorio> parametros = new ArrayList();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUsuario = new StringBuffer();
			queryUsuario.append(
					  "\n  SELECT intDistr.*, pipe.NOME, vei.CAPACIDADEPIPA FROM interacao_ditribuicao intDistr, pipeiro pipe, veiculo vei  "
					+ "\n  where intDistr.COD_PIPEIRO = pipe.ID AND intDistr.COD_VEICULO = vei.ID AND intDistr.COD_CIDADE = ? AND intDistr.ANO = ? AND intDistr.MES = ? "
			);

			logger.info("Query: " + queryUsuario.toString());
			
			statement = conecction.prepareStatement(queryUsuario.toString());
			
			statement.setInt(1, codCidade);
			statement.setInt(2, ano);
			statement.setInt(3, mes);
			
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				param = new ParametrosRelatorio();

				param.setNomePipeiro(resultSet.getString("NOME"));
				param.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				param.setQtdPessoas(resultSet.getInt("TOTAL_PESSOAS"));
				param.setQtdPA(resultSet.getInt("TOTAL_APONTADORES"));
				param.setQtqAgua(resultSet.getDouble("TOTAL_QTD_AGUA"));
				param.setQtdViagens(resultSet.getInt("TOTAL_VIAGEM_FORMULA"));
				param.setKmPecorrido(resultSet.getInt("TOTAL_DISTANCIA"));
				param.setValorBruto(resultSet.getInt("VALOR_TOTAL"));
				
				parametros.add(param);
			}
			
			return parametros;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
}
