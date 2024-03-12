package com.signals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignalProcessing {
    private final static String bw = "777754311202722013475377431011521600476223605176161766617304712233677173150334525253172157612535362075660142541170066756374026364510045547151567335222723304141360726150141170740245300312646526007353406272337671006731550234277452161047161240365454461251362726747301505345073270327061677142677427273763265663664106264155433463524117537165466234233741445461346174135265542151612714423473";
    private final static String lrp = "020466524766450067205726311631042113677365636464217212160707403754553506622232102470417672001750212704226257426267656637247352530012131164170060330332363000347215461454475266707342472155051536674712632646666646160033640013143154671360250346640347115775264471306123456463417763171224315475722774761214461700550005534264422623410172021532556442556504160054714636071643755041751715712061755166736657473222540730300713273615554171104124636336400745410254474220203734606176327757214661035232342727605431077155663047472650635042671554122100763710654524225514663567326524650156374105762663723303143510213645312707174007042023362201172126347155651177003570753572047224751021504155543511262355740560502737201646540330254502656047367574657622067206351463604360746225426151200120710435005076230002120225762467557320050073261221642776555415221336400105322332034657006640134762512056551453306266443101352161645202416011145515066777750325526143616275506350255772426773262131706156415726715227772420016707217321730353701110177520356547665072774342050734201206624162120742420436066223115160111040214576272355250451015333670215423741264104273126503234674335465464432276651140757024065232404727104350376117277550003211510452741742713437607352671542276601136256326404357617140337407005732714222455354402751774535261207767406673001307333442630776565473027673016220166653252341573710311266773062061304370641027062720040237325567770741212555417116343316721317717467610077251667543363641113363234155535623217056500527560175062303367737472614441272231261422337";
    public static List<String> decode8PSK_3000(List<Character> sig, String mode) throws URISyntaxException, IOException {
        List<String> finalResults = new ArrayList<>(); // Контейнер для итоговых результатов

        int start = 0;
        String jarPath = GUIStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile1 = new File(jarPath);
        File pathToDecodeWalshTable = new File(jarFile1.getParent() + "/WalsheDecodeTable2.xls");
        List<String> decodeTableWalsh = ExcelRead.readFromExcel(pathToDecodeWalshTable.getAbsolutePath(), 0);
        List<String> decodeTableBin = ExcelRead.readFromExcel(pathToDecodeWalshTable.getAbsolutePath(), 1);

        while (start < sig.size()) {
            int startData = SequenceFinder.findSequence(sig.subList(start, sig.size()), bw);
            DeInterleaveEngine deInterleaveEngine = new DeInterleaveEngine();
            if (startData == -1) {
                break;
            }
            startData += start;
            int endData = SequenceFinder.findSequence(sig.subList(startData + bw.length(), sig.size()), bw);
            if (endData == -1) {
                endData = sig.size();
            } else {
                endData += startData + bw.length();
            }
            List<Character> dataBlock = sig.subList(startData + bw.length(), endData);
            dataBlock = descramble(dataBlock, lrp);
            dataBlock = WalsheDecoder.fastDecode(dataBlock, decodeTableWalsh, decodeTableBin, 30, "16", "4"); //16- разрядность, 4 - количество повторов
            List<String> dataBlockListString = deInterleaveEngine.runEngine(dataBlock.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()), 4, 24, "Custom_decode", "FromLeftToRight", "FromUpToDown");

            finalResults.addAll(dataBlockListString); // Собираем результаты после каждой итерации

            start = endData + bw.length();
        }

        return finalResults; // Возвращаем итоговые собранные данные
    }

    public static List<Character> descramble(List<Character> dataBlock, String lrpp){
        List<Character> lrppChar = lrpp.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        for (int i = 0; i < dataBlock.size(); i++) {
            int dataDigit = Character.digit(dataBlock.get(i), 8);
            int lrppDigit = Character.digit(lrppChar.get(i % lrppChar.size()), 8);
            int scrambledDigit = (dataDigit + lrppDigit) % 8;
            dataBlock.set(i, Character.forDigit(scrambledDigit, 8));
        }
        return dataBlock;
    }
}
