package linda.server;

import java.rmi.RemoteException;

import linda.shm.CentralizedLinda;
import java.rmi.*;

public interface Backup extends Remote {
    public void alive(CentralizedLinda cl) throws RemoteException;
}
