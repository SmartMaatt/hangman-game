package pl.polsl.lab.mateusz.plonka.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;
import pl.polsl.lab.mateusz.plonka.resources.CookieManager;
import pl.polsl.lab.mateusz.plonka.resources.DataBaseManager;
import pl.polsl.lab.mateusz.plonka.view.View;

/**
 * Displays and manages history content
 *
 * @author Mateusz Płonka
 */
public class ShowHistory extends HttpServlet {

    /*VARIABLES*/
    private HttpSession session = null;
    private PrintWriter out = null;
    private View myView = null;
    private CookieManager myCookie = null;
    private DataBaseManager myDataBase = null;

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

        //Get session, writer, view and cookie
        session = request.getSession(false);
        out = response.getWriter();
        myView = new View(out);
        myCookie = new CookieManager();

        if (session != null) {

            HangmanGame myGame = (HangmanGame) session.getAttribute("myGame");
            myDataBase = (DataBaseManager) session.getAttribute("myDataBase");

            myView.addHeader("Game history", true, myGame.getMyPlayer().getName(), myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), "styles/cssStyles.css", "https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css");
            out.println("<div id=\"content\" class=\"box\">\n");

            //If clicked "Drop table"
            if (request.getParameterMap().containsKey("dropTable")) {

                myDataBase.dropTable("BANK");
                myDataBase.dropTable("ROUNDS");
                out.println("<h1>Database cleared succesfuly!</h1>\n");
                myDataBase.createTables("ROUNDS");
                myDataBase.createTables("BANK");
                out.println("<a href=\"app\"><input type=\"button\" value=\"<= Back\"></input></a>\n");

                //View game histrory    
            } else {
                //Displays bank only if it contains sth
                if (myDataBase.getLocalDataBase().size() > 0) {
                    out.println("<h1>Game history</h1>\n");
                    myView.displayGameHistory(myDataBase.getLocalDataBase());
                } else {
                    out.println("<h1>No data in game history!</h1>\n");
                }
                out.println("<a href=\"app\"><input type=\"button\" value=\"<= Back\"></input></a>\n");
                out.println("<form method='POST' action='history'><input type='hidden' name='dropTable' value='1'><input type='submit' class='specialSubmit' value='Drop Database'></form>");
                out.println("<div id=\"ICC\">\n</div>");
            }

        } else {
            myView.addHeader("New player", false, "", null, "styles/cssStyles.css");
            request.getRequestDispatcher("newPlayer.html").include(request, response);
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
        return "Displays and manages history content";
    }

}
