package org.example;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Server {

  public record OptimizeBookingsRequest(
      int freePremiumRooms, int freeEconomyRooms, List<Double> clientOffers) {}
  ;

  public static void main(String[] args) {
    SpringApplication.run(Server.class, args);
  }

  @PostMapping(
      path = "/optimize-bookings",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookingOptimizer.OptimizationResult> hello(
      @RequestBody OptimizeBookingsRequest request) {
    var clientOffers =
        request.clientOffers.stream()
            .map(BookingOptimizer.EuroAmount::ofDouble)
            .collect(Collectors.toList());
    var result =
        BookingOptimizer.optimize(request.freePremiumRooms, request.freeEconomyRooms, clientOffers);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
