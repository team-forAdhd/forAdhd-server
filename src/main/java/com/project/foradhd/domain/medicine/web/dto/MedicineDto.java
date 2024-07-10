package com.project.foradhd.domain.medicine.web.dto;

import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDto {
    @SerializedName("ITEM_SEQ") private String itemSeq;
    @SerializedName("ITEM_NAME") private String itemName;
    @SerializedName("ENTP_SEQ") private String entpSeq;
    @SerializedName("ENTP_NAME") private String entpName;
    @SerializedName("CHART") private String chart;
    @SerializedName("ITEM_IMAGE") private String itemImage;
    @SerializedName("DRUG_SHAPE") private String drugShape;
    @SerializedName("COLOR_CLASS1") private String colorClass1;
    @SerializedName("COLOR_CLASS2") private String colorClass2;
    @SerializedName("CLASS_NO") private String classNo;
    @SerializedName("CLASS_NAME") private String className;
    @SerializedName("FORM_CODE_NAME") private String formCodeName;
    @SerializedName("ITEM_ENG_NAME") private String itemEngName;
    @SerializedName("RATING") private double rating;
    @SerializedName("IS_FAVORITE") private boolean isFavorite;
}
