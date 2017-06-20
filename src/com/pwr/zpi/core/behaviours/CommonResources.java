package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.language.Formula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class holding common resources for threads, like formulas being processed or semaphore
 */
public class CommonResources {

    /**
     * List of formulas that are currently being processed by agent
     */
    List<Formula> formulasInProcessing = Collections.synchronizedList(new ArrayList<>());
    /**
     * Object used in synchronization
     */
    public static final Object foo = new Object();
    /**
     * Object used in synchronization
     */
    public static final Object foo2 = new Object();

    /**
     * Semaphore that controls co-existence of AnswerThread and UpdateThread
     */
    private static int semaphore = 0;

    /**
     * method which blocks access for specific threads
     * @param isMemoryUpdateThread  true if blocking thread is the memory update thread
     */
    public void acquire(boolean isMemoryUpdateThread)
    {
        synchronized (foo2) {
            if (isMemoryUpdateThread) {
                while (semaphore != 0)
                    try {
                        foo2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                semaphore--;
            } else {
                while (semaphore < 0)
                    try {
                        foo2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                semaphore++;
            }
        }
    }
    /**
     * method releases blockage and allows other threads to access resources
     * @param isMemoryUpdateThread  true if releasing thread is the memory update one
     */
    public void release(boolean isMemoryUpdateThread)
    {
        synchronized (foo2) {
            if (isMemoryUpdateThread)
                semaphore++;
            else semaphore--;
            foo2.notifyAll();
        }
    }

    /**
     * Adds next formula to formulasInProcessing
     * @param formula   Formula to be added
     */
    public void addFormula(Formula formula)
    {
        synchronized (foo)
        {
            while(checkIfContains(formula))
                try {
                System.out.print("Waiting");
                    foo.wait();
                } catch (InterruptedException e) {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Interrupted", e);
                }
            formulasInProcessing.add(formula);
        }
    }

    /**
     * Removes formula which agent has finished processing from formulasInProcessing
     * @param formula   formula to be removed
     */
    public void removeFormula(Formula formula)
    {
        synchronized (foo) {
            formulasInProcessing.remove(formula);
            foo.notifyAll();
        }
    }

    /**
     * Checks if a similar formula is being processed at the moment
     * @param formula   Formula to be checked
     * @return          true if there is one like this one, false otherwise
     */
    public boolean checkIfContains(Formula formula)
    {
        synchronized (foo) {
            Iterator i = formulasInProcessing.iterator();
            while (i.hasNext()) {
                Formula formula1 = ((Formula) i.next());
                if (formula1.isFormulaSimilar(formula)) {
                    System.out.print("cos");
                    return true;
                }
            }
        }
        return false;
    }
}
