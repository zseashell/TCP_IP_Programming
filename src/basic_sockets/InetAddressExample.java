package basic_sockets;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class InetAddressExample {

	public static void main(String[] args) {
		try {
			Enumeration<NetworkInterface> interfaceList = NetworkInterface
					.getNetworkInterfaces();
			if (interfaceList == null) {
				System.out.println("No interfaces found");
			} else {
				while (interfaceList.hasMoreElements()) {
					NetworkInterface iface = interfaceList.nextElement();
					System.out.println("Interface " + iface.getName() + ":");
					// get InetAddress of interface
					Enumeration<InetAddress> addressList = iface
							.getInetAddresses();
					if (!addressList.hasMoreElements()) {
						System.out
								.println("\t(No addresses for this interface)");
					}
					while (addressList.hasMoreElements()) {
						InetAddress address = addressList.nextElement();
						System.out
								.print("\tAddress "
										+ (address instanceof Inet4Address ? "(v4)"
												: (address instanceof Inet6Address ? "(v6)"
														: "(?)")));
						System.out.println(": " + address.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// www.baidu.com test
		String host = "www.baidu.com";
		System.out.println(host + ":");
		InetAddress[] addresses;
		try {
			addresses = InetAddress.getAllByName(host);
			for (InetAddress inetAddress : addresses) {
				System.out.println("\t" + inetAddress.getHostName() + "/"
						+ inetAddress.getHostAddress());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
