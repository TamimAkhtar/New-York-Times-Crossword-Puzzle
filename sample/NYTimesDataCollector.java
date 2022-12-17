package sample;
/**
 * PLUTO - NY Times Crossword Puzzle Solver
 * CS461 Project
 * Description: This class basically collects data from the NYTimes website.
 * NYTimesDataCollector Class
 */
/**
 * Jsoup is used for this project for dealing with the html data.
 * Below links used for learning the functions. Also, the css keys are mainly obtained from the
 * extracted HTML data itself. We analyzed the extracted data and found css keys and
 * Reference: https://www.htmlgoodies.com/html5/other/web-page-scraping-with-jsoup.html
 * Reference: https://jsoup.org/apidocs/
 *
 */
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
public class NYTimesDataCollector {

    /**
     * Declaring Variables
     */
    int[] blockDesign;
    int[] blockNumbers;
    ArrayList<String> puzzleCluesStr;
    ArrayList<PuzzleClue> puzzleCluesAcross;
    ArrayList<PuzzleClue> puzzleCluesDown;
    ArrayList<String> letters;
    ArrayList<PuzzleClue> puzzleClues;
    ArrayList<Integer> solutionLengths;
    ArrayList<String> traceWindowInput;
    String url;
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    String strDate = dateFormat.format(date);
    String file = strDate;
    String solutionFile = strDate + " - solution";
    String answers = "";
    String pulledData = "";

    public NYTimesDataCollector() throws IOException{
        // Initializing Variables
        puzzleClues = new ArrayList<>();
        puzzleCluesStr = new ArrayList<>();
        puzzleCluesAcross = new ArrayList<>();
        puzzleCluesDown = new ArrayList<>();
        traceWindowInput = new ArrayList<>();
        letters = new ArrayList<>();
        blockDesign = new int[25];
        blockNumbers = new int[25];
        solutionLengths = new ArrayList<>();
        url = "https://www.nytimes.com/crosswords/game/mini";
        pulledData = pullData(url);
    }

    /**
     * Getter and Setter Methods
     */
    public void setURL(String url){
        this.url = url;
    }
    public String getURL(){
        return url;
    }
    public int[] getBlockDesign(){
        return blockDesign;
    }
    public int[] getBlockNumbers(){
        return blockNumbers;
    }
    public void setPuzzleClues(ArrayList<PuzzleClue> puzzleClues){
        this.puzzleClues = puzzleClues;
    }
    public ArrayList<PuzzleClue> getPuzzleClues(){
        return puzzleClues;
    }
    public ArrayList<String> getPuzzleCluesStr(){
        return puzzleCluesStr;
    }
    public ArrayList<PuzzleClue> getPuzzleCluesAcross(){
        return puzzleCluesAcross;
    }
    public ArrayList<PuzzleClue> getPuzzleCluesDown(){
        return puzzleCluesDown;
    }
    public ArrayList<String> getLetters(){
        return letters;
    }
    public ArrayList<Integer> getSolutionLengths(){
        return solutionLengths;
    }
    public ArrayList<String> getTraceWindowInput(){
        return traceWindowInput;
    }

    /**
     * This method basically pulls all the data that we need for creating the puzzle.
     * And we are storing it to the local memory for further using, positioning of the black cells
     * and cell numbers.
     * @param url
     * @return
     */
    public String pullData(String url){
        try{
            String str = "Pulling the necessary information from website " +
                    "for creating our version of the puzzle in local.\n";
            traceWindowInput.add(str);
            System.out.println(str);
            Document document = Jsoup.connect(url).get();
            String documentToString = document.toString();
            // Getting necessary information about the puzzle with these keywords.
            // These are css keywords used in HTML.
            Elements websiteLinks = document.select("link");
            Elements websiteScripts = document.select("script");
            for(Element element: websiteLinks){
                documentToString += element.absUrl("href");
            }
            for(Element element: websiteScripts){
                documentToString += element.absUrl("src");
            }
            String str1 = "Saving html raw data to the local memory named with --> " + file + ".txt\n";
            traceWindowInput.add(str1);
            System.out.println(str1);
            // Save the data to the text file
            String filePath = "C:\\Users\\berkb\\Desktop\\Workspaces\\IntelliJ Workspace\\PLUTO\\src\\sample\\Collected Data\\" + file + ".txt";
            // Creating the file with the pulled data
            Files.write(Paths.get(filePath), documentToString.getBytes(), StandardOpenOption.CREATE);

            /**
             * Processing the extracted html for certain information
             */
            processData(documentToString);
            return documentToString;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Data couldn't pulled.";
        }
    }

