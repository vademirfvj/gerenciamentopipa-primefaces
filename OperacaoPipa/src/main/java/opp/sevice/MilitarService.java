package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.MilitarDAO;
import opp.dto.MilitarDTO;
import opp.impl.MilitarDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("militarService")
public class MilitarService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6938950410512908073L;
	
	@Autowired
	private MilitarDAO militarDAO = new MilitarDAOImpl();
	
	public void atualizaMilitar(MilitarDTO militar) throws Exception{
		militarDAO.atualizaMilitar(militar);
	}
	
	public void insereMilitar(MilitarDTO militar) throws Exception{
		militarDAO.insereMilitar(militar);
	}
 
	public MilitarDTO recuperaMilitarPorNome(String nome) throws Exception{
		return militarDAO.recuperaMilitarPorNome(nome);
	}
	
	public List<MilitarDTO> recuperaTodosMilitares() throws Exception{
		return militarDAO.recuperaTodosMilitares();
	}
	
	public MilitarDTO recuperaMilitarPorCpf(String cpf) throws Exception{
		return militarDAO.recuperaMilitarPorCpf(cpf);
	}
	
	public List<MilitarDTO> recuperaMilitarsPorNome(String nome, String status) throws Exception{
		return militarDAO.recuperaMilitarsPorNome(nome,status);
	}
}
