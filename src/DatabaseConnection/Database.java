package DatabaseConnection;

import java.sql.*;

public class Database implements Communication {
    private final String DB_URL= "jdbc:mysql://localhost:3306/";
    private final String USERNAME= "root";
    private final String PASSSWORD= "123123123";
    private  final  String CREATE_DB="CREATE DATABASE IF NOT EXISTS KOLOKWIUM";
    private  final  String IF_TABLE_EXISTS="DROP TABLE IF EXISTS pytania";

    private  final  String CREATE_TABLE=


            // "DROP TABLE IF EXISTS pytania; "
            "CREATE TABLE pytania (\n" +
                    "    id INTEGER NOT NULL PRIMARY KEY,\n" +
                    "    pytanie varchar(255),\n" +
                    "    odpowiedz varchar(255),\n" +
                    "    odpowiedzPrawidłowa varchar(255)\n" +

                    ");";
    private final String[] arrayOfQuestions= new String[]{
            "Które medium transmisyjne należy zastosować, aby połączyć dwa punkty dystrybucyjne odległe od siebie o 600 m?",
            "Która usługa serwerowa zapewnia automatyczną konfigurację parametrów sieciowych stacji roboczych?",
            "W firmowej sieci bezprzewodowej została uruchomiona usługa polegająca na tłumaczeniu nazw mnemonicznych. Jest to usługa"
    };
    private final String[] arrayOfAnswers= new String[]{
            "A. skrętkę STP  B. przewód koncentryczny C. światłowód D. skrętkę UTP",
            "A. WINS B. DHCP C. DNS D. NAT",
            "A. DNS B. DHCP C. RDS D. RADIUS"
    };
    private final String[] arrayOfCorrectAnswers= new String[]{
            "C. światłowód ",
            "A. WINS ",
            "A. DNS "
    };

    public Database(){

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSSWORD);
             PreparedStatement stmt = conn.prepareStatement(CREATE_DB)) {
            stmt.execute();
            stmt.close();
            System.out.println("DATABASE CREATED . . .");
            batchComandCreatingTable(CREATE_TABLE,
                    insertInto(0,arrayOfQuestions[0],arrayOfAnswers[0],arrayOfCorrectAnswers[0]),
                    insertInto(1,arrayOfQuestions[1],arrayOfAnswers[1],arrayOfCorrectAnswers[1]),
                    insertInto(2,arrayOfQuestions[2],arrayOfAnswers[2],arrayOfCorrectAnswers[2]));



            conn.close();

        }catch (SQLException s){
            s.printStackTrace();
        }


    }
    public void batchComandCreatingTable(String... sqls){
        String[] arrStrings = new String[sqls.length];
        try {

            Connection connection = DriverManager.getConnection(DB_URL + "kolokwium", USERNAME, PASSSWORD);
            DatabaseMetaData dbm= connection.getMetaData();
            Statement  statement = connection.createStatement();
            ResultSet tables = dbm.getTables(null, null, "pytania", null);
            if (tables.next()) {
                statement.execute("DROP TABLE pytania");
            }


            for (String s:sqls) {
                statement.addBatch(s);

            }
            connection.setAutoCommit(false);
            statement.executeBatch();

            ResultSet resultSet= statement.executeQuery("SELECT * FROM pytania");



            connection.commit();
            statement.close();
            connection.close();

        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public String insertInto(int id,String question,String answer,String correctAnswer){
        String sql="";
        sql="INSERT INTO pytania (id,pytanie,odpowiedz,odpowiedzPrawidłowa) VALUES('"+id+"','"+question+"','"+answer+"','"+correctAnswer+"')";
        return sql;
    }


    @Override
    public String  getQuestion(int number) throws SQLException {
        int counter=0;
        String []questions=new String[arrayOfQuestions.length];
        Connection connection = DriverManager.getConnection(DB_URL + "kolokwium", USERNAME, PASSSWORD);
        DatabaseMetaData dbm= connection.getMetaData();
        Statement  statement = connection.createStatement();
        ResultSet tables = dbm.getTables(null, null, "pytania", null);
        if (!tables.next()) {
            System.out.println("THERES NO TABLE IN DB");
        }

        ResultSet resultSet= statement.executeQuery("SELECT * FROM pytania");
        while (resultSet.next()){
            questions[counter++]=resultSet.getString(2);


        }

        return questions[number];
    }

    @Override
    public String getAnswer(int number) throws SQLException {
        int counter=0;
        String []answer=new String[arrayOfQuestions.length];
        Connection connection = DriverManager.getConnection(DB_URL + "kolokwium", USERNAME, PASSSWORD);
        DatabaseMetaData dbm= connection.getMetaData();
        Statement  statement = connection.createStatement();
        ResultSet tables = dbm.getTables(null, null, "pytania", null);
        if (!tables.next()) {
            System.out.println("THERES NO TABLE IN DB");
        }
        ResultSet resultSet= statement.executeQuery("SELECT * FROM pytania");
        while (resultSet.next()){
            answer[counter++]= resultSet.getString(3);


        }
        return answer[counter];
    }

    @Override
    public String getCorrectAnswer() throws SQLException {
        String answer="";
        Connection connection = DriverManager.getConnection(DB_URL + "kolokwium", USERNAME, PASSSWORD);
        DatabaseMetaData dbm= connection.getMetaData();
        Statement  statement = connection.createStatement();
        ResultSet tables = dbm.getTables(null, null, "pytania", null);
        if (!tables.next()) {
            System.out.println("THERES NO TABLE IN DB");
        }

        ResultSet resultSet= statement.executeQuery("SELECT * FROM pytania");
        while (resultSet.next()){
            answer=resultSet.getString(4) ;


        }

        return answer;
    }
}
