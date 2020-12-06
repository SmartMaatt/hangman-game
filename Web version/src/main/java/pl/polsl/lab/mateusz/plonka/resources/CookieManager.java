package pl.polsl.lab.mateusz.plonka.resources;

import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages cookies on HangmanGame
 *
 * @author Mateusz Płonka
 */
public class CookieManager {

    /**
     * Checks if page get cookie with given name
     *
     * @param request servlet request
     * @param cookieName name of cookie to find
     * @return [true] if found, [false] if not
     */
    public boolean findCookie(HttpServletRequest request, String cookieName) {
        boolean status = false;
        Cookie cookies[] = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    status = true;
                    break;
                }
            }
        }
        return status;
    }

    /**
     * Creates a new cookie with given name
     *
     * @param response servlet response
     * @param cookieName given cookie name
     */
    public void createCookie(HttpServletResponse response, String cookieName) {
        Cookie ck = new Cookie(cookieName, "0 0");//creating cookie object  
        response.addCookie(ck);//adding cookie in the response
    }

    /**
     * Returns cookie values converted to int
     *
     * @param request servlet request
     * @param response servlet response
     * @param cookieName given name of cookie
     * @return ArrayList of converted to int cookie values
     */
    public ArrayList<Integer> getCookieValue(HttpServletRequest request, HttpServletResponse response, String cookieName) {

        Cookie cookies[] = request.getCookies();
        ArrayList<Integer> myArr = new ArrayList<Integer>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {

                    //Cookie format- "<number of wins><space><number of fails>"
                    String[] splitedValues = cookie.getValue().split(" ");

                    myArr.add(Integer.parseInt(splitedValues[0]));
                    myArr.add(Integer.parseInt(splitedValues[1]));
                    return myArr;
                }
            }
        }

        //If user deleted cookie during game
        this.createCookie(response, cookieName);
        myArr.add(0);
        myArr.add(0);
        return myArr;
    }

    /**
     * Increments value of given player
     *
     * @param request servlet request
     * @param response servlet response
     * @param cookieName given cookie name
     * @param win [true] inc win, [false] inc fail
     */
    public void incValue(HttpServletRequest request, HttpServletResponse response, String cookieName, boolean win) {
        Cookie cookies[] = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {

                    //Cookie format- "<number of wins><space><number of fails>"
                    int integerValue = 0;
                    String value = cookie.getValue();
                    String[] splitedValues = value.split(" ");

                    //Convert to int and inc chosen value
                    if (win) {
                        integerValue = Integer.parseInt(splitedValues[0]);
                        integerValue++;
                        value = integerValue + " " + splitedValues[1];

                    } else {
                        integerValue = Integer.parseInt(splitedValues[1]);
                        integerValue++;
                        value = splitedValues[0] + " " + integerValue;
                    }

                    Cookie ck = new Cookie(cookieName, value);//creating cookie object  
                    response.addCookie(ck);//updating cookie in the response
                    return;
                }
            }
        }

        //If player deleted cookie during the game
        Cookie ck;
        if (win) {
            ck = new Cookie(cookieName, "1 0");//creating cookie object 
        } else {
            ck = new Cookie(cookieName, "0 1");//creating cookie object 
        }
        response.addCookie(ck);//adding cookie in the response
    }

    /**
     * Deletes cookie with given name
     *
     * @param response servlet response
     * @param cookieName given cookie name
     */
    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie ck = new Cookie(cookieName, null);//creating cookie object
        ck.setMaxAge(0);
        response.addCookie(ck);//updating cookie in the response
    }

    /**
     * Returns converted array of cookies to display
     *
     * @param request servlet request
     * @param response servlet response
     * @return converted array of cookies
     */
    public ArrayList<ArrayList<String>> cookiesToDisplay(HttpServletRequest request, HttpServletResponse response) {

        ArrayList<ArrayList<String>> cookieArray = new ArrayList<>();
        Cookie cookies[] = request.getCookies();

        if (cookies != null) {

            //Loop all cookies
            for (Cookie cookie : cookies) {

                if (!cookie.getName().equals("JSESSIONID")) {
                    //Pull all data
                    ArrayList<String> tmp = new ArrayList<>();
                    String[] splitedValues = cookie.getValue().split(" ");

                    //Push to arrayList
                    tmp.add(cookie.getName());
                    tmp.add(splitedValues[0]);
                    tmp.add(splitedValues[1]);

                    //Pust tmp arrayList to main arrayList
                    cookieArray.add(tmp);
                }
            }
        }

        return cookieArray;
    }
}
