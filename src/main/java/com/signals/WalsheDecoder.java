package com.signals;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

//porog-допустимое количество ошибок
public class WalsheDecoder {
    public static List<String> decArrWT=new ArrayList<>();
    private static int intNumOfSym;
    private static int intNumOfRepeats;
    private static int maxWeight;
    private static int indexOfMaxWeight;
    private static String tempStars = "****************************************************************";
    public static List<Character> fastDecode(List<Character> codeArr,List<String> tempDecodeTableWalsh,List<String> tempDecodeTableBin, int porog, String numOfSym,
                                             String numOfRepeats){
        List<Character> decArr=new ArrayList<>();
        intNumOfSym = Integer.parseInt(numOfSym);
        intNumOfRepeats = Integer.parseInt(numOfRepeats);
        decArrWT.clear();
        char[] tempArr=new char[intNumOfSym];
        try {
            for (int i = 0; i < tempDecodeTableWalsh.size(); i++) {
                if (tempDecodeTableWalsh.get(i).length() != intNumOfSym) {
                    tempDecodeTableWalsh.remove(i);
                    tempDecodeTableBin.remove(i);
                    i = i - 1;
                }
            }
        } catch (Exception e){
            System.out.println("s");
        }
        int x = 0;
        for (int i = 0; i < (int)(codeArr.size()/intNumOfSym/intNumOfRepeats); i++) {
            maxWeight = -1;
            String temp = "";
            String tempDec="";
            for (int j = 0; j < Integer.parseInt(numOfRepeats); j++) {
                for (int b = 0; b < intNumOfSym; b++) {
                    tempArr[b]=codeArr.get(x);
                    x++;
                }
                temp = (tempDecodeTableWalsh.get(GetDecodeIndex(tempArr,tempDecodeTableWalsh,porog,numOfRepeats)));
            }
            if (indexOfMaxWeight!=-1){
                decArrWT.add(tempDecodeTableWalsh.get(indexOfMaxWeight));
                tempDec = tempDecodeTableBin.get(indexOfMaxWeight);
            } else {
                decArrWT.add(tempStars.substring(0,Integer.parseInt(numOfSym)));
                tempDec = tempStars.substring(0,Integer.parseInt(numOfSym));
            }

            for (int j = 0; j < tempDec.length(); j++) { //TODO проверить decArr.size()-1
                decArr.add(tempDec.charAt(j));
            }
        }
        return decArr;
    }

    private static int GetDecodeIndex(char[] arr,List<String> decodeTableWalsh, int porog, String numOfRepeats){

        int index=0;
        int[] weight=new int[decodeTableWalsh.size()];
        for (int i = 0; i < decodeTableWalsh.size(); i++) {
            for (int j = 0; j < intNumOfSym; j++) {
                if (((decodeTableWalsh.get(i)).charAt(j))==arr[j]){
                    weight[i]++;
                }
            }
        }
        index=FindMaxElementIndex(weight,porog);

        return index;
    }
    private static int FindMaxElementIndex(int[] weight,int porog){
        int max=-1;
        int indexMax=-1;
        for (int i = 0; i < weight.length; i++) {
            if(weight[i]>max){
                max=weight[i];
                indexMax=i;
            }
        }
        if (weight[indexMax]>=(intNumOfSym-porog)){
            maxWeight=max;
            indexOfMaxWeight=indexMax;
        } else {
            indexMax=0;
            maxWeight=0;
            indexOfMaxWeight=-1;
        }
        return indexMax;
    }
}
