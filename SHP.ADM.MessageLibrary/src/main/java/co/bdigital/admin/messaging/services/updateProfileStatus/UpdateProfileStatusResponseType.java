//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.26 at 11:19:17 AM COT 
//


package co.bdigital.admin.messaging.services.updateProfileStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateProfileStatusResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateProfileStatusResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="updateProfileStatusRS" type="{http://www.grupobancolombia.com/BandaDigital/services/GetCustomerLocksService}UpdateProfileStatusRSType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateProfileStatusResponseType", propOrder = {
    "updateProfileStatusRS"
})
public class UpdateProfileStatusResponseType {

    @XmlElement(required = true)
    protected UpdateProfileStatusRSType updateProfileStatusRS;

    /**
     * Gets the value of the updateProfileStatusRS property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateProfileStatusRSType }
     *     
     */
    public UpdateProfileStatusRSType getUpdateProfileStatusRS() {
        return updateProfileStatusRS;
    }

    /**
     * Sets the value of the updateProfileStatusRS property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateProfileStatusRSType }
     *     
     */
    public void setUpdateProfileStatusRS(UpdateProfileStatusRSType value) {
        this.updateProfileStatusRS = value;
    }

}
