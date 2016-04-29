import java.util.*;
import java.lang.*;
public class Response {
    
    public Memory memoryWordsAroundIt = new Memory("wordsAroundIt");
    public Memory memoryWordsAssociateWith = new Memory("wordsAssociateWith");
    public Memory memoryWordsFirstWord = new Memory("firstWord");
    
    public String firstWordOfComputerResponse = "";
    
    public final String DIR;
    
    public Response(String dirOfBot) {
        DIR = dirOfBot;
        
        memoryWordsAroundIt = new Memory(DIR + "/wordsAroundIt");
        memoryWordsAssociateWith = new Memory(DIR + "/wordsAssociateWith");
        memoryWordsFirstWord = new Memory(DIR + "/firstWord");
    }
    
    public String talk(String statement) {
        statement = statement.toLowerCase();
        addMemoryAsAFull(statement);
        
        if (firstWordOfComputerResponse.length() > 0) {
            addMemoryForFirstWord(statement);
        }
        
        String topic = getTopic(statement);
        String startingWord = getBestFirstWordResponse(statement, topic);
        
        if (startingWord == null){
            startingWord = topic;
        }

        String response = getResponse(statement, topic, startingWord);
        if (response.length() > 0) {
            if (startingWord.indexOf("?") == -1 && startingWord.indexOf("!") == -1 && startingWord.indexOf(".") == -1) {
                response = startingWord + " " + response;
                firstWordOfComputerResponse = startingWord;
            }
            else if (response.indexOf(" ") != -1) {
                firstWordOfComputerResponse = response.substring(0, response.indexOf(" "));
            }
            else {
                firstWordOfComputerResponse = response;
            }
            
            return response;
        }
        
        firstWordOfComputerResponse = "k";
        
        return "k";
    }
    
    private String getResponse(String statement, String topic, String startingWord) {
        String response = "";
        String lastWord = startingWord;
        
        String wordsAssociateWith = getWordsAssociativeWith(getWords(statement));
        
        for (int i = 0; i < 50; i++) {
            ArrayList<String> wordBankWordsInSentence = getWords(memoryWordsAroundIt.getString(topic + ".txt"));
            ArrayList<String> wordBankWordsAroundWord = getWords(memoryWordsAssociateWith.getString(lastWord + ".txt").trim());
        
            //Collections.reverse(wordBankWordsInSentence);
            //Collections.reverse(wordBankWordsAroundWord);
        
            String newWord = getBestWord(wordBankWordsInSentence, wordBankWordsAroundWord, response, wordsAssociateWith);
            
            if (newWord == null) {
                return response;
            }
            else if (isThisWordInString(response, newWord)) {
                return response;
            }
            else {
                response = response + newWord + " ";
                lastWord = newWord;
            }
        }
        
        return response;
    }
    
