package modelPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SiritoriDao
{
    private Connection connection;

    public SiritoriDao() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");

        String server = "localhost";
        String database = "Olynpic";
        String user = "Mulder";
        String password = "TrustNo1";
        String encoding = "UTF-8";

        String strConn = "jdbc:mysql://" + server + "/" + database + "?user=" + user + "&password=" + password + "&useUnicode=true&characterEncoding=" + encoding;

        connection = DriverManager.getConnection(strConn);
    }

    public void close()
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String selectUserName(String key) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        String ret = null;

        try
        {
            String sql = ""
                    + " SELECT name "
                    + "   FROM tbl_user "
                    + "  WHERE key = ? ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, key);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = rs.getString("key");
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public boolean insertUser(String key, String name) throws SQLException
    {
        PreparedStatement pstatement = null;
        boolean ret = false;

        try
        {
            String sql = ""
                    + " INSERT INTO tbl_user "
                    + "             (key, name) "
                    + "      VALUES (?, ?) ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, key);
            pstatement.setString(2, name);
            ret = pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public ArrayList<Panel> getRandomPanel(int panelCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<Panel> ret = null;

        try
        {
            String sql = ""
                    + " SELECT ath_id, "
                    + "        name, "
                    + "        picture "
                    + "   FROM tbl_word_base "
                    + "  ORDER BY ath_id "
                    + "  LIMIT 0, ?";

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, panelCount);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<Panel>();
                }

                Panel p = new Panel();

                p.setId(rs.getInt("ath_id"));
                p.setBaseWord(rs.getString("name"));
                p.setPicture(rs.getString("picture"));

                PreparedStatement pStatement2 = null;
                ResultSet rs2 = null;

                String sql2 = ""
                        + "SELECT word_id, "
                        + "       word_disp, "
                        + "       word_read, "
                        + "       level, "
                        + "       LEFT(word_read, 1) AS word_head, "
                        + "       IF(right(word_read, 1) != \"ー\", "
                        + "                right(word_read, 1), "
                        + "                left(right(word_read, 2), 1)) AS word_tail "
                        + "  FROM tbl_word_siritori "
                        + " WHERE ath_id = ? "
                        + " ORDER BY level ";

                pStatement2 = connection.prepareStatement(sql2);
                pStatement2.setInt(1, p.getId());
                rs2 = pStatement2.executeQuery();
                try
                {
                    while (rs2.next())
                    {
                        int id = rs2.getInt("word_id");
                        int level = rs2.getInt("level");
                        String word = rs2.getString("word_disp");
                        String wordRead = rs2.getString("word_read");
                        String wordHead = rs2.getString("word_head");
                        String wordTail = rs2.getString("word_tail");

                        Word w = new Word(id, level, word, wordRead, wordHead, wordTail);

                        p.addWordList(w);
                    }

                    rs2.close();
                }
                finally
                {
                    pStatement2.close();
                }

                ret.add(p);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

}
