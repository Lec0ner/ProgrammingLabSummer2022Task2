package split;

/* Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному V
* ● Порядок названия если флаг есть,то “ofile1, ofile2, ofile3,ofile4 …”, если нет, то “ofileaa, ofileab,
* ofileac, ofilead … ofileaz, ofileba ofilebb … ”   X
* Размер файла в строчках  V
* Размер файла в символах  V
* кол-во выходных файлов  X
* Регулярка ([^\\]+)\.txt
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
    private int count = 0;
    private int counterFiles;

    public Split(String outputNameFile, boolean fileNumbering, int countLines
            , int countSymbols, int countFiles, String inputFileName) {
        this.fileNumbering = fileNumbering;
        this.countLines = countLines;
        this.countSymbols = countSymbols;
        this.inputFileName = inputFileName;
        this.countFiles = countFiles;
        correctFileName(outputNameFile);
    }


    /** Стартовая функция **/

    public void start() {
        new File("output").mkdir();
        if (countFiles > 0 )createFileFromFile();
        else if (countSymbols > 0){
            readFileSymbol();
            createFileFromSymbol();
        } else if (countLines > 0) {
            readFileLine();
            createFileFromLine();
        }
    }


    /**
     ● Возвращает базовое имя outputFileName если в параметр было передано имя,
     ● если параметр "-" то выходной = входному
     **/

    private void correctFileName(String outputNameFile) {
        if (outputNameFile.equals("-")) {
            // Определение стандартного имени файла и его присваивание без расширения
            String fileName = String.valueOf(Path.of(inputFileName).getFileName());
            outputNameFile = fileName.substring(0, fileName.lastIndexOf("."));
        }
        this.outputNameFile = outputNameFile;
    }


    /** Порядок названия файла **/

    private String orderFileName() {
        // Создание строки
        StringBuilder output = new StringBuilder();
        if (fileNumbering) output.append(count + 1);
        else {
            int k = count;
            // Определение разряда и добавление в строку нужных символов
            for (int i = 0; i < (int) (Math.log(counterFiles) / Math.log(26) - 0.000001) + 1; i++) {
                output.append((char) (97 + k % 26));
                k /= 26;
            }
            output.reverse();
        }
        count++;
        // Возвращение стандартного имени с символами и расширением
        return outputNameFile + output + ".txt";
    }

    /** Чтение файла построчно **/

    private void readFileLine(){
        // Создание счетчика
        int counter = 0;
        try {
            // Чтение файла и подсчет линий в файле
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            while (reader.readLine() != null) {
                counter++;
            }
            // Подсчет нужного количества файлов
            counterFiles = (int) Math.ceil((double) counter / this.countLines);
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /** Размер файла в строках **/

    private void createFileFromLine() {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            // Создание нужного количества файлов
            for (int i = 0; i < counterFiles; i++) {
                File file = new File("output/" + orderFileName());
                FileWriter writer = new FileWriter(file);
                // Заполнение файла n количеством строк
                for (int j = 0; j < this.countLines; j++) {
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

    /** Чтение файла посимвольно **/

    private int readFileSymbol(){
        // Создание счетчика
        int counter = 0;
        try {
            // Чтение файла и подсчет символов в файле
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            while ((char) reader.read() != (char) -1) {
                counter++;
            }
            // Подсчет нужного количества файлов
            counterFiles = (int) Math.ceil((double) counter / countSymbols);
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
       }
        return counter;
    }


    /** Размер файла в символах **/

    private void createFileFromSymbol() {
        char symbol;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            for (int i = 0; i < counterFiles; i++) {
                // Создание нужного количества файлов
                File file = new File("output/" + orderFileName());
                FileWriter writer = new FileWriter(file);
                // Заполнение файла n количеством символов
                for (int j = 0; j < this.countSymbols; j++) {
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

    /** Количество файлов на выходе заданное пользователем **/

    private void createFileFromFile(){
        int counterSymbols = readFileSymbol();
        counterFiles = countFiles;
        countSymbols = (int) Math.ceil((double) counterSymbols / counterFiles);
        createFileFromSymbol();
    }
}
