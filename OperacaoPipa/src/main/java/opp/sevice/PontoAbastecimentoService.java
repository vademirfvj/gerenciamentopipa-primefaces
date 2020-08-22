package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.PontoAbastecimentoDAO;
import opp.dto.IndiceDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.impl.PontoAbastecimentoDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pontoAbastecimentoService")
public class PontoAbastecimentoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2654279116750974417L;
	
	@Autowired
	private PontoAbastecimentoDAO pontoAbastecimentoDAO = new PontoAbastecimentoDAOImpl();
	
	public void atualizaPontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception{
		pontoAbastecimentoDAO.atualizaPontoAbastecimento(pontoAbastecimento);
	}
	
	public void inserePontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception{
		pontoAbastecimentoDAO.inserePontoAbastecimento(pontoAbastecimento);
	}
 
	public PontoAbastecimentoDTO recuperaPontoAbastecimentoPorComunidade(String comunidade, int codcidade) throws Exception{
		return pontoAbastecimentoDAO.recuperaPontoAbastecimentoPorComunidade(comunidade,codcidade);
	}

	public List<PontoAbastecimentoDTO> recuperaTodosPontoAbastecimentos() throws Exception{
		return pontoAbastecimentoDAO.recuperaTodosPontoAbastecimentos();
	}
	
	
	public List<PontoAbastecimentoDTO> recuperaPontoAbastecimentosPorComunidade(String comunidade, String status, int codcidadePesquisa) throws Exception{
		return pontoAbastecimentoDAO.recuperaPontoAbastecimentosPorComunidade(comunidade,status,codcidadePesquisa);
	}

	public IndiceDTO recuperaIndicePorDescricao(String maisAsfalto)  throws Exception{
		return pontoAbastecimentoDAO.recuperaIndicePorDescricao(maisAsfalto);
	}

	public List<IndiceDTO> recuperaIndiceTodos() throws Exception{
		return pontoAbastecimentoDAO.recuperaIndiceTodos();
	}

	public void atualizarIndices(IndiceDTO indice)throws Exception {
		pontoAbastecimentoDAO.atualizarIndices(indice);
		
	}
}
