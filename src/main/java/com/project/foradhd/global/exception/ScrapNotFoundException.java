package com.project.foradhd.global.exception;

public class ScrapNotFoundException extends PostScrapException {
    public ScrapNotFoundException(String scrapId) {
        super("해당 스크랩을 찾을 수 없습니다. scrapId: " + scrapId);
    }
}
