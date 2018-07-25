import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

//import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Servidor extends Thread {
	public String nome;
	public int posicao;
	public Cliente c;
	public long inicio;
	public long fim;
	public long tempo;
	public long tempoThread;
	public Arquivo[] meu;
	public Arquivo[] outro;
	public Semaphore cheio;
	public Semaphore vazio;
	public BufferModificados m;
	public Lock l;
	
	public Servidor (String nome, Cliente c, Arquivo[] meu, 
						Arquivo[] outro, Semaphore cheio, Semaphore vazio, BufferModificados m, Lock l) {
		this.nome = nome;
		this.c = c;
		this.meu = meu;
		this.outro = outro;
		this.cheio = cheio;
		this.vazio = vazio;
		this.m = m;
		this.l = l;
	}
	
	public void get() {
		l.lock();
		if(m.quantidade > 0) {
			posicao = m.retirarValor();
		}
		l.unlock();
		//System.out.println("Retirando a posicao " + posicao + " do buffer...");
	}
	
	public void enviarModificacao() throws InterruptedException {
		if(c.nome == "Jekyll") {
			long inicio = System.currentTimeMillis();
			outro[posicao].x.writeLock().lock(); //travar pra escrita
			meu[posicao].x.readLock().lock();  //travar pra leitura
			Thread.sleep(meu[posicao].tamanho);
			outro[posicao].tamanho = meu[posicao].tamanho;
			//System.out.println("Servidor " + c.nome + " enviando modificacao...");
			outro[posicao].x.writeLock().unlock();
			meu[posicao].x.readLock().unlock();
			long fim = System.currentTimeMillis();
			tempoThread = fim-inicio;
			System.out.println("Thread " + nome + " ficou em espera por " + tempoThread + " milisegundos...");
		} else {
			long inicio = System.currentTimeMillis();
			meu[posicao].x.writeLock().lock();
			outro[posicao].x.readLock().lock();
			Thread.sleep(meu[posicao].tamanho);
			meu[posicao].tamanho = outro[posicao].tamanho;
			//System.out.println("Servidor " + c.nome + " enviando modificacao...");
			meu[posicao].x.writeLock().unlock();
			outro[posicao].x.readLock().unlock();
			long fim = System.currentTimeMillis();
			tempoThread = fim-inicio;
			System.out.println("Thread " + nome + " ficou em espera por " + tempoThread + " milisegundos...");
		}
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 5000; i++) {
			try {
				cheio.acquire();
				get();
				enviarModificacao();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				vazio.release();
			}
			
		}
		
	}
}
