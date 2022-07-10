package modules.KeyChanger;

import java.math.BigInteger;
public class DH {
  public int q, alfa;
  private int x, y;

  public int gerarX()
  {
      return (int) (Math.random()*this.getQ());
  }

  public DH(int i, int j){
      int primes_options[] = {241, 251, 257, 263, 269};
      int alfa_options[][] = {{2, 3, 7, 8, 11}, {3, 5, 6, 10, 12}};
      this.q = primes_options[i];
      this.alfa = alfa_options[i][j];
  }

  public int getQ(){
      return q;
  }

  public void setQ(int q){
      this.q = q;
  }

  public int getAlfa(){
      return alfa;
  }

  public void setAlfa(int alfa){
      this.alfa = alfa;
  }

  public int getX() {
      return x;
  }

  public void setX(int x){
      if (x < this.getQ())
      {
          this.x = x;
          this.generateY();
      }
      else
          return;
  }

  public int getY(){
      return y;
  }

  public void setY(int y){
      this.y = y;
  }

  public void generateY(){
      BigInteger x, y, alfa, q;
      x = new BigInteger(Integer.toString(this.getX()));
      alfa = new BigInteger(Integer.toString(this.getAlfa()));
      q = new BigInteger(Integer.toString(this.getQ()));

      y = alfa.modPow(x, q);
      System.out.println("Resultado Y eh: " + y.intValue() );

      this.setY(y.intValue());
  }

  public BigInteger generateK(int otherY){
      BigInteger k, x, y, q;
      Integer.toString(this.getX());
      x = new BigInteger(Integer.toString(this.getX()));
      y = new BigInteger(Integer.toString(otherY));
      q = new BigInteger(Integer.toString(this.getQ()));
      k = y.modPow(x, q);
      System.out.println("Resultado K eh: " + k.intValue() );
      return k;
  }
}