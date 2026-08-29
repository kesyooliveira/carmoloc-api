package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.RentalOrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_order")
public class RentalOrderEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalOrderStatusEnum status = RentalOrderStatusEnum.QUOTE;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "rentalOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalOrderItemEntity> items = new ArrayList<>();

    public void addItem(RentalOrderItemEntity item) {
        item.setRentalOrder(this);
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(RentalOrderItemEntity item) {
        this.items.remove(item);
        item.setRentalOrder(null);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(RentalOrderItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public LocalDateTime getOverallStart() {
        return items.stream().map(RentalOrderItemEntity::getStartDateTime).min(LocalDateTime::compareTo).orElse(null);
    }

    public LocalDateTime getOverallEnd() {
        return items.stream().map(RentalOrderItemEntity::getEndDateTime).max(LocalDateTime::compareTo).orElse(null);
    }
}
