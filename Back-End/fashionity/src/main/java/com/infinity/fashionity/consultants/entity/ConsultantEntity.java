package com.infinity.fashionity.consultants.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "consultant_infos")
public class ConsultantEntity {
    @Id
    @Column(name = "consultant_info_seq")
    private Long seq;
}
