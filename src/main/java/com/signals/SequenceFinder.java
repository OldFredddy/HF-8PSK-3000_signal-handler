package com.signals;

import java.util.List;

public class SequenceFinder {
    public static int findSequence(List<Character> sig, String sequence) {
        int sequenceLength = sequence.length();
        int maxErrors = (int) Math.ceil(sequenceLength * 0.1); // 10% ошибок от длины последовательности

        // Преобразуем строку в массив символов для удобства сравнения
        char[] sequenceArray = sequence.toCharArray();

        // Итерируем по List<Character>
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

            // Если найдена последовательность с допустимым числом ошибок, возвращаем индекс
            if (errors <= maxErrors) {
                return i + sequenceLength; // Индекс следующего символа после последовательности
            }
        }

        return -1; // Последовательность не найдена
    }



    private static List<Character> convertStringToList(String str) {
        return str.chars().mapToObj(e -> (char) e).toList();
    }
}
