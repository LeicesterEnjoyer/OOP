package oop.processors;

import java.util.HashMap;

/**
 * The ValueProcessor class provides methods for processing float data based on different process types.
 */
public class ValueProcessor {
    /**
     * A map containing process functions for different process types.
     */
    private static final HashMap<ProcessTypes, Processable> processFunctions = new HashMap<>();
    
    static {
        processFunctions.put(ProcessTypes.NONE, (value, processData) -> value);

        processFunctions.put(ProcessTypes.PERCENT, (value, processData) -> {
            float upperLimit = processData.get(Limits.LESS_THAN);
            float lowerLimit = processData.get(Limits.MORE_THAN);

            return (value - lowerLimit) / (upperLimit - lowerLimit) * 100;
        });

        processFunctions.put(ProcessTypes.NORMALIZE, (value, processData) -> {
            float upperLimit = processData.get(Limits.LESS_THAN);
            float lowerLimit = processData.get(Limits.MORE_THAN);

            return value / (upperLimit - lowerLimit);
        });
    }

    /**
     * Processes the given float data based on the specified process type and range limits.
     *
     * @param data          The float data to be processed.
     * @param type          The ProcessType indicating the type of processing to be applied.
     * @param processData   The HashMap containing range limits data, where keys are Limits and values are the corresponding float values.
     * @return              The processed float value based on the specified process type and range limits.
     */
    public static float processData (float data, ProcessTypes type, HashMap<Limits, Float> processData) {
        Processable processable = processFunctions.get(type);

        if (processable != null)
            return processable.process(data, processData);
        else
            return data;
    }
}