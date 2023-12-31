package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
/**
 * Интерфейс RegisterMapper представляющий преобразование Register
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    User toEntity(Register register, @MappingTarget User user);
    Register toDTO(User user);
}
