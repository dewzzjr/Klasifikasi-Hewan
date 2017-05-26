/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author Dewangga
 */
public abstract class ZooApp {
    protected int output;
    protected double input[];
    
    protected ZooApp() { }
    
    public abstract void loadModel(String filename);
    protected abstract void calculate();
    
    public void setInput( double [] input ) {
        this.input = input;
        calculate();
    }
    
    public ClassType getOutput() {
        for (ClassType type : ClassType.values()) {
            if (type.getCode() == output) {
                return type;
            }
        }
        return ClassType.NULL;
    }
    

    
}
