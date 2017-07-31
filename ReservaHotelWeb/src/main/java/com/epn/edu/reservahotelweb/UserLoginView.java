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
     
    private String username;
     
    private String password;
 
    UsuarioJpaController userController;    
    
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    
    public UserLoginView(){
        
    }
    
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
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
        System.out.println(this.username + " , " + this.password);
        List<Usuario> listUsuario = userController.findUserbyUserAndPassword(username, password);
        if(listUsuario != null){
            loggedIn = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
            
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