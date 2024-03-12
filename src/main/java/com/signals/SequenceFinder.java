package com.signals;

import java.util.ArrayList;
import java.util.List;

public class SequenceFinder {

    public static int findSequence(List<Character> sig, String sequence) {
        int sequenceLength = sequence.length();
        int maxErrors = (int) Math.ceil(sequenceLength * 0.1); // 10% ошибок от длины последовательности

        // Генерируем все довернутые последовательности
        List<String> rotatedSequences = rotatePhases(sequence);

        for (String rotatedSequence : rotatedSequences) {
            char[] sequenceArray = rotatedSequence.toCharArray();

            for (int i = 0; i <= sig.size() - sequenceLength; i++) {
                int errors = 0;

                for (int j = 0; j < sequenceLength; j++) {
                    if (sig.get(i + j) != sequenceArray[j]) {
                        errors++;
                        if (errors > maxErrors) {
                            break; // Превышено максимально допустимое количество ошибок
                        }
                    }
                }

                if (errors <= maxErrors) {
                    return i + sequenceLength; // Индекс следующего символа после последовательности
                }
            }
        }

        return -1; // Последовательность не найдена
    }

    private static List<String> rotatePhases(String sequence) {
        List<String> result = new ArrayList<>();
        for (int phaseShift = 0; phaseShift < 8; phaseShift++) {
            StringBuilder sb = new StringBuilder();
            for (char ch : sequence.toCharArray()) {
                int digit = Character.digit(ch, 8);
                if (digit == -1) continue;
                int rotatedDigit = (digit + phaseShift) % 8;
                sb.append(rotatedDigit);
            }
            result.add(sb.toString());
        }
        return result;
    }

    private static List<Character> convertStringToList(String str) {
        return str.chars().mapToObj(e -> (char) e).toList();
    }

}
