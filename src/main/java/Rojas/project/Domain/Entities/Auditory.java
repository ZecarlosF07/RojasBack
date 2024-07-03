package Rojas.project.Domain.Entities;

import com.google.cloud.Timestamp;
import lombok.*;


@Getter
@Setter
@Data
public class Auditory {
    private Timestamp createdAt = Timestamp.now();
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private String deletedBy;
    private String updatedBy;
    private String createdBy;
}