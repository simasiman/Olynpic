package ControlPack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelPack.FinishedMatchList;
import modelPack.Match;
import modelPack.MatchList;
import modelPack.Panel;
import modelPack.User;
import modelPack.Word;

@SuppressWarnings("serial")
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

        String urlPath = "/";

        // TODO:重複したユーザのマッチング登録が発生しないかを確認

        try
        {
            String key = (String) req.getParameter("key");
            String name = (String) req.getParameter("name");

            if (key == null || name == null)
            {
                key = (String) session.getAttribute("key");
                name = (String) session.getAttribute("name");
            }

            session.setAttribute("key", key);
            session.setAttribute("name", name);

            // 既にマッチング済のユーザであれば、試合に戻らせる
            Match m = MatchList.getMatch(key);

            // ゲーム開始状態か判断する
            if (m == null)
            {
                String gameMode1 = (String) req.getParameter("mode1");
                String gameMode2 = (String) req.getParameter("mode2");

                if (gameMode1 != null)
                {
                    // 初回ゲーム作成時
                    // 一人用であれば必ず新規マッチ作成
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
                    m = MatchList.getMatchWaiting(key);

                    User user = new User(key, name);

                    if (m != null)
                    {
                        // 待機状態のマッチが存在すれば、そのマッチにユーザ登録
                        m.addUser(user);

                        // 参加人数を満たした場合、ゲームスタート
                        if (m.getPlayerCount() == m.getUserCount())
                        {
                            m.startMatch();
                        }
                    }
                    else
                    {
                        // 待機状態のマッチが存在しなければ新規マッチ登録
                        m = new Match();
                        m.createMatchMulti(user, 2);

                        // マッチ管理用リストに追加
                        MatchList.addMatchList(m);
                    }
                }
                else
                {
                    FinishedMatchList.clean();
                    m = FinishedMatchList.getMatch(key);

                    if (m == null)
                    {
                        // マッチング列に存在しないユーザの場合、トップ画面に戻る
                        req.setAttribute("match", null);
                        resp.sendRedirect("top");
                        return;
                    }
                }
            }
            else
            {
                // ゲーム継続時

                // TODO:各取得パラメータによってマッチ状態(ゲーム進行)を更新

                if (req.getParameter("selectedPanel") != null)
                {
                    int selected = Integer.parseInt(req.getParameter("selectedPanel"));

                    String wH = m.getFirstWord();
                    String wT = m.getLastWord();

                    Panel selectedPanel = m.getPanelList().get(selected);

                    if (!selectedPanel.isUsed())
                    {
                        int nextWordIndex = selectedPanel.isMatchWord(wH, wT);
                        if (nextWordIndex < 0)
                        {
                            // 単語がミスしている場合
                            m.addMissCount(key);

                            req.setAttribute("miss", "ミス");
                        }
                        else
                        {
                            // 単語がマッチしている場合
                            selectedPanel.setUsed(true);
                            selectedPanel.setSelectedUserId(key);

                            Word word = selectedPanel.getWordList().get(nextWordIndex);
                            m.setWord(word);
                            m.addSelectPanel(key, selectedPanel);
                            m.addScore(key, word);

                            m.setPlayerTurn((m.getPlayerTurn() + 1) % m.getPlayerCount());
                        }
                    }
                }
                else
                {
                    // ゲーム更新時
                }
            }

            if (!m.isEnableContinue())
            {
                m.finishMatch();
            }

            if (!m.isStart())
            {
                urlPath = "/gameMatching.jsp";
            }
            else if (m.isFinish())
            {
                urlPath = "/gameFinished.jsp";
            }
            else if (m.isHisTurn(key))
            {
                urlPath = "/gameMyTurn.jsp";
            }
            else
            {
                urlPath = "/gameNotMyTurn.jsp";
            }

            req.setAttribute("match", m);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher(urlPath);
        rd.forward(req, resp);
    }
}
