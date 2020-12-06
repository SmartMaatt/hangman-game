package pl.polsl.lab.mateusz.plonka.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.polsl.lab.mateusz.plonka.model.MyString;
import pl.polsl.lab.mateusz.plonka.model.Player;

/**
 * Class manages displaying content of webpage
 *
 * @author Mateusz Płonka
 */
public class View {

    PrintWriter out;

    //Constructor of view, defines an PrintWriter of webpage
    public View(PrintWriter out) {
        this.out = out;
    }

    /**
     * Displays single line form argumnet
     *
     * @param text Message to display
     */
    public void displayLine(String text) {
        out.println("<h4>" + text + "</h4>");
    }

    /**
     * Displays single line form argumnet and style it as error
     *
     * @param text Error information to display
     */
    public void displayError(String text) {
        out.println("<h4 style=\"color:red;\">" + text + "</h4>");
    }

    /**
     * Generates header of website
     *
     * @param header Header of website to insert
     * @param style List of styles to add
     */
    public void addHeader(String header, boolean looged, String playerName, ArrayList<Integer> winsAndFails, String... style) {
        out.print("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<title>" + header + "</title>\n"
                + "<link rel=\"icon\" href=\"footage/icon.png\">"
                + "<link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap&subset=latin-ext\" rel=\"stylesheet\">");

        for (Object c : style) {
            out.println("<link rel=\"stylesheet\" href=\"" + c + "\">");
        }

        out.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "</head>\n"
                + "<body>\n"
                + "<div id=\"container\">\n"
                + "<div id=\"header\" class=\"box\">"
                + "<h1>Hangman<span style=\"color:#E18A07;\">Game</span></h1>\n");

        if (looged) {

            out.println("<div id=\"statsWraper\"><p>Player: " + playerName + "</p><p>Wins: " + winsAndFails.get(0) + "</p><p>Fails: " + winsAndFails.get(1) + "</p></div>\n"
                    + "<ul><li><a href=\"show-word-bank\">WordBank</a></li><li><a href=\"show-scoreboard\">Scoreboard</a></li><li><a href=\"logout\">Logout</a></li></ul>");
        }

