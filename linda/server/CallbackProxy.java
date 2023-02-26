package linda.server;
import java.rmi.RemoteException;
import java.rmi.server.*;


import linda.Tuple;
import linda.Callback;

public class CallbackProxy extends UnicastRemoteObject  implements RemoteCallback {
    Callback cb;

    public CallbackProxy(Callback cb) throws RemoteException {
        this.cb = cb;
    }

    @Override
    public void call(Tuple t) throws RemoteException {
        cb.call(t);
    }
    
}
