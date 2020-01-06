package org.kawanfw.sql.tomcat;

import java.io.File;
import java.util.Properties;

public class TomcatStarterUtilIpList {


    /**
     * Gets the IP White List
     * @param properties
     * @param database
     * @return
     */
    public static File getIPWhiteListFile(Properties properties, String database) {
	String ipWhiteListFile = "ip_white_list_file";
	return getIpList(properties, database, ipWhiteListFile);
    }

    /**
     * Gets the IP Black List file
     * @param properties
     * @param database
     * @return
     */
    public static File getIPBlackListFile(Properties properties, String database) {
	String ipWhiteListFile = "ip_black_list_file";
	return getIpList(properties, database, ipWhiteListFile);
    }


    /**
     * Gets an IP List
     * @param properties
     * @param database
     * @param ipWhiteListFile
     * @return
     * @throws IllegalArgumentException
     */
    private static File getIpList(Properties properties, String database, String ipWhiteListFile)
	    throws IllegalArgumentException {
	if (properties == null) {
	    throw new IllegalArgumentException("properties is null");
	}

	if (database == null) {
	    throw new IllegalArgumentException("database is null");
	}

	database = database.trim();


	String fileName = properties
		.getProperty(database + "." + ipWhiteListFile);

	if (fileName == null || fileName.isEmpty()) {
	    return null;
	}

	File file = new File(fileName);
	return file;
    }


}
