package com.project.foradhd.domain.auth.web.mapper;

import com.project.foradhd.domain.auth.business.dto.out.AuthTokenData;
import com.project.foradhd.domain.auth.web.dto.response.TokenReissueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AuthMapper {

    TokenReissueResponse toTokenReissueResponse(AuthTokenData authTokenData);
}
