package com.example.demo.dtos;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskDTO {
	@Column(nullable = false)
	private String description;

	private boolean completed;
}
