/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.jpacontroller;

import com.epn.edu.reservahotel.entidades.HaHuesped;
import com.epn.edu.reservahotel.entidades.HaHuespedPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.entidades.Huesped;
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
public class HaHuespedJpaController implements Serializable {

    public HaHuespedJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HaHuesped haHuesped) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (haHuesped.getHaHuespedPK() == null) {
            haHuesped.setHaHuespedPK(new HaHuespedPK());
        }
        haHuesped.getHaHuespedPK().setIdHuesped(haHuesped.getHuesped().getIdHuesped());
        haHuesped.getHaHuespedPK().setIdHabitacion(haHuesped.getHabitacion().getIdHabitacion());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Habitacion habitacion = haHuesped.getHabitacion();
            if (habitacion != null) {
                habitacion = em.getReference(habitacion.getClass(), habitacion.getIdHabitacion());
                haHuesped.setHabitacion(habitacion);
            }
            Huesped huesped = haHuesped.getHuesped();
            if (huesped != null) {
                huesped = em.getReference(huesped.getClass(), huesped.getIdHuesped());
                haHuesped.setHuesped(huesped);
            }
            em.persist(haHuesped);
            if (habitacion != null) {
                habitacion.getHaHuespedList().add(haHuesped);
                habitacion = em.merge(habitacion);
            }
            if (huesped != null) {
                huesped.getHaHuespedList().add(haHuesped);
                huesped = em.merge(huesped);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHaHuesped(haHuesped.getHaHuespedPK()) != null) {
                throw new PreexistingEntityException("HaHuesped " + haHuesped + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HaHuesped haHuesped) throws NonexistentEntityException, RollbackFailureException, Exception {
        haHuesped.getHaHuespedPK().setIdHuesped(haHuesped.getHuesped().getIdHuesped());
        haHuesped.getHaHuespedPK().setIdHabitacion(haHuesped.getHabitacion().getIdHabitacion());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            HaHuesped persistentHaHuesped = em.find(HaHuesped.class, haHuesped.getHaHuespedPK());
            Habitacion habitacionOld = persistentHaHuesped.getHabitacion();
            Habitacion habitacionNew = haHuesped.getHabitacion();
            Huesped huespedOld = persistentHaHuesped.getHuesped();
            Huesped huespedNew = haHuesped.getHuesped();
            if (habitacionNew != null) {
                habitacionNew = em.getReference(habitacionNew.getClass(), habitacionNew.getIdHabitacion());
                haHuesped.setHabitacion(habitacionNew);
            }
            if (huespedNew != null) {
                huespedNew = em.getReference(huespedNew.getClass(), huespedNew.getIdHuesped());
                haHuesped.setHuesped(huespedNew);
            }
            haHuesped = em.merge(haHuesped);
            if (habitacionOld != null && !habitacionOld.equals(habitacionNew)) {
                habitacionOld.getHaHuespedList().remove(haHuesped);
                habitacionOld = em.merge(habitacionOld);
            }
            if (habitacionNew != null && !habitacionNew.equals(habitacionOld)) {
                habitacionNew.getHaHuespedList().add(haHuesped);
                habitacionNew = em.merge(habitacionNew);
            }
            if (huespedOld != null && !huespedOld.equals(huespedNew)) {
                huespedOld.getHaHuespedList().remove(haHuesped);
                huespedOld = em.merge(huespedOld);
            }
            if (huespedNew != null && !huespedNew.equals(huespedOld)) {
                huespedNew.getHaHuespedList().add(haHuesped);
                huespedNew = em.merge(huespedNew);
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
                HaHuespedPK id = haHuesped.getHaHuespedPK();
                if (findHaHuesped(id) == null) {
                    throw new NonexistentEntityException("The haHuesped with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HaHuespedPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            HaHuesped haHuesped;
            try {
                haHuesped = em.getReference(HaHuesped.class, id);
                haHuesped.getHaHuespedPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The haHuesped with id " + id + " no longer exists.", enfe);
            }
            Habitacion habitacion = haHuesped.getHabitacion();
            if (habitacion != null) {
                habitacion.getHaHuespedList().remove(haHuesped);
                habitacion = em.merge(habitacion);
            }
            Huesped huesped = haHuesped.getHuesped();
            if (huesped != null) {
                huesped.getHaHuespedList().remove(haHuesped);
                huesped = em.merge(huesped);
            }
            em.remove(haHuesped);
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

    public List<HaHuesped> findHaHuespedEntities() {
        return findHaHuespedEntities(true, -1, -1);
    }

    public List<HaHuesped> findHaHuespedEntities(int maxResults, int firstResult) {
        return findHaHuespedEntities(false, maxResults, firstResult);
    }

    private List<HaHuesped> findHaHuespedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HaHuesped.class));
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

    public HaHuesped findHaHuesped(HaHuespedPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HaHuesped.class, id);
        } finally {
            em.close();
        }
    }

    public int getHaHuespedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HaHuesped> rt = cq.from(HaHuesped.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
