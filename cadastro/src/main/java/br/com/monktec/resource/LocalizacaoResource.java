package br.com.monktec.resource;

import br.com.monktec.entity.Localizacao;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("localizacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocalizacaoResource {

    @GET
    public List<Localizacao> listaTodos() {
        return Localizacao.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Localizacao localizacaoDTO) {
        localizacaoDTO.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Localizacao localizacaoDTO) {
        Optional<Localizacao> localizacaoOP = localizacaoDTO.findByIdOptional(id);

        if (localizacaoOP.isEmpty()) {
            throw new NotFoundException();
        }

        Localizacao localizacao = localizacaoOP.get();
        localizacao.latitude = localizacaoDTO.latitude;
        localizacao.longitude = localizacaoDTO.longitude;

        localizacao.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Optional<Localizacao> localizacaoOP = Localizacao.findByIdOptional(id);

        localizacaoOP.ifPresentOrElse(Localizacao::delete, () -> {
            throw new NotFoundException();
        });
    }
}
