package com.musigma.controllers.components;

import com.musigma.utils.exceptionMethods.Setter;

import static com.musigma.utils.Dialogs.tryCatch;

public class FloatTextField extends NotEmptyTextField {

    private boolean positive;
    private boolean notNull;

    public FloatTextField() {
        super();
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[+-]?\\d+(?:\\.\\d+)?")) {
                input.setText(newValue.replaceAll("[^(+|\\-|\\.|\\d)]*", ""));
            }
        });
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public boolean isValid() {
        if (super.isValid()) {
            float value = Float.parseFloat(input.getText());
            if (positive && value < 0){
                setError("La valeur doit être positive");
                return false;
            }
            else if (notNull && value == 0){
                setError("La valeur doit être non-nulle");
                return false;
            }
            return super.isValid();
        } else return false;
    }

    public void bindFloat(String errrorMsg, Setter<Float> setter) {
        input.setOnKeyTyped(e -> {
            if (isValid())
                tryCatch(
                    errrorMsg,
                    () -> setter.accept(Float.parseFloat(input.getText()))
                );
        });
    }

    public void bindFloat(String errrorMsg, Float value, Setter<Float> setter) {
        input.setText(Float.toString(value).replaceAll("0*$", "").replaceAll("\\.$", ""));
        isValid();
        bindFloat(errrorMsg, setter);
    }
}
