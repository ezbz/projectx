package org.projectx.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetUtils {
  public static String getLocalHostname() {
    String hostname;
    try {
      final InetAddress addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
    } catch (final UnknownHostException e) {
      hostname = "unknown";
    }
    return hostname;
  }
}