package com.cesar.sharing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DtoConverter<D, B> implements IDtoConverter<D, B> {

    @JsonIgnore
    private ModelMapper mapper = new ModelMapper();

    public DtoConverter() {
        setup();
    }

    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void setup() {
        // Should be implemented by the child to inject some custom
        // mapper if necessary.
    }

    @Override
    public D toDTO(B entity, Class<D> clazz) {
        return this.mapper.map(entity, clazz);
    }

    @Override
    public B toBusiness(D dto, Class<B> clazz) {
        return this.mapper.map(dto, clazz);
    }

    @Override
    public B toBusiness(Class<B> clazz) {
        return toBusiness((D) this, clazz);
    }

    @Override
    public List<D> toDTOs(List<B> entities, Class<D> clazz) {
        List<D> dtos = new ArrayList<>();
        for (B entity: entities) {
            dtos.add(toDTO(entity, clazz));
        }
        return dtos;
    }
}