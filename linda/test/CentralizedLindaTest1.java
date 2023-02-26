package linda.test;

import linda.*;

// Vérifie que tous les read valides sont prioritaires
// On doit avoir:
// - D'abord le write
// - Puis les read (sauf le 6)
// - Finalement le take

public class CentralizedLindaTest1 {

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
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.read(motif);
                System.out.println("(2) READ Resultat:" + res);
                linda.debug("(2)");
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.read(motif);
                System.out.println("(3) READ Resultat:" + res);
                linda.debug("(3)");
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.read(motif);
                System.out.println("(4) READ Resultat:" + res);
                linda.debug("(4)");
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.read(motif);
                System.out.println("(5) READ Resultat:" + res);
                linda.debug("(5)");
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, Integer.class);
                Tuple res = linda.read(motif);
                System.out.println("(6) FAIL Shoudln't see this :" + res);
                linda.debug("(6)");
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
                System.out.println("(7) write: " + t3);
                linda.write(t3);
                                
                linda.debug("(7)");

            }
        }.start();
                
    }
}
