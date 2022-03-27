package br.com.monktec.resource;

import br.com.monktec.PratoResponse;
import br.com.monktec.dto.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurantes")
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;

    @Inject
    PratoMapper pratoMapper;

    @GET
    public List<RestauranteDTO> listAll() {
        Stream<Restaurante> restaurantes = Restaurante.streamAll();
        return restaurantes.map(restaurante -> restauranteMapper.toRestauranteDTO(restaurante))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public RestauranteDTO getRestaurante(@PathParam("id") Long id) {
        var restaurante = Restaurante.findById(id);

        if (restaurante == null) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        return restauranteMapper.toRestauranteDTO((Restaurante) restaurante);
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
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        var restaurante = restauranteOptional.get();
        restauranteMapper.toRestaurante(restauranteDTO, restaurante);

        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        restauranteOptional.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "pratos")
    public List<Restaurante> listaTodosPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        return Prato.list("restaurante", restauranteOptional.get());
    }

    @GET
    @Path("pratos/{idRestaurante}")
    @Tag(name = "pratos")
    public List<PratoResponse> pratos(@PathParam("idRestaurante") Long idRestaurante) {
        var pratos = Prato.findByRestauranteId(idRestaurante);

        var pratosResponses = new ArrayList<PratoResponse>();

        for (Prato prato : pratos) {
            var p = new PratoResponse();
            p.setPrato(prato.getNome());
            p.setPreco(prato.getPreco());
            p.setRestaurante(prato.getRestaurante().getNome());
            pratosResponses.add(p);
        }
        return pratosResponses;
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "pratos")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, AdicionaPratoDTO pratoDTO) {
        Optional<Restaurante> restauranteOptional =Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

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
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

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
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException("Prato não encontrado");
        });
    }
}
