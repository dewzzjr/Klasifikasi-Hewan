/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zoo;

import java.io.BufferedWriter;
import normalizer.MaxMinNormalizerFieldsAccessible;
import normalizer.MaxMinParameters;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
// import org.neuroph.util.data.norm.MaxMinNormalizer;
// import org.neuroph.util.data.norm.Normalizer;

/**
 *
 * @author Dewangga
 */
public class AnimalClassification {
    public static void main(String[] args) {
        // 1. INITIATION
        int input = 19;
        int output = 7;
        double learningRate = 0.5;
        double maxError = 0.01;
        int maxIterations = 10000;
        
        // 2. CONFIGURATION
        int hidden = 20;
        int trainSetPercent = 70;
        int testSetPercent  = 30;
        
        // 3. INPUT DATASET
        DataSet dataSet = new DataSet(input, output);
        setColumnNames(dataSet);
        readFile(dataSet);
        
        // 4. SPLIT DATASET
        DataSet[] training_testing = dataSet.createTrainingAndTestSubsets(trainSetPercent, testSetPercent);
        for (DataSet ds : training_testing) {
            setColumnNames(ds);
        }
        DataSet trainSet = training_testing[0];
        DataSet testSet  = training_testing[1];
        
        // 5. NEURAL NETWORK CONFIGURATION
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(input, hidden, output);
        BackPropagation learningRule = neuralNet.getLearningRule();
	
        learningRule.setLearningRate(learningRate);
        learningRule.setMaxError(maxError);
        learningRule.setMaxIterations(maxIterations);
        
        addLearningEventListener(learningRule);
        // Normalizer normal = new MaxMinNormalizer();
        
        // 6. NORMALIZE TRAINING SET
        MaxMinNormalizerFieldsAccessible mmn = new MaxMinNormalizerFieldsAccessible();
        mmn.normalize(trainSet);
        
        // 7. LEARNING AND WRITING MODEL
        neuralNet.learn(trainSet);
        //writeFile(neuralNet, mmn);
        
        // 8. NORMALIZE TEST SET AND MODEL ACCURACY
        double[] maxIn = mmn.getMaxIn();
        double[] minIn = mmn.getMinIn();
        mmn.normalize(testSet, minIn, maxIn);
        testAccuracy(neuralNet, testSet);
    }
    
