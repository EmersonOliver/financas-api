package br.com.emerson.core.service;

import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.app.dto.response.FaturaResponseModel;
import br.com.emerson.core.entity.FaturaEntity;

import java.util.Date;
import java.util.List;

public interface DashboardService {

    List<CartaoResponse> cartoesAll();
    List<FaturaEntity> findFaturaByData(List<Date> range);
    FaturaResponseModel loadFatura();
}
