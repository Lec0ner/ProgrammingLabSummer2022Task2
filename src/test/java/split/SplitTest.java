package split;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

class SplitTest {

    private void createDirectory(){
        new File("src/test/resources/output").mkdir();

    }


    private boolean checkCorrectWork(String expectedPath) throws IOException {
        File[] outputFiles = new File("src/test/resources/output").listFiles();
        File[] expectedFiles = new File(expectedPath).listFiles();
        if (Objects.requireNonNull(outputFiles).length != Objects.requireNonNull(expectedFiles).length) return false;
        for (int i = 0; i <= Objects.requireNonNull(outputFiles).length - 1; i++) {
            if (!checkFileContent(outputFiles[i], Objects.requireNonNull(expectedFiles)[i])) return false;
            outputFiles[i].delete();
        }
        return true;
    }

    private boolean checkFileContent(File actual, File expected) throws IOException {
        if (!actual.getName().equals(expected.getName())) return false;
        List<String> expectedLines = Files.readAllLines(expected.toPath());
        List<String> actualLines = Files.readAllLines(actual.toPath());
        if (expectedLines.size() != actualLines.size()) return false;
        for (int i = 0; i <= expectedLines.size() - 1; i++) {
            if (!actualLines.get(i).equals(expectedLines.get(i))) return false;
        }
        return true;
    }

    @Test
    public void split() throws IOException {
        createDirectory();
        new Split("x", true, 2, 0, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected")
        );

        new Split("-", false, 2, 0, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected1")
        );

        new Split("file", false, 2, 0, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected2")
        );

        new Split("file", false, 20, 0, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        new Split("file", false, 100, 0, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        new Split("file", false, 100, 10000, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        new Split("file", false, 100, 100, 0, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected4")
        );

        new Split("file", false, 100, 0, 1, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        new Split("file", false, 100, 0, 10, "src/test/resources/input/input.txt").start();
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected5")
        );
    }
    @Test
    public void splitLauncher() throws IOException {
        SplitLauncher.main(("-d -l 2 src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected")
        );

        SplitLauncher.main(("-l 2 -o - src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected1")
        );

        SplitLauncher.main(("-l 2 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected2")
        );

        SplitLauncher.main(("-l 20 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        SplitLauncher.main(("-o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        SplitLauncher.main(("-c 10000 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        SplitLauncher.main(("-c 100 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected4")
        );

        SplitLauncher.main(("-n 1 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected3")
        );

        SplitLauncher.main(("-n 10 -o file src/test/resources/input/input.txt").split(" "));
        assertTrue(
                checkCorrectWork("src/test/resources/expected/expected5")
        );

    }
}


