package pl.polsl.lab.mateusz.plonka.servlets;

import pl.polsl.lab.mateusz.plonka.resources.CookieManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;
import pl.polsl.lab.mateusz.plonka.resources.DataBaseManager;
import pl.polsl.lab.mateusz.plonka.view.View;

/**
 * Main servlet managing HangmanGame display and logic (controller)
 *
 * @author Mateusz Płonka
 */
public class HangmanGameServlet extends HttpServlet {

    /*VARIABLES*/
    private HttpSession session = null;
    private PrintWriter out = null;
    private View myView = null;
    private CookieManager myCookie = null;

    /**
     * Delete accident session and display newPlayer.html page
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void loginScene(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        session.invalidate();
        myView.addHeader("New player", false, "", null, "styles/cssStyles.css");
        request.getRequestDispatcher("newPlayer.html").include(request, response);
    }

    /**
     * Display addWordScene
     *
     * @param myGame game object
     * @param winAndFail current player cookie status
     * @param showPlay [true] show play button, [false] don't show play button
     */
    private void addWordScene(HangmanGame myGame, ArrayList<Integer> winAndFail, boolean showPlay) {
        myView.addHeader("Add words", true, myGame.getMyPlayer().getName(), winAndFail, "styles/cssStyles.css");
        myView.addWordScrean(myGame.getWordBank(), showPlay);

    }

    /**
     * Main game scene display
     *
     * @param myGame game object
     * @param winAndFail current player cookie status
     * @param msg message to display if needed
     */
    private void mainGameScene(HangmanGame myGame, ArrayList<Integer> winAndFail, String msg) {
        myView.addHeader("Let's play", true, myGame.getMyPlayer().getName(), winAndFail, "styles/cssStyles.css");
        myView.mainGameScrean(myGame.getMyPlayer(), myGame.getWordToFind(), myGame.getMaxErrors(), msg);
    }

    /**
     * Ending game scene display
     *
     * @param myGame game object
     * @param winAndFail current player cookie status
     * @param gameStatus [false] game is failed, [true] game is won
     */
    private void endGameScene(HangmanGame myGame, ArrayList<Integer> winAndFail, boolean gameStatus) {
        if (gameStatus) {
            myView.addHeader("You Won!", true, myGame.getMyPlayer().getName(), winAndFail, "styles/cssStyles.css");
        } else {
            myView.addHeader("You Failed!", true, myGame.getMyPlayer().getName(), winAndFail, "styles/cssStyles.css");
        }
        myView.endGameScrean(myGame.getMyPlayer(), myGame.getChosenWord(), myGame.getMaxErrors(), gameStatus);
    }

