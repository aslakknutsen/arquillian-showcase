package com.acme.cdi.event;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class WordProcessor
{
   @Inject
   private Event<Document> documentEvent;
   
   private Document document;
   
   public void create(int pages)
   {
      if (document != null)
      {
         throw new IllegalStateException("Document open. Please close it first!");
      }
      document = new Document(pages);
   }
   
   public void close()
   {
      document = null;
   }
   
   public void print()
   {
      documentEvent.select(new PrintJobLiteral(getJobSize())).fire(document);
   }
   
   public JobSize getJobSize()
   {
      int pp = document.getPages();
      if (pp < 5)
      {
         return JobSize.SMALL;
      }
      if (pp < 50)
      {
         return JobSize.MEDIUM;
      }
      return JobSize.LARGE;
   }
}
