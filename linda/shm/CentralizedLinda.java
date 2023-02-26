package linda.shm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda, Serializable {
	public Collection<Tuple> tuples = new ArrayList<Tuple>();
    ReentrantLock tuplesLock = new ReentrantLock();
    
    volatile Collection<Waiter> waitings = new ArrayList<>();
    ReentrantLock waitingsLock = new ReentrantLock();
    



    class Waiter implements Serializable {
        Tuple template;
        eventMode mode;
        volatile Callback cb;

        public Waiter(Tuple template, eventMode mode) {
            this.template = template;
            this.mode = mode;
            this.cb = null;
        }

        public Waiter(Tuple template, eventMode mode, Callback cb) {
            this.template = template;
            this.mode = mode;
            this.cb = cb;
        }
    }




    public CentralizedLinda() {

    }

    @Override
    public void write(Tuple t) {
        Collection<Waiter> toDelete = new ArrayList<>();

        // Ajout du tuple dans la liste
        tuplesLock.lock();
        tuples.add(t);
        tuplesLock.unlock();

        // Prévenir les abonnés au bon format
        //  - Tous les read
        //  - Plus 1 take ou un eventRegister

        // Read
        waitingsLock.lock();
        for(Waiter waiter: waitings) {
            if(t.matches(waiter.template) && waiter.mode == eventMode.READ) {
                if(waiter.cb != null) {
                    waiter.cb.call(t);
                } else {
                    synchronized(waiter.template) {
                        waiter.template.notify();
                    }
                }

                toDelete.add(waiter);
            }

            
        }
        waitingsLock.unlock();


        // Take
        for(Waiter waiter: waitings) {
            if(t.matches(waiter.template) && waiter.mode == eventMode.TAKE) {
                if(waiter.cb != null) {
                    waiter.cb.call(t);
                    toDelete.add(waiter);
                    break;
                } else {
                    synchronized(waiter.template) {
                        waiter.template.notify();
                        toDelete.add(waiter);
                    }
                    break;
                }
                
            }
        }

        
        // Vider les tuples traités
        for(Waiter toDel: toDelete) {
            waitingsLock.lock();
            waitings.remove(toDel);
            waitingsLock.unlock();
        }

    }

    @Override
    public Tuple take(Tuple template) {
        Tuple t = tryTake(template);

        // Attente bloquante
        if(t == null) {
            // Attente bloquante
            Waiter waiter = new Waiter(template, eventMode.TAKE);
            synchronized(waiter.template) {
                try {
                    waitingsLock.lock();
                    waitings.add(waiter);  
                    waitingsLock.unlock();              
                    template.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = tryTake(template);
        }

        return t;
    }

    @Override
    public Tuple read(Tuple template) {
        Tuple t = tryRead(template);

        // Attente bloquante
        if(t == null) {
            // Attente bloquante
            Waiter waiter = new Waiter(template, eventMode.READ);
            synchronized(waiter.template) {
                try {
                    waitingsLock.lock();
                    waitings.add(waiter); 
                    waitingsLock.unlock();
                    template.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = tryRead(template);
        }

        return t;
    }

    @Override
    public Tuple tryTake(Tuple template) {

        Tuple t = tryRead(template);

        if(t != null) {
            tuplesLock.lock();
            tuples.remove(t);
            tuplesLock.unlock();
        }

        return t;
    }

    @Override
    public Tuple tryRead(Tuple template) {
        
        for(Tuple t: tuples) {
            if(t.matches(template)) {
                Tuple match = t;
                return match;
            }
        }

        return null;
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) {
        Collection<Tuple> validTuples = readAll(template);

        for(Tuple t: validTuples) {
            tuplesLock.lock();
            tuples.remove(t);
            tuplesLock.unlock();
        }

        return validTuples;
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) {
        Collection<Tuple> validTuples = new ArrayList<>();

        for(Tuple t: tuples) {
            if(t.matches(template)) {
                validTuples.add(t);
            }
        }

        return validTuples;
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        if(timing == eventTiming.IMMEDIATE) {
            if(mode == eventMode.READ) {
                Tuple tuple = tryRead(template);

                if(tuple != null) {
                    callback.call(tuple);
                    return;
                } 
            } else if(mode == eventMode.TAKE) {
                Tuple tuple = tryTake(template);

                if(tuple != null) {
                    callback.call(tuple);
                    return;
                } 
            }

        }



        if(mode == eventMode.READ) {
            Waiter waiter = new Waiter(template, eventMode.READ, callback);
            waitingsLock.lock();
            waitings.add(waiter);
            waitingsLock.unlock();
        } else if (mode == eventMode.TAKE) {
            Waiter waiter = new Waiter(template, eventMode.TAKE, callback);
            waitingsLock.lock();
            waitings.add(waiter);
            waitingsLock.unlock();
        }
      
    }

    @Override
    public void debug(String prefix) {
        System.out.println("Unimplemented debug");
    }
}
