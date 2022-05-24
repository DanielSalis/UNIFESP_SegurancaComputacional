import java.util.Arrays;
import java.util.Scanner;

/**
 * Sdes
 */
public class Sdes {
  public Scanner scan = new Scanner(System.in);

  int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
  int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9 };
  int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7 };
  int PI[] = { 4, 1, 3, 5, 7, 2, 8, 6 };
  int EX[] = { 4, 1, 2, 3, 2, 3, 4, 1 };
  int P4[] = { 2, 4, 3, 1 };

  int[][] S0 = {
      { 1, 0, 3, 2 },
      { 3, 2, 1, 0 },
      { 0, 2, 1, 3 },
      { 3, 1, 3, 2 },
  };

  int[][] S1 = {
      { 1, 1, 2, 3 },
      { 2, 0, 1, 3 },
      { 3, 0, 1, 0 },
      { 2, 1, 0, 3 },
  };

  public void init() {
    int i, iterations;

    // printf("Entre com o número de operações: ");
    iterations = Integer.parseInt(scan.nextLine());

    char operation;

    char[] k = new char[10];
    char[] b = new char[8];

    char[] k1 = new char[8];
    char[] k2 = new char[8];

    char[][] responses = new char[iterations][];

    for (i = 0; i < iterations; i++)
      responses[i] = new char[8];

    for (i = 0; i < iterations; i++) {
      operation = scan.nextLine().charAt(0);

      k = scan.nextLine().toCharArray();

      b = scan.nextLine().toCharArray();

      this.get_keys(k, k1, k2);

      if (operation == 'C')
        responses[i] = encripty(b, k1, k2);
      else
        responses[i] = decripty(b, k1, k2);
    }

    for (i = 0; i < iterations; i++)
      System.out.println("\n" + responses[i].toString());

  }

  public void get_keys(char[] k, char[] k1, char[] k2) {
    char[] p10 = permuttation(k, P10, 10);

    char[] left = get_substring(p10, 0, 5);
    char[] rigth = get_substring(p10, 5, 5);

    left = rotate(left, 1, 5);
    rigth = rotate(rigth, 1, 5);

    k1 = permuttation(concat_strings(left, rigth), P8, 8);
    k2 = permuttation(concat_strings(rotate(left, 2, 5), rotate(rigth, 2, 5)), P8, 8);
  }

  char[] permuttation(char[] bits, int[] indexes, int size) {
    int i;

    char[] response = new char[size];

    for (i = 0; i < size; i++)
      response[i] = bits[indexes[i] - 1];

    return response;
  }

  char[] get_substring(char[] bits, int init, int size) {
    char[] response = new char[size];
    response = Arrays.copyOfRange(bits, init, size);
    return response;
  }

  char[] rotate(char[] bits, int displacement, int size) {
    int i = 0, k = 0;

    int[] first_bits = new int[displacement];
    char[] response = new char[size];

    for (i = 0; i < displacement; i++)
      first_bits[i] = bits[i];

    for (i = 0; i < size - displacement; i++)
      response[i] = bits[i + displacement];

    for (i = size - displacement; i < size; i++)
      response[i] = (char) first_bits[k++];

    return response;
  }

  char[] concat_strings(char[] left, char[] rigth) {
    char[] response = new char[10];
    StringBuilder sb = new StringBuilder(64);
    sb.append(left);
    sb.append(rigth);
    response = sb.toString().toCharArray();
    return response;
  }

  char[] encripty(char[] b, char[] k1, char[] k2) {
    return permuttation(f_function(swap(f_function(permuttation(b, IP, 8), k1), 8), k2), PI, 8);
  }

  char[] f_function(char[] bits, char[] key) {
    char[] left = get_substring(bits, 0, 4);
    char[] right = get_substring(bits, 4, 4);

    return concat_strings(xor(permuttation(blocks(xor(expand(right), key, 8)), P4, 4), left, 4), right);
  }

  char[] expand(char[] bits) {
    int i;
    char[] response = new char[8];

    for (i = 0; i < 8; i++)
      response[i] = bits[EX[i] - 1];

    return response;
  }

  char[] xor(char[] bits, char[] key, int size) {
    int i;
    char[] response = new char[size];

    for (i = 0; i < size; i++)
      if (bits[i] == '1' && key[i] == '0' || bits[i] == '0' && key[i] == '1')
        response[i] = '1';
      else
        response[i] = '0';

    return response;
  }

  char[] blocks(char[] bits) {
    char[] left = get_substring(bits, 0, 4);
    char[] right = get_substring(bits, 4, 4);

    int s0_row = binary_to_integer(left[0], left[3]);
    int s0_col = binary_to_integer(left[1], left[2]);

    int s1_row = binary_to_integer(right[0], right[3]);
    int s1_col = binary_to_integer(right[1], right[2]);

    return concat_strings(
        integer_to_binary(S0[s0_row][s0_col]),
        integer_to_binary(S1[s1_row][s1_col]));
  }

  int binary_to_integer(char a, char b) {
    if (a == '0' && b == '0')
      return 0;

    if (a == '0' && b == '1')
      return 1;

    if (a == '1' && b == '0')
      return 2;

    if (a == '1' && b == '1')
      return 3;

    return -1;
  }

  char[] integer_to_binary(int i) {
    char[] binary = new char[2];

    if (i == 0) {
      binary[0] = 0;
      binary[1] = 0;
      return binary;
    }

    if (i == 1) {
      binary[0] = 0;
      binary[1] = 1;
      return binary;
    }

    if (i == 2) {
      binary[0] = 1;
      binary[1] = 0;
      return binary;
    }

    if (i == 3) {
      binary[0] = 1;
      binary[1] = 1;
      return binary;
    }

    return binary;
  }

  char[] swap(char[] bits, int size) {
    int limit = size / 2;

    return concat_strings(get_substring(bits, limit, size - 1), get_substring(bits, 0, limit));
  }

  char[] decripty(char[] b, char[] k1, char[] k2) {
    return permuttation(f_function(swap(f_function(permuttation(b, IP, 8), k2), 8), k1), PI, 8);
  }

  public static void main(String[] args) {
    Sdes controller = new Sdes();
    controller.init();
  }
}