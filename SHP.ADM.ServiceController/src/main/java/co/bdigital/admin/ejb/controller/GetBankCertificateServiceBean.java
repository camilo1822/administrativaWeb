package co.bdigital.admin.ejb.controller;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import com.nequi.cmm.report.service.ServiceReportGenerator;
import com.nequi.cmm.report.util.ConstantReport;

import co.bdigital.admin.ejb.controller.view.GetBankCertificateServiceBeanLocal;
import co.bdigital.admin.messaging.reports.CustomerDetailsReport;
import co.bdigital.admin.messaging.reports.CustomerDetailsReportType;
import co.bdigital.admin.messaging.services.getrulesdescription.GetCustomerStatusRQType;
import co.bdigital.admin.messaging.services.getrulesdescription.GetCustomerStatusRSType;
import co.bdigital.admin.messaging.services.getrulesdescription.GetCustomerStatusRequestType;
import co.bdigital.admin.messaging.services.getrulesdescription.GetCustomerStatusResponseType;
import co.bdigital.admin.util.CallServiceBean;
import co.bdigital.admin.util.ConstantADM;
import co.bdigital.admin.util.ServiceControllerBean;
import co.bdigital.admin.util.WebConsoleUtil;
import co.bdigital.cmm.consumer.exception.RestClientException;
import co.bdigital.cmm.consumer.service.impl.RestClientImpl;
import co.bdigital.cmm.ejb.generic.ServiceControllerHelper;
import co.bdigital.cmm.ejb.util.Constant;
import co.bdigital.cmm.ejb.util.ContextUtilHelper;
import co.bdigital.cmm.jpa.model.Cliente;
import co.bdigital.cmm.jpa.model.DivisionGeografica;
import co.bdigital.cmm.jpa.model.Parametro;
import co.bdigital.cmm.jpa.service.impl.AWProfileJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.AWRolJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.ClientByDocumentJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.DivisionGeograficaJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.ParameterJPAServiceIMPL;
import co.bdigital.cmm.jpa.services.AWProfileJPAService;
import co.bdigital.cmm.jpa.services.AWRolJPAService;
import co.bdigital.cmm.jpa.services.DivisionGeograficaJPAService;
import co.bdigital.cmm.messaging.esb.BodyType;
import co.bdigital.cmm.messaging.esb.RequestHeaderOutMessageType;
import co.bdigital.cmm.messaging.esb.RequestHeaderOutType;
import co.bdigital.cmm.messaging.esb.ResponseHeaderOutMessageType;
import co.bdigital.cmm.messaging.general.RequestHeader;
import co.bdigital.cmm.messaging.general.ResponseBody;
import co.bdigital.cmm.messaging.general.ResponseHeader;
import co.bdigital.cmm.messaging.general.ResponseMessageObjectType;
import co.bdigital.cmm.messaging.general.ResponseMessageType;
import co.bdigital.cmm.messaging.general.StatusType;
import co.bdigital.shl.common.service.bean.view.ResourceLocator;
import co.bdigital.shl.common.service.pojo.ResourcesPojo;
import co.bdigital.shl.tracer.CustomLogger;
import co.bdigital.shl.tracer.ErrorEnum;
import net.sf.jasperreports.export.PdfExporterConfiguration;

/**
 * 
 * @author juan.arboleda
 *
 */
