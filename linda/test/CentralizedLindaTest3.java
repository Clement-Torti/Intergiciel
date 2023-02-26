package linda.test;

import java.util.Collection;

import linda.*;

// Test de readAll et takeAll


public class CentralizedLindaTest3 {

    public static void main(String[] a) {
                
        // final Linda linda = new linda.shm.CentralizedLinda();
        final Linda linda = new linda.server.LindaClient("//localhost:4000/LindaServer");
                

                
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Tuple t3 = new Tuple(4, "foo");
                System.out.println("(1) write: " + t3);
                linda.write(t3);

                Tuple t4 = new Tuple(5, "foo2");
                System.out.println("(1) write: " + t4);
                linda.write(t4);

                Tuple t5 = new Tuple(6, "foo3");
                System.out.println("(1) write: " + t5);
                linda.write(t5);

                Tuple invalide = new Tuple(6, 6);
                System.out.println("(1) write: " + invalide);
                linda.write(invalide);
                                
            }
        }.start();


        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Collection<Tuple> res = linda.readAll(motif);
                for(Tuple t: res) {
                    System.out.println("(2) READALL" + t);
                }
                
            }
        }.start();


        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Collection<Tuple> res = linda.takeAll(motif);
                for(Tuple t: res) {
                    System.out.println("(2) takeAll" + t);
                }
                
            }
        }.start();


        new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Tuple t3 = new Tuple(4, "foo");
                Tuple res = linda.tryRead(t3);
                System.out.println("This should be null" + res);
                                
            }
        }.start();
                
    }
}