        out.print("</div>\n");
    }

    /**
     * Generates bottom part of website code
     *
     * @param scripts List of scripts files to add
     */
    public void addFooter(String... scripts) {
        out.println("</div>\n</div>\n</body>");
        for (Object c : scripts) {
            out.println("<script src=\"" + c + "\"></script>");
        }
        out.print("</html>");
    }

    /**
     * Displays all elements of ArrayList with strings
     *
     * @param wordBank ArrayList of myString to display
     */
    public void displayArrayList(ArrayList<MyString> wordBank) {

        //Converting ArrayList into List to use Lambda and streams in one shot
        List<String> data = new ArrayList<>();
        for (MyString var : wordBank) {
            data.add(var.getValue());
        }

        if (!wordBank.isEmpty()) {
            out.print("<table>\n"
                    + "  <tr>\n"
                    + "    <th>Index</th>\n"
                    + "    <th>Word</th>\n"
                    + "  </tr>\n");

            data.stream() // Turning list into stream
                    .collect(HashMap::new, (h, o) -> {
                        h.put(h.size() + 1, o);
                    }, (h, o) -> {
                    }) // Creating a map of the index to the object
                    .forEach((i, o) -> { //Usage of a BiConsumer forEach!
                        out.print(String.format("<tr>\n<td>#%d</td>\n<td>%s</td>\n</tr>\n", i, o));
                    });

            out.print("</table>");
        }
    }

    /**
     * Displays cookie contant in form of html table
     *
     * @param ckData cookie content packed to arrayLists
     */
    public void displayCookies(ArrayList<ArrayList<String>> ckData) {

        if (!ckData.isEmpty()) {
            out.print("<table>\n"
                    + "  <tr>\n"
                    + "    <th>PlayerName</th>\n"
                    + "    <th>Wins</th>\n"
                    + "    <th>Fails</th>\n"
                    + "  </tr>\n");

            //Iterate by all cookies
            for (ArrayList<String> s : ckData) {
                out.print("<tr>\n<td>" + s.get(0) + "</td>\n<td>" + s.get(1) + "</td>\n<td>" + s.get(2) + "</td>\n</tr>\n");
            }

            out.print("</table>");
        }
    }

    /**
     * Displays unfound word
     *
     * @param wToFind Word that is to find in this round
     */
    public void displayUnfoundWord(ArrayList<Character> wToFind) {

        //No need to use index as above
        //Simple usage of stream
        out.print("<h1 id=\"wToFind\">");
        wToFind.stream().forEach((ch) -> out.print(ch + " "));
        out.println("</h1>");
    }

    /**
     * Adds new words to bank and displays array on website
     *
     * @param wordBank bank of words to refill and display
     */
    public void addWordScrean(ArrayList<MyString> wordBank, boolean showPlay) {
        out.println("<div id=\"content\" class=\"box\">\n"
                + "<h1>Add word to bank</h1>\n"
                + "<form method=\"GET\" action=\"app\">\n"
                + "<input type=\"text\" name=\"addWord\" maxlength=\"20\" oninput=\"addWordResult(this)\" onchange=\"addWordResult(this)\" required autofocus/>\n"
                + "<input type=\"submit\" value=\"Add\" />\n"
                + "</form></br>\n"
                + "<p id=\"addWordMsg\" class=\"message\" ></p>");

        if (showPlay) {
            out.println("<form method=\"POST\" action=\"app\" >\n"
                    + "<input type=\"hidden\" value=\"true\" name=\"gameKey\" />\n"
                    + "<input type=\"submit\" value=\"Play\" />\n"
                    + "</form></br>\n");

            this.displayArrayList(wordBank);
        } else {
            out.println("<h4>Create word bank and play with random!</h4>");
        }

    }

    /**
     * Displays console game gui
     *
     * @param myPlayer Object of player to get information like PlayerName an
     * Stats
     * @param wToFind Word that is to find in this round
     * @param maxErrors Maximum number of errors that could be done in this
     * round
     */
    public void mainGameScrean(Player myPlayer, ArrayList<Character> wToFind, int maxErrors, String msg) {

        out.println("<div id=\"content\" class=\"box\">\n");
        displayUnfoundWord(wToFind);
        out.println("<p><b>Stats:</b></p>\n");
        out.println("<p>Player Name: " + myPlayer.getName() + "</p>\n");
        out.println("<p>Tries: " + myPlayer.getTries() + " | Errors: " + myPlayer.getFails() + "/" + maxErrors + "</p>\n");
        out.print("<p>Used signs: ");

        ArrayList<Character> signsArray = myPlayer.getAlreadyTried();
        for (int i = 0; i < signsArray.size(); i++) {
            out.print(signsArray.get(i) + ",");
        }

        out.println("</p>\n"
                + "<form method=\"POST\" action=\"app\" >\n"
                + "<input type=\"text\" name=\"letter\" id=\"letter\" maxlength=\"1\" oninput=\"tryLetter(this)\" onchange=\"tryLetter(this)\" autofocus/>\n"
                + "<input type=\"submit\" value=\"Try!\" />\n"
                + "</form>"
                + "<h4 id=\"msg\"></h4>");

        if (msg.length() > 0) {
            this.displayError(msg);
        }
    }

    /**
     * Displays fail/win screan
     *
     * @param myPlayer Object of player to get information like PlayerName and
     * Stats
     * @param wToFind Word that was to find in this round
     * @param maxErrors Maximum number of errors that could be done in this
     * round
     * @param gameStatus Info whenever game was won(1) or failed(0)
     */
    public void endGameScrean(Player myPlayer, String wToFind, int maxErrors, boolean gameStatus) {

        out.println("<div id=\"content\" class=\"box\">\n");
        if (!gameStatus) {
            out.println("<h1 style=\"color:red;\">FAILITURE ;(</h1>");
            out.println("<h3>Even great minds sometimes lose, don't worry " + myPlayer.getName()
                    + "</br>Word to find: " + wToFind + "</h3>");
        } else {
            out.println("<h1 style=\"color:green;\">VICTORY ;D</h1>");
            out.println("<h3>Great minds, like you " + myPlayer.getName() + ", always wins!</br>"
                    + "Word to find: " + wToFind + "</h3>");
        }

        out.println("<p><b>STATS:</b></p>\n");
        out.println("<p>Player Name: " + myPlayer.getName() + "</p>\n");
        out.println("<p>Tries:" + myPlayer.getTries() + " | Errors: " + myPlayer.getFails() + "/" + maxErrors + "</p>\n");
        out.print("<p>Used signs: ");

        ArrayList<Character> signsArray = myPlayer.getAlreadyTried();
        for (int i = 0; i < signsArray.size(); i++) {
            out.print(signsArray.get(i) + ",");
        }

        out.println("</p></br><h3>Do you want to update word bank?</h3>\n"
                + "<form method=\"POST\" action=\"app\" >\n"
                + "<input type=\"hidden\" name=\"update\" value=\"true\" />\n"
                + "<input type=\"submit\" value=\"Yes\" />\n"
                + "</form>"
                + "<form method=\"POST\" action=\"app\" >\n"
                + "<input type=\"hidden\" name=\"update\" value=\"false\" />\n"
                + "<input type=\"submit\" value=\"No\" />\n"
                + "</form>");
    }
}
