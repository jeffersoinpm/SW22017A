package com.epn.edu.reservahotelweb;

import com.epn.edu.reservahotel.clientesServRest.UsuarioFacadeRest;
import com.epn.edu.reservahotel.entidades.Usuario;
import com.epn.edu.reservahotel.jpacontroller.UsuarioJpaController;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

import org.primefaces.context.RequestContext;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "userloginView")
public class UserLoginView implements Serializable {

    private String email;

    private String password;
    private Integer idUsuario;
    private String nombreUsuario;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    UsuarioJpaController userController;

    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;

    public UserLoginView() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() {
        userController = new UsuarioJpaController(utx, emf);
    }

    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
        System.out.println(this.email + " , " + this.password);
        List<Usuario> listUsuario = userController.findUserbyEmailAndPassword(email, password);
        boolean logeado = false;
        if (listUsuario != null) {
            loggedIn = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", listUsuario.get(0).getNombreUsuario());
            setIdUsuario(listUsuario.get(0).getIdUsuario());
            setNombreUsuario(listUsuario.get(0).getNombreUsuario());
            logeado = true;
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
            logeado = false;
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("estaLogeado", logeado);

        if (logeado) {
            context.addCallbackParam("view", "index.xhtml");
        }
    }
}
