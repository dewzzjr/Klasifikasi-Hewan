/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikum2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Valliant F
 */
public class WriteLibSVM {
    
    public static void write(String inputFile, String outputFile) 
    {
        try (Stream<String> stream = Files.lines(Paths.get(inputFile));
                BufferedWriter bw= Files.newBufferedWriter(Paths.get(outputFile), StandardCharsets.UTF_8)){
                
            Object[] lines = stream.toArray();
            for (Object line : lines) {
                if (line instanceof String && (((String)line).trim().length()>0)){
                    String[] data = ((String)line).split(",");
                    if (data.length==5){
                        String dataLine = "";
                        if(data[4].contains("setosa")){
                            dataLine +="1";
                        }
                        else if (data[4].contains("versicolor")){
                            dataLine +="2";
                        }
                        else if (data[4].contains("virginica")){
                            dataLine +="3";
                        }
                        for (int i=0; i<4; i++){
                        
                            double attributeValue=Double.parseDouble(data[i]);
                            if(attributeValue!=0){
                                dataLine+=" "+(i+1)+":"+attributeValue;
                            }
                        }
                        System.out.println(dataLine);
                        bw.write(dataLine);
                        bw.newLine();                                                
                    }
                }
                
            }
        } catch (IOException e){
            e.printStackTrace();
        }
                
    
    }
    public static void main(String[] args) {

        write("iris.data","iris_libsvm.out");
}
}
