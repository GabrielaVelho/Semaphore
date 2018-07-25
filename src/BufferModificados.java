public class BufferModificados {
	public int inicio;
	public int fim;
	public int limite;
	public int quantidade;
	public int buffer[];

	public BufferModificados (int limite) {
		this.inicio = 0;
		this.fim = 0;
		this.quantidade = 0;
		this.limite = limite;
		this.buffer = new int[limite];
	}

	public void adicionarValor(int valor) {
		buffer[fim++ % limite] = valor;
		quantidade++;
	}

	public int retirarValor() {
		quantidade--;
		return buffer[inicio++ % limite];
	}
}
