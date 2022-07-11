package modules.KeyChanger;

import java.math.BigInteger;

public class DH {
    public int q, alfa;
    private int x, y;

    public int gerarX() {
        return (int) (Math.random() * this.getQ());
    }

    public DH(int i, int j) {
        int primes_options[] = {2, 3, 7, 11, 13};
        int alfa_options[][] = { { 2, 3, 7, 8, 11 }, { 3, 5, 6, 10, 12 }, { 3, 6, 7, 12, 14 }, { 2, 3, 12, 15, 17 },
                { 3, 5, 12, 13, 14 } };
        this.q = primes_options[i];
        this.alfa = alfa_options[i][j];
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getAlfa() {
        return alfa;
    }

    public void setAlfa(int alfa) {
        this.alfa = alfa;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x < this.getQ()) {
            this.x = x;
            this.generateY();
        } else
            return;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void generateY() {
        BigInteger aux_x, aux_y, aux_alfa, aux_q;
        aux_x = new BigInteger(Integer.toString(this.getX()));
        aux_alfa = new BigInteger(Integer.toString(this.getAlfa()));
        aux_q = new BigInteger(Integer.toString(this.getQ()));

        aux_y = aux_alfa.modPow(aux_x, aux_q);
        System.out.println("Y: " + aux_y.intValue());

        this.setY(aux_y.intValue());
    }

    public BigInteger generateK(int otherY) {
        BigInteger aux_k, aux_x, aux_y, aux_q;
        Integer.toString(this.getX());
        aux_x = new BigInteger(Integer.toString(this.getX()));
        aux_y = new BigInteger(Integer.toString(otherY));
        aux_q = new BigInteger(Integer.toString(this.getQ()));
        aux_k = aux_y.modPow(aux_x, aux_q);
        System.out.println("K: " + aux_k.intValue());
        return aux_k;
    }
}