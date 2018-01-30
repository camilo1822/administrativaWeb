//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.02 at 02:06:25 PM COT 
//


package co.nequi.message.integration.services.getpromotiondetail;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.nequi.message.integration.services.getpromotiondetail package. 
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

    private final static QName _GetPromotionDetailResponse_QNAME = new QName("http://nequi.co/message/integration/services/GetPromotionDetail", "getPromotionDetailResponse");
    private final static QName _GetPromotionDetailRequest_QNAME = new QName("http://nequi.co/message/integration/services/GetPromotionDetail", "getPromotionDetailRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.nequi.message.integration.services.getpromotiondetail
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetPromotionDetailRequestType }
     * 
     */
    public GetPromotionDetailRequestType createGetPromotionDetailRequestType() {
        return new GetPromotionDetailRequestType();
    }

    /**
     * Create an instance of {@link GetPromotionDetailResponseType }
     * 
     */
    public GetPromotionDetailResponseType createGetPromotionDetailResponseType() {
        return new GetPromotionDetailResponseType();
    }

    /**
     * Create an instance of {@link RulesType }
     * 
     */
    public RulesType createRulesType() {
        return new RulesType();
    }

    /**
     * Create an instance of {@link PromotionType }
     * 
     */
    public PromotionType createPromotionType() {
        return new PromotionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPromotionDetailResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nequi.co/message/integration/services/GetPromotionDetail", name = "getPromotionDetailResponse")
    public JAXBElement<GetPromotionDetailResponseType> createGetPromotionDetailResponse(GetPromotionDetailResponseType value) {
        return new JAXBElement<GetPromotionDetailResponseType>(_GetPromotionDetailResponse_QNAME, GetPromotionDetailResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPromotionDetailRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nequi.co/message/integration/services/GetPromotionDetail", name = "getPromotionDetailRequest")
    public JAXBElement<GetPromotionDetailRequestType> createGetPromotionDetailRequest(GetPromotionDetailRequestType value) {
        return new JAXBElement<GetPromotionDetailRequestType>(_GetPromotionDetailRequest_QNAME, GetPromotionDetailRequestType.class, null, value);
    }

}
