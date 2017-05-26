package iris;

import normalizer.MaxMinParameters;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.neuroph.nnet.MultiLayerPerceptron;

public class IrisClassificationApp {

    public static void main(String[] args) {

        MultiLayerPerceptron neuralNet = null;
        MaxMinParameters     params    = null;

        // Now we read (deserialize) the multilayer perceptron
        // and the max-min normalizing parameters from the .ser files
        try {

            // Deserializing the parameters
            FileInputStream   fisParams = new FileInputStream("ser_NormalizingParameters.ser");
            ObjectInputStream oisParams = new ObjectInputStream(fisParams);

            params = (MaxMinParameters) oisParams.readObject();
            fisParams.close();
            oisParams.close();

            // Deserializing the trained neural network
            FileInputStream   fisNN = new FileInputStream("ser_TrainedModel.ser");
            ObjectInputStream oisNN = new ObjectInputStream(fisNN);

            neuralNet = (MultiLayerPerceptron) oisNN.readObject();
            fisNN.close();
            oisNN.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Now, let's test our newly-read parameters
        /*
        for (double max : params.maxIn) {
                System.out.println("Max In: " + max);
        }
        for (double min : params.minIn) {
                System.out.println("Min In: " + min);
        }
        */

        // Let's test our trained neural network
        // Supposed we have a data as follows:
        // sepal-length = 7.1
        // sepal-width  = 3.1
        // petal-length = 4.8
        // petal-width  = 1.5
        double[] input = { 7.1, 3.1, 4.8, 1.5 };

        // Before inputting into the model, don't forget to normalize the input
        double[] normalizedInput = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            // Normalize the input according to the max-min normalizing formula
            normalizedInput[i] = (input[i] - params.minIn[i]) / (params.maxIn[i] - params.minIn[i]);
        }

        neuralNet.setInput(normalizedInput);
        neuralNet.calculate();

        // The following is the classification result:
        double[] prediction = neuralNet.getOutput();
        for (double p : prediction) {
            System.out.print(p + " ");
        }
        // As it is, it's not immediately clear that the result is "versicolor",
        // but if you see closely, the prediction most closely resemble { 0, 1, 0 }
        // which is the one-hot encoding of Iris Versicolor

        /*
         * Just to make it more illustrative, let's make a function to interpret the prediction result
         */
        interpret(prediction);
    }
	
    public static void interpret(double[] prediction) {
        String[] labels = { "Iris Setosa", "Iris Versicolor", "Iris Virginica" };
        System.out.println("\nThe data is predicted as " + labels[maxOutput(prediction)]);
    }
	
    public static int maxOutput(double[] array) {

        double max = array[0];
        int index = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                index = i;
                max = array[i];
            }
        }
        return index;
    }
}
