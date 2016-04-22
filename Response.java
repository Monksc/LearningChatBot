import java.util.*;
public class Response {
    
    public Memory memoryWordsAroundIt = new Memory("wordsAroundIt");
    public Memory memoryWordsAssociateWith = new Memory("wordsAssociateWith");
    
    public Response() {
        
    }
    
    public String talk(String statement, String topic, int length) {
        statement = statement.toLowerCase();
        addStatement(statement);
        

        String response = getResponse(statement, topic, length);
        if (response.length() > 0) {
            return response;
        }
        
        return "k";
    }
    
    private String getResponse(String statement, String topic, int length) {
        String response = "";
        String lastWord = topic;
        
        for (int i = 0; i < length; i++) {
            ArrayList<String> wordBankWordsInSentence = getWords(memoryWordsAroundIt.getString(topic + ".txt"));
            ArrayList<String> wordBankWordsAroundWord = getWords(memoryWordsAssociateWith.getString(lastWord + ".txt"));
        
            String newWord = getSimilarWord(wordBankWordsInSentence, wordBankWordsAroundWord);
            System.out.print(newWord + " ");
            if (newWord == null) {
                return response;
            }
            else {
                response = response + newWord + " ";
                lastWord = newWord;
            }
        }
        
        return response;
    }
    private String getSimilarWord(ArrayList<String> wordBank1, ArrayList<String> wordBank2) {
        for (String w1: wordBank1) {
            for (String w2: wordBank2) {
                if (w1.equals(w2) && w1.length() > 0) {
                    if (w1.charAt(0) != ' ') {
                        return w1;
                    }
                }   
            }
        }
        
        return null;
    }
    
    private void addStatement(String statement) {
        ArrayList<String> partsOfStatement = new ArrayList<String>();
        
        int index = 0;
        while (getNextIndexOfCommaOrPeirod(statement, index) != -1) {
            int newIndex = getNextIndexOfCommaOrPeirod(statement, index + 1);
            
            partsOfStatement.add(statement.substring(index, newIndex));
            index = newIndex + 1;
            if (index == -1) index = statement.length();
        }
        partsOfStatement.add(statement.substring(index));
        
        addMemory(partsOfStatement);
    }
    
    private void addMemory(ArrayList<String> phrases) {
        for (String phrase: phrases) {
            ArrayList<String> words = getWords(phrase);
            for (String wordName: words) {
                for (String word: words) {
                    memoryWordsAroundIt.appendString(wordName + ".txt", word + " ");
                }
            }
            
            for (int i = 0; i < words.size() - 1; i++) {
                //memoryWordsAssociateWith.appendString(words.get(i) + ".txt", words.get(i-1) + " ");
                memoryWordsAssociateWith.appendString(words.get(i) + ".txt", words.get(i+1) + " ");
            }
            
            /*
            if (words.size() > 1) {
                memoryWordsAssociateWith.appendString(words.get(0) + ".txt", words.get(1) + " ");
                memoryWordsAssociateWith.appendString(words.get(words.size() - 1) + ".txt", words.get(words.size() - 2) + " ");
            }
            */
            
        }
    }
    
    private ArrayList<String> getWords(String sentence) {
        ArrayList<String> words = new ArrayList<String>();
        int index = 0;
        while (sentence.indexOf(" ", index) != -1) {
            int nextIndex = sentence.indexOf(" ", index + 1);
            if (nextIndex == -1) nextIndex = sentence.length();
            words.add(sentence.substring(index, nextIndex));
            index = nextIndex + 1;
        }
        words.add(sentence.substring(index));
        
        return words;
    }
    
    private int getNextIndexOfCommaOrPeirod(String str, int index) {
        int comma = str.indexOf(",", index);
        int period = str.indexOf(".", index);
        
        if (comma == -1) {
            return period;
        }
        
        if (period == -1) {
            return comma;
        }
        
        if (period > comma) {
            return comma;
        } else {
            return period;
        }
    }
    
}