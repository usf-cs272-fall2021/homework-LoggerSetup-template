import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Runs a couple of tests to make sure Log4j2 is setup.
 *
 * NOTE: There are better ways to test log configuration---we will keep it
 * simple here because we just want to make sure you can run and configure
 * Log4j2.
 *
 * This is also not the most informative configuration---it is just one of the
 * most testable ones that require you to learn about how to handle stack trace
 * output.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2021
 */
@TestMethodOrder(MethodName.class)
public class LoggerSetupTest {
	/** Used to capture console output. */
	private static ByteArrayOutputStream capture = null;

	/** Character set used to read files. */
	private static Charset UTF8 = StandardCharsets.UTF_8;

	/** Path to expected debug.log file. */
	private static Path debug = Path.of("src", "test", "resources", "debug.txt");

	/**
	 * Setup that runs before each test.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	@BeforeAll
	public static void setup() throws IOException {
		// delete any old debug files
		Files.deleteIfExists(Path.of("debug.log"));

		// capture all system console output
		PrintStream original = System.out;
		capture = new ByteArrayOutputStream();
		System.setOut(new PrintStream(capture));

		// run main() only ONCE to avoid duplicate entries
		// and shutdown log manager to flush the debug files
		LoggerSetup.main(null);
		LogManager.shutdown();

		// restore system.out
		System.setOut(original);
		System.out.println(capture.toString());
	}

	/**
	 * Tests configuration file location and name.
	 */
	@Nested
	public class A_ConfigFileTests {
		/**
		 * Make sure you are not using the log4j2-test.* name. That is the config
		 * file name used for test code not main code.
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testNotTest() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2-test\\.\\w+"))
					.toList();

			assertEquals(Collections.emptyList(), found,
					"Do not use this filename to configure logging of main code.");
		}

		/**
		 * Make sure you are using the correct file name.
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testNameCorrect() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2\\.\\w+"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file with correct name.");
		}

		/**
		 * Make sure you are using the correct location for the configuration file.
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testLocationCorrect() throws IOException {
			var found = Files.walk(Path.of("src", "main", "resources"))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2.*\\.\\w+"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file in the correct location.");
		}

		/**
		 * Make sure you are using the correct extension for the configuration file.
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testExtensionCorrect() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("(?i)log4j2.*\\.(properties|ya?ml|jso?n|xml)"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file with the correct extension.");
		}
	}

	/**
	 * Tests Root logger console output.
	 */
	@Nested
	public class B_RootConsoleTests {
		/**
		 * Captures the console output and compares to expected.
		 *
		 * @param line the line number of console output to test
		 * @param expected the expected output for that line of console output
		 * @throws IOException if an I/O error occurs
		 */
		@ParameterizedTest
		@CsvSource({
				"1,ERROR emu", "2,Catching falcon"
		})
		public void testConsole(int line, String expected) throws IOException {
			String[] captured = capture.toString().split("[\\n\\r]+");

			if (line >= captured.length) {
				assertEquals(expected, "", "Did not produce enough console output.");
			}

			assertEquals(expected, captured[line].strip());
		}
	}

	/**
	 * Tests LoggerSetup logger console output.
	 */
	@Nested
	public class C_LoggerConsoleTests {
		/**
		 * Captures the console output and compares to expected.
		 *
		 * @param line the line number of console output to test
		 * @param expected the expected output for that line of console output
		 * @throws IOException if an I/O error occurs
		 */
		@ParameterizedTest
		@CsvSource({
				"4,ibis", "5,wren", "6,ERROR emu", "7,Catching falcon"
		})
		public void testConsole(int line, String expected) throws IOException {
			String[] captured = capture.toString().split("[\\n\\r]+");

			if (line >= captured.length) {
				assertEquals(expected, "", "Did not produce enough console output.");
			}

			assertEquals(expected, captured[line].strip());
		}
	}

	/**
	 * Tests first part of file output.
	 */
	@Nested
	public class D_FileNormalTests {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// test file output is as expected
			List<String> expected = Files.lines(debug, UTF8)
					.map(String::strip)
					.collect(Collectors.toList());

			List<String> actual = Files.lines(Path.of("debug.log"), UTF8)
					.map(String::strip)
					.collect(Collectors.toList());

			// only test a subset here
			expected = expected.subList(0, 4);
			actual = actual.subList(0, 4);

			assertEquals(expected, actual, "Compare debug.log and test/debug.txt in Eclipse.");
		}
	}

	/**
	 * Tests first part of file output.
	 */
	@Nested
	public class E_FileExceptionTests {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// test file output is as expected
			List<String> expected = Files.lines(debug, UTF8)
					.map(String::strip)
					.collect(Collectors.toList());

			List<String> actual = Files.lines(Path.of("debug.log"), UTF8)
					.map(String::strip)
					.collect(Collectors.toList());

			// only test a subset here
			expected = expected.subList(4, expected.size());
			actual = actual.subList(4, actual.size());

			assertEquals(expected, actual, "Compare debug.log and test/debug.txt in Eclipse.");
		}
	}
}
