package sample;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * PLUTO
 * Description: This is the core class. GUI is made here and also constraint satisfaction algorithm
 * is here.
 * Controller Class
 */

public class Controller implements Initializable{

    // Declaring Other Attributes
    NYTimesDataCollector collector;
    int[] blockDesign;
    int[] blockNumbers;

    @FXML
    private Label dateTimeLabel;
    @FXML
    private Rectangle nytcell0, nytcell1, nytcell2, nytcell3, nytcell4, nytcell5, nytcell6, nytcell7, nytcell8, nytcell9, nytcell10;
    @FXML
    private Rectangle nytcell11, nytcell12, nytcell13, nytcell14, nytcell15, nytcell16, nytcell17, nytcell18, nytcell19, nytcell20;
    @FXML
    private Rectangle nytcell21, nytcell22, nytcell23, nytcell24;
    @FXML
    private Rectangle plutocell0, plutocell1, plutocell2, plutocell3, plutocell4, plutocell5, plutocell6, plutocell7, plutocell8, plutocell9, plutocell10;
    @FXML
    private Rectangle plutocell11, plutocell12, plutocell13, plutocell14, plutocell15, plutocell16, plutocell17, plutocell18, plutocell19, plutocell20;
    @FXML
    private Rectangle plutocell21, plutocell22, plutocell23, plutocell24;
    @FXML
    private Label nytblocknum0, nytblocknum1, nytblocknum2, nytblocknum3, nytblocknum4, nytblocknum5, nytblocknum6, nytblocknum7, nytblocknum8, nytblocknum9, nytblocknum10;
    @FXML
    private Label nytblocknum11, nytblocknum12, nytblocknum13, nytblocknum14, nytblocknum15, nytblocknum16, nytblocknum17, nytblocknum18, nytblocknum19, nytblocknum20, nytblocknum21;
    @FXML
    private Label nytblocknum22, nytblocknum23, nytblocknum24;
    @FXML
    private Label plutoblocknum0, plutoblocknum1, plutoblocknum2, plutoblocknum3, plutoblocknum4, plutoblocknum5, plutoblocknum6, plutoblocknum7, plutoblocknum8, plutoblocknum9, plutoblocknum10;
    @FXML
    private Label plutoblocknum11, plutoblocknum12, plutoblocknum13, plutoblocknum14, plutoblocknum15, plutoblocknum16, plutoblocknum17, plutoblocknum18, plutoblocknum19, plutoblocknum20;
    @FXML
    private Label plutoblocknum21, plutoblocknum22, plutoblocknum23, plutoblocknum24;
    @FXML
    public TextFlow trace_window;
    @FXML
    private VBox consoleVbox;
    @FXML
    private Label nytsolucell0, nytsolucell1, nytsolucell2, nytsolucell3, nytsolucell4, nytsolucell5, nytsolucell6, nytsolucell7, nytsolucell8, nytsolucell9, nytsolucell10;
    @FXML
    private Label nytsolucell11, nytsolucell12, nytsolucell13, nytsolucell14, nytsolucell15, nytsolucell16, nytsolucell17, nytsolucell18, nytsolucell19, nytsolucell20, nytsolucell21;
    @FXML
    private Label nytsolucell22, nytsolucell23, nytsolucell24;
    @FXML
    private Label plutocellsol0, plutocellsol1, plutocellsol2, plutocellsol3, plutocellsol4, plutocellsol5, plutocellsol6, plutocellsol7, plutocellsol8, plutocellsol9, plutocellsol10, plutocellsol11;
    @FXML
    private Label plutocellsol12, plutocellsol13, plutocellsol14, plutocellsol15, plutocellsol16, plutocellsol17, plutocellsol18, plutocellsol19, plutocellsol20, plutocellsol21;
    @FXML
    private Label plutocellsol22, plutocellsol23, plutocellsol24;

    public ArrayList<Rectangle> solutionRectangle;
    public ArrayList<Rectangle> plutoRectangle;
    public ArrayList<Label> nytimesBlockNum;
    public ArrayList<Label> plutoBlockNum;
    public ArrayList<Label> clueLabels;
    public ArrayList<PuzzleClue> todaysClues;
    public ArrayList<Integer> clueSolutionLength;
    public ArrayList<String> solutionPositions;
    public ArrayList<Label> nytSollCellList;
    public ArrayList<Label> plutoSollCellList;
    public ArrayList<Constraint> constraints;
    public ArrayList<PuzzleClue> plutoSolutions;
    public ArrayList<String> traceWindowInputs;
    public DataScraper dataScraper;

