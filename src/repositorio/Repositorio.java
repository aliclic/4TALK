package repositorio;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Programa��o Orientada a Objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import modelo.Participante;


public class Repositorio {
	
	private TreeMap<String, Participante> participantes;
	private TreeMap<Integer, Mensagem> mensagens;
	
	public void adicionar(Individual i) {
		participantes.put(i.getNome(), i);
	}
	
	public void adicionar(Grupo g) {
		participantes.put(g.getNome(), g);
	}
	
	private void adicionar(Mensagem m) {
		mensagens.put(m.getId(), m);
		
	}
	
	private Individual localizarIndividual(String nome) {
		for(Participante p : participantes.values()) {
			if(p instanceof Individual i && i.getNome().equals(nome))
				return i;
		}
		return null;
	}
	
	private Participante localizarParticipante(String nome) {
		for(Participante p : participantes.values()) {
			if(p.getNome().equals(nome))
				return p;
		}
		return null;
	}
	
	private ArrayList<Individual> getIndividuos() {
		ArrayList<Individual> individuos = new ArrayList<>();
		
		for(Participante p : participantes.values()) {
			if(p instanceof Individual i)
				individuos.add(i);
		}
		return individuos;
	}
	
	private ArrayList<Grupo> getGrupos() {
		ArrayList<Grupo> grupos = new ArrayList<>();
		
		for(Participante p : participantes.values()) {
			if(p instanceof Grupo g)
				grupos.add(g);
		}
		return grupos;
	}
	
	
	public void carregarObjetos()  	{
		// carregar para o repositorio os objetos dos arquivos csv
		try {
			//caso os arquivos nao existam, serao criados vazios
			File f1 = new File( new File(".\\mensagens.csv").getCanonicalPath() ) ; 
			File f2 = new File( new File(".\\individuos.csv").getCanonicalPath() ) ; 
			File f3 = new File( new File(".\\grupos.csv").getCanonicalPath() ) ; 
			if (!f1.exists() || !f2.exists() || !f3.exists() ) {
				//System.out.println("criando arquivo .csv vazio");
				FileWriter arquivo1 = new FileWriter(f1); arquivo1.close();
				FileWriter arquivo2 = new FileWriter(f2); arquivo2.close();
				FileWriter arquivo3 = new FileWriter(f3); arquivo3.close();
				return;
			}
		}
		catch(Exception ex)		{
			throw new RuntimeException("criacao dos arquivos vazios:"+ex.getMessage());
		}

		String linha;	
		String[] partes;	

		try	{
			String nome,senha,administrador;
			File f = new File( new File(".\\individuos.csv").getCanonicalPath())  ;
			Scanner arquivo1 = new Scanner(f);	 //  pasta do projeto
			while(arquivo1.hasNextLine()) 	{
				linha = arquivo1.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				nome = partes[0];
				senha = partes[1];
				administrador = partes[2];
				Individual ind = new Individual(nome,senha,Boolean.parseBoolean(administrador));
				this.adicionar(ind);
			}
			arquivo1.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de individuos:"+ex.getMessage());
		}

		try	{
			String nome;
			Grupo grupo;
			Individual individuo;
			File f = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
			Scanner arquivo2 = new Scanner(f);	 //  pasta do projeto
			while(arquivo2.hasNextLine()) 	{
				linha = arquivo2.nextLine().trim();	
				partes = linha.split(";");
				//System.out.println(Arrays.toString(partes));
				nome = partes[0];
				grupo = new Grupo(nome);
				if(partes.length>1)
					for(int i=1; i< partes.length; i++) {
						individuo = this.localizarIndividual(partes[i]);
						grupo.adicionar(individuo);
					}
				this.adicionar(grupo);
			}
			arquivo2.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de grupos:"+ex.getMessage());
		}


		try	{
			String id, nomeemitente, nomedestinatario,texto;
			Mensagem m;
			Participante emitente,destinatario;
			File f = new File( new File(".\\mensagens.csv").getCanonicalPath() )  ;
			Scanner arquivo3 = new Scanner(f);	 //  pasta do projeto
			while(arquivo3.hasNextLine()) 	{
				linha = arquivo3.nextLine().trim();		
				partes = linha.split(";");	
				//System.out.println(Arrays.toString(partes));
				id = partes[0];
				nomeemitente = partes[1];
				nomedestinatario = partes[2];
				texto = partes[3];
				emitente = this.localizarParticipante(nomeemitente);
				destinatario = this.localizarParticipante(nomedestinatario);
				m = new Mensagem(Integer.parseInt(id),emitente,destinatario,texto);
				this.adicionar(m);
			} 
			arquivo3.close();
		}
		catch(Exception ex)		{
			throw new RuntimeException("leitura arquivo de mensagens:"+ex.getMessage());
		}

	}


	public void	salvarObjetos()  {
		//gravar nos arquivos csv os objetos que est�o no reposit�rio
		try	{
			File f = new File( new File(".\\mensagens.csv").getCanonicalPath())  ;
			FileWriter arquivo1 = new FileWriter(f); 
			for(Mensagem m : mensagens.values()) 	{
				arquivo1.write(	m.getId()+";"+
						m.getEmitente().getNome()+";"+
						m.getDestinatario().getNome()+";"+
						m.getTexto()+"\n");	
			} 
			arquivo1.close();
		}
		catch(Exception e){
			throw new RuntimeException("problema na cria��o do arquivo  mensagens "+e.getMessage());
		}

		try	{
			File f = new File( new File(".\\individuos.csv").getCanonicalPath())  ;
			FileWriter arquivo2 = new FileWriter(f) ; 
			for(Individual ind : this.getIndividuos()) {
				arquivo2.write(ind.getNome() +";"+ ind.getSenha() +";"+ ind.getAdministrador() +"\n");	
			} 
			arquivo2.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na cria��o do arquivo  individuos "+e.getMessage());
		}

		try	{
			File f = new File( new File(".\\grupos.csv").getCanonicalPath())  ;
			FileWriter arquivo3 = new FileWriter(f) ; 
			for(Grupo g : this.getGrupos()) {
				String texto="";
				for(Individual ind : g.getIndividuos())
					texto += ";" + ind.getNome();
				arquivo3.write(g.getNome() + texto + "\n");	
			} 
			arquivo3.close();
		}
		catch (Exception e) {
			throw new RuntimeException("problema na cria��o do arquivo  grupos "+e.getMessage());
		}
	}
}

