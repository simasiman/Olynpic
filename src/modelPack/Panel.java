package modelPack;

import java.util.ArrayList;

/**
 * ゲーム中に使用されるパネル一枚を管理するクラスです
 */
public class Panel
{
    /**
     * パネルID (tbl_word_base の ath_id に対応)
     */
    private int id;
    /**
     * 基本名
     */
    private String baseWord;
    /**
     * 既存登録のオリンピックに関連するパネルかどうか
     */
    private boolean isOriginal;
    /**
     * パネルで使用する画像名
     */
    private String picture;
    /**
     * パネルに登録されている単語一覧
     */
    private ArrayList<Word> wordList;
    /**
     * パネル選択時に使用された単語
     */
    private Word selectedWord;
    /**
     * パネルを選択したユーザのキー
     */
    private String selectedUserId;
    /**
     * 既に選択されたパネルかどうか
     */
    private boolean isUsed = false;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getBaseWord()
    {
        return baseWord;
    }

    public void setBaseWord(String baseWord)
    {
        this.baseWord = baseWord;
    }

    public boolean isOriginal()
    {
        return isOriginal;
    }

    public void setOriginal(boolean isOriginal)
    {
        this.isOriginal = isOriginal;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public ArrayList<Word> getWordList()
    {
        return wordList;
    }

    public void addWordList(Word w)
    {
        if (wordList == null)
        {
            wordList = new ArrayList<Word>();
        }

        wordList.add(w);
    }

    public boolean isUsed()
    {
        return isUsed;
    }

    public void setUsed(boolean isUsed)
    {
        this.isUsed = isUsed;
    }

    public Word getSelectedWord()
    {
        return selectedWord;
    }

    public void setSelectedWord(Word selectedWord)
    {
        this.selectedWord = selectedWord;
    }

    public String getSelectedUserId()
    {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId)
    {
        this.selectedUserId = selectedUserId;
    }

    /**
     * 対象ワードが、このパネルが持つ単語にヒットするかどうかを判定します
     * 
     * @param word
     *            対象ワード
     * @param selectedWordList
     *            選択済みの単語リスト
     * @return ヒット:ヒットしたワードの索引番号 ヒットしない:-1
     */
    public int isMatchWord(Word word, ArrayList<Word> selectedWordList)
    {
        for (int i = 0; i < wordList.size(); i++)
        {
            Word w = wordList.get(i);
            if (isWordHit(word, w) && !isAlreadyUserdWord(w, selectedWordList))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * 対象ワードが、既に選択されたワードと接触するかを判定します
     * 
     * @param w
     *            対象ワード
     * @param selectedWordList
     *            選択済み単語リスト
     * @return 接触する:true 接触しない:false
     */
    public boolean isAlreadyUserdWord(Word w, ArrayList<Word> selectedWordList)
    {
        // 既に使用された単語でないかを確認
        for (Word word : selectedWordList)
        {
            if (w.getWord().equals(word.getWord()) || w.getWordRead().equals(word.getWordRead()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * ふたつの単語がルール上成立する組み合わせかを判定します
     * 
     * @param wordA
     *            対象ワード１
     * @param wordB
     *            対象ワード２
     * @return 可能:TRUE 不可能:FALSE
     */
    private boolean isWordHit(Word wordA, Word wordB)
    {
        String tH = wordA.getWordHead();
        String tT = wordA.getWordTail();

        String wH = wordB.getWordHead();
        String wT = wordB.getWordTail();

        // しりとりモード等で、単語同士の成否のルールを変更する必要性がでた場合、以下の条件のみ変更すればOK
        return tH.equals(wH) || tH.equals(wT) || tT.equals(wH) || tT.equals(wT);
    }

}