    /**
     * initialize Function
     * This function is used for initializing opening window
     * @param url ResourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle ResourceBundle) {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
            dateTimeLabel.setTextAlignment(TextAlignment.CENTER);
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        String str = "Welcome to the PLUTO.\n";
        Text a = new Text();
        a.setStyle("-fx-font-weight:bold;-fx-font-size: 20");
        a.setText(str);
        trace_window.getChildren().add(a);
        traceWindowInputs = new ArrayList<>();

    }

    /**
     * changeLog Function
     * This function is used for updating the Trace Window.
     * @param str
     */
    public void changeLog(String str){
        Text a = new Text();
        a.setStyle("-fx-font-weight:bold;-fx-font-size: 20");
        a.setText(str);
        trace_window.getChildren().add(a);
    }

    /**
     * Solve The Puzzle Button Function Event
     * Description: When the user clicks this button, PLUTO first prepares the candidates for the each clue.
     * Then, it filters these candidates according to the length obtained from the 5x5 grid.
     * After that, provideSolution() function is called and it goes on until finds the correct version of the puzzle.
     * If one solution is false, satisfyConstraint() function will be called again and everything will be start over.
     * Last, after solving puzzle for all the clues, showSolution() function is called and
     * solution is shown.
     * @param event
     */
    public void pressButton(MouseEvent event) throws InterruptedException {
        try {
            Thread.sleep(2000);
            prepareCandidates();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < todaysClues.size(); i++){
            int size = todaysClues.get(i).sizeOfSolutionCandidates();
            int solnLength = todaysClues.get(i).getSolutionLength();
            ArrayList<String> cand = new ArrayList<>();

        }

        System.out.println("Printing Possible Solution Candidates for Each Hint");
        for(int u = 0; u < todaysClues.size(); u++){
            System.out.println(todaysClues.get(u).getClueValue() + " --> " + todaysClues.get(u).getClueNumber() + ". " + todaysClues.get(u).getClueText());
            System.out.println(todaysClues.get(u).getSolutionCandidates());
        }

        /**
         * Creating constraints object for satisfyConstraint method
         */
        constraints = new ArrayList<>();
        for(PuzzleClue c: todaysClues){
            constraints.add(new Constraint(c));
        }

        /**
         * Before calling the satisfyConstraint function, we filter
         * the candidates according to their sizes.
         */
        /*changeLog("Filtering the Candidates according to the sizes \n");
        for(int i = 0; i < todaysClues.size(); i++){
            todaysClues.get(i).filterCandidates();
        }*/

        /**
         * Calling main function for solving the puzzle
         */
        for(int u = 0; u < todaysClues.size(); u++){
            ArrayList<String> newList = removeDuplicates(todaysClues.get(u).getSolutionCandidates());
            todaysClues.get(u).setSolutionCandidates(newList);
        }

        provideSolution();
        // Showing solutions on the right side of the frame
        changeLog("Execution completed. PLUTO result can be seen at the right side of the frame.\n");
        System.out.println("Execution completed. PLUTO result can be seen at the right side of the frame.");
        showSolutions();

    }

