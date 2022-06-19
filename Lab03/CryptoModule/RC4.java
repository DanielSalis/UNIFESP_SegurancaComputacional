package CryptoModule;

import java.util.Scanner;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RC4 {
    public int SIZE = 256;
    public int BYTE_SIZE = 8;

    String text, key;
    char[] S = new char[SIZE];
    char[] T = new char[SIZE];

    public RC4(String key) {
        this.key = key;
    }

    public String encrypt(String text) {
        this.text = text;

        int i, j = 0;

        // inicializa S e T
        for (i = 0; i < SIZE; i++) {
            S[i] = (char) i;
            T[i] = key.charAt(i % key.length());
        }

        // permutação de S
        j = 0;
        for (i = 0; i < SIZE; i++) {
            j = (j + S[i] + T[i]) % SIZE;
            swap(i, j);
        }

        return streamGeneration();
    }

    public String decrypt(String text) {
        String[] splitted = text.split(":");
        int[][] cypherText = new int[splitted.length][BYTE_SIZE];

        for (int i = 0; i < splitted.length; i++) {
            int n = Integer.valueOf(splitted[i], 16);
            cypherText[i] = this.convertDecimalToBinary(n);
        }

        int i, j = 0;

        // inicializa S e T
        for (i = 0; i < SIZE; i++) {
            S[i] = (char) i;
            T[i] = key.charAt(i % key.length());
        }

        // permutação de S
        j = 0;
        for (i = 0; i < SIZE; i++) {
            j = (j + S[i] + T[i]) % SIZE;
            swap(i, j);
        }

        return streamGenerationDecrypt(cypherText);
    }

    private String streamGeneration() {
        int i = 0, j = 0, l = 0, k, t;

        Charset currentCharSet = StandardCharsets.UTF_8;
        ByteBuffer bytes = currentCharSet.encode(text);
        byte[] plainText = bytes.array();
        StringBuilder encryptedString = new StringBuilder();

        while (l < text.length()) {
            i = (i + 1) % SIZE;
            j = (j + S[i]) % SIZE;

            swap(i, j);

            t = (S[i] + S[j]) % SIZE;
            k = S[t];

            int[] binaryK = this.convertDecimalToBinary(k);
            int[] binaryText = this.convertDecimalToBinary((int) plainText[l]);
            int[] xorResult = this.getXorBits(binaryK, binaryText);

            char result = this.convertBinaryToChar(xorResult);
            String hexString = Integer.toHexString(result);
            encryptedString.append(hexString + ":");

            l++;
        }

        return encryptedString.toString();
    }

    private String streamGenerationDecrypt(int[][] cypherText) {
        int i = 0, j = 0, l = 0, k, t;
        char[] result = new char[cypherText.length];

        while (l < cypherText.length) {
            i = (i + 1) % SIZE;
            j = (j + S[i]) % SIZE;

            swap(i, j);

            t = (S[i] + S[j]) % SIZE;
            k = S[t];

            int[] binaryK = this.convertDecimalToBinary(k);
            int[] xorResult = this.getXorBits(binaryK, cypherText[l]);

            result[l] = this.convertBinaryToChar(xorResult);
            l++;
        }

        return new String(result);
    }

    private int[] getXorBits(int[] bits1, int[] bits2) {
        int[] result = new int[bits1.length];
        int i;
        for (i = 0; i < bits1.length; i++) {
            int result_bit = bits1[i] == bits2[i] ? 0 : 1;
            result[i] = result_bit;
        }

        return result;
    }

    private char convertBinaryToChar(int[] binary) {
        long arr = 0;
        int length = binary.length - 1;

        for (int i = length; i > -1; i--) {
            arr += Math.pow(2, length - i) * binary[i];
        }

        return (char) arr;
    }

    private int[] convertDecimalToBinary(int decimal) {
        int binary[] = new int[BYTE_SIZE];
        int i = BYTE_SIZE - 1;

        while (i >= 0) {
            binary[i] = decimal % 2;
            decimal = decimal / 2;
            i--;
        }

        return binary;
    }

    private void swap(int i, int j) {
        char aux = S[i];
        S[i] = S[j];
        S[j] = aux;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String key = scanner.nextLine();

        RC4 crypt = new RC4(key);

        // String encrypted = crypt.encrypt(text);
        // System.out.println(encrypted);

        String decrypted = crypt.decrypt(text);
        System.out.println(decrypted);

        scanner.close();
    }
}