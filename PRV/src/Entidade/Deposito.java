package Entidade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import IO.ManipulaDados;

public class Deposito {
	
	private float coordenadaX;
	private float coordenadaY;
	private Veiculo veiculo;
	ArrayList<Integer> listaColunas =  new ArrayList<Integer>();	
	private ArrayList<Rota> listaRota = new ArrayList<>();
	private HashMap<Integer, Cliente> listaCliente = new HashMap<>(); 
	private float[][] matrizCustos;
	private HashMap<Integer, Float> listaDemanda = new HashMap<>();

	public Deposito() {
		// TODO Auto-generated constructor stub
	}

	public float getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(float coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public float getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(float coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public ArrayList<Rota> getListaRota() {
		return listaRota;
	}

	public void setListaRota(ArrayList<Rota> listaRota) {
		this.listaRota = listaRota;
	}

	public float[][] getMatrizCustos() {
		return matrizCustos;
	}

	public void setMatrizCustos(float[][] matrizCustos) {
		this.matrizCustos = matrizCustos;
	}

	public HashMap<Integer, Cliente> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(HashMap<Integer, Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public void criaCliente(String identificador, String coordenadaX, String coordenadaY,
			String demanda, String tempoInicio, String tempoFim, String string7) {
		
		Cliente cliente = new Cliente(Integer.parseInt(identificador)-1, Float.parseFloat(coordenadaX), Float.parseFloat(coordenadaY), 
										Float.parseFloat(demanda), Float.parseFloat(tempoInicio), Float.parseFloat(tempoFim));
		
		this.listaCliente.put(Integer.parseInt(identificador), cliente);
	}

	public void criaVeiculo(String capacidade) {
		this.veiculo = new Veiculo();
		this.veiculo.setCapacidade(Integer.parseInt(capacidade));
	}
	
	public void populaMatrizCusto() {
		int tamanho = this.listaCliente.size();
		this.matrizCustos = new float[tamanho][tamanho];
		
		for(int i = 0; i <= this.matrizCustos.length - 1; i++){
			for(int j = 0; j <= this.matrizCustos.length - 1; j++){			
				this.matrizCustos[i][j] = calculaDistancia(this.listaCliente.get(i+1).getCoordenadaX(), this.listaCliente.get(j+1).getCoordenadaX(), 
															   this.listaCliente.get(i+1).getCoordenadaY(), this.listaCliente.get(j+1).getCoordenadaY());
			}
		}
		
		ManipulaDados manipula = new ManipulaDados();
		manipula.escreveArquivo(this.matrizCustos, "matrizCusto.txt");
		
	}
	
	public float calculaDistancia(float x1, float x2, float y1, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
	}
	
	public void populaListaDemanda() {
		for (int i=0; i <= this.listaCliente.size() - 1; i++) {
			this.listaDemanda.put(this.listaCliente.get(i+1).getIdentificador(), this.listaCliente.get(i+1).getDemanda());
		}
	}
	
	public ArrayList<Rota> buscaTabu() {
		ArrayList<Rota> solucao = criaSolucaoInicial();
		for (Rota rota2 : solucao) {
			System.out.print("Rota - " + "Distancia: " + rota2.getCustoTotal()+ " - ");
			//System.out.println(" - Demanda: " + custoDemanda(rota2));
			for (Cliente cliente : rota2.getListaCliente()) {
				System.out.print(cliente.getIdentificador() + ";");
			}
			System.out.println("");
		}
		
		/*for (Rota rota2 : solucao) {
			System.out.print("Rota - " + "Distancia: " + rota2.getCustoTotal());
			System.out.println(" - Demanda: " + custoDemanda(rota2));
			for (Cliente cliente : rota2.getListaCliente()) {
				System.out.println(cliente.getIdentificador() + "; " + cliente.getDemanda());
				
				if(cliente.getIdentificador() == 0){
					System.out.println(cliente.getCoordenadaX()+";"+cliente.getCoordenadaY());
				} 
			}
			System.out.println("");
		}*/
		
		/*int iter = 0;
		int melhorIter = 0;
		int btMax = 100;
		ArrayList<Rota> solucaoTemp = null;
		while((iter - melhorIter) <= btMax){
			iter++;
			
			solucaoTemp = criaSolucaoTroca(solucao);
			
			if(funcaoObjetivo(solucaoTemp) < funcaoObjetivo(solucao)){
				solucao = solucaoTemp;
				melhorIter = iter;
			}			
		}*/
		
		System.out.println("---------NOVA SOLUCAO--------");

		ArrayList<Rota> solucaoTemp = criaSolucaoTroca(solucao);
		
		for (Rota rota2 : solucaoTemp) {
			System.out.print("Rota - " + "Distancia: " + rota2.getCustoTotal() + " - ");
			//System.out.println(" - Demanda: " + custoDemanda(rota2));
			for (Cliente cliente : rota2.getListaCliente()) {
				System.out.print(cliente.getIdentificador() + ";");
			}
			System.out.println("");
		}
		
		return solucao;
	}
	
	public ArrayList<Rota> criaSolucaoTroca(ArrayList<Rota> solucao){
		ArrayList<Rota> solucaoCriada = solucao;
		
		Rota rotaBase = new Rota();
		int custoTemp = 0;
		int indexRotaBase = -1;
		
		//Seleciona Rota Base
		for(int c = 0; c < solucao.size(); c++){			
						
			custoTemp = solucao.get(c).getCustoTotal();
			rotaBase = solucao.get(c);
			indexRotaBase = c;
						
			//Seleciona elemento da rota base
	 		for(int z = 1; z < rotaBase.getListaCliente().size() - 1; z++){	 			
	 				 			
	 			Cliente clienteBase = rotaBase.getListaCliente().get(z);				
				custoTemp = 99999999;
				Rota rotaBaseTemp = rotaBase;
				
				if(clienteBase.getIdentificador() != 0){
					//Seleciona Rota
					for(int i = 0; i < solucao.size(); i++){
						if(i != indexRotaBase){
							//Seleciona elemento rota
							ArrayList<Cliente> listaClienteTemp = solucao.get(i).getListaCliente();
							for(int j = 1; j < solucao.get(i).getListaCliente().size() - 1; j++){
								
								if(solucao.get(i).getListaCliente().get(j).getIdentificador() != 0){																
																		
									//Verifica se ultrapassa capacidade do veiculo
									if(custoDemanda(rotaBaseTemp) - clienteBase.getDemanda() + listaClienteTemp.get(j).getDemanda() > this.veiculo.getCapacidade()
									   && custoDemanda(solucao.get(i)) + clienteBase.getDemanda() - listaClienteTemp.get(j).getDemanda() > this.veiculo.getCapacidade()){
										/*System.out.println("TESTE-------------");
										System.out.println(custoDemanda(rotaBaseTemp) + " - " 
													+ clienteBase.getDemanda() +  " + " + listaClienteTemp.get(j).getDemanda() + " < " + this.veiculo.getCapacidade());
										
										System.out.println(custoDemanda(solucao.get(i)) + " + " 
												+ clienteBase.getDemanda() +  " - " + listaClienteTemp.get(j).getDemanda() + " < " + this.veiculo.getCapacidade());*/																		
										break;
									}
									
									
									System.out.println("TESTE-------------");
									System.out.println(custoDemanda(rotaBaseTemp) + " - " 
												+ clienteBase.getDemanda() +  " + " + listaClienteTemp.get(j).getDemanda() + " < " + this.veiculo.getCapacidade());
									
									System.out.println(custoDemanda(solucao.get(i)) + " + " 
											+ clienteBase.getDemanda() +  " - " + listaClienteTemp.get(j).getDemanda() + " < " + this.veiculo.getCapacidade());
									
									//Troca
									/*int custoRotaAntBase = custoDistancia(solucao.get(indexRotaBase));
									int custoRotaAnt = custoDistancia(solucao.get(i));
									
									listaClienteTemp.add(j,clienteBase);
									listaClienteTemp.remove(j+1);
									
									rotaBase.getListaCliente().add(z,solucao.get(i).getListaCliente().get(j));
									rotaBase.getListaCliente().remove(z+1);
									
									Rota rotaTempBase = new Rota();
									rotaTempBase.setListaCliente(listaClienteTemp);
									int custoRotaTempBase = custoDistancia(rotaTempBase);
									rotaTempBase.setCustoTotal(custoRotaTempBase);							
									
									Rota rotaTemp = new Rota();
									rotaTemp.setListaCliente(listaClienteTemp);
									int custoRotaTemp = custoDistancia(rotaTemp);
									rotaTemp.setCustoTotal(custoRotaTemp);
									
									if(custoRotaTempBase < custoRotaAntBase || custoRotaAnt < custoRotaTemp){
										//custoTemp = custoRota;
										rotaSolucao = rotaTemp;
									}*/
									
									//listaClienteTemp.remove(j);
								}												
							}												
						}
					}
				}
	 		}
		}
		return solucaoCriada;
	}
	
	public ArrayList<Rota> criaSolucaoRealocacao(ArrayList<Rota> solucao){
		ArrayList<Rota> solucaoCriada = solucao;
		
		Rota rotaBase = new Rota();
		int custoTemp = 0;
		int indexRotaBase = -1;
		
		//Escolhe rota base
		for(int c = 0; c < solucao.size(); c++){				
			rotaBase = solucao.get(c);
			indexRotaBase = c;			
			Rota rotaSolucao = null;
			
			//Escolhe Cliente rota base 
	 		for(int z = 0; z < rotaBase.getListaCliente().size(); z++){
	 			custoTemp = 99999999;
	 			Cliente clienteBase = rotaBase.getListaCliente().get(z);
				Rota rotaBaseTemp = rotaBase;
				
				//Escolhe rota
				for(int i = 0; i < solucao.size(); i++){
					
					if(i != indexRotaBase){
						
						if(clienteBase.getDemanda() + custoDemanda(solucao.get(i)) > this.veiculo.getCapacidade()){
							break;
						}
						
						else{
							
							//Escolhe elemento rota
							ArrayList<Cliente> listaClienteTemp = solucao.get(i).getListaCliente();
							for(int j = 1; j < solucao.get(i).getListaCliente().size() - 1; j++){
								
								if(solucao.get(i).getListaCliente().get(j).getIdentificador() != 0){																
									
									listaClienteTemp.add(j,clienteBase);
									
									Rota rotaTemp = new Rota();
									rotaTemp.setListaCliente(listaClienteTemp);
									int custoRota = custoDistancia(rotaTemp);
									rotaTemp.setCustoTotal(custoRota);							
									
									if(custoRota <= solucao.get(i).getCustoTotal() && custoRota < custoTemp){
										custoTemp = custoRota;
										rotaSolucao = rotaTemp;
									}
									
									listaClienteTemp.remove(j);
								}												
							}
						}
						
						if(rotaSolucao != null){
							solucaoCriada.remove(i);
							solucaoCriada.add(rotaSolucao);
							
							rotaBaseTemp.setCustoTotal(custoDistancia(rotaBaseTemp));						
							rotaBaseTemp.getListaCliente().remove(clienteBase);						
							
							solucaoCriada.remove(indexRotaBase);
							solucaoCriada.add(rotaBaseTemp);
						}
					}
				}
				
			}
		}
	
		return solucaoCriada;
	}
	
	public int custoDistancia(Rota rota){
		int valor = 0;
		
		ArrayList<Cliente> clientes = rota.getListaCliente();

		int indiceAnt = clientes.get(0).getIdentificador();

		for (int i = 1; i < clientes.size(); i++) {
			int indiceAtual = clientes.get(i).getIdentificador();
			
			valor += this.matrizCustos[indiceAnt][indiceAtual];
			indiceAnt = indiceAtual;			
		}
	
		return valor;
	}
	
	public int custoDemanda(Rota rota){
		int valor = 0;
		
		for (Cliente cliente : rota.getListaCliente()) {
			valor += cliente.getDemanda();
		}
		return valor;
	}
	
	public int funcaoObjetivo(ArrayList<Rota> solucao){
		int valor = 0;
		
		for (Rota rota : solucao) {
			valor += rota.getCustoTotal();
		}
	
		return valor;
	}
	
	private ArrayList<Rota> criaSolucaoInicial() {
		
		Cliente deposito = new Cliente();
		deposito.setCoordenadaX(this.coordenadaX);
		deposito.setCoordenadaY(this.coordenadaY);
		deposito.setDemanda(0);
		deposito.setIdentificador(0);
		
		int linhaAux = 0;
		HashMap<Integer, Cliente> listaCandidatos = this.listaCliente;
		int tamanhoLista = 3;
		ArrayList<Rota> rotas = new ArrayList<>();
		Rota rota = new Rota();
		
		while(listaCandidatos.size() != 1){	

			//Se deposito
			if(linhaAux == 0){
				rota = new Rota();
				rota.setItemListaCliente(deposito);
				rota.setCustoTotal((int) (rota.getCustoTotal() + this.matrizCustos[linhaAux][0]));
			}
			
			for(int colunaAux = 0; colunaAux < this.matrizCustos[linhaAux].length; colunaAux++) {
				
				if(listaCandidatos.get(colunaAux) != null){
					if((linhaAux != colunaAux) && //Verifica se i != i
						(verificaCusto(listaColunas, colunaAux, linhaAux, tamanhoLista) && //Pode ser na lista de colunas 
						(verificaCapacidade(custoDemanda(rota), 
								this.veiculo.getCapacidade(), 
								listaCandidatos.get(colunaAux).getDemanda())))){ //Verifica se ultrapassa a capacidade do veiculo
						/*System.out.println(colunaAux+";"
								+listaCandidatos.get(colunaAux).getDemanda()+";"
								+listaCandidatos.get(colunaAux).getIdentificador() );*/
						colocaListaColunas(listaColunas, colunaAux, linhaAux, tamanhoLista);
					}
				}												
			}
			
			//Insere cliente na rota			
			if(listaColunas.size() != 0){
				
				int coluna = escolheItemRota(listaColunas);
				
				rota.setItemListaCliente(listaCandidatos.get(listaColunas.get(coluna)));
				rota.setCustoTotal((int) (rota.getCustoTotal() + matrizCustos[linhaAux][listaColunas.get(coluna)]));
				linhaAux = listaColunas.get(coluna);
				
				listaCandidatos.remove(listaColunas.get(coluna));
				
				listaColunas.clear();
	
			} else {												
				linhaAux = 0;
								
				rota.setItemListaCliente(deposito);
				int posicao = rota.getListaCliente().get(rota.getListaCliente().size() - 1).getIdentificador();

				rota.setCustoTotal((int) (rota.getCustoTotal() + this.matrizCustos[linhaAux][posicao]));				
				rotas.add(rota);
				listaColunas.clear();
			}
		}				

		return rotas;
	}
	
	public int escolheItemRota(ArrayList<Integer> listaColunas) {
		
		if(listaColunas.size() == 1)
			return 0;
		
		Random gerador = new Random();
		
        int numero = gerador.nextInt(listaColunas.size() - 1);
        return numero;
	}
	
	public boolean verificaVazio(HashMap<Integer, Cliente> listaCandidatos) {
		if(listaCandidatos.size() == 1 && listaCandidatos.get(0) == null)
			return true;
		
		return false;
	}

	public boolean verificaCusto(ArrayList<Integer> listaColunas, int coluna, int linha, int tamanhoLista) {
		
		if(listaColunas.size() < tamanhoLista){
			return true;
		}
		
		for (Integer cliente : listaColunas) {
			if(matrizCustos[linha][coluna] < matrizCustos[linha][cliente]){
				return true;
			}
		}
		
		return false;
	}
	
	public void colocaListaColunas(ArrayList<Integer> listaColunas, int coluna, int linha, int tamanhoLista) {
		
		if(listaColunas.size() < tamanhoLista){		
			listaColunas.add(coluna);		
		} else {
			for(int i=0; i < listaColunas.size(); i++){			
				if(this.matrizCustos[linha][coluna] < this.matrizCustos[linha][listaColunas.get(i)]){
					listaColunas.remove(i);
					listaColunas.add(coluna);
				}
			}
		}
	}
	
	public boolean verificaCapacidade(float custoAtual, int capacidade, float demanda) {
		if((custoAtual + demanda) <= capacidade){
			return true;
		}			
		else
			return false;
	}
}