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
import java.util.stream.Stream;

/**
 *
 * @author Valliant F
 */
public class Tikum2 {

      public static void write(String inputFile, String outputFile) 
    {
        try (Stream<String> stream = Files.lines(Paths.get(inputFile));
                BufferedWriter bw= Files.newBufferedWriter(Paths.get(outputFile), StandardCharsets.UTF_8)){
                
            Object[] lines = stream.toArray();
            for (Object line : lines) {
                if (line instanceof String && (((String)line).trim().length()>0)){
                    String[] data = ((String)line).split(",");
                    if (data.length==11){
                        String dataLine = "";
                        if(data[10].contains("VeryBad")){
                            dataLine +="1";
                        }
                        else if (data[10].contains("Bad")){
                            dataLine +="2";
                        }
                        else if (data[10].contains("Normal")){
                            dataLine +="3";
                        }
                        else if (data[10].contains("Good")){
                            dataLine +="4";
                        }
                        else if (data[10].contains("VeryGood")){
                            dataLine +="5";
                        }
                        //Attribute 1
                        if (data[0].equalsIgnoreCase("1")){
                            dataLine+=" 1:1";
                        }
                        else if (data[0].equalsIgnoreCase("2")){
                            dataLine+=" 2:1";
                        }
                        //Attribute 2
                        double attributeValue=Double.parseDouble(data[1]);
                        dataLine+=" 3:"+attributeValue;
                        //Attribute 3
                         if (data[2].equalsIgnoreCase("1")){
                            dataLine+=" 4:1";
                        }
                        else if (data[2].equalsIgnoreCase("2")){
                            dataLine+=" 5:1";
                        }
                        else if (data[2].equalsIgnoreCase("3")){
                            dataLine+=" 6:1";
                        } else if (data[2].equalsIgnoreCase("4")){
                            dataLine+=" 7:1";
                        }
                         //Attribute 4
                         if (data[3].equalsIgnoreCase("0")){
                            dataLine+=" 8:1";
                        }else if (data[3].equalsIgnoreCase("1")){
                            dataLine+=" 9:1";
                        }
                         //attribute 5
                         if (data[4].equalsIgnoreCase("0")){
                            dataLine+=" 10:1";
                        }else if (data[4].equalsIgnoreCase("1")){
                            dataLine+=" 11:1";
                        }
                         //attribute 6
                         if (data[5].equalsIgnoreCase("0")){
                            dataLine+=" 12:1";
                        }else if (data[5].equalsIgnoreCase("1")){
                            dataLine+=" 13:1";
                        }
                         //attribute 7
                         if (data[6].equalsIgnoreCase("1")){
                            dataLine+=" 14:1";
                        }else if (data[6].equalsIgnoreCase("2")){
                            dataLine+=" 15:1";
                        }else if (data[6].equalsIgnoreCase("3")){
                            dataLine+=" 16:1";
                        }else if (data[6].equalsIgnoreCase("4")){
                            dataLine+=" 17:1";
                        }else if (data[6].equalsIgnoreCase("5")){
                            dataLine+=" 18:1";
                        }
                         
                         //attribute 8
                         if (data[7].equalsIgnoreCase("1")){
                            dataLine+=" 19:1";
                        }else if (data[7].equalsIgnoreCase("2")){
                            dataLine+=" 20:1";
                        }
                         else if (data[7].equalsIgnoreCase("3")){
                            dataLine+=" 21:1";
                        }
                         else if (data[7].equalsIgnoreCase("4")){
                            dataLine+=" 22:1";
                        }
                         else if (data[7].equalsIgnoreCase("5")){
                            dataLine+=" 23:1";
                        }
                         //attribute 9
                         if (data[8].equalsIgnoreCase("1")){
                            dataLine+=" 24:1";
                        }else if (data[8].equalsIgnoreCase("2")){
                            dataLine+=" 25:1";
                        }
                         else if (data[8].equalsIgnoreCase("3")){
                            dataLine+=" 26:1";
                        }
                         else if (data[8].equalsIgnoreCase("4")){
                            dataLine+=" 27:1";
                        }
                         else if (data[8].equalsIgnoreCase("5")){
                            dataLine+=" 28:1";
                        }
                         
                         //Attribute 10
                        double attributeValue2=Double.parseDouble(data[9]);
                        dataLine+=" 29:"+attributeValue;
                        
                       /* for (int i=0; i<4; i++){
                        
                            double attributeValue=Double.parseDouble(data[i]);
                            if(attributeValue!=0){
                                dataLine+=" "+(i+1)+":"+attributeValue;
                            }
                        }*/
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
        write("healthfix.data","healthfixsvm.out");
    }
}
