package split;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public class SplitLauncher {

    @Option(name = "-o", metaVar = "ofile", usage = "Base output file name")
    private String outputNameFile = "x";

    @Option(name = "-d", usage = "Change file numbering")
    private boolean fileNumbering = false;

    @Option(name = "-l", metaVar = "num", usage = "Output file size in line", forbids = {"-c", "-n"})
    private int countLines = 100;

    @Option(name = "-c", metaVar = "num", usage = "Output file size in symbols", forbids = {"-l", "-n"})
    private int countSymbols = 0;

    @Option(name = "-n", metaVar = "num", usage = "Number of output files", forbids = {"-l", "-c"})
    private int countFiles = 0;

    @Argument(required = true, metaVar = "file", usage = "File for split")
    private String inputFileName;

    public static void main(String[] args) {
        new SplitLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (countSymbols < 1 && countFiles < 1 && countLines < 1)
                throw new IllegalArgumentException("countSymbols or countFiles or contLines must be greater than zero");
            if (!new File(inputFileName).exists())
                throw new IllegalArgumentException("Input file not found");
        } catch (CmdLineException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar split.jar [-d] [-l num|-c num|-n num] [-o ofile] file");
            parser.printUsage(System.err);
        }

        Split split = new Split(outputNameFile, fileNumbering, countLines, countSymbols, countFiles, inputFileName);
    }
}