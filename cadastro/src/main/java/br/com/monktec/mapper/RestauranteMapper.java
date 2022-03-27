package br.com.monktec.mapper;

import br.com.monktec.dto.AdicionaRestauranteDTO;
import br.com.monktec.dto.AtualizaNomeRestauranteDTO;
import br.com.monktec.dto.RestauranteDTO;
import br.com.monktec.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    Restaurante toRestaurante(AdicionaRestauranteDTO dto);

    void toRestaurante(AtualizaNomeRestauranteDTO dto, @MappingTarget Restaurante restaurante);

    @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    @Mapping(target = "dataAtualizacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
    @Mapping(target = "id", source = "id")
    RestauranteDTO toRestauranteDTO(Restaurante restaurante);
}
