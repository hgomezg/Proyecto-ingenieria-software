/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db.controller;

import com.miguel.proyecto.db.Alimentos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.miguel.proyecto.db.Puesto;
import com.miguel.proyecto.db.controller.exceptions.NonexistentEntityException;
import com.miguel.proyecto.db.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hugo
 */
public class AlimentosJpaController implements Serializable {

    public AlimentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alimentos alimentos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto idPuesto = alimentos.getIdPuesto();
            if (idPuesto != null) {
                idPuesto = em.getReference(idPuesto.getClass(), idPuesto.getIdPuesto());
                alimentos.setIdPuesto(idPuesto);
            }
            em.persist(alimentos);
            if (idPuesto != null) {
                idPuesto.getAlimentosCollection().add(alimentos);
                idPuesto = em.merge(idPuesto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlimentos(alimentos.getIdAlimentos()) != null) {
                throw new PreexistingEntityException("Alimentos " + alimentos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alimentos alimentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alimentos persistentAlimentos = em.find(Alimentos.class, alimentos.getIdAlimentos());
            Puesto idPuestoOld = persistentAlimentos.getIdPuesto();
            Puesto idPuestoNew = alimentos.getIdPuesto();
            if (idPuestoNew != null) {
                idPuestoNew = em.getReference(idPuestoNew.getClass(), idPuestoNew.getIdPuesto());
                alimentos.setIdPuesto(idPuestoNew);
            }
            alimentos = em.merge(alimentos);
            if (idPuestoOld != null && !idPuestoOld.equals(idPuestoNew)) {
                idPuestoOld.getAlimentosCollection().remove(alimentos);
                idPuestoOld = em.merge(idPuestoOld);
            }
            if (idPuestoNew != null && !idPuestoNew.equals(idPuestoOld)) {
                idPuestoNew.getAlimentosCollection().add(alimentos);
                idPuestoNew = em.merge(idPuestoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alimentos.getIdAlimentos();
                if (findAlimentos(id) == null) {
                    throw new NonexistentEntityException("The alimentos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alimentos alimentos;
            try {
                alimentos = em.getReference(Alimentos.class, id);
                alimentos.getIdAlimentos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alimentos with id " + id + " no longer exists.", enfe);
            }
            Puesto idPuesto = alimentos.getIdPuesto();
            if (idPuesto != null) {
                idPuesto.getAlimentosCollection().remove(alimentos);
                idPuesto = em.merge(idPuesto);
            }
            em.remove(alimentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alimentos> findAlimentosEntities() {
        return findAlimentosEntities(true, -1, -1);
    }

    public List<Alimentos> findAlimentosEntities(int maxResults, int firstResult) {
        return findAlimentosEntities(false, maxResults, firstResult);
    }

    private List<Alimentos> findAlimentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alimentos.class));
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

    public Alimentos findAlimentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alimentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlimentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alimentos> rt = cq.from(Alimentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
