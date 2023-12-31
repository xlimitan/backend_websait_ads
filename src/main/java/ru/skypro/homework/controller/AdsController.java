package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

import javax.servlet.UnavailableException;
import javax.validation.Valid;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Объявления", description = "контроллер для работы с объявлениями")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    public AdsController(AdService adService) {
        this.adService = adService;
    }

    @Operation(
            summary = "Получение всех объявлений",
            responses = {@ApiResponse(responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Ads.class)))})
    @GetMapping
    public ResponseEntity<Ads> getAllAds(){
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(
            summary = "Добавление объявления",
            responses = {@ApiResponse(responseCode = "201",
                            description = "Created",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = AdDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = " Unauthorized",
                            content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestParam MultipartFile image,
                                       @RequestPart @Valid  CreateOrUpdateAd properties,
                                       Authentication authentication){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return ResponseEntity.ok(adService.addAd(properties, image, authentication, userName));
    }

    @Operation(
            summary = "Получение информации об объявлении",
            responses = {@ApiResponse(responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ExtendedAd.class))),
                    @ApiResponse(responseCode = "401",
                            description = " Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable int id){
        return new ResponseEntity<>(adService.getAd(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление объявления",
            responses = {@ApiResponse(responseCode = "204",
                            description = "No Content",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401",
                            description = " Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping("/{id}")
    public ResponseEntity<AdDto> deleteAds(@PathVariable("id") int adId,
                                           Authentication authentication)  throws UnavailableException {
        adService.removeAd(adId, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление информации об объявлении",
            responses = {@ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = AdDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = " Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true)))})
    @PatchMapping("/{id}")
    public ResponseEntity<CreateOrUpdateAd> updateAd (@PathVariable("id") int adId,
                                           @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                           Authentication authentication) throws UnavailableException {
        return ResponseEntity.ok(adService.updateAd(createOrUpdateAd, authentication, adId ));
    }

    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adService.getAdsMe(authentication));
    }

    @Operation(
            summary = "Обновление картинки объявления",
            responses = {@ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(hidden = true)))})
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAdsImage(@RequestParam MultipartFile image,
                                              @PathVariable int id,
                                              Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        adService.uploadImage(id, authentication, image, userName );
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/get/{filename}", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/*"})
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        return ResponseEntity.ok().body(adService.getAdImage(filename));
    }
}