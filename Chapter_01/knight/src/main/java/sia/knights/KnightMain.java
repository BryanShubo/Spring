package sia.knights;

import org.springframework.context.support.
                   ClassPathXmlApplicationContext;

/*
Here the main() method creates the Spring application context based on the
knights.xml file. Then it uses the application context as a factory to retrieve the bean
whose ID is knight. With a reference to the Knight object, it calls the embarkOnQuest()
method to have the knight embark on the quest he was given. Note that this class
knows nothing about which type of Quest your hero has. For that matter, it’s blissfully
unaware of the fact that it’s dealing with BraveKnight. Only the knights.xml file
knows for sure what the implementations are.
* */
public class KnightMain {

  public static void main(String[] args) throws Exception {
      /*
      * DI
      * */
      testKnightXml();

      /*
      * AOP
      * */
      testMinstrelXml();
  }

    //DI
    private static void testKnightXml() {
        ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext(
                "META-INF/spring/knight.xml");
        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }

    // AOP
    private static void testMinstrelXml() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "META-INF/spring/minstrel.xml");
        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();
        context.close();
    }
}
