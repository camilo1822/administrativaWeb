//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.02 at 11:16:42 AM COT 
//


package co.bdigital.admin.messaging.services.biometric.getProfileById;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetProfileByIDServiceRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProfileByIDServiceRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getProfileByIDRQ" type="{http://www.grupobancolombia.com/BandaDigital/services/GetProfileByIDServices}GetProfileByIDRQType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProfileByIDServiceRequestType", propOrder = {
    "getProfileByIDRQ"
})
public class GetProfileByIDServiceRequestType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected GetProfileByIDRQType getProfileByIDRQ;

    /**
     * Gets the value of the getProfileByIDRQ property.
     * 
     * @return
     *     possible object is
     *     {@link GetProfileByIDRQType }
     *     
     */
    public GetProfileByIDRQType getGetProfileByIDRQ() {
        return getProfileByIDRQ;
    }

    /**
     * Sets the value of the getProfileByIDRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetProfileByIDRQType }
     *     
     */
    public void setGetProfileByIDRQ(GetProfileByIDRQType value) {
        this.getProfileByIDRQ = value;
    }

}
