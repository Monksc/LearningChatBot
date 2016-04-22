public class ResponseTester {
    
    public static Response response = new Response();
    
    public static void main(String[] args) {
        
        System.out.println(response.getWords(" Hello My name is Cameron "));
        System.out.println(response.getWords(" Hello "));
        
        
    }
    
}