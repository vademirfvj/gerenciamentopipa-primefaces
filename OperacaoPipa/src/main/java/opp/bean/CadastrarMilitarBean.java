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
import opp.dto.MilitarDTO;
import opp.dto.PostoGraduDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.MilitarService;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.UsuarioService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

@ViewScoped
@ManagedBean(name = "cadastrarMilitarBean")
@SessionScoped
public class CadastrarMilitarBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3731448755456027633L;

	private static final Logger logger = Logger.getLogger(CadastrarMilitarBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos de Cadastro */
	private MilitarDTO militar;
	private int id;
	private String nome;   
	private int postGrad;       
	private String cpf;
	private String banco;    
	private String numBanco;
	private String agencia;
	private String conta;
	private String telefone;
	private String celular;
	private String email;
	private String status;

	private List<MilitarDTO> listaMilitar;
	
	private List<PostoGraduDTO> listaPosto;
	
	/* Atributos de Pesquisa */
	private String pesquisaMilitar;
	private String pesquisaStatusMilitar;
	private List<MilitarDTO> listaPesquisa;
	
	/* Atributos de Edição */
	private MilitarDTO militarEditavel;
	
	@ManagedProperty(value = "#{militarService}")
	private MilitarService militarService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		logger.info("Inicio da tela de Cadastro de Militar.");
		
		militarService = new MilitarService();
		usuarioService = new UsuarioService();
		service = new PagamentoPipeiroSevice();
		militarEditavel = new MilitarDTO();
		
		try {
			
			listaPosto = usuarioService.recuperaTodasPostos();
			List<PostoGraduDTO> listaPostoTeste = new ArrayList();
			listaPostoTeste.addAll(listaPosto);
			
			PostoGraduDTO pg;
			for (int i = 0; i < listaPostoTeste.size(); i++) {
				pg = listaPostoTeste.get(i);
				
				if(pg.getId() == 5){
					listaPosto.remove(pg);
				}
			}
			
		} catch (Exception ex) {
			logger.error(ex);
			FacesContext.getCurrentInstance().addMessage(    
	                "msgCadastrarPontoAbastecimento",    
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
	                        "Erro ao recuperar Post./Grad.", null));
		}
	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		logger.info("Inicio do método salvar()");

		
		if(campoEhValido(id,nome,postGrad,cpf,banco,numBanco,agencia,conta,telefone,celular,email,status)){
			
			
			setStatus("ATIVO");
			
			//Monta objeto
			militar = new MilitarDTO(
					nome,postGrad,cpf,banco,numBanco,agencia,
					conta,telefone,celular,email,status
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(militar.getCpf());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarMilitar",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Militar
				boolean erro = false;
				
				try{
					militarService.insereMilitar(militar);
					
					//Salvar dados em interacao_usuario
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_MILITAR,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarMilitar",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarMilitar",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarMilitar",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(nome)){
				msgErro.append("- Militar ");
			}
			if(Funcoes.isNuloOuVazioZero(postGrad)){
				msgErro.append("- Post/Grad ");
			}
			if(Funcoes.isNuloOuVazio(cpf)){
				msgErro.append("- CPF ");
			}
			if(Funcoes.isNuloOuVazio(banco)){
				msgErro.append("- Banco ");
			}
			if(Funcoes.isNuloOuVazio(numBanco)){
				msgErro.append("- Número Banco ");
			}
			if(Funcoes.isNuloOuVazio(conta)){
				msgErro.append("- Conta ");
			}
			if(Funcoes.isNuloOuVazio(agencia)){
				msgErro.append("- Agência ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}

		logger.info("Fim do método salvar()");

	}

	public boolean verificaSeExiste(String militar) throws Exception{
		boolean existe = false;
		
		MilitarDTO militarProcurada = militarService.recuperaMilitarPorCpf(militar);
		if(null != militarProcurada && militarProcurada.getCpf().equals(militar)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		militarEditavel = new MilitarDTO();
	}
	
	public void editar(){
		logger.info("Inicio do método editar()");

		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido( militarEditavel.getId(),
				militarEditavel.getNome(), militarEditavel.getPostGrad(), militarEditavel.getCpf(), militarEditavel.getBanco(), militarEditavel.getNumBanco(), militarEditavel.getAgencia(), 
				militarEditavel.getConta(), militarEditavel.getTelefone(), militarEditavel.getCelular(), militarEditavel.getEmail(), militarEditavel.getStatus())){
			try{
				militarService.atualizaMilitar(militarEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_MILITAR,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
				
			}catch(Exception ex){
				erro = true;
				msg = "Não foi possível editar o registro.";
				logger.error(ex);
			}
		}
	
		if(!erro){
			limpaEditavel();
			pesquisar();
		}else{
			logger.error("Não foi possível editar o registro.");
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método editar()");

	}
	
	public void enviaEditavel(MilitarDTO militar){
		militarEditavel = new MilitarDTO(
				militar.getId(), militar.getNome(), militar.getPostGrad(), militar.getCpf(), militar.getBanco(), militar.getNumBanco(),
				militar.getAgencia(), militar.getConta(), militar.getTelefone(), militar.getCelular(), militar.getEmail(), militar.getStatus()
				);
	}
	
	public void pesquisar(){
		
		logger.info("Inicio do método pesquisar()");

		
		String msg = null;
		//Pesquisa Militar
		try{
			listaPesquisa = militarService.recuperaMilitarsPorNome(pesquisaMilitar,pesquisaStatusMilitar);
		
			if(null == listaPesquisa){
				msg = "Não foi possível pesquisar o registro.";
	
			}else if(listaPesquisa.isEmpty()){
				msg = "Nenhum registro foi encontrado.";
			}
		
		}catch(Exception ex){
			logger.error(ex);
			msg = "Não foi possível pesquisar o registro.";
		}
		
		if(null != msg){
			logger.error(msg);
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		logger.info("Fim do método pesquisar()");

	}
	
	private boolean campoEhValido(int id, String nome, int postGrad, String cpf, String banco, String numBanco, String agencia, String 
			conta, String telefone, String celular, String email, String status){
		
		if(Funcoes.isNuloOuVazio(nome) || Funcoes.isNuloOuVazioZero(postGrad) ||
				Funcoes.isNuloOuVazio(cpf) || Funcoes.isNuloOuVazio(banco)
				|| Funcoes.isNuloOuVazio(numBanco) || Funcoes.isNuloOuVazio(conta) 
				|| Funcoes.isNuloOuVazio(agencia)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){

		id = 0;
		nome = "";
		postGrad = 0;       
		cpf = "";
		banco = "";    
		numBanco = "";
		agencia = "";
		conta = "";
		telefone = "";
		celular = "";
		email = "";
		status = "";
		
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		logger.info("Inicio do método imprimirRelatorio()");

		boolean erro = false;
		String msg = "";
		
		try {
			
			List<MilitarDTO> lista = new ArrayList<MilitarDTO>();
			lista = militarService.recuperaTodosMilitares();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_militar.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioMilitares_"+timeStamp+".pdf";
			
			// exportacao do relatorio para outro formato, no caso PDF
			JasperExportManager.exportReportToPdfFile(print,caminhoImpressao+arquivo);

		} catch (Exception e) {
			erro = true;
			logger.error(e);
		}
		
		if(erro){
			msg = "Erro ao gerar relatório.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}else{
			msg = "Relatório gerado com sucesso.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método imprimirRelatorio()");
	
	}
	
	public void onRowEdit(RowEditEvent event) {
		 
		 try {
			
			 PostoGraduDTO posto = (PostoGraduDTO) event.getObject();
			 
			 usuarioService.atualizarAssinaturaDistrPieiro(posto);
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao atualizar Posto/Grad. ["
	    				+e.toString()+ " ]"));
			}
			 
	 }
	
	/**
	 * Getters and Setters
	 */
	public Status[] getStatusEnum() {
		return Status.values();
	}

	public MilitarDTO getMilitar() {
		return militar;
	}

	public void setMilitar(MilitarDTO militar) {
		this.militar = militar;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPostGrad() {
		return postGrad;
	}

	public void setPostGrad(int postGrad) {
		this.postGrad = postGrad;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNumBanco() {
		return numBanco;
	}

	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<MilitarDTO> getListaMilitar() {
		if(null == listaMilitar){
			listaMilitar = new ArrayList<MilitarDTO>();
		}
		
		return listaMilitar;
	}

	public void setListaMilitar(List<MilitarDTO> listaMilitar) {
		this.listaMilitar = listaMilitar;
	}

	public String getPesquisaMilitar() {
		return pesquisaMilitar;
	}

	public void setPesquisaMilitar(String pesquisaMilitar) {
		this.pesquisaMilitar = pesquisaMilitar;
	}

	public String getPesquisaStatusMilitar() {
		return pesquisaStatusMilitar;
	}

	public void setPesquisaStatusMilitar(String pesquisaStatusMilitar) {
		this.pesquisaStatusMilitar = pesquisaStatusMilitar;
	}

	public List<MilitarDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<MilitarDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public MilitarDTO getMilitarEditavel() {
		return militarEditavel;
	}

	public void setMilitarEditavel(MilitarDTO militarEditavel) {
		this.militarEditavel = militarEditavel;
	}

	public MilitarService getMilitarService() {
		return militarService;
	}

	public void setMilitarService(MilitarService militarService) {
		this.militarService = militarService;
	}

	public List<PostoGraduDTO> getListaPosto() {
		return listaPosto;
	}

	public void setListaPosto(List<PostoGraduDTO> listaPosto) {
		this.listaPosto = listaPosto;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}
	
}