    private static void addLearningEventListener(LearningRule learningRule) {
        learningRule.addListener(new LearningEventListener() {
            @Override
            public void handleLearningEvent(LearningEvent event) {
                BackPropagation bp = (BackPropagation) event.getSource();
                if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
                    System.out.println();
                    System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations");
                    System.out.println("With total error " + bp.getTotalNetworkError() + '\n');
                } else {
                    System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
                }
            }
        });
    }
    
    private static void testAccuracy(MultiLayerPerceptron neuralNet, DataSet testSet) {
        // Here is the code for testing the accuracy of the model neuralNet using the test data testSet
        int match = 0;
        int total = 0;
        for (DataSetRow testSetRow : testSet.getRows()) {
                total += 1;
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();

            int desired = 0;
            double[] desiredArray = testSetRow.getDesiredOutput();
            for (int i = 0; i < desiredArray.length; i++) {
                if (desiredArray[i] == 1) {
                        desired = i;
                        break;
                }
            }

            int output = maxOutput(neuralNet.getOutput());

            if (desired == output) {
                match += 1;
            }
        }
        System.out.println("The model predicts correctly on " + match + " out of " + total + " test data instances.");
        System.out.println("Accuracy on test set: " + 100.0*match/total + "%");	
    }
    
    private static int maxOutput(double[] array) {
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
        
    private static void readFile(DataSet dataSet) {
        try {
            String value;
            Stream<String> stream = Files.lines(Paths.get("zoo.csv"));
            
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("zoo.data"), StandardCharsets.UTF_8)) {
                Object[] lines = stream.toArray();
                for (Object line : lines) {
                    // System.out.println(line);
                    if (line instanceof String) {
                        String[] split = ((String)line).split(";");
                        double mammal = 0, bird = 0,
                                reptile = 0, fish = 0,
                                amphibia = 0, bug = 0,
                                invertebrate = 0;
                        
                        DataSetRow newRow = null; 
                        
                        if (split.length == 18) {
                            if (split[17].contains("1")){ mammal = 1; } else
                                if (split[17].contains("2")){ bird = 1; } else
                                    if (split[17].contains("3")){ reptile = 1; } else
                                        if (split[17].contains("4")){ fish = 1; } else
                                            if (split[17].contains("5")){ amphibia = 1; } else
                                                if (split[17].contains("6")){ bug = 1; } else
                                                    if (split[17].contains("7")){ invertebrate = 1; }
                            
                            double [] kaki = new double[4];
                            if (Integer.parseInt(split[13]) == 0 ) { kaki[0] = 1; } else
                                if (Integer.parseInt(split[13]) == 2 ) { kaki[1] = 1; } else
                                    if (Integer.parseInt(split[13]) == 4 ) { kaki[2] = 1; } else
                                        if (Integer.parseInt(split[13]) > 4 ) { kaki[3] = 1; }
                            
                            
                            newRow = new DataSetRow (
                                    new double[]
                                    {
                                        Double.parseDouble(split[1]),
                                        Double.parseDouble(split[2]),
                                        Double.parseDouble(split[3]),
                                        Double.parseDouble(split[4]),
                                        Double.parseDouble(split[5]),
                                        Double.parseDouble(split[6]),
                                        Double.parseDouble(split[7]),
                                        Double.parseDouble(split[8]),
                                        Double.parseDouble(split[9]),
                                        Double.parseDouble(split[10]),
                                        Double.parseDouble(split[11]),
                                        Double.parseDouble(split[12]),
                                        kaki[0], kaki[1], kaki[2], kaki[3],
                                        Double.parseDouble(split[14]),
                                        Double.parseDouble(split[15]),
                                        Double.parseDouble(split[16])
                                    },
                                    new double[]
                                    {
                                        mammal,
                                        bird,
                                        reptile,
                                        fish,
                                        amphibia,
                                        bug,
                                        invertebrate
                                    });
                            value = newRow.toString();
                            bw.write(value);
                            bw.newLine();
                        }
                        
                        
                        if (newRow != null) {
                            dataSet.addRow(newRow);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException | VectorSizeMismatchException e) {
            System.out.println(e);
        }
        System.out.println("Jumlah Dataset\t: " + dataSet.size());
        if (dataSet.isEmpty()) {
            System.exit(0);
        }
    }

    private static void setColumnNames(DataSet ds) {
        ds.setColumnNames(
            new String [] {
                "hair","feathers",
                "eggs","milk",
                "airborne","aquatic",
                "predator","toothed",
                "backbone","breathes","venomous","fins",
                "legs_0","legs_2","legs_4","legs_more",
                "tail","domestic","catsize"
            }
        );
        ds.setLabel("class_type");
    }
    
    private static void writeFile (String neuralNet) {
        try {
            FileOutputStream fileOS = new FileOutputStream("zoo.data");
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

            objectOS.writeObject(neuralNet);
            objectOS.close();
            fileOS.close();

        // These catch clauses are automatically generated
        // You can do anything you think is appropriate with these
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private static void writeFile (MultiLayerPerceptron neuralNet, MaxMinNormalizerFieldsAccessible mmn) {
        try {
            FileOutputStream fileOS = new FileOutputStream("zoo.model");
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

            objectOS.writeObject(neuralNet);
            objectOS.close();
            fileOS.close();

        // These catch clauses are automatically generated
        // You can do anything you think is appropriate with these
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        try {
            FileOutputStream   fileOS   = new FileOutputStream("zoo.normalize");
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

            objectOS.writeObject(new MaxMinParameters(mmn));
            objectOS.close();
            fileOS.close();

        // These catch clauses are automatically generated
        // You can do anything you think is appropriate with these
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}
