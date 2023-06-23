package regras_negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

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
		
		Participante p = repositorio.localizarParticipante(nomeindividuo);	
		if(p != null) 
			throw new Exception("criar individual - nome ja existe: " + nomeindividuo);
		
		
		Individual individuo = new Individual(nomeindividuo,senha, false);
		repositorio.adicionar(individuo);		
	}
	
	public static boolean validarIndividuo(String nomeindividuo, String senha) {
		Individual individuo = repositorio.localizarIndividual(nomeindividuo);
		if(individuo != null && individuo.getSenha().equals(senha))
			return true;
		
		return false;
	}
	
	public static void criarAdministrador(String nomeadministrador, String senha) throws  Exception {
		if(nomeadministrador.isEmpty()) 
			throw new Exception("criar Administrador - nome vazio:");
		if(senha.isEmpty()) 
			throw new Exception("criar Administrador - senha vazia:");
		
		Participante p = repositorio.localizarParticipante(nomeadministrador);	
		if(p != null) 
			throw new Exception("criar Aministrador - nome ja existe: " + nomeadministrador);
		
		
		Individual individuo = new Individual(nomeadministrador,senha, true);
		repositorio.adicionar(individuo);	
	}
	
	public static void criarGrupo(String nomegrupo) throws  Exception{
		if(nomegrupo.isEmpty()) 
			throw new Exception("criar Grupo - nome vazio:");
		
		if(repositorio.localizarGrupo(nomegrupo) != null) 
			throw new Exception("criar Grupo - nome ja existe: " + nomegrupo);
		
		Grupo grupo = new Grupo(nomegrupo);
		repositorio.adicionar(grupo);	
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
		
		
		//cont.
		//gerar id no repositorio para a mensagem
		//int id = repositorio.gerarIdMensagem();
		//criar mensagem
		//Mensagem mensagem = new Mensagem(id, emitente, destinatario, texto);
		//adicionar mensagem ao emitente e destinatario
		//emitente.adicionarEnviada(mensagem);
		//destinatario.adicionarRecebida(mensagem);
		//adicionar mensagem ao repositorio
		//repositorio.adicionar(mensagem);
		//
		//caso destinatario seja tipo Grupo então criar copias da mensagem, tendo o grupo como emitente e cada membro do grupo como destinatario, 
		//  usando mesmo id e texto, e adicionar essas copias no repositorio
		
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
			if(m.getDestinatario().getNome().equals(nomedestinatario))
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
		destinatario.removerRecebida(m);
		repositorio.remover(m);	
		
		if(destinatario instanceof Grupo g) {
			ArrayList<Mensagem> lista = destinatario.getEnviadas();
			lista.removeIf(new Predicate<Mensagem>() {
				@Override
				public boolean test(Mensagem t) {
					if(t.getId() == m.getId()) {
						t.getDestinatario().removerRecebida(t);
						repositorio.remover(t);	
						return true;		//apaga mensagem da lista
					}
					else
						return false;
				}
				
			});
			
		}
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
	
	public static ArrayList<Mensagem> listarMensagens() {
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
					if(s == termo)
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
		ArrayList<Individual> users = repositorio.getIndividuos();
		ArrayList<String> lista = new ArrayList<>();
		
		for(Individual i : users) {
			if(i.getEnviadas().isEmpty()) {
				lista.add(i.getNome());
			}
		}
		
		return lista;
	}

}
