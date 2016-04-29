import java.util.Scanner;
public class ComputerOnComputerConversationMain {
    
    public static Response2 computer1 = new Response2("memoryForNicerBot");
    public static Response computer2 = new Response();
    public static Scanner scanner = new Scanner(System.in);
    
    
    public static void main(String[] args) {
        String firstPhraseSaidByComputer2 = "I hate Trump";
        System.out.println("Computer 2: " + firstPhraseSaidByComputer2);
        computer1(firstPhraseSaidByComputer2);
    }
    
    public static void computer1(String statement) {
        
        String response = computer1.talk(statement);
        
        System.out.println("Computer 1: " + response);
        
        scanner.nextLine();
        
        computer2(response.trim());
    }
    
    public static void computer2(String statement) {
        
        String response = computer2.talk(statement);
        
        System.out.println("Computer 2: " + response);
        
        scanner.nextLine();
        
        computer1(response.trim());
    }
}