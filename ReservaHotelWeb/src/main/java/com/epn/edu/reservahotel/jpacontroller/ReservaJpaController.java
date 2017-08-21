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
import com.epn.edu.reservahotel.entidades.Servicio;
import com.epn.edu.reservahotel.entidades.Usuario;
import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.Reserva;
import com.epn.edu.reservahotel.jpacontroller.exceptions.IllegalOrphanException;
import com.epn.edu.reservahotel.jpacontroller.exceptions.NonexistentEntityException;
import com.epn.edu.reservahotel.jpacontroller.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jefferson
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws RollbackFailureException, Exception {
        if (reserva.getReHabitacionList() == null) {
            reserva.setReHabitacionList(new ArrayList<ReHabitacion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Servicio idServicio = reserva.getIdServicio();
            if (idServicio != null) {
                idServicio = em.getReference(idServicio.getClass(), idServicio.getIdServicio());
                reserva.setIdServicio(idServicio);
            }
            Usuario idUsuario = reserva.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                reserva.setIdUsuario(idUsuario);
            }
            List<ReHabitacion> attachedReHabitacionList = new ArrayList<ReHabitacion>();
            for (ReHabitacion reHabitacionListReHabitacionToAttach : reserva.getReHabitacionList()) {
                reHabitacionListReHabitacionToAttach = em.getReference(reHabitacionListReHabitacionToAttach.getClass(), reHabitacionListReHabitacionToAttach.getReHabitacionPK());
                attachedReHabitacionList.add(reHabitacionListReHabitacionToAttach);
            }
            reserva.setReHabitacionList(attachedReHabitacionList);
            em.persist(reserva);
            if (idServicio != null) {
                idServicio.getReservaList().add(reserva);
                idServicio = em.merge(idServicio);
            }
            if (idUsuario != null) {
                idUsuario.getReservaList().add(reserva);
                idUsuario = em.merge(idUsuario);
            }
//            for (ReHabitacion reHabitacionListReHabitacion : reserva.getReHabitacionList()) {
//                Reserva oldIdReservaOfReHabitacionListReHabitacion = reHabitacionListReHabitacion.getIdReserva();
//                reHabitacionListReHabitacion.setIdReserva(reserva);
//                reHabitacionListReHabitacion = em.merge(reHabitacionListReHabitacion);
//                if (oldIdReservaOfReHabitacionListReHabitacion != null) {
//                    oldIdReservaOfReHabitacionListReHabitacion.getReHabitacionList().remove(reHabitacionListReHabitacion);
//                    oldIdReservaOfReHabitacionListReHabitacion = em.merge(oldIdReservaOfReHabitacionListReHabitacion);
//                }
//            }
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

    public void edit(Reserva reserva) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdReserva());
            Servicio idServicioOld = persistentReserva.getIdServicio();
            Servicio idServicioNew = reserva.getIdServicio();
            Usuario idUsuarioOld = persistentReserva.getIdUsuario();
            Usuario idUsuarioNew = reserva.getIdUsuario();
            List<ReHabitacion> reHabitacionListOld = persistentReserva.getReHabitacionList();
            List<ReHabitacion> reHabitacionListNew = reserva.getReHabitacionList();
            List<String> illegalOrphanMessages = null;
            for (ReHabitacion reHabitacionListOldReHabitacion : reHabitacionListOld) {
                if (!reHabitacionListNew.contains(reHabitacionListOldReHabitacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReHabitacion " + reHabitacionListOldReHabitacion + " since its idReserva field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idServicioNew != null) {
                idServicioNew = em.getReference(idServicioNew.getClass(), idServicioNew.getIdServicio());
                reserva.setIdServicio(idServicioNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                reserva.setIdUsuario(idUsuarioNew);
            }
            List<ReHabitacion> attachedReHabitacionListNew = new ArrayList<ReHabitacion>();
            for (ReHabitacion reHabitacionListNewReHabitacionToAttach : reHabitacionListNew) {
                reHabitacionListNewReHabitacionToAttach = em.getReference(reHabitacionListNewReHabitacionToAttach.getClass(), reHabitacionListNewReHabitacionToAttach.getReHabitacionPK());
                attachedReHabitacionListNew.add(reHabitacionListNewReHabitacionToAttach);
            }
            reHabitacionListNew = attachedReHabitacionListNew;
            reserva.setReHabitacionList(reHabitacionListNew);
            reserva = em.merge(reserva);
            if (idServicioOld != null && !idServicioOld.equals(idServicioNew)) {
                idServicioOld.getReservaList().remove(reserva);
                idServicioOld = em.merge(idServicioOld);
            }
            if (idServicioNew != null && !idServicioNew.equals(idServicioOld)) {
                idServicioNew.getReservaList().add(reserva);
                idServicioNew = em.merge(idServicioNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getReservaList().remove(reserva);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getReservaList().add(reserva);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (ReHabitacion reHabitacionListNewReHabitacion : reHabitacionListNew) {
                if (!reHabitacionListOld.contains(reHabitacionListNewReHabitacion)) {
                    Reserva oldIdReservaOfReHabitacionListNewReHabitacion = reHabitacionListNewReHabitacion.getIdReserva();
                    reHabitacionListNewReHabitacion.setIdReserva(reserva);
                    reHabitacionListNewReHabitacion = em.merge(reHabitacionListNewReHabitacion);
                    if (oldIdReservaOfReHabitacionListNewReHabitacion != null && !oldIdReservaOfReHabitacionListNewReHabitacion.equals(reserva)) {
                        oldIdReservaOfReHabitacionListNewReHabitacion.getReHabitacionList().remove(reHabitacionListNewReHabitacion);
                        oldIdReservaOfReHabitacionListNewReHabitacion = em.merge(oldIdReservaOfReHabitacionListNewReHabitacion);
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
                Integer id = reserva.getIdReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReHabitacion> reHabitacionListOrphanCheck = reserva.getReHabitacionList();
            for (ReHabitacion reHabitacionListOrphanCheckReHabitacion : reHabitacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the ReHabitacion " + reHabitacionListOrphanCheckReHabitacion + " in its reHabitacionList field has a non-nullable idReserva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Servicio idServicio = reserva.getIdServicio();
            if (idServicio != null) {
                idServicio.getReservaList().remove(reserva);
                idServicio = em.merge(idServicio);
            }
            Usuario idUsuario = reserva.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getReservaList().remove(reserva);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(reserva);
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

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Reserva> findAllReservaByFechaInicio(Date fechaInicio, Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Reserva.findAllReservaByFechaInicio", Reserva.class);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("idUsuario", idUsuario);
            List<Reserva> results = query.getResultList();

            return (List<Reserva>) results;

        } finally {
            em.close();
        }
    }
    
}