    /**
     * This function processes the pulled data from the New York Times website.
     * @param str
     */
    public void processData(String str) throws InterruptedException {
        String str1 = "Processing HTML raw data has started.\n";
        System.out.println(str1);
        traceWindowInput.add(str1);
        Thread.sleep(1000);
        extractBlockDesign(str);
        Thread.sleep(1000);
        extractBlockNumbers(str);
        Thread.sleep(1000);
        try{
            collectThePuzzleInfo();
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
        extractHints(str);
        Thread.sleep(1000);
        prepareSolutionForShow();
        Thread.sleep(1000);
        setWordLengths();
        Thread.sleep(1000);
        identifyLetterPosition();
        Thread.sleep(1000);
    }

    /**
     * This function extracts the block design from the html data.
     */
    public void extractBlockDesign(String str){
        String str1 = "First, 5x5 grid design extraction from the HTML raw data is started.\n";
        System.out.println(str1);
        traceWindowInput.add(str1);
        // First, we need to reach the block design code piece of the HTML code.
        // We need to search cells in the HTML code to get the block design.
        String designStartInHTML = "<g data-group=\"cells\" role=\"table\">";
        String designFinishInHTML = "<g data-group=\"grid\">";
        // We need take these part of the string, so we use substring
        int designStartIndex = str.indexOf(designStartInHTML) + designStartInHTML.length();
        int designFinishIndex = str.indexOf(designFinishInHTML) - 1;
        // Extracted Data
        String extractedBlockDesign = str.substring(designStartIndex, designFinishIndex);
        // We need to traverse the each "class" in the html code to get the design of the blocks.
        for(int i = 0; i < 25; i++){
            // Design is provided in the website with Cell-block--1oNa class. So we need the read it.
            int start = extractedBlockDesign.indexOf("\" class=\"") + 9;
            // Design is 16 characters, we only read it.
            int end = start + 17;
            String design = extractedBlockDesign.substring(start, end);
            if(design.equals("Cell-block--1oNaD")){
                blockDesign[i] = 0; // black blocks
            }else{
                blockDesign[i] = 1; // white blocks
            }
            //We trimming the read part from the string
            extractedBlockDesign = extractedBlockDesign.substring(end);
        }
        String str2 = "5x5 grid design extraction is completed.\n";
        System.out.println(str2);
        traceWindowInput.add(str2);
        String str3 = "Design of block is below, 0 indicates black cells.\n";
        System.out.println(str3);
        traceWindowInput.add(str3);
        String design = "Starting from Cell 0 --> ";
        for(int i = 0; i < blockDesign.length; i++){
            design += blockDesign[i] + " ";
        }
        design += " <-- Last cell\n";
        System.out.println(design);
        traceWindowInput.add(design);
    }

    /**
     * This function basically extracts the number data from the html raw data.
     * This code is mainly borrowed from the internet.
     */
    public void extractBlockNumbers(String str){
        String str1 = "Second, extraction of the 5x5 grid puzzle numbers is started.\n";
        System.out.println(str1);
        traceWindowInput.add(str1);
        // First, we need to reach the block design code piece of the HTML code.
        // We need to search cells in the HTML code to get the block design.
        String designStartInHTML = "<g data-group=\"cells\" role=\"table\">";
        String designFinishInHTML = "<g data-group=\"grid\">";
        // We need take these part of the string, so we use substring
        int designStartIndex = str.indexOf(designStartInHTML) + designStartInHTML.length();
        int designFinishIndex = str.indexOf(designFinishInHTML) - 1;
        // Extracted Data
        String extractedBlockNumbers = str.substring(designStartIndex, designFinishIndex);
        Document document = Jsoup.parse(extractedBlockNumbers);
        Elements elements = document.getElementsByTag("g");
        for (int i = 0; i < elements.size() && i < 25; i++) {
            String text = elements.get(i).text();
            if (!text.isEmpty()) {
                text = text.substring(0, 1);
                if( text.matches("-?\\d+"))
                    blockNumbers[i] = Integer.parseInt(text);
                else
                    blockNumbers[i] = 0;
            } else
                blockNumbers[i] = 0;
        }
        String str2 = "Extraction of the 5x5 grid puzzle numbers is completed.\n";
        System.out.println(str2);
        traceWindowInput.add(str2);
        String str3 = "Design of block numbers is below, 0 indicates blank cells.\n";
        System.out.println(str3);
        traceWindowInput.add(str3);
        String design = "Starting from Cell 0 --> ";
        for(int i = 0; i < blockNumbers.length; i++){
            design += blockNumbers[i] + " ";
        }
        design += " <-- Last cell\n";
        System.out.println(design);
        traceWindowInput.add(design);
    }

    /**
     * This function gets the today's puzzle with accessing the website.
     */
    public void collectThePuzzleInfo() throws IOException, InterruptedException {
        String str1 = "Third, by connecting to the New York Times Website, pressing the reveal button, the solutions will be extracted from HTML data.\n";
        System.out.println(str1);
        traceWindowInput.add(str1);
        // Opening Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\berkb\\Desktop\\Workspaces\\IntelliJ Workspace\\PLUTO\\src\\External Libraries\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.nytimes.com/crosswords/game/mini");

        // Pressing ok button first
        driver.findElement(By.xpath("//button[@aria-label='OK']")).click();
        driver.findElement(By.xpath("//button[@aria-label='reveal']")).click();
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div[4]/div/main/div[2]/div/div/ul/div[2]/li[2]/ul/li[3]/a")).click();
        driver.findElement(By.xpath("//button[@aria-label='Reveal']")).sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/span")).click();
        Thread.sleep(2000);

        List<WebElement> links = driver.findElements(By.tagName("text"));
        String path = "C:\\Users\\berkb\\Desktop\\Workspaces\\IntelliJ Workspace\\PLUTO\\src\\sample\\Collected Data\\" + solutionFile + ".txt";



        for(WebElement link : links){
            if(!link.getText().matches("-?\\d+")){
                answers += link.getText();
                if(!link.getText().isEmpty())
                    answers += "\n";
            }
        }

        // Save answers to the text file
        Files.write(Paths.get(path), answers.getBytes(), StandardOpenOption.CREATE);
        showRevealedPuzzle(path);
        Thread.sleep(2000); // For listening the music!
        driver.quit();
        String str2 = "Process completed. Solutions is saved to the file named --> " + solutionFile + ".txt.\n";
        System.out.println(str2);
        traceWindowInput.add(str2);
    }

    /**
     * This function reads the extracted data from the website and creates the solutions.
     * @return
     */
    public ArrayList<String> showRevealedPuzzle(String answerFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(answerFile));
        String line = "";
        for(int i = 0; i < 25; i++){
            if(blockDesign[i] == 0){
                letters.add("");
            }else{
                if((line = reader.readLine()) != null)
                    letters.add(line);
            }
        }
        return letters;
    }

