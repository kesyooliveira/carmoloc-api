package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.ClientDocumentTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class ClientEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private ClientDocumentTypeEnum documentType;

    @Column(nullable = false, unique = true, length = 14)
    private String document;

    @Column(nullable = false)
    private String phone;

    @Column
    private String email;

    @Embedded
    private Address address;

}