    /**
     * This function downloads the puzzle and shows it to the left side of the window.
     * @param event
     */
    public void downloadPuzzle(MouseEvent event) throws IOException {
        // Initializing the Puzzle first
        try{
            collector = new NYTimesDataCollector();
        }catch(IOException e){
            e.printStackTrace();
        }
        // Getting the data from collector class for showing and solving the puzzle.
        blockDesign = collector.getBlockDesign();
        blockNumbers = collector.getBlockNumbers();
        todaysClues = collector.getPuzzleClues();
        clueSolutionLength = collector.getSolutionLengths();
        solutionPositions = collector.getLetters();

        traceWindowInputs = collector.getTraceWindowInput();
        for(int z = 0; z < traceWindowInputs.size(); z++){
            changeLog(traceWindowInputs.get(z));
        }

        // Initializing the solution Rectangle
        solutionRectangle = new ArrayList<>();
        solutionRectangle.add(nytcell0); solutionRectangle.add(nytcell1); solutionRectangle.add(nytcell2); solutionRectangle.add(nytcell3);
        solutionRectangle.add(nytcell4); solutionRectangle.add(nytcell5); solutionRectangle.add(nytcell6); solutionRectangle.add(nytcell7);
        solutionRectangle.add(nytcell8); solutionRectangle.add(nytcell9); solutionRectangle.add(nytcell10); solutionRectangle.add(nytcell11);
        solutionRectangle.add(nytcell12); solutionRectangle.add(nytcell13); solutionRectangle.add(nytcell14); solutionRectangle.add(nytcell15);
        solutionRectangle.add(nytcell16); solutionRectangle.add(nytcell17); solutionRectangle.add(nytcell18); solutionRectangle.add(nytcell19);
        solutionRectangle.add(nytcell20); solutionRectangle.add(nytcell21); solutionRectangle.add(nytcell22); solutionRectangle.add(nytcell23); solutionRectangle.add(nytcell24);

        // Initializing the nyt block numbers
        nytimesBlockNum = new ArrayList<>();
        nytimesBlockNum.add(nytblocknum0); nytimesBlockNum.add(nytblocknum1); nytimesBlockNum.add(nytblocknum2); nytimesBlockNum.add(nytblocknum3); nytimesBlockNum.add(nytblocknum4);
        nytimesBlockNum.add(nytblocknum5); nytimesBlockNum.add(nytblocknum6); nytimesBlockNum.add(nytblocknum7); nytimesBlockNum.add(nytblocknum8); nytimesBlockNum.add(nytblocknum9);
        nytimesBlockNum.add(nytblocknum10); nytimesBlockNum.add(nytblocknum11); nytimesBlockNum.add(nytblocknum12); nytimesBlockNum.add(nytblocknum13); nytimesBlockNum.add(nytblocknum14);
        nytimesBlockNum.add(nytblocknum15); nytimesBlockNum.add(nytblocknum16); nytimesBlockNum.add(nytblocknum17); nytimesBlockNum.add(nytblocknum18); nytimesBlockNum.add(nytblocknum19);
        nytimesBlockNum.add(nytblocknum20); nytimesBlockNum.add(nytblocknum21); nytimesBlockNum.add(nytblocknum22); nytimesBlockNum.add(nytblocknum23); nytimesBlockNum.add(nytblocknum24);

        // Initializing the PLUTO rectangle
        plutoRectangle = new ArrayList<>();
        plutoRectangle.add(plutocell0); plutoRectangle.add(plutocell1); plutoRectangle.add(plutocell2); plutoRectangle.add(plutocell3); plutoRectangle.add(plutocell4);
        plutoRectangle.add(plutocell5); plutoRectangle.add(plutocell6); plutoRectangle.add(plutocell7); plutoRectangle.add(plutocell8); plutoRectangle.add(plutocell9);
        plutoRectangle.add(plutocell10); plutoRectangle.add(plutocell11); plutoRectangle.add(plutocell12); plutoRectangle.add(plutocell13); plutoRectangle.add(plutocell14);
        plutoRectangle.add(plutocell15); plutoRectangle.add(plutocell16); plutoRectangle.add(plutocell17); plutoRectangle.add(plutocell18); plutoRectangle.add(plutocell19);
        plutoRectangle.add(plutocell20); plutoRectangle.add(plutocell21); plutoRectangle.add(plutocell22); plutoRectangle.add(plutocell23); plutoRectangle.add(plutocell24);

        // Initializing the PLUTO block numbers
        plutoBlockNum = new ArrayList<>();
        plutoBlockNum.add(plutoblocknum0); plutoBlockNum.add(plutoblocknum1); plutoBlockNum.add(plutoblocknum2); plutoBlockNum.add(plutoblocknum3); plutoBlockNum.add(plutoblocknum4);
        plutoBlockNum.add(plutoblocknum5); plutoBlockNum.add(plutoblocknum6); plutoBlockNum.add(plutoblocknum7); plutoBlockNum.add(plutoblocknum8); plutoBlockNum.add(plutoblocknum9);
        plutoBlockNum.add(plutoblocknum10); plutoBlockNum.add(plutoblocknum11); plutoBlockNum.add(plutoblocknum12); plutoBlockNum.add(plutoblocknum13); plutoBlockNum.add(plutoblocknum14);
        plutoBlockNum.add(plutoblocknum15); plutoBlockNum.add(plutoblocknum16); plutoBlockNum.add(plutoblocknum17); plutoBlockNum.add(plutoblocknum18); plutoBlockNum.add(plutoblocknum19);
        plutoBlockNum.add(plutoblocknum20); plutoBlockNum.add(plutoblocknum21); plutoBlockNum.add(plutoblocknum22); plutoBlockNum.add(plutoblocknum23); plutoBlockNum.add(plutoblocknum24);

        // Initializing the NYT Solution Labels
        nytSollCellList = new ArrayList<>();
        nytSollCellList.add(nytsolucell0); nytSollCellList.add(nytsolucell1); nytSollCellList.add(nytsolucell2); nytSollCellList.add(nytsolucell3); nytSollCellList.add(nytsolucell4);
        nytSollCellList.add(nytsolucell5); nytSollCellList.add(nytsolucell6); nytSollCellList.add(nytsolucell7); nytSollCellList.add(nytsolucell8); nytSollCellList.add(nytsolucell9);
        nytSollCellList.add(nytsolucell10); nytSollCellList.add(nytsolucell11); nytSollCellList.add(nytsolucell12); nytSollCellList.add(nytsolucell13); nytSollCellList.add(nytsolucell14);
        nytSollCellList.add(nytsolucell15); nytSollCellList.add(nytsolucell16); nytSollCellList.add(nytsolucell17); nytSollCellList.add(nytsolucell18); nytSollCellList.add(nytsolucell19);
        nytSollCellList.add(nytsolucell20); nytSollCellList.add(nytsolucell21); nytSollCellList.add(nytsolucell22); nytSollCellList.add(nytsolucell23); nytSollCellList.add(nytsolucell24);

        // Initializing the PLUTO Solution Labels
        plutoSollCellList = new ArrayList<>();
        plutoSollCellList.add(plutocellsol0); plutoSollCellList.add(plutocellsol1); plutoSollCellList.add(plutocellsol2); plutoSollCellList.add(plutocellsol3); plutoSollCellList.add(plutocellsol4);
        plutoSollCellList.add(plutocellsol5); plutoSollCellList.add(plutocellsol6); plutoSollCellList.add(plutocellsol7); plutoSollCellList.add(plutocellsol8); plutoSollCellList.add(plutocellsol9);
        plutoSollCellList.add(plutocellsol10); plutoSollCellList.add(plutocellsol11); plutoSollCellList.add(plutocellsol12); plutoSollCellList.add(plutocellsol13); plutoSollCellList.add(plutocellsol14);
        plutoSollCellList.add(plutocellsol15); plutoSollCellList.add(plutocellsol16); plutoSollCellList.add(plutocellsol17); plutoSollCellList.add(plutocellsol18); plutoSollCellList.add(plutocellsol19);
        plutoSollCellList.add(plutocellsol20); plutoSollCellList.add(plutocellsol21); plutoSollCellList.add(plutocellsol22); plutoSollCellList.add(plutocellsol23); plutoSollCellList.add(plutocellsol24);

        // Filling the Rectangles with the today's puzzle information
        for(int i = 0; i < 25; i++){
            if(blockDesign[i] == 0){
                solutionRectangle.get(i).setFill(Color.BLACK);
                plutoRectangle.get(i).setFill(Color.BLACK);
            }
            if(blockNumbers[i] != 0){
                nytimesBlockNum.get(i).setText(String.valueOf(blockNumbers[i]));
                plutoBlockNum.get(i).setText(String.valueOf(blockNumbers[i]));
            }
        }

        // Filling the Clues Label
        clueLabels = new ArrayList<>();
        Label acrossLabel = new Label("ACROSS");
        acrossLabel.setFont(new Font("Cambria", 32));
        acrossLabel.setStyle("-fx-font-weight: bold;");
        Label downLabel = new Label("DOWN");
        downLabel.setFont(new Font("Cambria", 32));
        downLabel.setStyle("-fx-font-weight: bold;");
        clueLabels.add(acrossLabel);
        for(int i = 0; i < 5; i++){
            Label l = new Label(todaysClues.get(i).printPuzzleClue());
            l.setFont(new Font("Cambria", 24));
            l.setWrapText(true);
            clueLabels.add(l);
        }
        clueLabels.add(downLabel);
        for(int i = 5; i < 10; i++){
            Label l2 = new Label(todaysClues.get(i).printPuzzleClue());
            l2.setFont(new Font("Cambria", 24));
            l2.setWrapText(true);
            clueLabels.add(l2);
        }
        clueLabels.forEach(e -> consoleVbox.getChildren().add(e));

        /**
         * Adding Solutions to the Panel
         */
        for(int i = 0; i < 25; i++){
            if(blockDesign[i] == 1){
                nytSollCellList.get(i).setText(solutionPositions.get(i));
            }
        }
        changeLog("Official solution obtained. It can be seen at the left side of the frame.\n");
        System.out.println("Official solution obtained. It can be seen at the left side of the frame.\n");
    }

