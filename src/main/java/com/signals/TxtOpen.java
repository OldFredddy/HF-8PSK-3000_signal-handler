package com.signals;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TxtOpen {
 private String path_to_file;
 public TxtOpen(String path) {
  this.path_to_file = path;
 }
 public  byte[] ReadAll() {
  Path path = Paths.get(path_to_file);
  try {
   return Files.readAllBytes(path);
  } catch (IOException e) {
   e.printStackTrace();
   return null;
  }
 }
 public  List<Character> bytesToBits(byte[] bytes) {
  List<Character> bits = new ArrayList<>();
  for (byte b : bytes) {
   for (int i = 7; i >= 0; i--) {
    bits.add((b & (1 << i)) == 0 ? '0' : '1');
   }
  }
  return bits;
 }
 public List<Character> ReadTxt() throws IOException {
  BufferedReader varRead = null;
  List<Character> chars = new ArrayList<>();
  try {
   FileReader x = new FileReader(path_to_file);
   varRead = new BufferedReader(x);
   int c;char temp;
   while ((c = varRead.read()) != -1) {
      temp=((char)c);
    if ((temp=='0')|(temp=='1')|(temp=='2')|(temp=='3')|(temp=='4')|(temp=='5')|(temp=='6')|(temp=='7')|(temp=='8')|(temp=='9')){
     chars.add((char) c);
    }
   }
   varRead.close();
  } catch (IOException e) {
      e.printStackTrace();
  } finally {
   if (varRead != null) {
    try {
     varRead.close();
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
  }
  return (chars);
 }
 public void saveResult(List<String> decodeArr, String path, boolean isBinary) throws IOException {
  Path filePath = Paths.get(path);

  if (isBinary) {
   // Для бинарных файлов мы объединяем каждые 8 бит (8 ячеек String в нашем List) в один байт
   OutputStream os = new BufferedOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
   for (int i = 0; i < decodeArr.size(); i += 8) {
    int byteValue = 0;
    for (int j = 0; j < 8; j++) {
     byteValue = (byteValue << 1) | Integer.parseInt(decodeArr.get(i + j));
    }
    os.write(byteValue);
   }
   os.close();
  } else {
   // Для текстовых файлов мы записываем 64 ячейки массива на одной строке в записываемом txt, а только потом \n
   BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
   for (int i = 0; i < decodeArr.size(); i += 64) {
    for (int j = 0; j < 64 && i + j < decodeArr.size(); j++) {
     writer.write(decodeArr.get(i + j));
    }
    writer.newLine();
   }
   writer.close();
  }
 }
}
