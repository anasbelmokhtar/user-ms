package org.ac.cst8277.belmokhtar.anas.model;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    String roleName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
