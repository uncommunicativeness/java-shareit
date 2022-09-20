package ru.practicum.shareit.request.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "item_requests")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "item_requests_seq")
    Long id;

    @Column(name = "description", length = 10_000)
    String description;

    @Column(name = "created")
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requestor;

    @OneToMany(mappedBy = "request",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Set<Item> items;

    @PrePersist
    void onCreate() {
        this.created = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemRequest that = (ItemRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
