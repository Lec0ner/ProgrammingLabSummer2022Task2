package split;

/* Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному V
 * ● Порядок названия если флаг есть,то “ofile1, ofile2, ofile3,ofile4 …”, если нет, то “ofileaa, ofileab,
 * ofileac, ofilead … ofileaz, ofileba ofilebb … ”   X
 * Размер файла в строчках  V
 * Размер файла в символах  V
 * кол-во выходных файлов  X
 * */

import java.io.*;
import java.nio.file.Path;

public class Split {

    private String outputNameFile;
    private final boolean fileNumbering;
    private final int countLines;
    private int countSymbols;
    private final int countFiles;
    private final String inputFileName;

    public Split(String outputNameFile, boolean fileNumbering, int countLines
            , int countSymbols, int countFiles, String inputFileName) {
        this.fileNumbering = fileNumbering;
        this.countLines = countLines;
        this.countSymbols = countSymbols;
        this.inputFileName = inputFileName;
        this.countFiles = countFiles;
        correctFileName(outputNameFile);
    }


    /**
     * Стартовая функция
     **/

    public void start() {
        new File("output").mkdir();
        if (countFiles > 0) {
            createFileFromFile();
        } else if (countSymbols > 0) {
            readAndCreateFileFromSymbol();
        } else if (countLines > 0) {
            readAndCreateFileFromLine();
        }
    }


    /**
     * ● Возвращает базовое имя outputFileName если в параметр было передано имя,
     * ● если параметр "-" то выходной = входному
     **/

    private void correctFileName(String outputNameFile) {
        if (outputNameFile.equals("-")) {
            // Определение стандартного имени файла и его присваивание без расширения
            String fileName = Path.of(inputFileName).getFileName().toString();
            outputNameFile = fileName.substring(0, fileName.lastIndexOf("."));
        }
        this.outputNameFile = outputNameFile;
    }


    /**
     * Порядок названия файла
     **/

    private String orderFileName(int counter) {
        // Создание строки
        StringBuilder output = new StringBuilder();
        if (fileNumbering) output.append(counter);
        else {
            int k = counter - 1;
            // Определение разряда и добавление в строку нужных символов
            for (int i = 0; i < (int) (Math.log(counter) / Math.log(26) - 0.00001) + 1; i++) {
                output.append((char) (97 + k % 26));
                k /= 26;
            }
            output.reverse();
        }
        // Возвращение стандартного имени с символами и расширением
        return outputNameFile + output + ".txt";
    }


    /**
     * Чтение и создание файла с его заполнением построчно
     **/


    private void readAndCreateFileFromLine() {
        BufferedReader reader;
        try {
            // Чтение файла
            reader = new BufferedReader(new FileReader(inputFileName));
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        try {
            String line;
            line = reader.readLine();
            int counter = 1;
            while (line != null) {
                File file = new File("output/" + orderFileName(counter));
                FileWriter writer = new FileWriter(file);
                counter++;
                // Заполнение файла n количеством строк
                for (int j = 0; j < this.countLines; j++) {
                    if (line == null) break;
                    writer.write(line + "\n");
                    line = reader.readLine();
                }
                writer.close();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println(inputFileName + " not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        System.out.println("Line-by-line cutting " + inputFileName + " ended");
    }

    /**
     * Чтение и создание файла с его заполнением посимвольно
     **/

    private void readAndCreateFileFromSymbol() {
        BufferedReader reader;
        try {
            // Чтение файла
            reader = new BufferedReader(new FileReader(inputFileName));
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        try {
            char symbol = (char) reader.read();
            int counter = 1;
            while (symbol != (char) -1) {
                File file = new File("output/" + orderFileName(counter));
                FileWriter writer = new FileWriter(file);
                counter++;
                // Заполнение файла n количеством строк
                for (int j = 0; j < this.countSymbols; j++) {
                    if (symbol == (char) -1) break;
                    writer.write(symbol);
                    symbol = (char) reader.read();
                }
                writer.close();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println(inputFileName + " not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        System.out.println("Character-by-character cutting " + inputFileName + " ended");
    }


    /**
     * Количество файлов на выходе заданное пользователем
     **/

    private void createFileFromFile() {
        BufferedReader reader;
        try {
            // Чтение файла
            reader = new BufferedReader(new FileReader(inputFileName));
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        try {
            // Создание счетчика и подсчет им символов в файле
            int counter = 0;
            while ((char) reader.read() != (char) -1) {
                counter++;
            }
            reader.close();
            // Кол-во символов в 1 файле
            countSymbols = (int) Math.ceil((double) counter / countFiles);
            readAndCreateFileFromSymbol();
        } catch (FileNotFoundException e) {
            System.err.println(inputFileName + " not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        System.out.println("Cutting by file " + inputFileName + " ended");
    }
}
