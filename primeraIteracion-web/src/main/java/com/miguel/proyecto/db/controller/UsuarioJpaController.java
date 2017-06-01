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
import com.miguel.proyecto.db.Administrador;
import java.util.ArrayList;
import java.util.Collection;
import com.miguel.proyecto.db.Calificacion;
import com.miguel.proyecto.db.Comentarios;
import com.miguel.proyecto.db.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getAdministradorCollection() == null) {
            usuario.setAdministradorCollection(new ArrayList<Administrador>());
        }
        if (usuario.getCalificacionCollection() == null) {
            usuario.setCalificacionCollection(new ArrayList<Calificacion>());
        }
        if (usuario.getComentariosCollection() == null) {
            usuario.setComentariosCollection(new ArrayList<Comentarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Administrador> attachedAdministradorCollection = new ArrayList<Administrador>();
            for (Administrador administradorCollectionAdministradorToAttach : usuario.getAdministradorCollection()) {
                administradorCollectionAdministradorToAttach = em.getReference(administradorCollectionAdministradorToAttach.getClass(), administradorCollectionAdministradorToAttach.getIdAdministrador());
                attachedAdministradorCollection.add(administradorCollectionAdministradorToAttach);
            }
            usuario.setAdministradorCollection(attachedAdministradorCollection);
            Collection<Calificacion> attachedCalificacionCollection = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionCalificacionToAttach : usuario.getCalificacionCollection()) {
                calificacionCollectionCalificacionToAttach = em.getReference(calificacionCollectionCalificacionToAttach.getClass(), calificacionCollectionCalificacionToAttach.getCalificacionPK());
                attachedCalificacionCollection.add(calificacionCollectionCalificacionToAttach);
            }
            usuario.setCalificacionCollection(attachedCalificacionCollection);
            Collection<Comentarios> attachedComentariosCollection = new ArrayList<Comentarios>();
            for (Comentarios comentariosCollectionComentariosToAttach : usuario.getComentariosCollection()) {
                comentariosCollectionComentariosToAttach = em.getReference(comentariosCollectionComentariosToAttach.getClass(), comentariosCollectionComentariosToAttach.getIdComentarios());
                attachedComentariosCollection.add(comentariosCollectionComentariosToAttach);
            }
            usuario.setComentariosCollection(attachedComentariosCollection);
            em.persist(usuario);
            for (Administrador administradorCollectionAdministrador : usuario.getAdministradorCollection()) {
                Usuario oldIdUsuarioOfAdministradorCollectionAdministrador = administradorCollectionAdministrador.getIdUsuario();
                administradorCollectionAdministrador.setIdUsuario(usuario);
                administradorCollectionAdministrador = em.merge(administradorCollectionAdministrador);
                if (oldIdUsuarioOfAdministradorCollectionAdministrador != null) {
                    oldIdUsuarioOfAdministradorCollectionAdministrador.getAdministradorCollection().remove(administradorCollectionAdministrador);
                    oldIdUsuarioOfAdministradorCollectionAdministrador = em.merge(oldIdUsuarioOfAdministradorCollectionAdministrador);
                }
            }
            for (Calificacion calificacionCollectionCalificacion : usuario.getCalificacionCollection()) {
                Usuario oldUsuarioOfCalificacionCollectionCalificacion = calificacionCollectionCalificacion.getUsuario();
                calificacionCollectionCalificacion.setUsuario(usuario);
                calificacionCollectionCalificacion = em.merge(calificacionCollectionCalificacion);
                if (oldUsuarioOfCalificacionCollectionCalificacion != null) {
                    oldUsuarioOfCalificacionCollectionCalificacion.getCalificacionCollection().remove(calificacionCollectionCalificacion);
                    oldUsuarioOfCalificacionCollectionCalificacion = em.merge(oldUsuarioOfCalificacionCollectionCalificacion);
                }
            }
            for (Comentarios comentariosCollectionComentarios : usuario.getComentariosCollection()) {
                Usuario oldIdUsuarioOfComentariosCollectionComentarios = comentariosCollectionComentarios.getIdUsuario();
                comentariosCollectionComentarios.setIdUsuario(usuario);
                comentariosCollectionComentarios = em.merge(comentariosCollectionComentarios);
                if (oldIdUsuarioOfComentariosCollectionComentarios != null) {
                    oldIdUsuarioOfComentariosCollectionComentarios.getComentariosCollection().remove(comentariosCollectionComentarios);
                    oldIdUsuarioOfComentariosCollectionComentarios = em.merge(oldIdUsuarioOfComentariosCollectionComentarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Collection<Administrador> administradorCollectionOld = persistentUsuario.getAdministradorCollection();
            Collection<Administrador> administradorCollectionNew = usuario.getAdministradorCollection();
            Collection<Calificacion> calificacionCollectionOld = persistentUsuario.getCalificacionCollection();
            Collection<Calificacion> calificacionCollectionNew = usuario.getCalificacionCollection();
            Collection<Comentarios> comentariosCollectionOld = persistentUsuario.getComentariosCollection();
            Collection<Comentarios> comentariosCollectionNew = usuario.getComentariosCollection();
            List<String> illegalOrphanMessages = null;
            for (Calificacion calificacionCollectionOldCalificacion : calificacionCollectionOld) {
                if (!calificacionCollectionNew.contains(calificacionCollectionOldCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Calificacion " + calificacionCollectionOldCalificacion + " since its usuario field is not nullable.");
                }
            }
            for (Comentarios comentariosCollectionOldComentarios : comentariosCollectionOld) {
                if (!comentariosCollectionNew.contains(comentariosCollectionOldComentarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentarios " + comentariosCollectionOldComentarios + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Administrador> attachedAdministradorCollectionNew = new ArrayList<Administrador>();
            for (Administrador administradorCollectionNewAdministradorToAttach : administradorCollectionNew) {
                administradorCollectionNewAdministradorToAttach = em.getReference(administradorCollectionNewAdministradorToAttach.getClass(), administradorCollectionNewAdministradorToAttach.getIdAdministrador());
                attachedAdministradorCollectionNew.add(administradorCollectionNewAdministradorToAttach);
            }
            administradorCollectionNew = attachedAdministradorCollectionNew;
            usuario.setAdministradorCollection(administradorCollectionNew);
            Collection<Calificacion> attachedCalificacionCollectionNew = new ArrayList<Calificacion>();
            for (Calificacion calificacionCollectionNewCalificacionToAttach : calificacionCollectionNew) {
                calificacionCollectionNewCalificacionToAttach = em.getReference(calificacionCollectionNewCalificacionToAttach.getClass(), calificacionCollectionNewCalificacionToAttach.getCalificacionPK());
                attachedCalificacionCollectionNew.add(calificacionCollectionNewCalificacionToAttach);
            }
            calificacionCollectionNew = attachedCalificacionCollectionNew;
            usuario.setCalificacionCollection(calificacionCollectionNew);
            Collection<Comentarios> attachedComentariosCollectionNew = new ArrayList<Comentarios>();
            for (Comentarios comentariosCollectionNewComentariosToAttach : comentariosCollectionNew) {
                comentariosCollectionNewComentariosToAttach = em.getReference(comentariosCollectionNewComentariosToAttach.getClass(), comentariosCollectionNewComentariosToAttach.getIdComentarios());
                attachedComentariosCollectionNew.add(comentariosCollectionNewComentariosToAttach);
            }
            comentariosCollectionNew = attachedComentariosCollectionNew;
            usuario.setComentariosCollection(comentariosCollectionNew);
            usuario = em.merge(usuario);
            for (Administrador administradorCollectionOldAdministrador : administradorCollectionOld) {
                if (!administradorCollectionNew.contains(administradorCollectionOldAdministrador)) {
                    administradorCollectionOldAdministrador.setIdUsuario(null);
                    administradorCollectionOldAdministrador = em.merge(administradorCollectionOldAdministrador);
                }
            }
            for (Administrador administradorCollectionNewAdministrador : administradorCollectionNew) {
                if (!administradorCollectionOld.contains(administradorCollectionNewAdministrador)) {
                    Usuario oldIdUsuarioOfAdministradorCollectionNewAdministrador = administradorCollectionNewAdministrador.getIdUsuario();
                    administradorCollectionNewAdministrador.setIdUsuario(usuario);
                    administradorCollectionNewAdministrador = em.merge(administradorCollectionNewAdministrador);
                    if (oldIdUsuarioOfAdministradorCollectionNewAdministrador != null && !oldIdUsuarioOfAdministradorCollectionNewAdministrador.equals(usuario)) {
                        oldIdUsuarioOfAdministradorCollectionNewAdministrador.getAdministradorCollection().remove(administradorCollectionNewAdministrador);
                        oldIdUsuarioOfAdministradorCollectionNewAdministrador = em.merge(oldIdUsuarioOfAdministradorCollectionNewAdministrador);
                    }
                }
            }
            for (Calificacion calificacionCollectionNewCalificacion : calificacionCollectionNew) {
                if (!calificacionCollectionOld.contains(calificacionCollectionNewCalificacion)) {
                    Usuario oldUsuarioOfCalificacionCollectionNewCalificacion = calificacionCollectionNewCalificacion.getUsuario();
                    calificacionCollectionNewCalificacion.setUsuario(usuario);
                    calificacionCollectionNewCalificacion = em.merge(calificacionCollectionNewCalificacion);
                    if (oldUsuarioOfCalificacionCollectionNewCalificacion != null && !oldUsuarioOfCalificacionCollectionNewCalificacion.equals(usuario)) {
                        oldUsuarioOfCalificacionCollectionNewCalificacion.getCalificacionCollection().remove(calificacionCollectionNewCalificacion);
                        oldUsuarioOfCalificacionCollectionNewCalificacion = em.merge(oldUsuarioOfCalificacionCollectionNewCalificacion);
                    }
                }
            }
            for (Comentarios comentariosCollectionNewComentarios : comentariosCollectionNew) {
                if (!comentariosCollectionOld.contains(comentariosCollectionNewComentarios)) {
                    Usuario oldIdUsuarioOfComentariosCollectionNewComentarios = comentariosCollectionNewComentarios.getIdUsuario();
                    comentariosCollectionNewComentarios.setIdUsuario(usuario);
                    comentariosCollectionNewComentarios = em.merge(comentariosCollectionNewComentarios);
                    if (oldIdUsuarioOfComentariosCollectionNewComentarios != null && !oldIdUsuarioOfComentariosCollectionNewComentarios.equals(usuario)) {
                        oldIdUsuarioOfComentariosCollectionNewComentarios.getComentariosCollection().remove(comentariosCollectionNewComentarios);
                        oldIdUsuarioOfComentariosCollectionNewComentarios = em.merge(oldIdUsuarioOfComentariosCollectionNewComentarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Calificacion> calificacionCollectionOrphanCheck = usuario.getCalificacionCollection();
            for (Calificacion calificacionCollectionOrphanCheckCalificacion : calificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Calificacion " + calificacionCollectionOrphanCheckCalificacion + " in its calificacionCollection field has a non-nullable usuario field.");
            }
            Collection<Comentarios> comentariosCollectionOrphanCheck = usuario.getComentariosCollection();
            for (Comentarios comentariosCollectionOrphanCheckComentarios : comentariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Comentarios " + comentariosCollectionOrphanCheckComentarios + " in its comentariosCollection field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Administrador> administradorCollection = usuario.getAdministradorCollection();
            for (Administrador administradorCollectionAdministrador : administradorCollection) {
                administradorCollectionAdministrador.setIdUsuario(null);
                administradorCollectionAdministrador = em.merge(administradorCollectionAdministrador);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