    private String getBestWord(ArrayList<String> wordBank1, ArrayList<String> wordBank2, String response, String statementOfSimilarWords) {
        ArrayList<String> bestWords = new ArrayList<String>();
        
        for (String w1: wordBank1) {
            for (String w2: wordBank2) {
                if (w1.equals(w2) && w1.length() > 0) {
                    if (w1.charAt(0) != ' ') {
                        if ((" " + response + " ").indexOf(" " + w1 + " ") == -1) {
                            bestWords.add(w1);
                        }
                    }
                }   
            }
        }
        
        if (bestWords.size() > 0) {
            if (bestWords.size() > 1) {
                return mostRepetiveStringInString(bestWords, statementOfSimilarWords);
            }
            
            return bestWords.get(0);
        }
        
        return null;
    }
    private String getBestRandomWord(ArrayList<String> wordBank1, ArrayList<String> wordBank2, String response) {
        ArrayList<String> bestWords = new ArrayList<String>();
        
        for (String w1: wordBank1) {
            for (String w2: wordBank2) {
                if (w1.equals(w2) && w1.length() > 0) {
                    if (w1.charAt(0) != ' ') {
                        if ((" " + response + " ").indexOf(" " + w1 + " ") == -1) {
                            bestWords.add(w1);
                        }
                    }
                }   
            }
        }
        
        if (bestWords.size() > 0) {
            if (bestWords.size() > 20 && Math.random() > 0.1) {
                return bestWords.get(bestWords.size() - 1 - (int)(Math.random() * (bestWords.size() / 10)));
            }
            
            return bestWords.get((int)(Math.random() * bestWords.size()));
        }
        
        return null;
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
    private boolean isThisWordInString(String str, String word) {
        return (" " + str + " ").indexOf(" " + word + " ") != -1;
    }
    
    private void addMemoryAsAFull(String phrase) {
        ArrayList<String> words = getWords(phrase);
            for (String wordName: words) {
                for (String word: words) {
                    memoryWordsAroundIt.appendString(wordName + ".txt", word + " ");
                }
            }
            
            for (int i = 0; i < words.size() - 1; i++) {
                memoryWordsAssociateWith.appendString(words.get(i) + ".txt", words.get(i+1) + " ");
            }
            if (words.size() > 0) {
                memoryWordsAssociateWith.appendString(words.get(words.size() - 1) + ".txt", "");
            }   
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
                memoryWordsAssociateWith.appendString(words.get(i) + ".txt", words.get(i+1) + " ");
            }
            if (words.size() > 0) {
                memoryWordsAssociateWith.appendString(words.get(words.size() - 1) + ".txt", "");
            }            
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
    
    private String getRandomWordOutOfSentence(String str) {
        ArrayList<String> words = getWords(str);
        
        int randIndex = (int)(words.size() * Math.random());
        
        return words.get(randIndex);
    }
    
    private String getTopic(String str) {
        ArrayList<String> words = getWords(str);
        int lowest = 99999999;
        String bestWord = words.get(0);
        for (String word: words) {
            int length = memoryWordsAssociateWith.getString(word + ".txt").length();
            if (length < lowest) {
                lowest = length;
                bestWord = word;
            }
        }
        
        return bestWord;
    }

    private String mostRepetiveStringInString(ArrayList<String> wordBank, String str) {
        int highestCount = 0;
        String mostRepetiveWord = "";
        
        for (String word: wordBank) {
            int newCount = howManyTimesDoesWordGoIntoSentence(" " + word + " ", str);
            if (newCount > highestCount) {
                mostRepetiveWord = word;
                highestCount = newCount;
            }
        }
        
        return mostRepetiveWord;
    }

    private int howManyTimesDoesWordGoIntoSentence(String word, String str) {
        int index = str.indexOf(word);
        int count = 0;
        
        while (index != -1) {
            index = str.indexOf(word, index + 1);
            count++;
        }
        
        return count;
    }

    private String getWordsAssociativeWith(ArrayList<String> words) {
        String str = " ";
        
        for (String word: words) {
            str = str + memoryWordsAroundIt.getString(word + ".txt");
        }
        
        return str;
    }
    
    private void addMemoryForFirstWord(String statement) {
        if (statement.length() > 0) {
            if (statement.indexOf(" ") != -1) {
                memoryWordsFirstWord.appendString(firstWordOfComputerResponse + ".txt", statement.substring(0, statement.indexOf(" ")) + " ");
            }
        }
    }
    
    private ArrayList<String> getAllWordsThatGoIntoString(ArrayList<String> wordBank, String str) {
        str = " " + str.trim() + " ";
        ArrayList<String> newWordBank = new ArrayList<String>();
        for (String word: wordBank) {
            if ((" " + word + " ").indexOf(str) != -1) {
                newWordBank.add(word);
            }
        }
        
        return newWordBank;
    }
    
    private String getBestFirstWordResponse(String statement, String topic) {
        if (firstWordOfComputerResponse.length() > 0 && statement.indexOf(" ") != -1) {
            String options = memoryWordsFirstWord.getString(statement.substring(0, statement.indexOf(" ")) + ".txt");
            
            if (options != null) {
                if (options.length() > 0) {
                    ArrayList<String> words = getWords(options.substring(0, options.length() - 1));
                    
                    ArrayList<String> newSetOfWords = getAllWordsThatGoIntoString(words, memoryWordsAroundIt.getString(topic + ".txt"));
                    
                    if (newSetOfWords.size() > 0) {
                        words = newSetOfWords;
                    }
                    
                    if (words.size() > 20) {
                        int index = words.size() - (int)((1 - Math.pow(Math.random(), 2)) * 20);
                        return words.get(index);
                    }
                    else {
                        int index = (int)(Math.random() * words.size());
                        return words.get(index);
                    }
                }
            }
        }
        
        return null;
    }
}