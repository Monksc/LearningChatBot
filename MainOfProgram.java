import java.util.Scanner;
public class MainOfProgram {
    
    public static Response2 computer = new Response2("memoryForNicerBot");
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        String response = "";
        
        do {
            
            System.out.print("You: ");
            response = scanner.nextLine();
            if (!response.equalsIgnoreCase("exit")) {
                System.out.println("\t" + computer.talk(response) + " :Computer");
            }
            
        } while(!response.equalsIgnoreCase("exit") && !response.equalsIgnoreCase("bye") && !response.equalsIgnoreCase("see ya"));
        
    }
}