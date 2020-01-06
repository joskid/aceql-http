/**
 *
 */
package org.kawanfw.sql.api.server.firewall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.SystemUtils;
import org.kawanfw.sql.api.server.util.IpUtil;

/**
 * Firewall instance that denies access to all IP addresses declared in the
 * {@code user.home/.aceql-http/ip_blacklist.csv file}, where {@code user.home}
 * is the value {@link System#getProperty(String)} of "user.home". <br>
 * <br>
 * The IPs in the blacklist file must be comma separated. CIDR notation is accepted. <br>
 * Note that the file of blacklisted IPs is loaded once on AceQL server startup. Any
 * change to the black list file thus requires a server restart.
 *
 * @author Nicolas de Pomereu
 *
 */
public class DenyBlacklistedIpSqlFirewall extends DefaultSqlFirewall implements SqlFirewall {

    private static final String IP_BLACKLIST_CSV = "ip_blacklist.csv";
    private static File ipBlackListCsv = null;
    private static Set<String> ipSet = new TreeSet<>();

    /**
     * @return <code><b>true</b></code> if the client IP is on the blacklist file ({@code user.home/.aceql-http/ip_blacklist.csv} file).
     */
    @Override
    public boolean allowSqlRunAfterAnalysis(String username, Connection connection, String ipAddress, String sql,
	    boolean isPreparedStatement, List<Object> parameterValues) throws IOException, SQLException {

	if (ipBlackListCsv == null) {
	    ipBlackListCsv = new File(
		    SystemUtils.USER_HOME + File.separator + ".aceql-http" + File.separator + IP_BLACKLIST_CSV);
	    if (!ipBlackListCsv.exists()) {
		throw new FileNotFoundException("Black list file of IPs does not exist: " + ipBlackListCsv);
	    }

	    ipSet = IpUtil.extractIpAddressesFromCsv(ipBlackListCsv);
	    System.out.println("Blacklisted IPs: " + ipSet);
	}

	System.out.println("Testing IP: " + ipAddress);
	return (!IpUtil.isIpAddressInSet(ipAddress, ipSet));
    }

}
