package regras_negocio;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import modelo.Grupo;
import modelo.Individual;
import modelo.Mensagem;
import modelo.Participante;
import repositorio.Repositorio;

public class Fachada {
	private Fachada() {}

	private static Repositorio repositorio = new Repositorio();
	

	public static void criarIndividuo(String nomeindividuo, String senha) throws  Exception {
		if(nomeindividuo.isEmpty()) 
			throw new Exception("criar individual - nome vazio:");
		if(senha.isEmpty()) 
			throw new Exception("criar individual - senha vazia:");
		
		if(repositorio.getParticipantes().isEmpty()) {
			Individual individuo = new Individual(nomeindividuo, senha, false);
			repositorio.adicionar(individuo);	
			repositorio.salvarObjetos();
		} 
		else {
			Participante p = repositorio.localizarParticipante(nomeindividuo);	
			if(p != null) 
				throw new Exception("criar individual - nome ja existe: " + nomeindividuo);
			
			Individual individuo = new Individual(nomeindividuo,senha, false);
			repositorio.adicionar(individuo);
			repositorio.salvarObjetos();
		}
	}
	
	public static Individual validarIndividuo(String nomeindividuo, String senha) throws Exception{
		Individual individuo = repositorio.localizarIndividual(nomeindividuo);
		if(individuo == null) {
			throw new Exception("validar Individuo - Individuo nao existe: " + nomeindividuo);			
		} 
		else {
			if(senha.equals(individuo.getSenha()))
				return individuo;
			else {
				throw new Exception("validar Individuo - Senha inválida pra esse individuo");
			}
		}
	}
	
	public static void criarAdministrador(String nomeadministrador, String senha) throws  Exception {
		if(nomeadministrador.isEmpty()) 
			throw new Exception("criar Administrador - nome vazio:");
		if(senha.isEmpty()) 
			throw new Exception("criar Administrador - senha vazia:");
		
		if(repositorio.getParticipantes().isEmpty()) {   //verifica se o repositorio de participantes esta vazio e estamos adicionando o primeiro 
			
			Individual individuo = new Individual(nomeadministrador, senha, true);
			repositorio.adicionar(individuo);	
			repositorio.salvarObjetos();
		}
		else {
			Participante p = repositorio.localizarParticipante(nomeadministrador);	
			if(p != null) 
				throw new Exception("criar Aministrador - nome ja existe: " + nomeadministrador);
			
			
			Individual individuo = new Individual(nomeadministrador,senha, true);
			repositorio.adicionar(individuo);
			repositorio.salvarObjetos();
		}
	}
	
	public static void criarGrupo(String nomegrupo) throws  Exception{
		if(nomegrupo.isEmpty()) 
			throw new Exception("criar Grupo - nome vazio:");
		
		if(repositorio.localizarGrupo(nomegrupo) != null) 
			throw new Exception("criar Grupo - nome ja existe: " + nomegrupo);
		
		if(repositorio.getParticipantes().isEmpty()) {
			Grupo grupo= new Grupo(nomegrupo);
			repositorio.adicionar(grupo);
			repositorio.salvarObjetos();
		}
		else {
			Grupo grupo = new Grupo(nomegrupo);
			repositorio.adicionar(grupo);	
			repositorio.salvarObjetos();			
		}
	}
	
	public static void inserirGrupo(String nomeindividuo, String nomegrupo) throws  Exception {
		if(nomeindividuo.isEmpty()) 
			throw new Exception("inserir Grupo - nome do individuo vazio");
		if(nomegrupo.isEmpty()) 
			throw new Exception("inserir Grupo - nome do grupo vazio");
		
		//localizar nomeindividuo no repositorio
		Individual ind = repositorio.localizarIndividual(nomeindividuo);
		if(ind == null)
			throw new Exception("inserir Grupo - nome do individuo não existe no repositorio");
		
		//localizar nomegrupo no repositorio
		Grupo grupo = repositorio.localizarGrupo(nomegrupo);
		if(grupo == null)
			throw new Exception("inserir Grupo - nome do grupo não existe no repositorio");
		
		//verificar se individuo nao esta no grupo
		if(!grupo.getIndividuos().contains(ind)) {
			//adicionar individuo com o grupo e vice-versa
			ind.adicionar(grupo);
			grupo.adicionar(ind);
			repositorio.salvarObjetos();
		} 
		else
			throw new Exception("inserir Grupo - o individuo ja esta no grupo");
	}
	
