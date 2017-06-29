package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacionPK;
import com.epn.edu.reservahotel.entidades.Reserva;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-28T20:58:42")
@StaticMetamodel(ReHabitacion.class)
public class ReHabitacion_ { 

    public static volatile SingularAttribute<ReHabitacion, ReHabitacionPK> reHabitacionPK;
    public static volatile SingularAttribute<ReHabitacion, Reserva> reserva;
    public static volatile SingularAttribute<ReHabitacion, Habitacion> habitacion;

}