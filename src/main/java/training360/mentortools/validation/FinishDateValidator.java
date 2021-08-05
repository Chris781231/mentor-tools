package training360.mentortools.validation;

import training360.mentortools.entity.InternalDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FinishDateValidator implements ConstraintValidator<IsValidFinishDate, InternalDates> {

    @Override
    public void initialize(IsValidFinishDate constraint) {
        ConstraintValidator.super.initialize(constraint);
    }

    public boolean isValid(InternalDates internalDates, ConstraintValidatorContext context) {
        return (internalDates.getFinishDate() == null ||
                internalDates.getStartDate() != null && internalDates.getStartDate().isBefore(internalDates.getFinishDate()));
    }
}
