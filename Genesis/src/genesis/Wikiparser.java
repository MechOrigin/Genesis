package genesis;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Wikiparser {

	public static String learned_words;

	public static String questiontype;
   
	public static int wordcount;
	public static String wordword;
	public static String nopuncwordword;

	public static FileManager fm;
	
public static void main() throws IOException {
	
		fm = new FileManager(null, null);
	
        long time = System.currentTimeMillis();

        Map<String, Word> countMap = new HashMap<String, Word>();

        //connect to wikipedia and get the HTML
//        System.out.println("Downloading page...");
//        Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
        
//        String query = "Lebron James cavaliers";
        
        String query = questiontype;
        
        String query2 = questiontype;

//        String url = "https://www.google.com/search?q=" + query + "&num=25";
        
//        String url = "https://www.google.com/search?q=" + query + "&num=3"; //&num=25 will return the top 25 results for a search query
        
        Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/" + query.replace(" ", "_").replace("who_is", "").replace("what_is_a", "").replace("what_is", "")).get();

//        Document doc = Jsoup
//                .connect(url)
//                .userAgent("Jsoup client")
//                .timeout(5000).get();

        //Get the actual text from the page, excluding the HTML
        String text = doc.body().text();

//        System.out.println("Analyzing text...");
        //Create BufferedReader so the words can be counted
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+");
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                Word wordObj = countMap.get(word);
                if (wordObj == null) {
                    wordObj = new Word();
                    wordObj.word = word;
                    wordObj.count = 0;
                    countMap.put(word, wordObj);
                }

                wordObj.count++;
            }
        }

        reader.close();
        
        SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
        int i = 0;
        int maxWordsToDisplay = 500;

        String[] wordsToIgnore = {/*"the", "and", "a", "in", "of", "www", "The", "for", "s", "com", "https", "Cached", "to", "at", "by", "on", "an",
        							"�", "is", "ago", "are", "or", "Is", "his", nopuncwordword, wordword, "has"*/};
        
        

        for (Word word : sortedWords) {
            if (i >= maxWordsToDisplay) { //10 is the number of words you want to show frequency for
                break;
            }
            
            wordcount = word.count;
            wordword = word.word;
            nopuncwordword = removeEndPunc(word.word);

//            if(Wikiparser.wordcount > 11) {
            if (Arrays.asList(wordsToIgnore).contains(word.word)) {
                i++;
                maxWordsToDisplay++;
            } else {
//                System.out.println(word.count + "\t" + word.word);
////            	fm.setResponse(ResponseType.LEARNED_WORDS, removeEndPunc(word.word));
                if(Wikiparser.wordcount > 9) {
//                fm.setResponse(ResponseType.MOST_RELEVANT_DATA, removeEndPunc(word.word), Wikiparser.wordcount);
//                fm.setResponse(ResponseType.QUERRY2, removeEndPunc(word.word));
//                fm.setResponse(ResponseType.MOST_RELEVANT_DATA, removeEndPunc(word.word));
                }
                i++;
            }
//            }
        }

        time = System.currentTimeMillis() - time;

//        System.out.println("Finished in " + time + " ms");
        
        //this is another wikiparser..lol turned OG wikiparser into a googleparser...
        /*
        String subject = query;
        Document doc2 = Jsoup.connect("http://en.wikipedia.org/wiki/" + subject.replace(" ", "_")).get();
        Element contentDiv = doc2.select("div[id=content]").first();
        contentDiv.toString(); // The result
        System.out.println(contentDiv.text()); //also the result :)
        //end
        */  

Document doc3 = Jsoup.connect("http://en.wikipedia.org/wiki/" + query.replace(" ", "_").replace("who_is_", "").replace("what_is_a", "").replace("what_is", "")).get();
    Elements paragraphs = doc3.select(".mw-content-ltr p");

    Element firstParagraph = paragraphs.first();
    Element p;
    int ix=1;
    p=firstParagraph;
//    System.out.println(p.text());
        p=paragraphs.get(ix);
//        System.out.println("WikiParse:" + " " + p.text());
        fm.setResponse(ResponseType.MOST_RELEVANT_DATA, p.text().replace("[1]", "").replace("[2]", ""));
        ix++;


    }

    public static class Word implements Comparable<Word> {
        String word;
        int count;

        @Override
        public int hashCode() { return word.hashCode(); }

        @Override
        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }

        @Override
        public int compareTo(Word b) { return b.count - count; }
    }
    
    private static String removeEndPunc(String s) {
        return s.replaceAll("[!\\.\\?]+$", ""); //remove all !'s .'s and ?'s from the end of a string
    }

}