package linda.test;

import linda.*;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;


/*
(1) write: [ 1 "foo" ]
CB got [ 1 "foo" ]
CB got [ 1 "foo" ]
Après le eventRegister Immediate
(2) write: [ 2 "foo" ]
CB got [ 2 "foo" ]
(3) write: [ 3 "foo" ]
CB got [ 3 "foo" ]
*/
public class CentralizedLindaTest4 {

    private static Linda linda;
    private static Tuple cbmotif;

    private static class MyCallback implements Callback {
        public void call(Tuple t) {
            System.out.println("CB got "+t);
        }
    }

    public static void main(String[] a) {
        // linda = new linda.shm.CentralizedLinda();
        final Linda linda = new linda.server.LindaClient("//localhost:4000/LindaServer");

        Tuple t1 = new Tuple(1, "foo");
        System.out.println("(1) write: " + t1);
        linda.write(t1);

        cbmotif = new Tuple(Integer.class, String.class);
        //Celui-ci va lire le tuple 1
        linda.eventRegister(eventMode.READ, eventTiming.IMMEDIATE, cbmotif, new CentralizedLindaTest4.MyCallback());

        // Celui-ci va prendre le tuple 1
        linda.eventRegister(eventMode.TAKE, eventTiming.IMMEDIATE, cbmotif, new CentralizedLindaTest4.MyCallback());
        // Celui-ci va attendre le prochain write car aucun disponible
        linda.eventRegister(eventMode.READ, eventTiming.IMMEDIATE, cbmotif, new CentralizedLindaTest4.MyCallback());
        
        System.out.println("Après le eventRegister Immediate");

        Tuple t2 = new Tuple(2, "foo");
        System.out.println("(2) write: " + t2);
        linda.write(t2);
        // Et lui va attendre le prochain write par défault

        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new CentralizedLindaTest4.MyCallback());

        Tuple t3 = new Tuple(3, "foo");
        System.out.println("(3) write: " + t3);
        linda.write(t3);

        linda.debug("()");

    }

}