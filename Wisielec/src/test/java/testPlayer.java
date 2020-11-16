import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.polsl.lab1.mateusz.plonka.model.Player;

/**
 * @author Mateusz Płonka
 */
public class testPlayer {
    
    private Player myPlayer;

    @BeforeEach
    public void setUp() {
        myPlayer = new Player();
    }
    
   //Nothing to test here
}
