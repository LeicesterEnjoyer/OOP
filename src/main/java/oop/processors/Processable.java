package oop.processors;

import java.util.HashMap;

/**
 * The Processable interface represents an object capable of processing a float value based on provided range limits.
 */
public interface Processable {
    /**
     * Processes the given float value based on provided range limits data and returns the processed value.
     *
     * @param value         The float value to be processed.
     * @param processData   The HashMap containing range limits data, where Keys are Limits and Values are the corresponding float values.
     * @return              The processed float value.
     */
    float process(float value, HashMap<Limits, Float> processData); 
}