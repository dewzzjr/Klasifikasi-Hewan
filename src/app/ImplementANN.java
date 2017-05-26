/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import normalizer.MaxMinParameters;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 *
 * @author Dewangga, Hendro, Ave
 */
public final class ImplementANN extends ZooApp {
    private MultiLayerPerceptron neuralNet;
    private MaxMinParameters params;

    public ImplementANN(String filename, String param) {
        loadParameter(param);
        loadModel(filename);
    }

    @Override
    public void loadModel(String filename) {
        try {
            // Deserializing the trained neural network
            FileInputStream   fisNN = new FileInputStream(filename);
            ObjectInputStream oisNN = new ObjectInputStream(fisNN);

            neuralNet = (MultiLayerPerceptron) oisNN.readObject();
            fisNN.close();
            oisNN.close();

        } catch (FileNotFoundException e) {
            System.out.println(e + "\nFile Model Not Found : " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void calculate() {
        double [] normalInput = normalize(super.input);
        neuralNet.setInput(normalInput);
        neuralNet.calculate();
        // The following is the classification result:
        double[] prediction = neuralNet.getOutput();
        for (double p : prediction) {
            System.out.print(p + " ");
        }
        super.output = maxOutput(prediction);
    }
    
    private double[] normalize( double[] input ) {
        double []normalizedInput = input;
        for (int i = 0; i < input.length; i++) {
            // Normalize the input according to the max-min normalizing formula
            normalizedInput[i] = (input[i] - params.minIn[i]) / (params.maxIn[i] - params.minIn[i]);
        }
        return normalizedInput;
    }
    
    private void loadParameter(String filename) {
        try {
            // Deserializing the parameters
            FileInputStream   fisParams = new FileInputStream(filename);
            ObjectInputStream oisParams = new ObjectInputStream(fisParams);

            params = (MaxMinParameters) oisParams.readObject();
            fisParams.close();
            oisParams.close();

        } catch (FileNotFoundException e) {
            System.out.println(e + "\nFile Parameter Not Found : " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
    
    private int maxOutput(double[] array) {

        double max = array[0];
        int index = 1;

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                index = i + 1;
                max = array[i];
            }
        }
        return index;
    }
    
    public static void main(String[] args) {
        ZooApp a = new ImplementANN("zoo.model","zoo.normalize");
        //double [] input = {1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0}; // Mamalia
        //double [] input = {0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0}; // Ikan
        double [] input = {0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0}; // Invertebrata
        a.setInput(input);
        System.out.println(a.getOutput());
    }
}
