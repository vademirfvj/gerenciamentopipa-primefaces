package opp.dao;

import java.util.List;

import opp.dto.IndiceDTO;
import opp.dto.PontoAbastecimentoDTO;

public interface PontoAbastecimentoDAO {

	PontoAbastecimentoDTO recuperaPontoAbastecimentoPorComunidade(String comunidade, int codcidade) throws Exception;
	
	List<PontoAbastecimentoDTO> recuperaPontoAbastecimentosPorComunidade(String comunidade,String status, int codcidadePesquisa) throws Exception;
	
	void atualizaPontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception;
	
	void inserePontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception;
	
	List<PontoAbastecimentoDTO> recuperaTodosPontoAbastecimentos() throws Exception;
	
	PontoAbastecimentoDTO recuperaPontoAbastecimentoPorId(int id) throws Exception;

	IndiceDTO recuperaIndicePorDescricao(String maisAsfalto)throws Exception;

	List<IndiceDTO> recuperaIndiceTodos()throws Exception;

	void atualizarIndices(IndiceDTO indice) throws Exception;
	
}
