package sample;

/**
 * Description: This function uses clues to scrape the data from the Datamuse API.
 * DataScraper Class
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.json.parsers.JsonParserFactory;
import com.json.parsers.JSONParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DataScraper {

    public static ArrayList<String> results;
    private JSONParse ParseJSON;
    Merriam mer;

    public DataScraper() {
        ParseJSON = new JSONParse();
        results = new ArrayList<String>();
    }

    public String findSimilarWords(String word) {
        String s;
        s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?rd=" + s);
    }

    private String getJSON(String url) {
        URL data_muse_url;
        URLConnection dc;
        StringBuilder s = null;
        try {
            data_muse_url = new URL(url);
            dc = data_muse_url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(dc.getInputStream(), "UTF-8"));
            String inputLine;
            s = new StringBuilder();
            while(!in.ready());
            while ((inputLine = in.readLine()) != null)
                s.append(inputLine);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s != null ? s.toString() : null;
    }

    public void checkDatamuse(String key, int solnSize, int num) throws InterruptedException {
        String keepSame = key;
        key = key.replaceAll(" ", "+");

        if(!results.isEmpty())
            results.clear();

        String json;
        json = findSimilarWords(key);
        if(json.equals("[]"))
        {	}
        else
        {
            results = parseWords(json, solnSize);
        }
        System.out.println("Datamuse clue search = " + key);
        System.out.println();
        System.out.println("Search result as a JSON Format = " + json);
        System.out.println();
        System.out.println("Possible solution candidates for the clue: " + keepSame + ": " + results);
        for(int u = 0; u < results.size(); u++){
            if(results.get(u).length() != solnSize){
                results.remove(u);
            }
        }
        System.out.println();
    }

    public void setResults(ArrayList<String> results){
        this.results = results;
    }

    public ArrayList<String> getResults(){
        return results;
    }
    public void printCandidates(){
        for(int i = 0; i < results.size(); i++){
            System.out.println(results.get(i));
        }
    }

    /**
     * This function parses the words from raw data.
     * @param in
     * @return
     */
    public ArrayList<String> parseWords(String in, int size) {
        JsonParserFactory factory=JsonParserFactory.getInstance();
        JSONParser parser=factory.newJsonParser();
        Map jsonData=parser.parseJson(in);
        List al= (List) jsonData.get("root");
        String currentStr = "";
        ArrayList<String> results = new ArrayList<String>();
        for (int i = 0; i < al.size(); i++) {
            // We parsing the words from the raw data.
            currentStr = (String) ((Map)al.get(i)).get("word");
            if(currentStr.length() == size) {
                results.add(currentStr);
            }
        }
        return results;
    }
}
