package sample;
/**
 * PLUTO
 * Description: This class object holds all the information about the puzzle for showing and
 * solving the puzzle. All extracted data is hold in this class too.
 * PuzzleClue Class
 */
import java.util.*;
public class PuzzleClue {

    // Declaring properties
    int position_x;
    int position_y;
    private int clueNumber;
    private String clueValue;
    private String clueText;
    private boolean isSolved;
    private int solutionLength;
    private String itsSolution;
    private ArrayList<String> solutionCandidates;
    private String PLUTOSolution;

    // Constructor of the Clue
    PuzzleClue(String clueValue, int clueNumber, String clueText){
        this.clueValue = clueValue;
        this.clueText = clueText;
        isSolved = false;
        solutionLength = 0;
        this.clueNumber = -1;
        position_x = -1;
        position_y = -1;
    }

    // Default Constructor
    PuzzleClue(){
        position_y = -1;
        position_x = -1;
        clueNumber = -1;
        solutionLength = 0;
        isSolved = false;
        clueValue = "";
        clueText = "";
    }

    // Constructor for the PLUTO Solution
    PuzzleClue(String clueValue, int clueNumber, String clueText, String PLUTOSolution){
        position_x = -1;
        position_y = -1;
        this.clueValue = clueValue;
        this.clueNumber = clueNumber;
        this.clueText = clueText;
        this.PLUTOSolution = PLUTOSolution;
    }

    public void setPLUTOSolution(String PLUTOSolution){
        this.PLUTOSolution = PLUTOSolution;
    }

    public String getPLUTOSolution(){
        return PLUTOSolution;
    }

    public void setClueNumber(int clueNumber){
        this.clueNumber = clueNumber;
    }

    public int getClueNumber(){
        return clueNumber;
    }

    public String printPuzzleClue(){
        return clueNumber + ". " + clueText;
    }
    public String getClueValue(){
        return clueValue;
    }

    public void setSolutionLength(int solutionLength){
        this.solutionLength = solutionLength;
    }

    public int getSolutionLength(){
        return solutionLength;
    }

    public void setLetterCoordinates(int x, int y){
        position_x = x;
        position_y = y;
    }

    public int getPosition_x(){
        return position_x;
    }

    public int getPosition_y(){
        return position_y;
    }

    public void setItsSolution(String itsSolution){
        this.itsSolution = itsSolution;
    }

    public String getItsSolution(){
        return itsSolution;
    }

    public ArrayList<String> getSolutionCandidates(){
        return solutionCandidates;
    }

    public void setSolutionCandidates(ArrayList<String> solutionCandidates){
        this.solutionCandidates = solutionCandidates;
    }

    public void setClueText(String clueText){
        this.clueText = clueText;
    }

    public String getClueText(){
        return clueText;
    }


    /**
     * getIntersection Function
     * Description: This function checks the common letters between two candidates.
     * We basically checks the letter positions of two clues.
     * @param clue
     * @return
     */
    public int[] checkCommonLetters(PuzzleClue clue){
        if(this.getClueValue().equals(clue.getClueValue())){
            return null;  // no intersection
        }
        // If the clue value is "Down".
        if(this.getClueValue() == "Down"){
            int i = this.position_y;
            if(i > clue.position_y)
                return null;
            if(this.position_x > clue.position_x + clue.solutionLength -1){
                return null;
            }
            while(i != clue.position_y){
                i++;
            }
            int[] result = {position_x, i};
            return result;
        }
        // If the clue value is "Across".
        else{
            int i = this.position_x;
            if(i > clue.position_x)
                return null;
            if(this.solutionLength-1 < clue.position_x){
                return null;
            }
            while(i != clue.position_x){
                i++;
            }
            int[] result = {i, position_y};
            return result;
        }
    }


    /**
     * This function returns the size of the solution candidates
     */
    public int sizeOfSolutionCandidates(){
        return solutionCandidates.size();
    }


    /**
     * This Function basically removes "null" pointers from the candidates list.
     * In the candidates list,
     */
    public void releaseCandidates(){
        for(int i = 0; i < solutionCandidates.size(); i++){
            if(solutionCandidates.get(i) == null){
                solutionCandidates.remove(i);
            }
        }
    }
}
