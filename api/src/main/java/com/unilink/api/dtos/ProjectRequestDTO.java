package com.unilink.api.dtos;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisições de projeto")
public record ProjectRequestDTO(
    @Schema(description = "Nome do projeto", example = "Sistema de Gestão Escolar")
    String name,
    @Schema(description = "Descrição detalhada do projeto", example = "Sistema completo para gerenciamento de escolas")
    String description,
    @Schema(description = "ID do centro responsável pelo projeto")
    UUID centerId,
    @Schema(description = "ID do usuário proprietário do projeto")
    UUID ownerId,
    @Schema(description = "Indica se o projeto está aberto para aplicações", example = "true")
    boolean openForApplications,
    @Schema(description = "URL da imagem do projeto", example = "https://example.com/project-image.jpg")
    String imgUrl,
    @Schema(description = "Tamanho da equipe do projeto", example = "5")
    int teamSize,
    @Schema(description = "Array de IDs das tags a serem adicionadas ao projeto")
    UUID[] tagsToBeAdded,
    @Schema(description = "Array de IDs das tags a serem removidas do projeto")
    UUID[] tagsToBeRemoved,
    @Schema(description = "Dados da imagem em Base64 (opcional)")
    String imageBase64,
    @Schema(description = "Tipo de conteúdo da imagem (opcional, ex: image/jpeg)")
    String imageContentType
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               description != null && !description.isBlank() &&
               ownerId != null;
    }
    
    public boolean hasImageData() {
        return imageBase64 != null && !imageBase64.isBlank();
    }
}
