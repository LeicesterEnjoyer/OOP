package oop.processors;

/**
 * The Validatable interface represents an object capable of validating a float value against a certain criteria.
 */
public interface Validatable {
    /**
     * Validates the given float value and returns the corresponding RangeStatuses enum indicating its status.
     *
     * @param value     The float value to be validated.
     * @return          The RangeStatuses enum indicating whether the value meets the validation criteria.
     */
    RangeStatuses validate(float value);
}