@Stateless
@Local(GetBankCertificateServiceBeanLocal.class)
public class GetBankCertificateServiceBean extends CallServiceBean
        implements GetBankCertificateServiceBeanLocal {

    @PersistenceContext(unitName = "JPAManager")
    private EntityManager em;
    private AWProfileJPAService profileJPA;
    private AWRolJPAService rolJPA;
    private static CustomLogger logger;
    @EJB
    private ServiceControllerBean serviceLocatorBean;
    private EntityManagerFactory entityManagerFactory;

    private EntityManager emFRM;

    @Resource(name = "getBankCertificateIsTraceable")
    private Boolean getBankCertificateIsTraceable;

    @Resource(name = "getBankCertificateIsDebugable")
    private Boolean getBankCertificateIsDebugable;

    private ServiceControllerHelper sch;

    private DivisionGeograficaJPAService divisionGeograficaJPAService;

    private ResourceLocator resourceLocator;

    /**
     * Metodo para inicializar recursos del EJB.
     */
    @PostConstruct
    void init() {
        logger = new CustomLogger(GetBankCertificateServiceBean.class);
        profileJPA = new AWProfileJPAServiceIMPL();
        rolJPA = new AWRolJPAServiceIMPL();
        this.entityManagerFactory = Persistence
                .createEntityManagerFactory(Constant.FRM_MANAGER);
        logger = new CustomLogger(GetBankCertificateServiceBean.class,
                getBankCertificateIsTraceable, getBankCertificateIsDebugable);
        this.sch = ServiceControllerHelper.getInstance();

        this.divisionGeograficaJPAService = new DivisionGeograficaJPAServiceIMPL();
    }

    @PreDestroy
    void shutdown() {
        if ((null != this.entityManagerFactory)
                && (this.entityManagerFactory.isOpen())) {

            this.entityManagerFactory.close();
        }

        if ((null != this.emFRM) && (this.emFRM.isOpen())) {

            this.emFRM.close();
        }
    }

    @Override
    public void getCertificate(ResponseMessageObjectType responseBroker,
            String region, String phone, OutputStream outputStream) {
        Parametro paramJasper;
        String reportDate = null;
        try {
            StatusType statusTypeBroker = responseBroker.getResponseMessage()
                    .getResponseHeader().getStatus();

            /* Validar que la respuesta de BROKER */
            if (Constant.COMMON_STRING_ZERO
                    .equals(statusTypeBroker.getStatusCode())) {

                /*
                 * Se obtiene la ruta donde se encuentra el reporte JASPER
                 * BANK_CERTIFICATION
                 */
                /* Se consultan los parámetros de reporte */
                List<Parametro> parametros = ParameterJPAServiceIMPL
                        .getInstance().getRegionParameter(
                                ConstantADM.COMMON_STRING_PARAMETER_REPORTS,
                                region, em);

                paramJasper = sch.getParameterByName(parametros,
                        ConstantADM.COMMON_STRING_PARAMETER_REPORTS_JASPER_BANK_CERTIFICATION);

                GetCustomerStatusResponseType getCustomerStatusResponseType = (GetCustomerStatusResponseType) sch
                        .parsePayload(
                                responseBroker.getResponseMessage()
                                        .getResponseBody().getAny(),
                                new GetCustomerStatusResponseType());

                GetCustomerStatusRSType getCustomerStatusRSType = getCustomerStatusResponseType
                        .getGetCustomerStatusRS();

                CustomerDetailsReport customerDetailsReport = new CustomerDetailsReport();
                CustomerDetailsReportType customerDetailsReportType = new CustomerDetailsReportType();

                customerDetailsReportType.setFullName(
                        getCustomerStatusRSType.getFullName().toUpperCase());

                customerDetailsReportType
                        .setDocument(getCustomerStatusRSType.getDocumentId());

                customerDetailsReportType.setAccountNumber(phone);

                customerDetailsReportType.setAccountOpnDate(
                        getCustomerStatusRSType.getAcctOpnDate());

                customerDetailsReportType.setConventionalAccount(
                        getCustomerStatusRSType.getForacId());

                /* Se homologa tipo de documento */
                switch (getCustomerStatusRSType.getDocumentType()) {
                case ConstantADM.COMMON_STRING_CC:
                    customerDetailsReportType
                            .setDocumentType(ConstantADM.COMMON_STRING_DESC_CC);
                    break;
                case ConstantADM.COMMON_STRING_CE:
                    customerDetailsReportType
                            .setDocumentType(ConstantADM.COMMON_STRING_DESC_CE);
                    break;
                case ConstantADM.COMMON_STRING_PS:
                    customerDetailsReportType
                            .setDocumentType(ConstantADM.COMMON_STRING_DESC_PS);
                    break;
                case ConstantADM.COMMON_STRING_TI:
                    customerDetailsReportType
                            .setDocumentType(ConstantADM.COMMON_STRING_DESC_TI);
                    break;
                }

                /* Se homologa el estado */
                switch (getCustomerStatusRSType.getAcctStatus()) {
                case ConstantADM.COMMON_STRING_ESTADO_A:
                    customerDetailsReportType.setAccountStatus(
                            ConstantADM.COMMON_STRING_ESTADO_ACTIVO);
                    break;
                case ConstantADM.COMMON_STRING_ESTADO_I:
                    customerDetailsReportType.setAccountStatus(
                            ConstantADM.COMMON_STRING_ESTADO_INACTIVO);
                    break;
                default:
                    customerDetailsReportType.setAccountStatus(
                            ConstantADM.COMMON_STRING_ESTADO_DORMIDO);
                    break;
                }

                customerDetailsReport
                        .setCustomerDetails(customerDetailsReportType);

                String json = this.sch.parseJSONToString(customerDetailsReport);

                reportDate = WebConsoleUtil.formatDateToString(new Date(),
                        ConstantADM.FORMAT_DATE_PATTERN_BANK_CERTIFICATION);
                reportDate = reportDate.substring(0, 1).toUpperCase()
                        + reportDate.substring(1);

                Map<String, Object> reportParameter = new HashMap<String, Object>();
                reportParameter.put(ConstantADM.PATTERN_STRING_REPORT_DATE,
                        reportDate);

                reportParameter.put(
                        PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD,
                        phone);
                reportParameter.put(
                        PdfExporterConfiguration.PROPERTY_USER_PASSWORD, phone);
                reportParameter.put(PdfExporterConfiguration.PROPERTY_ENCRYPTED,
                        true);

                ServiceReportGenerator.buildEncryptedPdfFromJSON(
                        paramJasper.getValor(), reportParameter, json,
                        customerDetailsReportType.getDocument(),
                        customerDetailsReportType.getDocument(), outputStream);

            } else {
                logger.info(responseBroker.getResponseMessage()
                        .getResponseHeader().getStatus().getStatusDesc());
            }

        } catch (Exception e) {
            logger.error(ErrorEnum.MIDDLEWARE_ERROR,
                    ConstantReport.ERROR_MESSAGE_REPORT_BUILD, e);
        }
    }

    /**
     * 
     * @param requestHeader
     * @param cliente
     * @param divisionGeografica
     * @param bankId
     * @return
     * @throws RestClientException
     */
    public ResponseMessageObjectType callBroker(RequestHeader requestHeader,
            Cliente cliente, DivisionGeografica divisionGeografica,
            Parametro bankId, String phone) throws RestClientException {

        RequestHeaderOutMessageType requestESB = new RequestHeaderOutMessageType();
        RequestHeaderOutType requestHeaderOutType = new RequestHeaderOutType();
        ResponseMessageObjectType responseFront = new ResponseMessageObjectType();
        GetCustomerStatusRequestType getCustomerStatusRequestType = new GetCustomerStatusRequestType();
        GetCustomerStatusRQType getCustomerStatusRQType = new GetCustomerStatusRQType();
        ResponseMessageType responseMessageType = new ResponseMessageType();
        getCustomerStatusRQType.setBankId(bankId.getValor());
        getCustomerStatusRQType
                .setDocument(buildClientId(cliente.getNumeroId()));
        getCustomerStatusRQType.setPhoneNumber(
                WebConsoleUtil.buildString(ConstantADM.COMMON_STRING_PLUS,
                        divisionGeografica.getCodigoPostal(), phone));
        getCustomerStatusRequestType
                .setGetCustomerStatusRQ(getCustomerStatusRQType);
        requestHeaderOutType.setHeader(sch.configureRequestESBHeader(
                requestHeader, ConstantADM.COMMON_STRING_NAME_CUSTOMER,
                ConstantADM.COMMON_STRING_NAMESPACE_CUSTOMER,
                ConstantADM.COMMON_STRING_OPERATION_CUSTOMER));
        // Configuramos la operación:
        requestHeaderOutType.getHeader()
                .setMessageContext(sch.configureMessageContext(
                        ConstantADM.REQUEST_CUSTOMER_DETAILS,
                        ConstantADM.RESPONSE_CUSTOMER_DETAILS));
        // Configuramos las propiedades de internacionalizacion:
        sch.addPropertyInternationalizationMessageContext(
                requestHeaderOutType.getHeader().getMessageContext(),
                requestHeader.getDestination().getServiceRegion());
        // Se Configura el Body
        BodyType bodyType = new BodyType();
        bodyType.setAny(getCustomerStatusRequestType);
        requestHeaderOutType.setBody(bodyType);
        // Se configura el RequestESB Completo
        requestESB.setRequestHeaderOut(requestHeaderOutType);

        ResponseHeaderOutMessageType responseESB = new ResponseHeaderOutMessageType();

        ResourcesPojo resourcesPojo = null;
        ContextUtilHelper contextUtilHelper = ContextUtilHelper.getInstance();

        this.resourceLocator = contextUtilHelper
                .instanceResourceLocatorRemote();
        resourcesPojo = this.resourceLocator.getConnectionData(
                ConstantADM.COMMON_STRING_NAMESPACE_CUSTOMER);
        if (resourcesPojo == null) {
            logger.info(ConstantADM.ERROR_MESSAGE_SINGLETON);
            return sch.responseErrorMessage(requestHeader,
                    ConstantADM.ERROR_CODE_SINGLETON,
                    ConstantADM.ERROR_MESSAGE_SINGLETON,
                    ConstantADM.COMMON_SYSTEM_MDW, em);
        }
        try {
            responseESB = RestClientImpl.getInstance(resourceLocator.getPojo())
                    .executeOperation(requestESB);
        } catch (Exception e) {
            logger.error(ErrorEnum.BROKER_ERROR,
                    ConstantADM.ERROR_MESSAGE_IIB_COMMUNICATION, e);
            RestClientException e1 = new RestClientException(e);
            throw e1;
        }

        ResponseHeader responseHeader = sch.configureResponseFrontHeader(
                requestHeader, responseESB.getResponseHeaderOut().getHeader()
                        .getResponseStatus(),
                em);

        responseMessageType.setResponseHeader(responseHeader);

        // BODY
        ResponseBody responseBody = new ResponseBody();
        responseBody
                .setAny(responseESB.getResponseHeaderOut().getBody().getAny());

        responseMessageType.setResponseBody(responseBody);
        responseFront.setResponseMessage(responseMessageType);

        return responseFront;
    }

    /**
     * Metodo para construir numero de clientId (Cuetnas cerradas)
     * 
     * @param clientId
     * @return
     */
    public String buildClientId(String clientId) {
        if (ConstantADM.STRING_EMPTY.equals(clientId)) {
            return ConstantADM.STRING_EMPTY;
        } else {
            if (clientId.indexOf(
                    ConstantADM.COMMON_SUB_GUION) < ConstantADM.COMMON_ZERO) {
                return clientId;
            } else {
                clientId = clientId.substring(ConstantADM.COMMON_ZERO,
                        clientId.indexOf(ConstantADM.COMMON_SUB_GUION));
                return clientId;
            }
        }
    }

    @Override
    public ResponseMessageObjectType getCertificateData(String region,
            String phone) {
        Cliente client = null;
        ResponseMessageObjectType responseBroker = null;
        try {
            client = ClientByDocumentJPAServiceIMPL.getInstance()
                    .findClientByDocumentId(phone, region, em);
            if (null == client) {
                client = ClientByDocumentJPAServiceIMPL.getInstance()
                        .findClientCloseAcountByDocumentId(phone, region, em);
                if (null == client) {
                    logger.error(ErrorEnum.MIDDLEWARE_ERROR,
                            ConstantReport.ERROR_MESSAGE_REPORT_BUILD, null);
                }
            }
            // Se consulta el BANK_ID
            List<Parametro> bankIdList = ParameterJPAServiceIMPL.getInstance()
                    .getRegionParameter(
                            ConstantADM.COMMON_STRING_TYPE_PARAMETER_BANK_ID,
                            region, em);

            Parametro bankId = sch.getParameterByName(bankIdList,
                    ConstantADM.COMMON_STRING_PARAMETER_BANK_ID);
            /*
             * Se consulta la división geográfica para obtener el indicativo del
             * país
             */
            DivisionGeografica divisionGeografica = divisionGeograficaJPAService
                    .findDivisionGeografica(region, em);

            /* Consultar el estado de la cuenta en BROKER */
            RequestHeader requestHeader = new RequestHeader();
            requestHeader.setClientID(String.valueOf(client.getClienteId()));
            requestHeader.setMessageID(String.valueOf(new Date().getTime()));
            // requestHeader.set
            co.bdigital.cmm.messaging.general.DestinationType destinationType = new co.bdigital.cmm.messaging.general.DestinationType();
            destinationType.setServiceRegion(region);
            destinationType.setServiceName(
                    ConstantADM.COMMON_STRING_OP_GET_SERVICE_BROKER);
            destinationType.setServiceOperation(
                    ConstantADM.COMMON_STRING_OP_GET_CERTIFICATE);
            destinationType.setServiceVersion(
                    ConstantADM.COMMON_STRING_SERVICE_VERSION);
            requestHeader.setDestination(destinationType);
            responseBroker = callBroker(requestHeader, client,
                    divisionGeografica, bankId, phone);

            return responseBroker;
        } catch (Exception e) {
            logger.error(ErrorEnum.MIDDLEWARE_ERROR,
                    ConstantReport.ERROR_MESSAGE_REPORT_BUILD, e);
            return null;
        }
    }

}
