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
 * <p>Java class for GetProfileByIDServiceResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetProfileByIDServiceResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getProfileByIDRS" type="{http://www.grupobancolombia.com/BandaDigital/services/GetProfileByIDServices}GetProfileByIDRSType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProfileByIDServiceResponseType", propOrder = {
    "getProfileByIDRS"
})
public class GetProfileByIDServiceResponseType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected GetProfileByIDRSType getProfileByIDRS;

    /**
     * Gets the value of the getProfileByIDRS property.
     * 
     * @return
     *     possible object is
     *     {@link GetProfileByIDRSType }
     *     
     */
    public GetProfileByIDRSType getGetProfileByIDRS() {
        return getProfileByIDRS;
    }

    /**
     * Sets the value of the getProfileByIDRS property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetProfileByIDRSType }
     *     
     */
    public void setGetProfileByIDRS(GetProfileByIDRSType value) {
        this.getProfileByIDRS = value;
    }

}
