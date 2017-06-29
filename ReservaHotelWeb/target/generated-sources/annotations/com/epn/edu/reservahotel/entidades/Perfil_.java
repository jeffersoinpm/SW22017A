package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Menu;
import com.epn.edu.reservahotel.entidades.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-29T09:44:09")
@StaticMetamodel(Perfil.class)
public class Perfil_ { 

    public static volatile ListAttribute<Perfil, Usuario> usuarioList;
    public static volatile SingularAttribute<Perfil, Integer> idPerfil;
    public static volatile SingularAttribute<Perfil, String> descPerfil;
    public static volatile SingularAttribute<Perfil, Menu> idMenu;

}