import java.util.Scanner;
public class MainOfProgram {
    
    public static Response computer = new Response();
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        String response = "";
        
        do {
            
            System.out.print("You: ");
            response = scanner.nextLine();
            if (!response.equalsIgnoreCase("exit")) {
                System.out.print("Whats the topic: ");
                String topic = scanner.nextLine();
                System.out.print("What is the length: ");
                int length = scanner.nextInt();
                System.out.println("\t" + computer.talk(response, topic, length) + " :Computer");
            }
            
        } while(response.equalsIgnoreCase("exit"));
        
    }
}