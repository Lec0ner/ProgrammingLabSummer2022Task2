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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    }

    //Задает базовое имя выходного файла, базовое имя X,если параметри "-" выходной файл = входному

    private void correctFileName(String outputNameFile) {
        if (outputNameFile.equals("-")) {
            Matcher matcher = Pattern.compile("([^\\\\]+)\\.txt").matcher(inputFileName);
            matcher.find();
            outputNameFile = matcher.group(1);
        }
        this.outputNameFile = outputNameFile;
    }

    public void orderFileName(Split outputNameFile){
        if (fileNumbering){

        }else{

        }

    }



}
