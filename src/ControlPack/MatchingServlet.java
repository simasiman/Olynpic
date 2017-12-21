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
import modelPack.MatchUserList;
import modelPack.User;

/**
 * URL:matchingに対するPost時の処理
 */
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
            String key = (String) req.getParameter("key");
            if (key == null || key.isEmpty())
            {
                key = (String) session.getAttribute("key");
            }
            if (key == null || key.isEmpty())
            {
                // 正規ルートでのアクセスでないと判断し、トップに戻す
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            String name = (String) req.getParameter("name");
            if (name == null || name.isEmpty())
            {
                name = (String) session.getAttribute("name");
            }

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
            if (m != null && m.isStart())
            {
                // マッチングが存在すれば、gameへ遷移
                resp.sendRedirect("game");
                return;
            }
            // 終了済みマッチングリストからもユーザを検索
            m = MatchList.getMatchFinished(key);
            if (m != null)
            {
                // マッチングが完了し、未閲覧の場合、resultへ遷移
                resp.sendRedirect("result");
                return;
            }

            // マッチングが存在しない場合、マッチングを新規作成する
            String gameMode1 = (String) req.getParameter("mode1");
            String gameMode2 = (String) req.getParameter("mode2");
            String difficulty1 = (String) req.getParameter("difficulty1");
            String olympic = (String) req.getParameter("olympic");

            int playerCount = 0;
            boolean isShowRedHint = false;
            if (gameMode1 != null && !gameMode1.isEmpty())
            {
                playerCount = 1;
            }
            else if (gameMode2 != null && !gameMode2.isEmpty())
            {
                playerCount = 2;
            }
            else
            {
                // 正規ルートでのアクセスでないと判断し、トップに戻す
                resp.sendRedirect("top");
                return;
            }

            boolean isOlympic = olympic != null && !olympic.isEmpty();
            if (difficulty1 == null || difficulty1.isEmpty())
            {
                olympic = "-";
            }

            if (difficulty1 != null && !difficulty1.isEmpty())
            {
                isShowRedHint = !difficulty1.isEmpty();
            }
            else
            {
                difficulty1 = "-";
            }

            session.setAttribute("difficulty1", difficulty1);
            session.setAttribute("olympic", olympic);

            // 初回ゲーム作成時
            User user = MatchUserList.get(key);
            if (user == null)
            {
                user = new User(key, name);
                user.setWishPlayerCount(playerCount);
                user.setShowHint(isShowRedHint);
                user.setOlympic(isOlympic);

                MatchUserList.add(user);

                if (playerCount == 1)
                {
                    // 一人用であれば、即座にゲーム画面へと遷移
                    Match match = new Match();
                    match.createMatch(1, isOlympic);
                    match.startMatch();

                    resp.sendRedirect("game");
                    return;
                }
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
