package co.edu.uco.ucochallenge.primary.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.idtype.application.dto.IdTypeDTO;
import co.edu.uco.ucochallenge.idtype.application.interactor.usecase.IdTypeQueryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/uco-challenge/api/v1/id-types")
@RequiredArgsConstructor
public class IdTypeController {

        private final IdTypeQueryService idTypeQueryService;

        @GetMapping
        public ResponseEntity<List<IdTypeDTO>> getAll() {
                final var idTypes = idTypeQueryService.findAll();
                return ResponseEntity.ok(idTypes);
        }
}
