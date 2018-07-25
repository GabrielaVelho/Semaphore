import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Arquivo {
	public String nome;
	public int tamanho;
	final ReentrantReadWriteLock x = new ReentrantReadWriteLock();
	
	
	public Arquivo (String nome, int tamanho) {
		this.nome = nome;
		this.tamanho = tamanho;
	}
}