    /**
     * This function extracts the hints from collected html data.
     * @param str
     */
    public void extractHints(String str){
        String str2 = "Fourth, extracting clues from HTML raw data will be completed.";
        System.out.println(str2);
        traceWindowInput.add(str2);
        Document doc = Jsoup.parse(str);
        // This
        Elements clueNumbers = doc.getElementsByClass("Clue-label--2IdMY");
        Elements clues = doc.getElementsByClass("Clue-text--3lZl7");
        int i = 0;
        while(i < clueNumbers.size() && i < clues.size()){
            puzzleCluesStr.add(clueNumbers.get(i).text() + ". " + clues.get(i).text());
            if(i < 5){
                PuzzleClue tempClue = new PuzzleClue("Across", Integer.parseInt(clueNumbers.get(i).text()), clues.get(i).text());
                tempClue.setClueNumber(Integer.parseInt(clueNumbers.get(i).text()));
                this.puzzleClues.add(tempClue);
            }else{
                PuzzleClue tempClue2 = new PuzzleClue("Down", Integer.parseInt(clueNumbers.get(i).text()) ,clues.get(i).text());
                tempClue2.setClueNumber(Integer.parseInt(clueNumbers.get(i).text()));
                this.puzzleClues.add(tempClue2);
            }
            i++;
        }
        String str3 = "Extracted puzzle clues are listed in below:\n";
        System.out.println(str3);
        traceWindowInput.add(str3);
        String clue = "Across Clues:\n";
        for(int u = 0; u < puzzleClues.size(); u++){
            if(u == 5)
                clue +=  "Down Clues:\n";
            clue += puzzleClues.get(u).printPuzzleClue() + "\n";
        }
        System.out.println(clue);
        traceWindowInput.add(clue);
    }

