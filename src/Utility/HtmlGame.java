package Utility;

import java.util.ArrayList;

import modelPack.Match;
import modelPack.Panel;
import modelPack.User;
import modelPack.Word;

public class HtmlGame
{
    private static final boolean DEBUG = true;

    private static final int PANEL_COL = 4;

    private static int indentCount = 0;

    public static String makeGameHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        indentCount = 0;

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"page\">"));
        ret.append(Utility.appendLineIndent(indentCount, "<header><img src=\"img/logo/pane-tori-logo_s.png\" alt=\"ゲームのロゴ\"></header>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"wapper wapper_" + match.getPlayerCount() + " clearfix\">"));

        ret.append(Utility.appendLine(makeUserSelectedHtml(match, key, 1)));
        ret.append(Utility.appendLine(makeGamePanelHtml(match, key)));

        if (match.getPlayerCount() == 2)
        {
            ret.append(Utility.appendLine(makeUserSelectedHtml(match, key, 2)));
        }

        if (!match.isFinish() && DEBUG)
        {
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"javascript:void(0);\" onclick=\"WebSocketDemo.send('" + key + "," + "gameEnd" + "');\">※ゲーム終了</a>"));
        }

        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        ret.append(Utility.appendLineIndent(indentCount, "<footer><p>&copy;&nbsp;2017 ARAI CORPORATION.</p></footer>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        return ret.toString();
    }

    public static String makeGamePanelHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"panelWapper\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"tableOuter\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<table>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<tr>"));

        User userMe = match.getUser(key);

        ArrayList<Panel> panelList = match.getPanelList();
        for (int i = 0; i < panelList.size(); i++)
        {
            Panel panel = panelList.get(i);
            String cssClass = "";
            String panelHtml = "";

            boolean isCanSelect = match.isHisTurn(key) && !match.isFinish() && match.isEnableContinue() && !panel.isUsed();
            boolean isCorrectSelect = !match.isFirstPick() && panel.isMatchWord(match.getNowWord(), match.getSelectedWordList()) >= 0;
            boolean isUsed = !match.isFirstPick() && panel.isUsed();
            boolean isLastSelected = panel == match.getLastSelectedPanel();

            // CSS用のクラス名の定義
            if (isLastSelected)
            {
                if (match.isLAMiss())
                {
                    cssClass += "lastMiss ";
                }
                else
                {
                    cssClass += "lastCorrect ";
                }
            }

            if (isCanSelect)
            {
                cssClass += "canChoose ";
                if (userMe.isShowHint())
                {
                    if (isCorrectSelect)
                    {
                        cssClass += "correct ";
                    }
                    else
                    {
                        cssClass += "error ";
                    }
                }
            }
            else if (!isLastSelected && isUsed)
            {
                cssClass += "selected ";
            }
            else
            {
                cssClass += "cannotChoose ";
            }

            String panelImage = "<img src=\"img/panel/" + panel.getPicture() + "\" alt=\"" + panel.getBaseWord() + " \" class=\"" + cssClass + "\">";

            if (isCanSelect)
            {
                panelHtml = "<a href=\"javascript:void(0);\" onclick=\"WebSocketDemo.send('" + key + "," + i + "');\">" + panelImage + "</a>";
            }
            else
            {
                panelHtml = panelImage;
            }

            // HTMLの書き込み
            ret.append(Utility.appendLineIndent(indentCount++, "<td class=\"panelImg\">"));
            ret.append(Utility.appendLineIndent(indentCount, panelHtml));
            ret.append(Utility.appendLineIndent(--indentCount, "</td>"));

            // 一定枚数ごとの改行
            if (i != 0 && i != panelList.size() - 1 && i % PANEL_COL == PANEL_COL - 1)
            {
                ret.append(Utility.appendLineIndent(--indentCount, "</tr>"));
                ret.append(Utility.appendLineIndent(indentCount++, "<tr>"));
            }
        }

        ret.append(Utility.appendLineIndent(--indentCount, "</tr>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</table>"));

        if (match.isFinish())
        {
            ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"gameOverOuter\">"));
            ret.append(Utility.appendLineIndent(indentCount++, "<div>"));
            ret.append(Utility.appendLineIndent(indentCount++, "<div>"));
            ret.append(Utility.appendLineIndent(indentCount++, "<p>"));
            ret.append(Utility.appendLineIndent(indentCount, "ゲームが終了しました"));
            ret.append(Utility.appendLineIndent(--indentCount, "</p>"));
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"result\">リザルト画面へ</a>"));
            ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
            ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
            ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        }

        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        ret.append(Utility.appendLineIndent(indentCount, makeMessageHtml(match, key)));

        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        return ret.toString();
    }

    public static String makeMessageHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"message\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<p>"));

        if (match.isStart())
        {
            Word nowWord = match.getNowWord();
            if (nowWord != null)
            {
                String message1 = "選択されたパネルは「" + nowWord.getWord() + "」です。";
                String message2 = "使える文字は「" + nowWord.getWordHead() + "」「" + nowWord.getWordTail() + "」です。";
                ret.append(Utility.appendLineIndent(indentCount, message1 + "<br>" + message2));
            }
            else
            {
                String message1 = "最初のターンはすべてのパネルを選択できます。";
                String message2 = "好きなパネルを選択してください。";
                ret.append(Utility.appendLineIndent(indentCount, message1 + "<br>" + message2));
            }
        }
        else
        {
            String message1 = "Game Set!!";
            ret.append(Utility.appendLineIndent(indentCount, message1));
        }

        ret.append(Utility.appendLineIndent(--indentCount, "</p>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        return ret.toString();
    }

    public static String makeUserSelectedHtml(Match match, String key, int whichPlayer)
    {
        StringBuilder ret = new StringBuilder();

        User userMe = match.getUser(key);
        User userTarget = match.getUserList().get(whichPlayer - 1);

        boolean isPlayer1 = match.getPlayerCount() == 1;

        int col = 0;
        String cssClsPlayer = "";
        String cssClsPlayerName = "";
        String cssClsPlayerScore = "";
        if (isPlayer1)
        {
            col = 3;
            cssClsPlayer = "player_1 ";
            cssClsPlayerName = "playerName_1";
            cssClsPlayerScore = "score_1";
        }
        else
        {
            col = 2;
            cssClsPlayer = "player_2-" + whichPlayer + " ";
            cssClsPlayerName = "playerName_2";
            cssClsPlayerScore = "score_2";
        }

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"player " + cssClsPlayer + "\">"));

        String cssClsPlayerTop = "playerTop ";
        if (userTarget.session == null)
        {
            cssClsPlayerTop += "disconnect ";
        }

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"" + cssClsPlayerTop + "\">"));
        if (!isPlayer1)
        {
            ret.append(Utility.appendLineIndent(indentCount++, "<p class=\"playerDistinction\">"));
            String player = "プレイヤー" + whichPlayer;
            if (userTarget == userMe)
            {
                player += "（あなた)";
            }
            ret.append(Utility.appendLineIndent(indentCount, player));
            ret.append(Utility.appendLineIndent(--indentCount, "</p>"));
        }
        ret.append(Utility.appendLineIndent(indentCount++, "<p class=\"" + cssClsPlayerName + "\">"));
        ret.append(Utility.appendLineIndent(indentCount, userTarget.getName()));
        ret.append(Utility.appendLineIndent(--indentCount, "</p>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
        if (!isPlayer1)
        {
            if (match.isHisTurn(userTarget.getKey()))
            {
                if (!match.isFinish())
                {
                    ret.append(Utility.appendLineIndent(indentCount, "<div id=\"timer\"></div>"));
                }
                ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"nowChoosing\">"));
                ret.append(Utility.appendLineIndent(indentCount++, "<div>"));
                ret.append(Utility.appendLineIndent(indentCount++, "<span>"));
                String message = "対戦相手のターンです";
                if (userTarget == userMe)
                {
                    message = "あなたのターンです";
                }
                ret.append(Utility.appendLineIndent(indentCount, message));
                ret.append(Utility.appendLineIndent(--indentCount, "</span>"));
                ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
                ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
            }
            else
            {
                ret.append(Utility.appendLineIndent(indentCount, "<div class=\"notChoosing\"></div>"));
            }
        }
        else
        {
            if (!match.isFinish())
            {
                ret.append(Utility.appendLineIndent(indentCount, "<div id=\"timer\"></div>"));
            }
        }

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"score " + cssClsPlayerScore + "\">"));
        ret.append(Utility.appendLineIndent(indentCount, "<span>score：</span>" + userTarget.getScore()));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"getPanel\">"));
        ret.append(Utility.appendLineIndent(indentCount, "取得したパネル"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<table>"));

        ArrayList<Panel> panelList = userTarget.getSelectedPanel();
        for (int i = 0; i < panelList.size(); i++)
        {
            if (i == 0)
            {
                ret.append(Utility.appendLineIndent(indentCount++, "<tr>"));
            }

            Panel panel = panelList.get(i);
            String cssClass = "getPanel ";
            switch (i % col)
            {
                case 0:
                    cssClass += "getPanelLeft";
                    break;

                case 1:
                    if (isPlayer1)
                    {
                        cssClass += "getPanelCenter";
                    }
                    else
                    {
                        cssClass += "getPanelRight";
                    }
                    break;

                case 2:
                    cssClass += "getPanelRight";
                    break;
            }

            ret.append(Utility.appendLineIndent(indentCount++, "<td class=\"" + cssClass + "\">"));
            ret.append(Utility.appendLineIndent(indentCount, "<img src=\"img/panel/" + panel.getPicture() + "\" alt=\"" + panel.getBaseWord() + "\">"));
            ret.append(Utility.appendLineIndent(--indentCount, "</td>"));

            if (i != 0 && i % col == col - 1 || i == panelList.size() - 1)
            {
                ret.append(Utility.appendLineIndent(--indentCount, "</tr>"));
                if (i != panelList.size() - 1)
                {
                    ret.append(Utility.appendLineIndent(indentCount++, "<tr>"));
                }
            }
        }

        ret.append(Utility.appendLineIndent(--indentCount, "</table>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        return ret.toString();
    }

}
