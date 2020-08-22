package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.VeiculoDAO;
import opp.dto.VeiculoDTO;
import opp.dto.VeiculoDTO;
import opp.impl.VeiculoDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("veiculoService")
public class VeiculoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5803252560356377058L;
	
	@Autowired
	private VeiculoDAO veiculoDAO = new VeiculoDAOImpl();
	
	public void atualizaVeiculo(VeiculoDTO veiculo) throws Exception{
		veiculoDAO.atualizaVeiculo(veiculo);
	}
	
	public void insereVeiculo(VeiculoDTO veiculo) throws Exception{
		veiculoDAO.insereVeiculo(veiculo);
	}
	
	public List<VeiculoDTO> recuperaTodosVeiculos() throws Exception{
		return veiculoDAO.recuperaTodosVeiculos();
	}
	
	public List<VeiculoDTO> recuperaTodosVeiculosAtivos() throws Exception{
		return veiculoDAO.recuperaTodosVeiculosAtivos();
	}
	
	
	public VeiculoDTO recuperaVeiculoPorPlaca(String placa) throws Exception{
		return veiculoDAO.recuperaVeiculoPorPlaca(placa);
	}
	
	public List<VeiculoDTO> recuperaVeiculosPorPlaca(String placa, String status) throws Exception{
		return veiculoDAO.recuperaVeiculosPorPlaca(placa,status);
	}
}
