package modules.CryptoModule;

import java.util.ArrayList;
import java.util.List;

public class DES {
    public int[] originalKey;

    public DES(int []key) {
        this.originalKey = key;
    }

    public static class Key {
        private int[] originalKey;
        public List<Integer> k1;
        public List<Integer> k2;

        private int[] p10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
        private int[] p8 = { 6, 3, 7, 4, 8, 5, 10, 9 };

        public Key(int[] key) {
            this.originalKey = key;
            generateKeys();
        }

        private void generateKeys() {
            List<Integer> permutated, left, right, leftRotated, rightRotated;

            permutated = new ArrayList<Integer>();
            for (Integer index : p10) {
                permutated.add(this.originalKey[index - 1]);
            }

            left = permutated.subList(0, 5);
            right = permutated.subList(5, 10);

            leftRotated = leftRotate(left);
            rightRotated = leftRotate(right);

            generateK1(leftRotated, rightRotated);
            generateK2(leftRotated, rightRotated);
        }

        private void generateK1(List<Integer> left, List<Integer> right) {
            List<Integer> joinedKey;

            joinedKey = new ArrayList<>(left);
            ;
            for (Integer bit : right) {
                joinedKey.add(bit);
            }

            this.k1 = new ArrayList<Integer>();
            for (Integer index : p8) {
                this.k1.add(joinedKey.get(index - 1));
            }
        }

        private void generateK2(List<Integer> left, List<Integer> right) {
            List<Integer> leftRotated, rightRotated, joinedKey;

            leftRotated = leftRotate(leftRotate(left));
            rightRotated = leftRotate(leftRotate(right));

            joinedKey = new ArrayList<>(leftRotated);
            ;
            for (Integer bit : rightRotated) {
                joinedKey.add(bit);
            }

            this.k2 = new ArrayList<Integer>();
            for (Integer index : p8) {
                this.k2.add(joinedKey.get(index - 1));
            }
        }

        private List<Integer> leftRotate(List<Integer> bits) {
            List<Integer> newBits = new ArrayList<>(bits);
            int firstBit = newBits.remove(0);
            newBits.add(firstBit);
            return newBits;
        }
    }

    public static class Crypt {
        public int[] bits;
        public Key keys;
        public char operation;

        private int[] ip = { 2, 6, 3, 1, 4, 8, 5, 7 };
        private int[] fp = { 4, 1, 3, 5, 7, 2, 8, 6 };
        private int[] expand = { 4, 1, 2, 3, 2, 3, 4, 1 };
        private int[] p4 = { 2, 4, 3, 1 };

        private int[][] s0 = {
                { 1, 0, 3, 2 },
                { 3, 2, 1, 0 },
                { 0, 2, 1, 3 },
                { 3, 1, 3, 2 }
        };

        private int[][] s1 = {
                { 1, 1, 2, 3 },
                { 2, 0, 1, 3 },
                { 3, 0, 1, 0 },
                { 2, 1, 0, 3 }
        };

        public Crypt(int []originalKey, int[] bits, char operation) {
            this.keys = new Key(originalKey);;
            this.bits = bits;
            this.operation = operation;
        }

        public int[] execute() {
            if (operation == 'C') {
                return encrypt();
            }

            return decrypt();
        }

        private int[] encrypt() {
            List<Integer> permutated, left, right, result;
            int[] final_permutation;

            permutated = new ArrayList<Integer>();
            for (Integer index : ip) {
                permutated.add(this.bits[index - 1]);
            }

            left = permutated.subList(0, 4);
            right = permutated.subList(4, 8);

            result = fFunction(left, right, this.keys.k1);
            left = result.subList(0, 4);
            right = result.subList(4, 8);

            result = fFunction(right, left, this.keys.k2);

            final_permutation = new int[8];
            for (int i = 0; i < 8; i++) {
                final_permutation[i] = result.get(fp[i] - 1);
            }

            return final_permutation;
        }

        private int[] decrypt() {
            List<Integer> permutated, left, right, result;
            int [] final_permutation;

            permutated = new ArrayList<Integer>();
            for (Integer index : ip) {
                permutated.add(this.bits[index - 1]);
            }

            left = permutated.subList(0, 4);
            right = permutated.subList(4, 8);

            result = fFunction(left, right, this.keys.k2);
            left = result.subList(0, 4);
            right = result.subList(4, 8);

            result = fFunction(right, left, this.keys.k1);

            final_permutation = new int[8];
            for (int i = 0; i < 8; i++) {
                final_permutation[i] = result.get(fp[i] - 1);
            }

            return final_permutation;
        }

        private List<Integer> fFunction(List<Integer> left, List<Integer> right, List<Integer> key) {
            List<Integer> expanded, xOr, subLeft, subRight, joined, permutated, newLeft, finalBits;
            int[] s0, s1;

            expanded = new ArrayList<Integer>();
            for (Integer index : expand) {
                expanded.add(right.get(index - 1));
            }

            xOr = getXorBits(expanded, key);

            subLeft = xOr.subList(0, 4);
            subRight = xOr.subList(4, 8);

            s0 = sBox(subLeft, this.s0);
            s1 = sBox(subRight, this.s1);

            joined = new ArrayList<>();
            for (Integer bit : s0) {
                joined.add(bit);
            }

            for (Integer bit : s1) {
                joined.add(bit);
            }


            permutated = new ArrayList<>();
            for (Integer index : p4) {
                permutated.add(joined.get(index - 1));
            }

            newLeft = getXorBits(permutated, left);

            finalBits = new ArrayList<>(newLeft);
            for (Integer bit : right) {
                finalBits.add(bit);
            }

            return finalBits;
        }

        private List<Integer> getXorBits(List<Integer> bits1, List<Integer> bits2) {
            List<Integer> result = new ArrayList<>();
            int i;
            for (i = 0; i < bits1.size(); i++) {
                int k1_bit = bits1.get(i);
                int current_bit = bits2.get(i);

                int result_bit = k1_bit == current_bit ? 0 : 1;
                result.add(result_bit);
            }

            return result;
        }

        private int[] sBox(List<Integer> bits, int[][] matrix) {
            int row = binaryToDecimal(bits.get(0), bits.get(3));
            int col = binaryToDecimal(bits.get(1), bits.get(2));

            int binary = matrix[row][col];
            return decimalToBinary(binary);
        }

        private int binaryToDecimal(int bit1, int bit2) {
            if (bit1 == 1) {
                return bit2 == 1 ? 3 : 2;
            }

            return bit2 == 1 ? 1 : 0;
        }

        private int[] decimalToBinary(int decimal) {
            int[] result = new int[] {};
            switch (decimal) {
                case 0:
                    result = new int[] { 0, 0 };
                    break;
                case 1:
                    result = new int[] { 0, 1 };
                    break;
                case 2:
                    result = new int[] { 1, 0 };
                    break;
                case 3:
                    result = new int[] { 1, 1 };
                    break;
            }

            return result;
        }
    }

    public int[] encrypt (int[] bits) {
        return new Crypt(originalKey, bits, 'C').execute();
    }

    public int[] decrypt (int[] bits) {
        return new Crypt(originalKey, bits, 'D').execute();
    }
}