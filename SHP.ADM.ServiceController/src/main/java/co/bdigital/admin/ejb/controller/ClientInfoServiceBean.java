package co.bdigital.admin.ejb.controller;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.bdigital.admin.ejb.controller.view.ClientInfoServiceBeanLocal;
import co.bdigital.admin.messaging.updateprofile.UpdateProfileRQType;
import co.bdigital.admin.messaging.updateprofile.UpdateProfileServiceRequestType;
import co.bdigital.admin.model.StatusResponse;
import co.bdigital.admin.util.WebConsoleUtil;
import co.bdigital.cmm.consumer.exception.RestClientException;
import co.bdigital.cmm.consumer.service.impl.RestClientImpl;
import co.bdigital.cmm.ejb.generic.ServiceControllerHelper;
import co.bdigital.cmm.ejb.util.Constant;
import co.bdigital.cmm.ejb.util.ContextUtilHelper;
import co.bdigital.cmm.jpa.exception.JPAException;
import co.bdigital.cmm.jpa.model.Cliente;
import co.bdigital.cmm.jpa.model.Intento;
import co.bdigital.cmm.jpa.service.impl.ClientByPhoneNumberJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.IntentosJPAServiceIMPL;
import co.bdigital.cmm.jpa.services.ClientByPhoneNumberJPAService;
import co.bdigital.cmm.jpa.services.IntentosJPAService;
import co.bdigital.cmm.messaging.esb.BodyType;
import co.bdigital.cmm.messaging.esb.RequestHeaderOutMessageType;
import co.bdigital.cmm.messaging.esb.RequestHeaderOutType;
import co.bdigital.cmm.messaging.esb.ResponseHeaderOutMessageType;
import co.bdigital.shl.common.service.bean.view.ResourceLocator;
import co.bdigital.shl.common.service.pojo.ResourcesPojo;
import co.bdigital.shl.tracer.CustomLogger;
import co.bdigital.shl.tracer.ErrorEnum;

/**
 * 
 * @author daniel.pareja
 *
 */
@Stateless
@Local(ClientInfoServiceBeanLocal.class)
public class ClientInfoServiceBean implements ClientInfoServiceBeanLocal {

    private ResourceLocator resourceLocator;
    private ContextUtilHelper contextUtilHelper;
    private static final String ERROR_500 = "500";
    @PersistenceContext(unitName = "JPAManager")
    private EntityManager em;
    private static CustomLogger logger;
    ServiceControllerHelper sch;
    private ClientByPhoneNumberJPAService clienteByPhoneJPA;
    private IntentosJPAService intentosJPAService;

    public ClientInfoServiceBean() {

        logger = new CustomLogger(CloseAccountServiceBean.class);
        sch = ServiceControllerHelper.getInstance();
        contextUtilHelper = ContextUtilHelper.getInstance();
        clienteByPhoneJPA = new ClientByPhoneNumberJPAServiceIMPL();
        intentosJPAService = new IntentosJPAServiceIMPL();
    }

    /**
     * Método que retorna a un cliente filtrado por número de documento
     * 
     * @param documentId
     * @param documentType
     * @param region
     * @return <code>Cliente</code>
     */
    public Cliente getClientByNumDoc(String documentId, String documentType,
            String region) {

        Cliente cliente = null;
        try {
            cliente = clienteByPhoneJPA.findClientByDocumentIdAndRegion(
                    documentId, documentType, region, em);

        } catch (JPAException e) {
            logger.error(ErrorEnum.DB_ERROR, Constant.ERROR_MESSAGE_DB_QUERY,
                    e);

        }
        return cliente;
    }

