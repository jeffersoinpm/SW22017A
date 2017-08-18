package com.epn.edu.reservahotelweb;

import com.epn.edu.reservahotel.clientesServRest.HabitacionFacadeRest;
import com.epn.edu.reservahotel.clientesServRest.ReHabitacionFacadeRest;
import com.epn.edu.reservahotel.clientesServRest.ReservaFacadeRest;
import com.epn.edu.reservahotel.clientesServRest.ServicioFacadeRest;
import com.epn.edu.reservahotel.clientesServRest.TipoHabitacionFacadeRest;
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.entidades.Reserva;
import com.epn.edu.reservahotel.entidades.TipoHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.Servicio;
import com.epn.edu.reservahotel.entidades.ReHabitacionPK;
import com.epn.edu.reservahotel.entidades.Usuario;
import com.epn.edu.reservahotel.jpacontroller.ReHabitacionJpaController;
import com.epn.edu.reservahotel.jpacontroller.ReservaJpaController;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.RollbackFailureException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Jefferson Paredes
 */
@ViewScoped
@ManagedBean(name = "tipoHabitacionView")
public class TipoHabitacionView implements Serializable {

    private List<TipoHabitacion> listTipoHbitacion;
    private List<Habitacion> lstHabitacion;
    private String selectedTipoHabitacion;
    private Date selectedFechaInicio;
    private String fechasSeleccionadas;
    private ReservaFacadeRest reservaJpaController;
    private Usuario usuarioSelected;
    private Servicio servicioSelected;
    private Habitacion selectedHabitacion;
    private Integer numeroPersonas;
    private boolean servicioParqueadero;
    private boolean servicioDesayuno;
    @ManagedProperty("#{userloginView}")
    UserLoginView loginView;

    private Date selectedFechaFin;
    private Date fechaActual;
    private String cabeceraTabla;
    private List<Habitacion> selectedHabitaciones;
    private SimpleDateFormat dateFormat;
    private BigDecimal costoTotal;
    private List<ReHabitacion> listReHabitacion;
    private List<Servicio> listServicio;

    List<Integer> lstIdHabitacionesDisponibles;
    TipoHabitacionFacadeRest tipoHabitacionJpaController;
    ReHabitacionFacadeRest reHabitacionJpaController;
    HabitacionFacadeRest habitacionJpaController;
    ServicioFacadeRest servicioJpaController;
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;

    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.setDateFormat("yyyy-MM-dd").create();

    public boolean isServicioParqueadero() {
        return servicioParqueadero;
    }

    public void setServicioParqueadero(boolean servicioParqueadero) {
        this.servicioParqueadero = servicioParqueadero;
    }

    public boolean isServicioDesayuno() {
        return servicioDesayuno;
    }

    public void setServicioDesayuno(boolean servicioDesayuno) {
        this.servicioDesayuno = servicioDesayuno;
    }

    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public Habitacion getSelectedHabitacion() {
        return selectedHabitacion;
    }

    public void setSelectedHabitacion(Habitacion selectedHabitacion) {
        this.selectedHabitacion = selectedHabitacion;
    }

    public Servicio getServicioSelected() {
        return servicioSelected;
    }

    public void setServicioSelected(Servicio servicioSelected) {
        this.servicioSelected = servicioSelected;
    }

    public String getFechasSeleccionadas() {
        return fechasSeleccionadas;
    }

    public void setFechasSeleccionadas(String fechasSeleccionadas) {
        this.fechasSeleccionadas = fechasSeleccionadas;
    }

    public List<Servicio> getListServicio() {
        return listServicio;
    }

    public void setListServicio(List<Servicio> listServicio) {
        this.listServicio = listServicio;
    }

    public List<ReHabitacion> getListReHabitacion() {
        return listReHabitacion;
    }

