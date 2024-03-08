package com.signals;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class DeInterleaveEngine {
    private List<String> inputSignalp;

    /**
     * @param inputSignal cигнал на вход в List<String>
     * @param a количество строк перемежителя
     * @param b количество столбцов перемежителя
     * @param method необходимый метод (Custom_code, Custom_decode, 110A_code, 110A_decode, 110A_75N_decode, 110A_75N_code)
     * @param leftRight выбор направления линейного перемежителя (leftRight)
     * @param leftRight выбор направления линейного перемежителя (upDown)
     * @return результат деперемежения в List<String>
     */
    public List<String> runEngine(List<String> inputSignal, int a, int b, String method, String leftRight,String upDown ){
        switch (method){
            case "Custom_code":{
                inputSignalp=codeLinearCustom(inputSignal,a,b,leftRight,upDown);
                break;
            }
            case "110A_code":{
                inputSignalp=codeMil(inputSignal,a,b);
                break;
            }
            case "110A_75N_code":{
                inputSignalp=codeMil75N(inputSignal,a,b);
                break;
            }
            case "Custom_decode":{
                inputSignalp=decodeLinearCustom(inputSignal,a,b,leftRight,upDown);

                break;
            }
            case "110A_decode":{
                inputSignalp=decodeMil(inputSignal,a,b);
                break;
            }
            case "110A_75N_decode":{
                inputSignalp=decodeMil75N(inputSignal,a,b);
                break;
            }
        }
        return inputSignalp;
    }
    private List<String> codeMil(List<String> inputSignal, int a, int b){
        int i=0;
        int k;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){
            k=-9;
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k= mod(k,9,a);
                    try {
                        interleaverArr[k][m]=inputSignal.get(i);
                    } catch (IndexOutOfBoundsException e){
                        interleaverArr[k][m]="0";
                    }
                    i += 1;
                }
                k=-9;
            }
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k = mod((-(17 * l)) + m, b, b);
                    outputWriteSignal.add(interleaverArr[l][k]);
                }
            }
        }
        return outputWriteSignal;
    }
    private List<String> decodeMil(List<String> inputSignal, int a, int b){
        int i=0;
        int k=0;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){
                for (int m = 0; m < b; m++) {
                    for (int l = 0; l < a; l++) {
                        k = mod((-(17 * l)) + m, b, b);
                        try {
                            interleaverArr[l][k]=inputSignal.get(i);
                        } catch (IndexOutOfBoundsException e){
                            interleaverArr[l][k]="0";
                        }
                        i += 1;
                    }
                }
            k=-9;
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k= mod(k,9,a);
                    outputWriteSignal.add(interleaverArr[k][m]);
                }
                k=-9;
            }
        }
        return outputWriteSignal;
    }
    private List<String> codeMil75N(List<String> inputSignal, int a, int b){
        int i=0;
        int k=0;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){  // запись
            k=-7;
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k= mod(k,7,a);
                    try {
                        interleaverArr[k][m]=inputSignal.get(i);
                    } catch (IndexOutOfBoundsException e){
                        interleaverArr[k][m]="0";
                    }
                    i += 1;
                }
                k=-7;
            }
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k = mod((-(7 * l)) + m, b, b);
                    outputWriteSignal.add(interleaverArr[l][k]);
                }
            }
        }
        return outputWriteSignal;
    }
    private List<String> decodeMil75N(List<String> inputSignal, int a, int b){
        int i=0;
        int k=0;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k = mod((-(7 * l)) + m, b, b);
                    try {
                        interleaverArr[l][k]=inputSignal.get(i);
                    } catch (IndexOutOfBoundsException e){
                        interleaverArr[l][k]="0";
                    }
                    i += 1;
                }
            }
            k=-7;
            for (int m = 0; m < b; m++) {
                for (int l = 0; l < a; l++) {
                    k= mod(k,7,a);
                    outputWriteSignal.add(interleaverArr[k][m]);
                }
                k=-7;
            }
        }
        return outputWriteSignal;
    }
    private List<String> codeLinearCustom(List<String> inputSignal, int a, int b, String leftRight,String upDown ){
        int i=0;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){
            if (upDown=="FromUpToDown") {
                for (int m = 0; m < b; m++) {
                    for (int l = 0; l < a; l++) {
                        try {
                            interleaverArr[l][m] = inputSignal.get(i);
                        } catch (IndexOutOfBoundsException e) {
                            interleaverArr[l][m] = "0";
                        }
                        i+=1;
                    }
                }
            } else {
                for (int m = 0; m < b; m++) {
                    for (int l = a-1; l > -1; l--) {
                        try {
                            interleaverArr[l][m] = inputSignal.get(i);
                        } catch (IndexOutOfBoundsException e) {
                            interleaverArr[m][l] = "0";
                        }
                        i+=1;
                    }
                }
            }
            if (leftRight=="FromLeftToRight") {
                for (int m = 0; m < a; m++) {
                    for (int l = 0; l < b; l++) {
                        outputWriteSignal.add(interleaverArr[m][l]);
                    }
                }
            } else {
                for (int m = 0; m < a; m++) {
                    for (int l = b-1; l > -1; l--) {
                        outputWriteSignal.add(interleaverArr[m][l]);
                    }
                }
            }
        }
        return outputWriteSignal;
    }
    private List<String> decodeLinearCustom(List<String> inputSignal, int a, int b, String leftRight,String upDown){
        int i=0;
        List <String> outputWriteSignal= new ArrayList<>();
        String [][] interleaverArr = new String[a][b];
        while(i<inputSignal.size()){  // Обработка всего входного сигнала
               if (leftRight=="FromLeftToRight") {
                   for (int m = 0; m < a; m++) {
                       for (int l = 0; l < b; l++) {
                           try {
                               interleaverArr[m][l] = inputSignal.get(i);
                           } catch (IndexOutOfBoundsException e) {
                               interleaverArr[m][l] = "0";
                           }
                           i += 1;
                       }
                   }
               } else {
                    for (int m = 0; m < a; m++) {
                       for (int l = b-1; l > -1; l--) {
                           try {
                               interleaverArr[m][l] = inputSignal.get(i);
                           } catch (IndexOutOfBoundsException e) {
                               interleaverArr[m][l] = "0";
                           }
                           i += 1;
                       }
                   }
               }
          if (upDown=="FromUpToDown") {
              for (int m = 0; m < b; m++) {
                  for (int l = 0; l < a; l++) {
                      outputWriteSignal.add(interleaverArr[l][m]);
                  }
              }
          } else {
              for (int m = 0; m < b; m++) {
                  for (int l = a-1; l > -1; l--) {
                      outputWriteSignal.add(interleaverArr[l][m]);
                  }
              }
          }
        }
        return outputWriteSignal;
    }

    private int mod(int a, int b, int module){
        int sum=a+b;
        if (sum>=0){
            return sum%module;
        }
        else {
            return (module-(-sum%module))%module;
        }
    }
}