    /**
     * Método que actualiza datos del cliente
     * 
     * @param cliente
     */
    public StatusResponse updateClientInfo(Cliente cliente, String region) {
        final String OPERATION = "Operaciones";
        final String NAME_SPACE = "http://co.bancaDigital/sherpa/servicio/operaciones/v1.0";
        final String OPERATIONCLIENT = "modificarPerfil";
        final String INITTRANSACTIONRQ = "updateProfileRQ";
        final String INITTRANSACTIONRS = "updateProfileRS";

        RequestHeaderOutMessageType requestESB = new RequestHeaderOutMessageType();
        UpdateProfileServiceRequestType requestAnyType = new UpdateProfileServiceRequestType();
        UpdateProfileRQType profileRQType = new UpdateProfileRQType();

        // ****************************** REQUEST ESB
        // ****************************************
        // Se configura del Header ESB
        RequestHeaderOutType requestHeaderOutType = new RequestHeaderOutType();
        Date systemDate = new Date();
        requestHeaderOutType.setHeader(sch.configureRequestESBHeader(
                Constant.COMMON_STRING_ADM_CHANNEL_ID, null,
                String.valueOf(systemDate.getTime()), OPERATION, NAME_SPACE,
                OPERATIONCLIENT));

        // Configuramos la operación:
        requestHeaderOutType.getHeader().setMessageContext(this.sch
                .configureMessageContext(INITTRANSACTIONRQ, INITTRANSACTIONRS));

        // Configuramos las propiedades de internacionalizacion:
        this.sch.addPropertyInternationalizationMessageContext(
                requestHeaderOutType.getHeader().getMessageContext(),
                (null != region ? region : Constant.SERVICE_REGION_CO));

        profileRQType.setEmail(WebConsoleUtil.replaceAccentAndSpecialCharacter(
                cliente.getCorreoElectronico()));
        profileRQType.setNickName(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getNickname()));
        profileRQType.setPhoneNumber(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getCelular()));
        profileRQType.setName(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getNombre1()));
        profileRQType.setMiddleName(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getNombre2()));
        profileRQType.setFirstName(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getApellido1()));
        profileRQType.setLastName(WebConsoleUtil
                .replaceAccentAndSpecialCharacter(cliente.getApellido2()));

        requestAnyType.setUpdateProfileRQ(profileRQType);
        // Se Configura el Body
        BodyType bodyType = new BodyType();
        bodyType.setAny(requestAnyType);
        requestHeaderOutType.setBody(bodyType);
        // Se configura el RequestESB Completo
        requestESB.setRequestHeaderOut(requestHeaderOutType);
        // ************************************************************************************

        // ************************************************************************************

        // ****************************** Envío de Mensaje al IIB
        // ****************************
        ResponseHeaderOutMessageType responseESB = null;
        StatusResponse response = new StatusResponse();

        try {

            ResourcesPojo resourcesPojo;
            this.resourceLocator = contextUtilHelper
                    .instanceResourceLocatorRemote();
            resourcesPojo = this.resourceLocator.getConnectionData(NAME_SPACE);

            if (null == resourcesPojo) {
                logger.info(Constant.ERROR_MESSAGE_SINGLETON);
            }

            responseESB = RestClientImpl.getInstance(resourcesPojo)
                    .executeOperation(requestESB);

        } catch (RestClientException e) {
            logger.error(ErrorEnum.BROKER_ERROR,
                    Constant.ERROR_MESSAGE_IIB_COMMUNICATION, e);
            response.setStatusCode(ERROR_500);
            response.setStatusDesc(Constant.ERROR_MESSAGE_IIB_COMMUNICATION);
            return response;

        }

        response.setStatusCode(responseESB.getResponseHeaderOut().getHeader()
                .getResponseStatus().getStatusCode());
        response.setStatusDesc(responseESB.getResponseHeaderOut().getHeader()
                .getResponseStatus().getErrorMessage());
        return response;

    }

    /**
     * Metodo para buscar un cliente por numero de celular
     * 
     * @param documentId
     * @param documentType
     * @param region
     * @return
     */
    public Cliente getClientByPhoneNumber(String phoneNumber) {
        Cliente cliente = null;
        try {
            cliente = clienteByPhoneJPA.getClientByPhoneNumber(phoneNumber, em);

        } catch (JPAException e) {
            logger.error(ErrorEnum.DB_ERROR, Constant.ERROR_MESSAGE_DB_QUERY,
                    e);

        }
        return cliente;
    }

    /**
     * Metodo que retorna el intento delimitado por el tipo de acceso, este
     * puede ser de tipo OTP o biometria (BIO)
     * 
     * @param phone_number
     * @param access_type
     * @return String
     * @throws JPAException
     * 
     */
    public Intento getAttempts(String phone_number, String access_type) {
        Intento attempts = null;
        try {
            attempts = intentosJPAService.getAttempt(em, phone_number,
                    access_type);
        } catch (Exception e) {
            logger.error(ErrorEnum.DB_ERROR, Constant.ERROR_MESSAGE_DB_QUERY,
                    e);
        }
        return attempts;
    }
}
