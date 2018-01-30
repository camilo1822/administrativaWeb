package co.bdigital.admin.ejb.controller.view;

import java.io.OutputStream;

import co.bdigital.cmm.messaging.general.ResponseMessageObjectType;

public interface GetBankCertificateServiceBeanLocal {

    /**
     * Metodo para generar certificado bancario
     * 
     * @param region
     * @param docuemntId
     * @return
     */
    void getCertificate(ResponseMessageObjectType responseBroker, String region,
            String phone, OutputStream outputStream);

    /**
     * Metodo retorna datos de certificado bancario
     * 
     * @param region
     * @param docuemntId
     * @return
     */
    ResponseMessageObjectType getCertificateData(String region, String phone);

}
