package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Perfil;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-29T13:43:22")
@StaticMetamodel(Menu.class)
public class Menu_ { 

    public static volatile SingularAttribute<Menu, Integer> idMenu;
    public static volatile SingularAttribute<Menu, String> descMenu;
    public static volatile ListAttribute<Menu, Perfil> perfilList;

}