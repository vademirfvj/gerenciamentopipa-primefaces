package opp.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import opp.dto.CidadeDTO;
import opp.dto.DataDTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.dto.RotaPagamentoPipeiroDTO;
import opp.relacionamento.InteracaoBusinessInterface;
import opp.util.Funcoes;
import opp.util.Propriedades;

public class InteracaoBusinessImpl implements InteracaoBusinessInterface{
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	private static final Logger logger = Logger.getLogger(CidadeDAOImpl.class);

	@Override
	public void calcularRotaPagamentoRestante(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList,List<PontoAbastecimentoDTO> pontoAbastecimentos,
			List<PipeiroDTO> pipeiros, CidadeDTO cidadeDTO, Calendar calendar,List<DataDTO> datasDistribuicao, int totalDias) {
		
		
		RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTO;
		RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA;
		
		List pontoAbastecimentosObj = new ArrayList();
		
		pontoAbastecimentosObj.addAll(pontoAbastecimentos);
		
			int qtdDiasMes = totalDias;
		
			String mesAno = Funcoes.obterAnoMesExtenso(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
			
			//Adicionando Pipeiro
			PipeiroDTO pipeiroDTO;
			for (int i = 0; i < pipeiros.size(); i++) {
				pipeiroDTO = pipeiros.get(i);
				
				rotaPagamentoPipeiroDTO = new RotaPagamentoPipeiroDTO();
				
				rotaPagamentoPipeiroDTO.setPipeiroDTO(pipeiroDTO);
				rotaPagamentoPipeiroDTO.setCidadeDTO(cidadeDTO);
				rotaPagamentoPipeiroDTO.setLitrosPessoa(Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa")));
				rotaPagamentoPipeiroDTO.setNumDiasMes(qtdDiasMes);
				rotaPagamentoPipeiroDTO.setMesAno(mesAno);
				
				rotaPagamentoPipeiroDTOList.add(rotaPagamentoPipeiroDTO);
				
			}
			
			PontoAbastecimentoDTO pontoAbastecimentoDTO;
			
			for (int i = 0; i < pontoAbastecimentos.size(); i++) {
				
				pontoAbastecimentoDTO = pontoAbastecimentos.get(i);
				
				
				for (int j = 0; j < rotaPagamentoPipeiroDTOList.size(); j++) {
					
					rotaPagamentoPipeiroDTOPA = (RotaPagamentoPipeiroDTO) rotaPagamentoPipeiroDTOList.get(j);	
					
					
					if(Funcoes.isListaNulaOuVazia(rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList())){

						setValoresRotaPagamentoPontoAbacimento(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes);
						
						rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().add(pontoAbastecimentoDTO);
						pontoAbastecimentosObj.remove(pontoAbastecimentoDTO);
						
						distribuirDiasCarrada(pontoAbastecimentoDTO,datasDistribuicao);
						
						break;
						
					}else{
						
						if(!validaTodosReceberamPrimeiraLocalidade(rotaPagamentoPipeiroDTOList)){
							continue;
						}
						
						if (rotaPagamentoPipeiroDTOPA.getValorTotalPA() == recuperaMenorValor(rotaPagamentoPipeiroDTOList)){
							
							setValoresRotaPagamentoPontoAbacimento(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes);
							
							rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().add(pontoAbastecimentoDTO);
							pontoAbastecimentosObj.remove(pontoAbastecimentoDTO);
							
							distribuirDiasCarrada(pontoAbastecimentoDTO,datasDistribuicao);
							
							break;
							
						}
					}
				}
			}

		
			//Verifica se existe Ponto de Abastecimento sem alocacao
			if(Funcoes.isListaNulaOuVazia(pontoAbastecimentosObj)){
				System.out.println("Distribuição de Ponto de Abastecimento OK");
				logger.info("Distribuição de Ponto de Abastecimento OK");
			}else{
				
				for (int i = 0; i < pontoAbastecimentosObj.size(); i++) {
					PontoAbastecimentoDTO pa = pontoAbastecimentos.get(i);
					System.out.println("PA restante: "+pa.getComunidade());
					logger.info("PA restante: "+pa.getComunidade());
				}
			}
			
			
			
		
			//Distruibução equivalente
			//definirRotaEquivalente(rotaPagamentoPipeiroDTOList,qtdDiasMes);
			
			
			//Formatar Double
			formatarDouble(rotaPagamentoPipeiroDTOList);
		
	}
	
	@Override
	public List<RotaPagamentoPipeiroDTO> calcularRotaPagamento(List<PontoAbastecimentoDTO> pontoAbastecimentos,List<PipeiroDTO> pipeiros,
			CidadeDTO cidadeDTO,Calendar data,List<DataDTO> datasDistribuicao, int totalDias) {

		List listaRotaPagamentoPipeiroDTO = new ArrayList();
		
		//Random na lista de Ponto de Abastecimento
		Collections.shuffle(pontoAbastecimentos);
		
		//Random na lista de Pipeiros
		Collections.shuffle(pipeiros);
		
		int qtdDiasMes = totalDias;
		
		String mesAno = Funcoes.obterAnoMesExtenso(data.get(Calendar.MONTH), data.get(Calendar.YEAR));
		
		RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTO;
		RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA;
		PipeiroDTO pipeiroDTO;
		
		List pontoAbastecimentosObj = new ArrayList();
		
		pontoAbastecimentosObj.addAll(pontoAbastecimentos);
		
		//Adicionando Pipeiro
		for (int i = 0; i < pipeiros.size(); i++) {
			pipeiroDTO = pipeiros.get(i);
			
			rotaPagamentoPipeiroDTO = new RotaPagamentoPipeiroDTO();
			
			rotaPagamentoPipeiroDTO.setPipeiroDTO(pipeiroDTO);
			rotaPagamentoPipeiroDTO.setCidadeDTO(cidadeDTO);
			rotaPagamentoPipeiroDTO.setLitrosPessoa(Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa")));
			rotaPagamentoPipeiroDTO.setNumDiasMes(qtdDiasMes);
			rotaPagamentoPipeiroDTO.setMesAno(mesAno);
			
			listaRotaPagamentoPipeiroDTO.add(rotaPagamentoPipeiroDTO);
			
		}
		
		PontoAbastecimentoDTO pontoAbastecimentoDTO;
		
		for (int i = 0; i < pontoAbastecimentos.size(); i++) {
			
			pontoAbastecimentoDTO = pontoAbastecimentos.get(i);
			
			
			for (int j = 0; j < listaRotaPagamentoPipeiroDTO.size(); j++) {
				
				rotaPagamentoPipeiroDTOPA = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(j);	
				
				
				if(Funcoes.isListaNulaOuVazia(rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList())){

					setValoresRotaPagamentoPontoAbacimento(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes);
					
					rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().add(pontoAbastecimentoDTO);
					pontoAbastecimentosObj.remove(pontoAbastecimentoDTO);
					
					distribuirDiasCarrada(pontoAbastecimentoDTO,datasDistribuicao);
					
					break;
					
				}else{
					
					if(!validaTodosReceberamPrimeiraLocalidade(listaRotaPagamentoPipeiroDTO)){
						continue;
					}
					
					if (rotaPagamentoPipeiroDTOPA.getValorTotalPA() == recuperaMenorValor(listaRotaPagamentoPipeiroDTO)){
						
						setValoresRotaPagamentoPontoAbacimento(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes);
						
						rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().add(pontoAbastecimentoDTO);
						pontoAbastecimentosObj.remove(pontoAbastecimentoDTO);
						
						distribuirDiasCarrada(pontoAbastecimentoDTO,datasDistribuicao);
						
						break;
						
					}
				}
			}
		}
		
		
		//Verifica se existe Ponto de Abastecimento sem alocacao
		if(Funcoes.isListaNulaOuVazia(pontoAbastecimentosObj)){
			System.out.println("Distribuição de Ponto de Abastecimento OK");
		}else{
			
			for (int i = 0; i < pontoAbastecimentosObj.size(); i++) {
				PontoAbastecimentoDTO pa = pontoAbastecimentos.get(i);
				System.out.println("PA restante: "+pa.getComunidade());
			}
		}
		
		
		
	
		//Distruibução equivalente
		definirRotaEquivalente(listaRotaPagamentoPipeiroDTO,qtdDiasMes);
		
		
		//Distribuição dias
		//definirDiasCarradas(listaRotaPagamentoPipeiroDTO);

		
		//Formatar Double
		formatarDouble(listaRotaPagamentoPipeiroDTO);

		
		return listaRotaPagamentoPipeiroDTO;
	}
	
//	private void definirDiasCarradas(List listaRotaPagamentoPipeiroDTO) {
//		
////		int limiteCarrada = listaRotaPagamentoPipeiroDTO.get(0).getCidadeDTO().getCarr_max();
//		
//		
//		//inserir primeiro registro
//		inserirPrimeiroResgistro(listaRotaPagamentoPipeiroDTO);
//		
//		//inserir registros restantes
//		inserirRegistrosRestates(listaRotaPagamentoPipeiroDTO);
//		
//		//inserir sobras
//		inserirSobrasDistrDias(listaRotaPagamentoPipeiroDTO);
//		
//		//Random na lista de PA
//		randomListaPA(listaRotaPagamentoPipeiroDTO);
//		
//
//	}

	private void randomListaPA(RotaPagamentoPipeiroDTO rotaSelecionada) {
		// TODO Auto-generated method stub
		List novaPontoAbastecimentoDTOList = new ArrayList();
		
			Collections.shuffle(rotaSelecionada.getPontoAbastecimentoDTOList());
	}

	private void inserirSobrasDistrDias(RotaPagamentoPipeiroDTO rotaSelecionada) {
		// TODO Auto-generated method stub
		PontoAbastecimentoDTO pa;
		List listaDistribuição;
		List diasDisponivel;
		int proximoIndice;
		DataDTO data = null;
		
		List paRestante = new ArrayList();
		
		
			paRestante.clear();
			paRestante.addAll(rotaSelecionada.getPontoAbastecimentoDTOList());
			
			while(!paRestante.isEmpty()){
			
				for (int j = 0; j < rotaSelecionada.getPontoAbastecimentoDTOList().size(); j++) {
					pa = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(j);
					System.out.println(pa.getComunidade());
					System.out.println("somaDias() "+somaDias(pa));
					System.out.println("getQtdViagensReal() "+pa.getQtdViagensReal());
					if(somaDias(pa) == pa.getQtdViagensReal()){
						paRestante.remove(pa);
						continue;
					}
					
					
					listaDistribuição = new ArrayList();
					diasDisponivel = new ArrayList();
					
					povoarListaDistribuição(listaDistribuição,pa);
					
					proximoIndice = defineProximoIndiceSobras(pa,listaDistribuição);
					
					System.out.println("proximoIndice: "+proximoIndice);
					
					diasDisponiveis(rotaSelecionada,listaDistribuição,diasDisponivel);

					for (int j2 = 0; j2 < diasDisponivel.size(); j2++) {
						DataDTO dt = (DataDTO) diasDisponivel.get(j2);
						
						System.out.println("Dias disponiveis: "+dt.getIndice());
						if(dt.getIndice() >= proximoIndice){
							
							data = dt;
							break;
						}
					}
					
					if(pa.getDistribuicaoDiasPipeiroDTO().getDia1().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia1().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDias()+1);
						rotaSelecionada.setTotalDia1(rotaSelecionada.getTotalDia1()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getIndice() == data.getIndice()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDias()+1);
							rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDias()+1);
						rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDias()+1);
						rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDias()+1);
						rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDias()+1);
						rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDias()+1);
						rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDias()+1);
						rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDias()+1);
						rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDias()+1);
						rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDias()+1);
						rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDias()+1);
						rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDias()+1);
						rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDias()+1);
						rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDias()+1);
						rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDias()+1);
						rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDias()+1);
						rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDias()+1);
						rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDias()+1);
						rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDias()+1);
						rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDias()+1);
						rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDias()+1);
						rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDias()+1);
						rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDias()+1);
						rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDias()+1);
						rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDias()+1);
						rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDias()+1);
						rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDias()+1);
						rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDias()+1);
						rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDias()+1);
						rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+1);
					}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getIndice() == data.getIndice()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDias()+1);
						rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+1);
					}
					
				}
		}
		
		
		
	}

	private int defineProximoIndiceSobras(PontoAbastecimentoDTO pa, List listaDistribuição) {
		// TODO Auto-generated method stub
		
		
		int proximoIndice = 0;
		DataDTO dt;
		for (int i = 0; i < listaDistribuição.size(); i++) {
			dt = (DataDTO) listaDistribuição.get(i);
			
			if(dt.getDias() == 1){
				proximoIndice = dt.getIndice();
				break;
			}
		}
		
		return proximoIndice;
	}

	private void inserirRegistrosRestates(RotaPagamentoPipeiroDTO rotaSelecionada) {
		// TODO Auto-generated method stub
		
		PontoAbastecimentoDTO pa;
		
		List listaDistribuição;
		
		List diasDisponivel;
		
		List paRestante = new ArrayList();
		
		int proximoIndice;
		
		DataDTO data = null;
		
			paRestante.clear();
			paRestante.addAll(rotaSelecionada.getPontoAbastecimentoDTOList());
			
//			while(existirDiasParaDistribuir(rp.getPontoAbastecimentoDTOList())){
			while(!paRestante.isEmpty()){
				
				System.out.println("PA Restante: "+paRestante.size());
				
				for (int j = 0; j < rotaSelecionada.getPontoAbastecimentoDTOList().size(); j++) {
					pa = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(j);
					
					System.out.println("QTD de PAS "+rotaSelecionada.getPontoAbastecimentoDTOList().size());
					System.out.println("Apontador"+pa.getApontador());
					System.out.println("dias de 1 a 31");
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDias());
					System.out.println(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDias());
					
					
					System.out.println("TOTAIS");
					System.out.println(rotaSelecionada.getTotalDia1());
					System.out.println(rotaSelecionada.getTotalDia2());
					System.out.println(rotaSelecionada.getTotalDia3());
					System.out.println(rotaSelecionada.getTotalDia4());
					System.out.println(rotaSelecionada.getTotalDia5());
					System.out.println(rotaSelecionada.getTotalDia6());
					System.out.println(rotaSelecionada.getTotalDia7());
					System.out.println(rotaSelecionada.getTotalDia8());
					System.out.println(rotaSelecionada.getTotalDia9());
					System.out.println(rotaSelecionada.getTotalDia10());
					System.out.println(rotaSelecionada.getTotalDia11());
					System.out.println(rotaSelecionada.getTotalDia12());
					System.out.println(rotaSelecionada.getTotalDia13());
					System.out.println(rotaSelecionada.getTotalDia14());
					System.out.println(rotaSelecionada.getTotalDia15());
					System.out.println(rotaSelecionada.getTotalDia16());
					System.out.println(rotaSelecionada.getTotalDia17());
					System.out.println(rotaSelecionada.getTotalDia18());
					System.out.println(rotaSelecionada.getTotalDia19());
					System.out.println(rotaSelecionada.getTotalDia20());
					System.out.println(rotaSelecionada.getTotalDia21());
					System.out.println(rotaSelecionada.getTotalDia22());
					System.out.println(rotaSelecionada.getTotalDia23());
					System.out.println(rotaSelecionada.getTotalDia24());
					System.out.println(rotaSelecionada.getTotalDia25());
					System.out.println(rotaSelecionada.getTotalDia26());
					System.out.println(rotaSelecionada.getTotalDia27());
					System.out.println(rotaSelecionada.getTotalDia28());
					System.out.println(rotaSelecionada.getTotalDia29());
					System.out.println(rotaSelecionada.getTotalDia30());
					System.out.println(rotaSelecionada.getTotalDia31());
					
					diasDisponivel = new ArrayList();
					listaDistribuição = new ArrayList();
				
					povoarListaDistribuição(listaDistribuição,pa);
					
					proximoIndice = defineProximoIndice(pa,listaDistribuição);
					
					System.out.println("proximoIndice "+proximoIndice);
					
					diasDisponiveis(rotaSelecionada,listaDistribuição,diasDisponivel);
					
					proximoIndice =  proximoIndiceUtil(proximoIndice,diasDisponivel);
					
					System.out.println("proximoIndiceUtil "+proximoIndice);
					
					System.out.println("somaDias() "+somaDias(pa));
					System.out.println("getQtdViagensReal() "+pa.getQtdViagensReal());
					if(somaDias(pa) < pa.getQtdViagensReal()){
						
						for (int j2 = 0; j2 < diasDisponivel.size(); j2++) {
							DataDTO dt = (DataDTO) diasDisponivel.get(j2);
							
							System.out.println("Dias disponiveis: "+dt.getIndice());
							if(dt.getIndice() >= proximoIndice){
								
								data = dt;
								break;
							}
						}
						
						
						if(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia2() < rotaSelecionada.getCidadeDTO().getCarr_max()){
								pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(1);
								rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia3() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(1);
							rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia4() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(1);
							rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia5() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(1);
							rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia6() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(1);
							rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia7() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(1);
							rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia8() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(1);
							rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia9() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(1);
							rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia10() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(1);
							rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia11() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(1);
							rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia12() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(1);
							rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia13() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(1);
							rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia14() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(1);
							rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia15() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(1);
							rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia16() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(1);
							rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia17() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(1);
							rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia18() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(1);
							rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia19() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(1);
							rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia20() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(1);
							rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia21() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(1);
							rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia22() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(1);
							rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia23() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(1);
							rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia24() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(1);
							rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia25() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(1);
							rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia26() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(1);
							rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia27() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(1);
							rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia28() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(1);
							rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia29() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(1);
							rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia30() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(1);
							rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+1);
							}else{
								paRestante.remove(pa);
							}
						}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getIndice() == data.getIndice()){
							if(rotaSelecionada.getTotalDia31() < rotaSelecionada.getCidadeDTO().getCarr_max()){
							pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(1);
							rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+1);
							}else{
								paRestante.remove(pa);
							}
						}
						
					}else{
						System.out.println(pa.getComunidade()+" Já distribuido");
						paRestante.remove(pa);
					}
					
					data = null;
					proximoIndice = 0;
					
				}

			}
		
	}

	private int proximoIndiceUtil(int proximoIndice, List diasDisponivel) {
		// TODO Auto-generated method stub
		
		int proximoIndiceUtil = 0;
		boolean flagOk = false;
		
		for (int i = 0; i < diasDisponivel.size(); i++) {
			DataDTO dt = (DataDTO) diasDisponivel.get(i);
			
			if(dt.getIndice() == proximoIndice){
				proximoIndiceUtil = proximoIndice;
				flagOk = true;
				break;
			}
		}
		
		if(!flagOk){
			
			int maiorValor = 0;
			int menorValor = 0;
			
			for (int i = 0; i < diasDisponivel.size(); i++) {
				DataDTO dt = (DataDTO) diasDisponivel.get(i);
				
				if(i == 0 ){
					menorValor = dt.getIndice();
				}else if(dt.getIndice() < menorValor){
					menorValor = dt.getIndice();
				}
				
				if(i == 0 ){
					maiorValor = dt.getIndice();
				}else if(dt.getIndice() > maiorValor){
					maiorValor = dt.getIndice();
				}
			}
			
			
			if(proximoIndice < menorValor){
				proximoIndiceUtil = menorValor;
			} else if(proximoIndice > maiorValor){
				proximoIndiceUtil = maiorValor;
			}else{
				proximoIndiceUtil = proximoIndice; 
			}
		}
		
		return proximoIndiceUtil;
		
	}

	private void diasDisponiveis(RotaPagamentoPipeiroDTO rp,List listaDistribuição, List diasDisponivel) {
		// TODO Auto-generated method stub
		
		DataDTO dt;
		for (int i = 0; i < listaDistribuição.size(); i++) {
			dt = (DataDTO) listaDistribuição.get(i);
			
			if(!dt.isEhFimDeSemana() && dt.getIndice() != 0){
				diasDisponivel.add(dt);
			}
		}
		
		
		if(rp.getTotalDia1() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(0));
		}else if(rp.getTotalDia2() >= rp.getCidadeDTO().getCarr_max()){
				diasDisponivel.remove((DataDTO) listaDistribuição.get(1));
		}else if(rp.getTotalDia3() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(2));
		}else if(rp.getTotalDia4() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(3));
		}else if(rp.getTotalDia5() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(4));
		}else if(rp.getTotalDia6() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(5));
		}else if(rp.getTotalDia7() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(6));
		}else if(rp.getTotalDia8() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(7));
		}else if(rp.getTotalDia9() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(8));
		}else if(rp.getTotalDia10() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(9));
		}else if(rp.getTotalDia11() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(10));
		}else if(rp.getTotalDia12() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(11));
		}else if(rp.getTotalDia13() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(12));
		}else if(rp.getTotalDia14() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(13));
		}else if(rp.getTotalDia15() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(14));
		}else if(rp.getTotalDia16() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(15));
		}else if(rp.getTotalDia17() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(16));
		}else if(rp.getTotalDia18() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(17));
		}else if(rp.getTotalDia19() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(18));
		}else if(rp.getTotalDia20() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(19));
		}else if(rp.getTotalDia21() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(20));
		}else if(rp.getTotalDia22() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(21));
		}else if(rp.getTotalDia23() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(22));
		}else if(rp.getTotalDia24() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(23));
		}else if(rp.getTotalDia25() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(24));
		}else if(rp.getTotalDia26() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(25));
		}else if(rp.getTotalDia27() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(26));
		}else if(rp.getTotalDia28() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(27));
		}else if(rp.getTotalDia29() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(28));
		}else if(rp.getTotalDia30() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(29));
		}else if(rp.getTotalDia31() >= rp.getCidadeDTO().getCarr_max()){
			diasDisponivel.remove((DataDTO) listaDistribuição.get(30));
		}
		
	}

	private void povoarListaDistribuição(List listaDistribuição, PontoAbastecimentoDTO pa) {
		// TODO Auto-generated method stub
		
		DataDTO dataNetro;
		
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia1();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia2();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO() .getDia3();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia4();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia5();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia6();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia7();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia8();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia9();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia10();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia11();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia12();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia13();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia14();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia15();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia16();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia17();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia18();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia19();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia20();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia21();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia22();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia23();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia24();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia25();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia26();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia27();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia28();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia29();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia30();
		listaDistribuição.add(dataNetro);
		dataNetro = pa.getDistribuicaoDiasPipeiroDTO().getDia31();
		listaDistribuição.add(dataNetro);
		
	}

	private int defineProximoIndice(PontoAbastecimentoDTO pa, List listaDistribuição) {
		
	int intervalo = pa.getQtdCarradas();
	
	System.out.println("intervalo "+intervalo);
	if(intervalo == pa.getQtdViagensReal()){
		intervalo = 1;
	}
	
	int proximoIndice = 0;
	DataDTO dt;
	for (int i = 30; i >= 0; i--) {
		dt = (DataDTO) listaDistribuição.get(i);
		
		if(dt.getDias() != 0 && dt.getIndice() != 0){
			proximoIndice = dt.getIndice()+intervalo;
			break;
		}
	}
	
	if(proximoIndice > 31){
		proximoIndice = 31;
	}
	
	return proximoIndice;
		
	}

