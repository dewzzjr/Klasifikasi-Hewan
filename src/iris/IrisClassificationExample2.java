package iris;

import normalizer.MaxMinNormalizerFieldsAccessible;
import normalizer.MaxMinParameters;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;


public class IrisClassificationExample2 {

	public static void main(String[] args) {
		
		/*
		 * 1. CREATE THE DATASET
		 */
		int numInput  = 4;
		int numOutput = 3;
		
		DataSet dataSet = new DataSet(numInput, numOutput);
		
		// Let's set the names of the variables and target output
		// This step is not obligatory; you're free to omit it
		dataSet.setColumnNames(new String[] { "sepal_length", "sepal_width", "petal_length", "petal_width" });
		dataSet.setLabel("species");
		
		// A Java 8 idiom for reading data
		// You could also use BufferedReader
		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get("iris.data"))) {
			
			Object[] lines = stream.toArray();
			for (Object line : lines) {
				if (line instanceof String) {
					
					String[] split = ((String)line).split(",");
			    	double setosa = 0, versicolor = 0, virginica = 0;
			    	
			    	DataSetRow newRow = null;
			    	
			    	if (split.length == 5) {
			    		if (split[4].contains("setosa")) {
			    			setosa = 1;
			    		} else if (split[4].contains("versicolor")) {
			    			versicolor = 1;
			    		} else if (split[4].contains("virginica")) {
			    			virginica = 1;
			    		}
			    		
			    		newRow = new DataSetRow(
                                                new double[] {	
                                                    Double.parseDouble(split[0]),
                                                    Double.parseDouble(split[1]),
                                                    Double.parseDouble(split[2]),
                                                    Double.parseDouble(split[3])
                                                },
                                                new double[] {	
                                                    setosa, 
                                                    versicolor, 
                                                    virginica
                                                });
			    	}
			    	
			    	if (newRow != null) {
			    		dataSet.addRow(newRow);
			    	}	
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* 
		 * 2. SPLIT THE DATA SET INTO TRAINING AND TEST SETS
		 */
		
		// Let's first check that we actually have some data in the DataSet object
		System.out.println(dataSet.size());
		
		// Split the data
		int trainSetPercent = 20;
		int testSetPercent  = 80;
		
		DataSet[] training_testing = dataSet.createTrainingAndTestSubsets(trainSetPercent, testSetPercent);
		
		// Set the column names
		// Again, this is not obligatory
		for (DataSet ds : training_testing) {
                    setColumnNames(ds);
		}
		
		// Let's check the resulting DataSets
		printTrainingTestingSets(training_testing);
		
		// Let's make a reference variable for each the training and testing data set
		// to make it easier for us to refer to each later
		DataSet trainSet = training_testing[0];
		DataSet testSet  = training_testing[1];
		
		/*
		 * 3. CREATING THE MODEL USING THE !TRAINING! SET
		 */
		// Next we create the classification model based on the training set
		// In this example we use MultiLayerPerceptron just as in Example #1,
		// with BackPropagation learning rule.
		// Please remember that you could, and should, tweak the structure of the neural network
		// and also the parameters of the learning rule to get the best results
		// - this is what the experiments are all about.
		
		// First we create the structure of the neural network
		// Let's say we want to have a 4-2-3 multilayer neural network
		int inputCount    = 4;
		int outputCount   = 3;
		int hiddenNeurons = 2;
		
		MultiLayerPerceptron neuralNet =
                        new MultiLayerPerceptron(inputCount, hiddenNeurons, outputCount);
		
		// Then we set BackPropagation as the learning rule, and also its parameters
		BackPropagation learningRule = neuralNet.getLearningRule();
		
		// Set some parameters
		learningRule.setLearningRate(0.5);
		learningRule.setMaxError(0.01);
		learningRule.setMaxIterations(10000);
		
		// We also set a Listener for learning events
		// In this case, we will define the adding of Listener in a separate method
		// only to prevent code cluttering
		// thus here we're just calling that
		addLearningEventListener(learningRule);
		
		// Then we DO THE TRAINING!
		// Don't forget to only use the training set
		
		// To enhance the results, we could first NORMALIZE the data
		MaxMinNormalizerFieldsAccessible mmn = new MaxMinNormalizerFieldsAccessible();
		mmn.normalize(trainSet);
		
		// If we do normalize, we need also to keep some parameters used in the normalization process,
		// in this case the maximum and minimum values of the data in the training set
		// We will directly access some parameters in the normalizing object that we used
		double[] maxIn = mmn.getMaxIn();
		double[] minIn = mmn.getMinIn();	

		// This is the actual training
		neuralNet.learn(trainSet);
		
		// Done! The neuralNet object now holds the weight configuration learned from the training data
		
		// OBSERVE THE WEIGHTS
		// Same as in IrisClassificationExample
		
		/*
		 * 4. USING THE TRAINED MODEL ON THE TESTING SET
		 */
		// To make this code less messy, the code for the prediction is written in a separate function
		// But before that, it is IMPORTANT to do similar preprocessing to the test data
		// as with the preprocessing used in the training process
		// In this case, the only thing we did was normalizing the train data,
		// so let's do that also for the test data
		// In this case, we've already had the normalizing object, so we could use just that
		// But we need to remember to use THE SAME normalizing parameters as with the training
		mmn.normalize(testSet, minIn, maxIn);
		
		// Now let's do the actual prediction test
		testAccuracy(neuralNet, testSet);
		
		/*
		 * 5. SERIALIZING THE MODEL FOR USE ELSEWHERE
		 */
		
		// Now that we've had the model,
		// we want to serialize it to be able to use it without retraining the model
		try {
                    FileOutputStream   fileOS   = new FileOutputStream("ser_TrainedModel.ser");
                    ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

                    objectOS.writeObject(neuralNet);
                    objectOS.close();
                    fileOS.close();
		
		// These catch clauses are automatically generated
		// You can do anything you think is appropriate with these
		} catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		} catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		}
		
		// We also want to serialize the parameters used to normalize the training data
		// You could totally write the code below in the try-catch above,
		// they are separated here only for the sake of clarity
		try {
                    FileOutputStream   fileOS   = new FileOutputStream("ser_NormalizingParameters.ser");
                    ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);

                    objectOS.writeObject(new MaxMinParameters(mmn));
                    objectOS.close();
                    fileOS.close();
		
		// These catch clauses are automatically generated
		// You can do anything you think is appropriate with these
		} catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		} catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		}
		

	}
	
	public static void addLearningEventListener(LearningRule learningRule) {
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
	
	public static void printTrainingTestingSets(DataSet[] training_testing) {
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    System.out.println("\nPrinting the training set....");
                } else {
                    System.out.println("\nPrinting the test set....");
                }
                System.out.println(training_testing[i]);
            }
	}
	
	public static void setColumnNames(DataSet dataSet) {
		// Let's set the names of the variables and target output
		// This step is not obligatory; you're free to omit it
		dataSet.setColumnNames(
                        new String[] { 
                            "sepal_length", 
                            "sepal_width", 
                            "petal_length", 
                            "petal_width" 
                        });
		dataSet.setLabel("species");
	}
	
	public static void testAccuracy(MultiLayerPerceptron neuralNet, DataSet testSet) {
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
