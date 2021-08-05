package training360.mentortools.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = FinishDateValidator.class)
public @interface IsValidFinishDate {

    String message() default "A vége dátumnak későbbinek kell lenni, mint a kezdő dátum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
