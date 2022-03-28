package com.senla.cars.serviceImpl.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.cars.api.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @JoinColumn(name = "comment")
    private String comment;
    @NotNull
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    @JoinColumn(name = "date")
    private LocalDateTime date;




}
