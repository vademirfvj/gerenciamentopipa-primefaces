package opp.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import opp.dto.CidadeDTO;
import opp.dto.ParametrosRelatorio;
import opp.dto.PontoColetaDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.CidadeService;
import opp.sevice.UsuarioService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
@ViewScoped
@ManagedBean(name = "relatorioBean")
@SessionScoped
public class RelatorioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 873142096184188266L;

	private static final Logger logger = Logger.getLogger(RelatorioBean.class);
	
	/* Atributos */
	private ParametrosRelatorio parametrosRelatorio;
	private List<ParametrosRelatorio> listaParametros;
	
	private List<CidadeDTO> listacidades;
	private int codCidade;
	
	private String mesSelecionado;
	private String anoSelecionado;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	@ManagedProperty(value = "#{cidadeService}")
	private CidadeService cidadeService;
	
	@PostConstruct
	public void loadPage() {
		
		usuarioService = new UsuarioService();
		cidadeService = new CidadeService();
		
		try {
			listacidades = cidadeService.recuperaTodasCidades();
		} catch (Exception ex) {
			logger.error(ex);
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao recuperar as cidades"));
		}
		
	}
	

	public void recuperarInteracaoUsuario(){
		
		try{
			
			listaParametros = usuarioService.recuperarInteracaoUsuario();
			
		}catch(Exception ex){
		String	msg = "Não foi possível recuperar o registro.";
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
			
			logger.error(ex);
		}
	}
	
	public void buscarCidadePipeiro(){
		try{
			
			if(Funcoes.isNuloOuVazioZero(codCidade)){
				String	msg = "Preencher o campo cidade.";
				FacesContext facesContext = FacesContext.getCurrentInstance();  
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
				return;
			}

			if(Funcoes.isNuloOuVazio(anoSelecionado)){
				String	msg = "Preencher o campo ano.";
				FacesContext facesContext = FacesContext.getCurrentInstance();  
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
				return;
			}
			
			if(Funcoes.isNuloOuVazio(mesSelecionado)){
				String	msg = "Preencher o campo mês.";
				FacesContext facesContext = FacesContext.getCurrentInstance();  
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
				return;
			}
			
			listaParametros = usuarioService.recuperarCidadePipeiro(codCidade,Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()));
			
			
			if(Funcoes.isListaNulaOuVazia(listaParametros)){
				String	msg = "Não existe distribuição para essa cidade no mês e ano selecionado.";
				FacesContext facesContext = FacesContext.getCurrentInstance();  
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", msg));
			}else{
			
				ParametrosRelatorio pr;
				int contator = 0;
				int contatorTotal = 0;
				int media = 0;
				for (int i = 0; i < listaParametros.size(); i++) {
					pr = listaParametros.get(i);
					
					contator += (int) pr.getQtqAgua();
					contatorTotal++;
				}
				
				media = contator / contatorTotal;
				
				for (int i = 0; i < listaParametros.size(); i++) {
					pr = listaParametros.get(i);
					
					pr.setQtdViagensIdeal(Funcoes.formatarDouble((double) media/pr.getCapacidadePipa()));
					pr.setQtdViagensDiferenca(pr.getQtdViagens()- (int) pr.getQtdViagensIdeal());
				}
			
			}
			
			
		}catch(Exception ex){
		String	msg = "Não foi possível recuperar o registro.";
		FacesContext facesContext = FacesContext.getCurrentInstance();  
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
			logger.error(ex);
		}
		
	}
	
	public void zerarListaParametros(){
		
		if(!Funcoes.isListaNulaOuVazia(listaParametros))
		listaParametros.clear();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}


	public ParametrosRelatorio getParametrosRelatorio() {
		return parametrosRelatorio;
	}


	public void setParametrosRelatorio(ParametrosRelatorio parametrosRelatorio) {
		this.parametrosRelatorio = parametrosRelatorio;
	}


	public List<ParametrosRelatorio> getListaParametros() {
		return listaParametros;
	}


	public void setListaParametros(List<ParametrosRelatorio> listaParametros) {
		this.listaParametros = listaParametros;
	}


	public List<CidadeDTO> getListacidades() {
		return listacidades;
	}


	public void setListacidades(List<CidadeDTO> listacidades) {
		this.listacidades = listacidades;
	}


	public int getCodCidade() {
		return codCidade;
	}


	public void setCodCidade(int codCidade) {
		this.codCidade = codCidade;
	}


	public CidadeService getCidadeService() {
		return cidadeService;
	}


	public void setCidadeService(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
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

}