	public static void removerGrupo(String nomeindividuo, String nomegrupo) throws  Exception {
		if(nomeindividuo.isEmpty()) 
			throw new Exception("remover Grupo - nome do individuo vazio");
		if(nomegrupo.isEmpty()) 
			throw new Exception("remover Grupo - nome do grupo vazio");
		
		//localizar nomeindividuo no repositorio
		Individual ind = repositorio.localizarIndividual(nomeindividuo);
		if(ind == null)
			throw new Exception("remover Grupo - nome do individuo não existe no repositorio");
		
		//localizar nomegrupo no repositorio
		Grupo grupo = repositorio.localizarGrupo(nomegrupo);
		if(grupo == null)
			throw new Exception("remover Grupo - nome do grupo não existe no repositorio");
		
		//verificar se individuo ja esta no grupo
		if(grupo.getIndividuos().contains(ind)) {
			//remover individuo com o grupo e vice-versa
			ind.remover(grupo);
			grupo.remover(ind);
			repositorio.salvarObjetos();
		}
		else	
			throw new Exception("remover Grupo - o individuo nao esta no grupo");
	}
	
	public static void criarMensagem(String nomeemitente, String nomedestinatario, String texto) throws Exception {
		if(texto.isEmpty()) 
			throw new Exception("criar mensagem - texto vazio:");
		
		Individual emitente = repositorio.localizarIndividual(nomeemitente);	
		if(emitente == null) 
			throw new Exception("criar mensagem - emitente nao existe: " + nomeemitente);
		
		Participante destinatario = repositorio.localizarParticipante(nomedestinatario);	
		if(destinatario == null) 
			throw new Exception("criar mensagem - destinatario nao existe: " + nomeemitente);
		
		if(destinatario instanceof Grupo g && emitente.localizarGrupo(g.getNome())==null)
			throw new Exception("criar mensagem - grupo nao permitido: " + nomedestinatario);
		
		
		int cont = repositorio.GerarId();
		
		if (destinatario instanceof Grupo g ) {
			Mensagem mensagem= new Mensagem(cont,texto,emitente,g,LocalDateTime.now());
			emitente.adicionarEnviada(mensagem);
			g.adicionarRecebida(mensagem);
			repositorio.adicionar(mensagem);
			
			//verifica se nesse caso estamos enviando uma mensagem para um destinatario tipo Grupo 
			//CRIACAO DE COPIAS
			for(Individual i : g.getIndividuos()) {
				if(i.getNome() != emitente.getNome()){ //verificao pra mandar msg no grupo caso seja diferente do emitente (no grupo)
				Mensagem msg= new Mensagem(cont,emitente.getNome()+"/"+texto,g,i,LocalDateTime.now()); 
				g.adicionarEnviada(msg);
				i.adicionarRecebida(msg); 
				repositorio.adicionar(msg); //adicionando ao repositorio
				repositorio.salvarObjetos();
				}
			}
		}
		else {
		Mensagem mensagem = new Mensagem(cont,texto,emitente,destinatario,LocalDateTime.now()); //cria objeto de mensagem relacionando emitente e dest
		emitente.adicionarEnviada(mensagem); //adiciono objeto mensagem no enviados do emitente
		destinatario.adicionarRecebida(mensagem); //adiciono objeto mensagem no recebidos do destinatario
		repositorio.adicionar(mensagem); //adiciona msg no repositorio 
		repositorio.salvarObjetos();
		}
		
	}
	
	public static ArrayList<Mensagem> obterConversa(String nomeindividuo, String nomedestinatario) throws Exception {
		//localizar emitente no repositorio
		Individual emitente = repositorio.localizarIndividual(nomeindividuo);
		if(emitente == null)
			throw new Exception("obter conversa - individuo emitente nao encontrado: " + nomeindividuo);
		//localizar destinatario no repositorio
		Participante destinatario = repositorio.localizarParticipante(nomedestinatario);
		if(destinatario == null)
			throw new Exception("obter conversa - participante destinatario nao encontrado: " + nomedestinatario);
		//obter do emitente a lista  enviadas
		ArrayList<Mensagem> enviadas = emitente.getEnviadas();
		//obter do emitente a lista  recebidas
		ArrayList<Mensagem> recebidas = emitente.getRecebidas();
		
		//criar a lista conversa
		ArrayList<Mensagem> conversa = new ArrayList<>();
		
		//Adicionar na conversa as mensagens da lista enviadas cujo destinatario é igual ao parametro destinatario
		for(Mensagem m : enviadas) {
			if(m.getDestinatario().getNome().equals(nomedestinatario))
				conversa.add(m);
		}
		//Adicionar na conversa as mensagens da lista recebidas cujo emitente é igual ao parametro destinatario
		for(Mensagem m : recebidas) {
			if(m.getEmitente().getNome().equals(nomedestinatario))
				conversa.add(m);
		}
		
		//ordenar a lista conversa pelo id das mensagens
		Collections.sort(conversa, new Comparator<Mensagem>() {
			public int compare(Mensagem m1, Mensagem m2) {
				return Integer.compare(m1.getId(), m2.getId());
			}
		});
		//retornar a lista conversa
		return conversa;
	}
	
