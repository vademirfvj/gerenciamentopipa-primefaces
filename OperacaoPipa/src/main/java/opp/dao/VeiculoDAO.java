package opp.dao;

import java.util.List;

import opp.dto.VeiculoDTO;

public interface VeiculoDAO {
	
	VeiculoDTO recuperaVeiculoPorPlaca(String placa) throws Exception;
	
	List<VeiculoDTO> recuperaVeiculosPorPlaca(String placa,String status) throws Exception;
	
	void atualizaVeiculo(VeiculoDTO veiculo) throws Exception;
	
	void insereVeiculo(VeiculoDTO veiculo) throws Exception;
	
	List<VeiculoDTO> recuperaTodosVeiculos() throws Exception;
	
	List<VeiculoDTO> recuperaTodosVeiculosAtivos() throws Exception;
	
	VeiculoDTO recuperaVeiculoPorId(int id) throws Exception;
}
