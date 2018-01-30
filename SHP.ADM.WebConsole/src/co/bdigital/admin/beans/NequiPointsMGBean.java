package co.bdigital.admin.beans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import co.bdigital.admin.ejb.controller.ParametersServiceBean;
import co.bdigital.admin.model.PermissionByRol;
import co.bdigital.admin.util.ConstantADM;
import co.bdigital.admin.util.UtilADM;
import co.bdigital.cmm.jpa.model.Parametro;
import co.bdigital.shl.tracer.CustomLogger;

/**
 * Clase para la gestion de los puntos Nequi
 * 
 * 
 * @author juan.molinab
 *
 */
@ManagedBean(name = "nequiPointMGBean")
@RequestScoped
public class NequiPointsMGBean {

    /** Utilidad para almacenar la informacion de los logs **/
    private CustomLogger logger;
    /** Ubicacion de la carpeta temporal donde se suben los archivos **/
    private String realPath;
    /** Permisos de la accion **/
    private PermissionByRol permissionRol;
    /** Lista de tipos de puntos **/
    private List<SelectItem> selectOneItemPointsTypes;
    /** Codigo del pais **/
    private String countryId;
    /** Nombre tipo punto Nequi **/
    private String namePointType;
    /** columnas de csv para puntos **/
    private List<Parametro> pointsFileColumnOrder;
    private String columnsAllowed;

    @EJB
    private transient ParametersServiceBean parametersServiceBean;

