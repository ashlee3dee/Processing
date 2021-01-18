//package g4p.tool;
//
//import java.io.IOException;
//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
///**
// * Usage
// Log.logger().info(...)
// etc.
// * 
// * @author Peter Lager
// */
//public class Log {
//
//	private static Log instance = null;
//
//	public static Logger logger() {
//		if (instance == null) {
//			instance = new Log();
//		}
//		return instance.logger;
//	}
//
//	// ====================================================================
//
//	private final Logger logger;
//
//	private Log(){
//		logger = Logger.getLogger(this.getClass().getName());
//		logger.setLevel(Level.INFO);
//		//  false = don't use console output
//		logger.setUseParentHandlers(false);
//		// This block configures the logger with handler and formatter  
//		FileHandler fh;
//		try {
//			fh = new FileHandler("/Users/peter/gb-data-log.txt");
//			logger.addHandler(fh);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//		} catch (SecurityException | IOException e) {
//			System.err.println("####  Unable to create data logger  ####");
//		}
//	}
//
//}
