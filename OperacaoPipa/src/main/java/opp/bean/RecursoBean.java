package opp.bean;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import opp.dto.RecursoDTO;
import opp.sevice.RecursoService;
 
@ViewScoped
@ManagedBean(name = "recursoBean", eager = true)
@SessionScoped
public class RecursoBean implements Serializable {
 	
	private String mesSelecionado;
	private String anoSelecionado;
    private boolean halibitarCampoAno;
    private double saldo;
    private List<RecursoDTO> recursoDTOList = new ArrayList();
    
    @ManagedProperty("#{recursoService}")
    private RecursoService service;
    
    
    @PostConstruct
    public void init() {
    	halibitarCampoAno = true;
    	
    	//TESTE
    	saldo = 10000.00;
    	RecursoDTO rc = new RecursoDTO(1,"NC TESTE",1,2015,10000.00,"ATIVO");
    	recursoDTOList.add(rc);
    }
    
    public void habilitarAno(){
    	halibitarCampoAno = false;
    }

	public String getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(String mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public String getAnoSelecionado() {
		return anoSelecionado;
	}

	public void setAnoSelecionado(String anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public RecursoService getService() {
		return service;
	}

	public void setService(RecursoService service) {
		this.service = service;
	}

	public boolean isHalibitarCampoAno() {
		return halibitarCampoAno;
	}

	public void setHalibitarCampoAno(boolean halibitarCampoAno) {
		this.halibitarCampoAno = halibitarCampoAno;
	}

	public List<RecursoDTO> getRecursoDTOList() {
		return recursoDTOList;
	}

	public void setRecursoDTOList(List<RecursoDTO> recursoDTOList) {
		this.recursoDTOList = recursoDTOList;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
    
}