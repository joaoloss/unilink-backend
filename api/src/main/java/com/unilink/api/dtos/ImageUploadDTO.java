package com.unilink.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para upload de imagem em Base64")
public record ImageUploadDTO(
    @Schema(description = "Imagem codificada em Base64", example = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ...")
    @NotBlank(message = "A imagem em Base64 é obrigatória")
    String imageBase64,
    
    @Schema(description = "Nome do arquivo (opcional)", example = "projeto-imagem.jpg")
    String fileName
) {
    public boolean isValidBase64() {
        if (imageBase64 == null || imageBase64.isBlank()) {
            return false;
        }
        
        // Verifica se é um data URL válido
        if (imageBase64.startsWith("data:")) {
            String[] parts = imageBase64.split(",", 2);
            if (parts.length != 2) {
                return false;
            }
            // Verifica se o header indica uma imagem
            String header = parts[0];
            return header.contains("image/");
        }
        
        // Se não for data URL, assume que é Base64 puro
        return true;
    }
    
    public String getContentType() {
        if (imageBase64.startsWith("data:")) {
            String[] parts = imageBase64.split(",", 2);
            if (parts.length == 2) {
                String header = parts[0];
                if (header.contains("image/jpeg")) return "image/jpeg";
                if (header.contains("image/png")) return "image/png";
                if (header.contains("image/gif")) return "image/gif";
                if (header.contains("image/webp")) return "image/webp";
            }
        }
        return "image/jpeg"; // default
    }
    
    public String getBase64Data() {
        if (imageBase64.startsWith("data:")) {
            String[] parts = imageBase64.split(",", 2);
            return parts.length == 2 ? parts[1] : imageBase64;
        }
        return imageBase64;
    }
}