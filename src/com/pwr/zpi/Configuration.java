package com.pwr.zpi;

import com.pwr.zpi.exceptions.InvalidConfigurationException;
import com.pwr.zpi.language.Formula;

/**
 * This class is responsible for reading configuration from config file and to provide configuration values to
 * the application environment.
 *
 */
public class Configuration {
    /**
     * Thresholds for simple modalities.
     */
    public static final double MIN_POS = 0.2;
    public static final double MAX_POS = 0.6;
    public static final double MIN_BEL = 0.7;
    public static final double MAX_BEL = 0.9;
    public static final double KNOW = 1.0;

    /**
     * Thresholds for modal conjunctions.
     */
    private static final double CONJ_MIN_POS = 0.1;
    private static final double CONJ_MAX_POS = 0.6;
    private static final double CONJ_MIN_BEL = 0.65;
    private static final double CONJ_MAX_BEL = 0.9;
    public static final double CONJ_KNOW = 1.0;

    /**
     * Thresholds for modal disjunctions. //TODO rozwazyc inne wartosci
     */
    private static final double DISJ_MIN_POS = 0.4;
    private static final double DISJ_MAX_POS = 0.55;
    private static final double DISJ_MIN_BEL = DISJ_MAX_POS;
    private static final double DISJ_MAX_BEL = 1.0;
    public static final double DISJ_KNOW = 1.0;

    private static final double EX_DISJ_MIN_POS = 0.21;
    private static final double EX_DISJ_MAX_POS = 0.67;
    private static final double EX_DISJ_MIN_BEL = DISJ_MAX_POS;
    private static final double EX_DISJ_MAX_BEL = 1.0;
    public static final double EX_DISJ_KNOW = 1.0;


    /**
     * Arrays of thresholds for certain formula types. Order of elements in arrays is strictly defined and can't be other:
     * [MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW].
     */
    public final static double[] simpleThresholds = new double[]{MIN_POS, MAX_POS, MIN_BEL, MAX_BEL, KNOW};
    public final static double[] conjThresholds = new double[]{CONJ_MIN_POS, CONJ_MAX_POS, CONJ_MIN_BEL, CONJ_MAX_BEL, CONJ_KNOW};
    public final static double[] disjThresholds = new double[]{DISJ_MIN_POS, DISJ_MAX_POS, DISJ_MIN_BEL, DISJ_MAX_BEL, DISJ_KNOW};
    public final static double[] exDisjThresholds = new double[]{EX_DISJ_MIN_POS, EX_DISJ_MAX_POS, EX_DISJ_MIN_BEL, EX_DISJ_MAX_BEL, EX_DISJ_KNOW};


    /**
     * Returns array of appropriate thresholds for specified type. Yhis thresholds are used in grounding process.
     *
     * @param type
     * @return
     */
    public static double[] getThresholds(Formula.Type type) throws InvalidConfigurationException {
        double[] res = null;
        switch (type) {
            case SIMPLE_MODALITY:
                res= simpleThresholds;
                break;
            case MODAL_CONJUNCTION:
                res= conjThresholds;
                break;
            case MODAL_DISJUNCTION:
                res = disjThresholds;
                break;
            case MODAL_EXCLUSIVE_DISJUNCTION:
                res = exDisjThresholds;
                break;
            default:
                res= null;
        }
        if (res == null || res.length != 5)
            throw new InvalidConfigurationException("Invalid or not specified thresholds.");
        return res;
    }
}
