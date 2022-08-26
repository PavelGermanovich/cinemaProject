package cinema.service;

import cinema.dto.StatDto;
import cinema.model.Cinema;
import cinema.model.Seat;
import cinema.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private Cinema cinema;
    private final int totalRows = 9;
    private final int totalCol = 9;

    public BookingService() {
        this.cinema = new Cinema(totalRows, totalCol);
    }

    public Cinema getAvailableSeatsInfo() {
        return cinema;
    }

    public ResponseEntity purchaseTicket(Seat seat) {
        ResponseEntity response = getSeatAvailability(seat.getRow(), seat.getColumn());
        if (response.getStatusCode().is2xxSuccessful()) {
            changeSeatPurchased(seat, true);
        }
        return response;
    }

    public ResponseEntity returnTicket(UUID token) {
        ResponseEntity response = getReturnTicket(token);
        return response;
    }

    public Optional<Ticket> getPurchasedTicket(UUID token) {
        return cinema.getPurchasedTicket().stream().filter(ticket -> ticket.getToken().equals(token)).findFirst();
    }

    public ResponseEntity getReturnTicket(UUID token) {
        ResponseEntity ticketInfo;

        Optional<Ticket> ticket = getPurchasedTicket(token);
        if (ticket.isEmpty()) {
            ticketInfo = new ResponseEntity(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
        } else {
            cinema.removeTicket(ticket.get());
            changeSeatPurchased(ticket.get().getTicket(), false);
            ObjectMapper objectMapper = new ObjectMapper();

            ticketInfo = new ResponseEntity(Map.of("returned_ticket", ticket.get().getTicket()), HttpStatus.OK);
        }
        return ticketInfo;
    }


    public void changeSeatPurchased(Seat seat, boolean purchased) {
        cinema.getAllSeats().stream().filter(s -> s.equals(seat)).findFirst().get().setPurchased(purchased);
    }

    public ResponseEntity<String> getSeatAvailability(int row, int column) {
        ResponseEntity seatInfo;

        Optional<Seat> seatOpt = cinema.getAllSeats().stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findFirst();

        if (seatOpt.isEmpty()) {
            seatInfo = new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);
        } else if (seatOpt.get().isPurchased()) {
            seatInfo = new ResponseEntity(Map.of("error", "The ticket has been already purchased!"),
                    HttpStatus.BAD_REQUEST);
        } else {
            Ticket ticket = new Ticket(seatOpt.get());
            cinema.getPurchasedTicket().add(ticket);
            seatInfo = new ResponseEntity(ticket, HttpStatus.OK);
        }

        return seatInfo;
    }

    public ResponseEntity getStat(String password) {
        if (password == null || !password.equals(StatDto.getAdminPassword())) {
            return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity(new StatDto(cinema.getPuchasedIncome(), cinema.getAvailableSeats().size(),
                    cinema.getPurchasedTicket().size()), HttpStatus.OK);
        }
    }

}
