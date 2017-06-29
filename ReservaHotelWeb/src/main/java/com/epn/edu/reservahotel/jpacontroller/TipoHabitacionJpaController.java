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
import com.epn.edu.reservahotel.entidades.TipoHabitacion;
import com.epn.edu.reservahotel.jpacontrollers.exceptions.IllegalOrphanException;
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
public class TipoHabitacionJpaController implements Serializable {

    public TipoHabitacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoHabitacion tipoHabitacion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoHabitacion.getHabitacionList() == null) {
            tipoHabitacion.setHabitacionList(new ArrayList<Habitacion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Habitacion> attachedHabitacionList = new ArrayList<Habitacion>();
            for (Habitacion habitacionListHabitacionToAttach : tipoHabitacion.getHabitacionList()) {
                habitacionListHabitacionToAttach = em.getReference(habitacionListHabitacionToAttach.getClass(), habitacionListHabitacionToAttach.getIdHabitacion());
                attachedHabitacionList.add(habitacionListHabitacionToAttach);
            }
            tipoHabitacion.setHabitacionList(attachedHabitacionList);
            em.persist(tipoHabitacion);
            for (Habitacion habitacionListHabitacion : tipoHabitacion.getHabitacionList()) {
                TipoHabitacion oldIdTipoHabitacionOfHabitacionListHabitacion = habitacionListHabitacion.getIdTipoHabitacion();
                habitacionListHabitacion.setIdTipoHabitacion(tipoHabitacion);
                habitacionListHabitacion = em.merge(habitacionListHabitacion);
                if (oldIdTipoHabitacionOfHabitacionListHabitacion != null) {
                    oldIdTipoHabitacionOfHabitacionListHabitacion.getHabitacionList().remove(habitacionListHabitacion);
                    oldIdTipoHabitacionOfHabitacionListHabitacion = em.merge(oldIdTipoHabitacionOfHabitacionListHabitacion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoHabitacion(tipoHabitacion.getIdTipoHabitacion()) != null) {
                throw new PreexistingEntityException("TipoHabitacion " + tipoHabitacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoHabitacion tipoHabitacion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoHabitacion persistentTipoHabitacion = em.find(TipoHabitacion.class, tipoHabitacion.getIdTipoHabitacion());
            List<Habitacion> habitacionListOld = persistentTipoHabitacion.getHabitacionList();
            List<Habitacion> habitacionListNew = tipoHabitacion.getHabitacionList();
            List<String> illegalOrphanMessages = null;
            for (Habitacion habitacionListOldHabitacion : habitacionListOld) {
                if (!habitacionListNew.contains(habitacionListOldHabitacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Habitacion " + habitacionListOldHabitacion + " since its idTipoHabitacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Habitacion> attachedHabitacionListNew = new ArrayList<Habitacion>();
            for (Habitacion habitacionListNewHabitacionToAttach : habitacionListNew) {
                habitacionListNewHabitacionToAttach = em.getReference(habitacionListNewHabitacionToAttach.getClass(), habitacionListNewHabitacionToAttach.getIdHabitacion());
                attachedHabitacionListNew.add(habitacionListNewHabitacionToAttach);
            }
            habitacionListNew = attachedHabitacionListNew;
            tipoHabitacion.setHabitacionList(habitacionListNew);
            tipoHabitacion = em.merge(tipoHabitacion);
            for (Habitacion habitacionListNewHabitacion : habitacionListNew) {
                if (!habitacionListOld.contains(habitacionListNewHabitacion)) {
                    TipoHabitacion oldIdTipoHabitacionOfHabitacionListNewHabitacion = habitacionListNewHabitacion.getIdTipoHabitacion();
                    habitacionListNewHabitacion.setIdTipoHabitacion(tipoHabitacion);
                    habitacionListNewHabitacion = em.merge(habitacionListNewHabitacion);
                    if (oldIdTipoHabitacionOfHabitacionListNewHabitacion != null && !oldIdTipoHabitacionOfHabitacionListNewHabitacion.equals(tipoHabitacion)) {
                        oldIdTipoHabitacionOfHabitacionListNewHabitacion.getHabitacionList().remove(habitacionListNewHabitacion);
                        oldIdTipoHabitacionOfHabitacionListNewHabitacion = em.merge(oldIdTipoHabitacionOfHabitacionListNewHabitacion);
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
                Integer id = tipoHabitacion.getIdTipoHabitacion();
                if (findTipoHabitacion(id) == null) {
                    throw new NonexistentEntityException("The tipoHabitacion with id " + id + " no longer exists.");
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
            TipoHabitacion tipoHabitacion;
            try {
                tipoHabitacion = em.getReference(TipoHabitacion.class, id);
                tipoHabitacion.getIdTipoHabitacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoHabitacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Habitacion> habitacionListOrphanCheck = tipoHabitacion.getHabitacionList();
            for (Habitacion habitacionListOrphanCheckHabitacion : habitacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoHabitacion (" + tipoHabitacion + ") cannot be destroyed since the Habitacion " + habitacionListOrphanCheckHabitacion + " in its habitacionList field has a non-nullable idTipoHabitacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoHabitacion);
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

    public List<TipoHabitacion> findTipoHabitacionEntities() {
        return findTipoHabitacionEntities(true, -1, -1);
    }

    public List<TipoHabitacion> findTipoHabitacionEntities(int maxResults, int firstResult) {
        return findTipoHabitacionEntities(false, maxResults, firstResult);
    }

    private List<TipoHabitacion> findTipoHabitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoHabitacion.class));
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

    public TipoHabitacion findTipoHabitacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoHabitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoHabitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoHabitacion> rt = cq.from(TipoHabitacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
