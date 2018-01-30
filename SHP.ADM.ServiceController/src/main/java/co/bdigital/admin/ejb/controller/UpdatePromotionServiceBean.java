package co.bdigital.admin.ejb.controller;

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

import co.bdigital.admin.ejb.controller.view.UpdatePromotionServiceBeanLocal;
import co.bdigital.admin.util.CallServiceBean;
import co.bdigital.admin.util.ConstantADM;
import co.bdigital.admin.util.ServiceControllerBean;
import co.bdigital.admin.util.WebConsoleUtil;
import co.bdigital.cmm.ejb.generic.ServiceControllerHelper;
import co.bdigital.cmm.ejb.util.Constant;
import co.bdigital.cmm.jpa.service.impl.AWProfileJPAServiceIMPL;
import co.bdigital.cmm.jpa.service.impl.AWRolJPAServiceIMPL;
import co.bdigital.cmm.jpa.services.AWProfileJPAService;
import co.bdigital.cmm.jpa.services.AWRolJPAService;
import co.bdigital.shl.tracer.CustomLogger;
import co.bdigital.shl.tracer.ErrorEnum;
import co.nequi.message.integration.services.IntegrationRQ;
import co.nequi.message.integration.services.IntegrationRS;
import co.nequi.message.integration.services.updatepromotionrule.UpdatePromotionRuleRequestType;
import co.nequi.message.registry.serviceregistry.RegistryRS;

/**
 * 
 * @author juan.molinab
 *
 */
@Stateless
@Local(UpdatePromotionServiceBeanLocal.class)
public class UpdatePromotionServiceBean extends CallServiceBean
        implements UpdatePromotionServiceBeanLocal {

    @PersistenceContext(unitName = "JPAManager")
    private EntityManager em;
    private AWProfileJPAService profileJPA;
    private AWRolJPAService rolJPA;
    private static CustomLogger logger;
    @EJB
    private ServiceControllerBean serviceLocatorBean;
    private EntityManagerFactory entityManagerFactory;

    private EntityManager emFRM;

    @Resource(name = "getPromtionIsTraceable")
    private Boolean getPromtionIsTraceable;

    @Resource(name = "getPromtionIsDebugable")
    private Boolean getPromtionIsDebugable;

    private ServiceControllerHelper sch;

    /**
     * Metodo para inicializar recursos del EJB.
     */
    @PostConstruct
    void init() {
        logger = new CustomLogger(UpdatePromotionServiceBean.class);
        profileJPA = new AWProfileJPAServiceIMPL();
        rolJPA = new AWRolJPAServiceIMPL();
        this.entityManagerFactory = Persistence
                .createEntityManagerFactory(Constant.FRM_MANAGER);
        logger = new CustomLogger(UpdatePromotionServiceBean.class,
                getPromtionIsTraceable, getPromtionIsDebugable);
        this.sch = ServiceControllerHelper.getInstance();
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
    public boolean updatePromotion(String id, String description,
            String fromDate, String toDate, String minValue, String maxValue,
            String value, String valueType, String accountingAccount,
            String availableValue, String ocurrence, String status,
            String notificationType, String subject, String message,
            String region, String maximumBudget) {

        UpdatePromotionRuleRequestType UpdatePromotionRuleRequestType;

        try {
            this.emFRM = this.entityManagerFactory.createEntityManager();
            UpdatePromotionRuleRequestType = WebConsoleUtil
                    .getUpdatePromotionRuleRequestType(id, description,
                            fromDate, toDate, minValue, maxValue, value,
                            valueType, accountingAccount, availableValue,
                            ocurrence, status, notificationType, subject,
                            message, maximumBudget);

            IntegrationRQ integrationRQ = WebConsoleUtil.requestMessage(
                    ConstantADM.STRING_EMPTY, ConstantADM.STRING_EMPTY,
                    ConstantADM.STRING_EMPTY, region);
            integrationRQ.getIntegrationRequest().getBody()
                    .setUpdatePromotionRuleRequest(
                            UpdatePromotionRuleRequestType);

            RegistryRS registryRS = serviceLocatorBean.lookup(integrationRQ,
                    ConstantADM.COMMON_STRING_SERVICE_NAME_PROM_SERVICES,
                    ConstantADM.COMMON_STRING_UPDATE_OPERATION,
                    ConstantADM.COMMON_STRING_SERVICE_REGION_CORE,
                    ConstantADM.COMMON_STRING_SERVICE_VERSION_ONE_ZERO_ZERO,
                    emFRM);

            IntegrationRS integrationDaonResponse = (IntegrationRS) callService(
                    integrationRQ, registryRS.getRegistryResponse().getBody()
                            .getLookupResponse(),
                    IntegrationRS.class, logger);

            return ConstantADM.COMMON_STRING_SUCCESS_MAYUS
                    .equals(integrationDaonResponse.getIntegrationResponse()
                            .getHeader().getStatus().getDescription());

        } catch (Exception e) {
            logger.error(ErrorEnum.DB_ERROR, Constant.ERROR_MESSAGE_DB_QUERY,
                    e);
        }
        return false;

    }

}
