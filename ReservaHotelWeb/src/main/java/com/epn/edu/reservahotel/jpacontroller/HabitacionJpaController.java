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
import com.epn.edu.reservahotel.entidades.ExtraHabitacion;
import com.epn.edu.reservahotel.entidades.TipoHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import java.util.ArrayList;
import java.util.List;
import com.epn.edu.reservahotel.entidades.HaHuesped;
import com.epn.edu.reservahotel.entidades.Habitacion;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.IllegalOrphanException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.NonexistentEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.PreexistingEntityException;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.RollbackFailureException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Daniela Ramos
 */
public class HabitacionJpaController implements Serializable {

    public HabitacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Habitacion habitacion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (habitacion.getReHabitacionList() == null) {
            habitacion.setReHabitacionList(new ArrayList<ReHabitacion>());
        }
        if (habitacion.getHaHuespedList() == null) {
            habitacion.setHaHuespedList(new ArrayList<HaHuesped>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ExtraHabitacion idExtraHabitacion = habitacion.getIdExtraHabitacion();
            if (idExtraHabitacion != null) {
                idExtraHabitacion = em.getReference(idExtraHabitacion.getClass(), idExtraHabitacion.getIdExtraHabitacion());
                habitacion.setIdExtraHabitacion(idExtraHabitacion);
            }
            TipoHabitacion idTipoHabitacion = habitacion.getIdTipoHabitacion();
            if (idTipoHabitacion != null) {
                idTipoHabitacion = em.getReference(idTipoHabitacion.getClass(), idTipoHabitacion.getIdTipoHabitacion());
                habitacion.setIdTipoHabitacion(idTipoHabitacion);
            }
            List<ReHabitacion> attachedReHabitacionList = new ArrayList<ReHabitacion>();
            for (ReHabitacion reHabitacionListReHabitacionToAttach : habitacion.getReHabitacionList()) {
                reHabitacionListReHabitacionToAttach = em.getReference(reHabitacionListReHabitacionToAttach.getClass(), reHabitacionListReHabitacionToAttach.getReHabitacionPK());
                attachedReHabitacionList.add(reHabitacionListReHabitacionToAttach);
            }
            habitacion.setReHabitacionList(attachedReHabitacionList);
            List<HaHuesped> attachedHaHuespedList = new ArrayList<HaHuesped>();
            for (HaHuesped haHuespedListHaHuespedToAttach : habitacion.getHaHuespedList()) {
                haHuespedListHaHuespedToAttach = em.getReference(haHuespedListHaHuespedToAttach.getClass(), haHuespedListHaHuespedToAttach.getHaHuespedPK());
                attachedHaHuespedList.add(haHuespedListHaHuespedToAttach);
            }
            habitacion.setHaHuespedList(attachedHaHuespedList);
            em.persist(habitacion);
            if (idExtraHabitacion != null) {
                idExtraHabitacion.getHabitacionList().add(habitacion);
                idExtraHabitacion = em.merge(idExtraHabitacion);
            }
            if (idTipoHabitacion != null) {
                idTipoHabitacion.getHabitacionList().add(habitacion);
                idTipoHabitacion = em.merge(idTipoHabitacion);
            }
            for (ReHabitacion reHabitacionListReHabitacion : habitacion.getReHabitacionList()) {
                Habitacion oldHabitacionOfReHabitacionListReHabitacion = reHabitacionListReHabitacion.getHabitacion();
                reHabitacionListReHabitacion.setHabitacion(habitacion);
                reHabitacionListReHabitacion = em.merge(reHabitacionListReHabitacion);
                if (oldHabitacionOfReHabitacionListReHabitacion != null) {
                    oldHabitacionOfReHabitacionListReHabitacion.getReHabitacionList().remove(reHabitacionListReHabitacion);
                    oldHabitacionOfReHabitacionListReHabitacion = em.merge(oldHabitacionOfReHabitacionListReHabitacion);
                }
            }
            for (HaHuesped haHuespedListHaHuesped : habitacion.getHaHuespedList()) {
                Habitacion oldHabitacionOfHaHuespedListHaHuesped = haHuespedListHaHuesped.getHabitacion();
                haHuespedListHaHuesped.setHabitacion(habitacion);
                haHuespedListHaHuesped = em.merge(haHuespedListHaHuesped);
                if (oldHabitacionOfHaHuespedListHaHuesped != null) {
                    oldHabitacionOfHaHuespedListHaHuesped.getHaHuespedList().remove(haHuespedListHaHuesped);
                    oldHabitacionOfHaHuespedListHaHuesped = em.merge(oldHabitacionOfHaHuespedListHaHuesped);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHabitacion(habitacion.getIdHabitacion()) != null) {
                throw new PreexistingEntityException("Habitacion " + habitacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Habitacion habitacion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Habitacion persistentHabitacion = em.find(Habitacion.class, habitacion.getIdHabitacion());
            ExtraHabitacion idExtraHabitacionOld = persistentHabitacion.getIdExtraHabitacion();
            ExtraHabitacion idExtraHabitacionNew = habitacion.getIdExtraHabitacion();
            TipoHabitacion idTipoHabitacionOld = persistentHabitacion.getIdTipoHabitacion();
            TipoHabitacion idTipoHabitacionNew = habitacion.getIdTipoHabitacion();
            List<ReHabitacion> reHabitacionListOld = persistentHabitacion.getReHabitacionList();
            List<ReHabitacion> reHabitacionListNew = habitacion.getReHabitacionList();
            List<HaHuesped> haHuespedListOld = persistentHabitacion.getHaHuespedList();
            List<HaHuesped> haHuespedListNew = habitacion.getHaHuespedList();
            List<String> illegalOrphanMessages = null;
            for (ReHabitacion reHabitacionListOldReHabitacion : reHabitacionListOld) {
                if (!reHabitacionListNew.contains(reHabitacionListOldReHabitacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReHabitacion " + reHabitacionListOldReHabitacion + " since its habitacion field is not nullable.");
                }
            }
            for (HaHuesped haHuespedListOldHaHuesped : haHuespedListOld) {
                if (!haHuespedListNew.contains(haHuespedListOldHaHuesped)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HaHuesped " + haHuespedListOldHaHuesped + " since its habitacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idExtraHabitacionNew != null) {
                idExtraHabitacionNew = em.getReference(idExtraHabitacionNew.getClass(), idExtraHabitacionNew.getIdExtraHabitacion());
                habitacion.setIdExtraHabitacion(idExtraHabitacionNew);
            }
            if (idTipoHabitacionNew != null) {
                idTipoHabitacionNew = em.getReference(idTipoHabitacionNew.getClass(), idTipoHabitacionNew.getIdTipoHabitacion());
                habitacion.setIdTipoHabitacion(idTipoHabitacionNew);
            }
            List<ReHabitacion> attachedReHabitacionListNew = new ArrayList<ReHabitacion>();
            for (ReHabitacion reHabitacionListNewReHabitacionToAttach : reHabitacionListNew) {
                reHabitacionListNewReHabitacionToAttach = em.getReference(reHabitacionListNewReHabitacionToAttach.getClass(), reHabitacionListNewReHabitacionToAttach.getReHabitacionPK());
                attachedReHabitacionListNew.add(reHabitacionListNewReHabitacionToAttach);
            }
            reHabitacionListNew = attachedReHabitacionListNew;
            habitacion.setReHabitacionList(reHabitacionListNew);
            List<HaHuesped> attachedHaHuespedListNew = new ArrayList<HaHuesped>();
            for (HaHuesped haHuespedListNewHaHuespedToAttach : haHuespedListNew) {
                haHuespedListNewHaHuespedToAttach = em.getReference(haHuespedListNewHaHuespedToAttach.getClass(), haHuespedListNewHaHuespedToAttach.getHaHuespedPK());
                attachedHaHuespedListNew.add(haHuespedListNewHaHuespedToAttach);
            }
            haHuespedListNew = attachedHaHuespedListNew;
            habitacion.setHaHuespedList(haHuespedListNew);
            habitacion = em.merge(habitacion);
            if (idExtraHabitacionOld != null && !idExtraHabitacionOld.equals(idExtraHabitacionNew)) {
                idExtraHabitacionOld.getHabitacionList().remove(habitacion);
                idExtraHabitacionOld = em.merge(idExtraHabitacionOld);
            }
            if (idExtraHabitacionNew != null && !idExtraHabitacionNew.equals(idExtraHabitacionOld)) {
                idExtraHabitacionNew.getHabitacionList().add(habitacion);
                idExtraHabitacionNew = em.merge(idExtraHabitacionNew);
            }
            if (idTipoHabitacionOld != null && !idTipoHabitacionOld.equals(idTipoHabitacionNew)) {
                idTipoHabitacionOld.getHabitacionList().remove(habitacion);
                idTipoHabitacionOld = em.merge(idTipoHabitacionOld);
            }
            if (idTipoHabitacionNew != null && !idTipoHabitacionNew.equals(idTipoHabitacionOld)) {
                idTipoHabitacionNew.getHabitacionList().add(habitacion);
                idTipoHabitacionNew = em.merge(idTipoHabitacionNew);
            }
            for (ReHabitacion reHabitacionListNewReHabitacion : reHabitacionListNew) {
                if (!reHabitacionListOld.contains(reHabitacionListNewReHabitacion)) {
                    Habitacion oldHabitacionOfReHabitacionListNewReHabitacion = reHabitacionListNewReHabitacion.getHabitacion();
                    reHabitacionListNewReHabitacion.setHabitacion(habitacion);
                    reHabitacionListNewReHabitacion = em.merge(reHabitacionListNewReHabitacion);
                    if (oldHabitacionOfReHabitacionListNewReHabitacion != null && !oldHabitacionOfReHabitacionListNewReHabitacion.equals(habitacion)) {
                        oldHabitacionOfReHabitacionListNewReHabitacion.getReHabitacionList().remove(reHabitacionListNewReHabitacion);
                        oldHabitacionOfReHabitacionListNewReHabitacion = em.merge(oldHabitacionOfReHabitacionListNewReHabitacion);
                    }
                }
            }
            for (HaHuesped haHuespedListNewHaHuesped : haHuespedListNew) {
                if (!haHuespedListOld.contains(haHuespedListNewHaHuesped)) {
                    Habitacion oldHabitacionOfHaHuespedListNewHaHuesped = haHuespedListNewHaHuesped.getHabitacion();
                    haHuespedListNewHaHuesped.setHabitacion(habitacion);
                    haHuespedListNewHaHuesped = em.merge(haHuespedListNewHaHuesped);
                    if (oldHabitacionOfHaHuespedListNewHaHuesped != null && !oldHabitacionOfHaHuespedListNewHaHuesped.equals(habitacion)) {
                        oldHabitacionOfHaHuespedListNewHaHuesped.getHaHuespedList().remove(haHuespedListNewHaHuesped);
                        oldHabitacionOfHaHuespedListNewHaHuesped = em.merge(oldHabitacionOfHaHuespedListNewHaHuesped);
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
                Integer id = habitacion.getIdHabitacion();
                if (findHabitacion(id) == null) {
                    throw new NonexistentEntityException("The habitacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Habitacion habitacion;
            try {
                habitacion = em.getReference(Habitacion.class, id);
                habitacion.getIdHabitacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The habitacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReHabitacion> reHabitacionListOrphanCheck = habitacion.getReHabitacionList();
            for (ReHabitacion reHabitacionListOrphanCheckReHabitacion : reHabitacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Habitacion (" + habitacion + ") cannot be destroyed since the ReHabitacion " + reHabitacionListOrphanCheckReHabitacion + " in its reHabitacionList field has a non-nullable habitacion field.");
            }
            List<HaHuesped> haHuespedListOrphanCheck = habitacion.getHaHuespedList();
            for (HaHuesped haHuespedListOrphanCheckHaHuesped : haHuespedListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Habitacion (" + habitacion + ") cannot be destroyed since the HaHuesped " + haHuespedListOrphanCheckHaHuesped + " in its haHuespedList field has a non-nullable habitacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ExtraHabitacion idExtraHabitacion = habitacion.getIdExtraHabitacion();
            if (idExtraHabitacion != null) {
                idExtraHabitacion.getHabitacionList().remove(habitacion);
                idExtraHabitacion = em.merge(idExtraHabitacion);
            }
            TipoHabitacion idTipoHabitacion = habitacion.getIdTipoHabitacion();
            if (idTipoHabitacion != null) {
                idTipoHabitacion.getHabitacionList().remove(habitacion);
                idTipoHabitacion = em.merge(idTipoHabitacion);
            }
            em.remove(habitacion);
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

    public List<Habitacion> findHabitacionEntities() {
        return findHabitacionEntities(true, -1, -1);
    }

    public List<Habitacion> findHabitacionEntities(int maxResults, int firstResult) {
        return findHabitacionEntities(false, maxResults, firstResult);
    }

    private List<Habitacion> findHabitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Habitacion.class));
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

    public Habitacion findHabitacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Habitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getHabitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Habitacion> rt = cq.from(Habitacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Habitacion> findHabitacionbyTypeHabitacionId(Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<Habitacion> results = em.createNamedQuery("Habitacion.findByIdTipoHabitacion", Habitacion.class)
                    .setParameter("idTipoHabitacion", id).getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }

    public List<Habitacion> findHabitacionesDisponiblesUnDia(Date fechaActual) {
        EntityManager em = getEntityManager();
        try {

            List<Habitacion> results = em.createNamedQuery("Habitacion.findDisponiblesByUnDia", Habitacion.class)
                    .setParameter("fechaReservaHabitacion", fechaActual).getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }

    public List<Habitacion> findHabitacionesDisponiblesUnDiaAndTipoHabitacion(Date fechaActual, Integer idTipoHabitacion) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByUnDiaAndTipoHabitacion", Habitacion.class);
            query.setParameter("fechaReservaHabitacion", fechaActual);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }

    public List<Habitacion> findHabitacionesDisponiblesRangoDias(Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDias", Habitacion.class);
            query.setParameter("fechaReservaHabitacionInicio", fechaInicio);
            query.setParameter("fechaReservaHabitacionfin", fechaFin);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }
    public List<Habitacion> findHabitacionesDisponiblesRangoDiasAndTipoHabitacion(Date fechaInicio, Date fechaFin,Integer idTipoHabitacion) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDiasAndTipoHabitacion", Habitacion.class);
            query.setParameter("fechaReservaHabitacionInicio", fechaInicio);
            query.setParameter("fechaReservaHabitacionfin", fechaFin);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }
    
}
