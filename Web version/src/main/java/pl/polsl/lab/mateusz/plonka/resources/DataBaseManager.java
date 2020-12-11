package pl.polsl.lab.mateusz.plonka.resources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.javatuples.Septet;

/**
 * Manages adding, destroing and updating game history data base
 *
 * @author Mateusz Płonka
 */
public class DataBaseManager {

    //Const values of table names, connection parrams and login info
    private final String mainTable = "ROUNDS";
    private final String bankTable = "BANK";
    private final String driver = "org.apache.derby.jdbc.ClientDriver";
    private final String dbServer = "jdbc:derby://localhost:1527/HangmanGame";
    private final String user = "SmartMatt";
    private final String pass = "myPass";

    //Local data base reppository
    private ArrayList<Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>>> localDataBase;

    //Id counters
    private int mainTableIndex;
    private int bankTableIndex;

    /**
     * Main construcotr of data base manager
     */
    public DataBaseManager() {
        //Creating a local db
        this.localDataBase = new ArrayList<Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>>>();

        //Defining index var
        this.mainTableIndex = 0;
        this.bankTableIndex = 0;
    }

    /*Variables getters*/
    public String getMainTable() {
        return mainTable;
    }

    public String getBankTable() {
        return bankTable;
    }

    public int getMainTableIndex() {
        return mainTableIndex;
    }

    public int getBankTableIndex() {
        return bankTableIndex;
    }

    public ArrayList<Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>>> getLocalDataBase() {
        return localDataBase;
    }

    /*Variables setters*/
    public void setLocalDataBase(ArrayList<Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>>> localDataBase) {
        this.localDataBase = localDataBase;
    }

    /**
     * Check if table with given name exists in database
     *
     * @param tableName given table name
     * @return [true] if exists, [false] if not
     */
    public boolean tableExists(String tableName) {

        boolean result = false;

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return result;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {

            DatabaseMetaData dbm = con.getMetaData();
            // check if "employee" table is there
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            if (tables.next()) {
                result = true;
            }

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }

        return result;

    }

    /**
     * Creates two obligatory tables for HangmanGame
     *
     * @param tableName name of table to create
     */
    public void createTables(String tableName) {

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {
            Statement statement = con.createStatement();
            if (tableName.equals(this.mainTable)) {
                statement.executeUpdate("CREATE TABLE " + mainTable + " (ID INTEGER NOT NULL PRIMARY KEY, PLAYERNAME VARCHAR(20), STATUS BOOLEAN, WORD VARCHAR(20), MAXERRORS INTEGER, TRIES INTEGER)");
                this.mainTableIndex = 0;
                System.out.println("Table Rounds");
            } else if (tableName.equals(this.bankTable)) {
                if (this.tableExists("ROUNDS")) {
                    statement.executeUpdate("CREATE TABLE " + bankTable + " (ID INTEGER NOT NULL PRIMARY KEY, WORD VARCHAR(20), ROUNDKEY INTEGER ,FOREIGN KEY(ROUNDKEY) REFERENCES ROUNDS(ID))");
                    this.bankTableIndex = 0;
                    System.out.println("Table Bank");
                } else {
                    System.out.println("Can't create Bank table without Rounds table!!!");
                }
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

    /**
     * Drops table with given name
     *
     * @param tableName given name of table to drops
     */
    public void dropTable(String tableName) {

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {
            Statement statement = con.createStatement();
            // Drop whole table
            statement.executeUpdate("DROP TABLE " + tableName);
            System.out.println("Table " + tableName + "has been dropped!");

            //Cleaning index
            if (tableName.equals(this.bankTable)) {
                this.bankTableIndex = 0;
            } else if (tableName.equals(this.mainTable)) {
                this.localDataBase = new ArrayList<Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>>>();
                this.mainTableIndex = 0;
            }

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

    /**
     * Pulls data of table from given name
     *
     * @param tableName name of table to pull data
     */
    public void selectData(String tableName) {

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {
            Statement statement = con.createStatement();

            if (tableName.equals(this.mainTable)) {
                //Opening collection to db
                ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);

                Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>> tmpTuple = null;

                // Pulling data to local repository
                while (rs.next()) {

                    tmpTuple = Septet.with(rs.getInt("ID"), rs.getString("PLAYERNAME"), rs.getBoolean("STATUS"), rs.getString("WORD"), rs.getInt("MAXERRORS"), rs.getInt("TRIES"), new ArrayList<String>());
                    this.localDataBase.add(tmpTuple);
                    this.mainTableIndex++;
                }

                rs.close();

                //Adding to bank table, localDataBase has to exist  
            } else if (tableName.equals(this.bankTable) && this.localDataBase != null) {
                //Opening collection to db
                ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);

                // Pulling data to local repository
                while (rs.next()) {
                    this.localDataBase.get(rs.getInt("ROUNDKEY")).getValue6().add(rs.getString("WORD"));
                    this.bankTableIndex++;
                }

                rs.close();

            } else {
                System.out.println("HangmanGame does not consist table called " + tableName);
            }

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

    /**
     * Insterts data to table with given name (created for main table)
     *
     * @param tableName name of table to update
     * @param id id od tuple
     * @param playerName playerName to add
     * @param win if player won or failed in this round
     * @param playedWord word that was played in this round
     * @param maxErrors max mistakes in this round
     * @param tries tries did in this round
     */
    public void insertData(String tableName, int id, String playerName, boolean win, String playedWord, int maxErrors, int tries) {

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {
            Statement statement = con.createStatement();

            if (tableName.equals(this.mainTable)) {
                statement.executeUpdate("INSERT INTO " + tableName + " VALUES ("
                        + id + ", '"
                        + playerName + "', "
                        + win + ", '"
                        + playedWord + "', "
                        + maxErrors + ", "
                        + tries + ")");
                Septet<Integer, String, Boolean, String, Integer, Integer, ArrayList<String>> tmp = Septet.with(id, playerName, win, playedWord, maxErrors, tries, new ArrayList<String>());
                this.localDataBase.add(tmp);
                this.mainTableIndex++;

            } else {
                System.out.println("HangmanGame does not consist table called " + tableName);
            }

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

    /**
     * Insterts data to table with given name (created for bank table)
     *
     * @param tableName name of table to update
     * @param id id of tuple to add
     * @param word word to add
     * @param roundKey key to connect with main table
     */
    public void insertData(String tableName, int id, String word, int roundKey) {

        try {
            // loading the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try (Connection con = DriverManager.getConnection(dbServer, user, pass)) {
            Statement statement = con.createStatement();

            if (tableName.equals(this.bankTable)) {
                statement.executeUpdate("INSERT INTO " + tableName + " VALUES (" + id + ",'" + word + "'," + roundKey + ")");

                this.localDataBase.get(roundKey).getValue6().add(word);
                this.bankTableIndex++;

            } else {
                System.out.println("HangmanGame does not consist table called " + tableName + "\n or bad arguments were given!");
            }

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }
    }

}
