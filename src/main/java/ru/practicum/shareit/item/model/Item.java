package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "items")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", length = 10_000, nullable = false)
    String description;

    @Column(name = "is_available", nullable = false)
    Boolean available;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
