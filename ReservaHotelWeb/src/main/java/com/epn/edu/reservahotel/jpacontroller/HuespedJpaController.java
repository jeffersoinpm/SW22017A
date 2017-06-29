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
import com.epn.edu.reservahotel.entidades.HaHuesped;
import com.epn.edu.reservahotel.entidades.Huesped;
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
public class HuespedJpaController implements Serializable {

    public HuespedJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Huesped huesped) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (huesped.getHaHuespedList() == null) {
            huesped.setHaHuespedList(new ArrayList<HaHuesped>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<HaHuesped> attachedHaHuespedList = new ArrayList<HaHuesped>();
            for (HaHuesped haHuespedListHaHuespedToAttach : huesped.getHaHuespedList()) {
                haHuespedListHaHuespedToAttach = em.getReference(haHuespedListHaHuespedToAttach.getClass(), haHuespedListHaHuespedToAttach.getHaHuespedPK());
                attachedHaHuespedList.add(haHuespedListHaHuespedToAttach);
            }
            huesped.setHaHuespedList(attachedHaHuespedList);
            em.persist(huesped);
            for (HaHuesped haHuespedListHaHuesped : huesped.getHaHuespedList()) {
                Huesped oldHuespedOfHaHuespedListHaHuesped = haHuespedListHaHuesped.getHuesped();
                haHuespedListHaHuesped.setHuesped(huesped);
                haHuespedListHaHuesped = em.merge(haHuespedListHaHuesped);
                if (oldHuespedOfHaHuespedListHaHuesped != null) {
                    oldHuespedOfHaHuespedListHaHuesped.getHaHuespedList().remove(haHuespedListHaHuesped);
                    oldHuespedOfHaHuespedListHaHuesped = em.merge(oldHuespedOfHaHuespedListHaHuesped);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findHuesped(huesped.getIdHuesped()) != null) {
                throw new PreexistingEntityException("Huesped " + huesped + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Huesped huesped) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Huesped persistentHuesped = em.find(Huesped.class, huesped.getIdHuesped());
            List<HaHuesped> haHuespedListOld = persistentHuesped.getHaHuespedList();
            List<HaHuesped> haHuespedListNew = huesped.getHaHuespedList();
            List<String> illegalOrphanMessages = null;
            for (HaHuesped haHuespedListOldHaHuesped : haHuespedListOld) {
                if (!haHuespedListNew.contains(haHuespedListOldHaHuesped)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HaHuesped " + haHuespedListOldHaHuesped + " since its huesped field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<HaHuesped> attachedHaHuespedListNew = new ArrayList<HaHuesped>();
            for (HaHuesped haHuespedListNewHaHuespedToAttach : haHuespedListNew) {
                haHuespedListNewHaHuespedToAttach = em.getReference(haHuespedListNewHaHuespedToAttach.getClass(), haHuespedListNewHaHuespedToAttach.getHaHuespedPK());
                attachedHaHuespedListNew.add(haHuespedListNewHaHuespedToAttach);
            }
            haHuespedListNew = attachedHaHuespedListNew;
            huesped.setHaHuespedList(haHuespedListNew);
            huesped = em.merge(huesped);
            for (HaHuesped haHuespedListNewHaHuesped : haHuespedListNew) {
                if (!haHuespedListOld.contains(haHuespedListNewHaHuesped)) {
                    Huesped oldHuespedOfHaHuespedListNewHaHuesped = haHuespedListNewHaHuesped.getHuesped();
                    haHuespedListNewHaHuesped.setHuesped(huesped);
                    haHuespedListNewHaHuesped = em.merge(haHuespedListNewHaHuesped);
                    if (oldHuespedOfHaHuespedListNewHaHuesped != null && !oldHuespedOfHaHuespedListNewHaHuesped.equals(huesped)) {
                        oldHuespedOfHaHuespedListNewHaHuesped.getHaHuespedList().remove(haHuespedListNewHaHuesped);
                        oldHuespedOfHaHuespedListNewHaHuesped = em.merge(oldHuespedOfHaHuespedListNewHaHuesped);
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
                String id = huesped.getIdHuesped();
                if (findHuesped(id) == null) {
                    throw new NonexistentEntityException("The huesped with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Huesped huesped;
            try {
                huesped = em.getReference(Huesped.class, id);
                huesped.getIdHuesped();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The huesped with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<HaHuesped> haHuespedListOrphanCheck = huesped.getHaHuespedList();
            for (HaHuesped haHuespedListOrphanCheckHaHuesped : haHuespedListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Huesped (" + huesped + ") cannot be destroyed since the HaHuesped " + haHuespedListOrphanCheckHaHuesped + " in its haHuespedList field has a non-nullable huesped field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(huesped);
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

    public List<Huesped> findHuespedEntities() {
        return findHuespedEntities(true, -1, -1);
    }

    public List<Huesped> findHuespedEntities(int maxResults, int firstResult) {
        return findHuespedEntities(false, maxResults, firstResult);
    }

    private List<Huesped> findHuespedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Huesped.class));
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

    public Huesped findHuesped(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Huesped.class, id);
        } finally {
            em.close();
        }
    }

    public int getHuespedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Huesped> rt = cq.from(Huesped.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
