package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MedicineResponse {
    private ResponseHeader header;
    private ResponseBody body;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }

    // 내부 클래스로 ResponseHeader 정의
    public static class ResponseHeader {
        private String resultCode;
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    // 내부 클래스로 ResponseBody 정의
    public static class ResponseBody {
        private List<MedicineDto> items;

        public List<MedicineDto> getItems() {
            return items;
        }

        public void setItems(List<MedicineDto> items) {
            this.items = items;
        }
    }
}
