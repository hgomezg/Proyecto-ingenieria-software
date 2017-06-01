/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db.controller;

import com.miguel.proyecto.db.Localizacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.miguel.proyecto.db.Puesto;
import com.miguel.proyecto.db.controller.exceptions.NonexistentEntityException;
import com.miguel.proyecto.db.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hugo
 */
public class LocalizacionJpaController implements Serializable {

    public LocalizacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localizacion localizacion) throws PreexistingEntityException, Exception {
        if (localizacion.getPuestoCollection() == null) {
            localizacion.setPuestoCollection(new ArrayList<Puesto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Puesto> attachedPuestoCollection = new ArrayList<Puesto>();
            for (Puesto puestoCollectionPuestoToAttach : localizacion.getPuestoCollection()) {
                puestoCollectionPuestoToAttach = em.getReference(puestoCollectionPuestoToAttach.getClass(), puestoCollectionPuestoToAttach.getIdPuesto());
                attachedPuestoCollection.add(puestoCollectionPuestoToAttach);
            }
            localizacion.setPuestoCollection(attachedPuestoCollection);
            em.persist(localizacion);
            for (Puesto puestoCollectionPuesto : localizacion.getPuestoCollection()) {
                Localizacion oldIdLocalizaOfPuestoCollectionPuesto = puestoCollectionPuesto.getIdLocaliza();
                puestoCollectionPuesto.setIdLocaliza(localizacion);
                puestoCollectionPuesto = em.merge(puestoCollectionPuesto);
                if (oldIdLocalizaOfPuestoCollectionPuesto != null) {
                    oldIdLocalizaOfPuestoCollectionPuesto.getPuestoCollection().remove(puestoCollectionPuesto);
                    oldIdLocalizaOfPuestoCollectionPuesto = em.merge(oldIdLocalizaOfPuestoCollectionPuesto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocalizacion(localizacion.getIdLocaliza()) != null) {
                throw new PreexistingEntityException("Localizacion " + localizacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localizacion localizacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion persistentLocalizacion = em.find(Localizacion.class, localizacion.getIdLocaliza());
            Collection<Puesto> puestoCollectionOld = persistentLocalizacion.getPuestoCollection();
            Collection<Puesto> puestoCollectionNew = localizacion.getPuestoCollection();
            Collection<Puesto> attachedPuestoCollectionNew = new ArrayList<Puesto>();
            for (Puesto puestoCollectionNewPuestoToAttach : puestoCollectionNew) {
                puestoCollectionNewPuestoToAttach = em.getReference(puestoCollectionNewPuestoToAttach.getClass(), puestoCollectionNewPuestoToAttach.getIdPuesto());
                attachedPuestoCollectionNew.add(puestoCollectionNewPuestoToAttach);
            }
            puestoCollectionNew = attachedPuestoCollectionNew;
            localizacion.setPuestoCollection(puestoCollectionNew);
            localizacion = em.merge(localizacion);
            for (Puesto puestoCollectionOldPuesto : puestoCollectionOld) {
                if (!puestoCollectionNew.contains(puestoCollectionOldPuesto)) {
                    puestoCollectionOldPuesto.setIdLocaliza(null);
                    puestoCollectionOldPuesto = em.merge(puestoCollectionOldPuesto);
                }
            }
            for (Puesto puestoCollectionNewPuesto : puestoCollectionNew) {
                if (!puestoCollectionOld.contains(puestoCollectionNewPuesto)) {
                    Localizacion oldIdLocalizaOfPuestoCollectionNewPuesto = puestoCollectionNewPuesto.getIdLocaliza();
                    puestoCollectionNewPuesto.setIdLocaliza(localizacion);
                    puestoCollectionNewPuesto = em.merge(puestoCollectionNewPuesto);
                    if (oldIdLocalizaOfPuestoCollectionNewPuesto != null && !oldIdLocalizaOfPuestoCollectionNewPuesto.equals(localizacion)) {
                        oldIdLocalizaOfPuestoCollectionNewPuesto.getPuestoCollection().remove(puestoCollectionNewPuesto);
                        oldIdLocalizaOfPuestoCollectionNewPuesto = em.merge(oldIdLocalizaOfPuestoCollectionNewPuesto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = localizacion.getIdLocaliza();
                if (findLocalizacion(id) == null) {
                    throw new NonexistentEntityException("The localizacion with id " + id + " no longer exists.");
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
            Localizacion localizacion;
            try {
                localizacion = em.getReference(Localizacion.class, id);
                localizacion.getIdLocaliza();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localizacion with id " + id + " no longer exists.", enfe);
            }
            Collection<Puesto> puestoCollection = localizacion.getPuestoCollection();
            for (Puesto puestoCollectionPuesto : puestoCollection) {
                puestoCollectionPuesto.setIdLocaliza(null);
                puestoCollectionPuesto = em.merge(puestoCollectionPuesto);
            }
            em.remove(localizacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localizacion> findLocalizacionEntities() {
        return findLocalizacionEntities(true, -1, -1);
    }

    public List<Localizacion> findLocalizacionEntities(int maxResults, int firstResult) {
        return findLocalizacionEntities(false, maxResults, firstResult);
    }

    private List<Localizacion> findLocalizacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localizacion.class));
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

    public Localizacion findLocalizacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localizacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalizacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localizacion> rt = cq.from(Localizacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
