package linda.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import javax.security.auth.callback.Callback;

import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.shm.CentralizedLinda;
import linda.Tuple;

public class RemoteLindaImp  extends UnicastRemoteObject implements RemoteLinda {

    CentralizedLinda cl = new CentralizedLinda();

    protected RemoteLindaImp() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public CentralizedLinda getCentralizedLinda() throws RemoteException {
        return cl;
    }

    @Override
    public void setCentralizedLinda(CentralizedLinda linda) throws RemoteException {
        cl = linda;
    }



    @Override
    public void write(Tuple t) throws RemoteException {
        cl.write(t);
    }

    @Override
    public Tuple take(Tuple template) throws RemoteException {
        return cl.take(template);
    }

    @Override
    public Tuple read(Tuple template) throws RemoteException {
        return cl.read(template);
    }

    @Override
    public Tuple tryTake(Tuple template) throws RemoteException {
        return cl.tryTake(template);
    }

    @Override
    public Tuple tryRead(Tuple template) throws RemoteException {
        return cl.tryRead(template);
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) throws RemoteException {
        return cl.takeAll(template);
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) throws RemoteException {
        return cl.readAll(template);
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, RemoteCallback callback)
            throws RemoteException {
        linda.Callback cb = new linda.Callback(){

            @Override
            public void call(Tuple t) {
                try {
                    callback.call(t);
                } catch(RemoteException e) {
                    System.out.println("Remote exception in eventRegister -> RemoteLindaImp");
                }
                
                
            }
            
        };
        cl.eventRegister(mode, timing, template, cb);
        
    }


    @Override
    public void debug(String prefix) throws RemoteException {
        cl.debug(prefix);
    }
    
}
