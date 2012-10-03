package org.jboss.arquillian.showcase.universe.view;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaQuery;

import org.jboss.arquillian.showcase.universe.model.Conference;

@Named
@Stateful
@ConversationScoped
public class ConferenceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String redirect = null;
    
    /**
     * @return the redirect
     */
    public String getRedirect() {
        return redirect;
    }
    
    /**
     * @param redirect the redirect to set
     */
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Conference conference;

    public Conference getConference() {
        return this.conference;
    }

    @Inject
    private Conversation conversation;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public String create() {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }
        if (this.conversation.isTransient()) {
            this.conversation.begin();
        }

        if (getId() == null) {
            this.conference = new Conference();
        }
        else {
            this.conference = this.entityManager.find(Conference.class, getId());
        }
    }

    /*
     * Support updating and deleting Conference entities
     */

    public String update() {
        this.conversation.end();

        try {
            String output = getRedirect() == null ? "create?faces-redirect=true&id=" + this.conference.getId():""; 
            if(getId() != null) {
                this.conference.setId(getId());
            }
            this.entityManager.persist(this.conference);
            return output;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            this.entityManager.remove(this.entityManager.find(Conference.class, getId()));
            this.entityManager.flush();
            return "create?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support listing and POSTing back Conference entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Conference> getAll() {

        CriteriaQuery<Conference> criteria = this.entityManager.getCriteriaBuilder().createQuery(Conference.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Conference.class))).getResultList();
    }

    public Converter getConverter() {

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {

                return ConferenceBean.this.entityManager.find(Conference.class, Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {

                if (value == null) {
                    return "";
                }

                return String.valueOf(((Conference) value).getId());
            }
        };
    }
}