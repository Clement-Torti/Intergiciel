package linda.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {
    RemoteLinda remoteLinda;
    String serverURI;
	
    /** Initializes the Linda implementation.
     *  @param serverURI the URI of the server, e.g. "rmi://localhost:4000/LindaServer" or "//localhost:4000/LindaServer".
     */
    public LindaClient(String serverURI) {
        this.serverURI = serverURI;        

        //  Connexion au serveur de noms (obtention d'un handle)
        try {
            remoteLinda = (RemoteLinda) Naming.lookup("rmi:" + serverURI);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void write(Tuple t) {
        // TODO Auto-generated method stub
        try {
            remoteLinda.write(t);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public Tuple take(Tuple template) {
        try {
            Tuple t =  remoteLinda.take(template);
            return t;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Tuple read(Tuple template) {
        try {
            return remoteLinda.read(template);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Tuple tryTake(Tuple template) {
        try {
            return remoteLinda.tryTake(template);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Tuple tryRead(Tuple template) {
        try {
            return remoteLinda.tryRead(template);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) {
        try {
            return remoteLinda.takeAll(template);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) {
        try {
            return remoteLinda.readAll(template);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        
        try {
            RemoteCallback cb = new CallbackProxy(callback);
            remoteLinda.eventRegister(mode, timing, template, cb);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void debug(String prefix) {
        // TODO Auto-generated method stub
        
    }
    

}
