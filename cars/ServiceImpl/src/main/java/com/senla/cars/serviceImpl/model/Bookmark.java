package com.senla.cars.serviceImpl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmarks")
@IdClass(BookmarkKey.class)
public class Bookmark {
    @Id
    @Column(name = "ad_id")
    private Integer adId;
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "ad_id",insertable = false, updatable = false)
    private Ad ad;
    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false, updatable = false)
    private User user;

}
