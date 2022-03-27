package br.com.monktec.resource;

import br.com.monktec.dto.AdicionaPratoDTO;
import br.com.monktec.dto.AdicionaRestauranteDTO;
import br.com.monktec.dto.AtualizaNomeRestauranteDTO;
import br.com.monktec.dto.AtualizaPrecoPratoDTO;
import br.com.monktec.entity.Prato;
import br.com.monktec.entity.Restaurante;
import br.com.monktec.mapper.PratoMapper;
import br.com.monktec.mapper.RestauranteMapper;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurantes")
public class RestauranteResource {

    @Inject
    private RestauranteMapper restauranteMapper;

    @Inject
    private PratoMapper pratoMapper;

    @GET
    public List<Restaurante> listAll() {
        return Restaurante.listAll();
    }

    @GET
    @Path("{id}")
    public Restaurante getRestaurante(@PathParam("id") Long id) {
        return Restaurante.findById(id);
    }

    @POST
    @Transactional
    public Response adicionar(AdicionaRestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, AtualizaNomeRestauranteDTO restauranteDTO) {
        Optional<Restaurante> restauranteOptional = buscarRestaurante(id);

        var restaurante = restauranteMapper.toRestauranteNome(restauranteDTO);

        restaurante = restauranteOptional.get();
        restaurante.setNome(restauranteDTO.getNome());

        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = buscarRestaurante(id);
        Restaurante.deleteById(restauranteOptional.get().getId());
//        restauranteOptional.ifPresentOrElse(Restaurante::delete, () -> {
//            throw new NotFoundException();
//        });
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "pratos")
    public List<Restaurante> listaTodosPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = buscarRestaurante(idRestaurante);

        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "pratos")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, AdicionaPratoDTO pratoDTO) {
        Optional<Restaurante> restauranteOptional = buscarRestaurante(idRestaurante);

        Prato prato = pratoMapper.toPrato(pratoDTO);
        prato.setRestaurante(restauranteOptional.get());

        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "pratos")
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, AtualizaPrecoPratoDTO pratoDTO) {
        buscarRestaurante(idRestaurante);

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não encontrado");
        }

        var prato = pratoMapper.toPratoPreco(pratoDTO);
        prato = pratoOptional.get();
        prato.setPreco(pratoDTO.getPreco());
        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "pratos")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        buscarRestaurante(idRestaurante);

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException("Prato não encontrado");
        });
    }

    private Optional<Restaurante> buscarRestaurante(Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        return restauranteOptional;
    }
}
