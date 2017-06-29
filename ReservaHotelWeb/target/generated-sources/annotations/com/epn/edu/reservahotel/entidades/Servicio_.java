package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Reserva;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-28T20:58:42")
@StaticMetamodel(Servicio.class)
public class Servicio_ { 

    public static volatile ListAttribute<Servicio, Reserva> reservaList;
    public static volatile SingularAttribute<Servicio, Boolean> parqueadero;
    public static volatile SingularAttribute<Servicio, BigDecimal> costoTotal;
    public static volatile SingularAttribute<Servicio, Integer> idServicio;
    public static volatile SingularAttribute<Servicio, Boolean> desayuno;

}