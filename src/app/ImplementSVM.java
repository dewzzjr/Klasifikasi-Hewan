/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

/**
 *
 * @author Dewangga
 */
public final class ImplementSVM extends ZooApp {
    private svm_model model;
    
    public ImplementSVM(String filename) {
        loadModel(filename);
    }

    @Override
    public void loadModel(String filename) {
        try {
            model = svm.svm_load_model(filename);
        } catch (IOException ex) {
        }
    }

    @Override
    protected void calculate() {
        svm_node[] nodes = new svm_node[super.input.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new svm_node();
            nodes[i].index = i;
            nodes[i].value = input[i];
        }
        super.output = (int) svm.svm_predict(model, nodes);
        System.out.println("hasil : " + output);
    }
    
    public static void main(String[] args) {
        ZooApp a = new ImplementSVM("model10_90.svm");
        //double [] input = {1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0}; // Mamalia
        double [] input = {0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0}; // Ikan
        //double [] input = {0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0}; // Invertebrata
        a.setInput(input);
        System.out.println(a.getOutput());
    }
}

