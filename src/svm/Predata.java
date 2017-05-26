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
 * @author Dewangga
 */
public class Predata {
    public static void main(String[] args) {
        try  (
                Stream<String> stream = Files.lines(Paths.get("zoo.csv"));
                BufferedWriter bw = Files.newBufferedWriter(Paths.get("zoo.svm"), StandardCharsets.UTF_8)) {
                Object[] lines = stream.toArray();
                
                for (Object line : lines) {

                    if (line instanceof String) {
                        String value = "";
                        String format;
                        double input;
                        
                        String[] split = ((String)line).split(";");
                        value += split[17] + " ";

                        for (int i = 1; i < 13; i++) {
                            //format = Integer.toString(i);
                            input = Double.parseDouble(split[i]);
                            if (input != 0) {
                                format = Integer.toString(i) + ":" + input + " ";
                            } else {
                                format = "";
                            }
                            // format = Integer.toString(13 + i) + ":" + Double.parseDouble(split[i]) + " ";
                            value += format;
                        }
                        double [] kaki = new double[4];
                            if (Integer.parseInt(split[13]) == 0 ) { kaki[0] = 1; } else
                            if (Integer.parseInt(split[13]) == 2 ) { kaki[1] = 1; } else
                            if (Integer.parseInt(split[13]) == 4 ) { kaki[2] = 1; } else
                            if (Integer.parseInt(split[13]) > 4 ) { kaki[3] = 1; } 
                        for (int i = 0; i < kaki.length; i++) {
                            //format = Integer.toString(13 + i);
                            input = kaki[i];
                            if (input != 0) {
                                format = Integer.toString(13 + i) + ":" + input + " ";
                            } else {
                                format = "";
                            }
                            // format = Integer.toString(13 + i) + ":" + kaki[i] + " ";
                            value += format;
                        }
                        for (int i = 14; i < 17; i++) {
                            //format = Integer.toString(i + 3);
                            input = Double.parseDouble(split[i]);
                            if (input != 0) {
                                format = Integer.toString(3 + i) + ":" + input + " ";
                            } else {
                                format = "";
                            }
                            // format = Integer.toString(3 + i) + ":" + Double.parseDouble(split[i]) + " ";
                            value += format;
                        }
                        System.out.println(value);
                        bw.write(value);
                        bw.newLine();
                    }
                }
                
        } catch (IOException ex) {
            Logger.getLogger(Predata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
