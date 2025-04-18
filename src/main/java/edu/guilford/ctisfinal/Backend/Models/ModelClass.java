package edu.guilford.ctisfinal.Backend.Models;

public class ModelClass {

    private  String modelName;
    private  String modelMemory;
    private  String modelPath;

    public ModelClass(String modelName, String modelMemory, String modelPath) {
        this.modelName = modelName;
        this.modelMemory = modelMemory;
        this.modelPath = modelPath;
    }

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelMemory() {
        return modelMemory;
    }
    public void setModelMemory(String modelMemory) {
        this.modelMemory = modelMemory;
    }
    public String getModelPath() {
        return modelPath;
    }



}
