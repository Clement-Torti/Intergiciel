package linda.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import linda.shm.CentralizedLinda;

/** Création d'un serveur de nom intégré et d'un objet accessible à distance.
 *  Si la création du serveur de nom échoue, on suppose qu'il existe déjà (rmiregistry) et on continue. */
public class LindaServer extends UnicastRemoteObject {
    static RemoteLinda remoteLinda;
    static String SAVED_PATH = "./backup.txt";
    static Backup backup;

    protected LindaServer() throws RemoteException {
        super();
    }


    public static void main (String args[]) throws Exception {
        //  Création du serveur de noms
        try {
            LocateRegistry.createRegistry(4000);
            System.out.println("Running on localhost:4000");
        } catch (java.rmi.server.ExportException e) {
            System.out.println("A registry is already running, proceeding...");
        }


        remoteLinda = new RemoteLindaImp();
        CentralizedLinda cl = restore();

        if(cl != null) {
            remoteLinda.setCentralizedLinda(cl);
        }
        
        Registry reg = LocateRegistry.getRegistry(4000);
        reg.bind("LindaServer", remoteLinda);

        TimerTask task = new TimerTask() {
            public void run() {
               saveLinda();
            }
        };
        Timer timer = new Timer("Timer");
        
        long delay = 100000;
        timer.scheduleAtFixedRate(task, delay, delay);

        Thread printingHook = new Thread(() -> saveLinda());
        Runtime.getRuntime().addShutdownHook(printingHook);

        // Service prêt : attente d'appels
        System.out.println ("Le systeme est pret.");
    }

    public static CentralizedLinda restore() {
        try {            
            File file = new File(SAVED_PATH);

            if(file.exists()) {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read objects
                CentralizedLinda cl = (CentralizedLinda) oi.readObject();
                System.out.println(cl.tuples);

                oi.close();
                fi.close();

                return cl;

            } else {
                return null;
            }

			

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
        } catch (RemoteException e) {
                System.out.println("Remote exception on save");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveLinda() {

        try {
            CentralizedLinda cl = remoteLinda.getCentralizedLinda();
            
            File file = new File(SAVED_PATH);
            System.out.println(file.getAbsolutePath());

			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(cl);

			o.close();
			f.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
        } catch (RemoteException e) {
                System.out.println("Remote exception on save");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} 
    }




}
