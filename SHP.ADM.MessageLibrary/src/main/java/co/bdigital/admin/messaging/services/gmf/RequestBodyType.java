//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.12.11 at 04:58:00 PM COT 
//


package co.bdigital.admin.messaging.services.gmf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestBodyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://nequi.co/message/bussines/services/operation}operationGMFRQ"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestBodyType", propOrder = {
    "operationGMFRQ"
})
public class RequestBodyType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://nequi.co/message/bussines/services/operation")
    protected OperationGMFRQType operationGMFRQ;

    /**
     * Gets the value of the operationGMFRQ property.
     * 
     * @return
     *     possible object is
     *     {@link OperationGMFRQType }
     *     
     */
    public OperationGMFRQType getOperationGMFRQ() {
        return operationGMFRQ;
    }

    /**
     * Sets the value of the operationGMFRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationGMFRQType }
     *     
     */
    public void setOperationGMFRQ(OperationGMFRQType value) {
        this.operationGMFRQ = value;
    }

}
