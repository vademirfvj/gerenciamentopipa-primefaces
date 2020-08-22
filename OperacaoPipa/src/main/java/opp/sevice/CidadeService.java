package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.CidadeDAO;
import opp.dto.CidadeDTO;
import opp.impl.CidadeDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("cidadeService")
public class CidadeService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7402998549710190395L;
	
	@Autowired
	private CidadeDAO cidadeDAO = new CidadeDAOImpl();
	
	public void atualizaCidade(CidadeDTO cidade) throws Exception{
		cidadeDAO.atualizaCidade(cidade);
	}
	
	public void insereCidade(CidadeDTO cidade) throws Exception{
		cidadeDAO.insereCidade(cidade);
	}
 
	public CidadeDTO recuperaCidadePorNome(String nome) throws Exception{
		return cidadeDAO.recuperaCidadePorNome(nome);
	}
	
	public List<CidadeDTO> recuperaTodasCidades() throws Exception{
		return cidadeDAO.recuperaTodasCidades();
	}
	public List<CidadeDTO> recuperaTodasCidadesAtivas() throws Exception{
		return cidadeDAO.recuperaTodasCidadesAtivas();
	}
	
	public List<CidadeDTO> recuperaCidadesPorNome(String nome, String status) throws Exception{
		return cidadeDAO.recuperaCidadesPorNome(nome,status);
	}
	
	public CidadeDTO recuperaCidadePorId(int id) throws Exception{
		return cidadeDAO.recuperaCidadePorId(id);
	}
}
