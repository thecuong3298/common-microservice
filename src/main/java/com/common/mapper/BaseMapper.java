package com.common.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;

public interface BaseMapper<E, D> {

    @Named("dtoToEntityMapping")
    E dtoToEntity(D dto);

    @Named("entityToDtoMapping")
    D entityToDto(E entity);

    @IterableMapping(qualifiedByName = "dtoToEntityMapping")
    List<E> dtoToEntityList(List<D> dtoList);

    @IterableMapping(qualifiedByName = "entityToDtoMapping")
    List<D> entityToDtoList(List<E> entityList);
}

