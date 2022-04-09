package split;

import java.io.*;
import java.nio.file.Path;

public class Split {

    private String outputNameFile;
    private final boolean fileNumbering;
    private final int countLines;
    private int countSymbols;
    private final int countFiles;
    private final String inputFileName;

    public Split(String outputNameFile, boolean fileNumbering, int countLines,
                 int countSymbols, int countFiles, String inputFileName) {
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
        if (countFiles > 0)
            createFileFromFile();
        else if (countSymbols > 0)
            findingErrors();
        else if (countLines > 0)
            findingErrors();
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
        StringBuilder output = new StringBuilder();
        if (fileNumbering) output.append(counter);
        else {
            int k = counter - 1;
            // Определение разряда и добавление в строку нужных символов
            for (int i = 0; i < (int) (Math.log(counter) / Math.log(27)) + 1; i++, k /= 26)
                output.append((char) (97 + k % 26));
            output.reverse();
        }
        return outputNameFile + output + ".txt";
    }

    /**
     * Чтение и создание файла, заполнение его построчно
     **/
    private void createFileFromLine(BufferedReader reader) throws IOException {
        String line;
        line = reader.readLine();
        int countFilesNow = 0;
        while (line != null) {
            try (FileWriter writer = new FileWriter("output/" + orderFileName(++countFilesNow))) {
                // Заполнение файла n количеством строк
                for (int j = 0; j < this.countLines; j++) {
                    writer.write(line + "\n");
                    line = reader.readLine();
                    if (line == null) break;

                }
            }
        }
        System.out.println("Line-by-line cutting " + inputFileName + " ended");
    }

    /**
     * Чтение и создание файла, заполнение его посимвольно
     **/
    private void createFileFromSymbol(BufferedReader reader) throws IOException {
        int countFilesNow = 0;
        int symbol = reader.read();
        while (symbol != -1) {
            try (FileWriter writer = new FileWriter("output/" + orderFileName(++countFilesNow))) {
                // Заполнение файла n количеством строк
                for (int j = 0; j < this.countSymbols; j++) {
                    writer.write((char) symbol);
                    symbol = reader.read();
                    if (symbol == -1) break;
                }
            }
        }
        System.out.println("Character-by-character cutting " + inputFileName + " ended");
    }


    /**
     * Количество файлов на выходе, заданное пользователем
     **/
    private void createFileFromFile() {
        int counter = 0;
        // Подсчет символов в файле
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            while (reader.read() != -1) counter++;
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
            return;
        }
        // Кол-во символов в 1 файле
        countSymbols = (int) Math.ceil((double) counter / countFiles);
        findingErrors();
        System.out.println("Cutting by file " + inputFileName + " ended");
    }


    private void findingErrors() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            if (countSymbols > 0) {
                createFileFromSymbol(reader);
            } else if (countLines > 0) {
                createFileFromLine(reader);
            }
        } catch (FileNotFoundException e) {
            System.err.println(inputFileName + " not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(inputFileName + " was not created " + e.getMessage());
            System.err.println("Cutting is broken");
        }
    }
}

