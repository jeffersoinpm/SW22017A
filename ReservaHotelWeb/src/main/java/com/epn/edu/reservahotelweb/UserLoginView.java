package com.epn.edu.reservahotelweb;
 
import com.epn.edu.reservahotel.entidades.Usuario;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
 
import org.primefaces.context.RequestContext;

import com.epn.edu.reservahotel.jpacontroller.UsuarioJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;


 
@ViewScoped
@ManagedBean(name = "userLoginView")
public class UserLoginView implements Serializable{
     
    private String email;
     
    private String password;
 
    UsuarioJpaController userController;    
    
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    
    public UserLoginView(){
        
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
        String nombre = userController.findUsuario(new Integer(1)).getNombreUsuario();
        System.out.println("Usuario: " + nombre);
    }
   
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
        System.out.println(this.email + " , " + this.password);
        List<Usuario> listUsuario = userController.findUserbyEmailAndPassword(email, password);
        if(listUsuario != null){
            loggedIn = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", email);
            
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            } catch (IOException ex) {
                Logger.getLogger(UserLoginView.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
        
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
    }   
}