    public void setListReHabitacion(List<ReHabitacion> listReHabitacion) {
        this.listReHabitacion = listReHabitacion;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public TipoHabitacionView() {
    }

    @PostConstruct
    public void init() {
        //llenar cosas al iniciar

        tipoHabitacionJpaController = new TipoHabitacionFacadeRest();
        habitacionJpaController = new HabitacionFacadeRest();
        reHabitacionJpaController = new ReHabitacionFacadeRest();
        String tipoDeHabitaciones = tipoHabitacionJpaController.findAll_JSON(String.class);
        listTipoHbitacion = gson.fromJson(tipoDeHabitaciones, new TypeToken<List<TipoHabitacion>>() {
        }.getType());
        fechaActual = Calendar.getInstance().getTime();
        reservaJpaController = new ReservaFacadeRest();
        servicioJpaController = new ServicioFacadeRest();

//        lstIdHabitacionesDisponibles = reHabitacionJpaController.findIdHabitacionByFecha(fechaActual);
//        Integer[] ids = new Integer[lstIdHabitacionesDisponibles.size()];
//        for (Integer idHabitacionesDisponible : lstIdHabitacionesDisponibles) {
//            ids[lstIdHabitacionesDisponibles.indexOf(idHabitacionesDisponible)] = idHabitacionesDisponible;
//        }
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        cabeceraTabla = "Habitaciones disponibles para el " + dateFormat.format(fechaActual);
        String listaHabitacion = habitacionJpaController.findHabitacionesDisponiblesUnDia_JSON(String.class, dateFormat.format(fechaActual));
        lstHabitacion = gson.fromJson(listaHabitacion, new TypeToken<List<Habitacion>>() {
        }.getType());

        //     System.out.println("seleccionado:"+ selectedTipoHabitacion.getIdTipoHabitacion());
    }

    public void onDateSelect(SelectEvent event) {
        onChangeTipoHabitacionSelect();
    }

    public void onChangeTipoHabitacionSelect() {
        // System.out.println("entro, tipo habitacionseleccionada:"+selectedTipoHabitacion+" Inicio: "+selectedFechaInicio+" FIn: "+selectedFechaFin+"evento :"+event.getObject().toString());
        if (selectedTipoHabitacion != null && selectedFechaInicio == null && selectedFechaFin == null) {
            System.out.println("selec" + selectedTipoHabitacion);
            String listaHabitaciones = habitacionJpaController.findHabitacionbyTypeHabitacionId_JSON(String.class, "" + Integer.parseInt(selectedTipoHabitacion));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(fechaActual);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles para el " + dateFormat.format(fechaActual);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion != null && selectedFechaInicio != null && selectedFechaFin == null) {
            System.out.println("entro");
            String listaHabitaciones = habitacionJpaController.findHabitacionesDisponiblesUnDiaAndTipoHabitacion_JSON(String.class, dateFormat.format(selectedFechaInicio), "" + Integer.parseInt(selectedTipoHabitacion));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles para el " + dateFormat.format(selectedFechaInicio);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion != null && selectedFechaInicio != null && selectedFechaFin != null) {
            String listaHabitaciones = habitacionJpaController.findHabitacionesDisponiblesRangoDiasAndTipoHabitacion_JSON(String.class, dateFormat.format(selectedFechaInicio), dateFormat.format(selectedFechaFin), "" + Integer.parseInt(selectedTipoHabitacion));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones " + lstHabitacion.get(0).getIdTipoHabitacion().getDescripcion() + " disponibles del " + dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else if (selectedTipoHabitacion == null && selectedFechaInicio != null && selectedFechaFin != null) {
            System.out.println("fecha inicio y fin");
            String listaHabitaciones = habitacionJpaController.findHabitacionesDisponiblesRangoDias_JSON(String.class, dateFormat.format(selectedFechaInicio), dateFormat.format(selectedFechaFin));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones disponibles del " + dateFormat.format(selectedFechaInicio) + " al " + dateFormat.format(selectedFechaFin);
            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());

        } else if (selectedTipoHabitacion == null && selectedFechaInicio != null && selectedFechaFin == null) {
            System.out.println("solo inicio para :" + selectedFechaInicio);
            String listaHabitaciones = habitacionJpaController.findHabitacionesDisponiblesUnDia_JSON(String.class, dateFormat.format(selectedFechaInicio));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(selectedFechaInicio);
            if (!lstHabitacion.isEmpty()) {
                cabeceraTabla = "Habitaciones disponibles para el " + dateFormat.format(selectedFechaInicio);

            } else {
                cabeceraTabla = "No existen habitaciones disponibles para estos filtros";
            }
            System.out.println("Tamaño lista " + lstHabitacion.size());
        } else {
            String listaHabitaciones = habitacionJpaController.findHabitacionesDisponiblesUnDia_JSON(String.class, dateFormat.format(fechaActual));
            lstHabitacion = gson.fromJson(listaHabitaciones, new TypeToken<List<Habitacion>>() {
            }.getType());
            fechasSeleccionadas = dateFormat.format(fechaActual);
        }
    }

    public Date fechaMaximaReserva() {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fechaActual); // Configuramos la fecha que se recibe

        calendar.add(Calendar.YEAR, 1);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getCabeceraTabla() {
        return cabeceraTabla;
    }

    public void setCabeceraTabla(String cabeceraTabla) {
        this.cabeceraTabla = cabeceraTabla;
    }

    public void setSelectedHabitaciones(List<Habitacion> selectedHabitaciones) {
        costoTotal = BigDecimal.ZERO;
        for (Habitacion habitacion : selectedHabitaciones) {
            BigDecimal valorHabitacion = habitacion.getIdTipoHabitacion().getCosto();
            BigDecimal valorExtras = habitacion.getIdExtraHabitacion().getCostoTotal();
            costoTotal = costoTotal.add(valorExtras).add(valorHabitacion);
        }

        this.selectedHabitaciones = selectedHabitaciones;
    }

