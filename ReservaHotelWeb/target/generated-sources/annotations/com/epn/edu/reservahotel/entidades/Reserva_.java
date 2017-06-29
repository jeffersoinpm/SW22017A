package com.epn.edu.reservahotel.entidades;

import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.Servicio;
import com.epn.edu.reservahotel.entidades.Usuario;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-29T13:43:22")
@StaticMetamodel(Reserva.class)
public class Reserva_ { 

    public static volatile SingularAttribute<Reserva, Integer> numeroPersonas;
    public static volatile SingularAttribute<Reserva, Date> fechaInicio;
    public static volatile SingularAttribute<Reserva, Usuario> idUsuario;
    public static volatile SingularAttribute<Reserva, BigDecimal> costoTotal;
    public static volatile SingularAttribute<Reserva, Servicio> idServicio;
    public static volatile SingularAttribute<Reserva, Date> fechaFin;
    public static volatile ListAttribute<Reserva, ReHabitacion> reHabitacionList;
    public static volatile SingularAttribute<Reserva, Integer> idReserva;

}