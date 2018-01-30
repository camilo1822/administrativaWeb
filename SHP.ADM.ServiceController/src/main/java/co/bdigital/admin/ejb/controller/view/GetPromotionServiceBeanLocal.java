package co.bdigital.admin.ejb.controller.view;

import co.nequi.message.integration.services.getpromotiondetail.RulesType;

public interface GetPromotionServiceBeanLocal {

    /**
     * Metodo para retornar las promociones
     * 
     * @param actionByRol
     * @return
     */
    RulesType getPromotion(String service, String region, String type,
            String operation, String idPromotion, String descrptionRule);

}
