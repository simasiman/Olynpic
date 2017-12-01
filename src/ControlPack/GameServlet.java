package ControlPack;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelPack.Match;
import modelPack.MatchList;
import modelPack.Panel;
import modelPack.User;
import modelPack.Word;

public class GameServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        String urlPath = "/game.jsp";

        // TODO:重複したユーザのマッチング登録が発生しないかを確認

        try
        {
            // ゲーム開始状態か判断する
            String gameMode1 = (String) req.getParameter("mode1");
            String gameMode2 = (String) req.getParameter("mode2");

            Match m = null;

            if (gameMode1 != null)
            {
                // 初回ゲーム作成時
                // 一人用であれば必ず新規マッチ作成
                String key = (String) req.getParameter("key");
                String name = (String) req.getParameter("name");

                session.setAttribute("key", key);
                session.setAttribute("name", name);

                User user = new User(key, name);
                m = new Match();
                m.createMatch1(user);

                // マッチ管理用リストに追加
                MatchList.addMatchList(m);
            }
            else if (gameMode2 != null)
            {
                // 初回ゲーム作成時
                // 待機状態のマッチが存在しないか探査
                m = MatchList.getMatching();

                if (m != null)
                {
                    // 待機状態のマッチが存在すれば、そのマッチにユーザ登録
                    String key = (String) req.getParameter("key");
                    String name = (String) req.getParameter("name");

                    session.setAttribute("key", key);
                    session.setAttribute("name", name);

                    User user = new User(key, name);
                    m.addUser(user);

                    // 参加人数を満たした場合、ゲームスタート
                    if (m.getPlayerCount() == m.getUserCount())
                    {
                        m.setStartTime(new Date());
                        m.setStart(true);
                    }
                }
                else
                {
                    // 待機状態のマッチが存在しなければ新規マッチ登録
                    String key = (String) req.getParameter("key");
                    String name = (String) req.getParameter("name");

                    session.setAttribute("key", key);
                    session.setAttribute("name", name);

                    User user = new User(key, name);
                    m = new Match();
                    m.createMatchMulti(user, 2);

                    // マッチ管理用リストに追加
                    MatchList.addMatchList(m);
                }
            }
            else
            {
                // ゲーム継続時
                String key = (String) session.getAttribute("key");
                String name = (String) session.getAttribute("name");

                // 対象マッチを検索
                for (Match match : MatchList.getMatchList())
                {
                    if (match.isFinish())
                    {
                        continue;
                    }

                    for (User u : match.getUserList())
                    {
                        if (u.getKey().equals(key))
                        {
                            m = match;
                            break;
                        }
                    }

                    if (m != null)
                    {
                        break;
                    }
                }

                // TODO:各取得パラメータによってマッチ状態(ゲーム進行)を更新
                if (req.getParameter("selectedPanel") != null)
                {
                    int selected = Integer.parseInt(req.getParameter("selectedPanel"));

                    String wH = m.getFirstWord();
                    String wT = m.getLastWord();

                    Panel selectedPanel = m.getPanelList().get(selected);

                    int nextWordIndex = selectedPanel.isMatchWord(wH, wT);
                    if (nextWordIndex < 0)
                    {
                        // 単語がミスしている場合
                        req.setAttribute("miss", "ミス");
                    }
                    else
                    {
                        // 単語がマッチしている場合
                        selectedPanel.setUsed(true);
                        selectedPanel.setSelectedUserId(key);
                        m.getUserList().get(m.getPlayerTurn()).addSelectedPanel(selectedPanel);

                        Word word = selectedPanel.getWordList().get(nextWordIndex);
                        m.setNowWord(word.getWord());
                        m.setFirstWord(word.getWordHead());
                        m.setLastWord(word.getWordTail());

                        m.setPlayerTurn(m.getPlayerTurn() + 1 % m.getPlayerCount());

                        if (!m.isEnableContinue())
                        {
                            m.finishMatch();
                        }
                    }
                }
            }

            // TODO:ポストしてきたユーザのマッチを特定し、パラメータにセット
            req.setAttribute("match", m);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher(urlPath);
        rd.forward(req, resp);
        return;
    }
}
