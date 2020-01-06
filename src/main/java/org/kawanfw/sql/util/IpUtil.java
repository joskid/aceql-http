/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP
 * Copyright (C) 2020,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * AceQL HTTP is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * AceQL HTTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA
 *
 * Any modifications to this file must keep this entire header
 * intact.
 */

package org.kawanfw.sql.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.net.util.SubnetUtils;

/**
 * Misc utility methods for black or white listing.
 *
 * @author Nicolas de Pomereu
 *
 */
public class IpUtil {

    /**
     * Says if an IP address belongs to a subnet.
     *
     * @param subnet    the CIDR notation
     * @param ipAddress the IP Address to test
     * @return true if the IP address is in Range.
     */
    public static boolean isIpAddressInRange(String subnet, String ipAddress) {
	if (subnet == null) {
	    throw new NullPointerException("subnet is null!");
	}
	if (ipAddress == null) {
	    throw new NullPointerException("ipAddress is null!");
	}

	SubnetUtils utils = new SubnetUtils(subnet);
	return utils.getInfo().isInRange(ipAddress);
    }

    /**
     * Says if the passed IP address exists in the passed Set.
     * The CIDR notation is supported for addresses in the Set.
     *
     * @param ipAddress the IP address to search for
     * @param ipSet	the IP addresses to search IN.
     * @return	true if the passed IP address exists in the Set.
     */
    public static boolean isIpAddressInSet(String ipAddress, Set<String> ipSet) {
	if (ipAddress == null) {
	    throw new NullPointerException("ipAddress is null!");
	}
	if (ipSet == null) {
	    throw new NullPointerException("ipSet is null!");
	}

	// Test non CIDR notation
	if (ipSet.contains(ipAddress.trim())) {
	    return true;
	}

	// Test CIDR notation of IPs in Set
	for (String ip : ipSet) {
	    if (ip.contains("/")) {
		if (IpUtil.isIpAddressInRange(ip, ipAddress)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Extract all IP addresses for a CSV file with IP addresses separated by commas
     * or semicolons.
     *
     * @param file the CSV file containing the IP addresses
     * @return a set with all extracted IP addresses
     * @throws IOException if any I/O occurs.
     */
    public static Set<String> extractIpAddressesFromCsv(File file) throws IOException {
	if (file == null) {
	    throw new NullPointerException("file is null!");
	}
	if (!file.exists()) {
	    throw new FileNotFoundException("file does not exist: " + file);
	}

	Set<String> ips = new TreeSet<>();

	try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	    String line = null;
	    while ((line = br.readLine()) != null) {
		line = line.trim();
		line = line.replace(" ", "");
		StringTokenizer tok = new StringTokenizer(line, ",; ");
		while (tok.hasMoreElements()) {
		    String ip = (String) tok.nextElement();
		    ips.add(ip.trim());
		}
	    }
	}

	return ips;
    }

}
