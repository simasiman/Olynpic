package ControlPack;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import modelPack.Match;
import modelPack.MatchList;
import modelPack.User;

/**
 * URL:matchingにおけるWebSocket
 */
@ServerEndpoint(value = "/matching")
public class MatchingWebSocket
{
    private static Set<Session> ses = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println("onOpen : " + session);
        ses.add(session);
    }

    @OnMessage
    public void onMessage(String text, Session session)
    {
        String[] receive = text.split(",");
        String key = receive[0];

        Match match = MatchList.getMatch(key);

        if (receive.length == 1)
        {
            // 初回接続時の送信情報であれば、ユーザのセッション情報をセット
            User user = match.getUser(key);
            user.session = session;
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        System.out.println("onClose : " + session);
        ses.remove(session);
    }
}
