package modelo;

import java.util.ArrayList;

public class Individual extends Participante {
	private String senha;
	private boolean administrador;
	private ArrayList<Grupo> grupos;
	
	public Individual(String nome, String senha, boolean administrador) {
		super(nome);
		this.senha = senha;
		this.administrador = administrador;
	}
	
	public void adicionar(Grupo g) {
		grupos.add(g);
	}
	
	public void remover(Grupo g) {
		grupos.remove(g);
	}

	public Grupo localizarGrupo(String nomegrupo) {
		for(Grupo g : grupos) {
			if(g.getNome().equals(nomegrupo))
				return g;
		}
		return null;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public ArrayList<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(ArrayList<Grupo> grupos) {
		this.grupos = grupos;
	}

	@Override
	public String toString() {
		return "Individual [senha=" + senha + ", administrador=" + administrador + ", grupos=" + grupos + "]";
	}

	
}
