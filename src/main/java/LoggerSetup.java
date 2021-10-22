import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generates several log messages to test Log4j2 setup.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2021
 */
public class LoggerSetup {
	// TODO See the README file for what to do for this homework

	/**
	 * Outputs all levels of messages to a logger.
	 *
	 * @param log the logger to use
	 */
	public static void outputMessages(Logger log) {
		log.trace("turkey");
		log.debug("duck");
		log.info("ibis");
		log.warn("wren");
		log.error(Level.ERROR, new Exception("emu"));
		log.catching(Level.FATAL, new RuntimeException("falcon"));
	}

	/**
	 * Generates several log messages to two different loggers.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		System.out.println("ROOT LOGGER:");
		outputMessages(LogManager.getRootLogger());

		System.out.println();
		System.out.println("CLASS LOGGER:");
		outputMessages(LogManager.getLogger());
	}
}