    /**
     * Checks if current game is won of failed
     *
     * @param letter letter to check
     * @param myGame current game object
     * @return Error message to display on game board
     */
    private String checkGameStatus(char letter, HangmanGame myGame) {

        String msg = "";
        if (letter != '#') {
            // update word found
            // we update only if typed has not already been entered or was a special character
            if (!myGame.getMyPlayer().getAlreadyTried().contains(letter) && (letter != '#')) {
                // we check if word to find contains typed
                if (myGame.getChosenWord().contains(String.valueOf(letter))) {
                    // if so, we replace _ by the character typed
                    int index = myGame.getChosenWord().indexOf(String.valueOf(letter));

                    while (index >= 0) {
                        //Change all same signs
                        myGame.getWordToFind().set(index, letter);
                        index = myGame.getChosenWord().indexOf(letter, index + 1);
                    }
                } else {
                    //Increse mistakes
                    myGame.getMyPlayer().incErrors();
                    msg = "No such letter in the word";
                }

                // typed is now a letter entered
                myGame.getMyPlayer().addToAlreadyTried(letter);
                myGame.getMyPlayer().incTries();

                //Checks if game is won
                if (myGame.compareWords()) {
                    /*Load ending window*/

                    //Return win msg
                    msg = "win";
                }

                //Checks if game is failed
                if (myGame.getMyPlayer().getFails() == myGame.getMaxErrors()) {

                    //return fail msg
                    msg = "fail";
                }
            } else {
                msg = "Already used character!";
            }
        } else {
            msg = "No value!";
        }
        return msg;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //Get session, writer, view and cookies
        session = request.getSession();
        out = response.getWriter();
        myView = new View(out);
        myCookie = new CookieManager();

        if (!session.isNew()) {
            int screanMode = (int) session.getAttribute("screanMode");

            switch (screanMode) {
                //Screan mode == 1, display scene "addWord"
                case 1: {
                    //Get game from session
                    HangmanGame myGame = (HangmanGame) session.getAttribute("myGame");
                    //Exit adding new words and pressed "Play"
                    if (request.getParameterMap().containsKey("gameKey")) {
                        if (request.getParameter("gameKey").equals("true")) {
                            //Change screanMode to "2"
                            session.setAttribute("screanMode", 2);
                            myGame.randNewWord();
                            //Display scene "mainGame" [2]
                            this.mainGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), "");
                        } else {
                            //Display scene "addWord" [1]
                            this.addWordScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), !myGame.getWordBank().isEmpty());
                        }

                    } else {
                        //Added new word
                        if (request.getParameterMap().containsKey("addWord")) {
                            //Add new word to bank
                            myGame.addWordToBank(request.getParameter("addWord"));
                            //Display scene "addWord" [1]
                            this.addWordScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), true);
                        } else {
                            //Display scene "addWord" [1]
                            this.addWordScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), !myGame.getWordBank().isEmpty());
                        }
                    }

                    break;
                }
                //Screan mode == 2, display scene "mainGameScene"      
                case 2: {
                    HangmanGame myGame = (HangmanGame) session.getAttribute("myGame");
                    //Input occured
                    if (request.getParameterMap().containsKey("letter")) {

                        //Convert input string to char
                        char l = '#';
                        if (request.getParameter("letter").length() == 1) {
                            l = request.getParameter("letter").charAt(0);
                        }

                        //Display game board or game ending screan
                        String msg = this.checkGameStatus(l, myGame);
                        switch (msg) {
                            case "win": {
                                //Inc cookie win value
                                myCookie.incValue(request, response, myGame.getMyPlayer().getName(), true);
                                //Change session params
                                session.setAttribute("screanMode", 3);
                                session.setAttribute("gameStatus", true);
                                //Update data base
                                DataBaseManager myDataBase = (DataBaseManager) session.getAttribute("myDataBase");
                                myDataBase.insertData("ROUNDS", myDataBase.getMainTableIndex(), myGame.getMyPlayer().getName(), true, myGame.getChosenWord(), myGame.getMaxErrors(), myGame.getMyPlayer().getTries());
                                //Insert word bank to data base
                                myGame.getWordBank().forEach(tmp -> {
                                    myDataBase.insertData("BANK", myDataBase.getBankTableIndex(), tmp.getValue(), myDataBase.getMainTableIndex() - 1);
                                });
                                //Load endGame scene
                                this.endGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), true);
                                break;
                            }

                            case "fail": {
                                //Inc cookie fail value
                                myCookie.incValue(request, response, myGame.getMyPlayer().getName(), false);
                                //Change session params
                                session.setAttribute("screanMode", 3);
                                session.setAttribute("gameStatus", false);
                                //Update data base
                                DataBaseManager myDataBase = (DataBaseManager) session.getAttribute("myDataBase");
                                myDataBase.insertData("ROUNDS", myDataBase.getLocalDataBase().size(), myGame.getMyPlayer().getName(), false, myGame.getChosenWord(), myGame.getMaxErrors(), myGame.getMyPlayer().getTries());
                                //Insert word bank to data base
                                myGame.getWordBank().forEach(tmp -> {
                                    myDataBase.insertData("BANK", myDataBase.getBankTableIndex(), tmp.getValue(), myDataBase.getMainTableIndex() - 1);
                                });
                                //Load endGame scene
                                this.endGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), false);
                                break;
                            }

                            default:
                                this.mainGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), msg);
                                break;
                        }
                    } else {
                        this.mainGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), "");
                    }

                    break;
                }
                //Screan mode == 3, display scene "endGameScene"        
                case 3: {
                    HangmanGame myGame = (HangmanGame) session.getAttribute("myGame");
                    //Button was pressed
                    if (request.getParameterMap().containsKey("update")) {

                        switch (request.getParameter("update")) {
                            //Chosen "update"
                            case "true": {
                                session.setAttribute("screanMode", 1);
                                session.removeAttribute("gameStatus");
                                String pName = myGame.getMyPlayer().getName();
                                myGame.newPlayer(pName);
                                this.addWordScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), true);

                                break;
                            }
                            //Chosen "Don't update"
                            case "false": {
                                session.setAttribute("screanMode", 2);
                                session.removeAttribute("gameStatus");
                                myGame.randNewWord();
                                String pName = myGame.getMyPlayer().getName();
                                myGame.newPlayer(pName);
                                this.mainGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), "New word has been generated!");

                                break;
                            }
                            //Send request but not chosen (somehow ;p )
                            default:
                                boolean gameStatus = (boolean) session.getAttribute("gameStatus");
                                this.endGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), gameStatus);
                                break;
                        }
                    } else {
                        boolean gameStatus = (boolean) session.getAttribute("gameStatus");
                        this.endGameScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), gameStatus);
                    }
                    break;
                }
                default:
                    break;
            }

        } //If not
        else {
            // Check if creating player parameters exists
            if ((request.getParameterMap().containsKey("playerName")) && request.getParameterMap().containsKey("maxErrors")) {

                //Save parameters
                String playerName = request.getParameter("playerName");
                int maxErrors = Integer.parseInt(request.getParameter("maxErrors"));

                //Adding cookie if new player
                if (!myCookie.findCookie(request, playerName)) {
                    myCookie.createCookie(response, playerName);
                }

                //Reading or creating data base
                DataBaseManager myDataBase = new DataBaseManager();

                if (!myDataBase.tableExists("ROUNDS")) {
                    if (!myDataBase.tableExists("BANK")) {

                        //Both tables doesn't exist [X][X]
                        myDataBase.createTables("ROUNDS");
                        myDataBase.createTables("BANK");
                    } else {

                        //Only bank table exists [X][Y]
                        myDataBase.dropTable("BANK");
                        myDataBase.createTables("ROUNDS");
                        myDataBase.createTables("BANK");
                    }
                } else {
                    if (!myDataBase.tableExists("BANK")) {

                        //Only rounds table exists [Y][X]
                        myDataBase.createTables("BANK");
                        myDataBase.selectData("ROUNDS");
                    } else {

                        //Both exists [Y][Y]
                        myDataBase.selectData("ROUNDS");
                        myDataBase.selectData("BANK");
                    }
                }

                //Create new game object
                HangmanGame myGame = new HangmanGame(maxErrors, playerName);

                //Setting up session atributes
                session.setAttribute("myGame", myGame);
                session.setAttribute("myDataBase", myDataBase);
                session.setAttribute("screanMode", 1);

                //Display scene "addWord" [1]
                this.addWordScene(myGame, myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), false);

            } else {
                //Delete accident session and display newPlayer.html page [0]
                this.loginScene(request, response);
            }
        }
        myView.addFooter("scripts/jsScripts.js");
        out.close();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Main servlet managing HangmanGame display and logic (controller)";
    }

}
