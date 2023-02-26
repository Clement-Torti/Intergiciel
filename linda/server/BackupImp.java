package linda.server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import linda.server.*;
import linda.shm.CentralizedLinda;

public class BackupImp extends UnicastRemoteObject implements Backup {

    protected BackupImp() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public void alive(CentralizedLinda cl) throws RemoteException {
        System.out.println("Alive");
        // Save cl
    }
    
}
