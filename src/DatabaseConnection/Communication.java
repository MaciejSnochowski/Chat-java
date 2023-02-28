package DatabaseConnection;

import java.sql.SQLException;

public interface Communication {
    String getQuestion(int number) throws SQLException;
    String getAnswer(int number) throws  SQLException;
    String getCorrectAnswer() throws SQLException;
}
