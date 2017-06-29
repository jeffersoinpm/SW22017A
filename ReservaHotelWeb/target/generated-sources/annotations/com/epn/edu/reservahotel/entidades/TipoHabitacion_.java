package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Habitacion;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-29T09:44:09")
@StaticMetamodel(TipoHabitacion.class)
public class TipoHabitacion_ { 

    public static volatile SingularAttribute<TipoHabitacion, String> descripcion;
    public static volatile ListAttribute<TipoHabitacion, Habitacion> habitacionList;
    public static volatile SingularAttribute<TipoHabitacion, BigDecimal> costo;
    public static volatile SingularAttribute<TipoHabitacion, Integer> numeroCamas;
    public static volatile SingularAttribute<TipoHabitacion, Integer> idTipoHabitacion;

}