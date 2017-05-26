/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author ekico
 */
public class random {
    public static void main(String[] args) throws IOException {
//        write("oribreast.data", "oribreast.data.svm");
        randomize(90, "zoo.svm", "zoo_train90.svm", "zoo_test10.svm");
    }
    public static void randomize(double percentageInTrain, String inputFile,
            String outTrain, String outTest) {
        try (Stream<String> stream = Files.lines(Paths.get(inputFile));
                BufferedWriter btrain = Files.newBufferedWriter(Paths.get(outTrain),
                        StandardCharsets.UTF_8);
                BufferedWriter btest = Files.newBufferedWriter(Paths.get(outTest),
                        StandardCharsets.UTF_8)) {

            int ntotal = 0;
            int ntrain = 0;
            int ntest = 0;

            Object[] lines = stream.toArray();
            for (Object line : lines) {
                if (line instanceof String && (((String) line).trim().length() > 0)) {
                    String data = (String) line;
                    ntotal++;

                    if (Math.random() < percentageInTrain / 100) {
                        btrain.write(data);
                        btrain.newLine();
                        ntrain++;

                    } else {
                        btest.write(data);
                        btest.newLine();
                        ntest++;
                    }
                }
            }
            System.out.println("The total number of instances in the data   : " + ntotal);
            System.out.println("The number of data in the training set      : " + ntrain);
            System.out.println("The number of data in the test set          : " + ntest);
        } catch (IOException ex) {
            Logger.getLogger(random.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

