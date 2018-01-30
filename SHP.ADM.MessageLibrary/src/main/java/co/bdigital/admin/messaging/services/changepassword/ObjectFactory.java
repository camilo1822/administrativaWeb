//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.28 at 02:32:49 PM COT 
//


package co.bdigital.admin.messaging.services.changepassword;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.bdigital.admin.messaging.services.changepassword package. 
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

    private final static QName _ChangePasswordServiceResponse_QNAME = new QName("http://www.grupobancolombia.com/BandaDigital/services/ChangePasswordServices", "ChangePasswordServiceResponse");
    private final static QName _ChangePasswordServiceRequest_QNAME = new QName("http://www.grupobancolombia.com/BandaDigital/services/ChangePasswordServices", "ChangePasswordServiceRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.bdigital.admin.messaging.services.changepassword
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ChangePasswordServiceResponseType }
     * 
     */
    public ChangePasswordServiceResponseType createChangePasswordServiceResponseType() {
        return new ChangePasswordServiceResponseType();
    }

    /**
     * Create an instance of {@link ChangePasswordServiceRequestType }
     * 
     */
    public ChangePasswordServiceRequestType createChangePasswordServiceRequestType() {
        return new ChangePasswordServiceRequestType();
    }

    /**
     * Create an instance of {@link ChangePasswordRSType }
     * 
     */
    public ChangePasswordRSType createChangePasswordRSType() {
        return new ChangePasswordRSType();
    }

    /**
     * Create an instance of {@link ChangePasswordRQType }
     * 
     */
    public ChangePasswordRQType createChangePasswordRQType() {
        return new ChangePasswordRQType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangePasswordServiceResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.grupobancolombia.com/BandaDigital/services/ChangePasswordServices", name = "ChangePasswordServiceResponse")
    public JAXBElement<ChangePasswordServiceResponseType> createChangePasswordServiceResponse(ChangePasswordServiceResponseType value) {
        return new JAXBElement<ChangePasswordServiceResponseType>(_ChangePasswordServiceResponse_QNAME, ChangePasswordServiceResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangePasswordServiceRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.grupobancolombia.com/BandaDigital/services/ChangePasswordServices", name = "ChangePasswordServiceRequest")
    public JAXBElement<ChangePasswordServiceRequestType> createChangePasswordServiceRequest(ChangePasswordServiceRequestType value) {
        return new JAXBElement<ChangePasswordServiceRequestType>(_ChangePasswordServiceRequest_QNAME, ChangePasswordServiceRequestType.class, null, value);
    }

}