    public void registroRerservaHabitacion() throws RollbackFailureException, Exception {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        listReHabitacion = new ArrayList();
        if (selectedFechaInicio == null) {
            c1.setTime(fechaActual);
        } else {
            c1.setTime(selectedFechaInicio);
        }
        if (selectedFechaFin == null) {
            c2.setTime(c1.getTime());
            c2.add(Calendar.DATE, 1);
        } else {
            c2.setTime(selectedFechaFin);
        }
        if (servicioDesayuno && servicioParqueadero) {
            String servicio = servicioJpaController.find_JSON(String.class, "" + 1);
            servicioSelected = gson.fromJson(servicio, new TypeToken<Servicio>() {
            }.getType());
        } else if (!servicioDesayuno && servicioParqueadero) {
            String servicio = servicioJpaController.find_JSON(String.class, "" + 1);
            servicioSelected = gson.fromJson(servicio, new TypeToken<Servicio>() {
            }.getType());
        } else if (servicioDesayuno && !servicioParqueadero) {
            String servicio = servicioJpaController.find_JSON(String.class, "" + 1);
            servicioSelected = gson.fromJson(servicio, new TypeToken<Servicio>() {
            }.getType());
        } else {
            String servicio = servicioJpaController.find_JSON(String.class, "" + 1);
            servicioSelected = gson.fromJson(servicio, new TypeToken<Servicio>() {
            }.getType());
        }
        this.costoTotal = this.costoTotal.add(servicioSelected.getCostoTotal());
        Reserva reserva;

        reserva = new Reserva();
        System.out.println("Dato" + loginView.getIdUsuario());
        usuarioSelected = new Usuario(loginView.getIdUsuario());

        //reserva.setIdReserva();
        reserva.setIdUsuario(usuarioSelected);
        reserva.setIdServicio(servicioSelected);
        reserva.setFechaInicio(c1.getTime());
        reserva.setFechaFin(c2.getTime());
        reserva.setCostoTotal(costoTotal);
        reserva.setNumeroPersonas(this.numeroPersonas);
        System.out.println("Habitaciones seleccionadas:" + this.selectedHabitaciones.size());
        for (Habitacion Habitacion : this.selectedHabitaciones) {

            while (!c1.after(c2)) {
                ReHabitacionPK reHabitacionPk = new ReHabitacionPK();
                reHabitacionPk.setFechaReservaHabitacion(c1.getTime());
                reHabitacionPk.setIdHabitacion(Habitacion.getIdHabitacion());
                ReHabitacion reHabitacion = new ReHabitacion();
                reHabitacion.setReHabitacionPK(reHabitacionPk);
                reHabitacion.setHabitacion(Habitacion);
                reHabitacion.setIdReserva(reserva);
                listReHabitacion.add(reHabitacion);
                c1.add(Calendar.DAY_OF_MONTH, 1);
            }
            c1.setTime(reserva.getFechaInicio());
        }
        reserva.setReHabitacionList(listReHabitacion);
        System.out.println(listReHabitacion.size());
        ReservaJpaController jpaController=new ReservaJpaController(utx, emf);
        ReHabitacionJpaController jpaController1=new ReHabitacionJpaController(utx, emf);
        jpaController.create(reserva);
        System.out.println("Reserva luego de guardar:" + reserva);
        for (ReHabitacion reHabitacion1 : listReHabitacion) {
            jpaController1.create(reHabitacion1);
        }
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

    }

    public UserLoginView getLoginView() {
        return loginView;
    }

    public void setLoginView(UserLoginView loginView) {
        this.loginView = loginView;
    }

    public List<Habitacion> getSelectedHabitaciones() {
        return selectedHabitaciones;
    }

    public List<Habitacion> getLstHabitacion() {
        return lstHabitacion;
    }

    public void setLstHabitacion(List<Habitacion> lstHabitacion) {
        this.lstHabitacion = lstHabitacion;
    }

    public String getSelectedTipoHabitacion() {
        return selectedTipoHabitacion;
    }

    public Date getSelectedFechaFin() {
        return selectedFechaFin;
    }

    public void setSelectedFechaFin(Date selectedFechaFin) {
        this.selectedFechaFin = selectedFechaFin;
    }

    public Date getSelectedFechaInicio() {
        return selectedFechaInicio;
    }

    public void setSelectedFechaInicio(Date selectedFechaInicio) {
        this.selectedFechaInicio = selectedFechaInicio;
    }

    public void setSelectedTipoHabitacion(String selectedTipoHabitacion) {
        this.selectedTipoHabitacion = selectedTipoHabitacion;
    }

    public List<TipoHabitacion> getListTipoHbitacion() {
        return listTipoHbitacion;
    }

    public void setListTipoHbitacion(List<TipoHabitacion> listTipoHbitacion) {
        this.listTipoHbitacion = listTipoHbitacion;
    }

}
