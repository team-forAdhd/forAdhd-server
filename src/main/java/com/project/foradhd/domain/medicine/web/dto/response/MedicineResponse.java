package com.project.foradhd.domain.medicine.web.dto.response;

import com.project.foradhd.domain.medicine.web.dto.MedicineDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MedicineResponse {
    private Header header;
    private Body body;

    public static class Header {
        private String resultCode;
        private String resultMsg;

        // Getters and setters
        public String getResultCode() { return resultCode; }
        public void setResultCode(String resultCode) { this.resultCode = resultCode; }
        public String getResultMsg() { return resultMsg; }
        public void setResultMsg(String resultMsg) { this.resultMsg = resultMsg; }
    }

    public static class Body {
        private int pageNo;
        private int totalCount;
        private int numOfRows;
        private List<MedicineDto> items;

        // Getters and setters
        public int getPageNo() { return pageNo; }
        public void setPageNo(int pageNo) { this.pageNo = pageNo; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public int getNumOfRows() { return numOfRows; }
        public void setNumOfRows(int numOfRows) { this.numOfRows = numOfRows; }
        public List<MedicineDto> getItems() { return items; }
        public void setItems(List<MedicineDto> items) { this.items = items; }
    }

    // Getters and setters for header and body
    public Header getHeader() { return header; }
    public void setHeader(Header header) { this.header = header; }
    public Body getBody() { return body; }
    public void setBody(Body body) { this.body = body; }
}
