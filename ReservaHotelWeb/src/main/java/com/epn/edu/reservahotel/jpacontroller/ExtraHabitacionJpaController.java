/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.jpacontroller;

import com.epn.edu.reservahotel.entidades.ExtraHabitacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.NonexistentEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.PreexistingEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Daniela Ramos
 */
public class ExtraHabitacionJpaController implements Serializable {

    public ExtraHabitacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExtraHabitacion extraHabitacion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (extraHabitacion.getHabitacionList() == null) {
            extraHabitacion.setHabitacionList(new ArrayList<Habitacion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Habitacion> attachedHabitacionList = new ArrayList<Habitacion>();
            for (Habitacion habitacionListHabitacionToAttach : extraHabitacion.getHabitacionList()) {
                habitacionListHabitacionToAttach = em.getReference(habitacionListHabitacionToAttach.getClass(), habitacionListHabitacionToAttach.getIdHabitacion());
                attachedHabitacionList.add(habitacionListHabitacionToAttach);
            }
            extraHabitacion.setHabitacionList(attachedHabitacionList);
            em.persist(extraHabitacion);
            for (Habitacion habitacionListHabitacion : extraHabitacion.getHabitacionList()) {
                ExtraHabitacion oldIdExtraHabitacionOfHabitacionListHabitacion = habitacionListHabitacion.getIdExtraHabitacion();
                habitacionListHabitacion.setIdExtraHabitacion(extraHabitacion);
                habitacionListHabitacion = em.merge(habitacionListHabitacion);
                if (oldIdExtraHabitacionOfHabitacionListHabitacion != null) {
                    oldIdExtraHabitacionOfHabitacionListHabitacion.getHabitacionList().remove(habitacionListHabitacion);
                    oldIdExtraHabitacionOfHabitacionListHabitacion = em.merge(oldIdExtraHabitacionOfHabitacionListHabitacion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findExtraHabitacion(extraHabitacion.getIdExtraHabitacion()) != null) {
                throw new PreexistingEntityException("ExtraHabitacion " + extraHabitacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExtraHabitacion extraHabitacion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ExtraHabitacion persistentExtraHabitacion = em.find(ExtraHabitacion.class, extraHabitacion.getIdExtraHabitacion());
            List<Habitacion> habitacionListOld = persistentExtraHabitacion.getHabitacionList();
            List<Habitacion> habitacionListNew = extraHabitacion.getHabitacionList();
            List<Habitacion> attachedHabitacionListNew = new ArrayList<Habitacion>();
            for (Habitacion habitacionListNewHabitacionToAttach : habitacionListNew) {
                habitacionListNewHabitacionToAttach = em.getReference(habitacionListNewHabitacionToAttach.getClass(), habitacionListNewHabitacionToAttach.getIdHabitacion());
                attachedHabitacionListNew.add(habitacionListNewHabitacionToAttach);
            }
            habitacionListNew = attachedHabitacionListNew;
            extraHabitacion.setHabitacionList(habitacionListNew);
            extraHabitacion = em.merge(extraHabitacion);
            for (Habitacion habitacionListOldHabitacion : habitacionListOld) {
                if (!habitacionListNew.contains(habitacionListOldHabitacion)) {
                    habitacionListOldHabitacion.setIdExtraHabitacion(null);
                    habitacionListOldHabitacion = em.merge(habitacionListOldHabitacion);
                }
            }
            for (Habitacion habitacionListNewHabitacion : habitacionListNew) {
                if (!habitacionListOld.contains(habitacionListNewHabitacion)) {
                    ExtraHabitacion oldIdExtraHabitacionOfHabitacionListNewHabitacion = habitacionListNewHabitacion.getIdExtraHabitacion();
                    habitacionListNewHabitacion.setIdExtraHabitacion(extraHabitacion);
                    habitacionListNewHabitacion = em.merge(habitacionListNewHabitacion);
                    if (oldIdExtraHabitacionOfHabitacionListNewHabitacion != null && !oldIdExtraHabitacionOfHabitacionListNewHabitacion.equals(extraHabitacion)) {
                        oldIdExtraHabitacionOfHabitacionListNewHabitacion.getHabitacionList().remove(habitacionListNewHabitacion);
                        oldIdExtraHabitacionOfHabitacionListNewHabitacion = em.merge(oldIdExtraHabitacionOfHabitacionListNewHabitacion);
                    }
                }
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
                Integer id = extraHabitacion.getIdExtraHabitacion();
                if (findExtraHabitacion(id) == null) {
                    throw new NonexistentEntityException("The extraHabitacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ExtraHabitacion extraHabitacion;
            try {
                extraHabitacion = em.getReference(ExtraHabitacion.class, id);
                extraHabitacion.getIdExtraHabitacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The extraHabitacion with id " + id + " no longer exists.", enfe);
            }
            List<Habitacion> habitacionList = extraHabitacion.getHabitacionList();
            for (Habitacion habitacionListHabitacion : habitacionList) {
                habitacionListHabitacion.setIdExtraHabitacion(null);
                habitacionListHabitacion = em.merge(habitacionListHabitacion);
            }
            em.remove(extraHabitacion);
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

    public List<ExtraHabitacion> findExtraHabitacionEntities() {
        return findExtraHabitacionEntities(true, -1, -1);
    }

    public List<ExtraHabitacion> findExtraHabitacionEntities(int maxResults, int firstResult) {
        return findExtraHabitacionEntities(false, maxResults, firstResult);
    }

    private List<ExtraHabitacion> findExtraHabitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExtraHabitacion.class));
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

    public ExtraHabitacion findExtraHabitacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExtraHabitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getExtraHabitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExtraHabitacion> rt = cq.from(ExtraHabitacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
