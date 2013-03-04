package org.jboss.arquillian.showcase.universe;

import java.io.File;
import java.io.InputStream;

import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.NamedAsset;

public class Source
{
   private static final File PATH_MAIN = new File("src/main/java");
   private static final File PATH_MAIN_RESOURCE = new File("src/main/resource");
   private static final File PATH_WEBAPP = new File("src/main/webapp");
   private static final File PATH_TEST = new File("src/test/java");
   private static final File PATH_TEST_RESOURCE = new File("src/test/resource");
   
   // src/main/java + test.jaba
   public static NamedAsset main(String name) {
      return wrap(PATH_MAIN, name);
   }

   public static NamedAsset mainResource(String name) {
      return wrap(PATH_MAIN_RESOURCE, name);
   }

   public static NamedAsset test(String name) {
      return wrap(PATH_TEST, name);
   }
   
   public static NamedAsset testResource(String name) {
      return wrap(PATH_TEST_RESOURCE, name);
   }

   public static NamedAsset webapp(String name) {
      return wrap(PATH_WEBAPP, name);
   }

   private static NamedAsset wrap(File parent, String name) {
      return new WrappedNamedAsset(name, new FileAsset(new File(parent, name)));
   }
   
   private static class WrappedNamedAsset implements NamedAsset {
      
      private Asset target;
      private String name;
      
      public WrappedNamedAsset(String name, Asset target)
      {
         this.name = name;
         this.target = target;
      }
      
      @Override
      public String getName()
      {
         return name;
      }
      
      @Override
      public InputStream openStream()
      {
         return target.openStream();
      }
   }
}
