package modelPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Utility.GameSetting;

public class SiritoriDao
{
    private Connection connection;

    public SiritoriDao() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");

        String server = GameSetting.SERVER_ADDRESS;
        String database = GameSetting.DB_DATABASE;
        String user = GameSetting.DB_USER;
        String password = GameSetting.DB_PASS;
        String encoding = GameSetting.DB_ENCOFING;

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

    public int getNextMatchNumber() throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        int ret = 0;

        try
        {
            String sql = ""
                    + " SELECT MAX(id) + 1 as max"
                    + "   FROM tbl_play_result ";

            pstatement = connection.prepareStatement(sql);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = rs.getInt("max");
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public User selectUser(String key) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        User ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, "
                    + "        name "
                    + "   FROM tbl_user "
                    + "  WHERE user_key = ? ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, key);
            rs = pstatement.executeQuery();
            if (rs.next())
            {
                ret = new User(rs.getString("user_key"), rs.getString("name"));
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public boolean insertUser(User user) throws SQLException
    {
        PreparedStatement pstatement = null;
        boolean ret = false;

        try
        {
            String sql = ""
                    + " INSERT INTO tbl_user "
                    + "             (user_key, name) "
                    + "      VALUES (?, ?) ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, user.getKey());
            pstatement.setString(2, user.getName());
            ret = pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public boolean updateUser(User user) throws SQLException
    {
        PreparedStatement pstatement = null;
        boolean ret = false;

        try
        {
            String sql = ""
                    + " UPDATE tbl_user "
                    + "    SET name = ? "
                    + "  WHERE user_key  = ? ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, user.getName());
            pstatement.setString(2, user.getKey());
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
                    // + " ORDER BY ath_id "
                    + "  ORDER BY RAND() "
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
                        + "SELECT word_disp, "
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
                        int level = rs2.getInt("level");
                        String word = rs2.getString("word_disp");
                        String wordRead = rs2.getString("word_read");
                        String wordHead = rs2.getString("word_head");
                        String wordTail = rs2.getString("word_tail");

                        Word w = new Word(level, word, wordRead, wordHead, wordTail);

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

    public boolean insertPlayResult(PlayResult result) throws SQLException
    {
        PreparedStatement pstatement = null;
        boolean ret = false;

        try
        {
            String sql = ""
                    + " INSERT INTO tbl_play_result "
                    + "             (id, user_key, playdate, winlose, score, playerCount) "
                    + "      VALUES (?, ?, ?, ?, ?, ?) ";

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, getNextMatchNumber());
            pstatement.setString(2, result.getKey());
            pstatement.setDate(3, new java.sql.Date(result.getPlayDate().getTime()));
            pstatement.setInt(4, result.getWinLose());
            pstatement.setInt(5, result.getScore());
            pstatement.setInt(6, result.getPlayerCount());

            ret = pstatement.execute();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public ArrayList<User> getHighScoreRanking(int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, "
                    + "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, "
                    + "       MAX(score) as highScore "
                    + "  FROM tbl_play_result AS base "
                    + " WHERE playerCount = ? "
                    + " GROUP by user_key; ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, playerCount);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setHighScore(rs.getInt("highScore"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public ArrayList<User> getHighScoreRanking(String key, int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, "
                    + "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, "
                    + "       MAX(score) as highScore "
                    + "  FROM tbl_play_result AS base "
                    + " WHERE playerCount = ? "
                    + "   AND user_key = ? "
                    + " GROUP by user_key; ";

            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, playerCount);
            pstatement.setString(2, key);
            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setHighScore(rs.getInt("highScore"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public ArrayList<User> getWinLoseRanking(int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = "" +
                    " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 1) AS win, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 2) AS lose, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = -1) AS draw " +
                    "  FROM tbl_play_result AS base " +
                    " GROUP BY user_key " +
                    " ORDER BY win desc, draw, lose desc ";

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, playerCount);
            pstatement.setInt(2, playerCount);
            pstatement.setInt(3, playerCount);

            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setWinCount(rs.getInt("win"));
                user.setLoseCount(rs.getInt("lose"));
                user.setDrawCount(rs.getInt("draw"));

                ret.add(user);
            }

            rs.close();
        }
        finally
        {
            pstatement.close();
        }

        return ret;
    }

    public ArrayList<User> getWinLoseRanking(String key, int playerCount) throws SQLException
    {
        PreparedStatement pstatement = null;
        ResultSet rs = null;
        ArrayList<User> ret = null;

        try
        {
            String sql = ""
                    + " SELECT user_key, " +
                    "       (SELECT name FROM tbl_user WHERE user_key = base.user_key) AS name, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 1) AS win, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = 2) AS lose, " +
                    "       (SELECT COUNT(user_key) FROM tbl_play_result WHERE user_key = base.user_key AND playerCount = ? AND winLose = -1) AS draw " +
                    "  FROM tbl_play_result AS base " +
                    " WHERE user_key = ? " +
                    " GROUP BY user_key ";

            pstatement = connection.prepareStatement(sql);

            pstatement.setInt(1, playerCount);
            pstatement.setInt(2, playerCount);
            pstatement.setInt(3, playerCount);
            pstatement.setString(4, key);

            rs = pstatement.executeQuery();
            while (rs.next())
            {
                if (ret == null)
                {
                    ret = new ArrayList<User>();
                }

                User user = new User();

                user.setKey(rs.getString("user_key"));
                user.setName(rs.getString("name"));
                user.setWinCount(rs.getInt("win"));
                user.setLoseCount(rs.getInt("lose"));
                user.setDrawCount(rs.getInt("draw"));

                ret.add(user);
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
