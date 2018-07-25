import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Scanner user = new Scanner(System.in);
		
		System.out.print("Quantidade de arquivos: ");
		int qtdArquivos = user.nextInt();
		System.out.print("Numero de alteracoes feitas pelo cliente: ");
		int alteracoesCliente = user.nextInt();
		System.out.print("Quantidade de threads HTTP: ");
		int qtdThreadsHttp = user.nextInt();
		System.out.print("Numero de requisicoes das threads HTTP: ");
		int numRequisicoes = user.nextInt();
		System.out.println("\n\n");
		
		Semaphore cheio = new Semaphore(0);
		Semaphore vazio = new Semaphore(100);
		BufferModificados b1 = new BufferModificados(100);
		BufferModificados b2 = new BufferModificados(100);
		Lock l = new ReentrantLock();
		Arquivo[] a1 = new Arquivo[qtdArquivos];
		Arquivo[] a2 = new Arquivo[qtdArquivos];
		Cliente clienteA = new Cliente("Jekyll Client", a1, cheio, vazio, b1, qtdArquivos, alteracoesCliente);
		Cliente clienteB = new Cliente("Hyde Client", a2, cheio, vazio, b2, qtdArquivos, alteracoesCliente);
		Servidor servidorA = new Servidor("Jekyll Server", clienteA, a1, a2, cheio, vazio, b1, l);
		Servidor servidorB = new Servidor("Hyde Server", clienteB, a2, a1, cheio, vazio, b2, l);
		WorkerHttp[] w = new WorkerHttp[qtdThreadsHttp]; 
		
		for(int i = 0; i < qtdThreadsHttp; i++) {
			w[i] = new WorkerHttp(a1, numRequisicoes, qtdArquivos, "WorkerHTTP " + i);
		}
		
		for(int i = 0; i < qtdArquivos; i++) {
			int r = (int) (Math.random() * 1000 + 1);
			a1[i] = new Arquivo("Arquivo " + i, r);
			a2[i] = new Arquivo("Arquivo " + i, r);
		}
		
		clienteA.start();
		clienteB.start();
		servidorA.start();
		servidorB.start();
		
		for(int i = 0; i < qtdThreadsHttp; i++) {
			w[i].start();
		}
	}
}