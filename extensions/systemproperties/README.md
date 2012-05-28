AutoDiscover System Properties Extension 
========================================================

Extension showing how you can filter and copy client side System Properties over to the Container side via a File in the Deployment.

    <?xml version="1.0" encoding="UTF-8"?>
    <arquillian xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://jboss.org/schema/arquillian"
        xsi:schemaLocation="http://jboss.org/schema/arquillian http://jboss.org/schema/arquillian/arquillian_1_0.xsd">
    
        <extension qualifier="systemproperties">
            <property name="prefix">ARQT_</property>
        </extension>
    </arquillian>

Following SPI's are used:

Client side
------------

* org.jboss.arquillian.core.spi.LoadableExtension
* org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor
* org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender

Container side
---------------

* org.jboss.arquillian.container.test.spi.RemoteLoadableExtension
* Core @Observers