    /**
     * This function also will be used for showing our solution.
     * We identify the size of the Across clues.
     */
    public void prepareSolutionForShow(){
        int word1Length = 0;
        int word2Length = 0;
        int word3Length = 0;
        int word4Length = 0;
        int word5Length = 0;
        for(int i = 0; i < 5; i++){
            if(blockDesign[i] == 1)
                word1Length++;
        }
        solutionLengths.add(word1Length);
        for(int j = 5; j < 10; j++){
            if(blockDesign[j] == 1)
                word2Length++;
        }
        solutionLengths.add(word2Length);
        for(int x = 10; x < 15; x++){
            if(blockDesign[x] == 1)
                word3Length++;
        }
        solutionLengths.add(word3Length);
        for(int u = 15; u < 20; u++){
            if(blockDesign[u] == 1)
                word4Length++;
        }
        solutionLengths.add(word4Length);
        for(int a = 20; a < 25; a++){
            if(blockDesign[a] == 1)
                word5Length++;
        }
        solutionLengths.add(word5Length);
    }

    /**
     * setWordLengths Function
     * This function sets the solutions and its lengths and store
     * them to the related puzzle clue object.
     */
    public void setWordLengths(){
        // We will traverse each clue
        for(int x = 0; x < puzzleClues.size(); x++){
            // Index is the number of the clue.
            int index = puzzleClues.get(x).getClueNumber();
            int i = 0;
            int count = 0;
            String str = "";
            // We are trying the find the clue position in the blockNumbers list.
            while(blockNumbers[i] != index){
                i++;
            }
            // Across Clues
            str += letters.get(i);
            if(puzzleClues.get(x).getClueValue().equals("Across")) {
                do {
                    count++;
                    i++;
                    if(i < 25){
                        str += letters.get(i);
                    }
                }
                // We keep counting and setting the solution while we are in the same row and
                // color of the cell is not black.
                while (i % 5 != 0 && blockDesign[i] != 0);
                puzzleClues.get(x).setSolutionLength(count);
                if(str.length() > count)
                    str = str.substring(0, count);
                puzzleClues.get(x).setItsSolution(str);
            }
            /**
             * If the clue value is "Down".
             * Do - while will continue within the grid and block design is not black.
             *
             */
            else{
                do{
                    i += 5;
                    count++;
                    if(i < 25)
                        str += letters.get(i);
                }while( i<25 && blockDesign[i] != 0);
                // Setting the solution length
                puzzleClues.get(x).setSolutionLength(count);
                // Debugging
                if(str.length() > count)
                    str = str.substring(0, count);
                puzzleClues.get(x).setItsSolution(str);
            }
        }
        String a = "Solutions of clues and their lengths are identified. See below:\n";
        System.out.println(a);
        traceWindowInput.add(a);
        String soln = "";
        for(int n = 0; n < puzzleClues.size(); n++){
            soln += "Solution of " + puzzleClues.get(n).getClueValue() + " Clue " + puzzleClues.get(n).getClueNumber() + " is --> " + puzzleClues.get(n).getItsSolution() + " -- Its Length is: " +
                    puzzleClues.get(n).getSolutionLength() + "\n";
        }
        System.out.print(soln);
        traceWindowInput.add(soln);
    }

    /**
     * This function identifies the starting points of the solutions.
     * We will need it on showing our version of the puzzle.
     */
    public void identifyLetterPosition(){
        for(int i = 0; i < puzzleClues.size(); i++){
            PuzzleClue tempClue = puzzleClues.get(i);
            int clueNumber = tempClue.getClueNumber();
            int index;
            for(index = 0; index < 25; index++){
                if(blockNumbers[index] == clueNumber)
                    break;
            }
            /**
             * These coordinates indicates the starting points of the
             * solutions. It is needed for showing our solution.
             */
            int x = index % 5;
            int y = index / 5;
            puzzleClues.get(i).setLetterCoordinates(x,y);
        }
    }
}