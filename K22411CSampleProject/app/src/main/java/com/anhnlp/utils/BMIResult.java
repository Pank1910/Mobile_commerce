package com.anhnlp.utils;

public class BMIResult {
    private double BMI;
    private String description;

    public BMIResult(double BMI, String description) {
        this.BMI = BMI;
        this.description = description;
    }

    public BMIResult() {
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
