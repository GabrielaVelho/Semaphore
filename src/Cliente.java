import java.util.Random;
import java.util.concurrent.Semaphore;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class Cliente extends Thread {
	public int posicao;
	public int qtdArquivos;
	public int alteracoesCliente;
	public long fim;
	public long tempo;
	public long tempoThread;
	public String nome;
	public Arquivo[] a;
	public Semaphore cheio;
	public Semaphore vazio;
	public BufferModificados m;
	
	public Cliente (String nome, Arquivo[] a, Semaphore cheio, Semaphore vazio, BufferModificados m, int qtdArquivos, int alteracoesCliente) { 
		this.nome = nome;
		this.a = a;
		this.cheio = cheio;
		this.vazio = vazio;
		this.m = m;
		this.qtdArquivos = qtdArquivos;
		this.alteracoesCliente = alteracoesCliente;
	}
	
	public void put() {
		m.adicionarValor(posicao);
		//System.out.println("Adicionando a posicao " + posicao + " no buffer...");
	}
	
	
	public void modificarArquivo() throws InterruptedException {
		Random r = new Random();
		posicao = r.nextInt(qtdArquivos);
		long inicio = System.currentTimeMillis();
		a[posicao].x.writeLock().lock();
		a[posicao].tamanho++;
		//System.out.println("Cliente " + nome + " modificando o arquivo " + a[posicao].nome + " de tamanho: " + a[posicao].tamanho);
		Thread.sleep(a[posicao].tamanho);
		a[posicao].x.writeLock().unlock();	
		long fim = System.currentTimeMillis();
		tempoThread = fim-inicio;
		System.out.println("Thread " + nome + " ficou em espera por " + tempoThread + " milisegundos...");
	}
	
	public long tempoThread() {
		return tempoThread;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < alteracoesCliente; i++) {
			try {
				vazio.acquire();
				//l.lock();
				modificarArquivo();
				put();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				//l.unlock();
				cheio.release();
			}
			
		}
	}
}
