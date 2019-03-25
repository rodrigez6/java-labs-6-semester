package danyliuk.mykola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mykola Danyliuk
 */
class FileLoaderTest {

    private FileLoader loader;

    @BeforeEach
    void setUp() {
        loader = new FileLoader();
    }

    @Test
    void run() {
    }

    @Test
    void replaceWords() {
        assertEquals("55 33 4 123  ", loader.replaceWords("123 33 4 55  "));
        assertEquals("4 2 3 1", loader.replaceWords("1 2 3 4"));
    }
}