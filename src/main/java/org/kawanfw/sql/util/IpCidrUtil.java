package org.kawanfw.sql.util;

import org.apache.commons.net.util.SubnetUtils;

public class IpCidrUtil {

    /**
     * Sayss if an IP address belongs to a subnet
     * @param subnet	the CIDR notation
     * @param ipAdress	the IP Address to test
     * @return	true if the IP address is in Rnage
     */
    public static boolean isIpAddressInRange(String subnet, String ipAdress) {
	SubnetUtils utils = new SubnetUtils(subnet);
	return utils.getInfo().isInRange(ipAdress);
    }

}
