//Exercícios Threads com Semáforos
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Cavaleiro extends Thread {
   private static final int DISTANCIA_TOTAL = 2000;
   private static final int TOCHA_POSICAO = 500;
   private static final int PEDRA_POSICAO = 1500;
   private static final Lock tochaLock = new ReentrantLock();
   private static final Lock pedraLock = new ReentrantLock();
   private static boolean tochaPegada = false;
   private static boolean pedraPegada = false;
   private int velocidade;
   private int posicao = 0;
   private String nome;
   public Cavaleiro(String nome, int velocidade) {
       this.nome = nome;
       this.velocidade = velocidade;
   }
   @Override
   public void run() {
       Random random = new Random();
       while (posicao < DISTANCIA_TOTAL) {
           try {
               Thread.sleep(50);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           posicao += velocidade;
           if (posicao >= TOCHA_POSICAO && !tochaPegada) {
               tochaLock.lock();
               try {
                   if (!tochaPegada) {
                       tochaPegada = true;
                       velocidade += 2;
                       System.out.println(nome + " pegou a tocha e agora está a " + velocidade + " m por 50 ms.");
                   }
               } finally {
                   tochaLock.unlock();
               }
           }
           if (posicao >= PEDRA_POSICAO && !pedraPegada && !tochaPegada) {
               pedraLock.lock();
               try {
                   if (!pedraPegada) {
                       pedraPegada = true;
                       velocidade += 2;
                       System.out.println(nome + " pegou a pedra brilhante e agora está a " + velocidade + " m por 50 ms.");
                   }
               } finally {
                   pedraLock.unlock();
               }
           }
       }
       System.out.println(nome + " chegou ao final do corredor.");
   }
   public static void main(String[] args) {
       Cavaleiro[] cavaleiros = {
           new Cavaleiro("Cavaleiro 1", 2 + new Random().nextInt(3)),
           new Cavaleiro("Cavaleiro 2", 2 + new Random().nextInt(3)),
           new Cavaleiro("Cavaleiro 3", 2 + new Random().nextInt(3)),
           new Cavaleiro("Cavaleiro 4", 2 + new Random().nextInt(3))
       };
       for (Cavaleiro cavaleiro : cavaleiros) {
           cavaleiro.start();
       }
       for (Cavaleiro cavaleiro : cavaleiros) {
           try {
               cavaleiro.join();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       System.out.println("Todos os cavaleiros chegaram ao final do corredor.");
   }
}