//	private boolean existirDiasParaDistribuir(List pontoAbastecimentoDTOList) {
//		// TODO Auto-generated method stub
//		
//		PontoAbastecimentoDTO pa;
//		for (int i = 0; i < pontoAbastecimentoDTOList.size(); i++) {
//			pa = (PontoAbastecimentoDTO) pontoAbastecimentoDTOList.get(i);
//			
//			if(somaDias(pa) < pa.getQtdViagensReal()){
//				System.out.println(""+somaDias(pa));
//				return true;
//			}
//		}
//		return false;
//	}

	private int somaDias(PontoAbastecimentoDTO pa) {
		// TODO Auto-generated method stub
		int totalDiasDistribuido = 
		pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDias()+
		pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDias();
		
		return totalDiasDistribuido;
	}

	private void inserirPrimeiroResgistro(RotaPagamentoPipeiroDTO rotaSelecionada) {
		// TODO Auto-generated method stub
		int indiceUltimoDia = 0;
		
		PontoAbastecimentoDTO pa;
		
		for (int j = 0; j < rotaSelecionada.getPontoAbastecimentoDTOList().size(); j++) {
			pa = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(j);	
			
			if (j == 0 && !pa.getDistribuicaoDiasPipeiroDTO().getDia1().isEhFimDeSemana()){
				pa.getDistribuicaoDiasPipeiroDTO().getDia1().setDias(1);
				rotaSelecionada.setTotalDia1(1);
				indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia1().getIndice();
				continue;
			}else if(j == 0 && !pa.getDistribuicaoDiasPipeiroDTO().getDia2().isEhFimDeSemana()){
				pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(1);
				rotaSelecionada.setTotalDia2(1);
				indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia2().getIndice();
				continue;
			}else{
				
				if(j == 0){
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(1);
					rotaSelecionada.setTotalDia3(1);
					indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia3().getIndice();
					continue;
				}
				
				
				if(indiceUltimoDia == 1 ){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia2().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(1);
						rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia2().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia3().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(1);
						rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia3().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(1);
						rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia4().getIndice();
						continue;
					}

					
				}else if(indiceUltimoDia == 2){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia3().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(1);
						rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia3().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia4().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(1);
						rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia4().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(1);
						rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia5().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 3){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia4().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(1);
						rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia4().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia5().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(1);
						rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia5().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(1);
						rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia6().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 4){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia5().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(1);
						rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia5().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia6().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(1);
						rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia6().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(1);
						rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia7().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 5){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia6().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(1);
						rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia6().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia7().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(1);
						rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia7().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(1);
						rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia8().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 6){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia7().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(1);
						rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia7().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia8().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(1);
						rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia8().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(1);
						rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia9().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 7){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia8().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(1);
						rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia8().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia9().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(1);
						rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia9().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(1);
						rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia10().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 8){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia9().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(1);
						rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia9().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia10().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(1);
						rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia10().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(1);
						rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia11().getIndice();
						continue;
					}
				}else if(indiceUltimoDia == 9){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia10().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(1);
						rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia10().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia11().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(1);
						rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia11().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(1);
						rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia12().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 10){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia11().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(1);
						rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia11().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia12().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(1);
						rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia12().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(1);
						rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia13().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 11){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia12().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(1);
						rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia12().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia13().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(1);
						rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia13().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(1);
						rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia14().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 12){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia13().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(1);
						rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia13().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia14().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(1);
						rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia14().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(1);
						rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia15().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 13){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia14().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(1);
						rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia14().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia15().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(1);
						rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia15().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(1);
						rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia16().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 14){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia15().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(1);
						rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia15().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia16().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(1);
						rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia16().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(1);
						rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia17().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 15){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia16().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(1);
						rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia16().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia17().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(1);
						rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia17().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(1);
						rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia18().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 16){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia17().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(1);
						rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia17().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia18().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(1);
						rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia18().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(1);
						rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia19().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 17){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia18().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(1);
						rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia18().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia19().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(1);
						rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia19().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(1);
						rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia20().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 18){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia19().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(1);
						rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia19().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia20().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(1);
						rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia20().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(1);
						rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia21().getIndice();
						continue;
					}
				}else if(indiceUltimoDia == 19){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia20().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(1);
						rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia20().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia21().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(1);
						rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia21().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(1);
						rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia22().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 20){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia21().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(1);
						rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia21().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia22().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(1);
						rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia22().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(1);
						rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia23().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 21){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia22().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(1);
						rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia22().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia23().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(1);
						rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia23().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(1);
						rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia24().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 22){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia23().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(1);
						rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia23().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia24().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(1);
						rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia24().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(1);
						rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia25().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 23){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia24().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(1);
						rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia24().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia25().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(1);
						rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia25().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(1);
						rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia26().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 24){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia25().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(1);
						rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia25().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia26().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(1);
						rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia26().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(1);
						rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia27().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 25){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia26().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(1);
						rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia26().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia27().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(1);
						rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia27().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(1);
						rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia28().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 26){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia27().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(1);
						rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia27().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia28().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(1);
						rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia28().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(1);
						rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia29().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 27){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia28().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(1);
						rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia28().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia29().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(1);
						rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia29().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(1);
						rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia30().getIndice();
						continue;
					}
					
				}else if(indiceUltimoDia == 28){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia29().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(1);
						rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia29().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia30().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(1);
						rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia30().getIndice();
						continue;
					}else{
						pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(1);
						rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia31().getIndice();
						continue;
					}
				}else if(indiceUltimoDia == 29){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia30().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(1);
						rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia30().getIndice();
						continue;
					}else if(!pa.getDistribuicaoDiasPipeiroDTO().getDia31().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(1);
						rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia31().getIndice();
						continue;
					}		
				}else if(indiceUltimoDia == 30){
					
					if(!pa.getDistribuicaoDiasPipeiroDTO().getDia31().isEhFimDeSemana()){
						pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(1);
						rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+1);
						indiceUltimoDia = pa.getDistribuicaoDiasPipeiroDTO().getDia31().getIndice();
						continue;
					}	
				}
				
			}
			
			
			
		}
		
	}

	private void distribuirDiasCarrada(PontoAbastecimentoDTO pontoAbastecimentoDTO, List<DataDTO> datasDistribuicao) {

		int contadorDiasUteis = 0;
		int contadorQuantCarradas = 0;
		DataDTO dataObj;
		
		for (int i = 0; i < datasDistribuicao.size(); i++) {

			dataObj = datasDistribuicao.get(i);
			
			DataDTO data = new DataDTO(dataObj.getData(), dataObj.getDatasExtenso(), dataObj.getIndice(), dataObj.isEhFimDeSemana(), dataObj.isExibirColuna());
			
//			if(!data.isEhFimDeSemana()){
				contadorDiasUteis++;
//			}
			
			switch( i )
			{
			    case 0:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia1(data);
			            break;
			    
			    case 1:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia2(data);
			            break;
			    
			    case 2:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia3(data);
			            break;
			    case 3:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia4(data);
		            break;
			    case 4:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia5(data);
		            break;
			    case 5:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia6(data);
		            break;
			    case 6:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia7(data);
		            break;
			    case 7:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia8(data);
		            break;
			    case 8:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia9(data);
		            break;
			    case 9:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia10(data);
		            break;
			    case 10:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia11(data);
		            break;
			    case 11:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia12(data);
		            break;
			    case 12:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia13(data);
		            break;
			    case 13:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia14(data);
		            break;
			    case 14:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia15(data);
		            break;
			    case 15:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia16(data);
		            break;
			    case 16:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia17(data);
		            break;
			    case 17:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia18(data);
		            break;
			    case 18:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia19(data);
		            break;
			    case 19:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia20(data);
		            break;
			    case 20:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia21(data);
		            break;
			    case 21:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia22(data);
		            break;
			    case 22:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia23(data);
		            break;
			    case 23:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia24(data);
		            break;
			    case 24:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia25(data);
		            break;
			    case 25:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia26(data);
		            break;
			    case 26:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia27(data);
		            break;
			    case 27:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia28(data);
		            break;
			    case 28:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia29(data);
		            break;
			    case 29:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia30(data);
		            break;
			    case 30:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia31(data);
		            break;
			}
			
		}
			
			
		contadorQuantCarradas = contadorDiasUteis / pontoAbastecimentoDTO.getQtdViagensReal();
		
		
		if(contadorQuantCarradas == 0){
			contadorQuantCarradas = 1;
		}
		
		pontoAbastecimentoDTO.setQtdCarradas(contadorQuantCarradas);
		
		}
	

	private void formatarDouble(List listaRotaPagamentoPipeiroDTO) {

		for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
			RotaPagamentoPipeiroDTO rp = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO
					.get(i);

			rp.setTotalQtdAgua(Funcoes.formatarDouble(rp.getTotalQtdAgua()));
			rp.setTotalViagensFormula(Funcoes.formatarDouble(rp
					.getTotalViagensFormula()));
			rp.setValorTotalPA(Funcoes.formatarDouble(rp.getValorTotalPA()));

			for (int j = 0; j < rp.getPontoAbastecimentoDTOList().size(); j++) {
				PontoAbastecimentoDTO pa = (PontoAbastecimentoDTO) rp
						.getPontoAbastecimentoDTOList().get(j);

				pa.setCapacidadePipa(Funcoes.formatarDouble(pa
						.getCapacidadePipa()));
				pa.setDistancia(Funcoes.formatarDouble(pa.getDistancia()));
				pa.setQtdAgua(Funcoes.formatarDouble(pa.getQtdAgua()));
				pa.setQtdViagensFormula(Funcoes.formatarDouble(pa
						.getQtdViagensFormula()));
				pa.setValorBruto(Funcoes.formatarDouble(pa.getValorBruto()));
			}
		}
	}

	@Override
	public void calcularRotaPagamentoManualmente(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList,PontoAbastecimentoDTO pontoAbastecimentoSelecionado,PipeiroDTO pipeiroSelecionado, 
			CidadeDTO cidadeDTO, Calendar data,List<DataDTO> datasDistribuicao, int totalDias) {
		// TODO Auto-generated method stub
		RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTO;
		int flagPipeiroExiste = 0;
		
			int qtdDiasMes = totalDias;
		
			String mesAno = Funcoes.obterAnoMesExtenso(data.get(Calendar.MONTH), data.get(Calendar.YEAR));
			
			//Se o pipeiro existe, adiciona o Ponto de Abastecimento
			RotaPagamentoPipeiroDTO rp1;
			for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
				rp1 = rotaPagamentoPipeiroDTOList.get(i);
				
				if(rp1.getPipeiroDTO() != null){
					if(rp1.getPipeiroDTO().getId() == pipeiroSelecionado.getId()){
						flagPipeiroExiste = 1;
						adicionarPontoAbastecimento(rp1, pontoAbastecimentoSelecionado,pipeiroSelecionado, qtdDiasMes);
						rp1.setMesAno(mesAno);
						
						distribuirDiasCarrada(pontoAbastecimentoSelecionado,datasDistribuicao);
					}
				}
			}
			
			//Se não existir o Pipeiro, adiciona o Pipeiro e atribui o Ponto de Abastecimento para ele
			if(flagPipeiroExiste == 0){
				rotaPagamentoPipeiroDTO = new RotaPagamentoPipeiroDTO();
				
				rotaPagamentoPipeiroDTO.setPipeiroDTO(pipeiroSelecionado);
				rotaPagamentoPipeiroDTO.setCidadeDTO(cidadeDTO);
				rotaPagamentoPipeiroDTO.setLitrosPessoa(Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa")));
				rotaPagamentoPipeiroDTO.setNumDiasMes(qtdDiasMes);
				rotaPagamentoPipeiroDTO.setMesAno(mesAno);
				
				setValoresRotaPagamentoPontoAbacimento(pontoAbastecimentoSelecionado,rotaPagamentoPipeiroDTO,qtdDiasMes);
				
				rotaPagamentoPipeiroDTO.getPontoAbastecimentoDTOList().add(pontoAbastecimentoSelecionado);
				
				rotaPagamentoPipeiroDTOList.add(rotaPagamentoPipeiroDTO);
				
				distribuirDiasCarrada(pontoAbastecimentoSelecionado,datasDistribuicao);
			}
		
			formatarDouble(rotaPagamentoPipeiroDTOList);
		
	}

	private void definirRotaEquivalente(List listaRotaPagamentoPipeiroDTO, int qtdDiasMes) {
		
//		RotaPagamentoPipeiroDTO primeiroValor = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(0);
		double maiorValor;
		double menorValor;
		RotaPagamentoPipeiroDTO maiorValorObj = null;
		RotaPagamentoPipeiroDTO menorValorObj = null;
		
//		List listaRotaPagamentoPipeiroDTOTemp = new ArrayList();
//		listaRotaPagamentoPipeiroDTOTemp.addAll(listaRotaPagamentoPipeiroDTO);
		
		int limitador = 0;
		
		while(existeDivergencia(listaRotaPagamentoPipeiroDTO) && limitador < 40){
			
			 maiorValor = 0;
			 menorValor = 0;
			 maiorValorObj = null;
			 menorValorObj = null;
			 
			 limitador++;
			 
			 System.out.println("limitador:"+ limitador);
		
		//Recuperar maior e menor valor bruto
		RotaPagamentoPipeiroDTO rp;
		for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
			rp = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(i);
			System.out.println(""+rp.getValorTotalPA());
			
			if(i == 0){
				maiorValor = rp.getValorTotalPA();
				maiorValorObj = rp;
			}else if(rp.getValorTotalPA() > maiorValor){
				maiorValor = rp.getValorTotalPA();
				maiorValorObj = rp;
			}
			
			if(i == 0){
				menorValor = rp.getValorTotalPA(); 
				menorValorObj = rp;
			}else if(rp.getValorTotalPA() < menorValor){
				menorValor = rp.getValorTotalPA(); 
				menorValorObj = rp;
			}
			
		}
		
		System.out.println("menor valor: "+menorValor);
		System.out.println("maoir valor: "+maiorValor);
		
			//Remover Ponto de menor população
			RotaPagamentoPipeiroDTO rp3;
			PontoAbastecimentoDTO pontoRemovido = null;
//			PontoAbastecimentoDTO pontoRemovidoTemp = null;
			for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
				rp3 = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(i);

				if (rp3 == maiorValorObj) {
					PontoAbastecimentoDTO pontoAbastecimentoDTO = recuperarPontoMenorPopulacao(rp3.getPontoAbastecimentoDTOList());
					System.out.println("Ponto de menor populacao:"+pontoAbastecimentoDTO.getComunidade()+" "+pontoAbastecimentoDTO.getPopulacao());
					pontoRemovido = pontoAbastecimentoDTO;
					removerPontoAbastecimento(rp3,pontoRemovido);
					break;
				}
			}
			
			
			//Adicionando Ponto de menor população
			RotaPagamentoPipeiroDTO rp4;
			for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
				rp4 = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(i);

				if (rp4 == menorValorObj) {
					adicionarPontoAbastecimento(rp4,pontoRemovido,rp4.getPipeiroDTO(),qtdDiasMes);
					break;
				}
			}
		
		}
		
	}

	private void removerPontoAbastecimento(RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA,PontoAbastecimentoDTO pontoAbastecimentoDTO) {
		
		System.out.println("removendoPonto");
		
		rotaPagamentoPipeiroDTOPA.setValorTotalPA(rotaPagamentoPipeiroDTOPA.getValorTotalPA()-pontoAbastecimentoDTO.getValorBruto());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensFormula(rotaPagamentoPipeiroDTOPA.getTotalViagensFormula()-pontoAbastecimentoDTO.getQtdViagensFormula());
		
		rotaPagamentoPipeiroDTOPA.setTotalApontadores(rotaPagamentoPipeiroDTOPA.getTotalApontadores()-1);
		
		rotaPagamentoPipeiroDTOPA.setTotalPessoas(rotaPagamentoPipeiroDTOPA.getTotalPessoas()-pontoAbastecimentoDTO.getPopulacao());
		
		rotaPagamentoPipeiroDTOPA.setTotalQtdAgua(rotaPagamentoPipeiroDTOPA.getTotalQtdAgua()-pontoAbastecimentoDTO.getQtdAgua());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensReal(rotaPagamentoPipeiroDTOPA.getTotalViagensReal()-pontoAbastecimentoDTO.getQtdViagensReal());
		
		rotaPagamentoPipeiroDTOPA.setTotalDistacia(rotaPagamentoPipeiroDTOPA.getTotalDistacia()- pontoAbastecimentoDTO.getKmPercorridoPA());
		
		rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().remove(pontoAbastecimentoDTO);
		
		StringBuffer teste = new StringBuffer();
		
		teste.append(rotaPagamentoPipeiroDTOPA.getValorTotalPA()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalViagensFormula()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalApontadores()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalPessoas()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalQtdAgua()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalViagensReal()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalDistacia()+"\n");
		
		System.out.println(""+teste.toString());
		
		}

	private void adicionarPontoAbastecimento(RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA, PontoAbastecimentoDTO pontoAbastecimentoDTO, PipeiroDTO pipeiroSelecionado, int qtdDiasMes) {
		
		
		System.out.println("ADDPonto");
		
		pontoAbastecimentoDTO.setQtdViagensFormula(recuperarViagensFormula(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes));
		
		pontoAbastecimentoDTO.setQtdViagensReal(recuperarViagensReal(pontoAbastecimentoDTO.getQtdViagensFormula()));
		
		pontoAbastecimentoDTO.setQtdAgua(recuperaQtdAgua(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA));
		
		pontoAbastecimentoDTO.setKmPercorridoPA(recuperaKmPercorrido(pontoAbastecimentoDTO));
		
		pontoAbastecimentoDTO.setValorBruto(recuperaValorBruto(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA));
		
		pontoAbastecimentoDTO.setCapacidadePipa(pipeiroSelecionado.getVeiculoDTO().getCapacidadePipa());
		
		rotaPagamentoPipeiroDTOPA.setValorTotalPA(rotaPagamentoPipeiroDTOPA.getValorTotalPA()+pontoAbastecimentoDTO.getValorBruto());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensFormula(rotaPagamentoPipeiroDTOPA.getTotalViagensFormula()+pontoAbastecimentoDTO.getQtdViagensFormula());
		
		rotaPagamentoPipeiroDTOPA.setTotalApontadores(rotaPagamentoPipeiroDTOPA.getTotalApontadores()+1);
		
		rotaPagamentoPipeiroDTOPA.setTotalPessoas(rotaPagamentoPipeiroDTOPA.getTotalPessoas()+pontoAbastecimentoDTO.getPopulacao());
		
		rotaPagamentoPipeiroDTOPA.setTotalQtdAgua(rotaPagamentoPipeiroDTOPA.getTotalQtdAgua()+pontoAbastecimentoDTO.getQtdAgua());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensReal(rotaPagamentoPipeiroDTOPA.getTotalViagensReal()+pontoAbastecimentoDTO.getQtdViagensReal());
		
		rotaPagamentoPipeiroDTOPA.setTotalDistacia(rotaPagamentoPipeiroDTOPA.getTotalDistacia()+ pontoAbastecimentoDTO.getKmPercorridoPA());
		
		rotaPagamentoPipeiroDTOPA.getPontoAbastecimentoDTOList().add(pontoAbastecimentoDTO);
		
		StringBuffer teste = new StringBuffer();
		
		teste.append(rotaPagamentoPipeiroDTOPA.getValorTotalPA()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalViagensFormula()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalApontadores()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalPessoas()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalQtdAgua()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalViagensReal()+"\n");
		teste.append(rotaPagamentoPipeiroDTOPA.getTotalDistacia()+"\n");
		
		System.out.println(""+teste.toString());
	}

	private boolean existeDivergencia(List listaRotaPagamentoPipeiroDTO) {

		RotaPagamentoPipeiroDTO primeiroValor = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(0);
		double maiorValor = primeiroValor.getValorTotalPA();
		double menorValor = primeiroValor.getValorTotalPA();
		
		RotaPagamentoPipeiroDTO rp;
		
		for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
			rp = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(i);
		
			if(rp.getValorTotalPA() > maiorValor){
				maiorValor = rp.getValorTotalPA(); 
			}
			
			if(rp.getValorTotalPA() < menorValor){
				menorValor = rp.getValorTotalPA(); 
			}
		
		}
		
		if((maiorValor-menorValor) < 300.00){
			return false;
		}else{
			return true;
		}
	}

	private PontoAbastecimentoDTO recuperarPontoMenorPopulacao(List pontoAbastecimentoDTOList) {
		PontoAbastecimentoDTO paRetorno = null;
		int menorValor = 0;
		
		PontoAbastecimentoDTO pa;
		for (int i = 0; i < pontoAbastecimentoDTOList.size(); i++) {
			
			pa = (PontoAbastecimentoDTO) pontoAbastecimentoDTOList.get(i);
			
			System.out.println("Lista de pontos: "+pa.getComunidade()+" "+pa.getPopulacao());
			
			if(i == 0){
				menorValor = pa.getPopulacao();
				paRetorno = pa;
			}else if(pa.getPopulacao() < menorValor){
				menorValor = pa.getPopulacao();
				paRetorno = pa;
			}
			
		}
		
		return paRetorno;
	}

	private double recuperaMenorValor(List listaRotaPagamentoPipeiroDTO) {

		RotaPagamentoPipeiroDTO pa;
		double menorValor = 0;
		
		for (int j = 0; j < listaRotaPagamentoPipeiroDTO.size(); j++) {
			
			pa = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(j);	
			
			if(j == 0){
				menorValor = pa.getValorTotalPA();
			}else if (pa.getValorTotalPA() < menorValor){
				menorValor = pa.getValorTotalPA();
			}
			
		}
		
		return menorValor;
		
	}

	private boolean validaTodosReceberamPrimeiraLocalidade(List listaRotaPagamentoPipeiroDTO) {
		// TODO Auto-generated method stub
		int contador = 0;
		int totalRota = listaRotaPagamentoPipeiroDTO.size();
			
			for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
				RotaPagamentoPipeiroDTO pa = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO.get(i);
				
				if(!Funcoes.isListaNulaOuVazia(pa.getPontoAbastecimentoDTOList())){
					contador++;
				}
			}
			
		if(totalRota == contador){
			return true;
		}else{
			return false;
		}
		
	}

	private void setValoresRotaPagamentoPontoAbacimento(PontoAbastecimentoDTO pontoAbastecimentoDTO,RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA, int qtdDiasMes) {
		
		pontoAbastecimentoDTO.setQtdViagensFormula(recuperarViagensFormula(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA,qtdDiasMes));
		
		pontoAbastecimentoDTO.setQtdViagensReal(recuperarViagensReal(pontoAbastecimentoDTO.getQtdViagensFormula()));
		
		pontoAbastecimentoDTO.setQtdAgua(recuperaQtdAgua(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA));
		
		pontoAbastecimentoDTO.setKmPercorridoPA(recuperaKmPercorrido(pontoAbastecimentoDTO));
		
		pontoAbastecimentoDTO.setValorBruto(recuperaValorBruto(pontoAbastecimentoDTO,rotaPagamentoPipeiroDTOPA));
		
		pontoAbastecimentoDTO.setCapacidadePipa(rotaPagamentoPipeiroDTOPA.getPipeiroDTO().getVeiculoDTO().getCapacidadePipa());
		
		
		rotaPagamentoPipeiroDTOPA.setValorTotalPA(rotaPagamentoPipeiroDTOPA.getValorTotalPA()+pontoAbastecimentoDTO.getValorBruto());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensFormula(rotaPagamentoPipeiroDTOPA.getTotalViagensFormula()+pontoAbastecimentoDTO.getQtdViagensFormula());
		
		rotaPagamentoPipeiroDTOPA.setTotalApontadores(rotaPagamentoPipeiroDTOPA.getTotalApontadores()+1);
		
		rotaPagamentoPipeiroDTOPA.setTotalPessoas(rotaPagamentoPipeiroDTOPA.getTotalPessoas()+pontoAbastecimentoDTO.getPopulacao());
		
		rotaPagamentoPipeiroDTOPA.setTotalQtdAgua(rotaPagamentoPipeiroDTOPA.getTotalQtdAgua()+pontoAbastecimentoDTO.getQtdAgua());
		
		rotaPagamentoPipeiroDTOPA.setTotalViagensReal(rotaPagamentoPipeiroDTOPA.getTotalViagensReal()+pontoAbastecimentoDTO.getQtdViagensReal());
		
		rotaPagamentoPipeiroDTOPA.setTotalDistacia(rotaPagamentoPipeiroDTOPA.getTotalDistacia()+ (int) pontoAbastecimentoDTO.getKmPercorridoPA());
	}

	private double recuperaValorBruto(PontoAbastecimentoDTO pontoAbastecimentoDTO,RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA) {
		
		double valorBruto;
		
		double momento = pontoAbastecimentoDTO.getIndice();
		double capacidadePipa = rotaPagamentoPipeiroDTOPA.getPipeiroDTO().getVeiculoDTO().getCapacidadePipa();
		int qtdViagemReal = pontoAbastecimentoDTO.getQtdViagensReal();
		int distancia = (int) pontoAbastecimentoDTO.getDistancia();
		
		
		valorBruto = momento*capacidadePipa*qtdViagemReal*distancia;
		
		return valorBruto;
	}

	private int recuperaKmPercorrido(PontoAbastecimentoDTO pontoAbastecimentoDTO) {
		
		int qtdViagemReal = pontoAbastecimentoDTO.getQtdViagensReal();
		int distancia = (int) pontoAbastecimentoDTO.getDistancia();
		
		return qtdViagemReal*distancia;
	}

	private double recuperaQtdAgua(PontoAbastecimentoDTO pontoAbastecimentoDTO, RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA) {
		double qtdAgua;
		
		double capacidadePipa = rotaPagamentoPipeiroDTOPA.getPipeiroDTO().getVeiculoDTO().getCapacidadePipa();
		int qtdViagemReal = pontoAbastecimentoDTO.getQtdViagensReal();
		
		qtdAgua = capacidadePipa*qtdViagemReal;
		
		return qtdAgua;
	}

	private int recuperarViagensReal(double qtdViagensFormula) {
		
		int viagensReal;
		
		int arredondado = (int) Math.floor(qtdViagensFormula);
//		int truncado = (int) Math.ceil(qtdViagensFormula);
		
		if((qtdViagensFormula-arredondado)>=0.095 || arredondado == 0){
			
			viagensReal = arredondado+1;
			
		}else{
			
			viagensReal = arredondado;
			
		}
		
		
		return viagensReal;
	}

	private double recuperarViagensFormula(PontoAbastecimentoDTO pontoAbastecimentoDTO,RotaPagamentoPipeiroDTO rotaPagamentoPipeiroDTOPA, int qtdDiasMes) {
		
		double viagensFormula;
//		int qtdPessoas = pontoAbastecimentoDTO.getPopulacao();
//		int litrosPessoa = rotaPagamentoPipeiroDTOPA.getLitrosPessoa();
		int litrosDiario = pontoAbastecimentoDTO.getLitrosDiario();
		double capacidadePipa = rotaPagamentoPipeiroDTOPA.getPipeiroDTO().getVeiculoDTO().getCapacidadePipa();
		
//		viagensFormula = (qtdPessoas*qtdDiasMes*litrosPessoa)/capacidadePipa/1000;
		
		viagensFormula = (litrosDiario*qtdDiasMes)/capacidadePipa/1000;
		
		return viagensFormula;
	}

	@Override
	public void removerPontoAbastecimentoSelecionado(RotaPagamentoPipeiroDTO rp, PontoAbastecimentoDTO pontoSelecionado) {

		removerPontoAbastecimento(rp, pontoSelecionado);
		
	}

	@Override
	public void definirDiasCarradas(RotaPagamentoPipeiroDTO rotaSelecionada) {
		// TODO Auto-generated method stub
	
		//reseta a distribuição
		zerarDias(rotaSelecionada);
		
		//inserir primeiro registro
		inserirPrimeiroResgistro(rotaSelecionada);
				
		//inserir registros restantes
		inserirRegistrosRestates(rotaSelecionada);
				
		//inserir sobras
		inserirSobrasDistrDias(rotaSelecionada);
			
		//Random na lista de PA
		randomListaPA(rotaSelecionada);

		
	}

	private void zerarDias(RotaPagamentoPipeiroDTO rotaSelecionada) {
		
		rotaSelecionada.setTotalDia1(0);
		rotaSelecionada.setTotalDia2(0);
		rotaSelecionada.setTotalDia3(0);
		rotaSelecionada.setTotalDia4(0);
		rotaSelecionada.setTotalDia5(0);
		rotaSelecionada.setTotalDia6(0);
		rotaSelecionada.setTotalDia7(0);
		rotaSelecionada.setTotalDia8(0);
		rotaSelecionada.setTotalDia9(0);
		rotaSelecionada.setTotalDia10(0);
		rotaSelecionada.setTotalDia11(0);
		rotaSelecionada.setTotalDia12(0);
		rotaSelecionada.setTotalDia13(0);
		rotaSelecionada.setTotalDia14(0);
		rotaSelecionada.setTotalDia15(0);
		rotaSelecionada.setTotalDia16(0);
		rotaSelecionada.setTotalDia17(0);
		rotaSelecionada.setTotalDia18(0);
		rotaSelecionada.setTotalDia19(0);
		rotaSelecionada.setTotalDia20(0);
		rotaSelecionada.setTotalDia21(0);
		rotaSelecionada.setTotalDia22(0);
		rotaSelecionada.setTotalDia23(0);
		rotaSelecionada.setTotalDia24(0);
		rotaSelecionada.setTotalDia25(0);
		rotaSelecionada.setTotalDia26(0);
		rotaSelecionada.setTotalDia27(0);
		rotaSelecionada.setTotalDia28(0);
		rotaSelecionada.setTotalDia29(0);
		rotaSelecionada.setTotalDia30(0);
		rotaSelecionada.setTotalDia31(0);
		
		PontoAbastecimentoDTO pa;
		for (int j = 0; j < rotaSelecionada.getPontoAbastecimentoDTOList().size(); j++) {
			pa = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(j);
			
			pa.getDistribuicaoDiasPipeiroDTO().getDia1().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(0);
			pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(0);
		}
	}

	@Override
	public void recalcularPontoAbastecimento(RotaPagamentoPipeiroDTO rp,PontoAbastecimentoDTO pontoSelecionado, int reducao, String obs) {
		// TODO Auto-generated method stub
		
			
			//Removendo TOTAL antigo
			rp.setValorTotalPA(rp.getValorTotalPA()-pontoSelecionado.getValorBruto());
			
			rp.setTotalQtdAgua(rp.getTotalQtdAgua()-pontoSelecionado.getQtdAgua());
			
			rp.setTotalViagensReal(rp.getTotalViagensReal()-pontoSelecionado.getQtdViagensReal());
			
			rp.setTotalDistacia(rp.getTotalDistacia()- pontoSelecionado.getKmPercorridoPA());
		

			//Setando novos valores do Ponto de Abastecimento
			pontoSelecionado.setQtdViagensReal(reducao);
			
			pontoSelecionado.setQtdAgua(recuperaQtdAgua(pontoSelecionado,rp));
			
			pontoSelecionado.setKmPercorridoPA(recuperaKmPercorrido(pontoSelecionado));
			
			pontoSelecionado.setValorBruto(recuperaValorBruto(pontoSelecionado,rp));
			
			pontoSelecionado.setObs(obs);
			
			
			//Atribuindo novo Total
			rp.setValorTotalPA(rp.getValorTotalPA()+pontoSelecionado.getValorBruto());
			
			rp.setTotalQtdAgua(rp.getTotalQtdAgua()+pontoSelecionado.getQtdAgua());
			
			rp.setTotalViagensReal(rp.getTotalViagensReal()+pontoSelecionado.getQtdViagensReal());
			
			rp.setTotalDistacia(rp.getTotalDistacia()+ (int) pontoSelecionado.getKmPercorridoPA());
		
		
	}

}
