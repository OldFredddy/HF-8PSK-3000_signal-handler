package com.signals;
import java.util.ArrayList;
import java.util.List;

public class PhaseRotation {

    public static List<String> rotatePhases(String octalInput) {
        List<String> result = new ArrayList<>();

        // Для каждого возможного доворота (0-7)
        for (int phaseShift = 0; phaseShift < 8; phaseShift++) {
            StringBuilder sb = new StringBuilder();

            // Преобразование каждого символа в строке
            for (char ch : octalInput.toCharArray()) {
                int digit = Character.digit(ch, 8);
                if (digit == -1) {
                    // Если символ не является восьмиричной цифрой, пропустить его
                    continue;
                }

                // Доворот фазы для символа
                int rotatedDigit = (digit + phaseShift) % 8;
                sb.append(rotatedDigit);
            }

            // Добавление результата доворота в список
            result.add(sb.toString());
        }

        return result;
    }
}
