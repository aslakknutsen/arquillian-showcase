package com.acme.cdi.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;

@RequestScoped
public class PrintSpool
{
   private Map<JobSize, List<Document>> documents;
   
   private int numDocumentsSent = 0;
   
   public void onPrint(@Observes Document document)
   {
      numDocumentsSent++;
      System.out.println("A document is headed to the print spool!");
   }
   
   public void onPrintUnknownSize(@Observes @Default Document document)
   {
      System.out.println("Can't accept a document of unknown size");
      throw new IllegalArgumentException("Cannot print unknown document size");
   }
   
   public void onLargePrint(@Observes @PrintJob(JobSize.LARGE) Document document)
   {
      System.out.println(document.getPages() + " pages printed (large job)");
      documents.get(JobSize.LARGE).add(document);
   }
   
   public void onMediumPrint(@Observes @PrintJob(JobSize.MEDIUM) Document document)
   {
      System.out.println(document.getPages() + " pages printed (medium job)");
      documents.get(JobSize.MEDIUM).add(document);
   }
   
   public void onSmallPrint(@Observes @PrintJob(JobSize.SMALL) Document document)
   {
      System.out.println(document.getPages() + " pages printed (small job)");
      documents.get(JobSize.SMALL).add(document);
   }
   
   public List<Document> getDocumentsProcessed(JobSize withSize)
   {
      return documents.get(withSize);
   }
   
   public int getNumDocumentsSent()
   {
      return numDocumentsSent;
   }
   
   public List<Document> getDocumentsProcessed()
   {
      List<Document> all = new ArrayList<Document>();
      for (List<Document> ofSize : documents.values())
      {
         all.addAll(ofSize);
      }
      return all;
   }
   
   @PostConstruct
   public void initialize()
   {
      System.out.println("Initializing print spool");
      documents = new HashMap<JobSize, List<Document>>();
      for (JobSize s : JobSize.values())
      {
         documents.put(s, new ArrayList<Document>());
      }
   }
}
