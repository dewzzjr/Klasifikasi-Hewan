/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svm;

import java.io.IOException;
import java.util.TreeMap;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

/**
 *
 * @author ekico
 */
public class SVMApps {
    public int predict(TreeMap<Integer, Double> input, String modelFile) {
        int predictedClass = 0;

        try {
            //we read the model
            svm_model model = svm.svm_load_model(modelFile);

            //we format the data for processing by teh  libsvm
            svm_node[] x = new svm_node[input.size()];

            int i = 0;
            for (Integer attributeIndex : input.keySet()) {
                x[i] = new svm_node();
                x[i].index = attributeIndex;
                x[i].value = input.get(attributeIndex);
                i++;
            }

            //the result of the prediction
            predictedClass = (int) svm.svm_predict(model, x);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return predictedClass;
    }

    public String ubahHasil(int predictedClass) {

        if (predictedClass == 1) {
            return "Kanker anda termasuk kanker jinak";
        } else if (predictedClass == 2) {
            return "Kanker anda termasuk kanker ganas";
        } else {
            return "Belom Teridentifikasi";

        }
    }

    public String klasifikasiKanker(Double param1, Double param2, Double param3, Double param4) {

        TreeMap<Integer, Double> indexValue = new TreeMap<>();

        //misalkan kita memiliki data bunga iris sebagai berikut
        indexValue.put(1, param1); //=(L14-10,59)*(1-(-1))/(10,59-21,18)+(-1)
        indexValue.put(2, param2);
        indexValue.put(3, param3);
        indexValue.put(4, param4);
        

        System.out.println(indexValue);

        String modelFile = "oribreastranlinear.model";

        //SVMApps = new SVMApps();
        int predictedClass = predict(indexValue, modelFile);
        return ubahHasil(predictedClass);
    }
}
