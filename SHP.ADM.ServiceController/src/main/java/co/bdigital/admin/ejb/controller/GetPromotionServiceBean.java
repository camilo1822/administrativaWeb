package co.bdigital.admin.ejb.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import co.bdigital.admin.ejb.controller.view.GetPromotionServiceBeanLocal;
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
import co.nequi.message.integration.services.getpromotiondetail.GetPromotionDetailRequestType;
import co.nequi.message.integration.services.getpromotiondetail.RulesType;
import co.nequi.message.registry.serviceregistry.RegistryRS;

/**
 * 
 * @author juan.molinab
 *
 */
@Stateless
@Local(GetPromotionServiceBeanLocal.class)
public class GetPromotionServiceBean extends CallServiceBean
        implements GetPromotionServiceBeanLocal {

    private AWProfileJPAService profileJPA;
    private AWRolJPAService rolJPA;
    private CustomLogger logger;
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
        logger = new CustomLogger(GetPromotionServiceBean.class);
        profileJPA = new AWProfileJPAServiceIMPL();
        rolJPA = new AWRolJPAServiceIMPL();
        this.entityManagerFactory = Persistence
                .createEntityManagerFactory(Constant.FRM_MANAGER);
        logger = new CustomLogger(GetPromotionServiceBean.class,
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
    public RulesType getPromotion(String service, String region, String type,
            String operation, String idPromotion, String descrptionRule) {

        GetPromotionDetailRequestType getPromotionDetailRequestType;
        RulesType rulesType = null;
        List<RulesType> listRules = null;

        try {
            this.emFRM = this.entityManagerFactory.createEntityManager();
            getPromotionDetailRequestType = WebConsoleUtil
                    .getGetPromotionDetailRequestType(service, operation, type,
                            region, idPromotion);
            IntegrationRQ integrationRQ = WebConsoleUtil.requestMessage(
                    ConstantADM.COMMON_STRING_OP_GET_SERVICE,
                    ConstantADM.COMMON_STRING_OP_GET_PROMOTION,
                    ConstantADM.COMMON_STRING_SERVICE_VERSION, region);
            integrationRQ.getIntegrationRequest().getBody()
                    .setGetPromotionDetailRequest(
                            getPromotionDetailRequestType);

            RegistryRS registryRS = serviceLocatorBean.lookup(integrationRQ,
                    ConstantADM.COMMON_STRING_SERVICE_NAME_PROM_SERVICES,
                    ConstantADM.COMMON_STRING_GET_OPERATION,
                    ConstantADM.COMMON_STRING_SERVICE_REGION_CORE,
                    ConstantADM.COMMON_STRING_SERVICE_VERSION_ONE_ZERO_ZERO,
                    emFRM);

            IntegrationRS integrationResponse = (IntegrationRS) callService(
                    integrationRQ, registryRS.getRegistryResponse().getBody()
                            .getLookupResponse(),
                    IntegrationRS.class, logger);

            listRules = integrationResponse.getIntegrationResponse().getBody()
                    .getGetPromotionDetailResponse().getRules();

            for (int i = 0; i < listRules.size(); i++) {
                if (listRules.get(i).getDescription().equals(descrptionRule)) {
                    rulesType = listRules.get(i);
                }
            }

        } catch (Exception e) {
            logger.error(ErrorEnum.DB_ERROR, Constant.ERROR_MESSAGE_DB_QUERY,
                    e);
        } finally {
            if ((null != this.emFRM) && (this.emFRM.isOpen())) {

                this.emFRM.close();
            }
        }
        return rulesType;

    }

}
