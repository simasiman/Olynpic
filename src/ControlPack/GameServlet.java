package ControlPack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelPack.Match;
import modelPack.MatchList;

/**
 * URL:gameに対するPost時の処理
 */
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

        String urlPath = "/game.jsp";

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

            // 既にマッチング済のユーザであれば、試合に戻らせる
            Match m = MatchList.getMatch(key);
            if (m == null)
            {
                // 終了済みマッチングリストからもユーザを検索
                m = MatchList.getMatchFinished(key);
            }

            if (m == null)
            {
                // マッチング列に存在しないユーザの場合、トップ画面に戻る
                req.setAttribute("match", null);
                resp.sendRedirect("top");

                return;
            }

            if (!m.isStart())
            {
                // 開始されていない状態であれば、マッチング画面に戻る
                req.setAttribute("match", m);
                resp.sendRedirect("matching");

                return;
            }

            // TODO : デバッグモード有効状態
            if (req.getParameter("debugEnd") != null)
            {
                m.finishMatch();
            }

            m.panelSelect(key, (String) req.getParameter("selectedPanel"));

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
