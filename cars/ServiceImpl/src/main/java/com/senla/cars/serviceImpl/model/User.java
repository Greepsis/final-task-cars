package com.senla.cars.serviceImpl.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.cars.api.utils.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Email(message = "Invalid email!")
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String roles;
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private LocalDate blocking;
    @JsonFormat(pattern = DateUtil.DATE_PATTERN)
    private LocalDate deletion;
}
