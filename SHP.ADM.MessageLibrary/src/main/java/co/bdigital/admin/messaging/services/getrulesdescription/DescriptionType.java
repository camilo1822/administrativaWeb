//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.21 at 02:57:52 PM COT 
//


package co.bdigital.admin.messaging.services.getrulesdescription;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for descriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="descriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descriptionRule" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="promotionType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="service" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idPromOperacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "descriptionType", propOrder = {
    "descriptionRule",
    "promotionType",
    "service",
    "idPromOperacion"
})
public class DescriptionType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String descriptionRule;
    @XmlElement(required = true)
    protected String promotionType;
    @XmlElement(required = true)
    protected String service;
    @XmlElement(required = true)
    protected String idPromOperacion;

    /**
     * Gets the value of the descriptionRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionRule() {
        return descriptionRule;
    }

    /**
     * Sets the value of the descriptionRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionRule(String value) {
        this.descriptionRule = value;
    }

    /**
     * Gets the value of the promotionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionType() {
        return promotionType;
    }

    /**
     * Sets the value of the promotionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionType(String value) {
        this.promotionType = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Gets the value of the idPromOperacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPromOperacion() {
        return idPromOperacion;
    }

    /**
     * Sets the value of the idPromOperacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPromOperacion(String value) {
        this.idPromOperacion = value;
    }

}
