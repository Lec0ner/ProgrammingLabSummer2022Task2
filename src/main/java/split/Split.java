package split;

/*
● Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному V
● Порядок названия если флаг есть,то “ofile1, ofile2, ofile3,ofile4 …”, если нет, то “ofileaa, ofileab,
 ofileac, ofilead … ofileaz, ofileba ofilebb … ”   X
● Размер файла в строчках  V
● Размер файла в символах  V
● кол-во выходных файлов  X
● Регулярка ([^\\]+)\.txt
 */

import java.io.*;
import java.nio.file.Path;

public class Split {

    private String outputNameFile;
    private boolean fileNumbering;
    private int countLines;
    private int countSymbols;
    private int countFiles;
    private String inputFileName;
    private int count = 0;
    private int counterFiles;

    public Split(String outputNameFile, boolean fileNumbering, int countLines
            , int countSymbols, int countFiles, String inputFileName) {
        this.fileNumbering = fileNumbering;
        this.countLines = countLines;
        this.countSymbols = countSymbols;
        this.countFiles = countFiles;
        this.inputFileName = inputFileName;
        correctFileName(outputNameFile);
        //createFileFromLine(6);
        //createFileFromSymbol(500);
    }


    public void start() {
        if (countSymbols > 0) createFileFromSymbol(countSymbols);
        else if (countLines > 0) createFileFromLine(countLines);
    }


    //Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному

    private void correctFileName(String outputNameFile) {
        if (outputNameFile.equals("-")) {
            String fileName = String.valueOf(Path.of(inputFileName).getFileName());
            outputNameFile = fileName.substring(0, fileName.lastIndexOf("."));
        }
        this.outputNameFile = outputNameFile;
    }

    // Порядок названия файла

    public String orderFileName() {
        StringBuilder output = new StringBuilder();
        if (fileNumbering) output.append(count + 1);
        else {
            int k = count;
            for (int i = 0; i < (int) (Math.log(counterFiles) / Math.log(26) - 0.000001) + 1; i++) {
                output.append((char) (97 + k % 26));
                k /= 26;
            }
            output.reverse();
        }
        count++;
        return outputNameFile + output + ".txt";
    }

    // Размер файла в строках

    public void createFileFromLine(int countLines) {
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            String line = "";
            while ((line = reader.readLine()) != null) {
                counter++;
            }
            counterFiles = (int) Math.ceil((double) counter / countLines);
            reader.close();
            reader = new BufferedReader(new FileReader(inputFileName));
            for (int i = 0; i < counterFiles; i++) {
                File file = new File(orderFileName());
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < countLines; j++) {
                    line = reader.readLine();
                    if (line == null) break;
                    writer.write(line + "\n");
                }
                writer.close();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Размер файла в символах

    public void createFileFromSymbol(int countSymbols) {
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            char symbol;
            while ((symbol = (char) reader.read()) != (char) -1) {
                counter++;
            }
            counterFiles = (int) Math.ceil((double) counter / countSymbols);
            reader.close();
            reader = new BufferedReader(new FileReader(inputFileName));
            for (int i = 0; i < counterFiles; i++) {
                File file = new File(orderFileName());
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < countSymbols; j++) {
                    symbol = (char) reader.read();
                    if (symbol == (char) -1) break;
                    writer.write(symbol);
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
