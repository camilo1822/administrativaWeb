package co.bdigital.admin.beans;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import co.bdigital.admin.ejb.controller.UserInfoServiceBean;
import co.bdigital.admin.ejb.controller.view.GetBankCertificateServiceBeanLocal;
import co.bdigital.admin.model.ActionRol;
import co.bdigital.admin.model.PermissionByRol;
import co.bdigital.admin.util.ConstantADM;
import co.bdigital.cmm.jpa.model.AwRol;
import co.bdigital.cmm.messaging.general.ResponseMessageObjectType;
import co.bdigital.cmm.messaging.general.StatusType;
import co.bdigital.shl.tracer.CustomLogger;

/**
 * 
 * @author juan.arboleda
 *
 */
@ManagedBean(name = "getCertificateMGBean")
@SessionScoped
public class GetCertificateMGBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String document;
    private String documentType;

    /* Arreglo donde se guarda la informacion de las acciones con su id */
    private transient List<ActionRol> actionsRol = null;
    /* Usuario en sesion */
    private String userCurrent;
    /* Lista de todos los roles */
    private List<AwRol> listAllRol;
    /* Logger */
    private CustomLogger logger;
    /* Permisos de la accion */
    private PermissionByRol permissionRol;
    /* Codigo del pais */
    private String countryId;

    @EJB
    private transient GetBankCertificateServiceBeanLocal getBankCertificateServiceBeanLocal;

    @EJB
    private transient UserInfoServiceBean userInfoServiceBean;

    @SuppressWarnings("unchecked")
    public GetCertificateMGBean() {
        logger = new CustomLogger(this.getClass());
        this.countryId = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.COUNTRY_ID);
        this.actionsRol = (List<ActionRol>) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.ACTIONS_BY_ROL_SESION);
        this.userCurrent = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.USER_NAME_SESION);
        permissionRol = (PermissionByRol) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.PERMISSIONS_BY_ROL_SESION);
    }

    /**
     * Metodo para descargar pdf de certificado bancario
     * 
     */
    public void downloadPdf() throws IOException {

        FacesMessage messages = null;
        OutputStream outputStream = null;
        try {
            ResponseMessageObjectType responseBroker = getBankCertificateServiceBeanLocal
                    .getCertificateData(this.countryId, this.document);
            StatusType statusTypeBroker = responseBroker.getResponseMessage()
                    .getResponseHeader().getStatus();
            if (ConstantADM.COMMON_STRING_ZERO
                    .equals(statusTypeBroker.getStatusCode())) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getResponse();
                httpServletResponse.reset();
                httpServletResponse
                        .setContentType(ConstantADM.APPLICATION_TYPE_PDF);

                httpServletResponse.addHeader(ConstantADM.CONTENT_DISPOSITION,
                        ConstantADM.PDF_CONSTRUCTOR);

                outputStream = httpServletResponse.getOutputStream();

                getBankCertificateServiceBeanLocal.getCertificate(
                        responseBroker, this.countryId, this.document,
                        outputStream);

                FacesContext.getCurrentInstance().responseComplete();

            } else {
                logger.error(ConstantADM.ERROR_GET_PDF, null);
                messages = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        ConstantADM.ERROR_GET_PDF,
                        ConstantADM.ERROR_GET_PDF_DESC);
                FacesContext.getCurrentInstance().addMessage(null, messages);
            }

        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_GET_PDF, e);
            messages = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ConstantADM.ERROR_GET_PDF, ConstantADM.ERROR_GET_PDF_DESC);
            FacesContext.getCurrentInstance().addMessage(null, messages);
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public GetBankCertificateServiceBeanLocal getGetBankCertificateServiceBeanLocal() {
        return getBankCertificateServiceBeanLocal;
    }

    public void setGetBankCertificateServiceBeanLocal(
            GetBankCertificateServiceBeanLocal getBankCertificateServiceBeanLocal) {
        this.getBankCertificateServiceBeanLocal = getBankCertificateServiceBeanLocal;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    /**
     * @return the actionsRol
     */
    public List<ActionRol> getActionsRol() {
        return actionsRol;
    }

    /**
     * @param actionsRol
     *            the actionsRol to set
     */
    public void setActionsRol(List<ActionRol> actionsRol) {
        this.actionsRol = actionsRol;
    }

    /**
     * @return the listAllRol
     */
    public List<AwRol> getListAllRol() {
        return listAllRol;
    }

    /**
     * @param listAllRol
     *            the listAllRol to set
     */
    public void setListAllRol(List<AwRol> listAllRol) {
        this.listAllRol = listAllRol;
    }

    /**
     * @return the permissionRol
     */
    public PermissionByRol getPermissionRol() {
        return permissionRol;
    }

    /**
     * @param permissionRol
     *            the permissionRol to set
     */
    public void setPermissionRol(PermissionByRol permissionRol) {
        this.permissionRol = permissionRol;
    }

}