	public static void apagarMensagem(String nomeindividuo, int id) throws  Exception {
		Individual emitente = repositorio.localizarIndividual(nomeindividuo);	
		if(emitente == null) 
			throw new Exception("apagar mensagem - nome nao existe:" + nomeindividuo);
		
		Mensagem m = emitente.localizarEnviada(id);
		if(m == null)
			throw new Exception("apagar mensagem - mensagem nao pertence a este individuo:" + id);
		
		emitente.removerEnviada(m);
		
		Participante destinatario = m.getDestinatario();
		
		if(destinatario instanceof Grupo g) {
			g.removerRecebida(m);
			repositorio.remover(m);
			
			for(Mensagem msg : g.getEnviadas()) {
				if(msg.getId()==m.getId())
				{
					g.removerEnviada(msg);
					msg.getDestinatario().removerRecebida(msg);
					repositorio.remover(msg);
				}
				
			}
			repositorio.salvarObjetos();
		}
		destinatario.removerRecebida(m);
		repositorio.remover(m);	
		repositorio.salvarObjetos();
	}
	
	public static ArrayList<Mensagem> listarMensagensEnviadas(String nomeindividuo) throws Exception {
		Individual ind = repositorio.localizarIndividual(nomeindividuo);	
		if(ind == null) 
			throw new Exception("listar mensagens enviadas - nome nao existe:" + nomeindividuo);
		
		return ind.getEnviadas();	
	}
	
	public static ArrayList<Mensagem> listarMensagensRecebidas(String nomeparticipante) throws Exception {
		Participante part = repositorio.localizarParticipante(nomeparticipante);
		if(part == null)
			throw new Exception("listar mensagens enviadas - nome nao existe:" + nomeparticipante);
		
		return part.getRecebidas();
	}
	
	public static ArrayList<Individual> listarIndividuos() {
		return repositorio.getIndividuos();	
	}
	
	public static ArrayList<Grupo> listarGrupos() {
		return repositorio.getGrupos();
	}
	
	public static Collection<Mensagem> listarMensagens() {
		return repositorio.getMensagens();
	}

	public static ArrayList<Mensagem> espionarMensagens(String nomeadministrador, String termo) throws Exception {
		if(nomeadministrador.isEmpty())
			throw new Exception("espionar mensagens - nome do administrador vazio");
		//localizar individuo no repositorio
		Individual admin = repositorio.localizarIndividual(nomeadministrador);
		if(admin == null)
			throw new Exception("espionar mensagens - nome de administrador nao existe: " + nomeadministrador);
		//verificar se individuo é administrador
		if(admin.getAdministrador() == false)
			throw new Exception("espionar mensagens - " + nomeadministrador + " nao é um administrador!");
		
		//listar as mensagens que contem o termo no texto
		ArrayList<Mensagem> msgs = new ArrayList<>();
		
		if(termo.isEmpty()) {
			for(Mensagem m : repositorio.getMensagens())
				msgs.add(m);
		}
		else {
			for(Mensagem m : repositorio.getMensagens()) {
				String[] array = m.getTexto().split("");
				for(String s : array) {
					if(s.equals(termo))
						msgs.add(m);
				}
			}
		}
		
		return msgs;
	}

	public static ArrayList<String> ausentes(String nomeadministrador) throws Exception {
		if(nomeadministrador.isEmpty())
			throw new Exception("ausentes - nome do administrador vazio");
		//localizar individuo no repositorio
		Individual admin = repositorio.localizarIndividual(nomeadministrador);
		if(admin == null)
			throw new Exception("ausentes - nome de administrador nao existe: " + nomeadministrador);
		//verificar se individuo é administrador
		if(admin.getAdministrador() == false)
			throw new Exception("ausentes - " + nomeadministrador + " nao é um administrador!");
		
		//listar os nomes dos participante que nao enviaram mensagens
		Collection<Participante> users = repositorio.getParticipantes().values();
		ArrayList<String> lista = new ArrayList<>();
		
		for(Participante i : users) {
			if(i.getEnviadas().isEmpty()) {
				lista.add(i.getNome());
			}
		}
		
		return lista;
	}
	

}
