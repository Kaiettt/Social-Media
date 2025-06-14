package com.socialmedia.socialmedia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Resource {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String type;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
