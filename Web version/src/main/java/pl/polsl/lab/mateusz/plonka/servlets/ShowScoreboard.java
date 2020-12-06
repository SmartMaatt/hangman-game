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
import pl.polsl.lab.mateusz.plonka.view.View;

/**
 * Displays and manages scoreboard page
 *
 * @author Mateusz Płonka
 */
public class ShowScoreboard extends HttpServlet {

    /*VARIABLES*/
    private HttpSession session = null;
    private PrintWriter out = null;
    private View myView = null;
    private CookieManager myCookie = null;

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

            myView.addHeader("Scoreboard", true, myGame.getMyPlayer().getName(), myCookie.getCookieValue(request, response, myGame.getMyPlayer().getName()), "styles/cssStyles.css");
        } else {
            myView.addHeader("Scoreboard", false, "", null, "styles/cssStyles.css");
        }

        out.println("<div id=\"content\" class=\"box\">\n");
        //Main content
        if (!myCookie.cookiesToDisplay(request, response).isEmpty()) {
            out.println("<h1>Cookies scoreboard</h1>\n");
            myView.displayCookies(myCookie.cookiesToDisplay(request, response));
        } else {
            out.println("<h1>Somebody has eaten all the cookies ;( </h1>\n");
        }

        out.println("<a href=\"app\"><input type=\"button\" value=\"<= Back\"></input></a>\n");
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
        return "Displays and manages scoreboard page";
    }

}