    /**
     * showSolutions Function
     * This function uses both necessary information scraped and final candidates to show our solution
     * to the right side of the Frame.
     */
    public void showSolutions(){
        plutoSolutions = new ArrayList<>();
        // Adding necessary information without solution to the plutoSolutions first
        for(int i = 0; i < todaysClues.size(); i++){
            String clueValue = todaysClues.get(i).getClueValue();
            int clueNumber = todaysClues.get(i).getClueNumber();
            String clueText = todaysClues.get(i).getClueText();
            int solnLength = todaysClues.get(i).getSolutionLength();
            ArrayList<String> solution = todaysClues.get(i).getSolutionCandidates();
            int x = todaysClues.get(i).position_x;
            int y = todaysClues.get(i).position_y;
            String soln = "";
            solution.removeAll(Collections.singletonList(null));
            solution.add("X");
            soln = solution.get(0);
            PuzzleClue pluto = new PuzzleClue(clueValue, clueNumber, clueText, soln);
            pluto.setLetterCoordinates(x,y);
            plutoSolutions.add(pluto);
        }
        // Everything is set, we ready to fill the right side of the frame with PLUTO solutions
        for(int i = 0; i < plutoSolutions.size(); i++){
            if(plutoSolutions.get(i).getClueValue().equals("Across")){
                ArrayList<String> parsedPLUTOsolutions;
                parsedPLUTOsolutions = parseSolutions(plutoSolutions.get(i).getPLUTOSolution());
                int cluePosX = plutoSolutions.get(i).getPosition_x();
                int cluePosY = plutoSolutions.get(i).getPosition_y();
                for (int j = 0; j < parsedPLUTOsolutions.size(); j++){
                    // Since we are dealing with 5x5 grid, We are moving the positions by multiplying it with 5.
                    // Since this Across clue, we only moving the position x. Position indicates the starting point of the solution.
                    //if(plutoSollCellList.get(5*cluePosY + cluePosX).getText() == null)
                        plutoSollCellList.get(5*cluePosY + cluePosX).setText(parsedPLUTOsolutions.get(j));
                    cluePosX++;
                }
            }else{
                ArrayList<String> parsedPLUTOsolutions;
                parsedPLUTOsolutions = parseSolutions(plutoSolutions.get(i).getPLUTOSolution());
                int cluePosX = plutoSolutions.get(i).getPosition_x();
                int cluePosY = plutoSolutions.get(i).getPosition_y();
                for (int j = 0; j < parsedPLUTOsolutions.size(); j++){
                    // Only difference with the across is now we are moving the position y.
                    //if(plutoSollCellList.get(5*cluePosY + cluePosX).getText() == "")
                        plutoSollCellList.get(5*cluePosY + cluePosX).setText(parsedPLUTOsolutions.get(j));
                    cluePosY++;
                }
            }
        }
    }

