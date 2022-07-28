package se.ifmo.korotin.springmvc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class PositionController {
    @NonNull
    private final AircraftRepository repository;

    private WebClient client
            = WebClient.create("http://localhost:7634/aircraft");

    @GetMapping("/aircraft")
    public String getCurrentAircraftPosition(Model model) {

        Flux<Aircraft> aircraftFlux = repository.deleteAll()
                        .thenMany(client.get()
                                .retrieve()
                                .bodyToFlux(Aircraft.class)
                                .filter(p -> !p.getReg().isEmpty())
                                .flatMap(repository::save));

        model.addAttribute("currentPositions", aircraftFlux);
        return "positions";
    }
}
