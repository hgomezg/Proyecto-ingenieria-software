/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.miguel.proyecto.db.Localizacion;
import com.miguel.proyecto.db.Calificacion;
import java.util.ArrayList;
import java.util.Collection;
import com.miguel.proyecto.db.Comentarios;
import com.miguel.proyecto.db.Alimentos;
import com.miguel.proyecto.db.Puesto;
import com.miguel.proyecto.db.controller.exceptions.IllegalOrphanException;
import com.miguel.proyecto.db.controller.exceptions.NonexistentEntityException;
import com.miguel.proyecto.db.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hugo
 */
public class PuestoJpaController implements Serializable {

    public PuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puesto puesto) throws PreexistingEntityException, Exception {
        if (puesto.getCalificacionCollection() == null) {
            puesto.setCalificacionCollection(new ArrayList<Calificacion>());
        }
        if (puesto.getComentariosCollection() == null) {
            puesto.setComentariosCollection(new ArrayList<Comentarios>());
        }
        if (puesto.getAlimentosCollection() == null) {
            puesto.setAlimentosCollection(new ArrayList<Alimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion idLocaliza = puesto.getIdLocaliza();
            if (idLocaliza != null) {
                idLocaliza = em.getReference(idLocaliza.getClass(), idLocaliza.getIdLocaliza());
                puesto.setIdLocaliza(idLocaliza);
            }
            Collection<Calificacion> attachedCalificacionCollection = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionCalificacionToAttach : puesto.getCalificacionCollection()) {
                calificacionCollectionCalificacionToAttach = em.getReference(calificacionCollectionCalificacionToAttach.getClass(), calificacionCollectionCalificacionToAttach.getCalificacionPK());
                attachedCalificacionCollection.add(calificacionCollectionCalificacionToAttach);
            }
            puesto.setCalificacionCollection(attachedCalificacionCollection);
            Collection<Comentarios> attachedComentariosCollection = new ArrayList<Comentarios>();
            for (Comentarios comentariosCollectionComentariosToAttach : puesto.getComentariosCollection()) {
                comentariosCollectionComentariosToAttach = em.getReference(comentariosCollectionComentariosToAttach.getClass(), comentariosCollectionComentariosToAttach.getIdComentarios());
                attachedComentariosCollection.add(comentariosCollectionComentariosToAttach);
            }
            puesto.setComentariosCollection(attachedComentariosCollection);
            Collection<Alimentos> attachedAlimentosCollection = new ArrayList<Alimentos>();
            for (Alimentos alimentosCollectionAlimentosToAttach : puesto.getAlimentosCollection()) {
                alimentosCollectionAlimentosToAttach = em.getReference(alimentosCollectionAlimentosToAttach.getClass(), alimentosCollectionAlimentosToAttach.getIdAlimentos());
                attachedAlimentosCollection.add(alimentosCollectionAlimentosToAttach);
            }
            puesto.setAlimentosCollection(attachedAlimentosCollection);
            em.persist(puesto);
            if (idLocaliza != null) {
                idLocaliza.getPuestoCollection().add(puesto);
                idLocaliza = em.merge(idLocaliza);
            }
            for (Calificacion calificacionCollectionCalificacion : puesto.getCalificacionCollection()) {
                Puesto oldPuestoOfCalificacionCollectionCalificacion = calificacionCollectionCalificacion.getPuesto();
                calificacionCollectionCalificacion.setPuesto(puesto);
                calificacionCollectionCalificacion = em.merge(calificacionCollectionCalificacion);
                if (oldPuestoOfCalificacionCollectionCalificacion != null) {
                    oldPuestoOfCalificacionCollectionCalificacion.getCalificacionCollection().remove(calificacionCollectionCalificacion);
                    oldPuestoOfCalificacionCollectionCalificacion = em.merge(oldPuestoOfCalificacionCollectionCalificacion);
                }
            }
            for (Comentarios comentariosCollectionComentarios : puesto.getComentariosCollection()) {
                Puesto oldIdPuestoOfComentariosCollectionComentarios = comentariosCollectionComentarios.getIdPuesto();
                comentariosCollectionComentarios.setIdPuesto(puesto);
                comentariosCollectionComentarios = em.merge(comentariosCollectionComentarios);
                if (oldIdPuestoOfComentariosCollectionComentarios != null) {
                    oldIdPuestoOfComentariosCollectionComentarios.getComentariosCollection().remove(comentariosCollectionComentarios);
                    oldIdPuestoOfComentariosCollectionComentarios = em.merge(oldIdPuestoOfComentariosCollectionComentarios);
                }
            }
            for (Alimentos alimentosCollectionAlimentos : puesto.getAlimentosCollection()) {
                Puesto oldIdPuestoOfAlimentosCollectionAlimentos = alimentosCollectionAlimentos.getIdPuesto();
                alimentosCollectionAlimentos.setIdPuesto(puesto);
                alimentosCollectionAlimentos = em.merge(alimentosCollectionAlimentos);
                if (oldIdPuestoOfAlimentosCollectionAlimentos != null) {
                    oldIdPuestoOfAlimentosCollectionAlimentos.getAlimentosCollection().remove(alimentosCollectionAlimentos);
                    oldIdPuestoOfAlimentosCollectionAlimentos = em.merge(oldIdPuestoOfAlimentosCollectionAlimentos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPuesto(puesto.getIdPuesto()) != null) {
                throw new PreexistingEntityException("Puesto " + puesto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Puesto puesto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto persistentPuesto = em.find(Puesto.class, puesto.getIdPuesto());
            Localizacion idLocalizaOld = persistentPuesto.getIdLocaliza();
            Localizacion idLocalizaNew = puesto.getIdLocaliza();
            Collection<Calificacion> calificacionCollectionOld = persistentPuesto.getCalificacionCollection();
            Collection<Calificacion> calificacionCollectionNew = puesto.getCalificacionCollection();
            Collection<Comentarios> comentariosCollectionOld = persistentPuesto.getComentariosCollection();
            Collection<Comentarios> comentariosCollectionNew = puesto.getComentariosCollection();
            Collection<Alimentos> alimentosCollectionOld = persistentPuesto.getAlimentosCollection();
            Collection<Alimentos> alimentosCollectionNew = puesto.getAlimentosCollection();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionCollectionOldCalificacion : calificacionCollectionOld) {
                if (!calificacionCollectionNew.contains(calificacionCollectionOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionCollectionOldCalificacion + " since its puesto field is not nullable.");
                }
            }
            for (Comentarios comentariosCollectionOldComentarios : comentariosCollectionOld) {
                if (!comentariosCollectionNew.contains(comentariosCollectionOldComentarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentarios " + comentariosCollectionOldComentarios + " since its idPuesto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idLocalizaNew != null) {
                idLocalizaNew = em.getReference(idLocalizaNew.getClass(), idLocalizaNew.getIdLocaliza());
                puesto.setIdLocaliza(idLocalizaNew);
            }
            Collection<Calificacion> attachedCalificacionCollectionNew = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionNewCalificacionToAttach : calificacionCollectionNew) {
                calificacionCollectionNewCalificacionToAttach = em.getReference(calificacionCollectionNewCalificacionToAttach.getClass(), calificacionCollectionNewCalificacionToAttach.getCalificacionPK());
                attachedCalificacionCollectionNew.add(calificacionCollectionNewCalificacionToAttach);
            }
            calificacionCollectionNew = attachedCalificacionCollectionNew;
            puesto.setCalificacionCollection(calificacionCollectionNew);
            Collection<Comentarios> attachedComentariosCollectionNew = new ArrayList<Comentarios>();
            for (Comentarios comentariosCollectionNewComentariosToAttach : comentariosCollectionNew) {
                comentariosCollectionNewComentariosToAttach = em.getReference(comentariosCollectionNewComentariosToAttach.getClass(), comentariosCollectionNewComentariosToAttach.getIdComentarios());
                attachedComentariosCollectionNew.add(comentariosCollectionNewComentariosToAttach);
            }
            comentariosCollectionNew = attachedComentariosCollectionNew;
            puesto.setComentariosCollection(comentariosCollectionNew);
            Collection<Alimentos> attachedAlimentosCollectionNew = new ArrayList<Alimentos>();
            for (Alimentos alimentosCollectionNewAlimentosToAttach : alimentosCollectionNew) {
                alimentosCollectionNewAlimentosToAttach = em.getReference(alimentosCollectionNewAlimentosToAttach.getClass(), alimentosCollectionNewAlimentosToAttach.getIdAlimentos());
                attachedAlimentosCollectionNew.add(alimentosCollectionNewAlimentosToAttach);
            }
            alimentosCollectionNew = attachedAlimentosCollectionNew;
            puesto.setAlimentosCollection(alimentosCollectionNew);
            puesto = em.merge(puesto);
            if (idLocalizaOld != null && !idLocalizaOld.equals(idLocalizaNew)) {
                idLocalizaOld.getPuestoCollection().remove(puesto);
                idLocalizaOld = em.merge(idLocalizaOld);
            }
            if (idLocalizaNew != null && !idLocalizaNew.equals(idLocalizaOld)) {
                idLocalizaNew.getPuestoCollection().add(puesto);
                idLocalizaNew = em.merge(idLocalizaNew);
            }
            for (Calificacion calificacionCollectionNewCalificacion : calificacionCollectionNew) {
                if (!calificacionCollectionOld.contains(calificacionCollectionNewCalificacion)) {
                    Puesto oldPuestoOfCalificacionCollectionNewCalificacion = calificacionCollectionNewCalificacion.getPuesto();
                    calificacionCollectionNewCalificacion.setPuesto(puesto);
                    calificacionCollectionNewCalificacion = em.merge(calificacionCollectionNewCalificacion);
                    if (oldPuestoOfCalificacionCollectionNewCalificacion != null && !oldPuestoOfCalificacionCollectionNewCalificacion.equals(puesto)) {
                        oldPuestoOfCalificacionCollectionNewCalificacion.getCalificacionCollection().remove(calificacionCollectionNewCalificacion);
                        oldPuestoOfCalificacionCollectionNewCalificacion = em.merge(oldPuestoOfCalificacionCollectionNewCalificacion);
                    }
                }
            }
            for (Comentarios comentariosCollectionNewComentarios : comentariosCollectionNew) {
                if (!comentariosCollectionOld.contains(comentariosCollectionNewComentarios)) {
                    Puesto oldIdPuestoOfComentariosCollectionNewComentarios = comentariosCollectionNewComentarios.getIdPuesto();
                    comentariosCollectionNewComentarios.setIdPuesto(puesto);
                    comentariosCollectionNewComentarios = em.merge(comentariosCollectionNewComentarios);
                    if (oldIdPuestoOfComentariosCollectionNewComentarios != null && !oldIdPuestoOfComentariosCollectionNewComentarios.equals(puesto)) {
                        oldIdPuestoOfComentariosCollectionNewComentarios.getComentariosCollection().remove(comentariosCollectionNewComentarios);
                        oldIdPuestoOfComentariosCollectionNewComentarios = em.merge(oldIdPuestoOfComentariosCollectionNewComentarios);
                    }
                }
            }
            for (Alimentos alimentosCollectionOldAlimentos : alimentosCollectionOld) {
                if (!alimentosCollectionNew.contains(alimentosCollectionOldAlimentos)) {
                    alimentosCollectionOldAlimentos.setIdPuesto(null);
                    alimentosCollectionOldAlimentos = em.merge(alimentosCollectionOldAlimentos);
                }
            }
            for (Alimentos alimentosCollectionNewAlimentos : alimentosCollectionNew) {
                if (!alimentosCollectionOld.contains(alimentosCollectionNewAlimentos)) {
                    Puesto oldIdPuestoOfAlimentosCollectionNewAlimentos = alimentosCollectionNewAlimentos.getIdPuesto();
                    alimentosCollectionNewAlimentos.setIdPuesto(puesto);
                    alimentosCollectionNewAlimentos = em.merge(alimentosCollectionNewAlimentos);
                    if (oldIdPuestoOfAlimentosCollectionNewAlimentos != null && !oldIdPuestoOfAlimentosCollectionNewAlimentos.equals(puesto)) {
                        oldIdPuestoOfAlimentosCollectionNewAlimentos.getAlimentosCollection().remove(alimentosCollectionNewAlimentos);
                        oldIdPuestoOfAlimentosCollectionNewAlimentos = em.merge(oldIdPuestoOfAlimentosCollectionNewAlimentos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = puesto.getIdPuesto();
                if (findPuesto(id) == null) {
                    throw new NonexistentEntityException("The puesto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puesto puesto;
            try {
                puesto = em.getReference(Puesto.class, id);
                puesto.getIdPuesto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puesto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Calificacion> calificacionCollectionOrphanCheck = puesto.getCalificacionCollection();
            for (Calificacion calificacionCollectionOrphanCheckCalificacion : calificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Calificacion " + calificacionCollectionOrphanCheckCalificacion + " in its calificacionCollection field has a non-nullable puesto field.");
            }
            Collection<Comentarios> comentariosCollectionOrphanCheck = puesto.getComentariosCollection();
            for (Comentarios comentariosCollectionOrphanCheckComentarios : comentariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puesto (" + puesto + ") cannot be destroyed since the Comentarios " + comentariosCollectionOrphanCheckComentarios + " in its comentariosCollection field has a non-nullable idPuesto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localizacion idLocaliza = puesto.getIdLocaliza();
            if (idLocaliza != null) {
                idLocaliza.getPuestoCollection().remove(puesto);
                idLocaliza = em.merge(idLocaliza);
            }
            Collection<Alimentos> alimentosCollection = puesto.getAlimentosCollection();
            for (Alimentos alimentosCollectionAlimentos : alimentosCollection) {
                alimentosCollectionAlimentos.setIdPuesto(null);
                alimentosCollectionAlimentos = em.merge(alimentosCollectionAlimentos);
            }
            em.remove(puesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Puesto> findPuestoEntities() {
        return findPuestoEntities(true, -1, -1);
    }

    public List<Puesto> findPuestoEntities(int maxResults, int firstResult) {
        return findPuestoEntities(false, maxResults, firstResult);
    }

    private List<Puesto> findPuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puesto.class));
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

    public Puesto findPuesto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puesto> rt = cq.from(Puesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
