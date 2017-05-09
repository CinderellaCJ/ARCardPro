package com.cj.arcard.bean;

/**
 * Created by chenjing on 2017/4/15.
 */

public class CardInfo {
    private String cardId;
    private String cardName;
    private String cardDescribe;
    private String cardPictureUrl;
    private String cardVideoUrl;
    private String cardReceiverName;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDescribe() {
        return cardDescribe;
    }

    public void setCardDescribe(String cardDescribe) {
        this.cardDescribe = cardDescribe;
    }

    public String getCardPictureUrl() {
        return cardPictureUrl;
    }

    public void setCardPictureUrl(String cardPictureUrl) {
        this.cardPictureUrl = cardPictureUrl;
    }

    public String getCardVideoUrl() {
        return cardVideoUrl;
    }

    public void setCardVideoUrl(String cardVideoUrl) {
        this.cardVideoUrl = cardVideoUrl;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardReceiverName() {
        return cardReceiverName;
    }

    public void setCardReceiverName(String cardReceiverName) {
        this.cardReceiverName = cardReceiverName;
    }
}
