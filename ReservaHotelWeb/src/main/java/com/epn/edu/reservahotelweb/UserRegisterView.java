/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotelweb;

import com.epn.edu.reservahotel.entidades.Menu;
import com.epn.edu.reservahotel.entidades.Perfil;
import com.epn.edu.reservahotel.entidades.Usuario;
import com.epn.edu.reservahotel.jpacontroller.UsuarioJpaController;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.RollbackFailureException;
import static com.epn.edu.reservahotelweb.SendEmail.generateAndSendEmail;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;

/**
 *
 * @author mathcrap
 */

@ViewScoped
@ManagedBean(name = "userRegisterView")
public class UserRegisterView {
    
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    
    private String username;
    private String email;
    private String address;
    private String phone;
    private String password;
    
    UsuarioJpaController usuarioController;
    
    public UserRegisterView(){
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    @PostConstruct
    public void init(){
        usuarioController = new UsuarioJpaController(utx, emf);
    }
    
    public void register(ActionEvent event){
        if(username != null &&
            email != null &&
            address != null &&
            phone != null &&
            password != null){
            
            int cantidadUsuarios = usuarioController.getUsuarioCount();
            
            Perfil perfil = new Perfil();
            Usuario usuario = new Usuario();
            Menu menu = new Menu();
            FacesMessage message = null;
            
            menu.setIdMenu(1);
            
            perfil.setIdPerfil(1);
            perfil.setIdMenu(menu);
            
            usuario.setNombreUsuario(username);
            usuario.setCorreo(email);
            usuario.setDireccion(address);
            usuario.setTelefono(phone);
            usuario.setContrasenia(password);
            usuario.setIdPerfil(perfil);
            usuario.setIdUsuario(cantidadUsuarios + 1);
            try {
                usuarioController.create(usuario);
                System.out.println("Usuario creado");
                generateAndSendEmail(username, email);
		System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", email);
            
                try {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
                } catch (IOException ex) {
                        Logger.getLogger(UserLoginView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (RollbackFailureException ex) {
                System.err.println("Usuario no creado");
                Logger.getLogger(UserRegisterView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UserRegisterView.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
    }
}
