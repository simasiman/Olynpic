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
            if (m == null)
            {
                m = FinishedMatchList.getMatch(key);
            }

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
                }
                else if (gameMode2 != null)
                {
                    // 初回ゲーム作成時
                    // 待機状態のマッチが存在しないか探査
                    m = MatchList.getMatchWaiting(key);

                    User user = new User(key, name);

                    if (m == null)
                    {
                        // 待機状態のマッチが存在しなければ新規マッチ登録
                        m = new Match();
                        m.createMatchMulti(user, 2);
                    }
                    else
                    {
                        // 待機状態のマッチが存在すれば、そのマッチにユーザ登録
                        m.addUser(user);

                        // 参加人数を満たした場合、ゲームスタート
                        if (m.getPlayerCount() == m.getUserCount())
                        {
                            m.startMatch();
                        }
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
                if (req.getParameter("selectedPanel") != null)
                {
                    int selected = Integer.parseInt(req.getParameter("selectedPanel"));
                    Panel selectedPanel = m.getPanelList().get(selected);
                    if (!selectedPanel.isUsed())
                    {
                        // 使われていないパネルが選択された場合
                        if (m.isFirstPick())
                        {
                            // 最初のパネル選択の場合
                            m.firstPick(key, selectedPanel);
                        }
                        else if (m.nextPick(key, selectedPanel))
                        {
                            // 最初以降のパネル選択の場合
                            // パネル選択可能の場合
                        }
                        else
                        {
                            // 最初以降のパネル選択の場合
                            // パネル選択不可能の場合
                            req.setAttribute("message", "MISS");
                        }
                    }
                }
                else
                {
                    // ゲーム更新時
                }
            }

            if (!m.isFinish() && !m.isEnableContinue())
            {
                m.finishMatch();
            }

            if (!m.isStart())
            {
                urlPath = "/matching.jsp";
            }
            else
            {
                urlPath = "/game.jsp";
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
