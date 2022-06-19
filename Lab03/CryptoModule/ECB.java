package CryptoModule;

import java.util.*;

public class ECB {
    private int N_BITS = 8;
    private int[] key;

    public ECB(int[] key) {
        this.key = key;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        // String s1 = scan.next();
        // String s1 = "testando lalala";
        String s1 = "d3:c8:a:d3:4d:f:74:80:81:5a:4d:5a:4d:5a:4d:";
        int[] key = { 0, 1, 0, 1, 0, 0, 1, 1, 1, 0 };

        // new ECB(key).encrypt(s1);
        new ECB(key).decrypt(s1);

        scan.close();
    }

    public int[] encrypt(String plaintext) {
        char[] plaintext_array = plaintext.toCharArray();
        int[] num = new int[plaintext_array.length];

        // convert plaintext to int[] num
        for (int i = 0; i < plaintext_array.length; i++) {
            num[i] = plaintext_array[i];
        }

        String s = "";
        String temps;

        // expand each character in plaintext to 8 bits ASCII code
        for (int i = 0; i < num.length; i++) {
            int tem = num[i];
            temps = Integer.toBinaryString(tem);

            while (temps.length() < 8) {
                temps = "0" + temps;
                if (temps.length() < 8)
                    temps = "0" + temps;
            }
            s = s + temps;

        }

        // System.out.println("Plaintext convert to binary: " + s);

        if (s.length() < N_BITS) {
            throw new ArrayIndexOutOfBoundsException("Array size wrong");
        } else {
            // get the number of blocks
            int numofblock = ((s.length() - (s.length() % N_BITS)) / N_BITS);

            if (s.length() % N_BITS > 0) {
                // padding 0 to plaintext to have equal N_BITS bits blocks
                int n = N_BITS - (s.length() - N_BITS * numofblock);
                for (int i = 0; i < n; i++) {
                    s = s + "0";
                }
            }

            // store binary text in the int[]
            int[] text_result = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                text_result[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
            }

            // System.out.println("The orginal plaintext: ");
            // System.out.println("{");
            // for (int i = 0; i < text_result.length; i++) {
            // System.out.print("" + text_result[i] + ",");
            // }
            // System.out.println("}" + " ---- Count : " + text_result.length);

            // divide plaintext in every N_BITS bits blocks
            int[][] blocks = new int[numofblock][N_BITS];

            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    blocks[i][j] = text_result[j + (i * N_BITS)];
                }
            }

            DES des = new DES(key);

            int[][] ciphers = new int[numofblock][N_BITS];
            for (int i = 0; i < numofblock; i++) {
                ciphers[i] = des.encrypt(blocks[i]);
            }

            int[] finaltext = new int[text_result.length];
            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    finaltext[j + (i * N_BITS)] = ciphers[i][j];
                }
            }

            int numofdex = finaltext.length / N_BITS;
            String binarystring = "";
            int[] final_output = new int[numofdex];

            for (int i = 0; i < numofdex; i++) {
                for (int j = i * N_BITS; j <= i * N_BITS + (N_BITS - 1); j++) {
                    binarystring = binarystring + finaltext[j];
                }

                int numtemp;
                numtemp = Integer.parseInt(binarystring, 2);
                final_output[i] = numtemp;
                binarystring = "";
            }

            for (int i = 0; i < final_output.length; i++) {
                System.out.print("" + Integer.toHexString(final_output[i]) + ":");
            }

            return final_output;

        }
    }

    public int[] decrypt(String encryptedString) {
        String[] splitted = encryptedString.split(":");
        int[] decimals = new int[splitted.length];

        // convert hexa to decimal
        for (int i = 0; i < decimals.length; i++) {
            decimals[i] = Integer.parseInt(splitted[i], 16);
        }

        String s = "";
        String temps;

        for (int i = 0; i < decimals.length; i++) {
            int tem = decimals[i];
            temps = Integer.toBinaryString(tem);

            while (temps.length() < 8) {
                temps = "0" + temps;
                if (temps.length() < 8)
                    temps = "0" + temps;
            }
            s = s + temps;

        }

        if (s.length() < N_BITS) {
            throw new ArrayIndexOutOfBoundsException("Array size wrong");
        } else {
            // get the number of blocks
            int numofblock = ((s.length() - (s.length() % N_BITS)) / N_BITS);

            if (s.length() % N_BITS > 0) {
                // padding 0 to plaintext to have equal N_BITS bits blocks
                int n = N_BITS - (s.length() - N_BITS * numofblock);
                for (int i = 0; i < n; i++) {
                    s = s + "0";
                }
            }

            // store binary text in the int[]
            int[] text_result = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                text_result[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
            }

            int[][] blocks = new int[numofblock][N_BITS];

            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    blocks[i][j] = text_result[j + (i * N_BITS)];
                }
            }

            DES des = new DES(key);

            int[][] ciphers = new int[numofblock][N_BITS];
            for (int i = 0; i < numofblock; i++) {
                ciphers[i] = des.decrypt(blocks[i]);
            }

            int[] finaltext = new int[text_result.length];
            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    finaltext[j + (i * N_BITS)] = ciphers[i][j];
                }
            }

            int numofdex = finaltext.length / N_BITS;
            String binarystring = "";
            int[] final_output = new int[numofdex];

            for (int i = 0; i < numofdex; i++) {
                for (int j = i * N_BITS; j <= i * N_BITS + (N_BITS - 1); j++) {
                    binarystring = binarystring + finaltext[j];
                }

                int numtemp;
                numtemp = Integer.parseInt(binarystring, 2);
                final_output[i] = numtemp;
                binarystring = "";
            }

            for (int i = 0; i < final_output.length; i++) {
                System.out.print((char) final_output[i]);
            }

            return final_output;

        }
    }
}
