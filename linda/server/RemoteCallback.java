package linda.server;

import java.io.Serializable;

import linda.Callback;
import linda.Tuple;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;

public interface RemoteCallback extends Remote {
    public void call(Tuple t) throws RemoteException;
}
