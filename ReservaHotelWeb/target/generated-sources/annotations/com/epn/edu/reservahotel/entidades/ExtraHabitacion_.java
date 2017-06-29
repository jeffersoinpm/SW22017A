package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.Habitacion;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-28T20:58:43")
@StaticMetamodel(ExtraHabitacion.class)
public class ExtraHabitacion_ { 

    public static volatile ListAttribute<ExtraHabitacion, Habitacion> habitacionList;
    public static volatile SingularAttribute<ExtraHabitacion, Boolean> jacuzzi;
    public static volatile SingularAttribute<ExtraHabitacion, Integer> idExtraHabitacion;
    public static volatile SingularAttribute<ExtraHabitacion, Boolean> vistaAlMar;
    public static volatile SingularAttribute<ExtraHabitacion, BigDecimal> costoTotal;

}