package split;

/*
● Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному V
● Порядок названия если флаг есть,то “ofile1, ofile2, ofile3,ofile4 …”, если нет, то “ofileaa, ofileab,
 ofileac, ofilead … ofileaz, ofileba ofilebb … ”   X
● Размер файла в строчках  X
● Размер файла в символах  X
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

    public Split(String outputNameFile, boolean fileNumbering, int countLines
            , int countSymbols, int countFiles, String inputFileName) {
        this.fileNumbering = fileNumbering;
        this.countLines = countLines;
        this.countSymbols = countSymbols;
        this.countFiles = countFiles;
        this.inputFileName = inputFileName;
        correctFileName(outputNameFile);
        createFileFromLine(20);
        //createFileFromSymbol(0);
    }

    //Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному

    private void correctFileName(String outputNameFile) {
        if (outputNameFile.equals("-")) {
            String fileName = String.valueOf(Path.of(inputFileName).getFileName());
            outputNameFile = fileName.substring(0, fileName.lastIndexOf("."));
        }
        this.outputNameFile = outputNameFile;
    }


    public String orderFileName(String outputNameFile) {
        if (fileNumbering) {

        } else {

        }
        return "";
    }

    public void createFileFromLine(int countLines) {
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                counter++;
            }
            reader.close();
            reader = new BufferedReader(new FileReader(inputFileName));
            for (int i = 0; i < Math.ceil((double) counter / countLines); i++) {
                File file = new File("Aboba.txt");
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < countLines; j++) {
                    line = reader.readLine();
                    writer.write(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFileFromSymbol(int countSymbols) {
        int counter = 1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            char symbol;
            while ((symbol = (char) reader.read()) != (char) -1) {
                System.out.println(symbol);
                counter++;
            }
            reader.close();
            reader = new BufferedReader(new FileReader(inputFileName));
            for (int i = 0; i < Math.ceil((double) counter / countSymbols); i++) {
                File file = new File("Aboba.txt");
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < countSymbols; j++) {
                    symbol = (char) reader.read();
                    writer.write(symbol);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
