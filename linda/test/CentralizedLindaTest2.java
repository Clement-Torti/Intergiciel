package linda.test;

import linda.*;

// Tuple supprimé après un take
// D'abord write
// Ensuite Take
// tryRead à null

public class CentralizedLindaTest2 {

    public static void main(String[] a) {
                
        // final Linda linda = new linda.shm.CentralizedLinda();
        final Linda linda = new linda.server.LindaClient("//localhost:4000/LindaServer");
                
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.take(motif);
                System.out.println("(1) TAKE Resultat:" + res);
                linda.debug("(1)");
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.tryRead(motif);
                System.out.println("(2) READ Resultat:" + res);
                linda.debug("(2)");
            }
        }.start();
                
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Tuple t3 = new Tuple(4, "foo");
                System.out.println("(3) write: " + t3);
                linda.write(t3);
                                
                linda.debug("(3)");

            }
        }.start();
                
    }
}