    /**
     * This function prepares solution candidates for all the
     * New York Times Mini Crossword Puzzle clues.
     * Datamuse API is used for generating word according to the clues.
     * Merriam Webster Dictionary API is used for finding synonyms of words which are generated from Datamuse API.
     *
     */
    public void prepareCandidates() throws IOException, InterruptedException {
        System.out.println("PLUTO will search the clues on the Datamuse API.");
        Thread.sleep(3000);
        for(int i = 0; i < todaysClues.size(); i++) {
            ArrayList<String> rawDataCandidates;
            dataScraper= new DataScraper();
            try{
                dataScraper.checkDatamuse(todaysClues.get(i).getClueText(),todaysClues.get(i).getSolutionLength(),todaysClues.get(i).getClueNumber());
                //dataScraper.checkDatamuse(todaysClues.get(i).getClueText(),todaysClues.get(i).getSolutionLength(),todaysClues.get(i).getClueNumber());

            }catch (InterruptedException e){
                e.printStackTrace();
            }
            todaysClues.get(i).setSolutionCandidates(dataScraper.getResults());
            //todaysClues.get(i).getSolutionCandidates().add(todaysClues.get(i).getItsSolution().toLowerCase());
            //Use of Merriam Webster Dictionary API
            Thread.sleep(2000);
            System.out.println("Datamuse API sequence is completed.");
            System.out.println("Connecting to Merriam Webster Dictionary API to get the synonyms of the solution candidates.");
            Thread.sleep(2000);
            System.out.println("Now");
            Merriam mer = new Merriam(todaysClues);
            mer.findSynonym(i,todaysClues.get(i).getSolutionLength());
            for(int k=0;k<mer.syn.size();k++){
                if(mer.syn.size()!=0){
                    todaysClues.get(i).getSolutionCandidates().add(mer.syn.get(k).toString());
                }
            }
            rawDataCandidates=todaysClues.get(i).getSolutionCandidates();
            // After, we getting result from the Data Muse
            // First, we will make all the candidates uppercase in ENGLISH
            ArrayList<String> rawDataFinalized = new ArrayList<>();
            for (int j = 0; j < rawDataCandidates.size(); j++){
                String upperCaseCandid = rawDataCandidates.get(j).toUpperCase(Locale.ENGLISH);
                rawDataFinalized.add(upperCaseCandid);
            }
            // Setting candidates to the each clue
            todaysClues.get(i).setSolutionCandidates(rawDataFinalized);

            //todaysClues.get(i).getSolutionCandidates().add(todaysClues.get(i).getItsSolution());

            System.out.println("SOLUTION CANDIDATES FOR the CLUE -- " + todaysClues.get(i).getClueValue() + " " + todaysClues.get(i).getClueNumber() + ". " + todaysClues.get(i).getClueText() + " --");
            System.out.println(todaysClues.get(i).getSolutionCandidates());
        }//Solution candidates for each clue are finalized

    }

