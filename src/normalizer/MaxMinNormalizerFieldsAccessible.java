package normalizer;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.Normalizer;

public class MaxMinNormalizerFieldsAccessible implements Normalizer {
	
    double[] maxIn, maxOut; // contains max values for in and out columns
    double[] minIn, minOut; // contains min values for in and out columns     


    @Override
    public void normalize(DataSet dataSet) {
        // find min i max vectors
        findMaxAndMinVectors(dataSet);
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeMaxMin(row.getInput(), minIn, maxIn);
            row.setInput(normalizedInput);
            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeMaxMin(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(normalizedOutput);
            }
        }
    }
    
    // This is not an override of the normalize method of the Normalizer interface,
    // but we want to create a method such that we could normalize using minIn and maxIn values of the training set
    public void normalize(DataSet dataSet, double[] minIn, double[] maxIn) {
        // find min i max vectors
        findMaxAndMinVectors(dataSet);
       
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeMaxMin(row.getInput(), minIn, maxIn);
            row.setInput(normalizedInput);
            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeMaxMin(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(normalizedOutput);
            }
        }
    }
    
    private void findMaxAndMinVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();
        
        maxIn = new double[inputSize];
        minIn = new double[inputSize];
        
        for(int i=0; i<inputSize; i++) {
            maxIn[i] = Double.MIN_VALUE;
            minIn[i] = Double.MAX_VALUE;
        }
        
        maxOut = new double[outputSize];
        minOut = new double[outputSize];   
        
        for(int i=0; i<outputSize; i++) {
            maxOut[i] = Double.MIN_VALUE;
            minOut[i] = Double.MAX_VALUE;
        }        

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i];
                }
                if (input[i] < minIn[i]) {
                    minIn[i] = input[i];
                }
            }
            
            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < outputSize; i++) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i];
                }
                if (output[i] < minOut[i]) {
                    minOut[i] = output[i];
                }
            }            
                                    
        }        
    }     
  
    
    private double[] normalizeMaxMin(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i]);
        }

        return normalizedVector;             
    }    
    
	
	public double[] getMaxIn() {
		return maxIn;
	}
	
	public double[] getMaxOut() {
		return maxOut;
	}
	
	public double[] getMinIn() {
		return minIn;
	}
	
	public double[] getMinOut() {
		return minOut;
	}

}
