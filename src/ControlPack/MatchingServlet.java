package ControlPack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Utility.Utility;
import modelPack.Match;
import modelPack.MatchList;
import modelPack.User;

@SuppressWarnings("serial")
public class MatchingServlet extends HttpServlet
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

        String urlPath = "/matching.jsp";

        try
        {
            String key = (String) session.getAttribute("key");
            if (key == null || key.isEmpty())
            {
                // 正規ルートでのアクセスでないと判断し、トップに戻す
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            String name = (String) session.getAttribute("name");
            if (name == null || name.isEmpty())
            {
                name = Utility.getDefaultName();
            }
            else
            {
                name = Utility.htmlEscape(name);
            }

            session.setAttribute("key", key);
            session.setAttribute("name", name);

            // 既にマッチング済のユーザであれば、試合に戻らせる
            Match m = MatchList.getMatch(key);
            if (m == null)
            {
                // 終了済みマッチングリストからもユーザを検索
                m = MatchList.getMatchFinished(key);
            }

            if (m == null)
            {
                // マッチングが存在しない場合、マッチングを新規作成する
                String gameMode1 = (String) req.getParameter("mode1");
                String gameMode2 = (String) req.getParameter("mode2");

                if (gameMode1 != null && !gameMode1.isEmpty())
                {
                    // 初回ゲーム作成時
                    // 一人用であれば必ず新規マッチ作成
                    User user = new User(key, name);
                    m = new Match();
                    m.createMatch(user, 1);
                    m.startMatch();
                }
                else if (gameMode2 != null && !gameMode2.isEmpty())
                {
                    // 初回ゲーム作成時
                    User user = new User(key, name);

                    // 待機状態のマッチが存在しないか探査
                    m = MatchList.getMatchWaiting(key);
                    if (m != null)
                    {
                        // 待機状態のマッチが存在すれば、そのマッチにユーザ登録
                        m.addUser(user);

                        // 参加人数を満たした場合、ゲームスタート
                        if (m.isCanMatchStart())
                        {
                            m.startMatch();
                        }
                    }
                    else
                    {
                        // 待機状態のマッチが存在しなければ新規マッチ登録
                        m = new Match();
                        m.createMatch(user, 2);
                    }
                }
                else
                {
                    // 正規ルートでのアクセスでないと判断し、トップに戻す
                    req.setAttribute("match", null);
                    resp.sendRedirect("top");

                    return;
                }
            }
            else
            {
                String status = (String) req.getParameter("status");
                if (status != null && status.equals("dest"))
                {
                    // 「破棄」の選択時、マッチングを破棄してトップ画面に戻る
                    MatchList.remove(m);
                    req.setAttribute("match", null);
                    resp.sendRedirect("top");

                    return;
                }
            }

            req.setAttribute("match", m);

            if (m != null && m.isStart())
            {
                // マッチングが存在すれば、gameへ遷移
                resp.sendRedirect("game");

                return;
            }

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