    /**
     * CONSTRAINT SATISFACTION ALGORITHM
     * This is the core function of the program.
     * This is used for finding the correct words. Idea is referred from the pdf file that instructor sent
     * us early in the december.
     * Logic: For the crossword, there should be always at least one matched letter in the solution clues.
     * We call it matched letter as constraint and we start to check the solution candidates. And after searching,
     * if there are two words that have common letter, it is said that constraint satisfaction.
     * The idea is borrowed from following websites:
     * https://github.com/mayankk4/ConstraintSatisfaction
     * https://en.wikipedia.org/wiki/Constraint_satisfaction_problem
     * https://web.stanford.edu/~jduchi/projects/crossword_writeup.pdf
     */
    public void satisfyConstraints() throws InterruptedException {
        for(int c = 0; c < constraints.size(); c++){
            Thread.sleep(1000);
            System.out.println("Trial No:" + c);
            PuzzleClue clue1 = constraints.get(c).constraintClue;
            System.out.println("Desired word for constraint satisfaction: " + clue1.getItsSolution());
            for(int n = 0; n < constraints.get(c).nodes.size(); n++){
                Node node = constraints.get(c).nodes.get(n);
                PuzzleClue clue2 = node.clue;
                System.out.println("Desired word for constraint satisfaction: " + clue2.getItsSolution());
                int[] intersect = node.intersection;
                if(clue1.getClueValue().equals("Across")){
                    Thread.sleep(100);
                    //clue1 is across
                    System.out.println("Intersection points with -Candidate 1- and -Candidate 2-: -" + intersect[0] + "- -" + intersect[1] + "-");
                    int index1 = intersect[0] - clue1.position_x; // Char position of clue candidate 1
                    int index2 = intersect[1] - clue2.position_y; // Char position of clue candidate 2
                    for(int cand1 = 0; cand1 < clue1.getSolutionCandidates().size(); cand1++){
                        boolean eliminate = true;
                        Thread.sleep(100);
                        String candidate1 = clue1.getSolutionCandidates().get(cand1);
                        for(int cand2 = 0; cand2 < clue2.getSolutionCandidates().size(); cand2++){
                            String candidate2 = clue2.getSolutionCandidates().get(cand2);
                            System.out.println("Index of constraint word for candidate 1: " + index1 + " Index of constraint word for candidate 2: " + index2 + " Candidate 1: " + candidate1 + " Candidate 2: " + candidate2);
                            if((candidate1 != null) && (candidate2 != null)) {
                                if (candidate1.charAt(index1) == candidate2.charAt(index2)) {
                                    eliminate = false;
                                }
                            }
                        }
                        if(eliminate && clue1.getSolutionCandidates().size() > 1){
                            System.out.println(clue1.getSolutionCandidates().get(cand1) + " discarded!");
                            clue1.getSolutionCandidates().set(cand1, null);
                        }
                    }
                }
                else{
                    // If the Clue is down section
                    int index1 = intersect[1] - clue1.position_y;
                    int index2 = intersect[0] - clue2.position_x;
                    for(int cand1 = 0; cand1 < clue1.getSolutionCandidates().size(); cand1++){
                        boolean eliminate = true;
                        String candidate1 = clue1.getSolutionCandidates().get(cand1);
                        for(int cand2 = 0; cand2 < clue2.getSolutionCandidates().size(); cand2++){
                            String candidate2 = clue2.getSolutionCandidates().get(cand2);
                            System.out.println("Index of constraint word for candidate 1: " + index1 + " Index of constraint word for candidate 2: " + index2 + " Candidate 1: " + candidate1 + " Candidate 2: " + candidate2);
                            if((candidate1 != null) && (candidate2 != null)) {
                                if (candidate1.charAt(index1) == candidate2.charAt(index2)) {
                                    eliminate = false;
                                }
                            }
                        }
                        if(eliminate && clue1.getSolutionCandidates().size() > 1){
                            System.out.println(clue1.getSolutionCandidates().get(cand1) + " discarded!");
                            clue1.getSolutionCandidates().set(cand1, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * This function checks the solution candidates for certain clues.
     * We should have only one candidate for each clue.
     * So if it is bigger than 1, we marked as false which means is not solved.
     * @return solved
     */
    public boolean isSolved(){
        boolean isClueSolved = true;
        for(PuzzleClue currentClue : todaysClues){
            if(currentClue.getSolutionCandidates().size() > 1){
                isClueSolved = false;
            }
        }
        return isClueSolved;
    }

    /**
     * This function parses the solutions for the output
     * @param sln
     */
    public ArrayList<String> parseSolutions(String sln){
        ArrayList<String> parsed = new ArrayList<>();
        for(int i = 0; i < sln.length(); i++){
            parsed.add(String.valueOf(sln.charAt(i)));
        }
        return parsed;
    }

    /*
    public static String randomWord(int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            word.append((char)('a' + random.nextInt(26)));
        }

        return word.toString();
    }*/

    /**
     * This function calls the CSV algorithm and prints the final results.
     */
    public void provideSolution() throws InterruptedException {
        // It continues down to one final decision for all the clues.
        //while(!isSolved()){
            satisfyConstraints();
            for (PuzzleClue finalRes : todaysClues) {
                // If final result contains a null, we are releasing them before showing the PLUTO solution
                finalRes.releaseCandidates();
                String str = "Solution candidate of " + finalRes.getClueValue() + " Clue number " + finalRes.getClueNumber() + ". is: " + finalRes.getSolutionCandidates() + "!\n";
                System.out.println(str);
                changeLog(str);
            }
        //}
    }

    /**
     * This function removes duplicates in possible solution candidates for each clue
     * @param list
     */
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    /**
     * Node object for the Constraint Class
     */
    private class Node{
        PuzzleClue clue;
        int[] intersection;
    }

    /**
     * Private Constraint Class for using the Constraint Satisfaction Algorithm
     */
    private class Constraint{
        PuzzleClue constraintClue;
        ArrayList<Node> nodes;

        /**
         * Necessary class for the satisfyContstraints() Function
         * Constructor for the Constraint object.
         * We store the
         * @param clue
         */
        public Constraint(PuzzleClue clue){
            this.constraintClue = clue;
            nodes = new ArrayList<>();
            for(int i = 0; i < todaysClues.size(); i++){
                PuzzleClue temp = todaysClues.get(i);
                // We find all the intersections first for the
                int[] intersection = clue.checkCommonLetters(temp);
                if(intersection != null){
                    Node node = new Node();
                    node.clue = temp;
                    node.intersection = intersection;
                    nodes.add(node);
                }
            }
        }
    }
}
