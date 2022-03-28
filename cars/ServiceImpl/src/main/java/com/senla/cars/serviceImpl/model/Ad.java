package com.senla.cars.serviceImpl.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.cars.api.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ads")
public class Ad implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "seller_name")
    private String sellerName;
    @NotNull
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
    @NotNull
    @Column(name = "year_issue")
    private Integer yearIssue;
    @NotNull
    private Integer mileage;
    @NotNull
    @Column(name = "engine_volume")
    private Double engineVolume;
    @NotNull
    @Size(min = 5,max = 50)
    @Column(name = "engine_type")
    private String engineType;
    @NotNull
    @Size(min = 5,max = 50)
    private String transmission;
    @NotNull
    @Size(max = 50)
    private String region;
    @NotNull
    @Column(name = "customs_clearance")
    private boolean customsClearance;
    @NotNull
    private boolean exchange;
    @NotNull
    private Integer price;
    @NotNull
    @Column(name = "publication_date")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
    private LocalDateTime publicationDateTime;
    @Column(name = "deactivated")
    private boolean deactivated;

}