    /**
     * Constructor clase NequiPointsMGBean
     */
    public NequiPointsMGBean() {
        logger = new CustomLogger(this.getClass());
        permissionRol = (PermissionByRol) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.PERMISSIONS_BY_ROL_SESION);
        this.countryId = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get(ConstantADM.COUNTRY_ID);
    }

    @PostConstruct
    public void init() {
        if (null == this.pointsFileColumnOrder
                || this.pointsFileColumnOrder.isEmpty()) {
            this.pointsFileColumnOrder = this.getListPointsFileOrder();
        }
        this.columnsAllowed = this.buildColumnsAllowedMessage();
    }

    /**
     * Metodo para cargar el documento CSV
     * 
     * @param event
     * @throws IOException
     */
    public void loadData(FileUploadEvent event) throws IOException {
        FacesMessage message;

        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        namePointType = ec.getRequestParameterMap()
                .get(ConstantADM.SELECT_VALUE_NEQUI_POINTS);

        if (null != namePointType && !namePointType.isEmpty()) {
            UploadedFile file = event.getFile();
            Date currentDate = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(currentDate);
            realPath = getRealPath(currentDate);

            try {
                this.saveFileCSV(this.realPath, file);
                if (ConstantADM.COMMON_INT_TWELVE > calendar
                        .get(Calendar.HOUR_OF_DAY)) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                            ConstantADM.WORD_SUCCESS,
                            ConstantADM.FILE_UPLOAD_SUCCESS
                                    + ConstantADM.FILE_UPLOAD_MORNING);
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                            ConstantADM.WORD_SUCCESS,
                            ConstantADM.FILE_UPLOAD_SUCCESS
                                    + ConstantADM.FILE_UPLOAD_EVENING);
                }
            } catch (IllegalArgumentException e) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        ConstantADM.WORD_ERROR, e.getMessage());
            }

        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ConstantADM.POINT_TYPE_FIELD,
                    ConstantADM.SELECT_POINT_TYPE);
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Metodo para devolver el nombre como se guardar el archivo en la ruta
     * 
     * @return
     */
    public String getRealPath(Date currentDate) {
        StringBuilder str = new StringBuilder(
                System.getProperty(ConstantADM.PROPERTY_PATH_FILE_CSV)
                        + File.separator);
        str.append(ConstantADM.NAME_NEQUI_POINTS);
        str.append(namePointType);
        str.append(ConstantADM.COMMON_STRING_DASH);
        if (ConstantADM.COMMON_STRING_REGION_COL.equalsIgnoreCase(countryId)) {
            str.append(ConstantADM.COMMON_STRING_CO);
        } else {
            str.append(ConstantADM.COMMON_STRING_PA);
        }
        str.append(ConstantADM.COMMON_STRING_DASH);
        str.append(UtilADM.getFormatedCurrentDate(currentDate));
        str.append(ConstantADM.EXTENT_CSV_FILE);
        return str.toString();
    }

    /**
     * Metodo para hacer split d ela fecha y solo concatenar el dia-mes-anio sin
     * la hora
     * 
     * @param date
     * @return
     */
    public String splitDate(String date) {
        String splitString = "";
        Pattern pattern = Pattern.compile(ConstantADM.STRING_SPLIT_POINT_DATE);
        String[] split = pattern.split(date, 0);
        if (split.length > 0) {
            splitString = split[0];
        }
        return splitString;
    }

    /**
     * Metodo para guardar temporalmente el archivo SCV
     * 
     * @param realPath
     * @param file
     */
    public void saveFileCSV(String realPath, UploadedFile file) {

        BufferedReader br = null;
        String line = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileOutputStream out = null;
        FileReader reader = null;

        try {
            bis = new BufferedInputStream(file.getInputstream());
            out = new FileOutputStream(realPath);
            bos = new BufferedOutputStream(out);
            int contador = 0;
            while ((contador = bis.read()) != -1) {
                bos.write(contador);
            }
            bos.flush();

            reader = new FileReader(realPath);
            br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {

                String[] columnData = line.split(ConstantADM.COMMA);
                if (this.pointsFileColumnOrder.size() != columnData.length) {
                    String message = ConstantADM.ERROR_INVALID_COLUMNS_UPLOAD_FILE;
                    message = message.replace(
                            ConstantADM.COMMON_STRING_REPLACE_KEY_FIRST,
                            Integer.toString(columnData.length));
                    message = message.replace(
                            ConstantADM.COMMON_STRING_REPLACE_KEY_SECOND,
                            this.columnsAllowed);
                    throw new IllegalArgumentException(message);
                } else {
                    if (!columnData[ConstantADM.COMMON_ONE]
                            .equals(ConstantADM.CODIGO_INTERNO)) {
                        if (!columnData[ConstantADM.COMMON_TEN]
                                .equals(countryId)) {
                            String message = ConstantADM.ERROR_INVALID_COUNTRY;
                            message = message.replace(
                                    ConstantADM.COMMON_STRING_REPLACE_KEY_FIRST,
                                    countryId);
                            message = message.replace(
                                    ConstantADM.COMMON_STRING_REPLACE_KEY_SECOND,
                                    columnData[ConstantADM.COMMON_ONE]);
                            throw new IllegalArgumentException(message);
                        }
                        Pattern pat = Pattern.compile(
                                ConstantADM.REGULAR_EXPRESION_LONGITUD);
                        Matcher match = pat
                                .matcher(columnData[ConstantADM.COMMON_SEVEN]);
                        if (!match.matches()) {
                            String message = ConstantADM.ERROR_INVALID_LONGITUD;
                            message = message.replace(
                                    ConstantADM.COMMON_STRING_REPLACE_KEY_FIRST,
                                    columnData[ConstantADM.COMMON_ONE]);
                            throw new IllegalArgumentException(message);
                        } else {
                            pat = Pattern.compile(
                                    ConstantADM.REGULAR_EXPRESION_LATITUD);
                            match = pat.matcher(
                                    columnData[ConstantADM.COMMON_EIGHT]);
                            if (!match.matches()) {
                                String message = ConstantADM.ERROR_INVALID_LATITUD;
                                message = message.replace(
                                        ConstantADM.COMMON_STRING_REPLACE_KEY_FIRST,
                                        columnData[ConstantADM.COMMON_ONE]);
                                throw new IllegalArgumentException(message);
                            }
                        }
                        if (!ConstantADM.STRING_EMPTY
                                .equals(columnData[ConstantADM.COMMON_NINE])) {
                            pat = Pattern.compile(
                                    ConstantADM.REGULAR_EXPRESION_ACTION);
                            match = pat.matcher(
                                    columnData[ConstantADM.COMMON_NINE]);
                            if (!match.matches()) {
                                String message = ConstantADM.ERROR_INVALID_ACTION;
                                message = message.replace(
                                        ConstantADM.COMMON_STRING_REPLACE_KEY_FIRST,
                                        columnData[ConstantADM.COMMON_ONE]);
                                throw new IllegalArgumentException(message);
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            logger.error(ConstantADM.ERROR_READ_UPLOAD_FILE, e);
            throw new IllegalArgumentException(
                    ConstantADM.ERROR_READ_UPLOAD_FILE);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(ConstantADM.ERROR_CLOSING_RESOURCES_TO_UPLOAD_FILE,
                        e);
            }
            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error(ConstantADM.ERROR_CLOSING_RESOURCES_TO_UPLOAD_FILE,
                        e);
            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                logger.error(ConstantADM.ERROR_CLOSING_RESOURCES_TO_UPLOAD_FILE,
                        e);
            }
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error(ConstantADM.ERROR_CLOSING_RESOURCES_TO_UPLOAD_FILE,
                        e);
            }
            try {
                if (null != bis) {
                    bis.close();
                }
            } catch (IOException e) {
                logger.error(ConstantADM.ERROR_CLOSING_RESOURCES_TO_UPLOAD_FILE,
                        e);
            }

        }
    }

    /**
     * Metodo para eliminar el archivo que se ha subido con anterioridad
     */
    public void deleteFile() {
        Date currentDate = new Date();
        String path = getRealPath(currentDate);
        FacesMessage message = null;
        try {
            File ruta = new File(path);
            ruta.delete();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    ConstantADM.WORD_SUCCESS, ConstantADM.FILE_DELETE_SUCCESS);
        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_READ_FILE_UPLOAD, e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    ConstantADM.WORD_ERROR, ConstantADM.FILE_DELETE_ERROR);
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    /**
     * Metodo para devolver a la vista los tipos de puntos nequi
     * 
     * @return the selectOneItemTypeId
     */
    public List<SelectItem> getSelectOneItemPointsTypes() {
        this.selectOneItemPointsTypes = new ArrayList<SelectItem>();
        List<Parametro> pointsTypes = this.pointsTypes();

        if (null != pointsTypes && !pointsTypes.isEmpty()) {
            for (Parametro documentType : pointsTypes) {
                SelectItem selectItemPointType = new SelectItem(
                        documentType.getValor(), documentType.getNombre());
                this.selectOneItemPointsTypes.add(selectItemPointType);
            }
        }
        return this.selectOneItemPointsTypes;
    }

    /**
     * Metodo para retornar la lista de tipos de puntos Nequi.
     * 
     * @return lista de tipos de documentos.
     */
    private List<Parametro> pointsTypes() {
        List<Parametro> pointsTypes = null;
        try {
            pointsTypes = this.parametersServiceBean.getRegionParameter(
                    ConstantADM.TYPE_PARAMETER_ID_POINTS_TYPES, countryId);
        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_GET_LIST_POINTS_TYPES, e);
        }
        return pointsTypes;
    }

    /**
     * @return the namePointType
     */
    public String getNamePointType() {
        return namePointType;
    }

    /**
     * @param namePointType
     *            the namePointType to set
     */
    public void setNamePointType(String namePointType) {
        this.namePointType = namePointType;
    }

    /**
     * Metodo para constriur mensaje de error cuadno numero de columnas no
     * coinciden
     * 
     * @return
     */
    private String buildColumnsAllowedMessage() {
        StringBuilder columnsMessage = new StringBuilder();
        for (int i = 0; i < this.pointsFileColumnOrder.size(); i++) {
            columnsMessage
                    .append(this.pointsFileColumnOrder.get(i).getNombre());
            if (i == this.pointsFileColumnOrder.size() - 2) {
                columnsMessage.append(ConstantADM.STRING_SPLIT_POINT_DATE);
                columnsMessage.append(ConstantADM.COMMON_STRING_AND_TRANSLATED);
                columnsMessage.append(ConstantADM.STRING_SPLIT_POINT_DATE);
            } else if (i != this.pointsFileColumnOrder.size() - 1) {
                columnsMessage.append(ConstantADM.STRING_SPLIT_POINT_DATE);
                columnsMessage.append(ConstantADM.COMMA);
                columnsMessage.append(ConstantADM.STRING_SPLIT_POINT_DATE);
            }
        }
        return columnsMessage.toString();
    }

    /**
     * Metodo para cargar el orden de las columnas del archivo CSV con los
     * puntos NEQUI.
     * 
     * @return {@link List} listado de {@link Parametro}
     */
    private List<Parametro> getListPointsFileOrder() {
        List<Parametro> parameters = new ArrayList<Parametro>();
        try {
            parameters = this.parametersServiceBean.getRegionParameter(
                    ConstantADM.TYPE_PARAMETER_NEQUI_POINTS_FILE_COLUMN_ORDER,
                    countryId);
        } catch (Exception e) {
            logger.error(ConstantADM.ERROR_LOADING_CONFIGURATION_PARAMETERS, e);
        }
        return parameters;
    }
}
