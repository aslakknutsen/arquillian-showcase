# Arquillian Multinode Example

This example demonstrates how to use Arquillian to control multiple servers (of the same type), deploy an archive to each server and run tests in each container and a client test (mixed mode testing).

The test sets up a distributed cache across three servers using Infinispan. The tests round robin to increment the cache inside the container. The value of the counter is checked on each node to verify its being distributed properly. Finally, the value of the cache is checked using an HTTP request to a Servlet that has access to the cache.

## Configuring your firewall to allow multicasting

This example uses multicasting to negotiate the distribution of the cache values. If you have a firewall enabled and it's configured to block udp by default, you'll need to either open the port Infinispan reserves for multicasting (by way of jGroups) or disable the firewall.

### Opening the multicasting port in Fedora

If you attempt to run the test on Fedora, it will likely fail. Fedora installs with the firewall enabled and the default configuration disables udp on all ports (with a few special exceptions). We'll need to open up that port (or disable the firewall).

You first need to know which port to open. From the Infinispan documentation, we can learn that it sets the jGroups UDP multicast port to 46655. This port can be opened using the following iptables chain rule:

    -A INPUT -p udp -m state --state NEW -m udp --dport 46655 -j ACCEPT

You can also use the system-config-firewall if iptable chains aren't your thing. Under the Other Ports tab, add a custom entry with the following values:

* Port: 46655
* Protocol: udp

Activate the changes on the firewall and you are ready to run the test.

### Disabling the firewall in Fedora

Other option is to simply disable the firewall. You can disable the firewall from the commandline using:

    sudo systemctl stop iptables.service 
    sudo systemctl stop ip6tables.service 

You can also use system-config-firewall to disable the firewall with a single click.
