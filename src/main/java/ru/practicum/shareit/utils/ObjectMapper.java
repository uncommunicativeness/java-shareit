package ru.practicum.shareit.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class ObjectMapper {
    final ModelMapper modelMapper;

    public ObjectMapper() {
        this.modelMapper = new ModelMapper();
    }

    public UserDto userToUserDtoMapper(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User userDtoToUserMapper(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public ItemDto itemToItemDtoMapper(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    public Item itemDtoTOItemMapper(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }
}
