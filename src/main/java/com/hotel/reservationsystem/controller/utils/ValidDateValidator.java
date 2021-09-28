package com.hotel.reservationsystem.controller.utils;


        import javax.validation.ConstraintValidator;
        import javax.validation.ConstraintValidatorContext;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {




    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        return isValidFormat("yyyy-MM-dd", value);
    }

    private static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (value != null){
                date = sdf.parse(value);
                if (!value.equals(sdf.format(date))) {
                    date = null;
                }
            }

        } catch (ParseException ex) {
        }
        return date != null;
    }
}