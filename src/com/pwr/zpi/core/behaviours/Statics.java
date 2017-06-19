package com.pwr.zpi.core.behaviours;

import com.pwr.zpi.language.Formula;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that gathers static fields used in order to synchronize threads in lifycycle
 */
public class Statics {

    /**
     * static Object foo is used for synchronization between Threads
     */
    private static final Object foo = new Object();
    /**
     * Semaphore that controls co-existence of AnswerThread and UpdateThread
     */
    private static int semaphore = 0;
    /**
     * list of formulas that are currently being processed by agent
     */
    private static List<Formula> formulasInProcess = new ArrayList<>();

    /**
     * method which blocks access for specific threads
     * @param isMemoryUpdateThread  true if blocking thread is the memory update thread
     */
    public static void acquire(boolean isMemoryUpdateThread)
    {
        synchronized (foo) {
            if (isMemoryUpdateThread) {
                while (semaphore != 0)
                    try {
                        foo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                semaphore--;
            } else {
                while (semaphore < 0)
                    try {
                        foo.wait();
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
    public static void release(boolean isMemoryUpdateThread)
    {
        synchronized (foo) {
            if (isMemoryUpdateThread)
                semaphore++;
            else semaphore--;
            foo.notifyAll();
        }
    }

    /**
     * Method called when there is formula to process. It waits till no similar formula is being processed
     * @param formula
     */
    public static void tryProccessingFormula(Formula formula)
    {
        while(!canFormulaBeProccessed(formula))
            synchronized (formulasInProcess)
            {
                try {
                    formulasInProcess.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * Method which checks if any similar formula is being processed at the moment, if none such was found it
     * adds given formula to list of ones in middle of processing
     * @param formula   to which currently processed formula are compared
     * @return true if none was found, false if similar was found
     */
    public static boolean canFormulaBeProccessed(Formula formula)
    {
        synchronized (formulasInProcess) {
            for (Formula f : formulasInProcess) {
                if (formula.isFormulaSimilar(f))
                    return false;
            }
            formulasInProcess.add(formula);
            return true;
        }
    }

    /**
     * method removes given formula from formulas being processed
     * @param formula   formula which was completely processed and is not used anymore
     */
    public static void removeFromFormulasInProccess(Formula formula)
    {
        synchronized (formulasInProcess)
        {
            formulasInProcess.remove(formula);
            formulasInProcess.notifyAll();
        }
    }

}
