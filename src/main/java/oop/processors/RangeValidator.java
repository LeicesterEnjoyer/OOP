package oop.processors;

import java.util.HashMap;

/**
 * The RangeValidator class provides methods for validating whether a given value falls within a specified range.
 */
public class RangeValidator {
    /**
     * Returns a validator based on the provided range data.
     *
     * @param rangeData     The HashMap containing range limits data, where keys are Limits and values are the corresponding float values.
     * @return              The Validatable object representing the validator for the specified range.
     */
    private static Validatable getValidator(HashMap<Limits, Float> rangeData) {
        if (!rangeData.containsKey(Limits.LESS_THAN))
            return value -> (rangeData.get(Limits.MORE_THAN) <= value) ? RangeStatuses.NORMAL : RangeStatuses.LOWER;
        else if (!rangeData.containsKey(Limits.MORE_THAN))
            return value -> (value <= rangeData.get(Limits.LESS_THAN)) ? RangeStatuses.NORMAL : RangeStatuses.HIGHER;
            
        return value -> (rangeData.get(Limits.MORE_THAN) <= value && value <= rangeData.get(Limits.LESS_THAN)) ? RangeStatuses.NORMAL : ((value < rangeData.get(Limits.MORE_THAN)) ? RangeStatuses.LOWER : RangeStatuses.HIGHER);
    }

    /**
     * Validates the given value against the specified range.
     *
     * @param value         The float value to be validated.
     * @param rangeData     The HashMap containing range limits data, where keys are Limits and values are the corresponding float values.
     * @return              The RangeStatuses enum indicating whether the value falls within the specified range.
     */
    public static RangeStatuses validateRange(float value, HashMap<Limits, Float> rangeData) {
        Validatable validator = getValidator(rangeData);
        
        return validator.validate(value);
    }
}