//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.03 at 05:31:01 PM COT 
//


package co.bdigital.admin.messaging.biometric.markfinacle;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.bdigital.mdw.messaging.biometric.markfinacle package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MarkFinacleServiceRequest_QNAME = new QName("http://www.grupobancolombia.com/BandaDigital/services/MarkFinacle", "MarkFinacleServiceRequest");
    private final static QName _MarkFinacleServiceResponse_QNAME = new QName("http://www.grupobancolombia.com/BandaDigital/services/MarkFinacle", "MarkFinacleServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.bdigital.mdw.messaging.biometric.markfinacle
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MarkFinacleServiceRequestType }
     * 
     */
    public MarkFinacleServiceRequestType createMarkFinacleServiceRequestType() {
        return new MarkFinacleServiceRequestType();
    }

    /**
     * Create an instance of {@link MarkFinacleServiceResponseType }
     * 
     */
    public MarkFinacleServiceResponseType createMarkFinacleServiceResponseType() {
        return new MarkFinacleServiceResponseType();
    }

    /**
     * Create an instance of {@link MarkFinacleRSType }
     * 
     */
    public MarkFinacleRSType createMarkFinacleRSType() {
        return new MarkFinacleRSType();
    }

    /**
     * Create an instance of {@link MarkFinacleRQType }
     * 
     */
    public MarkFinacleRQType createMarkFinacleRQType() {
        return new MarkFinacleRQType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MarkFinacleServiceRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.grupobancolombia.com/BandaDigital/services/MarkFinacle", name = "MarkFinacleServiceRequest")
    public JAXBElement<MarkFinacleServiceRequestType> createMarkFinacleServiceRequest(MarkFinacleServiceRequestType value) {
        return new JAXBElement<MarkFinacleServiceRequestType>(_MarkFinacleServiceRequest_QNAME, MarkFinacleServiceRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MarkFinacleServiceResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.grupobancolombia.com/BandaDigital/services/MarkFinacle", name = "MarkFinacleServiceResponse")
    public JAXBElement<MarkFinacleServiceResponseType> createMarkFinacleServiceResponse(MarkFinacleServiceResponseType value) {
        return new JAXBElement<MarkFinacleServiceResponseType>(_MarkFinacleServiceResponse_QNAME, MarkFinacleServiceResponseType.class, null, value);
    }

}
