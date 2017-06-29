package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.ExtraHabitacion;
import com.epn.edu.reservahotel.entidades.HaHuesped;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.TipoHabitacion;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-29T13:43:22")
@StaticMetamodel(Habitacion.class)
public class Habitacion_ { 

    public static volatile SingularAttribute<Habitacion, Integer> numeroPiso;
    public static volatile ListAttribute<Habitacion, HaHuesped> haHuespedList;
    public static volatile SingularAttribute<Habitacion, ExtraHabitacion> idExtraHabitacion;
    public static volatile SingularAttribute<Habitacion, Integer> idHabitacion;
    public static volatile ListAttribute<Habitacion, ReHabitacion> reHabitacionList;
    public static volatile SingularAttribute<Habitacion, TipoHabitacion> idTipoHabitacion;

}