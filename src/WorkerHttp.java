import java.util.Random;
import java.util.concurrent.Semaphore;

public class WorkerHttp extends Thread {
	public Arquivo[] a;
	public String nome;
	public long inicio;
	public long fim;
	public long tempo;
	public long tempoThread;
	public int qtdArquivos;
	public int numRequisicoes;
	public int posicao;
	public Semaphore s = new Semaphore(1);
	
	public WorkerHttp (Arquivo[] a, int numRequisicoes, int qtdArquivos, String nome) {
		this.a = a;
		this.numRequisicoes = numRequisicoes;
		this.qtdArquivos = qtdArquivos;
		this.nome = nome;
		
	}
	
	public void lerArquivo() throws InterruptedException {
		Random r = new Random();
		posicao = r.nextInt(qtdArquivos);
		long inicio = System.currentTimeMillis();
		a[posicao].x.readLock().lock();
		//System.out.println(nome + " lendo " + a[posicao].nome);
		Thread.sleep(a[posicao].tamanho);
		a[posicao].x.readLock().unlock();
		long fim = System.currentTimeMillis();
		tempoThread = fim-inicio;
		System.out.println("Thread " + nome + " ficou em espera por " + tempoThread + " milisegundos...");
		}
	
	@Override
	public void run() {
		for(int i = 0; i < numRequisicoes; i++) {
			try {		
				s.acquire();
				lerArquivo();
				s.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
