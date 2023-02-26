package linda.test;

import linda.*;

// Vérifie que tous les read valides sont prioritaires
// On doit avoir:
// - D'abord le write
// - Puis les read (sauf le 6)
// - Finalement le take

public class TestBackup {

    public static void main(String[] a) {
                
        // final Linda linda = new linda.shm.CentralizedLinda();
        final Linda linda = new linda.server.LindaClient("//localhost:4000/LindaServer");

        Tuple t2 = new Tuple(5, "Coucou");
        System.out.println("(0) write: " + t2);
        linda.write(t2);
                
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tuple motif = new Tuple(Integer.class, String.class);
                Tuple res = linda.take(motif);
                System.out.println("(1) TAKE Resultat:" + res);
                linda.debug("(1)");
            }
        }.start();
                
    }
}
