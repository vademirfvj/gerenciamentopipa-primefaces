package opp.bean;
 
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FlowEvent;

import opp.dto.RecursoDTO;
import opp.sevice.EmpenhoService;
import opp.sevice.RecursoService;
 
@ViewScoped
@ManagedBean(name = "empenhoBean", eager = true)
@SessionScoped
public class EmpenhoBean implements Serializable {
 	
	private String mesSelecionado;
	private String mesSelecionadoExtenso;
	private String anoSelecionado;
    private boolean halibitarCampoAno;
    private double saldo;
    private String ncSelecionado;
    private List<RecursoDTO> recursoDTOList = new ArrayList();
    private String pipeiroSelecionado;
    private List<RecursoDTO> valorTeste = new ArrayList();
    private List<RecursoDTO> recursoSelecionado;
    
    @ManagedProperty("#{recursoService}")
    private RecursoService serviceRecurso;
	
    @ManagedProperty("#{empenhoService}")
    private EmpenhoService serviceEmpenho;
    
    
    @PostConstruct
    public void init() {

        halibitarCampoAno = true;
    	
    	//TESTE
    	saldo = 10000.00;
    	RecursoDTO rc = new RecursoDTO(1,"NC TESTE",1,2015,10000.00,"ATIVO");
    	recursoDTOList.add(rc);
    	
    	RecursoDTO rc2 = new RecursoDTO(1,"JOÃO GOMES SAMPAIO",1,2015,10000.00,"ATIVO");
    	valorTeste.add(rc2);
    }
    
    public void habilitarAno(){
    	halibitarCampoAno = false;
    }


    public String onFlowProcess(FlowEvent event) throws ParseException {
    	
    	if(event.getOldStep().equalsIgnoreCase("pesquisaEmpenho")){
    		
    		switch(getMesSelecionado()){
    		
    		case "0":
		    	setMesSelecionadoExtenso("Janeiro");
		            break;
    		case "1":
		    	setMesSelecionadoExtenso("Fevereiro");
		            break; 
    		case "2":
		    	setMesSelecionadoExtenso("Março");
		            break;
    		case "3":
		    	setMesSelecionadoExtenso("Abril");
		            break;
    		case "4":
		    	setMesSelecionadoExtenso("Maio");
		            break;
    		case "5":
		    	setMesSelecionadoExtenso("Junho");
		            break;
    		case "6":
		    	setMesSelecionadoExtenso("Julho");
		            break;
    		case "7":
		    	setMesSelecionadoExtenso("Agosto");
		            break;
    		case "8":
		    	setMesSelecionadoExtenso("Setembro");
		            break;
    		case "9":
		    	setMesSelecionadoExtenso("Outubro");
		            break;
    		case "10":
		    	setMesSelecionadoExtenso("Novembro");
		            break;
    		case "11":
		    	setMesSelecionadoExtenso("Dezembro");
		            break;
    		}
    	}
    	
    	return event.getNewStep();
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


	public RecursoService getServiceRecurso() {
		return serviceRecurso;
	}


	public void setServiceRecurso(RecursoService serviceRecurso) {
		this.serviceRecurso = serviceRecurso;
	}


	public EmpenhoService getServiceEmpenho() {
		return serviceEmpenho;
	}


	public void setServiceEmpenho(EmpenhoService serviceEmpenho) {
		this.serviceEmpenho = serviceEmpenho;
	}


	public double getSaldo() {
		return saldo;
	}


	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getMesSelecionadoExtenso() {
		return mesSelecionadoExtenso;
	}

	public void setMesSelecionadoExtenso(String mesSelecionadoExtenso) {
		this.mesSelecionadoExtenso = mesSelecionadoExtenso;
	}

	public String getNcSelecionado() {
		return ncSelecionado;
	}

	public void setNcSelecionado(String ncSelecionado) {
		this.ncSelecionado = ncSelecionado;
	}

	public String getPipeiroSelecionado() {
		return pipeiroSelecionado;
	}

	public void setPipeiroSelecionado(String pipeiroSelecionado) {
		this.pipeiroSelecionado = pipeiroSelecionado;
	}

	public List<RecursoDTO> getValorTeste() {
		return valorTeste;
	}

	public void setValorTeste(List<RecursoDTO> valorTeste) {
		this.valorTeste = valorTeste;
	}

	public List<RecursoDTO> getRecursoSelecionado() {
		return recursoSelecionado;
	}

	public void setRecursoSelecionado(List<RecursoDTO> recursoSelecionado) {
		this.recursoSelecionado = recursoSelecionado;
	}

}	