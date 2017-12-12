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
import modelPack.Panel;

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
            else
            {
                // TODO : デバッグモード有効状態
                if (req.getParameter("debugEnd") != null)
                {
                    m.finishMatch();
                }

                if (m.isFinish())
                {
                    // ゲーム終了時
                }
                else if (!m.isFinish() && m.timeOutCheck())
                {
                    // 制限時間外の場合(タイマー停止時はここに入る)
                    if (m.isTimeOutEnd())
                    {
                        m.finishMatch();
                    }
                }
                else if (req.getParameter("selectedPanel") != null)
                {
                    // ゲーム継続時
                    int selected = Integer.parseInt(req.getParameter("selectedPanel"));
                    Panel selectedPanel = m.getPanelList().get(selected);

                    if (m.isPanelRange(selected) && !selectedPanel.isUsed())
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
