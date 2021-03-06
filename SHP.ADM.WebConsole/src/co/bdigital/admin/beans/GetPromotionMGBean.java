package co.bdigital.admin.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

import co.bdigital.admin.ejb.controller.UserInfoServiceBean;
import co.bdigital.admin.ejb.controller.view.GetPromotionServiceBeanLocal;
import co.bdigital.admin.ejb.controller.view.GetRulesDescriptionServiceBeanLocal;
import co.bdigital.admin.ejb.controller.view.UpdatePromotionServiceBeanLocal;
import co.bdigital.admin.messaging.services.getrulesdescription.DescriptionType;
import co.bdigital.admin.model.ActionRol;
import co.bdigital.admin.model.PermissionByRol;
import co.bdigital.admin.util.ConstantADM;
import co.bdigital.admin.util.UtilADM;
import co.bdigital.cmm.jpa.model.AwRol;
import co.bdigital.shl.tracer.CustomLogger;
import co.nequi.message.integration.services.getpromotiondetail.RulesType;

/**
 * 
 * @author juan.arboleda
 *
 */
@ManagedBean(name = "getPromotionMGBean")
@ViewScoped
public class GetPromotionMGBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String nameService;
    /* Arreglo donde se guarda la informacion de las acciones con su id */
    private transient List<ActionRol> actionsRol = null;
    /* Usuario en sesion */
    private String userCurrent;
    /* Rol seleccionado para ser modificado */
    private AwRol selectedRol;
    /* Lista de todos los roles */
    private List<AwRol> listAllRol;
    /* Logger */
    private CustomLogger logger;
    /* Permisos de la accion */
    private PermissionByRol permissionRol;
    /* Codigo del pais */
    private String countryId;
    /** Lista de las descripciones **/
    private List<SelectItem> selectDescription;
    private List<DescriptionType> descriptionType;

    private String id;
    private String description;

    private String fromDate;
    private String toDate;
    private String fromHour;
    private String toHour;

    private Date dateFrom;
    private Date dateTo;
    private Date hourFrom;
    private Date hourTo;

    private String stringFromDate;
    private String stringToDate;
    private Date saveDateFrom;
    private Date saveDateTo;

    private Double minValue = new Double(0);
    private Double maxValue = new Double(0);
    private Double value = new Double(0);
    private String minValueTxt;
    private String maxValueTxt;
    private String valueTxt;
    private String valueType;
    private String accountingAccount;
    private Double availableValue = new Double(0);
    private Double availableValueOld;
    private String availableValueTxt;
    private Double ocurrence = new Double(0);
    private String ocurrenceTxt;
    private String status;
    private String notificationType;
    private String subject;
    private String message;
    private int position;
    private String code;
    private Double maximumBudget;
    private String maximumBudgetTxt;
    private Boolean resetCampaign;
    private Double amountToDeliver;
    private Double maximumBudgetOld;
    private Double availableValueAfterModify;
    private String statusTxt;

    /* Lista de todos las reglas */
    private RulesType rulesType;

    private transient List<RulesType> listRule = null;

    @EJB
    private transient GetPromotionServiceBeanLocal getPromotionServiceBeanLocal;
    @EJB
    private transient UpdatePromotionServiceBeanLocal updatePromotionServiceBeanLocal;
    @EJB
    private transient GetRulesDescriptionServiceBeanLocal getRulesDescriptionServiceBeanLocal;
    @EJB
    private transient UserInfoServiceBean userInfoServiceBean;

    @SuppressWarnings("unchecked")
    public GetPromotionMGBean() {
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

    @PostConstruct
    public void init() {
        this.listAllRol = getListRol(this.countryId);
        getSelect();
    }

    /**
     * Metodo llamado desde el boton de guardar perfil, el cual guarda un nuevo
     * rol
     */
    public void showPromotion() {

        FacesMessage messages = null;
        this.resetCampaign = Boolean.FALSE;
        try {
            this.descriptionType = getRulesDescriptionServiceBeanLocal
                    .getRulesDescription(countryId);
            String servicio = descriptionType.get(Integer.parseInt(nameService))
                    .getService();
            code = descriptionType.get(Integer.parseInt(nameService))
                    .getPromotionType();
            String idPromotion = descriptionType
                    .get(Integer.parseInt(nameService)).getIdPromOperacion();
            String descrptionRule = descriptionType
                    .get(Integer.parseInt(nameService)).getDescriptionRule();
            rulesType = getPromotionServiceBeanLocal.getPromotion(servicio,
                    countryId, code, ConstantADM.STRING_EMPTY, idPromotion,
                    descrptionRule);

            id = rulesType.getId();
            if (null == rulesType.getDescription()) {
                description = ConstantADM.STRING_EMPTY;
            } else {
                description = rulesType.getDescription();
            }
            stringToDate = rulesType.getToDate();
            stringFromDate = rulesType.getFromDate();
            if (null == rulesType.getMinValue() || ConstantADM.STRING_EMPTY
                    .equals(String.valueOf(rulesType.getMinValue()))) {
                minValue = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                minValue = Double.parseDouble(rulesType.getMinValue());
            }
            if (null == rulesType.getMaxValue() || ConstantADM.STRING_EMPTY
                    .equals(String.valueOf(rulesType.getMaxValue()))) {
                maxValue = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                maxValue = Double.parseDouble(rulesType.getMaxValue());
            }
            if (null == rulesType.getValue() || ConstantADM.STRING_EMPTY
                    .equals(String.valueOf(rulesType.getValue()))) {
                value = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                value = Double.parseDouble(rulesType.getValue());
            }

            valueType = rulesType.getValueType();
            if (null == rulesType.getAccountingAccount()) {
                accountingAccount = ConstantADM.STRING_EMPTY;
            } else {
                accountingAccount = rulesType.getAccountingAccount();
            }
            if (null == rulesType.getAvailableValue()
                    || ConstantADM.STRING_EMPTY.equals(
                            String.valueOf(rulesType.getAvailableValue()))) {
                availableValue = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                availableValue = Double
                        .parseDouble(rulesType.getAvailableValue());
            }
            availableValueOld = availableValue;
            
            if (null == rulesType.getMaximumBudget()
                    || ConstantADM.STRING_EMPTY.equals(
                            String.valueOf(rulesType.getMaximumBudget()))) {
                maximumBudget = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                maximumBudget = Double
                        .parseDouble(rulesType.getMaximumBudget());
            }
            maximumBudgetOld = maximumBudget;

            if (null == rulesType.getOcurrence() || ConstantADM.STRING_EMPTY
                    .equals(String.valueOf(rulesType.getOcurrence()))) {
                ocurrence = ConstantADM.COMMON_DOUBLE_ZERO;
            } else {
                ocurrence = Double.parseDouble(rulesType.getOcurrence());
            }

            status = rulesType.getStatus();
            if(Boolean.TRUE.toString().equalsIgnoreCase(status)){
                status = ConstantADM.STRING_UNO;
            } else {
                status = ConstantADM.COMMON_STRING_ZERO;
            }
                
            notificationType = rulesType.getNotificationtype();
            if (null == rulesType.getSubject()) {
                subject = ConstantADM.STRING_EMPTY;
            } else {
                subject = rulesType.getSubject();
            }
            if (null == rulesType.getMessage()) {
                message = ConstantADM.STRING_EMPTY;
            } else {
                message = rulesType.getMessage();
            }

            // Se setea date y time desde fecha completa
            SimpleDateFormat formatter = new SimpleDateFormat(
                    ConstantADM.STRING_DATE_FORMAT);

            saveDateFrom = formatter.parse(stringFromDate);
            saveDateTo = formatter.parse(stringToDate);
            
            formatter = new SimpleDateFormat(ConstantADM.STRING_FORMAT_DATE);

            fromDate = formatter.format(saveDateFrom);
            toDate = formatter.format(saveDateTo);

            formatter = new SimpleDateFormat(ConstantADM.STRING_FORMAT_TIME);

            fromHour = formatter.format(saveDateFrom);
            toHour = formatter.format(saveDateTo);
            
            //Se inicializa los valores
            dateFrom = saveDateFrom;
            dateTo = saveDateTo;
            hourFrom = saveDateFrom;
            hourTo = saveDateTo;

        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_GET_PROMOTION, e);
            messages = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ConstantADM.ERROR_GET_PROMOTION,
                    ConstantADM.ERROR_GET_PROMOTION_DESC);
            FacesContext.getCurrentInstance().addMessage(null, messages);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void savePromotion() {
        FacesMessage messages = null;
        try {
            
            this.logger.info(ConstantADM.MESSAGE_TYPE_PROMOTION+this.code);
            
            // Se valida que tipo de promocion sea bono, en este caso el minimo
            // y maximo deben quedar en cero.
            if (ConstantADM.STRING_TYPE_PROMOTION_BONO.equals(this.code)) {

                minValueTxt = null;
                maxValueTxt = null;

            } else {

                minValueTxt = String.valueOf(minValue);
                maxValueTxt = String.valueOf(maxValue);

            }
            
            valueTxt = String.valueOf(value);
            availableValue = availableValueAfterModify;
            availableValueTxt = String.valueOf(availableValue);
            ocurrenceTxt = String.valueOf(ocurrence);
            maximumBudgetTxt = String.valueOf(maximumBudget); 
            
            boolean isUpdate = updatePromotionServiceBeanLocal.updatePromotion(
                    id, description, stringFromDate, stringToDate, minValueTxt,
                    maxValueTxt, valueTxt, valueType, accountingAccount,
                    availableValueTxt, ocurrenceTxt, status, notificationType,
                    subject, message, countryId, maximumBudgetTxt);
            if (isUpdate) {
                messages = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        ConstantADM.SUCCESS_UPDATE_PROMOTION,
                        ConstantADM.SUCCESS_UPDATE_PROMOTION_DESC);
            } else {
                messages = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        ConstantADM.ERROR_UPDATE_PROMOTION,
                        ConstantADM.ERROR_UPDATE_PROMOTION_DESC);
            }
        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_UPDATE_PROMOTION, e);
            messages = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    ConstantADM.ERROR_UPDATE_PROMOTION,
                    ConstantADM.ERROR_UPDATE_PROMOTION_DESC);
        }
        //se inicializa valores con los nuevos guardados
        availableValueOld = availableValue;
        maximumBudgetOld = maximumBudget;
        
        FacesContext.getCurrentInstance().addMessage(null, messages);
        resetCampaign = Boolean.FALSE;
    }

    /**
     * Metodo para controlar las pestañas de promociones
     * 
     * @param event
     * @return
     */
    public String onFlowProcess(FlowEvent event) {
        if (ConstantADM.STRING_CONFIRMATION.equals(event.getNewStep())) {
           //inicializar valores
            availableValue = new Double(0);
            amountToDeliver = new Double(0);
            availableValueAfterModify = new Double(0);
            maximumBudgetOld = new Double(0);
            showPromotion();
        }
        return event.getNewStep();
    }

    /**
     * Metodo para controlar las pestañas en la actualizacion de promocion
     * 
     * @param event
     * @return
     * @throws Exception
     */
    public String onFlowProcessUpdate(FlowEvent event) throws Exception {
        if (ConstantADM.STRING_CONFIRMATION_UPDATE.equals(event.getNewStep())) {

            SimpleDateFormat formatter = new SimpleDateFormat(
                    ConstantADM.STRING_DATE_FORMAT);

            saveDateFrom = dateTime(dateFrom, hourFrom);
            saveDateTo = dateTime(dateTo, hourTo);

            stringFromDate = formatter.format(saveDateFrom);
            stringToDate = formatter.format(saveDateTo);

            formatter = new SimpleDateFormat(ConstantADM.STRING_FORMAT_DATE);

            fromDate = formatter.format(dateFrom);
            toDate = formatter.format(dateTo);

            formatter = new SimpleDateFormat(ConstantADM.STRING_FORMAT_TIME);

            fromHour = formatter.format(hourFrom);
            toHour = formatter.format(hourTo);
            
            if(null != status &&  ConstantADM.COMMON_NUM_ESTADO_PROMO_ACTIVO.equals(status)){
                statusTxt =ConstantADM.COMMON_STRING_ESTADO_PROMO_ACTIVO;
            } else {
                statusTxt =ConstantADM.COMMON_STRING_ESTADO_PROMO_INACTIVO;
            }
            //si se reinicia campaña
            //el valor disponible es el presupuesto máximo
            if(resetCampaign){
                availableValueAfterModify = maximumBudget;
                amountToDeliver = availableValueAfterModify / value;
            } else {
                //sino el disponible es cambia segun el disponible maximo 
                availableValue = maximumBudget - (maximumBudgetOld - availableValue);
                availableValueAfterModify = availableValue;
                amountToDeliver = availableValue / value;
            }
            
        } else if (ConstantADM.STRING_GET_PROMOTION.equals(event.getNewStep())) {
            //se reinicia el presupuesto maximo valor disponible
            availableValue = availableValueOld;
        }
        
        
        return event.getNewStep();
    }

    /**
     * Metodo para retornar la lista de roles
     * 
     * @param countryId
     * @return lista de roles
     */
    private List<AwRol> getListRol(String countryId) {
        List<AwRol> roles = new ArrayList<AwRol>();
        try {
            roles = UtilADM.getListRolSplitProfileName(
                    userInfoServiceBean.getListRol(countryId));
        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_GET_LIST_ROL, e);
        }
        return roles;
    }

    /**
     * <p>
     * Utilidad que reinicia el estado de los campos de la ventana de
     * edici&oacute;n de datos de l apromocion.
     * </p>
     */
    public void resetEditUserDialog() {
        RequestContext.getCurrentInstance()
                .reset(ConstantADM.UPDATE_PRMOTION_DIALOG);
    }

    /**
     * Metodo para devolver a la vista la descripcion de las promociones
     * 
     * @return the selectDescription
     */
    public List<SelectItem> getSelect() {
        this.selectDescription = new ArrayList<SelectItem>();
        this.descriptionType = getRulesDescriptionServiceBeanLocal
                .getRulesDescription(countryId);
        position = 0;
        for (DescriptionType descriptions : descriptionType) {

            SelectItem selectItem = new SelectItem(position,
                    descriptions.getDescriptionRule());
            this.selectDescription.add(selectItem);
            position++;
        }
        return this.selectDescription;
    }

    /**
     * @return the nameService
     */
    public String getNameService() {
        return nameService;
    }

    /**
     * @param nameService
     *            the nameService to set
     */
    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public List<RulesType> getRules() {
        return listRule;
    }

    public void setRules(List<RulesType> listRule) {
        this.listRule = listRule;
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
     * @param selectedRol
     *            the selectedRol to set
     */
    public void setSelectedRol(AwRol selectedRol) {
        this.selectedRol = selectedRol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getAccountingAccount() {
        return accountingAccount;
    }

    public void setAccountingAccount(String accountingAccount) {
        this.accountingAccount = accountingAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAvailableValue() {
        return availableValue;
    }

    public void setAvailableValue(Double availableValue) {
        this.availableValue = availableValue;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Double getOcurrence() {
        return ocurrence;
    }

    public void setOcurrence(Double ocurrence) {
        this.ocurrence = ocurrence;
    }

    public String getMinValueTxt() {
        return minValueTxt;
    }

    public void setMinValueTxt(String minValueTxt) {
        this.minValueTxt = minValueTxt;
    }

    public String getMaxValueTxt() {
        return maxValueTxt;
    }

    public void setMaxValueTxt(String maxValueTxt) {
        this.maxValueTxt = maxValueTxt;
    }

    public String getValueTxt() {
        return valueTxt;
    }

    public void setValueTxt(String valueTxt) {
        this.valueTxt = valueTxt;
    }

    public String getOcurrenceTxt() {
        return ocurrenceTxt;
    }

    public void setOcurrenceTxt(String ocurrenceTxt) {
        this.ocurrenceTxt = ocurrenceTxt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SelectItem> getSelectDescription() {
        return selectDescription;
    }

    public void setSelectDescription(List<SelectItem> selectDescription) {
        this.selectDescription = selectDescription;
    }

    public RulesType getRulesType() {
        return rulesType;
    }

    public void setRulesType(RulesType rulesType) {
        this.rulesType = rulesType;
    }

    public List<RulesType> getListRule() {
        return listRule;
    }

    public void setListRule(List<RulesType> listRule) {
        this.listRule = listRule;
    }

    /**
     * @return the maximumBudget
     */
    public Double getMaximumBudget() {
        return maximumBudget;
    }

    /**
     * @param maximumBudget
     *            the maximumBudget to set
     */
    public void setMaximumBudget(Double maximumBudget) {
        this.maximumBudget = maximumBudget;
    }

    /**
     * @return the fromHour
     */
    public String getFromHour() {
        return fromHour;
    }

    /**
     * @param fromHour
     *            the fromHour to set
     */
    public void setFromHour(String fromHour) {
        this.fromHour = fromHour;
    }

    /**
     * @return the toHour
     */
    public String getToHour() {
        return toHour;
    }

    /**
     * @param toHour
     *            the toHour to set
     */
    public void setToHour(String toHour) {
        this.toHour = toHour;
    }

    /**
     * @return the hourFrom
     */
    public Date getHourFrom() {
        return hourFrom;
    }

    /**
     * @param hourFrom
     *            the hourFrom to set
     */
    public void setHourFrom(Date hourFrom) {
        this.hourFrom = hourFrom;
    }

    /**
     * @return the hourTo
     */
    public Date getHourTo() {
        return hourTo;
    }

    /**
     * @param hourTo
     *            the hourTo to set
     */
    public void setHourTo(Date hourTo) {
        this.hourTo = hourTo;
    }

    /**
     * Método que construye el date con fecha y hora especifico
     * 
     * @param date
     * @param time
     */
    public Date dateTime(Date date, Date time) {

        Calendar aDate = Calendar.getInstance();
        aDate.setTime(date);

        Calendar aTime = Calendar.getInstance();
        aTime.setTime(time);

        Calendar aDateTime = Calendar.getInstance();
        aDateTime.set(Calendar.DAY_OF_MONTH, aDate.get(Calendar.DAY_OF_MONTH));
        aDateTime.set(Calendar.MONTH, aDate.get(Calendar.MONTH));
        aDateTime.set(Calendar.YEAR, aDate.get(Calendar.YEAR));
        aDateTime.set(Calendar.HOUR, aTime.get(Calendar.HOUR));
        aDateTime.set(Calendar.MINUTE, aTime.get(Calendar.MINUTE));
        aDateTime.set(Calendar.SECOND, aTime.get(Calendar.SECOND));

        return aDateTime.getTime();
    }

    /**
     * @return the resetCampaign
     */
    public Boolean getResetCampaign() {
        return resetCampaign;
    }

    /**
     * @param resetCampaign the resetCampaign to set
     */
    public void setResetCampaign(Boolean resetCampaign) {
        this.resetCampaign = resetCampaign;
    }

    /**
     * @return the amountToDeliver
     */
    public Double getAmountToDeliver() {
        return amountToDeliver;
    }

    /**
     * @param amountToDeliver the amountToDeliver to set
     */
    public void setAmountToDeliver(Double amountToDeliver) {
        this.amountToDeliver = amountToDeliver;
    }

    /**
     * @return the stringFromDate
     */
    public String getStringFromDate() {
        return stringFromDate;
    }

    /**
     * @param stringFromDate the stringFromDate to set
     */
    public void setStringFromDate(String stringFromDate) {
        this.stringFromDate = stringFromDate;
    }

    /**
     * @return the stringToDate
     */
    public String getStringToDate() {
        return stringToDate;
    }

    /**
     * @param stringToDate the stringToDate to set
     */
    public void setStringToDate(String stringToDate) {
        this.stringToDate = stringToDate;
    }

    /**
     * @return the availableValueAfterModify
     */
    public Double getAvailableValueAfterModify() {
        return availableValueAfterModify;
    }

    /**
     * @param availableValueAfterModify the availableValueAfterModify to set
     */
    public void setAvailableValueAfterModify(Double availableValueAfterModify) {
        this.availableValueAfterModify = availableValueAfterModify;
    }

    /**
     * @return the maximumBudgetTxt
     */
    public String getMaximumBudgetTxt() {
        return maximumBudgetTxt;
    }

    /**
     * @param maximumBudgetTxt the maximumBudgetTxt to set
     */
    public void setMaximumBudgetTxt(String maximumBudgetTxt) {
        this.maximumBudgetTxt = maximumBudgetTxt;
    }

    /**
     * @return the statusTxt
     */
    public String getStatusTxt() {
        return statusTxt;
    }

    /**
     * @param statusTxt the statusTxt to set
     */
    public void setStatusTxt(String statusTxt) {
        this.statusTxt = statusTxt;
    }

    /**
     * @return the availableValueOld
     */
    public Double getAvailableValueOld() {
        return availableValueOld;
    }

    /**
     * @param availableValueOld the availableValueOld to set
     */
    public void setAvailableValueOld(Double availableValueOld) {
        this.availableValueOld = availableValueOld;
    }
    
}
