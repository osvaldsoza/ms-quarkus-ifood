package br.com.monktec.mapper;

import br.com.monktec.dto.AdicionaRestauranteDTO;
import br.com.monktec.dto.AtualizaNomeRestauranteDTO;
import br.com.monktec.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {

    Restaurante toRestaurante(AdicionaRestauranteDTO dto);

    @Mapping(source = "nome", target = "nome")
    Restaurante toRestauranteNome(AtualizaNomeRestauranteDTO dto);
}
