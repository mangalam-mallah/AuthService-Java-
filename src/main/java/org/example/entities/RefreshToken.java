    package org.example.entities;
    import com.fasterxml.jackson.databind.PropertyNamingStrategies;
    import com.fasterxml.jackson.databind.annotation.JsonNaming;
    import javax.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.time.Instant;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Table(name = "tokens")
    public class RefreshToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String  token;
        private Instant expiryDate;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private UserInfo userInfo;
    }
