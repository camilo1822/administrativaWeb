//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.09 at 11:27:02 AM COT 
//


package co.nequi.message.integration.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.nequi.message.integration.services package. 
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

    private final static QName _IntegrationRequest_QNAME = new QName("http://nequi.co/message/integration/services/", "integrationRequest");
    private final static QName _IntegrationResponse_QNAME = new QName("http://nequi.co/message/integration/services/", "integrationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.nequi.message.integration.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IntegrationRequestType }
     * 
     */
    public IntegrationRequestType createIntegrationRequestType() {
        return new IntegrationRequestType();
    }

    /**
     * Create an instance of {@link IntegrationRS }
     * 
     */
    public IntegrationRS createIntegrationRS() {
        return new IntegrationRS();
    }

    /**
     * Create an instance of {@link IntegrationResponseType }
     * 
     */
    public IntegrationResponseType createIntegrationResponseType() {
        return new IntegrationResponseType();
    }

    /**
     * Create an instance of {@link IntegrationRQ }
     * 
     */
    public IntegrationRQ createIntegrationRQ() {
        return new IntegrationRQ();
    }

    /**
     * Create an instance of {@link ContainerType }
     * 
     */
    public ContainerType createContainerType() {
        return new ContainerType();
    }

    /**
     * Create an instance of {@link ProviderType }
     * 
     */
    public ProviderType createProviderType() {
        return new ProviderType();
    }

    /**
     * Create an instance of {@link ResponseBodyType }
     * 
     */
    public ResponseBodyType createResponseBodyType() {
        return new ResponseBodyType();
    }

    /**
     * Create an instance of {@link ConsumerType }
     * 
     */
    public ConsumerType createConsumerType() {
        return new ConsumerType();
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link RequestHeaderType }
     * 
     */
    public RequestHeaderType createRequestHeaderType() {
        return new RequestHeaderType();
    }

    /**
     * Create an instance of {@link ServiceType }
     * 
     */
    public ServiceType createServiceType() {
        return new ServiceType();
    }

    /**
     * Create an instance of {@link DataType }
     * 
     */
    public DataType createDataType() {
        return new DataType();
    }

    /**
     * Create an instance of {@link DestinationType }
     * 
     */
    public DestinationType createDestinationType() {
        return new DestinationType();
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link RequestBodyType }
     * 
     */
    public RequestBodyType createRequestBodyType() {
        return new RequestBodyType();
    }

    /**
     * Create an instance of {@link MessageContextType }
     * 
     */
    public MessageContextType createMessageContextType() {
        return new MessageContextType();
    }

    /**
     * Create an instance of {@link ResponseHeaderType }
     * 
     */
    public ResponseHeaderType createResponseHeaderType() {
        return new ResponseHeaderType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntegrationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nequi.co/message/integration/services/", name = "integrationRequest")
    public JAXBElement<IntegrationRequestType> createIntegrationRequest(IntegrationRequestType value) {
        return new JAXBElement<IntegrationRequestType>(_IntegrationRequest_QNAME, IntegrationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntegrationResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nequi.co/message/integration/services/", name = "integrationResponse")
    public JAXBElement<IntegrationResponseType> createIntegrationResponse(IntegrationResponseType value) {
        return new JAXBElement<IntegrationResponseType>(_IntegrationResponse_QNAME, IntegrationResponseType.class, null, value);
    }

}
