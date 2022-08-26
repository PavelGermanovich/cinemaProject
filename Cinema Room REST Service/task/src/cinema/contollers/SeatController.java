package cinema.contollers;

import cinema.model.Cinema;
import cinema.model.Seat;
import cinema.model.Ticket;
import cinema.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeatController {
    private BookingService bookingService;

    @Autowired
    public SeatController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/seats")
    public Cinema getSeats() {
        return bookingService.getAvailableSeatsInfo();
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseSeat(@RequestBody Seat seat) {
        return bookingService.purchaseTicket(seat);
    }

    @PostMapping("/return")
    public ResponseEntity purchaseSeat(@RequestBody Ticket ticket) {
        return bookingService.returnTicket(ticket.getToken());
    }

    @PostMapping("/stats")
    public ResponseEntity getStat(@RequestParam(required = false) String password) {
        return bookingService.getStat(password);
    }
}