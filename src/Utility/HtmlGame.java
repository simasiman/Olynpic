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

    public static String makeGameHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"page\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<header><img src=\"img/logo/pane-tori-logo_s.png\" alt=\"ゲームのロゴ\"></header>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"wapper wapper_" + match.getPlayerCount() + " clearfix\">"));

        ret.append(Utility.appendLine(makeUserSelectedHtml(match, key, 1, indentCount)));
        ret.append(Utility.appendLine(makeGamePanelHtml(match, key, indentCount)));

        if (match.getPlayerCount() == 2)
        {
            ret.append(Utility.appendLine(makeUserSelectedHtml(match, key, 2, indentCount)));
        }

        if (DEBUG)
        {
            ret.append(Utility.appendLine(makeGameReloadHtml(match, key)));
        }

        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
        ret.append(Utility.appendLineIndent(--indentCount, "<footer><p>&copy;&nbsp;2017 ARAI CORPORATION.</p></footer>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div>"));

        return ret.toString();
    }

    public static String makeGameReloadHtml(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        if (!match.isFinish())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"game\">更新</a>"));

            if (DEBUG)
            {
                ret.append(Utility.appendLineIndent(indentCount, "<a href=\"game?debugEnd=1\">※ゲーム終了</a>"));
            }
        }
        else
        {
            ret.append(Utility.appendLineIndent(indentCount, "<a href=\"result\">結果画面へ</a>"));
        }

        return ret.toString();
    }

    public static String makeGameMessageHtml(Match match, String key, int indent)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = indent;

        ret.append(Utility.appendLineIndent(indentCount, String.valueOf(match.getTimeDiff())));

        if (match.isFinish())
        {
            // ゲーム結果の表示
            String result = "";

            int winLose = match.getUserWin(key);

            switch (winLose)
            {
                case User.WIN:
                    result = "勝ちました。";
                    break;

                case User.LOSE:
                    result = "負けました。";
                    break;

                case User.DRAW:
                    result = "引き分けです。";
                    break;
            }

            ret.append(Utility.appendLineIndent(indentCount, "<h1>※ゲームが終了しました</h1>"));
            ret.append(Utility.appendLineIndent(indentCount, "<h2>" + result + "</h2>"));
        }
        else if (match.isHisTurn(key))
        {
            // 自ターン時のメッセージを表示
            ret.append(Utility.appendLineIndent(indentCount, "<h1>※貴方のターンです</h1>"));
        }
        else
        {
            // 相手ターン時のメッセージを表示
            ret.append(Utility.appendLineIndent(indentCount, "<h1>※対戦相手のターンです</h1>"));
        }

        return ret.toString();
    }

    public static String makeGamePanelHtml(Match match, String key, int indent)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = indent;

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"panelWapper\"><!--パネル選択する領域-->"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"tableOuter\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<table>"));
        ret.append(Utility.appendLineIndent(indentCount++, "<tr>"));

        ArrayList<Panel> panelList = match.getPanelList();
        for (int i = 0; i < panelList.size(); i++)
        {
            // ①現在自分のターンである
            // ②ゲームが終了していない
            // ③ゲームマッチが継続可能な状態である
            // ④対象のパネルは未選択である
            // 以上の条件を満たす場合のみ、パネルのリンクを有効にする

            Panel panel = panelList.get(i);
            String cssClass = "";
            String panelHtml = "";

            boolean isCanSelect = match.isHisTurn(key) && !match.isFinish() && match.isEnableContinue() && !panel.isUsed();
            boolean isCorrectSelect = !match.isFirstPick() && panel.isMatchWord(match.getNowWord(), match.getSelectedWordList()) >= 0;
            boolean isUsed = !match.isFirstPick() && panel.isUsed();

            // CSS用のクラス名の定義
            if (isCanSelect)
            {
                cssClass += "canChoose ";
                if (isCorrectSelect)
                {
                    cssClass += "correct ";
                }
            }
            else if (isUsed)
            {
                cssClass += "selected ";
            }
            else
            {
                cssClass += "cannotChoose ";
            }

            String panelImage = "<img src=\"img/panel/" + panel.getPicture() + "\" alt=\"" + panel.getBaseWord() + " \"class=\"" + cssClass + "\">";

            if (isCanSelect)
            {
                // 選択可能であれば<a>タグで囲う
                panelHtml = "<a href=\"game?selectedPanel=" + i + "\">" + panelImage + "</a>";
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
        ret.append(Utility.appendLineIndent(--indentCount, "</div><!--tableOuterここまで-->"));

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"message\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<p>"));
        // <!--プレイ中のメッセージを表示-->プレイヤー2が選んだパネルは「柔道」（しゅうとう）です。<br>
        // プレイヤー1が使える文字は「し」「う」です。
        Word nowWord = match.getNowWord();
        if (nowWord != null)
        {
            String comment = "<!--プレイ中のメッセージを表示-->";
            String message1 = "対戦相手が選択したパネルは「" + nowWord.getWord() + "」です。";
            String message2 = "使える文字は「" + nowWord.getWordHead() + "」「" + nowWord.getWordTail() + "」です。";
            ret.append(Utility.appendLineIndent(indentCount, comment + message1 + "<br>" + message2));
        }
        ret.append(Utility.appendLineIndent(--indentCount, "</p>"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div><!--messageここまで-->"));
        ret.append(Utility.appendLineIndent(--indentCount, "</div><!--panelWapperここまで-->"));

        return ret.toString();
    }

    public static String makeUserSelectedHtml(Match match, String key, int whichPlayer, int indent)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = indent;

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
            cssClsPlayer = "player_1";
            cssClsPlayerName = "playerName_1";
            cssClsPlayerScore = "score_1";
        }
        else
        {
            col = 2;
            cssClsPlayer = "player_2-" + whichPlayer;
            cssClsPlayerName = "playerName_2";
            cssClsPlayerScore = "score_2";
        }

        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"player " + cssClsPlayer + "\">"));
        ret.append(Utility.appendLineIndent(indentCount++, "<div class=\"playerTop\">"));
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
            if (userTarget == userMe)
            {
                ret.append(Utility.appendLineIndent(indentCount++, "<a href=\"game\" class=\"reload\">更新</a><!--相手プレイヤーの領域には非表示-->"));
                ret.append(Utility.appendLineIndent(--indentCount, "</a><!--相手プレイヤーの領域には非表示-->"));
            }

            if (match.isHisTurn(userTarget.getKey()))
            {
                ret.append(Utility.appendLineIndent(indentCount, "<div id=\"timer\"></div>"));
                ret.append(Utility.appendLineIndent(indentCount, "<div class=\"nowChoosing\"><div>"));
                ret.append(Utility.appendLineIndent(indentCount++, "<span>"));
                ret.append(Utility.appendLineIndent(indentCount, "パネルを選んでいます"));
                ret.append(Utility.appendLineIndent(--indentCount, "</span>"));
                ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
                ret.append(Utility.appendLineIndent(--indentCount, "</div>"));
            }
            else
            {
                ret.append(Utility.appendLineIndent(indentCount, "<div class=\"notChoosing\"></div>"));
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
            ret.append(Utility.appendLineIndent(indentCount, "<img src=\"img/panel/" + panel.getPicture() + "\">"));
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

    public static String makeUserSelectedHtmlOld(Match match, String key)
    {
        StringBuilder ret = new StringBuilder();

        int indentCount = 0;

        // 選択指示の表示
        if (match.isFirstPick())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<h3>最初に使用するパネルを選択してください</h3>"));
        }
        else
        {
            String wordRead = match.getNowWord().getWord();
            String wordHead = "「" + match.getNowWord().getWordHead() + "」";
            String wordTail = "「" + match.getNowWord().getWordTail() + "」";

            ret.append(Utility.appendLineIndent(indentCount, "<h3>" + wordHead + wordRead + wordTail + "</h3>"));
        }

        ret.append(Utility.appendLineIndent(indentCount, "<!-- ユーザ別に選択された単語の一覧の表示 -->"));
        ret.append(Utility.appendLineIndent(indentCount, "<h2>選択した単語</h2>"));

        for (User user : match.getUserList())
        {
            ret.append(Utility.appendLineIndent(indentCount, "<h3>" + user.getName() + "</h3>"));
            ret.append(Utility.appendLineIndent(indentCount, "<h4>" + user.getScore() + "</h4>"));
            ret.append(Utility.appendLineIndent(indentCount, "<ul>"));
            indentCount++;

            for (int j = 0; j < user.getSelectedPanel().size(); j++)
            {
                Panel p = user.getSelectedPanel().get(j);
                Word word = p.getSelectedWord();

                String scoreText = p.getBaseWord() + "(" + word.getWord() + ")[" + word.getBaseScore() + "+" + word.getBonusScore() + "]";

                ret.append(Utility.appendLineIndent(indentCount, "<li>" + scoreText));
            }

            indentCount--;
            ret.append(Utility.appendLineIndent(indentCount, "</ul>"));
        }

        return ret.toString();
    }
}
