/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.jpacontroller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacionPK;
import com.epn.edu.reservahotel.entidades.Reserva;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.NonexistentEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.PreexistingEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Daniela Ramos
 */
public class ReHabitacionJpaController implements Serializable {

    public ReHabitacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReHabitacion reHabitacion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (reHabitacion.getReHabitacionPK() == null) {
            reHabitacion.setReHabitacionPK(new ReHabitacionPK());
        }
        reHabitacion.getReHabitacionPK().setIdReserva(reHabitacion.getReserva().getIdReserva());
        reHabitacion.getReHabitacionPK().setIdHabitacion(reHabitacion.getHabitacion().getIdHabitacion());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Habitacion habitacion = reHabitacion.getHabitacion();
            if (habitacion != null) {
                habitacion = em.getReference(habitacion.getClass(), habitacion.getIdHabitacion());
                reHabitacion.setHabitacion(habitacion);
            }
            Reserva reserva = reHabitacion.getReserva();
            if (reserva != null) {
                reserva = em.getReference(reserva.getClass(), reserva.getIdReserva());
                reHabitacion.setReserva(reserva);
            }
            em.persist(reHabitacion);
            if (habitacion != null) {
                habitacion.getReHabitacionList().add(reHabitacion);
                habitacion = em.merge(habitacion);
            }
            if (reserva != null) {
                reserva.getReHabitacionList().add(reHabitacion);
                reserva = em.merge(reserva);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReHabitacion(reHabitacion.getReHabitacionPK()) != null) {
                throw new PreexistingEntityException("ReHabitacion " + reHabitacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReHabitacion reHabitacion) throws NonexistentEntityException, RollbackFailureException, Exception {
        reHabitacion.getReHabitacionPK().setIdReserva(reHabitacion.getReserva().getIdReserva());
        reHabitacion.getReHabitacionPK().setIdHabitacion(reHabitacion.getHabitacion().getIdHabitacion());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReHabitacion persistentReHabitacion = em.find(ReHabitacion.class, reHabitacion.getReHabitacionPK());
            Habitacion habitacionOld = persistentReHabitacion.getHabitacion();
            Habitacion habitacionNew = reHabitacion.getHabitacion();
            Reserva reservaOld = persistentReHabitacion.getReserva();
            Reserva reservaNew = reHabitacion.getReserva();
            if (habitacionNew != null) {
                habitacionNew = em.getReference(habitacionNew.getClass(), habitacionNew.getIdHabitacion());
                reHabitacion.setHabitacion(habitacionNew);
            }
            if (reservaNew != null) {
                reservaNew = em.getReference(reservaNew.getClass(), reservaNew.getIdReserva());
                reHabitacion.setReserva(reservaNew);
            }
            reHabitacion = em.merge(reHabitacion);
            if (habitacionOld != null && !habitacionOld.equals(habitacionNew)) {
                habitacionOld.getReHabitacionList().remove(reHabitacion);
                habitacionOld = em.merge(habitacionOld);
            }
            if (habitacionNew != null && !habitacionNew.equals(habitacionOld)) {
                habitacionNew.getReHabitacionList().add(reHabitacion);
                habitacionNew = em.merge(habitacionNew);
            }
            if (reservaOld != null && !reservaOld.equals(reservaNew)) {
                reservaOld.getReHabitacionList().remove(reHabitacion);
                reservaOld = em.merge(reservaOld);
            }
            if (reservaNew != null && !reservaNew.equals(reservaOld)) {
                reservaNew.getReHabitacionList().add(reHabitacion);
                reservaNew = em.merge(reservaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ReHabitacionPK id = reHabitacion.getReHabitacionPK();
                if (findReHabitacion(id) == null) {
                    throw new NonexistentEntityException("The reHabitacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ReHabitacionPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReHabitacion reHabitacion;
            try {
                reHabitacion = em.getReference(ReHabitacion.class, id);
                reHabitacion.getReHabitacionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reHabitacion with id " + id + " no longer exists.", enfe);
            }
            Habitacion habitacion = reHabitacion.getHabitacion();
            if (habitacion != null) {
                habitacion.getReHabitacionList().remove(reHabitacion);
                habitacion = em.merge(habitacion);
            }
            Reserva reserva = reHabitacion.getReserva();
            if (reserva != null) {
                reserva.getReHabitacionList().remove(reHabitacion);
                reserva = em.merge(reserva);
            }
            em.remove(reHabitacion);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReHabitacion> findReHabitacionEntities() {
        return findReHabitacionEntities(true, -1, -1);
    }

    public List<ReHabitacion> findReHabitacionEntities(int maxResults, int firstResult) {
        return findReHabitacionEntities(false, maxResults, firstResult);
    }

    private List<ReHabitacion> findReHabitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReHabitacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ReHabitacion findReHabitacion(ReHabitacionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReHabitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReHabitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReHabitacion> rt = cq.from(ReHabitacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
