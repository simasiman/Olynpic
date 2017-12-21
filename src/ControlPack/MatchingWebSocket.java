package ControlPack;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import modelPack.MatchUserList;

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
        ses.add(session);
    }

    @OnMessage
    public void onMessage(String text, Session session)
    {
        if (session == null || !session.isOpen())
        {
            return;
        }

        // 初回接続時の送信情報であれば、ユーザのセッション情報をセット
        MatchUserList.setUserSession(text, session);
    }

    @OnClose
    public void onClose(Session session)
    {
        MatchUserList.remove(session);
        ses.remove(session);
    }

}
