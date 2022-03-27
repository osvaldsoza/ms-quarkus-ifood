package br.com.monktec.mapper;

import br.com.monktec.dto.AdicionaPratoDTO;
import br.com.monktec.dto.AtualizaPrecoPratoDTO;
import br.com.monktec.entity.Prato;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PratoMapper {

    Prato toPrato(AdicionaPratoDTO dto);

    Prato toPratoPreco(AtualizaPrecoPratoDTO dto);
}
