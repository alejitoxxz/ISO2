package co.edu.uco.ucochallenge.primary.controller.dev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.crosscutting.dto.MessageDTO;
import co.edu.uco.ucochallenge.crosscutting.dto.ParameterDTO;
import co.edu.uco.ucochallenge.secondary.ports.restclient.MessageServicePort;
import co.edu.uco.ucochallenge.secondary.ports.restclient.ParameterServicePort;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class CrosscheckController {

    private final ParameterServicePort parameterServicePort;
    private final MessageServicePort messageServicePort;

    public CrosscheckController(ParameterServicePort parameterServicePort,
            MessageServicePort messageServicePort) {
        this.parameterServicePort = parameterServicePort;
        this.messageServicePort = messageServicePort;
    }

    @GetMapping("/parameters/{key}")
    public Mono<ParameterDTO> getParameter(@PathVariable String key) {
        return parameterServicePort.getParameter(key);
    }

    @GetMapping("/messages/{code}")
    public Mono<MessageDTO> getMessage(@PathVariable String code) {
        return messageServicePort.getMessage(code);
    }
}
