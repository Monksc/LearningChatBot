import java.util.*;
import java.lang.*;
public class Response2 {
    
    public Memory memoryWordsAroundIt = new Memory("wordsAroundIt");
    public Memory memoryWordsAssociateWith = new Memory("wordsAssociateWith");
    public Memory memoryWordsFirstWord = new Memory("firstWord");
    
    public String firstWordOfComputerResponse = "";
    
    public final String DIR;
    
    private final static boolean CREATE_MEMORY = false;
    
    public Response2(String dirOfBot) {
        DIR = dirOfBot;
        
        memoryWordsAroundIt = new Memory(DIR + "/wordsAroundIt");
        memoryWordsAssociateWith = new Memory(DIR + "/wordsAssociateWith");
        memoryWordsFirstWord = new Memory(DIR + "/firstWord");
    }
    
    public String talk(String statement) {
        statement = statement.toLowerCase();
        
        if (CREATE_MEMORY) {
            addMemoryAsAFull(statement);
        
            if (firstWordOfComputerResponse.length() > 0) {
                addMemoryForFirstWord(statement);
            }
        }
        
        String topic = getTopic(statement);
        String startingWord = getBestFirstWordResponse(statement, topic);
        
        int indexOfMarkInTopic = getNextIndexOfCommaOrPeirod(topic, 0);
        if (indexOfMarkInTopic != -1) {
            topic = topic.substring(0, indexOfMarkInTopic);
        }
        
        if (startingWord == null){
            startingWord = topic;
        }

        String response = getResponse(statement, topic, startingWord);
        if (response.length() > 0) {
            if (getNextIndexOfCommaOrPeirod(startingWord, 0) == -1 || !startingWord.equals(topic)) {
                response = startingWord + " " + response;
                firstWordOfComputerResponse = startingWord;
            }
            else if (response.indexOf(" ") != -1) {
                firstWordOfComputerResponse = response.substring(0, response.indexOf(" "));
            }
            else {
                firstWordOfComputerResponse = response;
            }
            
            return fixeUpResponseBeforeSendingIt(response);
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
            else {
                response = response + newWord + " ";
                lastWord = newWord;
            }
        }
        
        return response;
    }
    
    private String getBestWord(ArrayList<String> wordBank1, ArrayList<String> wordBank2, String response, String statementOfSimilarWords) {
        ArrayList<String> bestWords = new ArrayList<String>();
        ArrayList<String> extraWords = new ArrayList<String>();
        
        for (String w1: wordBank1) {
            for (String w2: wordBank2) {
                if (w1.equals(w2) && w1.length() > 0) {
                    if (w1.charAt(0) != ' ') {
                        if ((" " + response + " ").indexOf(" " + w1 + " ") != -1) {
                            extraWords.add(w1);
                        }
                        else {
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
        
        if (extraWords.size() > 0) {
            if (extraWords.size() > 1) {
                return mostRepetiveStringInString(extraWords, statementOfSimilarWords);
            }
            
            return extraWords.get(0);
        }
        
        return null;
    }
    
    private void addMemoryAsAFull(String phrase) {
        ArrayList<String> words = getWords(phrase);
            for (String wordName: words) {
                for (String word: words) {
                    int index = getNextIndexOfCommaOrPeirod(wordName, 0);
                    if (index != -1) {
                        memoryWordsAroundIt.appendString(wordName.substring(0, index) + ".txt", word + " ");
                    }
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
        String[] punctuations = {".", ",", "!", "?"};
        int lowestIndex = 99999;
        
        for (String punc: punctuations) {
            int indexOfPunc = str.indexOf(punc, index) ;
            if (indexOfPunc != -1 && indexOfPunc < lowestIndex) {
                lowestIndex = indexOfPunc;
            }
        }
        
        if (lowestIndex == 99999) {
            return -1;
        }
        return lowestIndex;
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

    private String fixeUpResponseBeforeSendingIt(String response) {
        
        int oldIndex = 0;
        String lastSentence = "";
        while (getNextIndexOfCommaOrPeirod(response, oldIndex) != -1) {
            int newIndex = getNextIndexOfCommaOrPeirod(response, oldIndex) + 1;
            if (lastSentence.equals(response.substring(oldIndex, newIndex))) {
                return response.substring(0, oldIndex);
            }
            lastSentence = response.substring(oldIndex, newIndex);
            oldIndex = newIndex;
        }
        
        return response;
    }
}