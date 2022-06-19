package CryptoModule;

import java.util.*;

public class CBC {
    private int N_BITS = 8;
    private int[] key, IV;

    public CBC(int[] key, int[] IV) {
        this.key = key;
        this.IV = IV;
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        String s1 = "testando lalala";
        // String s1 = "f1:9:73:52:be:98:5b:47:4a:2f:a7:b:6e:e2:71:";

        int[] key = { 0, 1, 0, 1, 0, 0, 1, 1, 1, 0 };
        int[] IV = { 0, 1, 1, 1, 0, 1, 0, 1, 1, 0 };

        String result = new CBC(key, IV).encrypt(s1);
        // String result = new CBC(key, IV).decrypt(s1);

        System.out.println(result);
        scan.close();
    }

    private int[] xor(int[] input1, int[] intput2) {
        int[] output = new int[input1.length];

        for (int i = 0; i < input1.length; i++) {
            output[i] = (input1[i] + intput2[i]) % 2;
        }
        return output;
    }

    private String encrypt(String plaintext) {
        char[] plaintext_array = plaintext.toCharArray();
        int[] num = new int[plaintext_array.length];

        // convert plaintext to int[] num
        for (int i = 0; i < plaintext_array.length; i++) {
            num[i] = plaintext_array[i];
        }

        String stext = "";
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
            stext = stext + temps;
        }

        if (stext.length() < N_BITS) {
            throw new ArrayIndexOutOfBoundsException("Array size wrong");
        } else {
            // get the number of blocks
            int numofblock = ((stext.length() - (stext.length() % N_BITS)) / N_BITS);

            if (stext.length() % N_BITS > 0) {
                // padding 0 to plaintext to have equal N_BITS bits blocks
                int n = N_BITS - (stext.length() - N_BITS * numofblock);
                for (int i = 0; i < n; i++) {
                    stext = stext + "0";
                }
            }

            // store binary text in the int[]
            int[] text_result = new int[stext.length()];
            for (int i = 0; i < stext.length(); i++) {
                text_result[i] = Integer.parseInt(String.valueOf(stext.charAt(i)));
            }

            // divide plaintext in every 64 bits blocks
            int[][] blocks = new int[numofblock][N_BITS];

            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    blocks[i][j] = text_result[j + (i * N_BITS)];
                }
            }

            DES des = new DES(key);
            int[] cBits = IV;

            int[][] ciphers = new int[numofblock][N_BITS];

            for (int i = 0; i < numofblock; i++) {
                int[] bits = xor(blocks[i], cBits);
                int[] encrypted = des.encrypt(bits);
                cBits = encrypted;
                ciphers[i] = encrypted;
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

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < final_output.length; i++) {
                hexString.append(Integer.toHexString(final_output[i]) + ":");
            }

            return hexString.toString();
        }
    }

    public String decrypt(String encryptedString) {
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
            int[] cBits = IV;

            int[][] plaintext = new int[numofblock][N_BITS];
            for (int i = 0; i < numofblock; i++) {
                int[] decrypted = des.decrypt(blocks[i]);
                int[] bits = xor(decrypted, cBits);
                cBits = blocks[i];
                plaintext[i] = bits;
            }

            int[] finaltext = new int[text_result.length];
            for (int i = 0; i < numofblock; i++) {
                for (int j = 0; j < N_BITS; j++) {
                    finaltext[j + (i * N_BITS)] = plaintext[i][j];
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

            char[] charArray = new char[final_output.length];
            for (int i = 0; i < final_output.length; i++) {
                charArray[i] = (char) final_output[i];
            }

            return new String(charArray);
        }
    }
}
