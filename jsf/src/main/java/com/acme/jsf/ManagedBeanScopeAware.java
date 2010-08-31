/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.acme.jsf;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author ssilvert
 */
public abstract class ManagedBeanScopeAware
{
   public String getScope()
   {
      FacesContext facesCtx = FacesContext.getCurrentInstance();
      ExternalContext extCtx = facesCtx.getExternalContext();

      if (extCtx.getRequestMap().containsValue(this))
      {
         return "request";
      }

      if (facesCtx.getViewRoot().getViewMap().containsValue(this))
      {
         return "view";
      }

      if (extCtx.getSessionMap().containsValue(this))
      {
         return "session";
      }

      if (extCtx.getApplicationMap().containsValue(this))
      {
         return "application";
      }

      return "unknown";
   }
}