package com.dripsoda.community.vos.qna;

import com.dripsoda.community.entities.qna.QnaArticleEntity;

public class QnaArticleReadVo extends QnaArticleEntity{


    private int prev;
    private int next;

    private String prevTitle;

    private String nextTitle;

    public int getPrev() {
        return prev;
    }

    public QnaArticleReadVo setPrev(int prev) {
        this.prev = prev;
        return this;
    }

    public int getNext() {
        return next;
    }

    public QnaArticleReadVo setNext(int next) {
        this.next = next;
        return this;
    }

    public String getPrevTitle() {
        return prevTitle;
    }

    public QnaArticleReadVo setPrevTitle(String prevTitle) {
        this.prevTitle = prevTitle;
        return this;
    }

    public String getNextTitle() {
        return nextTitle;
    }

    public QnaArticleReadVo setNextTitle(String nextTitle) {
        this.nextTitle = nextTitle;
        return this;
    }
}
