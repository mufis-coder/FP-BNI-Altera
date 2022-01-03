package com.bnifp.mufis.authservice.dto.output;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOutputDetail {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String role;
    private String registerFrom;
    protected Date createdAt;
    protected Date updatedAt;
}
