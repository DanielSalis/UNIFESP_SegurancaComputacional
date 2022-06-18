package CryptoModule;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RC4 {
	public Scanner scanner = new Scanner(System.in);
  public String plain_text;
  public char[] key;
  public static int BYTES = 256;

  public void run(){
    plain_text = scanner.nextLine();
    key = scanner.nextLine().toCharArray();

    cifrar(plain_text, key);
  }

	public static char[] getS() {
		char[] aux = new char[BYTES];

		for (int i = 0; i < aux.length; i++)
			aux[i] = (char)i;

		return aux;
	}

	public static char[] getT (char[] K) {
		char[] aux = new char[BYTES];
		int j = 0;

		for (int i = 0; i < aux.length; i++) {
			if (j >= K.length)
				j = 0;
			aux[i] = K[j];
			j++;
		}

		return aux;
	}

	public static char[] executeSFunction (char[] S, char[] T) {
		int j = 0;

		for (int i = 0; i < BYTES; i++) {
			j = (j + S[i] + T[i]) % BYTES;
			swap(S, i, j);
		}

		return S;
	}

	public static void swap (char[] S, int i, int j) {
		char aux = S[i];
		S[i] = S[j];
		S[j] = aux;
	}

	public int[] applyXor (int[] a, int[] b) {
		int[] aux = new int[a.length];

		for (int i = 0; i < a.length; i++) {
			if (a[i] == b[i])
				aux[i] = 0;
			else
				aux[i] = 1;
		}

		return aux;
	}

	public int [] convertIntToBin (int num) {
		int bin [] = new int[8];
		int i = 7;

		while (i >- 1) {
			bin[i] = num % 2;
			num = num/2;
			i--;
		}

		return bin;
	}

	public char convetBinToChar (int[] bin) {
		long currentAscString = 0;

		for (int i = bin.length-1; i >- 1; i--) {
			currentAscString += Math.pow(2, bin.length-1-i) * bin[i];
		}

		return (char) currentAscString;
	}

	public void streamGeneration (char [] S, String entrada) {
		int i = 0, j = 0, l = 0, k, t;

		Charset currentCharSet = StandardCharsets.UTF_8;
		ByteBuffer BytesArray = currentCharSet.encode(entrada);
		byte [] aux = BytesArray.array();

		while (true) {
			i = (i + 1) % BYTES;
			j = (j + S[i]) % BYTES;
			swap(S, i, j);
			t = (S[i] + S[j]) % BYTES;
			k = S[t];
			if (l < entrada.length()){
				int[] xorResult = this.applyXor(this.convertIntToBin(k), this.convertIntToBin((int) aux[l++]));
				System.out.print(Integer.toHexString(this.convetBinToChar(xorResult)) + ":");
			}
			else
				break;
		}
	}

	public void cifrar(String entrada, char[] K){
		char [] S = getS();
		char [] T = getT(K);

		executeSFunction(S, T);
		this.streamGeneration(S, entrada);
	}

	public static void main (String[] args){
    RC4 program = new RC4();
    program.run();
	}
}